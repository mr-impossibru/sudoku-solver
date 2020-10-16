package com.github.mr.impossibru.sudoku.solver.iterative;

import com.github.mr.impossibru.sudoku.solver.model.SudokuBoard;
import lombok.Getter;
import lombok.Setter;

public class IterativeSudokuSolver {

    @Getter
    @Setter
    private SudokuBoard sudoku;

    public IterativeSudokuSolver(SudokuBoard sudokuBoard) {
        this.sudoku = sudokuBoard;
    }

    public boolean solve() {
        for (int row = 0; row < sudoku.getSide(); row++) {
            for (int column = 0; column < sudoku.getSide(); column++) {
                if (sudoku.getBoard()[row][column] == 0) {
                    for (int candidate = 1; candidate <= sudoku.getMaxNumber(); candidate++) {
                        if (passConstraints(sudoku.getBoard(), candidate, row, column)) {
                            sudoku.getBoard()[row][column] = candidate;
                            if (solve()) {
                                return true;
                            } else {
                                sudoku.getBoard()[row][column] = 0;
                            }
                        }
                    }

                    return false;
                }
            }
        }

        return true;
    }

    private boolean passConstraints(int[][] board, int candidate, int row, int column) {
        return checkRowConstraint(board, candidate, row)
                && checkColumnConstraint(board, candidate, column)
                && checkSectorConstraint(board, candidate, row, column);
    }

    private boolean checkColumnConstraint(int[][] board, int candidate, int column) {
        for (int i = 0; i < sudoku.getSide(); i++) {
            if (board[i][column] == candidate) {
                return false;
            }
        }

        return true;
    }

    private boolean checkRowConstraint(int[][] board, int candidate, int row) {
        for (int j = 0; j < sudoku.getSide(); j++) {
            if (board[row][j] == candidate) {
                return false;
            }
        }

        return true;
    }

    private boolean checkSectorConstraint(int[][] board, int candidate, int row, int column) {
        int sectorStartRow = row - row % sudoku.getSectorSide();
        int sectorStartColumn = column - column % sudoku.getSectorSide();

        for (int i = sectorStartRow; i < sectorStartRow + sudoku.getSectorSide(); i++) {
            for (int j = sectorStartColumn; j < sectorStartColumn + sudoku.getSectorSide(); j++) {
                if (board[i][j] == candidate) {
                    return false;
                }
            }
        }

        return true;
    }
}
