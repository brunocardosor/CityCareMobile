package com.example.administrador.citycaremobile.Adapters;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.example.administrador.citycaremobile.Exceptions.APIError;
import com.example.administrador.citycaremobile.Modelo.Cidadao;
import com.example.administrador.citycaremobile.Modelo.Empresa;
import com.example.administrador.citycaremobile.Modelo.Login;
import com.example.administrador.citycaremobile.Modelo.Postagem;
import com.example.administrador.citycaremobile.Modelo.UsuarioApplication;
import com.example.administrador.citycaremobile.R;
import com.example.administrador.citycaremobile.Services.CallService;
import com.example.administrador.citycaremobile.Services.Service;
import com.example.administrador.citycaremobile.Utils.ErrorUtils;
import com.google.gson.Gson;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrador on 17/09/2017.
 */

public class FeedDenunciaAdapter extends RecyclerView.Adapter<FeedDenunciaAdapter.FeedDenunciaHolder> {

    private ArrayList<Postagem> postagens;
    private Context context;
    private Cidadao cidadao;
    private Empresa empresa;

    public FeedDenunciaAdapter(Context context) {
        this.postagens = new ArrayList<>();
        this.context = context;
    }

    public void setPostagens(ArrayList<Postagem> postagens) {
        this.postagens = postagens;
    }

    public void inserirPostagem(Postagem post) {
        postagens.add(post);
        notifyItemInserted(getItemCount());
    }

    public void atualizarPostagem(int position, Postagem post) {
        Postagem postAtualizado = postagens.get(position);
        postAtualizado = post;
        notifyItemChanged(position);
    }

