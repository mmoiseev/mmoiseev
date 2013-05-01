package com.suchorukov.task.command;

import com.suchorukov.task.main.CalcResource;
import com.suchorukov.task.main.Command;
import com.suchorukov.task.main.InvalidCommandException;
import com.suchorukov.task.main.ResType;

import java.util.Map;
import java.util.Stack;

public class Push implements Command {
    @CalcResource(type = ResType.STACK)
    Stack<Double> stack;
    @CalcResource(type = ResType.DEFINES)
    Map<String,Double> varMap;

    public void execute(String[] args) throws InvalidCommandException{
            try{
                double val = varMap.containsKey(args[1]) ? varMap.get(args[1]): Double.valueOf(args[1].trim());
                stack.push(val);
            } catch (Exception e){
                throw new InvalidCommandException(e);
            }

    };
}
