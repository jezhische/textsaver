package com.jezh.textsaver.trainingTests;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TrainingTest {

    @Test
    public void forloopOfZeroLength() {
        int[] i = new int[0];
        for (int i1 : i) {
            System.out.println("i");
        }
        List<Integer> ilist = new ArrayList<>();
        ilist.forEach(System.out::println);
    }

    @Test
    public void arrayCasting() {
        Integer[] i = {4, 5, 6};
        Number[] n/* = new Number[10]*/;
        n = i;
        for (Number number : n) {
            System.out.println(number);
        }
    }

    @Test
    public void substringTest() {
        String s = "sub";
        System.out.println(s.substring(0, s.length() - 1));
        System.out.println(s.substring(s.length() - 1));
    }
}
