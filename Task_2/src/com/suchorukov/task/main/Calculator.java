package com.suchorukov.task.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.*;

public class Calculator {
    Stack<Double> stack = new Stack<Double>();
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
                if (inputCommand.startsWith("#"))continue;
                try {
                    commands.get(inputStr[0]).execute(inputStr);
                } catch (InvalidCommandException e) {
                    System.out.println("Ошибка во время выполнения команды");
                } catch (Exception e) {
                    System.out.println("Не верный формат команды");
                } finally {
                    continue;
                }
            } else {
                System.out.println("Введите команду");
            }
        }
        scanner.close();
    }


    public void initialize() {
        Properties p = new Properties();
        try {
            InputStream in = Calculator.class.getResourceAsStream("commands.properties");
            p.load(in);
            in.close();
        } catch (Exception e) {
            System.out.println("Ресурс недоступен");
            System.exit(1);
        }
        for (Object key : p.keySet()) {
            Class command = null;
            try {
                command = Class.forName((String) p.get(key));
                Command commandInst = (Command) command.newInstance();
                //commandInst.execute(new String[]{"", ""});
                for (Field field : command.getDeclaredFields()) {
                    CalcResource calcRes = field.getAnnotation(CalcResource.class);
                    if (calcRes != null) {
                        if (ResType.STACK.equals(calcRes.type())) {
                            field.setAccessible(true);
                            field.set(commandInst, stack);
                            field.setAccessible(false);
                        } else if (ResType.DEFINES.equals(calcRes.type())) {
                            field.setAccessible(true);
                            field.set(commandInst, varMap);
                            field.setAccessible(false);
                        }
                    }
                }
                Command commandProxy = (Command) Proxy.newProxyInstance(Command.class.getClassLoader(),new Class<?>[] {Command.class},new CalcInvoker(commandInst));
                commands.put(key.toString(), commandProxy);
            } catch (ClassNotFoundException e) {
                System.out.println("Не найдена команда" + p.get(key));
            } catch (IllegalAccessException e) {
                System.out.println("Нед доступа к одному из полей команды" + p.get(key));
            } catch (InstantiationException e) {
                System.out.println("Невозможно создать команду" + p.get(key));
            } /* (InvalidCommandException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }   */
        }
    }
}
