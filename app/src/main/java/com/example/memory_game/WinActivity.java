package com.example.memory_game;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.app.services.Requests;

public class WinActivity extends AppCompatActivity {
    private String player_name = "Player";
    private Requests req = new Requests();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);
        ((TextView) findViewById(R.id.pontuacao)).setText(getIntent().getStringExtra("Time") + " seconds");

        req.setMethod(1);
        // Nome default para o jogador
        player_name+=getIntent().getStringExtra("Time") + "S";
        obterDadosJogador();
    }

    public String obterDadosJogador(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String nome;

        builder.setTitle("Gravar Pontuação");
        builder.setMessage("Nickname");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                player_name = input.getText().toString();
                req.execute(String.format("{\"player\": \"%s\", \"punctuation\":%d}", player_name, Integer.parseInt(getIntent().getStringExtra("Time"))));
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        return player_name;
    }

    public void newGame(View view){
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void ranking(View view){
        finish();
        Intent intent = new Intent(this, Ranking.class);
        startActivity(intent);
    }
}
