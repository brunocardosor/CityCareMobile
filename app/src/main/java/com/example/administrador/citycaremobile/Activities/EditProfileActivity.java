package com.example.administrador.citycaremobile.Activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrador.citycaremobile.Fragments.EditProfileCidadaoFragment;
import com.example.administrador.citycaremobile.Fragments.ProfileFragment;
import com.example.administrador.citycaremobile.Modelo.Cidadao;
import com.example.administrador.citycaremobile.Modelo.Empresa;
import com.example.administrador.citycaremobile.Modelo.UsuarioApplication;
import com.example.administrador.citycaremobile.R;

public class EditProfileActivity extends AppCompatActivity {

    FragmentManager fm;
    FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        if(UsuarioApplication.getInstance().getUsuario() instanceof Cidadao){
            ft.add(R.id.frame_edit_profile, ProfileFragment.newInstance(((Cidadao) UsuarioApplication.getInstance().getUsuario()).getLoginCidadao().getIdLogin()));
            ft.commit();
        } else {
            ft.add(R.id.frame_edit_profile, ProfileFragment.newInstance(((Empresa) UsuarioApplication.getInstance().getUsuario()).getLoginEmpresa().getIdLogin()));
            ft.commit();
        }
    }
}
