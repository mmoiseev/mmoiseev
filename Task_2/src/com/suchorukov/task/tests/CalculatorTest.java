package com.suchorukov.task.tests;

import com.suchorukov.task.main.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;


public class CalculatorTest {

    private Stack<Double> testStack = new Stack<Double>();
    private Map<String, Double> testVarMap = new HashMap<String, Double>();

    @Before
    public void setUp() throws Exception {
        testStack.empty();
        testStack.push(5.0);
        testStack.push(4.0);

        testVarMap.clear();
        testVarMap.put("a", 3.0);

    }

    private Command getCommand(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class command = Class.forName(className);
        Command commandInst = (Command) command.newInstance();
        for (Field field : command.getDeclaredFields()) {
            CalcResource calcRes = field.getAnnotation(CalcResource.class);
            if (calcRes != null) {
                field.setAccessible(true);
                if (ResType.STACK.equals(calcRes.type())) {
                    field.set(commandInst, testStack);
                } else {
                    field.set(commandInst, testVarMap);
                }
                field.setAccessible(false);
            }
        }
        return commandInst;
    }

    @Test
    public void testPush() throws Exception {
        Command command = getCommand("com.suchorukov.task.command.Push");
        command.execute("push 19".split(" "));
        assertTrue(testStack.peek() == 19.0);
    }

    @Test
    public void testAdd() throws Exception {
        Command command = getCommand("com.suchorukov.task.command.Add");
        command.execute(new String[0]);
        assertEquals(9.0, (Object) testStack.peek());
    }

    @Test(expected = InvalidCommandException.class)
    public void testEx() throws Exception {
        Command command = getCommand("com.suchorukov.task.command.Define");
        command.execute(new String[0]);
    }

    @Test
    public void testCalculate() throws Exception {
        String data = "define a 2\r\n" +
                "define b 5\r\n" +
                "define c 2\r\n" +
                "push 2\r\n" +
                "push a\r\n" +
                "mul\r\n" +
                "push 4\r\n" +
                "push a\r\n" +
                "push c\r\n" +
                "mul\r\n" +
                "mul\r\n" +
                "push b\r\n" +
                "push b\r\n" +
                "mul\r\n" +
                "sub\r\n" +
                "sqrt\r\n" +
                "push b\r\n" +
                "push -1\r\n" +
                "mul\r\n" +
                "add\r\n" +
                "div\r\n" +
                "print\r\n";

        InputStream stdin = System.in;
        Scanner scanner;
        System.setIn(new ByteArrayInputStream(data.getBytes()));
        scanner = new Scanner(System.in);

        OutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        Calculator calculator = new Calculator(scanner);
        System.setIn(stdin);
        assertTrue("Тест провален", Double.valueOf(outputStream.toString()) == -0.5);

    }
}
