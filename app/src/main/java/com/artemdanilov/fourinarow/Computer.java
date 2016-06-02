package com.artemdanilov.fourinarow;

import android.util.Log;
//import android.util.Pair;

import com.artemdanilov.fourinarow.Four.Cell;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by artemdanilov
 */
public class Computer {

    public static class Pair<F, S> {
        public final F first;
        public final S second;

        public Pair(F first, S second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Pair<?, ?> pair = (Pair<?, ?>) o;

            return first != null ? first.equals(pair.first) : pair.first == null &&
                    (second != null ? second.equals(pair.second) : pair.second == null);

        }

        @Override
        public String toString() {
            return "first: " + first.toString() +
                    ", second: " + second.toString() +
                    '\n';
        }

        @Override
        public int hashCode() {
            int result = first != null ? first.hashCode() : 0;
            result = 31 * result + (second != null ? second.hashCode() : 0);
            return result;
        }
    }

    static String TAG = "DEBUG";
    private static int DEPTH = 4;

    public static int foundMove(Four mainGame) {
        try{
        Four game = mainGame.clone();

        Four.Player player = game.getCurrentPlayer();

        List<Integer> possible = game.possibleColumns();

        List<Pair<Integer, Integer>> scored = new LinkedList<>();

        for (Integer column : possible) {
            Four.Cell move = game.makeMove(player, column);

            int score = alphaBeta(game, DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
            scored.add(new Pair<>(column, score));
//Log.i(TAG,"column "+column+" score "+score);
            game.unmakeMove(move);
        }
        if (scored.isEmpty())
            return -1;
        int maxScore = scored.get(0).second;
        int bestColumn = scored.get(0).first;

        for (Pair<Integer, Integer> column : scored) {
            if (column.second > maxScore) {
                maxScore = column.second;
                bestColumn = column.first;
            }
        }
        return bestColumn;}catch (Exception e){
          //  Log.i(TAG,e.toString());
            return 0;
        }
    }

    public static void setDEPTH(int depth) {
        DEPTH = depth;
    }

    private static int alphaBeta(Four node, int depth, int α, int β, boolean maximizingPlayer) {
        try {


//            Log.i(TAG, "depth = " + depth);
            if (depth == 0)
                return eval(node, maximizingPlayer);
            if (maximizingPlayer) {
                int v = Integer.MIN_VALUE;

                List<Integer> possible = node.possibleColumns();


                for (Integer column : possible) {
                    Four.Cell move = node.makeMove(column);
                    v = Math.max(v, alphaBeta(node, depth - 1, α, β, false));
                    α = Math.max(α, v);
                    node.unmakeMove(move);
                    if (β <= α)
                        break;
                }
                return v;
            } else {
                int v = Integer.MAX_VALUE;
                List<Integer> possible = node.possibleColumns();
                for (Integer column : possible) {
                    Four.Cell move = node.makeMove(column);
                    v = Math.min(v, alphaBeta(node, depth - 1, α, β, true));
                    β = Math.min(β, v);
                    node.unmakeMove(move);
                    if (β <= α)
                        break;
                }
                return v;

            }
        }catch (Exception e){
        //    Log.i(TAG,e.toString());
            return 0;
        }

    }

    private static int eval(Four node, boolean maxPlayer) {
        if (!maxPlayer) return -eval(node, true);
     //   Log.i(TAG, "Eval");
        int result = 0;
        int width = Four.WIDTH;
        int height = Four.HEIGHT;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Four.Player startPlayer = node.getCell(x, y);
                Four.Cell startCell = new Four.Cell(x, y);
                if(startPlayer==null)
                    continue;
                result += scoreDirection(node, startCell, startPlayer, 0, 1) +
                        scoreDirection(node, startCell, startPlayer, 1, 0) +
                        scoreDirection(node, startCell, startPlayer, 1, 1) +
                        scoreDirection(node, startCell, startPlayer, 1, -1);
            }
        }
        return result;

    }

    private static int scoreDirection(Four node, Four.Cell currentCell, Four.Player player, int xShift, int yShift) {
       // Log.i(TAG, "ScoreDirection");
        int result = 0;
        int winLength = Four.WIN_LENGTH;
        int fx = currentCell.getX() + xShift * (winLength - 1);
        int fy = currentCell.getY() + yShift * (winLength - 1);
        Cell finish = new Cell(fx, fy);
        if (!(fx >= 0 && fx <= Four.WIDTH && fy >= 0 && fy <= Four.HEIGHT)) return 0;
        int whiteChips = 0;
        int blackChips = 0;
        Four.Cell current = currentCell;
        Four.Player chip = node.getCell(current);
        if(chip==null)
            return 0;
        if (chip.equals(Four.Player.WHITE)) {
            whiteChips++;
        } else if (chip.equals(Four.Player.BLACK)) {
            blackChips++;
        }
        while (!current.equals(finish)) {
            int xx = current.getX() + xShift;
            int yy = current.getY() + yShift;
            current = new Four.Cell(xx, yy);
            chip = node.getCell(current);
            if(chip==null)
                break;
            if (chip.equals(Four.Player.WHITE)) {
                whiteChips++;
            } else if (chip.equals(Four.Player.BLACK)) {
                blackChips++;
            }
        }

        if (whiteChips == 0)
            result += evalSequence(blackChips);
        if (blackChips == 0)
            result -= evalSequence(whiteChips);

        return result;
    }

    private static int evalSequence(int length) {
        switch (length) {

            case 1:
                return 1;

            case 2:
                return 10;

            case 3:
                return 500;

            case 4:
                return Integer.MAX_VALUE;

            default:
                return 0;
        }
    }

}


