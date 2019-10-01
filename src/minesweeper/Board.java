package minesweeper;

import DLibX.DConsole;
import java.awt.Font;

public class Board {

    private static String TimesNewRoman;

    private int n = 29;
    private int[][] map = new int[n][n];
    private boolean[][] uncovered = new boolean[n][n];
    private int[] dirX = {0, 1, 1, 1, 0, -1, -1, -1}; //direction starting above on a grid moving clockwise
    private int[] dirY = {-1, -1, 0, 1, 1, 1, 0, -1};
    private int[][] flagnum = new int[n][n];
    private int found = 0;
    private boolean alive = true;

    private DConsole dc;

    public Board(DConsole dc) {
        this.dc = dc;
        init();
    }

    public void init() {
        int bombCount = (int) ((n * n) / 6.84);
        while (bombCount > 0) {
            for (int c = 0; c < map.length; c++) {
                for (int v = 0; v < map.length; v++) {
                    if (map[c][v] != 9 && (int) (Math.random() * 5) == 0) {
                        map[c][v] = 9;
                        bombCount--;
                    }
                }
            }
        }
        for (int c = 0; c < map.length; c++) {
            for (int v = 0; v < map.length; v++) {
                for (int dir = 0; dir < dirX.length; dir++) {
                    if ((c + dirY[dir] != -1) && (c + dirY[dir] != n) && (v + dirX[dir] != -1) && (v + dirX[dir] != n) && map[c][v] != 9) {
                        if (map[c + dirY[dir]][v + dirX[dir]] == 9) {
                            map[c][v]++;
                        }
                    }
                }
            }
        }
    }

    public void Restart() {
        found = 0;
        for (int c = 0; c < map.length; c++) {
            for (int v = 0; v < map.length; v++) {
                map[c][v] = 0;
                uncovered[c][v] = false;
                flagnum[c][v] = 0;
            }
        }
        init();
        alive = true;
    }

    public void draw() {
        for (int c = 0; c < map.length; c++) {
            for (int v = 0; v < map.length; v++) {
                if (uncovered[c][v]) { //change under here when you make png's
                    dc.drawImage("png/0.png", v * 32, c * 32);
                    if (map[c][v] > 0 && map[c][v] != 9) {
                        dc.setFont(new Font(TimesNewRoman, Font.BOLD, 20));
                        dc.drawString(map[c][v], (v * 32) + 12, (c * 32));
                    } else if (map[c][v] == 9) {
                        dc.drawImage("png/bomb.png", v * 32, c * 32);
                    }
                } else if (!uncovered[c][v]) {
                    if (map[c][v] > 9) {
                        dc.drawImage("png/flag" + flagnum[c][v] + ".png", v * 32, c * 32);
                    } else {
                        dc.drawImage("png/cover.png", v * 32, c * 32);
                    }
                }
            }
        }
    }

    public void uncover() {
        if (dc.isMouseButton(1) && getX() > -1 && getX() < n && getY() > -1 && getY() < n && map[getY()][getX()] < 10) {
            uncovered[getY()][getX()] = true;
            if (map[getY()][getX()] == 0) {
                zero(getY(), getX());
            } else if (map[getY()][getX()] == 9) {
                reveal();
            }
        }
    }

    public void cover() {
        if (dc.isMouseButton(3) && !uncovered[getY()][getX()] && flagnum[getY()][getX()] < 1) {
            found();
            if (map[getY()][getX()] < 10) {
                map[getY()][getX()] += 10;
                dc.pause(100);
            } else {
                map[getY()][getX()] -= 10;
                dc.pause(100);
            }
        }
    }

    public void zero(int c, int v) {
        for (int dir = 0; dir < 8; dir++) {
            if ((c + dirY[dir] > -1) && (c + dirY[dir] < n) && (v + dirX[dir] > -1) && (v + dirX[dir] < n)) { // will not go out of array
                if (!uncovered[c + dirY[dir]][v + dirX[dir]]) { //if covered
                    uncovered[c + dirY[dir]][v + dirX[dir]] = true; // uncover it
                    if (map[c + dirY[dir]][v + dirX[dir]] == 0) {
                        zero(c + dirY[dir], v + dirX[dir]); // try the next one
                    }
                }
            }
        }
    }

    public void reveal() {
        for (int c = 0; c < map.length; c++) {
            for (int v = 0; v < map.length; v++) {
                if (map[c][v] < 10 && !uncovered[c][v]) {
                    uncovered[c][v] = true;
                } else if (map[c][v] < 19 && !uncovered[c][v]) {
                    flagnum[c][v] = 1;
                }
            }
        }
        alive = false;
    }

    public void found() {
        if (map[getY()][getX()] == 9) {
            found++;
        } else if (map[getY()][getX()] == 19) {
            found--;
        }
    }
    
    public boolean isAlive() {
        return alive;
    }

    public int getX() {
        return dc.getMouseXPosition() / 32;
    }

    public int getY() {
        return dc.getMouseYPosition() / 32;
    }

}
