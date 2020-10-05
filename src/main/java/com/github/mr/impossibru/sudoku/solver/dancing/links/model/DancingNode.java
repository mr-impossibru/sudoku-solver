package com.github.mr.impossibru.sudoku.solver.dancing.links.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class DancingNode {

    protected DancingNode left;

    protected DancingNode right;

    protected DancingNode up;

    protected DancingNode down;

    protected ColumnDancingNode column;

    public DancingNode(ColumnDancingNode column) {
        this();
        this.column = column;
    }

    public DancingNode() {
        this.left = this;
        this.right = this;
        this.up = this;
        this.down = this;
    }

    public DancingNode removeFromRow() {
        this.left.setRight(this.right);
        this.right.setLeft(this.left);

        return this;
    }

    public DancingNode putToRow() {
        this.left.setRight(this);
        this.right.setLeft(this);

        return this;
    }

    public DancingNode removeFromColumn() {
        this.up.setDown(this.down);
        this.down.setUp(this.up);

        return this;
    }

    public DancingNode putToColumn() {
        this.up.setDown(this);
        this.down.setUp(this);

        return this;
    }


    public DancingNode addRight(DancingNode node) {
        node.setLeft(this);
        node.setRight(this.right);
        this.right.setLeft(node);
        setRight(node);

        return node;
    }

    public DancingNode addDown(DancingNode node) {
        node.setUp(this);
        node.setDown(this.down);
        this.down.setUp(node);
        this.setDown(node);

        return node;
    }

}
