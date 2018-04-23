package com.scu.turing.utils;

import java.util.function.Function;

public class ParamUtils {


    public static void requirePositiveNumber(int... num) {
        for (int n : num) {
            if (n < 0) {
                throw new IllegalArgumentException("Require Positive Number!");
            }
        }
    }
/*

    private void check(Function f,Num){
        if (f.apply())
    }*/
}
