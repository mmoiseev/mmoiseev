package com.suchorukov.task.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.*;

public class Calculator {
    @CalcResource(type = ResType.STACK)
    Stack<Double> stack = new Stack<Double>();

    @CalcResource(type = ResType.DEFINES)
    Map<String, Double> varMap = new HashMap<String, Double>();

    Map<String, Command> commands = new HashMap<String, Command>();

    public Calculator() throws FileNotFoundException, InvalidCommandException {
        calculate(new Scanner(System.in));

    }

    public Calculator(String fileName) throws FileNotFoundException, InvalidCommandException {
        calculate(new Scanner(getInputStream(fileName)));
    }

    public Calculator(Scanner scanner) throws FileNotFoundException, InvalidCommandException {
        calculate(scanner);
    }

    private InputStream getInputStream(String fileName) {
        InputStream stream = null;
        if (fileName.length() > 0) {
            try {
                stream = new FileInputStream(fileName);
            } catch (FileNotFoundException e) {
                System.out.println("Файл не найден, включён консольный ввод");
                stream = System.in;
            }
        } else {
            stream = System.in;
        }
        return stream;
    }

    private void calculate(Scanner scanner) throws InvalidCommandException, FileNotFoundException {
        initialize();
        while (scanner.hasNextLine()) {
            String inputCommand = scanner.nextLine();
            inputCommand = inputCommand.toLowerCase();
            String[] inputStr = inputCommand.split("\\s");
            if (inputStr.length > 0 && !"".equals(inputStr[0])) {
                if (inputCommand.equalsIgnoreCase("stop")) break;
                if (inputCommand.startsWith("#")) continue;
                try {
                    commands.get(inputStr[0]).execute(inputStr);
                } catch (InvalidCommandException e) {
                    System.out.println("Ошибка во время выполнения команды");
                } catch (Exception e) {
                    System.out.println("Не верный формат команды");
                } finally {
                    continue;  // todo finally & continue тут лишние, поэтому идея их подсветила желтым
                }
            } else {
                System.out.println("Введите команду");
            }
        }
        scanner.close();
        // todo закрывать ресурс лучше в том блоке, где он был выделен с использованием finally, на случай сбоя, чтобы не было утечки
    }


    public void initialize() {
        Properties p = new Properties();
        try {
            InputStream in = Calculator.class.getResourceAsStream("commands.properties");
            p.load(in);
            in.close();
        } catch (Exception e) {
            // todo в подобной ситуации лучше бросить наверх RuntimeException
            // todo throw new RuntimeException("Нет файла commands.properties",e)
            // todo тогда на верху будет ясно где грохнулось и какой файл нужен
            // todo всегда думайте о том, кто будет сопровождать программу в будущем
            System.out.println("Ресурс недоступен");
            System.exit(1);
        }
        for (Object key : p.keySet()) {
            Class command = null;
            try {
                command = Class.forName((String) p.get(key));
                Command commandInst = (Command) command.newInstance();
                for (Field field : command.getDeclaredFields()) {
                    CalcResource calcRes = field.getAnnotation(CalcResource.class);
                    if (calcRes != null) {
                        field.setAccessible(true);
                        if (ResType.STACK.equals(calcRes.type())) {
                            field.set(commandInst, stack);
                        } else if (ResType.DEFINES.equals(calcRes.type())) {
                            field.set(commandInst, varMap);
                        }
                        field.setAccessible(false);
                    }
                }
                Command commandProxy = (Command) Proxy.newProxyInstance(Command.class.getClassLoader(), new Class<?>[]{Command.class}, new CalcInvoker(commandInst, stack, varMap));
                commands.put(key.toString(), commandProxy);
            } catch (ClassNotFoundException e) {
                System.out.println("Не найдена команда" + p.get(key));
            } catch (IllegalAccessException e) {
                System.out.println("Нед доступа к одному из полей команды" + p.get(key));
            } catch (InstantiationException e) {
                System.out.println("Невозможно создать команду" + p.get(key));
            }
        }
    }
}
