package com.example.administrador.citycaremobile.Adapters;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrador.citycaremobile.Modelo.Agiliza;
import com.example.administrador.citycaremobile.Modelo.Comentario;
import com.example.administrador.citycaremobile.Modelo.Denuncia;
import com.example.administrador.citycaremobile.Modelo.Postagem;
import com.example.administrador.citycaremobile.R;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrador on 17/09/2017.
 */

public class FeedDenunciaAdapter extends RecyclerView.Adapter<FeedDenunciaAdapter.FeedDenunciaHolder> {

    private List<Postagem> postagens;
    private Context context;

    public FeedDenunciaAdapter(List<Postagem> postagens, Context context) {
        this.postagens = postagens;
        this.context = context;
    }

    @Override
    public FeedDenunciaAdapter.FeedDenunciaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_denuncia, parent, false);
        FeedDenunciaHolder viewHolder = new FeedDenunciaHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FeedDenunciaHolder holder, int position) {
        Postagem post = postagens.get(position);
        holder.profilePicPost.setImageURI(null);
        holder.nameProfilePost.setText(post.getCidadao().getNome());
        holder.timePost.setText("");
        holder.descricaoPost.setText(post.getDenuncia().getDescricaoDenuncia());
        holder.categoriaPost.setText(post.getDenuncia().getCategoriaDenuncia().toString());
        holder.denunciaPicPost.setImageURI(null);

        //INSERIR LOGICA PARA VERIFICAR SE O USUÁRIO JÀ AGILIZOU A POSTAGEM
        /*


         */
        if (post.getAgilizas().size() > 2) {
            holder.statusPost.setText(post.getAgilizas().size() + "pessoas agilizaram esta denúncia");
        } else {

        }


    }

    @Override
    public int getItemCount() {
        return 0;
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
            profilePicPost = (CircleImageView) view.findViewById(R.id.pic_profile_denuncia);
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
                    switch (item.getItemId()){
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

            edtComentario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
