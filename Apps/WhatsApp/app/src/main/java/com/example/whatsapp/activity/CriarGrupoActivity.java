package com.example.whatsapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.example.whatsapp.adapter.GrupoSelecionadoAdapter;
import com.example.whatsapp.helper.UsuarioFirebase;
import com.example.whatsapp.model.Grupo;
import com.example.whatsapp.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whatsapp.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CriarGrupoActivity extends AppCompatActivity {

    private List<Usuario> listaMembrosSelecionados = new ArrayList<>();
    private TextView textoTotalParticipantes;
    private EditText editNomeGrupo;
    private GrupoSelecionadoAdapter adapter;
    private RecyclerView recyclerMembrosDoGrupo;
    private FloatingActionButton fab;
    private CircleImageView imgGrupo;
    private static final int SELECAO_GALERIA = 200;

    //Firebase
    private StorageReference storageReference;
    private Grupo grupo;

    public void inicializarComponentes() {
        textoTotalParticipantes = findViewById(R.id.txtTotalParticipantes);
        recyclerMembrosDoGrupo = findViewById(R.id.recyclerParticipantesGrupo);
        imgGrupo = findViewById(R.id.imgGrupo);
        editNomeGrupo = findViewById(R.id.editNomeGrupo);
    }

    public void inicializarFirebaseLibs() {
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_grupo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo Grupo");
        toolbar.setSubtitle("Defina o nome");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inicializarComponentes();
        inicializarFirebaseLibs();

        grupo = new Grupo();

        //Verificação para recuperar dados da Activity Parent
        if (getIntent().getExtras() != null) {
            List<Usuario> membros = (List<Usuario>) getIntent().getExtras().getSerializable("membros");
            listaMembrosSelecionados.addAll(membros);

            textoTotalParticipantes.setText(listaMembrosSelecionados.size() + " Participantes.");
        }

        //Configurar RecyclerView
        adapter = new GrupoSelecionadoAdapter(listaMembrosSelecionados, getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerMembrosDoGrupo.setLayoutManager(layoutManager);
        recyclerMembrosDoGrupo.setHasFixedSize(true);
        recyclerMembrosDoGrupo.setAdapter(adapter);

        imgGrupo.setOnClickListener((v) -> {

            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            if (i.resolveActivity(getPackageManager()) != null) {
                // obsoleto, mas é o que tem pra hoje!
                startActivityForResult(i, SELECAO_GALERIA);
            }

        });

        //Configuração do Floating Action Button (autogerado)
        fab = findViewById(R.id.fabSalvarGrupo);
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomeGrupo = editNomeGrupo.getText().toString();
                listaMembrosSelecionados.add(UsuarioFirebase.getDadosUsuarioLogado());

                grupo.setMembros(listaMembrosSelecionados);

                grupo.setNome(nomeGrupo);
                grupo.salvar();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap imagem = null;

            try {

                Uri locaImgSelecionada = data.getData();
                imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), locaImgSelecionada);

                if (imagem != null) {
                    imgGrupo.setImageBitmap(imagem);

                    //Recuperar dados da Imagem
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();


                    //Encontra a referencia no Storage, e cria os nós para inserir a imagem;
                    StorageReference imagemRef = storageReference
                            .child("imagens")
                            .child("groups")
                            .child(grupo.getId() + ".jpeg");


                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);

                    uploadTask.addOnFailureListener(new OnFailureListener() {

                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CriarGrupoActivity.this, "Erro ao fazer upload da Imagem", Toast.LENGTH_SHORT).show();
                        }

                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri url = task.getResult();
                                    grupo.setFoto(url.toString());
                                }
                            });

                        }
                        
                    });

                }

            } catch (Exception e) {

                e.printStackTrace();

            }
        }
    }

}