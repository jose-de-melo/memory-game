package com.example.memory_game;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.services.Requests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Integer> listaNumeros;
    private int posicaoAtual = 1;
    private int[] colorsButtons;
    private int hoursElapsed = 0, minutesElapsed = 0, secondsElapsed = 0;
    private int recorde, recordeAtual;
    private SharedPreferences myPreferences;
    private SharedPreferences.Editor myEditor;
    private boolean firstButtonClicked = false;
    private final Handler att = new Handler();
    private Requests reqs = new Requests();
    private String player_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Memory Game");
        }
        toolbar.inflateMenu(R.menu.main_menu);

        gerarSequencia();
        colorsButtons = new int[]{
                getResources().getColor(R.color.colorButton1),
                getResources().getColor(R.color.colorButton2),
                getResources().getColor(R.color.colorButton3),
                getResources().getColor(R.color.colorButton4),
                getResources().getColor(R.color.colorButton5),
                getResources().getColor(R.color.colorButton6)
        };

        hoursElapsed = minutesElapsed = secondsElapsed = 0;

    }

    private void gravarRecorde(){
        myEditor.putInt("RECORDE", recordeAtual);
        myEditor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_ranking:
                ranking(getCurrentFocus());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void attTime(boolean clear){
        TextView timer = (TextView) findViewById(R.id.time);

        if(clear){
            hoursElapsed = minutesElapsed = secondsElapsed = 0;
            timer.setText(String.format("%02d", hoursElapsed) + ":" + String.format("%02d", minutesElapsed) + ":" + String.format("%02d", secondsElapsed));
        }
        else {
            secondsElapsed++;
            if (secondsElapsed == 60) {
                secondsElapsed = 0;
                minutesElapsed++;
                if (minutesElapsed == 60) {
                    minutesElapsed = 0;
                    hoursElapsed++;
                    if (hoursElapsed == 24) {
                        restartGame(getCurrentFocus());
                    }
                }
            }
            timer.setText(String.format("%02d", hoursElapsed) + ":" + String.format("%02d", minutesElapsed) + ":" + String.format("%02d", secondsElapsed));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        shuffleSeq();
        Log.d("SEQUENCIA", listaNumeros.toString());
        myPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        myEditor = myPreferences.edit();

        recorde = recordeAtual = myPreferences.getInt("RECORDE", 0);
        Log.d("RECORDE", String.valueOf(recorde));
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
        if(!firstButtonClicked){
            att.post(new Runnable() {
                @Override
                public void run() {
                    att.postDelayed(this, 1000);
                    attTime(false);
                }
            });
            firstButtonClicked = true;
        }
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
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            finish();
            Intent intent = new Intent(this, WinActivity.class);
            int time =  calculateTime();
            if(time < recordeAtual || recordeAtual == 0) {
                recordeAtual = time;
                gravarRecorde();
                intent.putExtra("NEWRECORD", "1");
            }
            else{
                intent.putExtra("NEWRECORD", "0");
            }
            intent.putExtra("Time", String.valueOf(time));
            intent.putExtra("RECORD", String.valueOf(recordeAtual));
            startActivity(intent);
            playSound(0);
        }
    }

    public void restartGame(View view){
        attTime(true);
        ((ConstraintLayout) findViewById(R.id.initialWindow)).setBackgroundColor(Color.WHITE);
        showButtons();
        ((ProgressBar) findViewById(R.id.progressBar)).setProgress(0);
        posicaoAtual = 1;
        shuffleSeq();
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

    public int calculateTime(){
        int time = 0;

        time += hoursElapsed * 3600;
        time += minutesElapsed * 60;
        time += secondsElapsed;

        return time;
    }

    public void ranking(View view){
        Intent intent = new Intent(this, Ranking.class);
        startActivity(intent);
    }
}
