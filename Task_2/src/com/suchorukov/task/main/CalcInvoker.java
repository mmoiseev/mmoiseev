package com.suchorukov.task.main;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class CalcInvoker implements InvocationHandler {
    private Command command;
    private Logger logger;
    private Stack stack;
    private Map varMap;

    public CalcInvoker(Command command, Stack stack, Map varMap) {
        this.command = command;
        this.stack = stack;
        this.varMap = varMap;
        this.logger = Logger.getLogger(command.getClass());
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        logger.debug("Stack before " + stack);
        logger.debug("Context " + varMap);
        logger.debug("Arguments " + Arrays.toString((String[])args[0]));
        command.execute((String[]) args[0]);
        logger.debug("Stack after " + stack);
        logger.debug("-----------------------------------------------------------------");
        return null;
    }
}
