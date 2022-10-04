package com.example.caraoucoroa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultadoActivity extends AppCompatActivity {

    private TextView txtResultado;
    private ImageView imageResultado;
    private Button btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        txtResultado = findViewById(R.id.txtResultado);
        imageResultado = findViewById(R.id.imgResultado);
        btnVoltar = findViewById(R.id.btnVoltar);

        Bundle dados = getIntent().getExtras();
        int numero = dados.getInt("numero");

        if(numero == 0){
            imageResultado.setImageResource(R.drawable.moeda_cara);
            txtResultado.setText("Cara!");
        } else {
            imageResultado.setImageResource(R.drawable.moeda_coroa);
            txtResultado.setText("Coroa!");
        }

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
    }

}