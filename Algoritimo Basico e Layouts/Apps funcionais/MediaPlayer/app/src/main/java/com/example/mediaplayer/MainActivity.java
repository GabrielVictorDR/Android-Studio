package com.example.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView txtVolume;
    private Button btnStop, btnPlay, btnPause, btnSkip;
    private SeekBar volume;
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // BUTTONS:

        btnPlay = findViewById(R.id.btnReproduzirMusica);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mediaPlayer != null) {
                    mediaPlayer.start();
                    btnPlay.setVisibility(View.INVISIBLE);
                    btnPause.setVisibility(View.VISIBLE);
                    Toast toast = Toast.makeText(getApplicationContext(), "Reproduzindo", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 160);
                    toast.show();
                }

            }

        });

        btnPause = findViewById(R.id.btnPausarMusica);
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    btnPlay.setVisibility(View.VISIBLE);
                    btnPause.setVisibility(View.INVISIBLE);
                    Toast toast = Toast.makeText(getApplicationContext(), "Pausado", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 160);
                    toast.show();
                }

            }

        });

        btnStop = findViewById(R.id.btnFinalizarMusica);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    btnPlay.setVisibility(View.VISIBLE);
                    btnPause.setVisibility(View.INVISIBLE);
                    Toast toast = Toast.makeText(getApplicationContext(), "Faixa Removida", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 160);
                    toast.show();
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.teste);
                }

            }
        });

        btnSkip = findViewById(R.id.btnPularMusica);

        inicializarSeekBar();
    }

    private void inicializarSeekBar() {

        volume = findViewById(R.id.seekBarVolume);
        txtVolume = findViewById(R.id.txtPorcentagemVolume);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.teste);


        // Configura o audio manager
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // Recupera o volume máximo e atual;
        int volumeMaximo = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int volumeAtual = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        // Passa o valor maximo da SeekBar;
        volume.setMax(volumeMaximo);

        // Configura o progresso atual da SeekBar;
        volume.setProgress(volumeAtual);
        txtVolume.setText("Volume (" + volumeAtual + ")");


        volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @SuppressLint("ResourceAsColor")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                txtVolume.setText("Volume (" + progress + ")");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}