package com.example.tarefasapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tarefasapp.R;
import com.example.tarefasapp.helper.TarefaDAO;
import com.example.tarefasapp.model.Tarefa;
import com.google.android.material.textfield.TextInputEditText;

public class AddTarefaActivity extends AppCompatActivity {

    private TextInputEditText txtTarefa;
    private Tarefa tarefaAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tarefa);

        txtTarefa = findViewById(R.id.txtAddTarefa);

        tarefaAtual = (Tarefa) getIntent().getSerializableExtra(
                "tarefaSelecionada"
        );

        if (tarefaAtual != null) {
            txtTarefa.setText(tarefaAtual.getNome());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_tarefa, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.itemSalvar:
                // Executa ação para o item salvar

                TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());

                if (tarefaAtual != null) {

                    String nomeTarefa = txtTarefa.getText().toString();
                    if(!nomeTarefa.isEmpty()){
                        Tarefa tarefa = new Tarefa();
                        tarefa.setNome(nomeTarefa.toString());
                        tarefa.setId(tarefaAtual.getId());

                        //atualizar no banco de dados
                        if(tarefaDAO.atualizar(tarefa)){
                            this.finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Falha", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {

                    if (!txtTarefa.getText().toString().isEmpty()) {

                        Tarefa tarefa = new Tarefa();
                        tarefa.setNome(txtTarefa.getText().toString());

                        if (tarefaDAO.salvar(tarefa)) {
                            Toast.makeText(getApplicationContext(), "Sucesso ao salvar tarefa.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Falha ao salvar tarefa.", Toast.LENGTH_SHORT).show();
                        }
                        finish();

                    }
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}