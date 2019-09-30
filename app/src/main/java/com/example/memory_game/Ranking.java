package com.example.memory_game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.app.models.PositionRanking;
import com.app.services.Requests;
import com.app.util.RankingAdapterList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class Ranking extends AppCompatActivity {

    private Requests req = new Requests();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ListView list =  (ListView) findViewById(R.id.list);
        list.setAdapter(new RankingAdapterList(this, 0, getRanking()));
    }

    public List<PositionRanking> getRanking(){
        String jsonString = "";

        List<PositionRanking> positionRankings = new ArrayList<PositionRanking>();
        PositionRanking position;


        try {
            jsonString = req.execute("").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            JSONArray rankingList = new JSONArray(jsonString);
            JSONObject pos;

            for(int i =0; i < 5 && i < rankingList.length(); i++){
                pos = rankingList.getJSONObject(i);

                position = new PositionRanking(pos.getString("player_name"), pos.getInt("punctuation"), i + 1);
                positionRankings.add(position);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return positionRankings;
    }

    public void backMainPage(View view){
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
