package com.example.calculadoradegorjeta;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText edtValorInicial;
    private TextView txtTotal, txtGorjeta, txtPercent, txtValorTotal, txtValorGorjeta;
    private SeekBar seekPercent;

    private double porcentagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtValorInicial = findViewById(R.id.txtValorInicial);
        txtTotal = findViewById(R.id.txtTotal);
        txtGorjeta = findViewById(R.id.txtGorjeta);
        txtPercent = findViewById(R.id.txtPercent);
        txtValorGorjeta = findViewById(R.id.txtValorGorjeta);
        txtValorTotal = findViewById(R.id.txtValorTotal);
        seekPercent = findViewById(R.id.seekBar2);

        edtValorInicial.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Toast.makeText(getApplicationContext(), "Digitou", Toast.LENGTH_LONG).show();
                return true;
            }
        });

        seekPercent.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                porcentagem = progress;
                txtPercent.setText(Math.round(porcentagem) + " %");
                calcular();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void calcular(){

        String valorInicial = edtValorInicial.getText().toString();
        if(valorInicial == null || valorInicial.equals("")){
            Toast.makeText(getApplicationContext(), "Digite um valor primeiro!", Toast.LENGTH_SHORT).show();
        } else {
            double valorDigitado = Double.parseDouble(valorInicial);

            double gorjeta = valorDigitado * (porcentagem/100);

            txtValorGorjeta.setText("R$ " + Math.round(gorjeta));
            txtValorTotal.setText("R$ " + Math.round(valorDigitado + gorjeta));
        }
    }

}