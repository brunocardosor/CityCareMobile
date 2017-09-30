package com.example.administrador.citycaremobile.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrador.citycaremobile.Adapters.TabAdapter;

import com.example.administrador.citycaremobile.Exceptions.APIError;
import com.example.administrador.citycaremobile.Modelo.UsuarioApplication;
import com.example.administrador.citycaremobile.R;
import com.example.administrador.citycaremobile.Services.CallService;
import com.example.administrador.citycaremobile.Services.Service;
import com.example.administrador.citycaremobile.Services.Token;
import com.example.administrador.citycaremobile.Utils.ErrorUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView btEntrar;
    private ViewPager viewPager;
    private DrawerLayout drawerLayout;
    private NavigationView navDrawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        btEntrar = (TextView) findViewById(R.id.move_to_login);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navDrawer = (NavigationView) findViewById(R.id.nav_drawer);
        navDrawer.setVisibility(View.GONE);
        navDrawer.setClickable(false);

        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AcessoActivity.class);
                startActivity(i);
            }
        });

        if (UsuarioApplication.getInstance().getUsuario() != null) {
            btEntrar.setVisibility(View.GONE);
            btEntrar.setClickable(false);
            navDrawer.setVisibility(View.VISIBLE);
            toolbar.setTitle("");
            toolbar.setNavigationIcon(R.drawable.ic_drawable_menu);
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

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    public void initSearchView(){
    }

    @Override
    protected void onResume() {
        super.onResume();
        btEntrar.setVisibility(View.VISIBLE);
        btEntrar.setClickable(true);
        navDrawer.setVisibility(View.GONE);
        navDrawer.setClickable(false);


        if (UsuarioApplication.getInstance().getUsuario() != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_drawable_menu);
            navDrawer.setClickable(true);
            navDrawer.setVisibility(View.VISIBLE);
            btEntrar.setVisibility(View.GONE);
            btEntrar.setClickable(false);
        }
    }
}
