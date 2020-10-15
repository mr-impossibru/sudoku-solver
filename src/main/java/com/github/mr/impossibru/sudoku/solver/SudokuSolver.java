package com.github.mr.impossibru.sudoku.solver;

import java.util.List;

public interface SudokuSolver {

    List<Integer[][]> solve(int[][] initialData);

}
