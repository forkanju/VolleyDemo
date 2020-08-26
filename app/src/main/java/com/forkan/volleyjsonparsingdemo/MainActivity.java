package com.forkan.volleyjsonparsingdemo;


import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Song> songs;
    private static String JSON_URL = "http://starlord.hackerearth.com/studio";
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        songs = new ArrayList<>();

        Log.e("TAG1", "onCreate Called!");

        extractSongs();
    }

    private void extractSongs() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject songObject = response.getJSONObject(i);

                        Log.e("TAG1", "Response "+ response);

                        Song song = new Song();
                        song.setTitle(songObject.getString("song").toString());
                        song.setArtists(songObject.getString("artists".toString()));
                        song.setCoverImage(songObject.getString("cover_image"));
                        song.setSongURL(songObject.getString("url"));
                        songs.add(song);



                    } catch (JSONException e) {
                        e.printStackTrace();

                        Log.e("TAG1", "Response "+ e.getMessage());
                    }
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter = new Adapter(getApplicationContext(), songs);
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("tag", "onErrorResponse: " + error.getMessage());
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);

    }
}
