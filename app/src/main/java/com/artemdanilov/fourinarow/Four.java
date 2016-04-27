package com.artemdanilov.fourinarow;

import android.util.Log;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by artemdanilov
 */
public class Four {
    public static final int WIDTH = 6;
    public static final int HEIGHT = 5;
    public static final int WIN_LENGTH = 4;
    private static final String TAG = "DEBUG";
    private final Map<Cell, Player> board = new LinkedHashMap<>();
    private Player currentPlayer;
    public Four() {
        board.clear();
        currentPlayer = Player.WHITE;
    }

    public Player getCell(int x, int y) {
        return board.get(new Cell(x, y));
    }

    public void makeMove(int column) {
        if (makeMove(currentPlayer, column)) {
            if (currentPlayer == Player.WHITE)
                currentPlayer = Player.BLACK;
            else
                currentPlayer = Player.WHITE;
        }
    }

    public boolean makeMove(Player player, int column) {

        if (board.get(new Cell(column, HEIGHT)) != null) {
            Log.i(TAG, "column full");
            return false;
        }

        int pos = HEIGHT;
        while (board.get(new Cell(column, pos)) == null && pos != 0)
            pos--;
        Log.i(TAG, "pos to move " + pos);

        if (pos == 0 && board.get(new Cell(column, pos)) == null)
            board.put(new Cell(column, pos), player);
        else
            board.put(new Cell(column, pos + 1), player);

        return true;
    }

    public boolean hasFreeCells() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (board.get(new Cell(x, y)) == null)
                    return true;
            }
        }
        return false;
    }

    private boolean checkDirection(Cell currentCell, Player player, int xShift, int yShift) {
        int length = 0;
        Cell current = currentCell;
        while (++length < WIN_LENGTH) {
            int currentX = current.getX() + xShift;
            int currentY = current.getY() + yShift;

            if (!(currentX >= 0 && currentX < WIDTH && currentY >= 0 && currentY < HEIGHT))
                break;

            current = new Cell(currentX + xShift, currentY + yShift);

            if (board.get(current) != player) break;
        }
        return length == WIN_LENGTH;
    }

    public Player winner() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {

                Cell cell = new Cell(x, y);
                if (board.get(cell) != null) {

                    Player player = board.get(cell);

                    if (checkDirection(cell, player, 0, 1) ||
                            checkDirection(cell, player, 1, 0) ||
                            checkDirection(cell, player, 1, 1) ||
                            checkDirection(cell, player, 1, -1))
                        return player;

                }
            }
        }
        return null;
    }


    public enum Player {
        BLACK, WHITE
    }

    public class Cell {
        private final int x;
        private final int y;

        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Cell cell = (Cell) o;

            if (x != cell.x) return false;
            return y == cell.y;

        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
    }
}
