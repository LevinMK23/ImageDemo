package com.samsung.imagedemo.core;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import com.samsung.imagedemo.model.Ball;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

public class GameThread extends Thread {

    private final SurfaceHolder holder;

    private float bulletSpeed;
    private float enemySpeed;
    private boolean running;
    private Paint heroPaint;
    private Paint bulletPaint;
    private Paint enemyPaint;
    private final Ball hero;
    private final ConcurrentLinkedDeque<Ball> enemies;
    private final ConcurrentLinkedDeque<Ball> bullets;
    private float width;
    private float height;
    private EnemiesThread enemiesThread;

    public GameThread(SurfaceHolder holder) {
        this.holder = holder;
        float heroRadius = 100;
        bulletSpeed = 10;
        enemySpeed = 2;
        float heroX = 540;
        float heroY = 1200 + heroRadius;
        running = true;
        enemies = new ConcurrentLinkedDeque<>();
        bullets = new ConcurrentLinkedDeque<>();
        hero = new Ball(heroX, heroY, heroRadius, 0, 0);

        heroPaint = new Paint();
        heroPaint.setColor(Color.RED);
        heroPaint.setStyle(Paint.Style.FILL);

        enemyPaint = new Paint();
        enemyPaint.setColor(Color.BLACK);
        enemyPaint.setStyle(Paint.Style.FILL);

        bulletPaint = new Paint();
        bulletPaint.setColor(Color.BLUE);
        bulletPaint.setStyle(Paint.Style.FILL);

        enemiesThread = new EnemiesThread(hero, this);
        enemiesThread.setDaemon(true);
        enemiesThread.start();

        setDaemon(true);
    }

    private void update(Canvas canvas) {

        canvas.drawColor(Color.WHITE);
        for (Ball ball : bullets) {
            canvas.drawCircle(ball.getX(), ball.getY(), ball.getR(), bulletPaint);
            ball.update(canvas.getWidth(), canvas.getHeight());
            if (!ball.isInScreen(canvas.getWidth(), canvas.getHeight())) {
                bullets.remove(ball);
            }
        }

        for (Ball ball : enemies) {
            canvas.drawCircle(ball.getX(), ball.getY(), ball.getR(), enemyPaint);
            ball.update(canvas.getWidth(), canvas.getHeight());
            if (ball.getY() > ball.getR()) {
                if (!ball.isInScreen(canvas.getWidth(), canvas.getHeight())) {
                    bullets.remove(ball);
                }
            }
        }

        for (Ball enemy : enemies) {
            for (Ball bullet : bullets) {
                if (enemy.isCollated(bullet)) {
                    enemies.remove(enemy);
                    bullets.remove(bullet);
                }
            }
        }

        canvas.drawCircle(hero.getX(), hero.getY(), hero.getR(), heroPaint);
    }

    public void stopThread() {
        running = false;
    }

    public void addEnemy(Ball ball) {
        enemies.add(ball);
    }

    public void shot(float x, float y) {
        float bx = hero.getX();
        float by = hero.getY();
        float br = 5;
        float dx = x - bx;
        float dy = y - by;
        float hypo = (float) Math.sqrt(dx * dx + dy * dy);
        dx = bulletSpeed * dx / hypo;
        dy = bulletSpeed * dy / hypo;
        Ball ball = new Ball(bx, by, br, dx, dy);
        bullets.add(ball);
    }

    public void setCoordinates(float x, float y) {
       // balls.add(new Ball(x, y));
    }

    @Override
    public void run() {
        while (running) {
            synchronized (holder) {
                Canvas canvas = holder.lockCanvas();
                width = canvas.getWidth();
                height = canvas.getHeight();
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
