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
        head = this;
    }

    public void cover() {
        removeFromRow();
        for (DancingNode row = down; row != this; row = row.getDown()) {
            for (DancingNode node = row.getRight(); node != row; node = node.getRight()) {
                node.removeFromColumn();
                node.getHead().decrementSize();
            }
        }
    }

    public void discover() {
        for (DancingNode row = down; row != this; row = row.getDown()) {
            for (DancingNode node = row.getRight(); node != row; node = node.getRight()) {
                node.putToColumn();
                node.getHead().incrementSize();
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
