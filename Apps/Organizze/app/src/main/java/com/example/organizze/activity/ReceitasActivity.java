package com.example.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizze.R;
import com.example.organizze.config.ConfiguracaoFirebase;
import com.example.organizze.helper.Base64Custom;
import com.example.organizze.helper.DateCustom;
import com.example.organizze.model.Movimentacao;
import com.example.organizze.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReceitasActivity extends AppCompatActivity {

    private EditText campoValor;
    private TextInputEditText campoData, campoCategoria, campoDescricao;

    private FirebaseAuth mAuth = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();

    private Movimentacao movimentacao;

    private Double receitaGerada;
    private Double receitaTotal;
    private Double receitaAtualizada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);

        campoValor = findViewById(R.id.editValorRecieta);
        campoData = findViewById(R.id.editDataReceita);
        campoCategoria = findViewById(R.id.editCategoriaReceita);
        campoDescricao = findViewById(R.id.editDescricaoReceita);

        campoData.setText(DateCustom.dataAtual());
        recuperarReceitaAtual();
    }

    public void salvarReceita(View view){

        if(validarCamposReceita() == true){

            movimentacao = new Movimentacao();

            receitaGerada = Double.parseDouble( campoValor.getText().toString() );

            movimentacao.setValor(receitaGerada);
            movimentacao.setCategoria( campoCategoria.getText().toString() );
            movimentacao.setDescricao( campoDescricao.getText().toString() );
            movimentacao.setData( campoData.getText().toString() );
            movimentacao.setTipo( "r" );

            receitaAtualizada = receitaTotal + receitaGerada;

            atualizarReceita(receitaAtualizada);
            movimentacao.salvar( campoData.getText().toString() );
        }

    }

    private void recuperarReceitaAtual(){

        /*
         *
         *   CONVERTER EMAIL EM B64 -> USAR COMO CHILD PARA ENCONTRAR O BANCO;
         *
         */

        String email = mAuth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(email);
        DatabaseReference usuarioRef = mDataBase.child("usuarios").child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                receitaTotal = usuario.getReceitaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void atualizarReceita(Double receita){
        String email = mAuth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(email);
        DatabaseReference usuarioRef = mDataBase.child("usuarios").child(idUsuario);

        usuarioRef.child("receitaTotal").setValue(receita);
    }

    public boolean validarCamposReceita(){

        if(!campoValor.getText().toString().isEmpty()){
            if (!campoData.getText().toString().isEmpty()){
                if(!campoCategoria.getText().toString().isEmpty()){
                    if(!campoDescricao.getText().toString().isEmpty()){


                        return true;


                    }else {
                        Toast.makeText(getApplicationContext(), "Você se esqueceu da Descrição!", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Você se esqueceu da Categoria!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else {
                Toast.makeText(getApplicationContext(), "Você se esqueceu da Data!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else {
            Toast.makeText(getApplicationContext(), "Você se esqueceu do Valor!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}