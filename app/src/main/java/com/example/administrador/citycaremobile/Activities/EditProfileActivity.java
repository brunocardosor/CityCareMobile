package com.example.administrador.citycaremobile.Activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrador.citycaremobile.Fragments.EditProfileCidadaoFragment;
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

        if(UsuarioApplication.getInstance().getUsuario() instanceof Cidadao){
            fm = getSupportFragmentManager();
            ft = fm.beginTransaction();
            ft.add(new EditProfileCidadaoFragment(),"CidadaoFrag");
            ft.commit();
        } else if(UsuarioApplication.getInstance().getUsuario() instanceof Empresa){
            fm = getSupportFragmentManager();
            ft = fm.beginTransaction();
            ft.add(new EditProfileCidadaoFragment(),"EmpresaFrag");
            ft.commit();
        }
    }
}
