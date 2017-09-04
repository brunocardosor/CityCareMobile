package com.example.administrador.citycaremobile.Activities;

import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.administrador.citycaremobile.Fragments.CadastroFragment;
import com.example.administrador.citycaremobile.Fragments.LoginFragment;
import com.example.administrador.citycaremobile.Fragments.RecepcaoFragment;
import com.example.administrador.citycaremobile.R;

public class MainActivity extends AppCompatActivity
        implements RecepcaoFragment.OnFragmentInteractionListener,
        LoginFragment.OnFragmentInteractionListener,
        CadastroFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
