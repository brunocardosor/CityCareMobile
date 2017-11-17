package com.example.administrador.citycaremobile.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrador.citycaremobile.Exceptions.APIError;
import com.example.administrador.citycaremobile.Modelo.Login;
import com.example.administrador.citycaremobile.Modelo.UsuarioApplication;
import com.example.administrador.citycaremobile.R;
import com.example.administrador.citycaremobile.Services.CallService;
import com.example.administrador.citycaremobile.Services.Service;
import com.example.administrador.citycaremobile.Utils.ErrorUtils;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrador on 16/11/2017.
 */

public class RecuperarSenhaFragment extends DialogFragment {

    private EditText edtEmail;
    private Button cancelar, recuperar;
    private TextView tvSucesso;
    private TextInputLayout tvLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recuperar_senha,container, false);

        tvLayout = (TextInputLayout) view.findViewById(R.id.view_edt_text_email);
        tvSucesso = (TextView) view.findViewById(R.id.view_sucesso);
        edtEmail = (EditText) view.findViewById(R.id.edt_email_recuperar);
        edtEmail.requestFocus();
        cancelar = (Button) view.findViewById(R.id.button_cancelar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        recuperar = (Button) view.findViewById(R.id.button_recuperar_senha);
        recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recuperar.setClickable(false);
                Service service = CallService.createService(Service.class);
                Call<Void> recuperarSenha = service.recuperarSenha(UsuarioApplication.getToken(),edtEmail.getText().toString());
                recuperarSenha.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()){
                            recuperar.setClickable(true);
                            tvLayout.setVisibility(View.GONE);
                            tvSucesso.setVisibility(View.VISIBLE);
                        } else if(response.code() == 404){
                            recuperar.setClickable(true);
                            edtEmail.setError("Não há acesso relacionado a este e-mail");
                        }
                        else{
                            recuperar.setClickable(true);
                            APIError error = ErrorUtils.parseError(response);
                            Log.e("recuperarSenha", error.getMessage());
                            Toasty.error(getContext(),"Erro na conexão", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        recuperar.setClickable(true);
                        Log.e("recuperarSenha", t.getMessage());
                        Toasty.error(getContext(),"Erro na conexão", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
