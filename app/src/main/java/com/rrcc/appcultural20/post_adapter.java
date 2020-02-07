package com.rrcc.appcultural20;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.List;

public class post_adapter extends RecyclerView.Adapter<post_adapter.MyViewHolder> {

    Context mContext;
    List<Post> mData;

    public post_adapter(Context mContext, List<Post> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.evento_fila1,parent,false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.title.setText(mData.get(position).getTitle());
        //holder.numTelefono.setText(mData.get(position).getNumTelefonoEvento());
        //holder.detalle.setText(mData.get(position).getDescription());
        holder.direccion.setText(mData.get(position).getDireccion());
        holder.fechaIni.setText(mData.get(position).getFechaIni());
        holder.fechafin.setText(mData.get(position).getFechaFin());
        holder.horaIni.setText(mData.get(position).getHoraIni());
        holder.horaFin.setText(mData.get(position).getHoraFin());
        holder.spinnerOpc.setText(mData.get(position).getSpinnerOpc());
        //holder.usuarioPropietario.setText(mData.get(position).getUserName());
        //holder.correoPropietario.setText(mData.get(position).getCorreo());

        Glide.with(mContext).load(mData.get(position).getPicture()).into(holder.imgPost);
        Glide.with(mContext).load(mData.get(position).getUserPhoto()).into(holder.imgPostUsuario);

        holder.btnVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,eventoActivity.class);
                intent.putExtra("tituloPost",mData.get(position).getTitle());
                intent.putExtra("numeroPropietario",mData.get(position).getNumTelefonoEvento());
                intent.putExtra("detallePost",mData.get(position).getDescription());
                intent.putExtra("horaIniPost",mData.get(position).getHoraIni());
                intent.putExtra("horaFinPost",mData.get(position).getHoraFin());
                intent.putExtra("fechaIniPost",mData.get(position).getFechaIni());
                intent.putExtra("fechaFinPost",mData.get(position).getFechaFin());
                intent.putExtra("direccionPost",mData.get(position).getDireccion());
                intent.putExtra("imagenPost",mData.get(position).getPicture());
                intent.putExtra("categoria",mData.get(position).getSpinnerOpc());
                intent.putExtra("usuarioPropietario",mData.get(position).getUserName());
                intent.putExtra("correoPropietario",mData.get(position).getCorreo());
                intent.putExtra("postKey",mData.get(position).getPostKey());
                Toast.makeText(mContext, mData.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView numTelefono;
        TextView detalle;
        TextView direccion;
        ImageButton imgPost;
        ImageView imgPostUsuario;
        //agregar fechas y hora
        TextView fechaIni;
        TextView fechafin;
        TextView horaIni;
        TextView horaFin;
        TextView spinnerOpc;
        TextView usuarioPropietario;
        TextView correoPropietario;
        Button btnVer;

        AdapterView.OnItemClickListener itemClickListener;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.texto_titulo_evento);
            numTelefono = itemView.findViewById(R.id.telefonoPropietario);
            direccion = itemView.findViewById(R.id.texto_ubicacion_evento);
            imgPost = itemView.findViewById(R.id.imagen_evento1);
            imgPostUsuario = itemView.findViewById(R.id.imagen_usuario_evento);
            fechaIni = itemView.findViewById(R.id.texto_fechaIni_evento);
            fechafin = itemView.findViewById(R.id.texto_fechaFin_evento);
            horaIni = itemView.findViewById(R.id.texto_tiempoIni_evento);
            horaFin = itemView.findViewById(R.id.texto_tiempoFin_evento);
            spinnerOpc = itemView.findViewById(R.id.text_categoria_evento);
            usuarioPropietario = itemView.findViewById(R.id.nameOrganizador);
            correoPropietario = itemView.findViewById(R.id.correoOrgaizador);
            btnVer = itemView.findViewById(R.id.btnVerMas);
        }
    }
}
