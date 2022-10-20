package com.example.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.whatsapp.R;
import com.example.whatsapp.config.ConfiguracaoFirebase;
import com.example.whatsapp.helper.Base64Custom;
import com.example.whatsapp.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import br.com.sapereaude.maskedEditText.MaskedEditText;

public class LoginActivity extends AppCompatActivity {

    private Usuario usuario;

    private FirebaseAuth auth;

    private MaskedEditText campoNro;
    private Button cadastrarLogar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        campoNro = findViewById(R.id.editTelefone);
        cadastrarLogar = findViewById(R.id.btnLoginCadastro);

        cadastrarLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!campoNro.getText().toString().equals("")) {

                    /*
                     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                     * CUIDADO, GAMBIARRA A FRENTE (PS: PREGUIÇA DE CONFIGURAR SING IN POR TELEFONE NO FIREBASE);
                     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                     */

                    //pega o número sem os espaços da mascara, transforma em um email padrão e seta como email do usuário;
                    String format = (campoNro.getText().toString() + "@phone.com").replace((" "), "");

                    usuario = new Usuario();

                    //seta o numero do usuario como o texto completo da mascara.
                    usuario.setNumero(campoNro.getText().toString());
                    usuario.setEmail(format);
                    usuario.setSenha("123456_always");
                    login();

                } else {
                    Toast.makeText(getApplicationContext(), "Você esqueceu de digitar o número de telefone.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = ConfiguracaoFirebase.getFirebaseAuth();
        if(auth.getCurrentUser() != null){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    public void login(){

        auth = ConfiguracaoFirebase.getFirebaseAuth();

        auth.signInWithEmailAndPassword(

                usuario.getEmail(),
                usuario.getSenha()

        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){ //Se houver um Telefone.email cadastrado, faz login;

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();

                } else {

                    auth.createUserWithEmailAndPassword( usuario.getEmail(), usuario.getSenha() ) //Se não houver, cria um novo usuario instantaneamente.
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if(task.isSuccessful()){

                                        String idUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                                        usuario.setId(idUsuario);
                                        usuario.salvar();

                                        auth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha());

                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();


                                        Toast.makeText(getApplicationContext(), "Conta Cadastrada com Sucesso.", Toast.LENGTH_SHORT).show();

                                    } else {


                                        String excecao;
                                        try {
                                            throw task.getException();
                                        } catch (FirebaseAuthUserCollisionException e) {
                                            excecao = "Conta já cadastrada!";
                                        } catch (Exception e) {
                                            excecao = "Erro ao cadastrar usuário";
                                        }
                                        Toast.makeText(getApplicationContext(), "Erro: " + excecao, Toast.LENGTH_LONG).show();


                                    }

                                }
                            });

                }

            }
        });



    }

    public void termsAndConditions(View view){
        Toast.makeText(getApplicationContext(), "Termos & Condições", Toast.LENGTH_SHORT).show();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
        alertDialog.setTitle("Termos & Condições");
        alertDialog.setMessage("Ao enviar seu número você concorda em passar o pix de R$ 2k, pra conta do paê B)");

        alertDialog.setPositiveButton("Fechar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alertDialog.create();
        alertDialog.show();
    }
}