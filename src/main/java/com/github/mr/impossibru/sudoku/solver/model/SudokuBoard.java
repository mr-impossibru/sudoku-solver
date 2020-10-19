package com.github.mr.impossibru.sudoku.solver.model;

import lombok.Getter;

@Getter
public class SudokuBoard {

    /**
     * Size of the board side.
     */
    private final int side;

    /**
     * Max number in sudoku board, equals size of board side.
     */
    private final int maxNumber;

    /**
     * Size of sector side (square root of board side).
     */
    private final int sectorSide;

    /**
     * N x N Sudoku board
     */
    private final int[][] board;

    public SudokuBoard(int[][] board) {
        this.side = board.length;
        this.maxNumber = board.length;
        this.sectorSide = (int) Math.rint(Math.sqrt(side));
        this.board = new int[side][side];
        System.arraycopy(board, 0, this.board, 0 , side);
    }
}
