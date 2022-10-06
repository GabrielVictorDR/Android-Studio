package com.example.instacard.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.instacard.R;
import com.example.instacard.adapter.PostagemAdapter;
import com.example.instacard.model.Postagem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerPostagem;
    private List<Postagem> postagens = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerPostagem = findViewById(R.id.recyclerPostagem);

        //Define Layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerPostagem.setLayoutManager( layoutManager );
        recyclerPostagem.setHasFixedSize(true);
        PostagemAdapter adapter = new PostagemAdapter(postagens);
        recyclerPostagem.setAdapter(adapter);

        this.prepararPostagens();
    }

    public void prepararPostagens(){

        Postagem p = new Postagem("Wesley Virícimo #2119", "#flwLudovico #fui #chegaDesseChamado", R.drawable.imagem1, R.drawable.icons8user_96);
        this.postagens.add(p);

        p = new Postagem("André Luiz #9999", "Sampa é simplesmente a melhor! #SI #MorandoComOMozão", R.drawable.imagem2, R.drawable.icons8_usuario_andre);
        this.postagens.add(p);

        p = new Postagem("Adriano Nakamura #0001", "Java me levou à París!!!", R.drawable.imagem3, R.drawable.icons8user_96);
        this.postagens.add(p);

        p = new Postagem("Gabriel Victor #3322", "Daqui a pouco essa doida cai... :/", R.drawable.imagem4, R.drawable.icons8_usuario_gabriel);
        this.postagens.add(p);

    }
}