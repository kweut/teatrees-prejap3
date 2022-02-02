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
        drawHint(grid[0].length, hintBlock);
        border(grid[0].length);
        drawPlayfield(grid);
        border(grid[0].length);
    }

    void drawHint(int width, Block hintBlock) {
        for (int i = 0; i < hintBlock.rows(); i++) {
            startRow();
            spaces((width - hintBlock.cols()) / 2);
            for (int j = 0; j < hintBlock.cols(); j++) {
                print(hintBlock.dotAt(i, j));
            }
            spaces(width - (width - hintBlock.cols()) / 2 - hintBlock.cols());
            endRow();
        }
    }

    void drawPlayfield(byte[][] grid) {
        for (byte[] bytes : grid) {
            startRow();
            for (byte aByte : bytes) {
            print(aByte);
        }
            endRow();
        }
    }

    void spaces(int times) {
        out.print(" ".repeat(times));
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
