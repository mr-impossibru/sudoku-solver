package com.github.mr.impossibru;

import com.github.mr.impossibru.sudoku.solver.SudokuSolver;
import com.github.mr.impossibru.sudoku.solver.dancing.links.DancingLinksSudokuSolver;
import com.github.mr.impossibru.sudoku.solver.iterative.IterativeSudokuSolver;
import com.github.mr.impossibru.sudoku.solver.util.SudokuUtil;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.println("Select solver type: DLX or Iterative");
        SudokuSolver solver;
        switch (in.next()) {
            case "DLX": {
                solver = new DancingLinksSudokuSolver();
                break;
            }
            case "Iterative": {
                solver = new IterativeSudokuSolver();
                break;
            }
            default: {
                throw new IllegalArgumentException("Solver type should be DLX or Iterative");
            }
        }

        System.out.println("Please enter size of board side");
        int boardSide = in.nextInt();
        int[][] initialState = new int[boardSide][boardSide];

        System.out.println("Please enter matrix initial state. Example : " +
                "\n 0 0 0 3" +
                "\n 0 0 0 0" +
                "\n 1 0 4 0" +
                "\n 0 0 0 1\n");
        for (int i = 0; i < boardSide; i++) {
            for (int j = 0; j < boardSide; j++) {
                initialState[i][j] = Integer.parseInt(in.next());
            }
        }

        List<Integer[][]> solutions = solver.solve(initialState);
        SudokuUtil.printSudokuBoard(solutions);

        System.out.println("Enter any symbol to exit");
        in.next();
    }
}
