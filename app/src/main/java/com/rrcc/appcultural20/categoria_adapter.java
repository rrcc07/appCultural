package com.rrcc.appcultural20;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

public class categoria_adapter extends RecyclerView.Adapter<categoria_adapter.MyViewHolder> {

    Context mContext;
    List<Categoria> mCategoria;

    public categoria_adapter(Context mContext, List<Categoria> mCategoria) {
        this.mContext = mContext;
        this.mCategoria = mCategoria;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.categorias_fila,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        //holder.imagenCategoria.setImageResource(mCategoria.get(position).getImagenCategoria());
        Glide.with(mContext).load(mCategoria.get(position).getImagenCategoria()).into(holder.imagenCategoria);

        holder.imagenCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,adapterCategorias.class);
                intent.putExtra("catName",mCategoria.get(position).getImagenCategoria());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCategoria.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imagenCategoria;

        public MyViewHolder(View itemView) {
            super(itemView);
            imagenCategoria = itemView.findViewById(R.id.imagenCategoria);
        }
    }
}
