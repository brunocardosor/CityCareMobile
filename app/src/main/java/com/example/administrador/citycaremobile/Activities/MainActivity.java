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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrador.citycaremobile.Adapters.TabAdapter;

import com.example.administrador.citycaremobile.Exceptions.APIError;
import com.example.administrador.citycaremobile.Modelo.Cidadao;
import com.example.administrador.citycaremobile.Modelo.Empresa;
import com.example.administrador.citycaremobile.Modelo.UsuarioApplication;
import com.example.administrador.citycaremobile.R;
import com.example.administrador.citycaremobile.Services.CallService;
import com.example.administrador.citycaremobile.Services.Service;
import com.example.administrador.citycaremobile.Services.Token;
import com.example.administrador.citycaremobile.Utils.ErrorUtils;

import de.hdodenhof.circleimageview.CircleImageView;
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
        setSupportActionBar(toolbar);

        View headerView = navDrawer.getHeaderView(0);
        Menu menuView = navDrawer.getMenu();

        TextView nomeNavView = (TextView) headerView.findViewById(R.id.name_nav);
        CircleImageView picProfileNav = (CircleImageView) headerView.findViewById(R.id.pic_profile_drawer);

        if (UsuarioApplication.getInstance().getUsuario() != null) {
            btEntrar.setVisibility(View.GONE);
            btEntrar.setClickable(false);
            navDrawer.setVisibility(View.VISIBLE);
            toolbar.setTitle("");
            toolbar.setNavigationIcon(R.drawable.ic_drawable_menu);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(Gravity.START);
                }
            });
            if(UsuarioApplication.getInstance().getUsuario() instanceof Empresa){
                Empresa empresa = (Empresa) UsuarioApplication.getInstance().getUsuario();
                nomeNavView.setText(empresa.getNome_fantasia());
                picProfileNav.setImageURI(null);
            } else {
                Cidadao cidadao = (Cidadao) UsuarioApplication.getInstance().getUsuario();
                nomeNavView.setText(cidadao.getNome() + cidadao.getSobrenome());
                picProfileNav.setImageURI(null);
            }
        }

        btEntrar = (TextView) findViewById(R.id.move_to_login);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navDrawer = (NavigationView) findViewById(R.id.nav_drawer);
        navDrawer.setVisibility(View.GONE);
        navDrawer.setClickable(false);

        menuView.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.edit_profile){
                    Intent i = new Intent(MainActivity.this,EditProfileActivity.class);
                    startActivity(i);
                }
                return true;
            }
        });

        menuView.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.bt_logout){
                    UsuarioApplication.getInstance().logout();
                    onRestart();
                }
                return true;
            }
        });


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
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(UsuarioApplication.getInstance().getUsuario() == null){
            btEntrar.setVisibility(View.VISIBLE);
            btEntrar.setClickable(true);
            navDrawer.setVisibility(View.GONE);
            navDrawer.setClickable(false);
            toolbar.setNavigationIcon(null);
        } else {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_drawable_menu);
            navDrawer.setClickable(true);
            navDrawer.setVisibility(View.VISIBLE);
            btEntrar.setVisibility(View.GONE);
            btEntrar.setClickable(false);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
