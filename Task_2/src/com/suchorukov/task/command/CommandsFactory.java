package com.suchorukov.task.command;

import com.suchorukov.task.main.*;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;

public class CommandsFactory {
    private static CommandsFactory ourInstance = new CommandsFactory();
    Map<String, Command> commands = new HashMap<String, Command>();
    @CalcResource(type = ResType.STACK)
    Stack<Double> stack = new Stack<Double>();
    @CalcResource(type = ResType.DEFINES)
    Map<String, Double> varMap = new HashMap<String, Double>();

    public static CommandsFactory getInstance() {
        return ourInstance;
    }

    private CommandsFactory() {
        initialize();
    }

    public void initialize() {
        Properties p = new Properties();
        try {
            InputStream in = CommandsFactory.class.getResourceAsStream("commands.properties");
            p.load(in);
            in.close();
        } catch (Exception e) {
            throw new RuntimeException("Нет файла commands.propeties", e);
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

    public void callCommand(String[] args) throws InvalidCommandException {
        commands.get(args[0]).execute(args);
    }
}
