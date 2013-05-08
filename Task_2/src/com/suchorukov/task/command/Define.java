package com.suchorukov.task.command;

import com.suchorukov.task.main.CalcResource;
import com.suchorukov.task.main.Command;
import com.suchorukov.task.main.InvalidCommandException;
import com.suchorukov.task.main.ResType;

import java.util.Map;
import java.util.Stack;

public class Define implements Command {
    @CalcResource(type = ResType.DEFINES)
    private Map<String, Double> varMap;

    public void execute(String[] args) throws InvalidCommandException {
        try {
            varMap.put(args[1], Double.valueOf(args[2]));
        } catch (Exception e) {
            throw new InvalidCommandException();
        }


    }

    ;
}
