package com.example.administrador.citycaremobile.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.example.administrador.citycaremobile.Adapters.ComentariosAdapter;
import com.example.administrador.citycaremobile.Modelo.Cidadao;
import com.example.administrador.citycaremobile.Modelo.Comentario;
import com.example.administrador.citycaremobile.Modelo.Empresa;
import com.example.administrador.citycaremobile.Modelo.UsuarioApplication;
import com.example.administrador.citycaremobile.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class ComentarioFragment extends DialogFragment {

    private static ArrayList<Comentario> comentariosList;
    private RecyclerView recyclerView;
    private CircleImageView picProfileComentario;
    private EditText edtComentario;
    private Button btEnviar;
    private Toolbar toolbar;

    public ComentarioFragment() {
        // Required empty public constructor
    }

    public static ComentarioFragment newInstance(ArrayList<Comentario> comentarios) {
        Bundle args = new Bundle();
        comentariosList = comentarios;
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
        picProfileComentario = (CircleImageView) view.findViewById(R.id.pic_profile_novo_comentario);
        edtComentario = (EditText) view.findViewById(R.id.edt_novo_comentario);
        btEnviar = (Button) view.findViewById(R.id.button_enviar_comentario);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar_fragment_comentar);
        if(UsuarioApplication.getInstance().getUsuario()!= null){
            if(UsuarioApplication.getInstance().getUsuario() instanceof Cidadao){
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
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        ComentariosAdapter adapter = new ComentariosAdapter(getContext(),comentariosList);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
