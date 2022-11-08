package com.example.instagram.padrao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.instagram.R;
import com.example.instagram.businessrules.Usuario;
import com.example.instagram.helper.ConfiguracaoFirebase;
import com.example.instagram.validate.LoginActivityValidate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivityUI extends AppCompatActivity {

    private EditText campoEmail;
    private EditText campoSenha;
    private Button botaoLogin;
    private ProgressBar progressBar;
    //
    private FirebaseAuth firebaseAuth;
    //
    private LoginActivityValidate validate;
    //
    private Usuario _regras;

    private void loadControls(){
        campoEmail = findViewById(R.id.editLoginEmail);
        campoEmail.requestFocus();
        campoSenha = findViewById(R.id.editLoginSenha);
        botaoLogin = findViewById(R.id.buttonLoginEntrar);
        progressBar = findViewById(R.id.progressBarLogin);
        progressBar.setVisibility(View.GONE);
        //
        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        //
        validate = new LoginActivityValidate();
        //
        _regras = new Usuario();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //
        verificarUsuarioLogado();
        //
        loadControls();
        //
        botaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();

                if(validate.validar(LoginActivityUI.this, email, senha)){
                    progressBar.setVisibility(View.VISIBLE);
                    logar(email, senha);
                }

            }
        });
    }

    private void verificarUsuarioLogado() {
        //Instanciado novamente para evitar NullPointerException, pois este método vem antes do loadControls
        //Onde o firebaseAuth é instanciado "Globalmente".
        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();

        try {
            if (firebaseAuth.getCurrentUser() != null) {
                startActivity(new Intent(getApplicationContext(), MainActivityUI.class));
                finish();
            }
        }catch (Exception e){
            Log.e("LOGINERRO", e.getMessage());
        }
    }

    private void logar(String email, String senha) {
        aplicarDados(email, senha);

        firebaseAuth.signInWithEmailAndPassword(email, senha)
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(getApplicationContext(), MainActivityUI.class));
                    finish();
                }else {
                    Toast.makeText(LoginActivityUI.this, "Email ou Senha incorretos.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }

            }
        });
    }

    private void aplicarDados(String email, String senha) {
        if(_regras != null){
            _regras.setEmail(email);
            _regras.setSenha(senha);
        }
    }

    public void abrirCadastro(View view){
        Intent i = new Intent(LoginActivityUI.this, CadastroActivityUI.class);
        startActivity(i);
    }
}