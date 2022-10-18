package com.example.sharedpreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button salvar;
    private EditText entrada;
    private TextView saida;
    private static final String ARQUIVO_PREFERENCIA = "ArquivoPreferencia";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        salvar = findViewById(R.id.btnSalvar);
        entrada = findViewById(R.id.txtEntrada);
        saida = findViewById(R.id.txtNome);

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);
                SharedPreferences.Editor editor = preferences.edit();

                if(entrada.getText().toString().equals("") || entrada.getText().toString() == null) {
                    Toast.makeText(getApplicationContext(), "Preencha o nome", Toast.LENGTH_SHORT).show();
                } else {
                    String nome = entrada.getText().toString();
                    editor.putString("nome", nome);
                    editor.commit();
                    saida.setText("Olá " + nome);
                }

            }
        });

        SharedPreferences preferences = getSharedPreferences(ARQUIVO_PREFERENCIA, 0);

        if( preferences.contains("nome")) {
            String nome = preferences.getString("nome", "usuário não definido");
            saida.setText("Olá " + nome);
        } else {
            saida.setText("Olá, usuário não definido");
        }

    }
}