package com.example.whatsapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.R;
import com.example.whatsapp.activity.ChatActivity;
import com.example.whatsapp.activity.GrupoActivity;
import com.example.whatsapp.adapter.ContatosAdapter;
import com.example.whatsapp.helper.UsuarioFirebase;
import com.example.whatsapp.model.Usuario;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import aula.com.whatsappclone.helper.RecyclerItemClickListener;

import java.util.ArrayList;

public class ContatosFragment extends Fragment {

    private RecyclerView recyclerContatos;
    private ContatosAdapter adapter;
    private ArrayList<Usuario> listaUsuaios = new ArrayList<>();

    private DatabaseReference usuariosRef;
    private FirebaseUser usuarioAtual;

    private ValueEventListener valueEventListenerContatos;

    public ContatosFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);

        adapter = new ContatosAdapter(listaUsuaios, getActivity());

        recyclerContatos = view.findViewById(R.id.recyclerContatos);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerContatos.setLayoutManager(layoutManager);
        recyclerContatos.setHasFixedSize(true);
        recyclerContatos.setAdapter(adapter);

        usuarioAtual = UsuarioFirebase.getUsuarioAtual();

        recyclerContatos.addOnItemTouchListener( new RecyclerItemClickListener(
                getActivity(),
                recyclerContatos,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Usuario usuarioSelecionado = listaUsuaios.get( position );
                        boolean cabecalho = usuarioSelecionado.getNumero().isEmpty();

                        if( cabecalho ){

                            Intent i = new Intent(getActivity(), GrupoActivity.class);
                            startActivity(i);

                        } else {

                            Intent i = new Intent(getActivity(), ChatActivity.class);
                            i.putExtra("chatContato", usuarioSelecionado);
                            startActivity(i);

                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                }
        ));

        usuariosRef = FirebaseDatabase.getInstance().getReference().child("usuarios");

        Usuario itemGrupo = new Usuario();
        itemGrupo.setNome("Novo Grupo");
        itemGrupo.setNumero("");

        listaUsuaios.add(itemGrupo);
        recuperarContatos();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        usuariosRef.removeEventListener(valueEventListenerContatos);
    }

    public void recuperarContatos() {

        valueEventListenerContatos = usuariosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Percorre Todos os Usuarios (child) na coleção do Firebase
                for (DataSnapshot dados : snapshot.getChildren()) {

                    Usuario usuario = dados.getValue(Usuario.class);

                    String emailUsuarioAtual = usuario.getNumero().replace(" ", "") + "@phone.com";

                    if( !emailUsuarioAtual.equals( usuarioAtual.getEmail()) ) {
                        listaUsuaios.add(usuario);
                    }

                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}