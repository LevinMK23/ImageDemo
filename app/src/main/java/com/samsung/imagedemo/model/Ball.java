package com.samsung.imagedemo.model;

public class Ball {

    private float x;
    private float y;
    private final float r;
    private float dx;
    private float dy;

    public Ball(float x, float y) {
        this.x = x;
        this.y = y;
        r = 20;
        dx = 10;
        dy = 15;
    }

    public void processCollision(Ball ball) {
        float distance = (float) Math.sqrt(
                (x - ball.x) * (x - ball.x) +
                        (y - ball.y) * (y - ball.y)
        );
        if (distance <= 2 * r + 1) {
            dx *= -1;
            dy *= -1;
            ball.dx *= -1;
            ball.dy *= -1;
        }
    }

    public float getR() {
        return r;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void update(int width, int height) {

        x += dx;
        y += dy;

        if (x + r >= width || x - r <= 0) {
            dx *= -1;
        }
        if (y + r >= height || y - r <= 0) {
            dy *= -1;
        }

    }
}
