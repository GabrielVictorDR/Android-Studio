package com.example.whatsapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsapp.R;
import com.example.whatsapp.adapter.MensagensAdapter;
import com.example.whatsapp.config.ConfiguracaoFirebase;
import com.example.whatsapp.helper.Base64Custom;
import com.example.whatsapp.helper.UsuarioFirebase;
import com.example.whatsapp.model.Conversa;
import com.example.whatsapp.model.Mensagem;
import com.example.whatsapp.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private CircleImageView imgChat;
    private TextView nomeChat, txtMensagem;
    private Usuario destinatario;
    private ImageView imgCamera;

    //identificador de usuarios (rementente e destinatario)
    private String idUsuarioRemetente;
    private String idUsuarioDestinatario;

    //Parte do Adapter
    private RecyclerView recyclerView;
    private MensagensAdapter adapter;
    private List<Mensagem> mensagens = new ArrayList<>();

    //Firebase
    private DatabaseReference firebaseDatabase;
    private DatabaseReference mensagensRef;
    private ChildEventListener childEventListenerMensagens;
    private StorageReference storage;

    //Selecao de Imagens
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nomeChat = findViewById(R.id.txtNomeChat);
        imgChat = findViewById(R.id.imgFotoPerifl);
        txtMensagem = findViewById(R.id.txtMenssagem);
        imgCamera = findViewById(R.id.imgEnviarFotoConversa);

        idUsuarioRemetente = UsuarioFirebase.getItentificadorUsuario();

        recyclerView = findViewById(R.id.recyclerMensagens);



        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            destinatario = (Usuario) bundle.getSerializable("chatContato");
            nomeChat.setText( destinatario.getNome() );

            String foto = destinatario.getFoto();
            if (foto != null){

                Uri url = Uri.parse(destinatario.getFoto());

                Glide.with(ChatActivity.this)
                        .load(url)
                        .into(imgChat);
            }

            //recupera o email do usuario passado pela intent e codifica em b64;
            idUsuarioDestinatario = Base64Custom.codificarBase64((destinatario.getNumero().replace(" ", "")) + "@phone.com");
        }


        //Configuração Adapter
        adapter = new MensagensAdapter(mensagens, getApplicationContext());


        //Recycler View
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getApplicationContext() );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.hasFixedSize();
        recyclerView.setAdapter( adapter );

        //Recuperar coleção de mensagens
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        mensagensRef = firebaseDatabase.child("mensagens")
                .child( idUsuarioRemetente )
                .child( idUsuarioDestinatario );


        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if ( i.resolveActivity(getPackageManager()) != null ){

                    //Obsoleto, mas é o que tem pra hoje.

                    startActivityForResult(i, SELECAO_GALERIA );
                }

            }
        });

        storage = FirebaseStorage.getInstance().getReference();

        //salvarConversa();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( resultCode == RESULT_OK){

            Bitmap imagem = null;

            try {

                switch ( requestCode ){
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;

                    case SELECAO_GALERIA:
                        Uri imgLocalSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), imgLocalSelecionada);
                        break;
                }

                if(imagem != null) {

                    //Recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    //Criar o nome da imagem
                    String nomeImagem = UUID.randomUUID().toString();


                    //Configurar referencias do firebase
                    StorageReference imagemRef = storage.child("imagens")
                            .child("send")
                            .child(idUsuarioRemetente)
                            .child(nomeImagem);

                    UploadTask uploadTask = imagemRef.putBytes( dadosImagem );

                    //Se falhar
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Erro", "Erro no Upload");
                        }
                    })
                    //Se enviar
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {

                                    Uri url = task.getResult();
                                    Mensagem mensagem = new Mensagem();
                                    mensagem.setIdUsuario( idUsuarioRemetente );
                                    mensagem.setMensagem("imagem.jpeg");
                                    mensagem.setImagem( url.toString() );

                                    salvarMensagem(idUsuarioRemetente, idUsuarioDestinatario, mensagem);
                                    salvarMensagem(idUsuarioDestinatario, idUsuarioRemetente, mensagem);

                                }
                            });
                        }
                    });

                }

            }catch (Exception e){

            }

        }
    }

    @Override
    protected void onStart() {
        recuperarMensagens();
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mensagensRef.removeEventListener( childEventListenerMensagens );
    }

    public void enviarMensagem(View view){

        try {
            String textoMensagem = txtMensagem.getText().toString();

            if (!textoMensagem.isEmpty()) {

                Mensagem mensagem = new Mensagem();
                mensagem.setIdUsuario(idUsuarioRemetente);
                mensagem.setMensagem(textoMensagem);

                //Salvar mensagem para quem escreve.
                salvarMensagem(idUsuarioRemetente, idUsuarioDestinatario, mensagem);

                //Salvar mensagem para quem recebe;
                salvarMensagem(idUsuarioDestinatario, idUsuarioRemetente, mensagem);

            } else {

            }
        }catch (Exception e){
            Toast.makeText(ChatActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void salvarMensagem(String idUsuarioRemetente, String idUsuarioDestinatario, Mensagem mensagem){

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mensagemRef = database.child("mensagens");

        mensagemRef.child(idUsuarioRemetente)
                .child(idUsuarioDestinatario)
                .push()
                .setValue(mensagem);

        txtMensagem.setText("");
    }

    private void recuperarMensagens(){

        childEventListenerMensagens = mensagensRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Mensagem mensagem = snapshot.getValue(Mensagem.class);
                mensagens.add(mensagem);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void salvarConversa( Mensagem msg){
        Conversa conversaRemetente = new Conversa();
        conversaRemetente.setIdUsuario(idUsuarioRemetente);
        conversaRemetente.setIdDestinatario( idUsuarioDestinatario );
        conversaRemetente.setUltimaMensagem( msg.getMensagem() );
        //conversaRemetente.setUsuario_da_conversa(  );
    }

}