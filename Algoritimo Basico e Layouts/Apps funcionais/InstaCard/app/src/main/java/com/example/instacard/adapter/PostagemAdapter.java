package com.example.instacard.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instacard.R;
import com.example.instacard.model.Postagem;

import java.util.List;

public class PostagemAdapter extends RecyclerView.Adapter<PostagemAdapter.MyViewHolder> {

    private List<Postagem> listaPostagens;

    public PostagemAdapter(List<Postagem> lista){ this.listaPostagens = lista; }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.postagem_detalhe, parent, false);

        return new MyViewHolder(itemLista);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Postagem post = listaPostagens.get(position);
        holder.nome.setText(post.getUsuario_main());
        holder.userPerfilimg.setImageResource(post.getUsuario_perfil());
        holder.postagem.setImageResource((post.getPostagem()));
        holder.desc_postagem.setText(post.getDesc_postagem());

    }

    @Override
    public int getItemCount() {
        return listaPostagens.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView nome;
        private TextView desc_postagem;
        private ImageView postagem, userPerfilimg;
        private Button btnLike;

        public MyViewHolder(View itemView) {
            super(itemView);

            btnLike = itemView.findViewById(R.id.btnCurtirPost);
            nome = itemView.findViewById(R.id.txtNomeUsuario);
            desc_postagem = itemView.findViewById(R.id.txtDescPost);
            postagem = itemView.findViewById(R.id.imgPost);
            userPerfilimg = itemView.findViewById(R.id.img_Perfil_Usuario);
        }
    }
}
