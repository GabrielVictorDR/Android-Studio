package com.example.whatsapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.whatsapp.R;
import com.example.whatsapp.activity.ArquivadasActivity;
import com.example.whatsapp.activity.ChatActivity;
import com.example.whatsapp.activity.MainActivity;
import com.example.whatsapp.adapter.ConversasAdapter;
import com.example.whatsapp.config.ConfiguracaoFirebase;
import com.example.whatsapp.helper.UsuarioFirebase;
import com.example.whatsapp.model.Conversa;
import com.example.whatsapp.model.Usuario;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import aula.com.whatsappclone.helper.RecyclerItemClickListener;

public class MensagensFragment extends Fragment {

    private LinearLayout linearLayout;
    private RecyclerView recyclerConversas;
    private ArrayList<Conversa> conversas = new ArrayList<>();
    private ConversasAdapter adapter;

    private DatabaseReference conversaRef;
    private ChildEventListener childEventListenerConversa;

    public MensagensFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mensagens, container, false);

        linearLayout = view.findViewById(R.id.layoutAbrirArq);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), ArquivadasActivity.class);
                startActivity(i);

            }
        });

        //Adapter e Recycler
        adapter = new ConversasAdapter(conversas, getActivity());
        recyclerConversas = view.findViewById(R.id.recyclerConversas);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerConversas.setLayoutManager(layoutManager);
        recyclerConversas.setHasFixedSize(true);
        recyclerConversas.setAdapter( adapter );
        //evento de clique
        recyclerConversas.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(),
                recyclerConversas,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Conversa conversaSelecionada = conversas.get( position );

                        if( conversaSelecionada.getIsGroup().equals("true")){

                            Intent i = new Intent(getActivity(), ChatActivity.class);
                            i.putExtra("chatGrupo", conversaSelecionada.getGrupo());
                            startActivity(i);

                        } else {

                            Intent i = new Intent(getActivity(), ChatActivity.class);
                            i.putExtra("chatContato", conversaSelecionada.getUsuario_da_conversa());
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

        //Firebase
        String identificadorUsuario = UsuarioFirebase.getItentificadorUsuario();
        conversaRef = FirebaseDatabase.getInstance().getReference().child("conversas")
                .child( identificadorUsuario );

        recuperarConversas();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        conversaRef.removeEventListener( childEventListenerConversa );
        super.onStop();
    }

    public void pesquisarConversas(String pesquisa){

        List<Conversa> listaConversaBusca = new ArrayList<>();

        for (Conversa conversa : conversas){

            String nome = conversa.getUsuario_da_conversa().getNome().toLowerCase();
            String ultimaMsg = conversa.getUltimaMensagem().toLowerCase();

            if( nome.contains(pesquisa) || ultimaMsg.contains( pesquisa )){

                listaConversaBusca.add( conversa );

            }

            //Cria um novo Adapter com a lista de conversas que possuem nome ou mensagens iguais a pesquisa;
            adapter = new ConversasAdapter( listaConversaBusca , getActivity() );
            recyclerConversas.setAdapter( adapter );
            adapter.notifyDataSetChanged();
        }
    }

    public void recarregarConversas(){
        adapter = new ConversasAdapter(conversas, getActivity());
        recyclerConversas.setAdapter( adapter );
        adapter.notifyDataSetChanged();
    }

    public void recuperarConversas(){

        childEventListenerConversa = conversaRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Conversa conversa = snapshot.getValue(Conversa.class);
                conversas.add( conversa );
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

    }

}