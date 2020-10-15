package com.github.mr.impossibru.sudoku.solver.dancing.links;

import com.github.mr.impossibru.sudoku.solver.SudokuSolver;
import com.github.mr.impossibru.sudoku.solver.dancing.links.model.DancingNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DancingLinksSudokuSolver implements SudokuSolver {

    /**
     * Size of the board side.
     */
    private final int boardSide;

    /**
     * Max number in sudoku board, equals size of board side.
     */
    private final int maxNumber;

    /**
     * Size of sector side (square root of board side).
     */
    private final int sectorSide;

    /**
     * Number of constraints:
     * cell - 1 number in cell
     * row - no duplicate numbers in row
     * column - no duplicate numbers in column
     * square - no duplicates numbers in square
     */
    private static final int CONSTRAINTS = 4;

    /**
     * Initialize Sudoku Solver based on Algorithm X
     *
     * @param boardSide size of sudoku board side
     */
    public DancingLinksSudokuSolver(int boardSide) {
        this.boardSide = boardSide;
        this.maxNumber = boardSide;
        this.sectorSide = (int) Math.rint(Math.sqrt(boardSide));
    }

    /**
     * Solves sudoku using Dancing Links Algorithm
     *
     * @param initialState initial state of sudoku board
     * @return solved sudoku board
     */
    @Override
    public List<Integer[][]> solve(int[][] initialState) {
        boolean[][] coverBoard = createCoverBoard();
        putInitialStateToCoverBoard(initialState, coverBoard);

        DLX dlx = new DLX(coverBoard);
        List<List<DancingNode>> dancingNodes = dlx.runAlgorithmX();

        return transformAnswerToSudokuGrid(dancingNodes);
    }

    private int getCoverBoardRowIndex(int candidate, int row, int col) {
        return (candidate - 1) + (row - 1) * boardSide * boardSide + (col - 1) * boardSide;
    }

    /**
     * Creates exact cover board.
     * Rows - each possible number in each cells (candidate)
     * Columns - each constraint for candidate.
     *
     * @return cover board
     */
    private boolean[][] createCoverBoard() {
        boolean[][] coverBoard = new boolean[boardSide * boardSide * boardSide][boardSide * boardSide * CONSTRAINTS];
        /*
            Index of constraint column. For 4x4 board:
            0 - 15 - cell constraint
            16 - 31 - row constraint
            32 - 47 - column constraint
            48 - 63 - sector constraint
         */
        int constraintColIndex = -1;
        constraintColIndex = fillCellConstraint(coverBoard, constraintColIndex);
        constraintColIndex = fillRowConstraint(coverBoard, constraintColIndex);
        constraintColIndex = fillColumnConstraint(coverBoard, constraintColIndex);
        fillSectorConstraint(coverBoard, constraintColIndex);

        return coverBoard;
    }

    private int fillCellConstraint(boolean[][] coverBoard, int constraintColIndex) {
        for (int row = 1; row <= boardSide; row++) {
            for (int column = 1; column <= boardSide; column++) {
                constraintColIndex++;
                for (int candidate = 1; candidate <= maxNumber; candidate++) {
                    coverBoard[getCoverBoardRowIndex(candidate, row, column)][constraintColIndex] = true;
                }
            }
        }

        return constraintColIndex;
    }

    private int fillRowConstraint(boolean[][] coverBoard, int constraintColIndex) {
        for (int row = 1; row <= boardSide; row++) {
            for (int candidate = 1; candidate <= maxNumber; candidate++) {
                constraintColIndex++;
                for (int column = 1; column <= boardSide; column++) {
                    coverBoard[getCoverBoardRowIndex(candidate, row, column)][constraintColIndex] = true;
                }
            }
        }

        return constraintColIndex;
    }

    private int fillColumnConstraint(boolean[][] coverBoard, int constraintColIndex) {
        for (int column = 1; column <= boardSide; column++) {
            for (int candidate = 1; candidate <= maxNumber; candidate++) {
                constraintColIndex++;
                for (int row = 1; row <= boardSide; row++) {
                    coverBoard[getCoverBoardRowIndex(candidate, row, column)][constraintColIndex] = true;
                }
            }
        }

        return constraintColIndex;
    }

    private int fillSectorConstraint(boolean[][] coverBoard, int constraintColIndex) {
        for (int row = 1; row <= boardSide; row += sectorSide) {
            for (int column = 1; column <= boardSide; column += sectorSide) {
                for (int candidate = 1; candidate <= maxNumber; candidate++) {
                    constraintColIndex++;
                    for (int sectorRow = 0; sectorRow < sectorSide; sectorRow++) {
                        for (int sectorCol = 0; sectorCol < sectorSide; sectorCol++) {
                            int rowIndex = getCoverBoardRowIndex(
                                    candidate,
                                    row + sectorRow,
                                    column + sectorCol
                            );
                            coverBoard[rowIndex][constraintColIndex] = true;
                        }
                    }
                }
            }
        }

        return constraintColIndex;
    }

    /**
     * Takes initial state and updates cover board - removes other possible rows for this cell.
     *
     * @param initialState initial state of sudoku board
     * @param coverBoard   exact cover board
     */
    private void putInitialStateToCoverBoard(int[][] initialState, boolean[][] coverBoard) {
        for (int col = 1; col <= boardSide; col++) {
            for (int row = 1; row <= boardSide; row++) {
                int value = initialState[row - 1][col - 1];
                if (value != 0) {
                    for (int candidate = 1; candidate <= maxNumber; candidate++) {
                        if (candidate != value) {
                            Arrays.fill(
                                    coverBoard[getCoverBoardRowIndex(candidate, row, col)],
                                    false
                            );
                        }
                    }
                }
            }
        }
    }

    public List<Integer[][]> transformAnswerToSudokuGrid(List<List<DancingNode>> solutions) {
        List<Integer[][]> result = new ArrayList<>();
        for (List<DancingNode> solution : solutions) {
            Integer[][] solvedBoard = new Integer[boardSide][boardSide];

            for (DancingNode node : solution) {
                DancingNode rcNode = node;

                int min = Integer.parseInt(rcNode.getColumn().getName());

                for (DancingNode rightNode = node.getRight(); rightNode != node; rightNode = rightNode.getRight()) {
                    int value = Integer.parseInt(rightNode.getColumn().getName());

                    if (value < min) {
                        min = value;
                        rcNode = rightNode;
                    }
                }

                //TODO rename
                int ans1 = Integer.parseInt(rcNode.getColumn().getName());
                int ans2 = Integer.parseInt(rcNode.getRight().getColumn().getName());

                int rowIndex = ans1 / boardSide;
                int colIndex = ans1 % boardSide;

                int num = (ans2 % boardSide) + 1;

                solvedBoard[rowIndex][colIndex] = num;
            }
            result.add(solvedBoard);
        }


        return result;
    }
}
