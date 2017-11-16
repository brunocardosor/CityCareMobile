package com.example.administrador.citycaremobile.Activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrador.citycaremobile.Fragments.AtualizarDenunciaFragment;
import com.example.administrador.citycaremobile.Fragments.CriarDenunciaFragment;
import com.example.administrador.citycaremobile.Modelo.Denuncia;
import com.example.administrador.citycaremobile.R;

public class DenunciaActivity extends AppCompatActivity {

    private Denuncia denuncia;
    private Integer position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denuncia);

        denuncia = getIntent().getExtras().getParcelable("denuncia");
        position = getIntent().getIntExtra("posicao", 0);

        if (denuncia != null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.frame_denuncia_layout, AtualizarDenunciaFragment.newInstance(denuncia, position));
            ft.commit();

        } else {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.frame_denuncia_layout, new CriarDenunciaFragment());
            ft.commit();
        }

    }
}

