package com.example.minesweeper.client.screen;

import java.util.Random;

public class MinesweeperGame {
    private final int width;
    private final int height;
    private final int mineCount;
    private final boolean[][] mines;
    private final boolean[][] revealed;
    private final boolean[][] flagged;
    private final int[][] neighborMines;
    private boolean gameOver;
    private boolean win;
    private boolean firstClick;
    private int revealedCount;

    public MinesweeperGame(int width, int height, int mineCount) {
        this.width = width;
        this.height = height;
        this.mineCount = mineCount;
        this.mines = new boolean[width][height];
        this.revealed = new boolean[width][height];
        this.flagged = new boolean[width][height];
        this.neighborMines = new int[width][height];
        this.gameOver = false;
        this.win = false;
        this.firstClick = true;
        this.revealedCount = 0;
    }

    private void generateMines(int safeX, int safeY) {
        Random random = new Random();
        int placed = 0;
        while (placed < mineCount) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            if (!mines[x][y] && !(x == safeX && y == safeY)) {
                mines[x][y] = true;
                placed++;
            }
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                neighborMines[x][y] = countNeighborMines(x, y);
            }
        }
    }

    private int countNeighborMines(int x, int y) {
        int count = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                int nx = x + dx;
                int ny = y + dy;
                if (nx >= 0 && nx < width && ny >= 0 && ny < height && mines[nx][ny]) {
                    count++;
                }
            }
        }
        return count;
    }

    public void reveal(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) return;
        if (revealed[x][y] || flagged[x][y]) return;

        if (firstClick) {
            generateMines(x, y);
            firstClick = false;
        }

        revealed[x][y] = true;
        revealedCount++;

        if (mines[x][y]) {
            gameOver = true;
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (mines[i][j]) revealed[i][j] = true;
                }
            }
            return;
        }

        if (neighborMines[x][y] == 0) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    reveal(x + dx, y + dy);
                }
            }
        }

        checkWin();
    }

    public void toggleFlag(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) return;
        if (revealed[x][y]) return;
        flagged[x][y] = !flagged[x][y];
    }

    private void checkWin() {
        if (revealedCount == width * height - mineCount) {
            win = true;
            gameOver = true;
        }
    }

    public int getRemainingMines() {
        int flags = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (flagged[x][y]) flags++;
            }
        }
        return mineCount - flags;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public boolean isMine(int x, int y) { return mines[x][y]; }
    public boolean isRevealed(int x, int y) { return revealed[x][y]; }
    public boolean isFlagged(int x, int y) { return flagged[x][y]; }
    public int getNeighborMines(int x, int y) { return neighborMines[x][y]; }
    public boolean isGameOver() { return gameOver; }
    public boolean isWin() { return win; }
    public boolean isExploded(int x, int y) { return mines[x][y] && revealed[x][y] && !win; }
}
