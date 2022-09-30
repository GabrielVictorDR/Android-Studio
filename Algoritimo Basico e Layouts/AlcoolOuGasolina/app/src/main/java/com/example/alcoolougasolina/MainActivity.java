package com.example.alcoolougasolina;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {
    private TextInputEditText editPrecoAlcool, editPrecoGasolina;
    private TextView textResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editPrecoAlcool = findViewById(R.id.txtValorAlcool);
        editPrecoGasolina = findViewById(R.id.txtValorGasolina);
        textResultado = findViewById(R.id.txtResultado);
    }

    public void calcularPreco(View view){
        String vGas = editPrecoGasolina.getText().toString();
        String vAlc = editPrecoAlcool.getText().toString();

        if( validarCampos(vAlc, vGas) ){

            if( Double.parseDouble(vAlc) / Double.parseDouble(vGas) >= 0.7 ){
                textResultado.setText("É melhor utilizar gasolina!");
            } else {
                textResultado.setText("É melhor utilizar alcool!");
            }

        } else {
            textResultado.setText("Preencha todos os Campos");
        }

    }

    public Boolean validarCampos(String pAlcool, String pGasoliina){

        if( (pAlcool == null || pAlcool.equals("")) || (pGasoliina == null || pGasoliina.equals("")) ){
            return false;
        } else {
            return true;
        }

    }

    public void limparTela(View view){
        editPrecoGasolina.setText("");
        editPrecoAlcool.setText("");
        textResultado.setText("Aguardando valores...");
    }

    public void limparFoco(View view){
        editPrecoAlcool.clearFocus();
        editPrecoGasolina.clearFocus();
    }

}