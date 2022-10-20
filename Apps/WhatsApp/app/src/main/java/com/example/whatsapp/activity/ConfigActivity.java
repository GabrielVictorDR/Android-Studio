package com.example.whatsapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.whatsapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConfigActivity extends AppCompatActivity {

    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;
    private CircleImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Ajustes");
        setSupportActionBar( toolbar );

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        img = findViewById(R.id.imgFotoPerifl);
    }


    private void editarImgUsuario(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if ( intent.resolveActivity(getPackageManager()) != null ) {
            // obsoleto, mas é o que tem pra hoje!
            startActivityForResult(intent, SELECAO_CAMERA);
        }
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
                }

            }catch (Exception e){
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }


        }
    }
}