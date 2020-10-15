package com.github.mr.impossibru.sudoku.solver.dancing.links.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Special node for top of the column
 */
@Getter
@Setter
public class ColumnDancingNode extends DancingNode {

    private final String name;

    private int size = 0;

    public ColumnDancingNode(String name) {
        super();
        this.name = name;
        column = this;
    }

    /**
     * Covers column and rows which intersect with column nodes.
     */
    public void cover() {
        removeFromRow();
        for (DancingNode row = down; row != this; row = row.getDown()) {
            for (DancingNode node = row.getRight(); node != row; node = node.getRight()) {
                node.removeFromColumn();
                node.getColumn().decrementSize();
            }
        }
    }

    /**
     * Discovers column and rows which intersect with column nodes.
     */
    public void discover() {
        for (DancingNode row = down; row != this; row = row.getDown()) {
            for (DancingNode node = row.getRight(); node != row; node = node.getRight()) {
                node.putToColumn();
                node.getColumn().incrementSize();
            }
        }

        putToRow();
    }

    public void incrementSize() {
        size++;
    }

    public void decrementSize() {
        size--;
    }
}
