package com.example.administrador.citycaremobile.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrador.citycaremobile.Adapters.TabAdapter;

import com.example.administrador.citycaremobile.Modelo.UsuarioApplication;
import com.example.administrador.citycaremobile.R;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView btEntrar;
    private SearchView searchView;
    private ImageView logoImage;
    private ViewPager viewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        btEntrar = (TextView) findViewById(R.id.move_to_login);
        searchView = (SearchView) findViewById(R.id.seach_button);

        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AcessoActivity.class);
                startActivity(i);
            }
        });

        //TAB LAYOUT
        TabLayout tabLayout = (TabLayout) findViewById(R.id.mTabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Home"));
        tabLayout.addTab(tabLayout.newTab().setText("Mapa"));
        tabLayout.addTab(tabLayout.newTab().setText("Perfil"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //VIEW PAGER
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(2);
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        //EVENTOS DO VIEW PAGER
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if(UsuarioApplication.getInstance() != null){
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_drawable_menu);
            btEntrar.setVisibility(View.INVISIBLE);
            btEntrar.setClickable(false);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(new UsuarioApplication().getUsuario() != null){
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_drawable_menu);
            btEntrar.setVisibility(View.INVISIBLE);
            btEntrar.setClickable(false);
        }
    }
}
