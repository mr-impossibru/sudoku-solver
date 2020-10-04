package com.github.mr.impossibru.sudoku.solver.dancing.links;

import com.github.mr.impossibru.sudoku.solver.SudokuSolver;
import com.github.mr.impossibru.sudoku.solver.dancing.links.model.DancingNode;

import java.util.Arrays;
import java.util.List;

public class DancingLinksSudokuSolver implements SudokuSolver {

    /**
     * Size of the board side
     */
    private final int boardSide;

    private final int maxNumber;

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

    @Override
    public int[][] solve(int[][] initialState) {
        boolean[][] coverBoard = createCoverBoard();
        putInitialStateToCoverBoard(initialState, coverBoard);

        DLX dlx = new DLX(coverBoard);
        List<DancingNode> dancingNodes = dlx.runAlgorithmX();

        return transformAnswerToSudokuGrid(dancingNodes);
    }

    private int getCoverBoardRowIndex(int candidate, int row, int col) {
        return (candidate - 1) + (row - 1) * boardSide * boardSide + (col - 1) * boardSide;
    }

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

    /**
     * Creates exact cover board.
     * Rows - each possible number in each cells.
     * Columns - each constraint for candidate (number / row / column).
     *
     * @return cover board
     */
    private boolean[][] createCoverBoard() {
        // add 1 for header column
        boolean[][] coverBoard = new boolean[boardSide * boardSide * boardSide][boardSide * boardSide * CONSTRAINTS];
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
    
    public int[][] transformAnswerToSudokuGrid(List<DancingNode> nodes) {
        int[][] result = new int[boardSide][boardSide];

        for (DancingNode node : nodes) {
            DancingNode rcNode = node;
            
            int min = Integer.parseInt(rcNode.getHead().getName());

            for (DancingNode rightNode = node.getRight(); rightNode != node; rightNode = rightNode.getRight()) {
                int value = Integer.parseInt(rightNode.getHead().getName());

                if (value < min) {
                    min = value;
                    rcNode = rightNode;
                }
            }

            //TODO rename
            int ans1 = Integer.parseInt(rcNode.getHead().getName());
            int ans2 = Integer.parseInt(rcNode.getRight().getHead().getName());

            int rowIndex = ans1 / boardSide;
            int colIndex = ans1 % boardSide;

            int num = (ans2 % boardSide) + 1;

            result[rowIndex][colIndex] = num;
        }

        return result;
    }
}
