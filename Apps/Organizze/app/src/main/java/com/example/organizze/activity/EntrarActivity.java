package com.example.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizze.R;
import com.example.organizze.config.ConfiguracaoFirebase;
import com.example.organizze.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class EntrarActivity extends AppCompatActivity {

    private EditText campoEmail, campoSenha;
    private Button buttonEntrar;
    private Usuario usuario;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrar);
        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
        buttonEntrar = findViewById(R.id.buttonEntrar);

        buttonEntrar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();

                if(!email.isEmpty()){
                    if(!senha.isEmpty()){

                        usuario = new Usuario();
                        usuario.setEmail(email);
                        usuario.setSenha(senha);

                        validarLogin();

                    } else {
                        Toast.makeText(EntrarActivity.this, "Faltou a Senha!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EntrarActivity.this, "Faltou o E-mail!", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }

    public void validarLogin(){
        auth = ConfiguracaoFirebase.getFirebaseAutenticacao();

        auth.signInWithEmailAndPassword(

                usuario.getEmail(),
                usuario.getSenha()

        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    abrirTelaPrincipal();
                }else{

                    String excecao;
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e ){
                        excecao = "Usuário não cadastrado!";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "E-mail ou senha não correspondem ao usuário!";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(EntrarActivity.this, excecao, Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    public void abrirTelaPrincipal(){
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

}