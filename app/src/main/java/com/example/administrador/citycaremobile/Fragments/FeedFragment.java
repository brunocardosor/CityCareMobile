package com.example.administrador.citycaremobile.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.administrador.citycaremobile.Activities.AcessoActivity;
import com.example.administrador.citycaremobile.Activities.DenunciaActivity;
import com.example.administrador.citycaremobile.Adapters.FeedDenunciaAdapter;
import com.example.administrador.citycaremobile.Exceptions.APIError;
import com.example.administrador.citycaremobile.Modelo.Postagem;
import com.example.administrador.citycaremobile.Modelo.UsuarioApplication;
import com.example.administrador.citycaremobile.R;
import com.example.administrador.citycaremobile.Services.CallService;
import com.example.administrador.citycaremobile.Services.Service;
import com.example.administrador.citycaremobile.Utils.ErrorUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedFragment extends Fragment {

    private FloatingActionButton bt_denunciar;
    private RecyclerView recyclerView;
    private FeedDenunciaAdapter feedAdapter = UsuarioApplication.getFeedDenuncia();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.denuncia_recyler);
        bt_denunciar = (FloatingActionButton) view.findViewById(R.id.fab_create_denuncia);
        bt_denunciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UsuarioApplication.getInstance().getUsuario() == null) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialog);
                    builder.setMessage("Você não está logado ao CityCare para fazer uma denúncia.")
                            .setTitle("Oooops!")
                            .setPositiveButton("Acessar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(getActivity(), AcessoActivity.class);
                                    startActivity(i);
                                }
                            }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                } else {
                    Intent i = new Intent(getActivity(), DenunciaActivity.class);
                    startActivity(i);
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(feedAdapter);
        Service service = CallService.createService(Service.class);
        Call<ArrayList<Postagem>> listPosts = service.getPostagens(UsuarioApplication.getToken());
        listPosts.enqueue(new Callback<ArrayList<Postagem>>() {
            @Override
            public void onResponse(Call<ArrayList<Postagem>> call, Response<ArrayList<Postagem>> response) {
                feedAdapter.setContext(getContext());
                for (Postagem p : response.body()){
                    feedAdapter.inserirPostagem(p);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Postagem>> call, Throwable t) {
                Log.e("PostRequest", t.getLocalizedMessage());
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}
