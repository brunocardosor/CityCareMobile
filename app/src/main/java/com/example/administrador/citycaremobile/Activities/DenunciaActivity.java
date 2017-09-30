package com.example.administrador.citycaremobile.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.administrador.citycaremobile.Modelo.Categoria;
import com.example.administrador.citycaremobile.Modelo.Denuncia;
import com.example.administrador.citycaremobile.R;
import com.example.administrador.citycaremobile.Utils.DadosUtils;

import java.util.List;

public class DenunciaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Spinner spinCategoria;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denuncia);

        toolbar = (Toolbar) findViewById(R.id.toolbar_edt_denuncia);
        spinCategoria = (Spinner) findViewById(R.id.spinner_categoria);

        DadosUtils dadosUtils = new DadosUtils(this);
        List<String> categorias = dadosUtils.listarCategoria();
        ArrayAdapter<String> categoriasAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,categorias);
        spinCategoria.setAdapter(categoriasAdapter);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
