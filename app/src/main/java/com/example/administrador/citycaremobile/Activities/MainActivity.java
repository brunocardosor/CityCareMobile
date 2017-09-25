package com.example.administrador.citycaremobile.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrador.citycaremobile.Adapters.TabAdapter;

import com.example.administrador.citycaremobile.Modelo.Cidadao;
import com.example.administrador.citycaremobile.Modelo.Empresa;
import com.example.administrador.citycaremobile.Modelo.UsuarioApplication;
import com.example.administrador.citycaremobile.R;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView btEntrar;
    private SearchView searchView;
    private ViewPager viewPager;
    private DrawerLayout drawerLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        btEntrar = (TextView) findViewById(R.id.move_to_login);
        drawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer);
        drawerLayout.setVisibility(View.GONE);
        drawerLayout.setClickable(false);

        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AcessoActivity.class);
                startActivity(i);
            }
        });

        if(UsuarioApplication.getInstance().getUsuario() != null){
            btEntrar.setVisibility(View.GONE);
            btEntrar.setClickable(false);
            drawerLayout.setVisibility(View.VISIBLE);
            toolbar.setNavigationIcon(R.drawable.ic_drawable_menu);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(Gravity.START);
                }
            });
        }

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        btEntrar.setVisibility(View.VISIBLE);
        btEntrar.setClickable(true);
        drawerLayout.setVisibility(View.GONE);
        drawerLayout.setClickable(false);


        if(UsuarioApplication.getInstance().getUsuario() != null){
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_drawable_menu);
            drawerLayout.setClickable(true);
            drawerLayout.setVisibility(View.VISIBLE);
            btEntrar.setVisibility(View.GONE);
            btEntrar.setClickable(false);
        }
    }
}
