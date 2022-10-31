package com.example.whatsapp.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.whatsapp.adapter.ContatosAdapter;
import com.example.whatsapp.adapter.GrupoSelecionadoAdapter;
import com.example.whatsapp.helper.UsuarioFirebase;
import com.example.whatsapp.model.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whatsapp.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import aula.com.whatsappclone.helper.RecyclerItemClickListener;

public class GrupoActivity extends AppCompatActivity {

    private RecyclerView recyclerMembrosSelecionados, recyclerASelecionar;
    private ContatosAdapter contatosAdapter;
    private GrupoSelecionadoAdapter grupoSelecionadoAdapter;
    private ArrayList<Usuario> listaMembros = new ArrayList<>();
    private ArrayList<Usuario> listaMembrosSelecionados = new ArrayList<>();
    private ValueEventListener valueEventListenerMembros;
    private TextView txtMembros;

    private static final DatabaseReference membrosRef = FirebaseDatabase.getInstance().getReference().child("usuarios");
    private static final FirebaseUser usuarioAtual = UsuarioFirebase.getUsuarioAtual();

    private Toolbar toolbar;

    public void atualizarMembrosToolbar(){
        int totalSelecionados = listaMembrosSelecionados.size();
        int total = listaMembros.size() + totalSelecionados;

        toolbar.setSubtitle(totalSelecionados + " de " + total + " selecionados.");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo Grupo");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtMembros = findViewById(R.id.txtMembrosV);

        FloatingActionButton fab = findViewById(R.id.fabSalvarGrupo);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(listaMembrosSelecionados.size() <= 0){
                    Toast.makeText(GrupoActivity.this, "Escolha ao menos um contato.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(GrupoActivity.this, CriarGrupoActivity.class);

                    i.putExtra("membros", listaMembrosSelecionados);

                    startActivity(i);
                }

            }
        });


        contatosAdapter = new ContatosAdapter(listaMembros, getApplicationContext());

        /*
        *   CONFIGURAÇÃO DO RECYCLER DE SELEÇÃO DOS MEMBROS DO GRUPO + EVENTO DE CLICK
        */

        recyclerASelecionar = findViewById(R.id.recyclerASelecionar);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerASelecionar.setLayoutManager(layoutManager);
        recyclerASelecionar.setHasFixedSize(true);
        recyclerASelecionar.setAdapter( contatosAdapter );
        recyclerASelecionar.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerASelecionar,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                txtMembros.setVisibility(View.VISIBLE);
                                Usuario usuarioClicado = listaMembros.get( position );

                                //Remover da lista
                                listaMembros.remove( usuarioClicado );
                                contatosAdapter.notifyDataSetChanged();

                                //Adicionar usuario a lista de selecionados
                                listaMembrosSelecionados.add( usuarioClicado );
                                grupoSelecionadoAdapter.notifyDataSetChanged();

                                atualizarMembrosToolbar();

                            }

                            @Override
                            public void onLongItemClick(View view, int position) { }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) { }
                        }
                )
        );

        /*
         *   CONFIGURAÇÃO DO RECYCLER DOS MEMBROS JÁ SELECIONADOS + EVENTO DE CLICK
         */

        grupoSelecionadoAdapter = new GrupoSelecionadoAdapter(listaMembrosSelecionados, getApplicationContext());

        recyclerMembrosSelecionados = findViewById(R.id.recyclerMembrosSelecionados);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerMembrosSelecionados.setLayoutManager(layoutManager2);
        recyclerMembrosSelecionados.setHasFixedSize(true);
        recyclerMembrosSelecionados.setAdapter( grupoSelecionadoAdapter );
        recyclerMembrosSelecionados.addOnItemTouchListener(new RecyclerItemClickListener(
                getApplicationContext(),
                recyclerMembrosSelecionados,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Usuario usuarioSelecionado = listaMembrosSelecionados.get( position );

                        listaMembrosSelecionados.remove( usuarioSelecionado );
                        grupoSelecionadoAdapter.notifyDataSetChanged();

                        listaMembros.add( usuarioSelecionado );
                        contatosAdapter.notifyDataSetChanged();

                        atualizarMembrosToolbar();

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                }
        ));


        carregarMembros_a_selecionar();
    }

    @Override
    protected void onStop() {
        super.onStop();
        membrosRef.removeEventListener( valueEventListenerMembros );
    }

    public void carregarMembros_a_selecionar(){

        valueEventListenerMembros = membrosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Percorre Todos os Usuarios (child) na coleção do Firebase
                for (DataSnapshot dados : snapshot.getChildren()) {

                    Usuario usuario = dados.getValue(Usuario.class);

                    String emailUsuarioAtual = usuario.getNumero().replace(" ", "") + "@phone.com";

                    if( !emailUsuarioAtual.equals( usuarioAtual.getEmail()) ) {
                        listaMembros.add(usuario);
                    }

                }

                contatosAdapter.notifyDataSetChanged();
                atualizarMembrosToolbar();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}