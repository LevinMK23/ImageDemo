package com.samsung.imagedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.samsung.imagedemo.view.GameView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GameView(this));
    }
}