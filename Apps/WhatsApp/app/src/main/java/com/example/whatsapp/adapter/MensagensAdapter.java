package com.example.whatsapp.adapter;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsapp.R;
import com.example.whatsapp.helper.UsuarioFirebase;
import com.example.whatsapp.model.Mensagem;

import java.util.List;

public class MensagensAdapter extends RecyclerView.Adapter<MensagensAdapter.MyViewHolder> {

    private List<Mensagem> lista;
    private Context context;
    private static final int TIPO_REMETENTE = 0;
    private static final int TIPO_DESTINATARIO = 1;

    public MensagensAdapter(List<Mensagem> lista, Context c){
        this.lista = lista;
        this.context = c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View item = null;
        if(viewType == TIPO_REMETENTE){

            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensagem_remetente, parent, false);

        } else if( viewType == TIPO_DESTINATARIO){
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensagem_destinatario, parent, false);
        }
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Mensagem mensagem = lista.get( position );

        String msg = mensagem.getMensagem();
        String imagem = mensagem.getImagem();

        //Carrega apenas um ou outro, por questões de simplicidade;
        if ( imagem != null){

            Uri url = Uri.parse(imagem);
            Glide.with(context)
                    .load(url)
                    .into(holder.imagem);

            holder.mensagem.setVisibility(View.GONE);

        }else {
            holder.mensagem.setText(msg);
            holder.imagem.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    @Override
    public int getItemViewType(int position) {

        Mensagem mensagem = lista.get( position );
        String idUsuario = UsuarioFirebase.getItentificadorUsuario();

        if( idUsuario.equals(mensagem.getIdUsuario()) ){
            return TIPO_REMETENTE;
        }

        return TIPO_DESTINATARIO;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mensagem;
        ImageView imagem;

        public MyViewHolder(View itemView){
            super(itemView);

            mensagem = itemView.findViewById(R.id.txtMensagemAdapter);
            imagem = itemView.findViewById(R.id.imageMensagemAdapter);
        }
    }
}
