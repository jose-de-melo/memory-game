package com.example.memory_game;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class WinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);
        ((TextView) findViewById(R.id.pontuacao)).setText("Time: " + getIntent().getStringExtra("Time") + " seconds");
    }

    public void newGame(View view){
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
