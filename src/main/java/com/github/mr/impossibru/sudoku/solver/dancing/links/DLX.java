package com.github.mr.impossibru.sudoku.solver.dancing.links;

import com.github.mr.impossibru.sudoku.solver.dancing.links.model.ColumnDancingNode;
import com.github.mr.impossibru.sudoku.solver.dancing.links.model.DancingNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DLX {

    private ColumnDancingNode header;
    private List<DancingNode> result;
    private final Stack<DancingNode> stack;

    public DLX(boolean[][] exactCoverBoard) {
        int columnsNumber = exactCoverBoard[0].length;
        //FIXME: change to array?
        final List<ColumnDancingNode> columns = new ArrayList<>(columnsNumber);
        stack = new Stack<>();
        header = new ColumnDancingNode("h");

        //link columns to header
        for (int i = 0; i < columnsNumber; i++) {
            ColumnDancingNode column = new ColumnDancingNode(Integer.toString(i));
            columns.add(column);
            header = (ColumnDancingNode) header.addRight(column);
        }
        //set header node
        header = header.getRight().getHead();

        for (boolean[] row : exactCoverBoard) {
            DancingNode previous = null;
            for (int i = 0; i < columnsNumber; i++) {
                boolean cell = row[i];
                if (cell) {
                    ColumnDancingNode column = columns.get(i);
                    DancingNode node = new DancingNode(column);
                    column.getUp().addDown(node);
                    column.incrementSize();
                    previous = previous == null ? node : previous.addRight(node);
                }
            }
        }

        header.setSize(columnsNumber);
    }

    public List<DancingNode> runAlgorithmX() {
        if (header == header.getRight()) {
            result = new ArrayList<>(stack);
        } else {
            ColumnDancingNode column = selectColumn();
            column.cover();

            for (DancingNode columnDownNode = column.getDown(); columnDownNode != column; columnDownNode = columnDownNode.getDown()) {
                stack.push(columnDownNode);

                for (DancingNode node = columnDownNode.getRight(); node != columnDownNode; node = node.getRight()) {
                    node.getHead().cover();
                }

                runAlgorithmX();
                DancingNode poppedNode = stack.pop();
                column = poppedNode.getHead();

                for (DancingNode node = poppedNode.getLeft(); node != poppedNode; node = node.getLeft()) {
                    node.getHead().discover();
                }
            }

            column.discover();
        }

        return result;
    }

    private ColumnDancingNode selectColumn() {
        ColumnDancingNode result = null;
        int minSize = Integer.MAX_VALUE;

        for (ColumnDancingNode node = (ColumnDancingNode) header.getRight();
             node != header;
             node = (ColumnDancingNode) node.getRight()) {
            if (node.getSize() < minSize) {
                minSize = node.getSize();
                result = node;
            }
        }

        return result;
    }



}
