package com.samsung.imagedemo.core;

import com.samsung.imagedemo.model.Ball;

public class EnemiesThread extends Thread {

    private volatile boolean running;
    private final Ball hero;
    private final GameThread gameThread;
    private long sleep;

    public EnemiesThread(
            Ball hero,
            GameThread gameThread
    ) {
        this.hero = hero;
        this.gameThread = gameThread;
        running = true;
        sleep = 500;
    }

    @Override
    public void run() {
        while (running) {
            float ex = (float) (Math.random() * 3000);
            float ey = -100;
            float er = 20;
            float dx = hero.getX() - ex;
            float dy = hero.getY() - ey;
            float hypo = (float) Math.sqrt(dx * dx + dy * dy);
            dx = 3 * dx / hypo;
            dy = 3 * dy / hypo;
            Ball enemy = new Ball(ex, ey, er, dx, dy);
            gameThread.addEnemy(enemy);
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
