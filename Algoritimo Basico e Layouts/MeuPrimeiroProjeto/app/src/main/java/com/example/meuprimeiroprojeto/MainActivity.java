package com.example.meuprimeiroprojeto;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

//    private int status;

//    public void alterarTextoExibicao(View view){
//
//
//        TextView texto = findViewById(R.id.txtExibicao);
//
//
//        switch (status){
//            case 0:
//                texto.setText("Mudou!, Vivaaaaa!!!!!!!!!!!");
//                status = 1;
//                break;
//            case 1:
//                texto.setText("Mudou de Novo");
//                status = 2;
//                break;
//            case 2:
//                texto.setText("Clique Abaixo");
//                status = 0;
//                break;
//        }
//
//
//    }

    public void sortearNumero(View view){

        TextView txtResultado = findViewById(R.id.txtResultadoSorteio);
        int nro = new Random().nextInt(11);
        txtResultado.setText("O número sorteado foi: " + nro);
        txtResultado.setVisibility(1);

    }


}