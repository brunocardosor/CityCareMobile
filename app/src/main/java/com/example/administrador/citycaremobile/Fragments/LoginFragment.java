package com.example.administrador.citycaremobile.Fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import com.example.administrador.citycaremobile.Utils.SystemUtils;
import com.facebook.login.widget.LoginButton;
import com.example.administrador.citycaremobile.R;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.lang.annotation.Annotation;

import es.dmoral.toasty.Toasty;
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
                } else if (!new SystemUtils().verificaConexao(getContext())) {
                    Toasty.error(getContext(), "Sem conexão com a internet").show();
                } else {
                    String login = edtLogin.getText().toString();
                    String senha = edtSenha.getText().toString();
                    Login acesso = new Login();
                    acesso.setLogin(login);
                    acesso.setSenha(senha);

                    Service service = CallService.createService(Service.class);
                    Call<Object> call = service.login("application/json",
                            UsuarioApplication.getInstance().getToken(),
                            acesso);
                    call.enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(final Call<Object> call, Response<Object> response) {

                            if (response.isSuccessful()) {
                                Gson gson = new Gson();
                                if (response.code() == 222) {
                                    Object o = response.body();
                                    String jsonCidadao = gson.toJson(o);
                                    Cidadao cidadao = gson.fromJson(jsonCidadao, Cidadao.class);
                                    if (cidadao.getLoginCidadao().isStatus_login()) {
                                        try {
                                            UsuarioApplication.getInstance().setUsuario(cidadao);
                                            FeedFragment.getFeedAdapter().notifyDataSetChanged();
                                            saveLogin(cidadao);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        getActivity().finish();
                                    } else {
                                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                        builder.setTitle("Acesso desativado")
                                                .setMessage("O acesso utilizado não está ativado. Deseja ativa-lo?")
                                                .setPositiveButton("Ativar", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Service service = CallService.createService(Service.class);

                                                        //serviço de re-ativação da conta
                                                    }
                                                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                                    }

                                } else if (response.code() == 223) {
                                    Object o = response.body();
                                    String jsonEmpresa = gson.toJson(o);
                                    Empresa empresa = gson.fromJson(jsonEmpresa, Empresa.class);
                                    if (empresa.getLoginEmpresa().isStatus_login()) {
                                        try {
                                            UsuarioApplication.getInstance().setUsuario(empresa);
                                            FeedFragment.getFeedAdapter().notifyDataSetChanged();
                                            saveLogin(empresa);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        getActivity().finish();
                                    } else {
                                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                        builder.setTitle("Acesso desativado")
                                                .setMessage("O acesso utilizado não está ativado. Deseja ativa-lo?")
                                                .setPositiveButton("Ativar", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Service service = CallService.createService(Service.class);
                                                        Toast.makeText(getContext(), "Falta Implementar", Toast.LENGTH_LONG).show();
                                                        //serviço de re-ativação da conta
                                                    }
                                                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                                    }

                                }

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
                FragmentManager fm = getFragmentManager();
                RecuperarSenhaFragment rst = new RecuperarSenhaFragment();
                rst.show(fm, "k");
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

        /*loginButtonFacebook = (LoginButton) view.findViewById(R.id.bt_facebook_login);
        loginButtonFacebook.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

            }
        });*/

        edtLogin.requestFocus();
        return view;
    }

    private void saveLogin(Object o){
        Login login;
        SharedPreferences.Editor editor = UsuarioApplication.getInstance().getPreferences().edit();
        if(o instanceof Cidadao){
            editor.putInt("idCidadao",((Cidadao) o).getIdCidadao());
            editor.putString("nome", ((Cidadao) o).getNome());
            editor.putString("sobrenome", ((Cidadao) o).getSobrenome());
            editor.putString("sexo", ((Cidadao) o).getSexo());
            editor.putString("cidade", ((Cidadao) o).getCidade());
            editor.putString("estado", ((Cidadao) o).getEstado());
            editor.putString("dirFoto", ((Cidadao) o).getDirFotoUsuario());
            login = ((Cidadao) o).getLoginCidadao();
            editor.putBoolean("administrator",login.isAdministrador());
            editor.putBoolean("statusAcc", login.isStatus_login());
            editor.putInt("idLogin", login.getIdLogin());
            editor.putString("email", login.getEmail());
            editor.putString("login", login.getLogin());
            editor.apply();
        } else if(o instanceof Empresa) {
            editor.putInt("idEmpresa", ((Empresa) o).getIdEmpresa());
            editor.putString("cnpj", ((Empresa) o).getCnpj());
            editor.putString("razaoSocial", ((Empresa) o).getRazaoSocial());
            editor.putString("nomeFantasia", ((Empresa) o).getNomeFantasia());
            editor.putString("cidade", ((Empresa) o).getCidade());
            editor.putString("estado", ((Empresa) o).getEstado());
            editor.putString("dirFoto", ((Empresa) o).getDirFotoUsuario());
            login = ((Empresa) o).getLoginEmpresa();
            editor.putBoolean("administrator",login.isAdministrador());
            editor.putBoolean("statusAcc", login.isStatus_login());
            editor.putInt("idLogin", login.getIdLogin());
            editor.putString("email", login.getEmail());
            editor.putString("login", login.getLogin());
            editor.apply();
        }
    }
}
