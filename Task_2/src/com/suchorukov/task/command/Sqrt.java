package com.suchorukov.task.command;

import com.suchorukov.task.main.CalcResource;
import com.suchorukov.task.main.Command;
import com.suchorukov.task.main.InvalidCommandException;
import com.suchorukov.task.main.ResType;

import java.util.Stack;


public class Sqrt implements Command {
    @CalcResource(type = ResType.STACK)
    Stack<Double> stack;

    public void execute(String[] args) throws InvalidCommandException {
        try {
            double val = Math.sqrt(stack.pop());
            stack.push(val);
        } catch (Exception e) {
            throw new InvalidCommandException();
        }
    };
}
