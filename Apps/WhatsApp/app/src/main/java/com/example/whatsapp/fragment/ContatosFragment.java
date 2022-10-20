package com.example.whatsapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whatsapp.R;
import com.example.whatsapp.adapter.ContatosAdapter;
import com.example.whatsapp.model.Usuario;

import java.util.ArrayList;

public class ContatosFragment extends Fragment {

    private ContatosAdapter adapter;
    private ArrayList<Usuario> listaUsuaios;

    public ContatosFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View view = inflater.inflate(R.layout.fragment_contatos, container, false);

       RecyclerView recyclerContatos = view.findViewById(R.id.recyclerContatos);

       adapter = new ContatosAdapter(listaUsuaios, getActivity());

       RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getActivity() );
       recyclerContatos.setLayoutManager(layoutManager);
       recyclerContatos.setHasFixedSize(true);
       recyclerContatos.setAdapter( adapter );

       return view;
    }
}