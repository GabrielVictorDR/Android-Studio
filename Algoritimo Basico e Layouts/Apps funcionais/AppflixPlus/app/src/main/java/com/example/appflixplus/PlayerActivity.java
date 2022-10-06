package com.example.appflixplus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

public class PlayerActivity extends AppCompatActivity {

    private VideoView videoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        videoPlayer = findViewById(R.id.videoView);

        // Esconder a ActionBar
        getSupportActionBar().hide();

//        View decorView = getWindow().getDecorView();
//        int uiOpcoes = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//
//        decorView.setSystemUiVisibility(uiOpcoes);

        // Executar o vídeo
        videoPlayer.setMediaController( new MediaController(this) );
        videoPlayer.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.video);
        videoPlayer.start();
    }
}