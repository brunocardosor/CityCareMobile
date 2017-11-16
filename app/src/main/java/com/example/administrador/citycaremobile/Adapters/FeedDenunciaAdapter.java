package com.example.administrador.citycaremobile.Adapters;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.bumptech.glide.signature.ObjectKey;
import com.example.administrador.citycaremobile.Activities.AcessoActivity;
import com.example.administrador.citycaremobile.Activities.DenunciaActivity;
import com.example.administrador.citycaremobile.Exceptions.APIError;
import com.example.administrador.citycaremobile.Fragments.AtualizarDenunciaFragment;
import com.example.administrador.citycaremobile.Fragments.ComentarioFragment;
import com.example.administrador.citycaremobile.Fragments.MapsFragment;
import com.example.administrador.citycaremobile.Modelo.Agiliza;
import com.example.administrador.citycaremobile.Modelo.Cidadao;
import com.example.administrador.citycaremobile.Modelo.Comentario;
import com.example.administrador.citycaremobile.Modelo.Denuncia;
import com.example.administrador.citycaremobile.Modelo.Empresa;
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
import java.util.HashMap;
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

    private ArrayList<Postagem> postagens = new ArrayList<>();
    private HashMap<Integer, Object> usuarios = new HashMap<>();
    private Context context;
    private Drawable ic_agilizaSelected, ic_agilizaUnselected;

    public FeedDenunciaAdapter(Context context) {
        this.postagens = new ArrayList<>();
        this.context = context;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public FeedDenunciaAdapter.FeedDenunciaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_denuncia, parent, false);
        ic_agilizaSelected = context.getResources().getDrawable(R.drawable.ic_action_agiliza_orange, null);
        ic_agilizaUnselected = context.getResources().getDrawable(R.drawable.ic_action_agiliza, null);
        return new FeedDenunciaHolder(view);
    }

    @Override
    public void onBindViewHolder(final FeedDenunciaHolder holder, final int position) {
        final Postagem post = postagens.get(position);
        final Boolean[] agilizado = {false};
        holder.semPostagens.setVisibility(View.GONE);
        holder.agilizarPost.setCompoundDrawablesWithIntrinsicBounds(ic_agilizaUnselected, null, null, null);
        holder.agilizarPost.setTextColor(context.getResources().getColor(R.color.com_facebook_button_background_color_focused_disabled));

        //Serviço para pegar dados de quem criou a denúncia e Binda-los
        final Service service = CallService.createService(Service.class);
        Call<Object> callUsuario = service.getUsuario(UsuarioApplication.getInstance().getToken(), post.getDenuncia().getLogin());
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
                            holder.nameProfilePost.setText(cidadao.getNome());
                            Glide.with(context).load(cidadao.getDirFotoUsuario()).into(holder.profilePicPost);
                        } else if (response.code() == 223) {
                            Object o = response.body();
                            String jsonEmpresa = gson.toJson(o);
                            Empresa empresa = gson.fromJson(jsonEmpresa, Empresa.class);
                            holder.nameProfilePost.setText(empresa.getNomeFantasia());
                            Glide.with(context).load(empresa.getDirFotoUsuario()).into(holder.profilePicPost);
                            usuarios.put(position, empresa);
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

        } else {
            if (usuarios.get(position) instanceof Cidadao) {
                holder.nameProfilePost.setText(((Cidadao) usuarios.get(position)).getNome());
                Glide.with(context).load(((Cidadao) usuarios.get(position)).getDirFotoUsuario()).into(holder.profilePicPost);
            } else {
                holder.nameProfilePost.setText(((Empresa) usuarios.get(position)).getNomeFantasia());
                Glide.with(context).load(((Empresa) usuarios.get(position)).getDirFotoUsuario()).into(holder.profilePicPost);

            }
        }

        //Bindando dados da postagem
        holder.timePost.setText(getPeriodo(new DateTime(post.getDenuncia().getDataDenuncia())));
        holder.descricaoPost.setText(post.getDenuncia().getDescricaoDenuncia());
        holder.categoriaPost.setText(post.getDenuncia().getCategoriaDenuncia().toString());
        Glide.with(context).load(post.getDenuncia().getDirFotoDenuncia()).apply(new RequestOptions().skipMemoryCache(true)).into(holder.denunciaPicPost);

        //Geocoder para endereço
        Geocoder geo = new Geocoder(context);
        try {
            List<Address> addresses = geo.getFromLocation(post.getDenuncia().getLatitude(), post.getDenuncia().getLongitude(), 1);
            holder.localizacaoPost.setText(addresses.get(0).getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
        }

        agilizado[0] = atualizarStatus(holder, post);

        if (post.getComentarios().size() >= 2) {
            holder.comentariosPostQnd.setText(post.getComentarios().size() + " comentários");
        } else if (post.getComentarios().size() == 1) {
            holder.comentariosPostQnd.setText("1 comentário");
        } else {
            holder.comentariosPostQnd.setText("Nenhum comentário");
        }

        //evento do agilizar
        holder.agilizarPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UsuarioApplication.getInstance().getUsuario() == null) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.AlertDialog);
                    dialog.setMessage("Você não está logado ao CityCare para agilizar uma denúncia.")
                            .setTitle("Oooops!")
                            .setPositiveButton("Acessar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(context, AcessoActivity.class);
                                    context.startActivity(i);
                                }
                            }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                } else {
                    final Agiliza agiliza;
                    if (agilizado[0]) {
                        agilizado[0] = false;
                        holder.agilizarPost.setCompoundDrawablesWithIntrinsicBounds(ic_agilizaUnselected, null, null, null);
                        holder.agilizarPost.setTextColor(context.getResources().getColor(R.color.com_facebook_button_background_color_focused_disabled));
                        if (UsuarioApplication.getInstance().getUsuario() instanceof Cidadao) {
                            agiliza = new Agiliza(((Cidadao) UsuarioApplication.getInstance().getUsuario()).getLoginCidadao(),
                                    post.getDenuncia(), false);
                        } else {
                            agiliza = new Agiliza(((Empresa) UsuarioApplication.getInstance().getUsuario()).getLoginEmpresa(),
                                    post.getDenuncia(), false);
                        }
                        holder.agilizarPost.setCompoundDrawablesWithIntrinsicBounds(ic_agilizaUnselected, null, null, null);
                        holder.agilizarPost.setTextColor(context.getResources().getColor(R.color.com_facebook_button_background_color_focused_disabled));
                        Call<Void> agilizar = service.agilizar(UsuarioApplication.getToken(), agiliza);
                        agilizar.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    for (Agiliza a : post.getAgilizas()) {
                                        if (a.compareTo(agiliza) == 1) {
                                            post.getAgilizas().remove(a);
                                            atualizarStatus(holder, post);
                                            return;
                                        }
                                    }
                                } else {
                                    agilizado[0] = true;
                                    holder.agilizarPost.setCompoundDrawablesWithIntrinsicBounds(ic_agilizaSelected, null, null, null);
                                    holder.agilizarPost.setTextColor(context.getResources().getColor(R.color.colorAscend));
                                    APIError error = ErrorUtils.parseError(response);
                                    Log.e("agilizaTrue", error.getMessage());
                                    Toasty.error(context, "Erro na conexão", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                agilizado[0] = true;
                                holder.agilizarPost.setCompoundDrawablesWithIntrinsicBounds(ic_agilizaSelected, null, null, null);
                                holder.agilizarPost.setTextColor(context.getResources().getColor(R.color.colorAscend));
                                Log.e("agilizaTrue", t.getMessage());
                                Toasty.error(context, "Erro na conexão", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        agilizado[0] = true;
                        if (UsuarioApplication.getInstance().getUsuario() instanceof Cidadao) {
                            agiliza = new Agiliza(((Cidadao) UsuarioApplication.getInstance().getUsuario()).getLoginCidadao(),
                                    post.getDenuncia(), true);
                        } else {
                            agiliza = new Agiliza(((Empresa) UsuarioApplication.getInstance().getUsuario()).getLoginEmpresa(),
                                    post.getDenuncia(), true);
                        }
                        holder.agilizarPost.setCompoundDrawablesWithIntrinsicBounds(ic_agilizaSelected, null, null, null);
                        holder.agilizarPost.setTextColor(context.getResources().getColor(R.color.colorAscend));
                        Call<Void> agilizar = service.agilizar(UsuarioApplication.getToken(), agiliza);
                        agilizar.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    post.getAgilizas().add(agiliza);
                                    atualizarStatus(holder, post);
                                    return;
                                } else {
                                    agilizado[0] = false;
                                    holder.agilizarPost.setCompoundDrawablesWithIntrinsicBounds(ic_agilizaUnselected, null, null, null);
                                    holder.agilizarPost.setTextColor(context.getResources().getColor(R.color.com_facebook_button_background_color_focused_disabled));
                                    APIError error = ErrorUtils.parseError(response);
                                    Log.e("agilizaTrue", error.getMessage());
                                    Toasty.error(context, "Erro na conexão", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                agilizado[0] = false;
                                holder.agilizarPost.setCompoundDrawablesWithIntrinsicBounds(ic_agilizaUnselected, null, null, null);
                                holder.agilizarPost.setTextColor(context.getResources().getColor(R.color.com_facebook_button_background_color_focused_disabled));
                                Log.e("agilizaTrue", t.getMessage());
                                Toasty.error(context, "Erro na conexão", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }
        });
        //evento do comentar
        holder.comentarPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                if (post.getComentarios() == null) {
                    ComentarioFragment cf = ComentarioFragment.newInstance(new ArrayList<Comentario>(0),
                            post.getDenuncia().getIdDenuncia(),
                            post.getDenuncia().getLogin().getIdLogin(), position);
                    cf.show(fm, "comentarios");
                } else {
                    ComentarioFragment cf = ComentarioFragment.newInstance(post.getComentarios(),
                            post.getDenuncia().getIdDenuncia(),
                            post.getDenuncia().getLogin().getIdLogin(), position);
                    cf.show(fm, "comentarios");

                }
            }
        });

        holder.toolbarPostMenu.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.editar_denuncia:
                        Intent i = new Intent(context, DenunciaActivity.class);
                        i.putExtra("denuncia", post.getDenuncia());
                        i.putExtra("posicao", position);
                        context.startActivity(i);
                        break;
                    case R.id.deletar_denuncia:
                        servicoDeletarDenuncia(position);
                        break;
                }
                return true;
            }
        });

        if (position == postagens.size() - 1) {
            carregarPostagens(post.getDenuncia().getIdDenuncia(), position, holder);
        }
    }


    public void setContext(Context context) {
        this.context = context;
    }

    public void inserirPostagem(Postagem post) {
        postagens.add(0, post);
        notifyItemInserted(getItemCount());
        notifyItemRangeChanged(0, getItemCount());
    }

    public void inserirData(ArrayList<Postagem> postagens) {
        this.postagens = postagens;
        notifyDataSetChanged();
    }

    public void carregarPostagens(final int idDenuncia, final int position, final FeedDenunciaHolder holder) {
        holder.progressBar.setVisibility(View.VISIBLE);
        Service service = CallService.createService(Service.class);
        Call<ArrayList<Postagem>> carregarPostagem = service.carregarPostagem(UsuarioApplication.getToken(),
                new Denuncia(idDenuncia));
        carregarPostagem.enqueue(new Callback<ArrayList<Postagem>>() {
            @Override
            public void onResponse(Call<ArrayList<Postagem>> call, Response<ArrayList<Postagem>> response) {
                if (response.isSuccessful()) {
                    if (response.body().isEmpty()) {
                        holder.progressBar.setVisibility(View.GONE);
                        holder.semPostagens.setVisibility(View.VISIBLE);
                        return;
                    } else {
                        postagens.addAll(response.body());
                        holder.progressBar.setVisibility(View.GONE);
                        notifyItemRangeInserted(position + 1, response.body().size());
                        return;
                    }
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    Log.e("carregarPostagens", error.getMessage());
                    Toasty.error(context, "Erro na conexão", Toast.LENGTH_LONG).show();
                    carregarPostagens(idDenuncia, position, holder);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Postagem>> call, Throwable t) {
                Log.e("carregarPostagens", t.getMessage());
                Toasty.error(context, "Erro na conexão", Toast.LENGTH_LONG).show();
                carregarPostagens(idDenuncia, position, holder);
            }
        });
    }

    public void atualizarPostagem(int position, Denuncia den) {
        postagens.get(position).setDenuncia(den);
        notifyItemChanged(position);
    }

    public void deletarPostagem(int position) {
        postagens.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }


    public boolean atualizarStatus(final FeedDenunciaHolder holder, Postagem post) {

        if (UsuarioApplication.getInstance().getUsuario() == null) {
            holder.toolbarPostMenu.setVisibility(View.GONE);
            if (post.getAgilizas().size() >= 2) {
                holder.statusPostQnd.setText(post.getAgilizas().size() + " pessoas agilizaram");
            } else if (post.getAgilizas().size() == 1) {
                holder.statusPostQnd.setText("1 pessoa agilizou");
            } else {
                holder.statusPostQnd.setText("Nenhum agiliza");
            }
        } else {
            if (UsuarioApplication.getInstance().getUsuario() instanceof Cidadao) {
                if (((Cidadao) UsuarioApplication.getInstance().getUsuario()).getLoginCidadao().getIdLogin() ==
                        post.getDenuncia().getLogin().getIdLogin()) {
                    holder.toolbarPostMenu.setVisibility(View.VISIBLE);
                }
                for (Agiliza a : post.getAgilizas()) {
                    if (a.getLogin().getIdLogin() == ((Cidadao) UsuarioApplication.getInstance().getUsuario()).getLoginCidadao().getIdLogin()) {
                        holder.agilizarPost.setCompoundDrawablesWithIntrinsicBounds(ic_agilizaSelected, null, null, null);
                        holder.agilizarPost.setTextColor(context.getResources().getColor(R.color.colorAscend));
                        if (post.getAgilizas().size() >= 2) {
                            if (post.getAgilizas().size() == 2) {
                                holder.statusPostQnd.setText("Você e mais 1 pessoa agilizaram");
                                return true;
                            } else {
                                holder.statusPostQnd.setText("Você e mais " + (post.getAgilizas().size() - 1) + " agilizaram");
                                return true;
                            }
                        } else {
                            if (post.getAgilizas().size() == 1) {
                                holder.statusPostQnd.setText("Você agilizou");
                                return true;
                            }
                        }
                    }
                }
                holder.agilizarPost.setCompoundDrawablesWithIntrinsicBounds(ic_agilizaUnselected, null, null, null);
                holder.agilizarPost.setTextColor(context.getResources().getColor(R.color.com_facebook_button_background_color_focused_disabled));
                if (post.getAgilizas().size() >= 2) {
                    holder.statusPostQnd.setText(post.getAgilizas().size() + " pessoas agilizaram");
                    return false;
                } else if (post.getAgilizas().size() == 1) {
                    holder.statusPostQnd.setText("1 pessoa agilizou");
                    return false;
                } else {
                    holder.statusPostQnd.setText("Nenhum agiliza");
                    return false;
                }
            } else if (UsuarioApplication.getInstance().getUsuario() instanceof Empresa) {
                if (((Empresa) UsuarioApplication.getInstance().getUsuario()).getLoginEmpresa().getIdLogin() ==
                        post.getDenuncia().getLogin().getIdLogin()) {
                    holder.toolbarPostMenu.setVisibility(View.VISIBLE);
                }
                for (Agiliza a : post.getAgilizas()) {
                    if (a.getLogin().getIdLogin() == ((Empresa) UsuarioApplication.getInstance().getUsuario()).getLoginEmpresa().getIdLogin()) {
                        holder.agilizarPost.setCompoundDrawablesWithIntrinsicBounds(ic_agilizaSelected, null, null, null);
                        holder.agilizarPost.setTextColor(context.getResources().getColor(R.color.colorAscend));
                        if (post.getAgilizas().size() >= 2) {
                            if (post.getAgilizas().size() == 2) {
                                holder.statusPostQnd.setText("Você e mais 1 pessoa agilizaram");
                                return true;
                            } else {
                                holder.statusPostQnd.setText("Você e mais " + (post.getAgilizas().size() - 1) + " agilizaram");
                                return true;
                            }
                        } else {
                            if (post.getAgilizas().size() == 1) {
                                holder.statusPostQnd.setText("Você agilizou");
                                return true;
                            }
                        }
                    }
                    if (post.getAgilizas().size() >= 2) {
                        holder.statusPostQnd.setText(post.getAgilizas().size() + " pessoas agilizaram");
                        return false;
                    } else if (post.getAgilizas().size() == 1) {
                        holder.statusPostQnd.setText("1 pessoa agilizou");
                        return false;
                    } else {
                        holder.statusPostQnd.setText("Nenhum agiliza");
                        return false;
                    }
                }
            }
        }
        return false;
    }

    private String getPeriodo(DateTime inicio) {
        Period period = new Period(inicio, DateTime.now());
        if (period.getYears() != 0) {
            if (period.getYears() > 1) {
                return period.getYears() + " anos atrás";
            } else {
                return period.getYears() + " ano atrás";
            }
        }
        if (period.getWeeks() != 0) {
            if (period.getWeeks() > 1) {
                return period.getWeeks() + " semanas atrás";
            } else {
                return period.getWeeks() + " semana atrás";
            }
        }
        if (period.getDays() != 0) {
            if (period.getDays() > 1) {
                return period.getDays() + " dias atrás";
            } else {
                return period.getDays() + " dia atrás";
            }
        }
        if (period.getHours() != 0) {
            if (period.getHours() > 1) {
                return period.getHours() + " horas atrás";
            } else {
                return period.getHours() + " hora atrás";
            }
        }
        if (period.getMinutes() != 0) {
            if (period.getMinutes() > 1) {
                return period.getSeconds() + " minutos atrás";
            } else {
                return period.getSeconds() + " minuto atrás";
            }
        }
        if (period.getSeconds() != 0) {
            if (period.getSeconds() > 1) {
                return period.getSeconds() + " segundos atrás";
            } else {
                return period.getSeconds() + " segundo atrás";
            }
        }
        return null;
    }

    public void servicoDeletarDenuncia(final int position) {
        Service service = CallService.createService(Service.class);
        final Call<Void> deletarDenuncia = service.excluirPostagem(UsuarioApplication.getToken(),
                new Denuncia(postagens.get(position).getDenuncia().getIdDenuncia()));
        deletarDenuncia.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    MapsFragment.getInstance().removeMarker(postagens.get(position));
                    deletarPostagem(position);
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    Log.e("deletarDenuncia", error.getMessage());
                    Toasty.error(context, "Erro na conexão", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("deletarDenuncia", t.getMessage());
                Toasty.error(context, "Erro na conexão", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onViewRecycled(FeedDenunciaHolder holder) {
        super.onViewRecycled(holder);
        Glide.with(context).clear(holder.denunciaPicPost);
    }

    @Override
    public int getItemCount() {
        return postagens.size();
    }

    class FeedDenunciaHolder extends RecyclerView.ViewHolder {

        CircleImageView profilePicPost;
        TextView nameProfilePost, timePost, descricaoPost, semPostagens,
                localizacaoPost, categoriaPost, statusPostQnd, comentariosPostQnd;
        Toolbar toolbarPostMenu;
        ImageView denunciaPicPost;
        Button comentarPost, agilizarPost;
        ProgressBar progressBar;

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
            agilizarPost = (Button) view.findViewById(R.id.bt_agilizar_post);
            comentarPost = (Button) view.findViewById(R.id.bt_comentar_post);
            statusPostQnd = (TextView) view.findViewById(R.id.tv_status_post_qtd);
            comentariosPostQnd = (TextView) view.findViewById(R.id.tv_comentarios_post_qtd);
            progressBar = (ProgressBar) view.findViewById(R.id.progress_bar_denuncia);
            semPostagens = (TextView) view.findViewById(R.id.sem_postagens);

            toolbarPostMenu.inflateMenu(R.menu.menu_denuncia);
        }
    }
}
