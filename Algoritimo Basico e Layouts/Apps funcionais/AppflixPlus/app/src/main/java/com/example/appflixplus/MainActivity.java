package com.example.appflixplus;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.Gravity.TOP;

public class MainActivity extends AppCompatActivity {

    private TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt = findViewById(R.id.textView);
    }

    public void abrirVideo(View view) {
        startActivity(new Intent(this, PlayerActivity.class));
    }

    public void salvarNaPlaylist(View view){
        Toast toast = Toast.makeText(getApplicationContext(), "Salvo na Playlist Principal", Toast.LENGTH_SHORT);
        toast.setGravity(TOP, 0, 165);
        toast.show();
    }
}