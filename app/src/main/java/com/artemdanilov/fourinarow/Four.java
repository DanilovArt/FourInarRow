package com.artemdanilov.fourinarow;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by artemdanilov
 */
public class Four {
    public static final int WIDTH = 6;
    public static final int HEIGHT = 5;
    public static final int WIN_LENGTH = 4;
    private static final String TAG = "DEBUG";
    private final HashMap<Cell, Player> board;
    private Player currentPlayer;

    public Four() {
        board = new LinkedHashMap<>();
        currentPlayer = Player.WHITE;
    }

    private Four(Player currentPlayer, HashMap<Cell,Player> board) {
        this.currentPlayer = currentPlayer;
        this.board = board;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    @SuppressWarnings("unchecked")
    @Override
    protected Four clone() {
        return new Four(currentPlayer, (HashMap<Cell,Player>) board.clone());
    }

    public List<Integer> possibleColumns() {
        List<Integer> columns = new ArrayList<>(WIDTH);
        for (int i = 0; i < WIDTH; i++) {
            if (board.get(new Cell(i, HEIGHT)) == null) {
                columns.add(i);
            }
        }
        return columns;
    }

    public Player getCell(int x, int y) {
        return board.get(new Cell(x, y));
    }

    public Player getCell(Cell cell){
        return board.get(cell);
    }

    public Cell makeMove(int column) {
        Log.i(TAG,"make move");
        Cell moveTo = makeMove(currentPlayer, column);
        if (moveTo != null) {
            if (currentPlayer == Player.WHITE)
                currentPlayer = Player.BLACK;
            else
                currentPlayer = Player.WHITE;
        }
        return moveTo;
    }

    public Cell makeMove(Player player, int column) {

        if (board.get(new Cell(column, HEIGHT)) != null) {
            //Log.i(TAG, "column full");
            return null;
        }

        int pos = HEIGHT;
        Cell cellToMove = null;
        while (board.get(new Cell(column, pos)) == null && pos != 0)
            pos--;
        // Log.i(TAG, "pos to move " + pos);

        if (pos == 0 && board.get(new Cell(column, pos)) == null) {
            cellToMove = new Cell(column, pos);
            board.put(cellToMove, player);
        } else {
            cellToMove = new Cell(column, pos + 1);
            board.put(cellToMove, player);
        }
        return cellToMove;
    }

    public boolean hasFreeCells() {
        for (int x = 0; x <= WIDTH; x++) {
            for (int y = 0; y <= HEIGHT; y++) {
                if (board.get(new Cell(x, y)) == null)
                    return true;
            }
        }
        return false;
    }

    public void unmakeMove(Cell move) {
        Player player = board.remove(move);
        if (player == null)
            return;
        assert (player == currentPlayer);
        if (player == Player.WHITE) {
            currentPlayer = Player.BLACK;
        } else {
            currentPlayer = Player.WHITE;
        }
    }

  /* private boolean checkDirection(Cell currentCell, Player player, int xShift, int yShift) {
        int length = 0;
        Cell current = currentCell;
        Log.i(TAG, "length " + length);
        while (++length <= WIN_LENGTH) {
            Log.i(TAG, "length in while " + length);
            int currentX = current.getX() + xShift;
            int currentY = current.getY() + yShift;

            if (!(currentX >= 0 && currentX < WIDTH && currentY >= 0 && currentY < HEIGHT))
                break;

            current = new Cell(currentX + xShift, currentY + yShift);
            if (board.get(current) != player) break;
        }
        return length == WIN_LENGTH;
    }
    */
    //ищет 4 ряд в одном направлении
    private boolean checkDirection(Cell currentCell, Player player, int xShift, int yShift) {

        int length = 0;

        Cell current = currentCell;


//        if (xShift == 0) {
//
//            Log.i(TAG, "check up");
//
//        } else {
//
//            if (yShift == 0) {
//                Log.i(TAG, "check right");
//            } else if (yShift == 1) {
//
//                Log.i(TAG, "check right up");
//
//            } else if (yShift == -1) {
//
//                Log.i(TAG, "check right down");
//
//            }
//
//        }

//        Log.i(TAG, "Start cell " + current.toString())
//        Log.i(TAG, "Color " + player.toString());


        while (++length < WIN_LENGTH) {

            int currentX = current.getX() + xShift;

            int currentY = current.getY() + yShift;


            if (!(currentX >= 0 && currentX <= WIDTH && currentY >= 0 && currentY <= HEIGHT))

                break;

            current = new Cell(currentX, currentY);

//            Log.i(TAG, "length " + length);
//            Log.i(TAG, "cell " + current.toString());
            Player occupied = board.get(current);

            //  Log.i(TAG, "Color " + (occupied == null ? "empty" : occupied.toString()));


            if (occupied != player) break;

        }

        //  Log.i(TAG, "end check. length " + length + "\n");

        return length == WIN_LENGTH;

    }

    public Player winner() {
        for (int x = 0; x <= WIDTH; x++) {
            for (int y = 0; y <= HEIGHT; y++) {

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


    public static enum Player {
        BLACK, WHITE
    }

    public static class Cell {
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
        public String toString() {
            return "x " + x + " y " + y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Cell cell = (Cell) o;

            return x == cell.x && y == cell.y;

        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
    }
}
