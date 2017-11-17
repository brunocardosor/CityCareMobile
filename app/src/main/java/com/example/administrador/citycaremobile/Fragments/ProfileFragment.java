package com.example.administrador.citycaremobile.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrador.citycaremobile.Adapters.ProfileDenunciaAdapter;
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

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private Toolbar toolbar;
    private CircleImageView profilePicture;
    private TextView username, localizacao;
    private RecyclerView recyclerView;

    private static Integer idLogin;
    private ProfileDenunciaAdapter denunciaAdapter;

    private Object usuario;

    public static ProfileFragment newInstance(Integer id) {

        Bundle args = new Bundle();

        idLogin = id;

        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar_profile);
        profilePicture = (CircleImageView) view.findViewById(R.id.pic_profile_profile);
        username = (TextView) view.findViewById(R.id.nome_profile);
        localizacao = (TextView) view.findViewById(R.id.localizacao_profile);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_profile);
        popularView(idLogin);
        denunciaAdapter = new ProfileDenunciaAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(denunciaAdapter);
        getData(idLogin);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void getData(int idLogin) {
        Service service = CallService.createService(Service.class);
        Call<ArrayList<Postagem>> getDenunciaProfile = service.profileDenunciaData(UsuarioApplication.getToken(),
                new Login(idLogin));
        getDenunciaProfile.enqueue(new Callback<ArrayList<Postagem>>() {
            @Override
            public void onResponse(Call<ArrayList<Postagem>> call, Response<ArrayList<Postagem>> response) {
                if (response.isSuccessful()) {
                    denunciaAdapter.inserirData(response.body());
                    return;
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    Log.e("getToken", error.getMessage());
                    Toasty.error(getContext(), "Erro na conexão", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Postagem>> call, Throwable t) {
                Log.e("carregarPostagens", t.getMessage());
                Toasty.error(getContext(), "Erro na conexão", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void popularView(Integer idLogin) {
        Service service = CallService.createService(Service.class);
        Call<Object> callUser = service.getUsuario(UsuarioApplication.getInstance().getToken(), new Login(idLogin));
        callUser.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Gson gson = new Gson();
                if (response.code() == 222) {
                    Object o = response.body();
                    String jsonCidadao = gson.toJson(o);
                    Cidadao cidadao = gson.fromJson(jsonCidadao, Cidadao.class);
                    usuario = cidadao;
                    Glide.with(getContext()).load(((Cidadao) usuario).getDirFotoUsuario()).into(profilePicture);
                    username.setText(((Cidadao) usuario).getNome()+ " " +((Cidadao) usuario).getSobrenome());
                    localizacao.setText(((Cidadao) usuario).getCidade() + " - " + ((Cidadao) usuario).getEstado());
                } else if (response.code() == 223) {
                    Object o = response.body();
                    String jsonEmpresa = gson.toJson(o);
                    Empresa empresa = gson.fromJson(jsonEmpresa, Empresa.class);
                    usuario = empresa;
                    Glide.with(getContext()).load(((Empresa) usuario).getDirFotoUsuario()).into(profilePicture);
                    username.setText(((Empresa) usuario).getNomeFantasia());
                    localizacao.setText(((Cidadao) usuario).getCidade() + " - " + ((Cidadao) usuario).getEstado());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    Toasty.error(getContext(), "erro", Toast.LENGTH_LONG);
                    Log.e("Erro de parse", error.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e("Falha", t.getLocalizedMessage());
                Toasty.error(getContext(), "erro", Toast.LENGTH_LONG);
            }
        });
    }
}
