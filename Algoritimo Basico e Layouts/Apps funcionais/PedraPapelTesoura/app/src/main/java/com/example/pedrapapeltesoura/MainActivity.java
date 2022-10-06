package com.example.pedrapapeltesoura;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void selecionarPedra(View view) {
        this.janKenPo("pedra");
    }

    public void selecionarPapel(View view) {
        this.janKenPo("papel");
    }

    public void selecionarTesoura(View view) {
        this.janKenPo("tesoura");
    }

    private int i;
    private int j;

    public void janKenPo(String escolhaUsuario) {

        ImageView imagemResult = findViewById(R.id.imgPadrao);
        TextView txtResult = findViewById(R.id.txtResultado);
        TextView txtVenceu = findViewById(R.id.txtContadorVitorias);
        TextView txtPerdeu = findViewById(R.id.txtContadorDerrotas);

        int numero = new Random().nextInt(3);
        String[] opcoes = {"pedra", "papel", "tesoura"};
        String escolhaApp = opcoes[numero];

        switch (escolhaApp) {
            case "pedra":
                imagemResult.setImageResource(R.drawable.pedra);
                break;
            case "papel":
                imagemResult.setImageResource(R.drawable.papel);
                break;
            case "tesoura":
                imagemResult.setImageResource(R.drawable.tesoura);
                break;
        }

        if ((escolhaUsuario.equals("pedra") && escolhaApp.equals("papel"))
                || (escolhaUsuario.equals("papel") && escolhaApp.equals("tesoura"))
                || (escolhaUsuario.equals("tesoura") && escolhaApp.equals("pedra"))) { // App Ganhador
            i++;
            txtResult.setText(" Você Perdeu!");
            txtPerdeu.setText("Perdeu: " + i);

        } else if ((escolhaApp.equals("pedra") && escolhaUsuario.equals("papel"))
                || (escolhaApp.equals("papel") && escolhaUsuario.equals("tesoura"))
                || (escolhaApp.equals("tesoura") && escolhaUsuario.equals("pedra"))) { //Usuário Ganhador
            j++;
            txtResult.setText(" Você Ganhou! :)");
            txtVenceu.setText("Ganhou: " + j);

        } else { // Empate
            txtResult.setText("Parece que empatou!");
        }
    }
}