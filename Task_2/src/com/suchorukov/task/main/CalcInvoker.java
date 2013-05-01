package com.suchorukov.task.main;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class CalcInvoker implements InvocationHandler {
    private Command command;
    private Logger logger;

    public CalcInvoker(Command command) {
        this.command = command;
        this.logger = Logger.getLogger(command.getClass());
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //logger.info("Вызвали метод "  + method.getName() + " объекта " + command.getClass());
        logger.debug("Вызвали метод " + method.getName() + " объекта " + command.getClass());
        System.out.println("Вызвали метод "  + method.getName() + " объекта " + command.getClass());
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
