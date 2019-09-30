package com.app.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.models.PositionRanking;
import com.example.memory_game.R;

import java.util.Comparator;
import java.util.List;

public class RankingAdapterList extends ArrayAdapter<PositionRanking> {

    private Context context;
    private List<PositionRanking> positions;

    public RankingAdapterList(Context context, int resource, List<PositionRanking> positions) {
        super(context, resource, positions);
        this.context = context;
        this.positions = positions;
    }

    @Override
    public View getView(int indice, View convertView, ViewGroup parent) {
        PositionRanking pos = this.positions.get(indice);
        convertView = LayoutInflater.from(this.context).inflate(R.layout.item_list_ranking, null);


        TextView nomeUsuarioText = (TextView) convertView.findViewById(R.id.nome_usuario_text);
        TextView pontuacaoText = (TextView) convertView.findViewById(R.id.pontos_text);
        TextView posText = (TextView) convertView.findViewById(R.id.position_text);

        posText.setText(String.format("%dº", pos.getPosition()));
        nomeUsuarioText.setText(pos.getName());
        pontuacaoText.setText(String.format("%d s", pos.getPontuaction()));
        return convertView;
    }
}
