package com.example.administrador.citycaremobile.Fragments;

import android.app.AlertDialog;
import android.app.UiAutomation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
import com.example.administrador.citycaremobile.Utils.SystemUtils;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedFragment extends Fragment {

    private FloatingActionButton bt_denunciar;
    private RecyclerView recyclerView;
    private static FeedDenunciaAdapter feedAdapter;
    private static FeedFragment instance;
    private SwipeRefreshLayout swipe;

    public static FeedDenunciaAdapter getFeedAdapter() {
        return feedAdapter;
    }

    public static void setFeedAdapter(FeedDenunciaAdapter feedAdapter) {
        FeedFragment.feedAdapter = feedAdapter;
    }

    public FeedFragment(){
        instance = this;
    }

    public static FeedFragment getInstance() {
        return instance;
    }

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
        swipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe_feed);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateFeed(feedAdapter);
            }
        });
        feedAdapter = new FeedDenunciaAdapter(getContext());
        insertData();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(feedAdapter);
        return view;
    }

    public void updateFeed(final FeedDenunciaAdapter feedAdapter) {
        Service service = CallService.createService(Service.class);
        Call<ArrayList<Postagem>> listPosts = service.getPostagens(UsuarioApplication.getToken());
        listPosts.enqueue(new Callback<ArrayList<Postagem>>() {
            @Override
            public void onResponse(Call<ArrayList<Postagem>> call, Response<ArrayList<Postagem>> response) {
                if(response.isSuccessful()){
                    feedAdapter.setContext(getContext());
                    feedAdapter.inserirData(response.body());
                    swipe.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Postagem>> call, Throwable t) {
                Log.e("PostRequest", t.getLocalizedMessage());
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                swipe.setRefreshing(false);
            }
        });

        Call<ArrayList<Postagem>> mapsList = service.mapsPostagens(UsuarioApplication.getToken());
        mapsList.enqueue(new Callback<ArrayList<Postagem>>() {
            @Override
            public void onResponse(Call<ArrayList<Postagem>> call, Response<ArrayList<Postagem>> response) {
                MapsFragment.getInstance().setData(response.body());
                swipe.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ArrayList<Postagem>> call, Throwable t) {

            }
        });
    }

    private void insertData() {
        Service service = CallService.createService(Service.class);
        Call<ArrayList<Postagem>> getFeedData = service.getPostagens(UsuarioApplication.getToken());
        getFeedData.enqueue(new Callback<ArrayList<Postagem>>() {
            @Override
            public void onResponse(Call<ArrayList<Postagem>> call, Response<ArrayList<Postagem>> response) {
                if (response.isSuccessful()) {
                    feedAdapter.inserirData(response.body());
                    feedAdapter.notifyDataSetChanged();
                    return;
                } else if (response.code() == 401) {
                    new SystemUtils().authenticateToken(getContext());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    Log.e("getToken", error.getMessage());
                    Toasty.error(getContext(), "Erro na conexão", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Postagem>> call, Throwable t) {
                Log.e("getToken", t.getMessage());
                Toasty.error(getContext(), "Erro na conexão", Toast.LENGTH_LONG).show();
            }
        });
    }
}
