package com.epam.prejap.teatrees.game;

import com.epam.prejap.teatrees.block.Block;
import com.epam.prejap.teatrees.block.BlockFeed;
import com.epam.prejap.teatrees.block.RotatedBlock;

public class Playfield {

    private final int rows;
    private final int cols;
    private final Printer printer;
    private final BlockFeed feed;

    final Grid grid;
    Block block;
    int row;
    int col;
    private Block hintBlock;

    public Playfield(int rows, int cols, BlockFeed feed, Printer printer) {
        this.rows = rows;
        this.cols = cols;
        this.feed = feed;
        this.printer = printer;
        grid = new Grid(new byte[rows][cols]);
        hintBlock = feed.nextBlock();
    }

    /**
     * Before next block appears on the playfield, all complete lines should be removed and replaced with empty
     * lines on the top.
     */
    public void nextBlock() {
        grid.removeCompleteLines();
        block = hintBlock;
        hintBlock = feed.nextBlock();
        row = 0;
        col = (cols - block.cols()) / 2;
        show();
    }

    /**
     * Perform move for current block if possible
     * (there is place for the block after move).
     *
     * After each move, the block is shifted down one unit (if possible).
     *
     * Possible moves:
     * <ul>
     * <li>LEFT - move block one unit left;</li>
     * <li>RIGHT - move block one unit right;</li>
     * <li>UP - rotate block clockwise.</li>
     * </ul>
     *
     * @param move action for current block
     * @return true if the current block was moved down
     */
    public boolean move(Move move) {
        hide();
        boolean moved;
        switch (move) {
            case LEFT  -> moveLeft();
            case RIGHT -> moveRight();
            case UP    -> rotate();
        }
        moved = moveDown();
        show();
        return moved;
    }

    private void moveRight() {
        move(0, 1);
    }

    private void moveLeft() {
        move(0, -1);
    }

    private boolean moveDown() {
        return move(1, 0);
    }

    private boolean move(int rowOffset, int colOffset) {
        boolean moved = false;
        if (isValidMove(block, rowOffset, colOffset)) {
            doMove(rowOffset, colOffset);
            moved = true;
        }
        return moved;
    }

    private void rotate() {
        Block rotated = new RotatedBlock(block);
        if (isValidMove(rotated, 0, 0))
            block = rotated;
    }

    private boolean isValidMove(Block block, int rowOffset, int colOffset) {
        for (int i = 0; i < block.rows(); i++) {
            for (int j = 0; j < block.cols(); j++) {
                var dot = block.dotAt(i, j);
                if (dot > 0) {
                    int newRow = row + i + rowOffset;
                    int newCol = col + j + colOffset;
                    if (newRow >= rows || newCol >= cols || !grid.isCellEmpty(newRow, newCol)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void hide() {
        forEachBrick((i, j, dot) -> grid.cleanCell(row + i, col + j));
    }

    private void show() {
        forEachBrick((i, j, dot) -> grid.fillCell(row + i, col + j, dot));
        printer.draw(grid.getGrid(), hintBlock);
    }

    private void doMove(int rowOffset, int colOffset) {
        row += rowOffset;
        col += colOffset;
    }

    private void forEachBrick(BrickAction action) {
        for (int i = 0; i < block.rows(); i++) {
            for (int j = 0; j < block.cols(); j++) {
                var dot = block.dotAt(i, j);
                if (dot > 0) {
                    action.act(i, j, dot);
                }
            }
        }
    }

    private interface BrickAction {
        void act(int i, int j, byte dot);
    }
}
