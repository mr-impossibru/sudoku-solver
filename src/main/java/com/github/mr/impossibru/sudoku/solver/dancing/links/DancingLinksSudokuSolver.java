package com.github.mr.impossibru.sudoku.solver.dancing.links;

import com.github.mr.impossibru.sudoku.solver.SudokuSolver;
import com.github.mr.impossibru.sudoku.solver.dancing.links.model.DancingNode;
import com.github.mr.impossibru.sudoku.solver.model.SudokuBoard;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
public class DancingLinksSudokuSolver implements SudokuSolver {

    @Getter
    @Setter
    private SudokuBoard board;

    /**
     * Number of constraints:
     * cell - 1 number in cell
     * row - no duplicate numbers in row
     * column - no duplicate numbers in column
     * square - no duplicates numbers in square
     */
    private static final int CONSTRAINTS = 4;

    /**
     * Solves sudoku using Dancing Links Algorithm
     *
     * @param initialState initial state of sudoku board
     * @return solved sudoku board
     */
    @Override
    public List<Integer[][]> solve(int[][] initialState) {
        long start = System.currentTimeMillis();
        this.board = new SudokuBoard(initialState);
        boolean[][] coverBoard = createCoverBoard();
        putInitialStateToCoverBoard(initialState, coverBoard);

        DLX dlx = new DLX(coverBoard);
        List<List<DancingNode>> dancingNodes = dlx.runAlgorithmX();
        long end = System.currentTimeMillis();

        System.out.println("Took " + (end - start) + "ms");

        return transformAnswerToSudokuGrid(dancingNodes);
    }

    private int getCoverBoardRowIndex(int candidate, int row, int col) {
        return (candidate - 1) + (row - 1) * board.getSide() * board.getSide() + (col - 1) * board.getSide();
    }

    /**
     * Creates exact cover board.
     * Rows - each possible number in each cells (candidate)
     * Columns - each constraint for candidate.
     *
     * @return cover board
     */
    private boolean[][] createCoverBoard() {
        boolean[][] coverBoard = new boolean[board.getSide() * board.getSide() * board.getSide()][board.getSide() * board.getSide() * CONSTRAINTS];
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
        for (int row = 1; row <= board.getSide(); row++) {
            for (int column = 1; column <= board.getSide(); column++) {
                constraintColIndex++;
                for (int candidate = 1; candidate <= board.getMaxNumber(); candidate++) {
                    coverBoard[getCoverBoardRowIndex(candidate, row, column)][constraintColIndex] = true;
                }
            }
        }

        return constraintColIndex;
    }

    private int fillRowConstraint(boolean[][] coverBoard, int constraintColIndex) {
        for (int row = 1; row <= board.getSide(); row++) {
            for (int candidate = 1; candidate <= board.getMaxNumber(); candidate++) {
                constraintColIndex++;
                for (int column = 1; column <= board.getSide(); column++) {
                    coverBoard[getCoverBoardRowIndex(candidate, row, column)][constraintColIndex] = true;
                }
            }
        }

        return constraintColIndex;
    }

    private int fillColumnConstraint(boolean[][] coverBoard, int constraintColIndex) {
        for (int column = 1; column <= board.getSide(); column++) {
            for (int candidate = 1; candidate <= board.getMaxNumber(); candidate++) {
                constraintColIndex++;
                for (int row = 1; row <= board.getSide(); row++) {
                    coverBoard[getCoverBoardRowIndex(candidate, row, column)][constraintColIndex] = true;
                }
            }
        }

        return constraintColIndex;
    }

    private int fillSectorConstraint(boolean[][] coverBoard, int constraintColIndex) {
        for (int row = 1; row <= board.getSide(); row += board.getSectorSide()) {
            for (int column = 1; column <= board.getSide(); column += board.getSectorSide()) {
                for (int candidate = 1; candidate <= board.getMaxNumber(); candidate++) {
                    constraintColIndex++;
                    for (int sectorRow = 0; sectorRow < board.getSectorSide(); sectorRow++) {
                        for (int sectorCol = 0; sectorCol < board.getSectorSide(); sectorCol++) {
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
        for (int col = 1; col <= board.getSide(); col++) {
            for (int row = 1; row <= board.getSide(); row++) {
                int value = initialState[row - 1][col - 1];
                if (value != 0) {
                    for (int candidate = 1; candidate <= board.getMaxNumber(); candidate++) {
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
            Integer[][] solvedBoard = new Integer[board.getSide()][board.getSide()];

            for (DancingNode node : solution) {
                DancingNode rowColumnNode = node;

                int min = Integer.parseInt(rowColumnNode.getColumn().getName());

                //find cell constraint node
                for (DancingNode rightNode = node.getRight(); rightNode != node; rightNode = rightNode.getRight()) {
                    int value = Integer.parseInt(rightNode.getColumn().getName());

                    if (value < min) {
                        min = value;
                        rowColumnNode = rightNode;
                    }
                }

                // we pick cell constraint node to get row and column index
                // and row constraint node to get value
                // for more information print cover board before initial data input
                int cellConstraintNode = Integer.parseInt(rowColumnNode.getColumn().getName());
                int rowConstrainIndex = Integer.parseInt(rowColumnNode.getRight().getColumn().getName());

                int rowIndex = cellConstraintNode / board.getSide();
                int colIndex = cellConstraintNode % board.getSide();

                int candidate = (rowConstrainIndex % board.getSide()) + 1;

                solvedBoard[rowIndex][colIndex] = candidate;
            }
            result.add(solvedBoard);
        }


        return result;
    }
}
