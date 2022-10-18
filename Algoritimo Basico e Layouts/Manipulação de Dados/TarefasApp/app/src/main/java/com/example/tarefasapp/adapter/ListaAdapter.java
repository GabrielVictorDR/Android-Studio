package com.example.tarefasapp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tarefasapp.R;
import com.example.tarefasapp.model.Tarefa;

import java.util.List;

public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.MyViewHolder> {

    private List<Tarefa> listaTarefas;

    public ListaAdapter(List<Tarefa> lista) {

        this.listaTarefas = lista;

    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_layout, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Tarefa t = listaTarefas.get(position);
        holder.textTarefa.setText(t.getNome());
        Log.i("tarefaAdapter", t.getNome());

    }

    @Override
    public int getItemCount() {
        return this.listaTarefas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView textTarefa;

        public MyViewHolder(View itemView) {
            super(itemView);

            textTarefa = itemView.findViewById(R.id.txtNomeTarefa);

        }

    }
}
