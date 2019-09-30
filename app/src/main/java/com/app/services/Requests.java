package com.app.services;

import android.os.AsyncTask;

import java.io.IOException;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Requests extends AsyncTask<String, Void, String> {
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private int method = 0; // 0 = get, 1 = post

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public String httpGet(String url){
        try{
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
            return "Erro na conexão!";
        }
    }

    public String post(String json) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url("https://game-serv.herokuapp.com/api/rank")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return "Erro ao postar a nova pontuação!";
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        if(method == 0)
            return httpGet("https://game-serv.herokuapp.com/api/rank/5");
        else{
            return post(strings[0]);
        }

    }


}
