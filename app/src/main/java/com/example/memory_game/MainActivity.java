package com.example.memory_game;

import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Integer> listaNumeros;
    private int posicaoAtual = 1;
    private int[] colorsButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gerarSequencia();
        colorsButtons = new int[]{
                getResources().getColor(R.color.colorButton1),
                getResources().getColor(R.color.colorButton2),
                getResources().getColor(R.color.colorButton3),
                getResources().getColor(R.color.colorButton4),
                getResources().getColor(R.color.colorButton5),
                getResources().getColor(R.color.colorButton6)
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        shuffleSeq();
        Log.d("SEQUENCIA", listaNumeros.toString());
    }

    private void gerarSequencia(){
        listaNumeros = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
           listaNumeros.add(i);
        }
    }

    private void shuffleSeq(){
        Collections.shuffle(listaNumeros);
    }

    public void analisarJogada(View view){
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.initialWindow);
        Button button = (Button) view;
        ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar);
        int valorBtn = Integer.parseInt(button.getText().toString());

        if(valorBtn == listaNumeros.get(posicaoAtual - 1)){
            button.setVisibility(View.INVISIBLE);
            layout.setBackgroundColor(colorsButtons[valorBtn - 1]);
            bar.setProgress((102/6)*posicaoAtual);
            posicaoAtual++;
        }
        else{
            layout.setBackgroundColor(Color.WHITE);
            showButtons();
            bar.setProgress(0);
            posicaoAtual = 1;


        }

        if(posicaoAtual == 7){
            finish();
            Intent intent = new Intent(this, WinActivity.class);
            overridePendingTransition(0, 0);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            overridePendingTransition(0, 0);
            startActivity(intent);
        }
    }

    public void restartGame(View view){
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    public void showButtons(){
        ((Button)findViewById(R.id.b1)).setVisibility(View.VISIBLE);
        ((Button)findViewById(R.id.b2)).setVisibility(View.VISIBLE);
        ((Button)findViewById(R.id.b3)).setVisibility(View.VISIBLE);
        ((Button)findViewById(R.id.b4)).setVisibility(View.VISIBLE);
        ((Button)findViewById(R.id.b5)).setVisibility(View.VISIBLE);
        ((Button)findViewById(R.id.b6)).setVisibility(View.VISIBLE);
    }

    public void loadViewWinGame(){}
}
