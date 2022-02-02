package com.epam.prejap.teatrees.game;

import com.epam.prejap.teatrees.block.Block;

import java.io.PrintStream;

public class Printer {

    final PrintStream out;

    public Printer(PrintStream out) {
        this.out = out;
    }

    void draw(byte[][] grid, Block hintBlock) {
        clear();
        border(grid[0].length);
        for (int i = 0; i < hintBlock.rows(); i++) {
            startRow();
            out.print(" ".repeat((grid[0].length - hintBlock.cols()) / 2));
            for (int j = 0; j < hintBlock.cols(); j++) {
                print(hintBlock.dotAt(i, j));
            }
            out.print(" ".repeat(grid[0].length - (grid[0].length - hintBlock.cols()) / 2 - hintBlock.cols()));
            endRow();
        }

        border(grid[0].length);
        for (byte[] bytes : grid) {
            startRow();
            for (byte aByte : bytes) {
                print(aByte);
            }
            endRow();
        }
        border(grid[0].length);
    }

    void clear() {
        out.print("\u001b[2J\u001b[H");
    }

    void print(byte dot) {
        out.format(dot == 0 ? " " :"#");
    }

    void startRow() {
        out.print("|");
    }

    void endRow() {
        out.println("|");
    }

    void border(int width) {
        out.println("+" + "-".repeat(width) + "+");
    }
}
