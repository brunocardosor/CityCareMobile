package com.example.administrador.citycaremobile.Activities;

import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.administrador.citycaremobile.Fragments.CadastroFragment;
import com.example.administrador.citycaremobile.Fragments.LoginFragment;
import com.example.administrador.citycaremobile.R;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button btEntrar;
    private SearchView searchView;
    private ImageView logoImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        btEntrar = (Button) findViewById(R.id.dialog_login_button);
        searchView = (SearchView) findViewById(R.id.seach_button);
        logoImage = (ImageView) findViewById(R.id.logo_toolbar);

        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                LoginFragment lf = new LoginFragment();
                lf.show(fm,"LoginDialog");
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoImage.setVisibility(View.GONE);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                logoImage.setVisibility(View.VISIBLE);
                return false;
            }
        });
    }
}
