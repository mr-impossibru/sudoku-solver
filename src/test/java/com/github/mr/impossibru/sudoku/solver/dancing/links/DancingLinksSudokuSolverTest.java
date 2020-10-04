package com.github.mr.impossibru.sudoku.solver.dancing.links;

import com.github.mr.impossibru.sudoku.solver.SudokuSolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class DancingLinksSudokuSolverTest {

    private SudokuSolver sudokuSolver;

    @BeforeEach
    void setUp() {
       sudokuSolver = new DancingLinksSudokuSolver(4);
    }

    @Test
    void sudoku_4x4_OneEmptyCell() {
        int[][] initialState = {
                {4, 2, 1, 3},
                {0, 1, 2, 4},
                {1, 3, 4, 2},
                {2, 4, 3, 1},
        };
        int[][] expected = {
                {4, 2, 1, 3},
                {3, 1, 2, 4},
                {1, 3, 4, 2},
                {2, 4, 3, 1},
        };
        int[][] actual = sudokuSolver.solve(initialState);
        for (int[] ints : actual) {
            System.out.println(Arrays.toString(ints));
        }
        assertArrayEquals(expected, actual);
    }

    @Test
    void sudoku_4x4_TwelveEmptyCells() {
        int[][] initialState = {
                {0, 0, 0, 3},
                {0, 0, 0, 0},
                {1, 0, 4, 0},
                {0, 0, 0, 1},
        };
        int[][] expected = {
                {4, 2, 1, 3},
                {3, 1, 2, 4},
                {1, 3, 4, 2},
                {2, 4, 3, 1},
        };
        int[][] actual = sudokuSolver.solve(initialState);
        for (int[] ints : actual) {
            System.out.println(Arrays.toString(ints));
        }
        assertArrayEquals(expected, actual);
    }




}