package com.example.administrador.citycaremobile.Adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrador.citycaremobile.Exceptions.APIError;
import com.example.administrador.citycaremobile.Fragments.ComentarioFragment;
import com.example.administrador.citycaremobile.Fragments.FeedFragment;
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
import java.util.HashMap;

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
    private HashMap<Integer, Object> usuarios = new HashMap<>();
    private Context context;
    private Integer idLoginDenuncia;
    private ComentarioFragment cf;

    public ComentariosAdapter(final Context context, ArrayList<Comentario> comentarios, ComentarioFragment comentarioFragment, Integer idLoginDenuncia) {
        this.comentarios = comentarios;
        this.context = context;
        this.idLoginDenuncia = idLoginDenuncia;
        this.cf = comentarioFragment;
    }

    @Override
    public ComentarioHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_comentarios, parent, false);
        return new ComentarioHolder(view);
    }

    @Override
    public void onBindViewHolder(final ComentarioHolder holder, final int position) {
        if (UsuarioApplication.getInstance().getUsuario() == null) {
            cf.viewButtonLogin(View.VISIBLE, true);
        }
        if (comentarios.isEmpty()) {
            cf.viewSemComentarios(View.VISIBLE, true);
        } else {
            Comentario comentario = comentarios.get(position);
            holder.descricaoComentario.setText(comentario.getDescricao());
            Service service = CallService.createService(Service.class);
            Call<Object> callUsuario = service.getUsuario(UsuarioApplication.getToken(), comentario.getLogin());
            if (usuarios.get(position) == null) {
                callUsuario.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (response.isSuccessful()) {
                            Gson gson = new Gson();
                            if (response.code() == 222) {
                                Object o = response.body();
                                String jsonCidadao = gson.toJson(o);
                                Cidadao cidadao = gson.fromJson(jsonCidadao, Cidadao.class);
                                usuarios.put(position, cidadao);
                                Glide.with(context).load(cidadao.getDirFotoUsuario()).into(holder.picProfileComentario);
                                holder.tvPorfileComentario.setText(cidadao.getNome() + " " + cidadao.getSobrenome());
                            } else if (response.code() == 223) {
                                Object o = response.body();
                                String jsonEmpresa = gson.toJson(o);
                                Empresa empresa = gson.fromJson(jsonEmpresa, Empresa.class);
                                usuarios.put(position, empresa);
                                Glide.with(context).load(empresa.getDirFotoUsuario()).into(holder.picProfileComentario);
                                holder.tvPorfileComentario.setText(empresa.getNomeFantasia());
                            }
                        } else if (response.code() == 404) {
                            holder.tvPorfileComentario.setText("Desativado");
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

            } else {
                if (usuarios.get(position) instanceof Cidadao) {
                    Glide.with(context).load(((Cidadao) usuarios.get(position)).getDirFotoUsuario()).into(holder.picProfileComentario);
                    holder.tvPorfileComentario.setText(((Cidadao) usuarios.get(position)).getNome() + " " + ((Cidadao) usuarios.get(position)).getSobrenome());
                } else {
                    Glide.with(context).load(((Empresa) usuarios.get(position)).getDirFotoUsuario()).into(holder.picProfileComentario);
                    holder.tvPorfileComentario.setText(((Empresa) usuarios.get(position)).getNomeFantasia());
                }
            }
            if (UsuarioApplication.getInstance().getUsuario() != null) {
                if (UsuarioApplication.getInstance().getUsuario() instanceof Cidadao) {
                    if (comentario.getLogin().getIdLogin()
                            == ((Cidadao) UsuarioApplication.getInstance().getUsuario()).getLoginCidadao().getIdLogin()) {
                        holder.toolbar.setVisibility(View.VISIBLE);
                        holder.toolbar.setClickable(true);
                    } else if (((Cidadao) UsuarioApplication.getInstance().getUsuario()).getLoginCidadao().getIdLogin()
                            == idLoginDenuncia) {
                        holder.toolbar.setVisibility(View.VISIBLE);
                        holder.toolbar.setClickable(true);
                    } else {
                        holder.toolbar.setVisibility(View.INVISIBLE);
                        holder.toolbar.setClickable(false);
                    }
                } else if (UsuarioApplication.getInstance().getUsuario() instanceof Empresa) {
                    if (comentario.getLogin().getIdLogin()
                            == ((Empresa) UsuarioApplication.getInstance().getUsuario()).getLoginEmpresa().getIdLogin()) {
                        holder.toolbar.setVisibility(View.VISIBLE);
                        holder.toolbar.setClickable(true);
                    } else {
                        holder.toolbar.setVisibility(View.INVISIBLE);
                        holder.toolbar.setClickable(false);
                    }
                    if (((Empresa) UsuarioApplication.getInstance().getUsuario()).getLoginEmpresa().getIdLogin()
                            == idLoginDenuncia) {
                        holder.toolbar.setVisibility(View.VISIBLE);
                        holder.toolbar.setClickable(true);
                    } else {
                        holder.toolbar.setVisibility(View.INVISIBLE);
                        holder.toolbar.setClickable(false);
                    }
                } else {
                    holder.toolbar.setVisibility(View.INVISIBLE);
                    holder.toolbar.setClickable(false);
                }
            } else {
                holder.toolbar.setVisibility(View.INVISIBLE);
                holder.toolbar.setClickable(false);
            }

            holder.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.excluir_comentario:
                            Service service = CallService.createService(Service.class);
                            final Call<Void> deletarComentario = service.deletarComentario(UsuarioApplication.getToken(),
                                    comentarios.get(position));
                            deletarComentario.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        removerComentario(position);
                                        if (comentarios.isEmpty()) {
                                            cf.viewSemComentarios(View.VISIBLE, true);
                                        }
                                    } else {
                                        APIError error = ErrorUtils.parseError(response);
                                        Log.e("deletarComentario", error.getMessage());
                                        Toasty.error(context, "Erro na conexão", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Log.e("deletarComentario", t.getMessage());
                                    Toasty.error(context, "Erro na conexão", Toast.LENGTH_LONG).show();
                                }
                            });
                    }
                    return false;
                }
            });
        }
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
            descricaoComentario = (TextView) itemView.findViewById(R.id.tv_comentario);
            toolbar = (Toolbar) itemView.findViewById(R.id.toolbar_edt_comentario);

            toolbar.inflateMenu(R.menu.menu_comentarios);
        }
    }

    public void inserirComentario(Comentario comentario) {
        comentarios.add(comentario);
        notifyItemInserted(getItemCount());
        cf.viewSemComentarios(View.INVISIBLE, false);
    }

    public void removerComentario(int position) {
        comentarios.remove(position);
        usuarios.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
        cf.atualizarPostagem();
    }
}
