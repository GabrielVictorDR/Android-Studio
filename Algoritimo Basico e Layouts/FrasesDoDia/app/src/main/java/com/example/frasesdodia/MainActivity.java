package com.example.frasesdodia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    int i;

    public void GerarFraseDoDia(View view) {
        TextView txt = findViewById(R.id.txtFrase);
        TextView txtcount = findViewById(R.id.txtContadorDeFrases);

        String[] frases = {
                "O importante não é vencer todos os dias, mas lutar sempre.",
                "Maior que a tristeza de não haver vencido é a vergonha de não ter lutado!",
                "Quem ousou conquistar e saiu pra lutar, chega mais longe!",
                "É melhor conquistar a si mesmo do que vencer mil batalhas.",
                "A persistência é o caminho do êxito.",
                "O sucesso nasce do querer, da determinação e persistência em se chegar a um objetivo. Mesmo não atingindo o alvo, quem busca e vence obstáculos, no mínimo fará coisas admiráveis.",
                "A esperança é o sonho do homem acordado."
        };

        int campo = new Random().nextInt(frases.length);

        if(txt.getText().equals(frases[campo])){
            txtcount.setText("Frases repetidas não somam ao contador!");
            i--;
        } else {
            txt.setText(frases[campo]);
            i++;
            txtcount.setText("Você mudou de frase: " + i + " vezes.");
        }
    }


    public void ResetarTxtFrase(View view) {

        TextView txt = findViewById(R.id.txtFrase);
        TextView txtcound = findViewById(R.id.txtContadorDeFrases);
        i = 0;
        txtcound.setText("Você já gerou: 0 frases.");
        txt.setText("Você ainda não gerou uma Frase!");

    }
}