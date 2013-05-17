package com.suchorukov.task.main;

import com.suchorukov.task.command.CommandsFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class Calculator {
    Scanner in = null;

    public Calculator() throws FileNotFoundException, InvalidCommandException {
        try {
            in = new Scanner(System.in);
            calculate(in);
        } finally {
            if (in != null)
                in.close();
        }
    }

    public Calculator(String fileName) throws FileNotFoundException, InvalidCommandException {
        try {
            in = new Scanner(getInputStream(fileName));
            calculate(in);
        } finally {
            if (in != null)
                in.close();
        }
    }

    public Calculator(Scanner scanner) throws FileNotFoundException, InvalidCommandException {
        try{
            calculate(scanner);
        } finally {
            if (scanner != null)
                scanner.close();
        }

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
        CommandsFactory commands = CommandsFactory.getInstance();
        while (scanner.hasNextLine()) {
            String inputCommand = scanner.nextLine();
            inputCommand = inputCommand.toLowerCase();
            String[] inputStr = inputCommand.split("\\s");
            if (inputStr.length > 0 && !"".equals(inputStr[0])) {
                if (inputCommand.equalsIgnoreCase("stop")) break;
                if (inputCommand.startsWith("#")) continue;
                try {
                    commands.callCommand(inputStr);
                } catch (InvalidCommandException e) {
                    System.out.println("Ошибка во время выполнения команды");
                } catch (Exception e) {
                    System.out.println("Не верный формат команды");
                }
            } else {
                System.out.println("Введите команду");
            }
        }
    }
}
