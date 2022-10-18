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

public class DespesasActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText campoValor;

    private Movimentacao movimentacao;

    private DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = ConfiguracaoFirebase.getFirebaseAutenticacao();

    private Double despesaTotal;
    private Double despesaGerada;
    private Double despesaAtualizada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        campoValor = findViewById(R.id.editValorDespesa);
        campoData = findViewById(R.id.editDataDespesa);
        campoCategoria = findViewById(R.id.editCategoriaDespesa);
        campoDescricao = findViewById(R.id.editDescricaoDespesa);

        campoData.setText(DateCustom.dataAtual());
        recuperarDespesaAtual();
    }

//    public void mostrarBase64(View view){
//        String convert = campoData.getText().toString();
//
//        Toast.makeText(getApplicationContext(), Base64Custom.codificarBase64(convert), Toast.LENGTH_SHORT).show();
//    }

    public void salvarDespesa(View view){

        if(validarCamposDespesa() == true){

            movimentacao = new Movimentacao();

            despesaGerada = Double.parseDouble( campoValor.getText().toString() );

            movimentacao.setValor( despesaGerada );
            movimentacao.setCategoria( campoCategoria.getText().toString() );
            movimentacao.setDescricao( campoDescricao.getText().toString() );
            movimentacao.setData( campoData.getText().toString() );
            movimentacao.setTipo( "d" );

            despesaAtualizada = despesaTotal + despesaGerada;

            atualizarDespesa(despesaAtualizada);
            movimentacao.salvar( campoData.getText().toString() );
        }

    }

    private void recuperarDespesaAtual(){

        /*
        *
        *   CONVERTER EMAIL EM B64 -> USAR COMO CHILD PARA ENCONTRAR O BANCO;
        *
        */

        String email = mAuth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(email);
        DatabaseReference usuarioRef = mReference.child("usuarios").child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                despesaTotal = usuario.getDespesaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void atualizarDespesa(Double despesa){
        String email = mAuth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(email);
        DatabaseReference usuarioRef = mReference.child("usuarios").child(idUsuario);

        usuarioRef.child("despesaTotal").setValue(despesa);
    }

    public boolean validarCamposDespesa(){

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