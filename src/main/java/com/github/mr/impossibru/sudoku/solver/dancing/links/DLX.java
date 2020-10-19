package com.github.mr.impossibru.sudoku.solver.dancing.links;

import com.github.mr.impossibru.sudoku.solver.dancing.links.model.ColumnDancingNode;
import com.github.mr.impossibru.sudoku.solver.dancing.links.model.DancingNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DLX {

    private ColumnDancingNode root;
    private final List<List<DancingNode>> result;
    private final Stack<DancingNode> stack;

    public DLX(boolean[][] exactCoverBoard) {
        final int columnsNumber = exactCoverBoard[0].length;
        final ColumnDancingNode[] columns = new  ColumnDancingNode[columnsNumber];
        result = new ArrayList<>();
        stack = new Stack<>();
        root = new ColumnDancingNode("root");

        //link columns to root
        for (int i = 0; i < columnsNumber; i++) {
            ColumnDancingNode column = new ColumnDancingNode(Integer.toString(i));
            columns[i] = column;
            root = (ColumnDancingNode) root.addRight(column);
        }
        //set root node
        root = root.getRight().getColumn();

        //add nodes to columns
        for (boolean[] row : exactCoverBoard) {
            DancingNode previous = null;
            for (int i = 0; i < columnsNumber; i++) {
                boolean cell = row[i];
                if (cell) {
                    ColumnDancingNode column = columns[i];
                    DancingNode node = new DancingNode(column);
                    column.getUp().addDown(node);
                    column.incrementSize();
                    previous = previous == null ? node : previous.addRight(node);
                }
            }
        }

        root.setSize(columnsNumber);
    }

    public List<List<DancingNode>> runAlgorithmX() {
        if (root == root.getRight()) {
            result.add(new ArrayList<>(stack));
        } else {
            ColumnDancingNode column = selectColumn();
            column.cover();

            // pick node from selected column
            // add it to solutions stack
            // cover columns, which are linked to selected row
            // run Algo X again
            // pop from stack and discover columns to try another combination
            for (DancingNode columnDownNode = column.getDown(); columnDownNode != column; columnDownNode = columnDownNode.getDown()) {
                stack.push(columnDownNode);

                for (DancingNode node = columnDownNode.getRight(); node != columnDownNode; node = node.getRight()) {
                    node.getColumn().cover();
                }

                runAlgorithmX();

                DancingNode poppedNode = stack.pop();
                column = poppedNode.getColumn();

                for (DancingNode node = poppedNode.getLeft(); node != poppedNode; node = node.getLeft()) {
                    node.getColumn().discover();
                }
            }

            column.discover();
        }

        return result;
    }

    /**
     * Selects column with minimal number of nodes -> less possible variations
     * @return column node
     */
    private ColumnDancingNode selectColumn() {
        ColumnDancingNode result = null;
        int minSize = Integer.MAX_VALUE;

        for (ColumnDancingNode node = (ColumnDancingNode) root.getRight();
             node != root;
             node = (ColumnDancingNode) node.getRight()) {
            if (node.getSize() < minSize) {
                minSize = node.getSize();
                result = node;
            }
        }

        return result;
    }

}
