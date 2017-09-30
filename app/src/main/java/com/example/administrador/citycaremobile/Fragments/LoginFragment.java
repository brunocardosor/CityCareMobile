package com.example.administrador.citycaremobile.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrador.citycaremobile.Exceptions.APIError;
import com.example.administrador.citycaremobile.Modelo.Cidadao;
import com.example.administrador.citycaremobile.Modelo.Empresa;
import com.example.administrador.citycaremobile.Modelo.Login;
import com.example.administrador.citycaremobile.Modelo.UsuarioApplication;
import com.example.administrador.citycaremobile.Services.CallService;
import com.example.administrador.citycaremobile.Services.Service;
import com.example.administrador.citycaremobile.Utils.ErrorUtils;
import com.facebook.login.widget.LoginButton;
import com.example.administrador.citycaremobile.R;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class LoginFragment extends Fragment {
    //View Atributes
    private EditText edtLogin, edtSenha;
    private LoginButton loginButtonFacebook;
    private Button btLogin, btMoveToCadastro, btRecuperarSenha;

    public LoginFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        edtLogin = (EditText) view.findViewById(R.id.edt_login);
        edtSenha = (EditText) view.findViewById(R.id.edt_senha);
        btLogin = (Button) view.findViewById(R.id.bt_login);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtLogin.getText())) {
                    edtLogin.setError("Campo necessário para Acesso");
                }
                if (TextUtils.isEmpty(edtSenha.getText())) {
                    edtSenha.setError("Campo nessário para Acesso");
                } else if (edtSenha.getText().length() < 8) {
                    edtSenha.setError("Tamanho mínimo para senha é de 8 dígitos");
                } else {
                    String login = edtLogin.getText().toString();
                    String senha = edtSenha.getText().toString();
                    Login acesso = new Login();
                    acesso.setLogin(login);
                    acesso.setSenha(senha);

                    Service service = CallService.createService(Service.class);
                    Call<Object> call = service.login("application/json",
                            UsuarioApplication.getToken().getToken(),
                            acesso);
                    call.enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            Gson gson = new Gson();
                            if (response.code() == 222) {
                                Object o = response.body();
                                String jsonCidadao = gson.toJson(o);
                                Cidadao cidadao = gson.fromJson(jsonCidadao,Cidadao.class);
                                UsuarioApplication.getInstance().setUsuario(cidadao);
                                getActivity().finish();
                            } else if (response.code() == 223) {
                                Object o = response.body();
                                String jsonEmpresa = gson.toJson(o);
                                Empresa empresa = gson.fromJson(jsonEmpresa,Empresa.class);
                                UsuarioApplication.getInstance().setUsuario(empresa);
                                getActivity().finish();
                            } else {
                                APIError error = ErrorUtils.parseError(response);
                                Log.i("ERRO", error.getMessage());
                                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {
                            Log.e("ERRORLogin", t.getMessage());
                            Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        btRecuperarSenha = (Button) view.findViewById(R.id.bt_recupera_senha);
        btRecuperarSenha.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

            }
        });
        btMoveToCadastro = (Button) view.findViewById(R.id.bt_move_to_cadastro);
        btMoveToCadastro.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_acess_activity, new CadastroFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        loginButtonFacebook = (LoginButton) view.findViewById(R.id.bt_facebook_login);
        loginButtonFacebook.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

            }
        });

        edtLogin.requestFocus();
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return true;
    }

}
