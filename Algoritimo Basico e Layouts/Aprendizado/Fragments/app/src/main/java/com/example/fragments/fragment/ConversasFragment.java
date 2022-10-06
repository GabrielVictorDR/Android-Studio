package com.example.fragments.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.fragments.R;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

public class ConversasFragment extends Fragment {

    private TextView txtContatos;
    private Button btnAbrirSnackBar;

    public ConversasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_conversas, container, false);

        txtContatos = view.findViewById(R.id.txtConversas);
        btnAbrirSnackBar = view.findViewById(R.id.btnAbrirSnackBar);

        btnAbrirSnackBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(
                        view,
                        "Botão pressionado",
                        Snackbar.LENGTH_INDEFINITE
                ).setAction("Confimar", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txtContatos.setText("Contatos Atualizado...");
                    }
                }).setActionTextColor(getResources().getColor(R.color.teal_700)).show();
            }
        });

        return view;
    }
}