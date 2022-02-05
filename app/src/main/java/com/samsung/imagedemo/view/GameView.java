package com.samsung.imagedemo.view;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import com.samsung.imagedemo.core.GameThread;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    private final GameThread thread;

    public GameView(Context context) {
        super(context);
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        setOnTouchListener(this);
        thread = new GameThread(surfaceHolder);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        // start thread
        thread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        // kill thread
        try {
            thread.stopThread();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        thread.setCoordinates(motionEvent.getX(), motionEvent.getY());
        return false;
    }
}
