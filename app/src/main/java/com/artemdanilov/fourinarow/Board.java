package com.artemdanilov.fourinarow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by artemdanilov
 */
public class Board extends View {


    private static final String TAG = "DEBUG";

    private final StartActivity context;

    private final Four game;
    private final Paint holePainter = new Paint();
    private float holeSize;

    public Board(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = (StartActivity) getContext();
        game = new Four();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN)
            press(event);
        return true;
    }

    private void press(MotionEvent event) {

        int x = (int) event.getX();

        int y = (int) event.getY();

        int columnToMove = parseColumn(x, y);

        Log.i(TAG, "column " + columnToMove);

        if (columnToMove == -1)
            return;

        game.makeMove(columnToMove);

        invalidate();

        Four.Player winner = game.winner();

        Log.i(TAG, winner == null ? "winner null" : winner.toString());

        if (winner != null)
            context.showWinner(winner);

    }

    private int parseColumn(int x, int y) {

        int boardRange = getHeight();


        if (x < 0 || x > boardRange || y < 0 || y > boardRange) {
            return -1;
        }

        int c = (int) (x / holeSize);

        return c;


    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Log.i(TAG, "size " + holeSize);
        holePainter.reset();
        holePainter.setColor(Color.YELLOW);
        canvas.drawRect(0, 0, (int) ((Four.WIDTH + 1) * holeSize),
                (int) ((Four.HEIGHT + 1) * holeSize), holePainter);
        for (int x = 0; x <= Four.WIDTH; x++) {
            for (int y = 0; y <= Four.HEIGHT; y++) {
                Four.Player player = game.getCell(x, Four.HEIGHT - y);
                if (player == null) {
                    // Log.i(TAG, "x y payer null" + x + " " + y);
                    holePainter.setColor(Color.GRAY);
                } else if (player == Four.Player.WHITE) {
                    // Log.i(TAG, "x y payer white" + x + " " + y);

                    holePainter.setColor(Color.WHITE);
                } else {
                    // Log.i(TAG, "x y payer black" + x + " " + y);

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
//        int largestPadding = Math.max(Math.max(getPaddingBottom(), getPaddingTop()),
//                Math.max(getPaddingLeft(), getPaddingRight()));


        holeSize = (size /*- 2 * largestPadding*/) / Math.max(Four.HEIGHT + 1, Four.WIDTH + 1);

    }
}
