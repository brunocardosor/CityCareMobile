package com.example.administrador.citycaremobile.Fragments;

import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrador.citycaremobile.Exceptions.APIError;
import com.example.administrador.citycaremobile.Modelo.Empresa;
import com.example.administrador.citycaremobile.Modelo.Solucao;
import com.example.administrador.citycaremobile.Modelo.UsuarioApplication;
import com.example.administrador.citycaremobile.R;
import com.example.administrador.citycaremobile.Services.CallService;
import com.example.administrador.citycaremobile.Services.Service;
import com.example.administrador.citycaremobile.Utils.ErrorUtils;
import com.google.gson.Gson;

import org.joda.time.DateTime;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrador on 16/11/2017.
 */

public class SolucaoFragment extends DialogFragment{

    private static Solucao sol;
    private Empresa usuario;

    private CircleImageView picProfileSolucao;
    private TextView tvNomeProfileSolucao, tvTimeSolucao, tvDescricaoSolucao;
    private ImageView picSolucao;

    public static SolucaoFragment newInstance(Solucao solucao) {

        Bundle args = new Bundle();
        sol = solucao;
        SolucaoFragment fragment = new SolucaoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public SolucaoFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_solucao, container, false);

        picProfileSolucao = (CircleImageView) view.findViewById(R.id.pic_profile_solucao);
        tvNomeProfileSolucao = (TextView) view.findViewById(R.id.name_profile_solucao);
        tvTimeSolucao = (TextView) view.findViewById(R.id.time_solucao);
        tvDescricaoSolucao = (TextView) view.findViewById(R.id.descricao_solucao);
        picSolucao = (ImageView) view.findViewById(R.id.denuncia_picture_solucao);

        Glide.with(getContext()).load(sol.getDirFotoSolucao()).into(picSolucao);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if(usuario == null){
            Service service = CallService.createService(Service.class);
            Call<Object> callUsuario = service.getUsuario(UsuarioApplication.getToken(),sol.getLoginSolucao());
            callUsuario.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    if(response.isSuccessful()){
                        Gson gson = new Gson();
                        String json = gson.toJson(response.body());
                        usuario = gson.fromJson(json, Empresa.class);
                        Glide.with(getContext()).load(usuario.getDirFotoUsuario()).into(picProfileSolucao);
                        tvNomeProfileSolucao.setText(usuario.getNomeFantasia());
                    } else {
                        APIError error = ErrorUtils.parseError(response);
                        Toasty.error(getContext(), "Erro na conexão", Toast.LENGTH_LONG).show();
                        Log.e("carregarUsuario", error.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    Toasty.error(getContext(), "Erro na conexão", Toast.LENGTH_LONG).show();
                    Log.e("carregarUsuario", t.getMessage());
                }
            });
        } else {
            Glide.with(getContext()).load(usuario.getDirFotoUsuario()).into(picProfileSolucao);
            tvNomeProfileSolucao.setText(usuario.getNomeFantasia());
        }
        tvTimeSolucao.setText(FeedFragment.getFeedAdapter().getPeriodo(new DateTime(sol.getDataSolucao())));
        tvDescricaoSolucao.setText(sol.getDescricaoSolucao());
    }
}
