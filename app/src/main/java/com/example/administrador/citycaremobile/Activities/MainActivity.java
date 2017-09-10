package com.example.administrador.citycaremobile.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.administrador.citycaremobile.Fragments.CadastroFragment;
import com.example.administrador.citycaremobile.Fragments.LoginFragment;
import com.example.administrador.citycaremobile.Fragments.RecepcaoFragment;
import com.example.administrador.citycaremobile.R;

public class MainActivity extends AppCompatActivity
        implements RecepcaoFragment.OnFragmentInteractionListener,
        LoginFragment.OnFragmentInteractionListener,
        CadastroFragment.OnFragmentInteractionListener {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fManager = getSupportFragmentManager();

        FragmentTransaction fTransaction = fManager.beginTransaction();
        fTransaction.add(R.id.main_fragment, new RecepcaoFragment());
        fTransaction.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
