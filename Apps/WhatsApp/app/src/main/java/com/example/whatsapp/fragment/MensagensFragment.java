package com.example.whatsapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.whatsapp.R;
import com.example.whatsapp.activity.ArquivadasActivity;
import com.example.whatsapp.activity.MainActivity;

public class MensagensFragment extends Fragment {

    private LinearLayout linearLayout;

    public MensagensFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_mensagens, container, false);


        linearLayout = view.findViewById(R.id.layoutAbrirArq);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), ArquivadasActivity.class);
                startActivity(i);

            }
        });


        return view;
    }

}