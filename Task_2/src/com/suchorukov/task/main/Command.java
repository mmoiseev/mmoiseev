package com.suchorukov.task.main;

import java.util.Map;
import java.util.Stack;

public interface Command {
    public void execute(String[] args) throws InvalidCommandException;
}
