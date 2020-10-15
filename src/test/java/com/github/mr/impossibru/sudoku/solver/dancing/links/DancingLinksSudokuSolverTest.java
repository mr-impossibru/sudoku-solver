package com.github.mr.impossibru.sudoku.solver.dancing.links;

import com.github.mr.impossibru.sudoku.solver.SudokuSolver;
import com.github.mr.impossibru.sudoku.solver.Util;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DancingLinksSudokuSolverTest {

    private SudokuSolver sudokuSolver;

    @Test
    void sudoku_4x4_OneSolution() {
        int[][] initialState = {
                {4, 2, 1, 3},
                {0, 1, 2, 4},
                {1, 3, 4, 2},
                {2, 4, 3, 1},
        };
        Integer[][] expected = {
                {4, 2, 1, 3},
                {3, 1, 2, 4},
                {1, 3, 4, 2},
                {2, 4, 3, 1},
        };

        sudokuSolver = new DancingLinksSudokuSolver(initialState.length);
        List<Integer[][]> actual = sudokuSolver.solve(initialState);
        Util.printSudokuBoard(actual);
        assertEquals(1, actual.size());
        assertArrayEquals(expected, actual.get(0));
    }

    @Test
    void sudoku_4x4_MultipleSolutions() {
        int[][] initialState = {
                {0, 0, 0, 3},
                {0, 0, 0, 0},
                {1, 0, 4, 0},
                {0, 0, 0, 1},
        };
        Integer[][] expectedFirst = {
                {2, 4, 1, 3},
                {3, 1, 2, 4},
                {1, 3, 4, 2},
                {4, 2, 3, 1}
        };
        Integer[][] expectedSecond = {
                {4, 1, 2, 3},
                {3, 2, 1, 4},
                {1, 3, 4, 2},
                {2, 4, 3, 1}
        };
        Integer[][] expectedThird = {
                {4, 2, 1, 3},
                {3, 1, 2, 4},
                {1, 3, 4, 2},
                {2, 4, 3, 1}
        };

        sudokuSolver = new DancingLinksSudokuSolver(initialState.length);
        List<Integer[][]> actual = sudokuSolver.solve(initialState);
        Util.printSudokuBoard(actual);
        assertEquals(3, actual.size());
        assertArrayEquals(expectedFirst, actual.get(0));
        assertArrayEquals(expectedSecond, actual.get(1));
        assertArrayEquals(expectedThird, actual.get(2));
    }

    @Test
    void sudoku_4x4_allCombinations() {
        int[][] initialState = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
        };

        sudokuSolver = new DancingLinksSudokuSolver(initialState.length);
        List<Integer[][]> actual = sudokuSolver.solve(initialState);
        assertEquals(288, actual.size());
        Util.printSudokuBoard(actual);
    }

    @Test
    void sudoku_9x9_OneSolutions() {
        int[][] initialState = {
                {8, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 3, 6, 0, 0, 0, 0, 0},
                {0, 7, 0, 0, 9, 0, 2, 0, 0},
                {0, 5, 0, 0, 0, 7, 0, 0, 0},
                {0, 0, 0, 0, 4, 5, 7, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 3, 0},
                {0, 0, 1, 0, 0, 0, 0, 6, 8},
                {0, 0, 8, 5, 0, 0, 0, 1, 0},
                {0, 9, 0, 0, 0, 0, 4, 0, 0}
        };
        Integer[][] expected = {
                {8, 1, 2, 7, 5, 3, 6, 4, 9},
                {9, 4, 3, 6, 8, 2, 1, 7, 5},
                {6, 7, 5, 4, 9, 1, 2, 8, 3},
                {1, 5, 4, 2, 3, 7, 8, 9, 6},
                {3, 6, 9, 8, 4, 5, 7, 2, 1},
                {2, 8, 7, 1, 6, 9, 5, 3, 4},
                {5, 2, 1, 9, 7, 4, 3, 6, 8},
                {4, 3, 8, 5, 2, 6, 9, 1, 7},
                {7, 9, 6, 3, 1, 8, 4, 5, 2}
        };

        sudokuSolver = new DancingLinksSudokuSolver(initialState.length);
        List<Integer[][]> actual = sudokuSolver.solve(initialState);
        Util.printSudokuBoard(actual);
        assertEquals(1, actual.size());
        assertArrayEquals(expected, actual.get(0));
    }
    
}