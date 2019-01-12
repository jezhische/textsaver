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
}
