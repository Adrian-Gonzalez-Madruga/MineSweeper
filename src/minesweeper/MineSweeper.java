package minesweeper;

import DLibX.DConsole;

public class MineSweeper {

    public static void main(String[] args) {
        DConsole dc = new DConsole(29 * 32, 29 * 32);
        Board b = new Board(dc);
        while (true) {
            while (b.isAlive()) {
                dc.clear();
                b.uncover();
                b.cover();
                b.draw();
                dc.redraw();
                dc.pause(20);
            }
            while (!dc.isKeyPressed(' '));
            b.Restart();
        }
    }
}
