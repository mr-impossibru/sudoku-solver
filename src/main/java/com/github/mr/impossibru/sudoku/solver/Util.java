package com.github.mr.impossibru.sudoku.solver;

import java.util.Arrays;
import java.util.List;

public class Util {

    public static void printSudokuBoard(List<Integer[][]> solutions) {
        System.out.println("Total solutions - " + solutions.size());
        for (int i = 0; i < solutions.size(); i++) {
            System.out.println("Solution #" +  (i + 1));
            for (Integer[] row : solutions.get(i)) {
                System.out.println(Arrays.toString(row));
            }
        }
    }
}
