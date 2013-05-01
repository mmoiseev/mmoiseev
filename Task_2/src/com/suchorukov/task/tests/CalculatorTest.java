package com.suchorukov.task.tests;

import com.suchorukov.task.main.Calculator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;


public class CalculatorTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testCalculate() throws Exception {
        /*String data = "push 5\r\n" + "print\r\n" + "stop\r\n";
        InputStream stdin = System.in;
        try {
            System.setIn(new ByteArrayInputStream(data.getBytes()));
            Scanner scanner = new Scanner(System.in);

            //System.out.println(scanner.nextLine());
        } finally {
            System.setIn(stdin);
        } */

        OutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        Calculator calculator = new Calculator("c:\\DiscD\\testCommand2.txt");


        assertTrue( "Тест провален", Double.valueOf(outputStream.toString()) == -0.5);

    }
}
