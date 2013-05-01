package com.suchorukov.task.main;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CalcResource{
    ResType type();
}

