package com.app.models;

public class PositionRanking {
    private String name;
    private int pontuaction, position;

    public PositionRanking(String name, int pontuaction, int position) {
        this.name = name;
        this.pontuaction = pontuaction;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPontuaction() {
        return pontuaction;
    }

    public void setPontuaction(int pontuaction) {
        this.pontuaction = pontuaction;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return String.format("%dº - %d seconds : %s", getPosition(), getPontuaction(), getName());
    }
}
