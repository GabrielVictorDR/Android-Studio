package com.example.instagram.padrao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.instagram.R;
import com.example.instagram.businessrules.Usuario;
import com.example.instagram.helper.ConfiguracaoFirebase;
import com.example.instagram.validate.CadastroActivityValidate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivityUI extends AppCompatActivity {

    private EditText campoUsuario;
    private EditText campoEmail;
    private EditText campoSenha;
    private Button botaoCadastrar;
    private ProgressBar progressBar;
    //
    private CadastroActivityValidate validate;
    //
    private Usuario _regras;
    //
    private FirebaseAuth autenticacao;

    private void loadControls(){
        campoUsuario = findViewById(R.id.editCadastroNome);
        campoUsuario.requestFocus();
        campoEmail = findViewById(R.id.editCadastroEmail);
        campoSenha = findViewById(R.id.editCadastroSenha);
        botaoCadastrar = findViewById(R.id.buttonCadastroEntrar);
        progressBar = findViewById(R.id.progressBarCadastro);
        //
        progressBar.setVisibility(View.GONE);
        //
        validate = new CadastroActivityValidate();
        //
        _regras = new Usuario();
        //
        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        //
        loadControls();
        //
        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = campoUsuario.getText().toString();
                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();

                if (validate.validar(CadastroActivityUI.this, nome, email, senha)) {
                    progressBar.setVisibility(View.VISIBLE);
                    cadastrar(nome, email, senha);
                }
            }
        });
    }

    private void cadastrar(String nome, String email, String senha) {
        aplicarDados(nome, email, senha);

        autenticacao.createUserWithEmailAndPassword(_regras.getEmail(), _regras.getSenha())
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(CadastroActivityUI.this, "Cadastro completo!", Toast.LENGTH_SHORT).show();
                    startActivity( new Intent(getApplicationContext(), MainActivityUI.class));
                    finish();
                    
                }else {
                    progressBar.setVisibility(View.GONE);
                    String erro = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        erro = "Digite uma senha mais forte.";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erro = "Digite um e-mail válido";
                    }catch (FirebaseAuthUserCollisionException e){
                        erro = "E-mail já foi cadastrado!";
                    } catch (Exception e){
                        erro = "Erro no Cadastro: " + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroActivityUI.this, erro, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void aplicarDados(String nome, String email, String senha) {
        if(_regras != null){
            _regras.setNome(nome);
            _regras.setEmail(email);
            _regras.setSenha(senha);
        }
    }
}