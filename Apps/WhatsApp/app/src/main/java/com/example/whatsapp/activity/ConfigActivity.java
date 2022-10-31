package com.example.whatsapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.whatsapp.R;
import com.example.whatsapp.config.ConfiguracaoFirebase;
import com.example.whatsapp.helper.UsuarioFirebase;
import com.example.whatsapp.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConfigActivity extends AppCompatActivity {

    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;
    private CircleImageView img;
    private EditText editNome;
    private ImageButton imgSalvar;
    private StorageReference storageReference;
    private String identificadorUsuario;

    private Usuario userLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Ajustes");
        setSupportActionBar( toolbar );

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        img = findViewById(R.id.imgFotoPerifl);
        editNome = findViewById(R.id.editUsuarioNome);
        imgSalvar = findViewById(R.id.imgButtonAlterarNome);

        //Configurações Inciais
        storageReference = FirebaseStorage.getInstance().getReference();
        identificadorUsuario = UsuarioFirebase.getItentificadorUsuario();
        userLogado = UsuarioFirebase.getDadosUsuarioLogado();

        //Recuperar dados usuario;
        FirebaseUser user = UsuarioFirebase.getUsuarioAtual();
        Uri url = user.getPhotoUrl();

        if (url != null){

            Glide.with(ConfigActivity.this)
                    .load(url)
                    .into(img);

        }else{
            img.setImageResource(R.drawable.portrait_placeholder);
        }

        editNome.setText( user.getDisplayName() );

        imgSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                String nome = editNome.getText().toString();
                boolean retorno = UsuarioFirebase.atualizarNomeUsuario( nome );
                
                if(retorno){

                    userLogado.setNome( nome );
                    userLogado.atualizar();

                    Toast.makeText(ConfigActivity.this, "Nome Alterado com Sucesso.", Toast.LENGTH_SHORT).show();
                }
                
            }
        });
        
    }

    public void abrirGaleria(View view){

        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
        if ( i.resolveActivity(getPackageManager()) != null ){
            // obsoleto, mas é o que tem pra hoje!
            startActivityForResult(i, SELECAO_GALERIA );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 200){
            Bitmap imagem = null;

            try{

                switch (requestCode){
                    case SELECAO_CAMERA :
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                    case SELECAO_GALERIA:
                        Uri imgLocalSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), imgLocalSelecionada);
                        break;
                }



                if (imagem != null){
                    img.setImageBitmap( imagem );

                    //Recuperar dados da Imagem
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();



                    //Encontra a referencia no Storage, e cria os nós para inserir a imagem;
                    StorageReference imagemRef = storageReference
                            .child("imagens")
                            .child("perfil")
                            .child(identificadorUsuario + ".jpeg");



                    UploadTask uploadTask = imagemRef.putBytes( dadosImagem );
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ConfigActivity.this, "Erro ao fazer upload da Imagem", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            Toast.makeText(ConfigActivity.this, "Sucesso ao fazer upload da Imagem", Toast.LENGTH_SHORT).show();

                            imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri url = task.getResult();
                                    atualizaFotoUsuario(url);
                                }
                            });


                        }
                    });


                }

            }catch (Exception e){
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }


        }
    }

    public void atualizaFotoUsuario(Uri url){
        boolean retorno = UsuarioFirebase.atualizarFotoUsuario(url);
        if ( retorno ){
            userLogado.setFoto( url.toString() );
            userLogado.atualizar();

            Toast.makeText(ConfigActivity.this,
                    "Sua foto foi alterada!",
                    Toast.LENGTH_SHORT).show();
        }

    }

}