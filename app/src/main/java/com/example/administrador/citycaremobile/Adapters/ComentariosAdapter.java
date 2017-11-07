package com.example.administrador.citycaremobile.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrador.citycaremobile.Exceptions.APIError;
import com.example.administrador.citycaremobile.Modelo.Cidadao;
import com.example.administrador.citycaremobile.Modelo.Comentario;
import com.example.administrador.citycaremobile.Modelo.Empresa;
import com.example.administrador.citycaremobile.Modelo.UsuarioApplication;
import com.example.administrador.citycaremobile.R;
import com.example.administrador.citycaremobile.Services.CallService;
import com.example.administrador.citycaremobile.Services.Service;
import com.example.administrador.citycaremobile.Utils.ErrorUtils;
import com.google.gson.Gson;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrador on 06/11/2017.
 */

public class ComentariosAdapter extends RecyclerView.Adapter<ComentariosAdapter.ComentarioHolder> {

    private ArrayList<Comentario> comentarios;
    private Context context;

    public ComentariosAdapter(final Context context, ArrayList<Comentario> comentarios) {
        this.comentarios = comentarios;
        this.context = context;
    }

    @Override
    public ComentarioHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_comentarios, parent, false);
        return new ComentarioHolder(view);
    }

    @Override
    public void onBindViewHolder(final ComentarioHolder holder, final int position) {
        final Comentario comentario = comentarios.get(position);
        Service service = CallService.createService(Service.class);
        Call<Object> callUsuario = service.getUsuario(UsuarioApplication.getToken(), comentario.getUsuarioComentario());
        callUsuario.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    if (response.code() == 222) {
                        Object o = response.body();
                        String jsonCidadao = gson.toJson(o);
                        Cidadao cidadao = gson.fromJson(jsonCidadao, Cidadao.class);
                        Glide.with(context).load(cidadao.getDirFotoUsuario()).into(holder.picProfileComentario);
                        holder.tvPorfileComentario.setText(cidadao.getNome() + " " + cidadao.getSobrenome());
                        holder.descricaoComentario.setText(comentario.getDescricaoComentario());
                    } else if (response.code() == 223) {
                        Object o = response.body();
                        String jsonEmpresa = gson.toJson(o);
                        Empresa empresa = gson.fromJson(jsonEmpresa, Empresa.class);
                        Glide.with(context).load(empresa.getDirFotoUsuario()).into(holder.picProfileComentario);
                        holder.tvPorfileComentario.setText(empresa.getNomeFantasia());
                        holder.descricaoComentario.setText(comentario.getDescricaoComentario());
                    }
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    Toasty.error(context, "Erro na Conexão", Toast.LENGTH_LONG).show();
                    Log.e("CallUsuario", error.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toasty.error(context, "Erro na Conexão", Toast.LENGTH_LONG).show();
                Log.e("CallUsuario", t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return comentarios.size();
    }

    public class ComentarioHolder extends RecyclerView.ViewHolder {
        private Toolbar toolbar;
        private CircleImageView picProfileComentario;
        private TextView tvPorfileComentario;
        private TextView descricaoComentario;

        public ComentarioHolder(View itemView) {
            super(itemView);
            picProfileComentario = (CircleImageView) itemView.findViewById(R.id.pic_profile_comentario);
            tvPorfileComentario = (TextView) itemView.findViewById(R.id.nome_profile_comentario);
            descricaoComentario = (TextView) itemView.findViewById(R.id.descricao_denuncia);
            toolbar = (Toolbar) itemView.findViewById(R.id.toolbar_edt_comentario);

            toolbar.inflateMenu(R.menu.menu_comentarios);
        }
    }

    public void inserirComentario(Comentario comentario) {
        comentarios.add(comentario);
        notifyItemInserted(getItemCount());
    }

    public void removerComentario(int position) {
        comentarios.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

}
