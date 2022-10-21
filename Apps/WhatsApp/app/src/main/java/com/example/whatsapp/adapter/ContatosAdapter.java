package com.example.whatsapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsapp.R;
import com.example.whatsapp.model.Usuario;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContatosAdapter extends RecyclerView.Adapter<ContatosAdapter.MyViewHolder>{

    private List<Usuario> contatos;
    private Context context;

    public ContatosAdapter(List<Usuario> list, Context context){
        this.contatos = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.contatos_adapter, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Usuario usuario = contatos.get(position);
        holder.nome.setText(usuario.getNome());
        holder.telefone.setText(usuario.getNumero());

        if(usuario.getFoto() != null){
            Uri uri = Uri.parse(usuario.getFoto());
            Glide.with(context).load(uri).into(holder.foto);
        } else  {
            holder.foto.setImageResource(R.drawable.portrait_placeholder);
        }
    }

    @Override
    public int getItemCount() {

        return contatos.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView foto;
        TextView nome, telefone;

        public MyViewHolder(View itemView) {
            super(itemView);

            foto = itemView.findViewById(R.id.imgFotoPerifl);
            nome = itemView.findViewById(R.id.lblNomeContato);
            telefone = itemView.findViewById(R.id.lblEmailContato);

        }

    }

}
