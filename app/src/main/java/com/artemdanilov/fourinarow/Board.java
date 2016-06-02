package com.artemdanilov.fourinarow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by artemdanilov
 */
public class Board extends View {

    private static final String TAG = "DEBUG";
    private final ExecutorService engineExecutor = Executors.newSingleThreadExecutor();
    private final StartActivity context;
    private final Four game;
    private final Paint holePainter = new Paint();
    private final long delay = 680;
    private final Runnable engineCalculatingTask = new Runnable() {
        @Override
        public void run() {
            try {

                Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                Log.i(TAG,"start calculating");
                int move = Computer.foundMove(game);

                Message msg = (engineResultHandler.obtainMessage());
                Bundle bundle = new Bundle();
                bundle.putInt("calculatedMove", move);
                msg.setData(bundle);
                engineResultHandler.sendMessage(msg);
            } catch (Exception e) {
                Log.i(TAG, e.toString());
            }
        }
    };
    public boolean isEngineCalculate = false;
    private boolean gameEnd = false;
    private final Handler engineResultHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            int calculatedMove = bundle.getInt("calculatedMove");
            Log.i(TAG, "message handled " + calculatedMove);

            makeComputerMove(calculatedMove);
        }
    };
    private float holeSize;
    private boolean playVsComputer = false;
    private long previousClickTime = 0;

    public Board(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = (StartActivity) getContext();
        game = new Four();
    }

    public void setDifficulty(int difficulty) {
        Computer.setDEPTH(difficulty);
    }

    private void makeComputerMove(int move) {


        isEngineCalculate = false;

        Log.i(TAG, "Comp move " + move + " Current player " + game.getCurrentPlayer());

        game.makeMove(move);

        invalidate();

        Four.Player compWinner = game.winner();

        Log.i(TAG, compWinner == null ? "winner null" : compWinner.toString());

        if (compWinner != null) {
            context.showWinner(compWinner);
            gameEnd = true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "touch");
        if (gameEnd)
            return true;
        if (System.currentTimeMillis() - previousClickTime > delay) {
            previousClickTime = System.currentTimeMillis();
            if (isEngineCalculate)
                return true;
            press(event);
        }
        return true;
    }

    private void press(MotionEvent event) {

        int x = (int) event.getX();

        int y = (int) event.getY();

        int columnToMove = parseColumn(x, y);

        //Log.i(TAG, "column " + columnToMove);

        if (columnToMove == -1)
            return;

        Four.Cell moveTo = game.makeMove(columnToMove);

        invalidate();

        if (moveTo != null) {

            Four.Player winner = game.winner();

            Log.i(TAG, winner == null ? "winner null" : winner.toString());

            if (winner != null) {
                gameEnd = true;
                context.showWinner(winner);
            } else {
                if (playVsComputer) {
                    isEngineCalculate = true;
                    engineExecutor.execute(engineCalculatingTask);
                }
            }
        }
    }

    private int parseColumn(int x, int y) {

        int boardWidth = getWidth();
        int boardHeight = getHeight();

        if (x < 0 || x > boardWidth || y < 0 || y > boardHeight) {
            return -1;
        }

        return (int) (x / holeSize);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Log.i(TAG, "size " + holeSize);
        holePainter.reset();
        holePainter.setColor(Color.YELLOW);
        canvas.drawRect(0,0, (int) ((6+1 ) * holeSize),
                (int) ((5+1) * holeSize), holePainter);
        for (int x = 0; x <=6; x++) {
            for (int y = 0; y <=5; y++) {
                Four.Player player = game.getCell(x, 5-y);
                if (player == null) {
                     Log.i(TAG, "x y payer null" + x + " " + y);
                    holePainter.setColor(Color.GRAY);
                } else if (player == Four.Player.WHITE) {
                     Log.i(TAG, "x y payer white" + x + " " + y);

                    holePainter.setColor(Color.WHITE);
                } else {
                     Log.i(TAG, "x y payer black" + x + " " + y);

                    holePainter.setColor(Color.BLACK);
                }

                canvas.drawOval((int) (x * holeSize), (int) ((y) * holeSize), (int) ((x + 1) * holeSize),
                        (int) ((y + 1) * holeSize), holePainter);

            }
        }

    }

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        int size = Math.min(xNew, yNew);
        holeSize = (size) / Math.max(Four.HEIGHT+1 , Four.WIDTH+1);

    }

    public void setPlayer(boolean computer) {
        playVsComputer = computer;
    }
}
