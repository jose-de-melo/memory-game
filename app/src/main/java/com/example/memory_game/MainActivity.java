package com.example.memory_game;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Integer> listaNumeros;
    private int posicaoAtual = 1;
    private int[] colorsButtons;
    private String initialTime = null, endTime = null;

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
        initialTime = null;
        endTime = null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        shuffleSeq();
        Log.d("SEQUENCIA", listaNumeros.toString());
        initialTime = getTime();
    }

    private void gerarSequencia(){
        listaNumeros = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
           listaNumeros.add(i);
        }
    }

    private void shuffleSeq(){
        Collections.shuffle(listaNumeros);
        Log.d("SEQUENCIA", listaNumeros.toString());
    }

    public void analisarJogada(View view){
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.initialWindow);
        Button button = (Button) view;
        ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar);
        int valorBtn = Integer.parseInt(button.getText().toString());

        if(valorBtn == listaNumeros.get(posicaoAtual - 1)){
            button.setVisibility(View.INVISIBLE);
            layout.setBackgroundColor(colorsButtons[valorBtn - 1]);
            playSound(valorBtn);
            bar.setProgress((102/6)*posicaoAtual);
            posicaoAtual++;
        }
        else{
            layout.setBackgroundColor(Color.WHITE);
            showButtons();
            bar.setProgress(0);
            posicaoAtual = 1;
            buildVibration();

        }

        if(posicaoAtual == 7){
            endTime = getTime();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finish();
            Intent intent = new Intent(this, WinActivity.class);
            intent.putExtra("Time", calculateTime(initialTime, endTime));
            startActivity(intent);
            playSound(0);
        }
    }

    public void restartGame(View view){
        ((ConstraintLayout) findViewById(R.id.initialWindow)).setBackgroundColor(Color.WHITE);
        showButtons();
        ((ProgressBar) findViewById(R.id.progressBar)).setProgress(0);
        posicaoAtual = 1;
        shuffleSeq();
        initialTime = getTime();

    }

    public void showButtons(){
        ((Button)findViewById(R.id.b1)).setVisibility(View.VISIBLE);
        ((Button)findViewById(R.id.b2)).setVisibility(View.VISIBLE);
        ((Button)findViewById(R.id.b3)).setVisibility(View.VISIBLE);
        ((Button)findViewById(R.id.b4)).setVisibility(View.VISIBLE);
        ((Button)findViewById(R.id.b5)).setVisibility(View.VISIBLE);
        ((Button)findViewById(R.id.b6)).setVisibility(View.VISIBLE);
    }

    public void playSound(int numberButton){

        MediaPlayer mediaPlayer = criarPlayer(numberButton);
        mediaPlayer.start();

    }

    public MediaPlayer criarPlayer(int numberButton){
        MediaPlayer mediaPlayer;

        switch (numberButton){
            case 0:
                mediaPlayer = MediaPlayer.create(this, R.raw.win);
                break;
            case 1:
                mediaPlayer = MediaPlayer.create(this, R.raw.sound1);
                break;
            case 2:
                mediaPlayer = MediaPlayer.create(this, R.raw.sound2);
                break;
            case 3:
                mediaPlayer = MediaPlayer.create(this, R.raw.sound3);
                break;
            case 4:
                mediaPlayer = MediaPlayer.create(this, R.raw.sound4);
                break;
            case 5:
                mediaPlayer = MediaPlayer.create(this, R.raw.sound5);
                break;
            case 6:
                mediaPlayer = MediaPlayer.create(this, R.raw.sound6);
                break;
            default:
                mediaPlayer = null;
                break;
        }

        return mediaPlayer;
    }

    public void buildVibration(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(500);
        }
    }


    public String getTime(){
        SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm:ss");
        Date data = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        Date data_atual = cal.getTime();
        String hora_atual = dateFormat_hora.format(data_atual);
        Log.d("hora_atual", hora_atual);
        return hora_atual;
    }

    public String calculateTime(String initialDate, String finalDate){
        int time = 0;

        String[] breakDateI =  initialDate.split(":"), breakDateF = finalDate.split(":");

        time += (Integer.parseInt(breakDateF[0]) - Integer.parseInt(breakDateI[0])) * 3600;
        time += (Integer.parseInt(breakDateF[1]) - Integer.parseInt(breakDateI[1])) * 60;
        time += (Integer.parseInt(breakDateF[2]) - Integer.parseInt(breakDateI[2]));

        Log.d("TIMEHOUR", String.valueOf(time));

        return String.valueOf(time);
    }
}