    public void deletarPostagem(int position) {
        postagens.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, postagens.size());
    }

    @Override
    public FeedDenunciaAdapter.FeedDenunciaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_denuncia, parent, false);
        return new FeedDenunciaHolder(view);
    }

    @Override
    public void onViewRecycled(FeedDenunciaHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(final FeedDenunciaHolder holder, int position) {
        Postagem post = postagens.get(position);

        //Serviço para pegar dados de quem criou a denúncia e Binda-los
        Service service = CallService.createService(Service.class);
        Call<Object> callUsuario = service.getUsuario(UsuarioApplication.getInstance().getToken(), post.getDenuncia().getLogin());
        callUsuario.enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    if (response.code() == 222) {
                        Object o = response.body();
                        String jsonCidadao = gson.toJson(o);
                        cidadao = gson.fromJson(jsonCidadao, Cidadao.class);
                        holder.nameProfilePost.setText(cidadao.getNome());
                        Glide.with(context).load(cidadao.getDirFotoUsuario()).into(holder.profilePicPost);
                    } else if (response.code() == 223) {
                        Object o = response.body();
                        String jsonEmpresa = gson.toJson(o);
                        empresa = gson.fromJson(jsonEmpresa, Empresa.class);
                        holder.nameProfilePost.setText(empresa.getNomeFantasia());
                        Glide.with(context).load(empresa.getDirFotoUsuario()).into(holder.profilePicPost);
                    } else {
                        APIError error = ErrorUtils.parseError(response);
                        Log.e("Erro de parse", error.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e("Falha", t.getLocalizedMessage());
                Toasty.error(context, "erro", Toast.LENGTH_LONG);
            }
        });

        //Bindando dados da postagem
        holder.timePost.setText(getPeriodo(new DateTime(post.getDenuncia().getDataDenuncia())));
        holder.descricaoPost.setText(post.getDenuncia().getDescricaoDenuncia());
        holder.categoriaPost.setText(post.getDenuncia().getCategoriaDenuncia().toString());
        Glide.with(context).load(post.getDenuncia().getDirFotoDenuncia()).into(holder.denunciaPicPost);

        //Geocoder para endereço
        Geocoder geo = new Geocoder(context);
        try {
            List<Address> addresses = geo.getFromLocation(post.getDenuncia().getLatitude(), post.getDenuncia().getLongitude(),1);
            holder.localizacaoPost.setText(addresses.get(0).getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //INSERIR LOGICA PARA VERIFICAR SE O USUÁRIO JÀ AGILIZOU A POSTAGEM
        if(UsuarioApplication.getInstance().getUsuario() != null){
            for (int i = 0; i < post.getAgilizas().size(); i++) {
                if (post.getAgilizas().get(i).getLogin().getIdLogin() == ((Cidadao) UsuarioApplication.getInstance().getUsuario()).getLoginCidadao().getIdLogin()) {

                }
            }

        }

        if (post.getAgilizas().size() > 2) {
            holder.statusPost.setText(post.getAgilizas().size() + "pessoas agilizaram esta denúncia");
        } else if (post.getAgilizas().size() == 1) {
            holder.statusPost.setText("Uma pessoa agilizou esta denúncia");
        } else {
            holder.statusPost.setText("Nenhuma pessoa agilizou esta denúncia");
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return postagens.size();
    }

    class FeedDenunciaHolder extends RecyclerView.ViewHolder {

        CircleImageView profilePicPost, profilePicComentario;
        TextView nameProfilePost, timePost, descricaoPost, localizacaoPost, categoriaPost, statusPost;
        Toolbar toolbarPostMenu;
        EditText edtComentario;
        ImageButton agilizarPost, sharePost, enviarComentario;
        ImageView denunciaPicPost;

        public FeedDenunciaHolder(View view) {
            super(view);
            profilePicPost = (CircleImageView) view.findViewById(R.id.pic_profile_post);
            nameProfilePost = (TextView) view.findViewById(R.id.name_profile_post);
            timePost = (TextView) view.findViewById(R.id.time_post);
            toolbarPostMenu = (Toolbar) view.findViewById(R.id.toolbar_post_menu);
            descricaoPost = (TextView) view.findViewById(R.id.descricao_post);
            localizacaoPost = (TextView) view.findViewById(R.id.localizacao_post);
            categoriaPost = (TextView) view.findViewById(R.id.categoria_post);
            denunciaPicPost = (ImageView) view.findViewById(R.id.denuncia_picture_post);
            statusPost = (TextView) view.findViewById(R.id.status_post);
            agilizarPost = (ImageButton) view.findViewById(R.id.bt_agilizar_post);
            sharePost = (ImageButton) view.findViewById(R.id.bt_share_post);
            profilePicComentario = (CircleImageView) view.findViewById(R.id.pic_profile_comentario);
            edtComentario = (EditText) view.findViewById(R.id.edt_comentario);
            enviarComentario = (ImageButton) view.findViewById(R.id.bt_enviar_comentario);

            //menu
            toolbarPostMenu.inflateMenu(R.menu.menu_denuncia);

            toolbarPostMenu.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.editar_denuncia:
                            break;
                        case R.id.deletar_denuncia:
                            break;
                    }
                    return true;
                }
            });

            agilizarPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            sharePost.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {

                                             }
                                         }
            );

            enviarComentario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    private String getPeriodo(DateTime inicio) {
        Period period = new Period(inicio, DateTime.now());
        if (period.getYears() != 0) {
            if (period.getYears() > 1) {
                return period.getYears() + " anos atrás";
            } else {
                return period.getYears() + " ano atrás";
            }
        } else if (period.getWeeks() != 0) {
            if (period.getWeeks() > 1) {
                return period.getWeeks() + " semanas atrás";
            } else {
                return period.getWeeks() + " semana atrás";
            }
        } else if (period.getDays() != 0) {
            if (period.getDays() > 1) {
                return period.getDays() + " dias atrás";
            } else {
                return period.getDays() + " dia atrás";
            }
        } else if (period.getHours() != 0) {
            if (period.getHours() > 1) {
                return period.getHours() + " horas atrás";
            } else {
                return period.getHours() + " hora atrás";
            }
        } else if (period.getSeconds() != 0) {
            if (period.getSeconds() > 1) {
                return period.getSeconds() + " segundos atrás";
            } else {
                return period.getSeconds() + " segundo atrás";
            }
        }
        return null;
    }

    public Object getUsuario(Login login) {


        if (cidadao != null) {
            return cidadao;
        } else if (empresa != null) {
            return empresa;
        } else {
            return null;
        }
    }
}
