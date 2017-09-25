package com.example.administrador.citycaremobile.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrador.citycaremobile.Exceptions.APIError;
import com.example.administrador.citycaremobile.Modelo.Cidadao;
import com.example.administrador.citycaremobile.Modelo.Empresa;
import com.example.administrador.citycaremobile.Modelo.UsuarioApplication;
import com.example.administrador.citycaremobile.Services.CallService;
import com.example.administrador.citycaremobile.Services.Service;
import com.example.administrador.citycaremobile.Utils.ErrorUtils;
import com.example.administrador.citycaremobile.Utils.PatternUtils;
import com.facebook.login.widget.LoginButton;
import com.example.administrador.citycaremobile.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {
    //View Atributes
    private EditText edtLogin, edtSenha;
    private LoginButton loginButtonFacebook;
    private Button btLogin, btMoveToCadastro, btRecuperarSenha;
    private PatternUtils patternUtils;

    public LoginFragment(){

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
                if(TextUtils.isEmpty(edtLogin.getText())){
                    edtLogin.setError("Campo necessário para Acesso");
                }
                if(TextUtils.isEmpty(edtSenha.getText())){
                    edtSenha.setError("Campo nessário para Acesso");
                }else if(edtSenha.getText().length() >= 8){
                    edtSenha.setError("Tamanho mínimo para senha é de 8 dígitos");
                } else {
                    String login = edtLogin.getText().toString();
                    String senha = edtSenha.getText().toString();

                    if(patternUtils.emailValido(edtLogin.getText().toString())){
                        Service service = CallService.createService(Service.class);
                        Call<Object> call = service.getAcessByEmail(login, senha);
                        call.enqueue(new Callback<Object>() {
                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {
                                if(response.isSuccessful()){
                                    if (response.code() == 1){
                                        Cidadao cidadao = (Cidadao) response.body();
                                        UsuarioApplication.getInstance().setUsuario(cidadao);
                                        getActivity().finish();
                                    } else if(response.code() == 2){
                                        Empresa empresa = (Empresa) response.body();
                                        UsuarioApplication.getInstance().setUsuario(empresa);
                                        getActivity().finish();
                                    } else {
                                        APIError error = ErrorUtils.parseError(response);
                                        Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {

                            }
                        });
                    }
                }
            }
        });
        btRecuperarSenha = (Button) view.findViewById(R.id.bt_recupera_senha);
        btRecuperarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btMoveToCadastro = (Button) view.findViewById(R.id.bt_move_to_cadastro);
        btMoveToCadastro.setOnClickListener(new View.OnClickListener() {
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
        loginButtonFacebook.setOnClickListener(new View.OnClickListener() {
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
