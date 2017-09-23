package com.example.administrador.citycaremobile.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.login.widget.LoginButton;
import com.example.administrador.citycaremobile.R;

public class LoginFragment extends Fragment {
    //View Atributes
    private EditText edtLogin, edtSenha;
    private LoginButton loginButtonFacebook;
    private Button btLogin, btMoveToCadastro, btRecuperarSenha;

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
