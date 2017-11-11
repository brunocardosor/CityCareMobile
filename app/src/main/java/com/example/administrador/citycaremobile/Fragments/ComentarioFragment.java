package com.example.administrador.citycaremobile.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrador.citycaremobile.Activities.AcessoActivity;
import com.example.administrador.citycaremobile.Adapters.ComentariosAdapter;
import com.example.administrador.citycaremobile.Exceptions.APIError;
import com.example.administrador.citycaremobile.Modelo.Cidadao;
import com.example.administrador.citycaremobile.Modelo.Comentario;
import com.example.administrador.citycaremobile.Modelo.Denuncia;
import com.example.administrador.citycaremobile.Modelo.Empresa;
import com.example.administrador.citycaremobile.Modelo.UsuarioApplication;
import com.example.administrador.citycaremobile.R;
import com.example.administrador.citycaremobile.Services.CallService;
import com.example.administrador.citycaremobile.Services.Service;
import com.example.administrador.citycaremobile.Utils.ErrorUtils;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComentarioFragment extends DialogFragment {

    private static ArrayList<Comentario> comentariosList;
    private static Integer idDenuncia;
    private static Integer idDenunciaLogin;
    private static Integer posicao;
    private RecyclerView recyclerView;
    private CircleImageView picProfileComentario;
    private EditText edtComentario;
    private Button btEnviar;
    private Toolbar toolbar;
    private FrameLayout layoutInfo;
    private Button buttonLogin;
    private ComentariosAdapter adapter;

    public ComentarioFragment() {
        // Required empty public constructor
    }

    public static ComentarioFragment newInstance(ArrayList<Comentario> comentarios, Integer denuncia, Integer DenunciaLogin, Integer position) {
        Bundle args = new Bundle();
        idDenuncia = denuncia;
        idDenunciaLogin = DenunciaLogin;
        comentariosList = comentarios;
        posicao = position;
        ComentarioFragment fragment = new ComentarioFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comentario, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.comentario_recycler);
        layoutInfo = (FrameLayout) view.findViewById(R.id.sem_comentarios_layout);
        buttonLogin = (Button) view.findViewById(R.id.button_login_comentario);
        picProfileComentario = (CircleImageView) view.findViewById(R.id.pic_profile_novo_comentario);
        edtComentario = (EditText) view.findViewById(R.id.edt_novo_comentario);
        btEnviar = (Button) view.findViewById(R.id.button_enviar_comentario);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar_fragment_comentar);
        if (UsuarioApplication.getInstance().getUsuario() != null) {
            if (UsuarioApplication.getInstance().getUsuario() instanceof Cidadao) {
                Glide.with(getContext()).load(((Cidadao) UsuarioApplication.getInstance().getUsuario()).getDirFotoUsuario()).into(picProfileComentario);
                edtComentario.requestFocus();
            } else {
                Glide.with(getContext()).load(((Empresa) UsuarioApplication.getInstance().getUsuario()).getDirFotoUsuario()).into(picProfileComentario);
                edtComentario.requestFocus();
            }
        } else {
            toolbar.setVisibility(View.GONE);
            btEnviar.setClickable(false);
            edtComentario.setClickable(false);
            viewButtonLogin(View.VISIBLE, true);
        }
        if (comentariosList.isEmpty()) {
            viewSemComentarios(View.VISIBLE, true);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new ComentariosAdapter(getContext(), comentariosList, this ,idDenunciaLogin);
        recyclerView.setAdapter(adapter);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AcessoActivity.class);
                getContext().startActivity(i);
            }
        });

        btEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtComentario.getText().toString())) {
                    edtComentario.requestFocus();
                    edtComentario.animate();
                } else {
                    final Comentario comentario = new Comentario(null, edtComentario.getText().toString(), ((Cidadao) UsuarioApplication.getInstance().getUsuario()).getLoginCidadao(), new Denuncia(idDenuncia));
                    Service service = CallService.createService(Service.class);
                    Call<Comentario> enviarComentario = service.enviarComentario(UsuarioApplication.getToken(), comentario);
                    enviarComentario.enqueue(new Callback<Comentario>() {
                        @Override
                        public void onResponse(Call<Comentario> call, Response<Comentario> response) {
                            if (response.isSuccessful()) {
                                edtComentario.setText(null);
                                adapter.inserirComentario(response.body());
                                UsuarioApplication.getFeedDenuncia().notifyItemChanged(posicao);
                                return;
                            } else {
                                APIError error = ErrorUtils.parseError(response);
                                Log.e("enviarComentario", error.getMessage());
                                Toasty.error(getContext(), "Erro na conexão", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Comentario> call, Throwable t) {
                            Log.e("enviarComentario", t.getMessage());
                            Toasty.error(getContext(), "Erro na conexão", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (UsuarioApplication.getInstance().getUsuario() != null) {
            adapter.notifyDataSetChanged();
            toolbar.setVisibility(View.VISIBLE);
            btEnviar.setClickable(true);
            edtComentario.setClickable(true);
            if (UsuarioApplication.getInstance().getUsuario() instanceof Cidadao) {
                Glide.with(getContext()).load(((Cidadao) UsuarioApplication.getInstance().getUsuario()).getDirFotoUsuario()).into(picProfileComentario);
                edtComentario.requestFocus();
            } else {
                Glide.with(getContext()).load(((Empresa) UsuarioApplication.getInstance().getUsuario()).getDirFotoUsuario()).into(picProfileComentario);
                edtComentario.requestFocus();
            }
            edtComentario.requestFocus();


        } else {
            toolbar.setVisibility(View.GONE);
            btEnviar.setClickable(false);
            edtComentario.setClickable(false);
            viewButtonLogin(View.VISIBLE, false);
        }
        if (comentariosList.isEmpty()) {
            viewSemComentarios(View.VISIBLE,false);
        } else {
            viewSemComentarios(View.GONE,true);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void viewSemComentarios(int visibility, boolean clickable) {
        layoutInfo.setVisibility(visibility);
        layoutInfo.setClickable(clickable);
    }

    public void viewButtonLogin(int visibility, boolean clickable) {
        buttonLogin.setVisibility(visibility);
        buttonLogin.setClickable(clickable);
    }

    public void atualizarPostagem(){
        UsuarioApplication.getFeedDenuncia().notifyItemChanged(posicao);
    }
}
