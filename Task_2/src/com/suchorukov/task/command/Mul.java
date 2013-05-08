package com.suchorukov.task.command;

import com.suchorukov.task.main.CalcResource;
import com.suchorukov.task.main.Command;
import com.suchorukov.task.main.InvalidCommandException;
import com.suchorukov.task.main.ResType;

import java.util.Map;
import java.util.Stack;

public class Mul implements Command {
    @CalcResource(type = ResType.STACK)
    private Stack<Double> stack;

    public void execute(String[] args) throws InvalidCommandException {
        try{
            double val1 = stack.pop();
            double val2 = stack.pop();
            stack.push(val1*val2);
        } catch (Exception e){
            throw new InvalidCommandException();
        }


    };
}
