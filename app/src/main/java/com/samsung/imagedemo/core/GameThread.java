package com.samsung.imagedemo.core;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import com.samsung.imagedemo.model.Ball;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

public class GameThread extends Thread {

    private final SurfaceHolder holder;

    private boolean running;
    private Paint paint;
    private final ConcurrentLinkedDeque<Ball> balls;

    public GameThread(SurfaceHolder holder) {
        this.holder = holder;
        running = true;
        balls = new ConcurrentLinkedDeque<>();
        balls.add(new Ball(200, 200));
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
    }

    private void update(Canvas canvas) {

        canvas.drawColor(Color.WHITE);
        for (Ball ball : balls) {
            canvas.drawCircle(ball.getX(), ball.getY(), ball.getR(), paint);
            ball.update(canvas.getWidth(), canvas.getHeight());
            for (Ball other : balls) {
                if (!ball.equals(other)) {
                    ball.processCollision(other);
                }
            }
        }
    }

    public void stopThread() {
        running = false;
    }

    public void setCoordinates(float x, float y) {
        balls.add(new Ball(x, y));
    }

    @Override
    public void run() {
        while (running) {
            synchronized (holder) {
                Canvas canvas = holder.lockCanvas();
                update(canvas);
                holder.unlockCanvasAndPost(canvas);
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
