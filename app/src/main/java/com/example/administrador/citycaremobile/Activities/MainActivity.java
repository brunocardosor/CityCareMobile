package com.example.administrador.citycaremobile.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrador.citycaremobile.Adapters.TabAdapter;

import com.example.administrador.citycaremobile.Modelo.Cidadao;
import com.example.administrador.citycaremobile.Modelo.Empresa;
import com.example.administrador.citycaremobile.Modelo.UsuarioApplication;
import com.example.administrador.citycaremobile.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView btEntrar;
    private ViewPager viewPager;
    private DrawerLayout drawerLayout;
    private NavigationView navDrawer;
    private TextView nomeNavView;
    private CircleImageView picProfileNav;
    private View headerView;
    private Menu menuView;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        navDrawer = (NavigationView) findViewById(R.id.nav_drawer);
        navDrawer.setClickable(false);

        btEntrar = (TextView) findViewById(R.id.move_to_login);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        headerView = navDrawer.getHeaderView(0);
        menuView = navDrawer.getMenu();

        nomeNavView = (TextView) headerView.findViewById(R.id.name_nav);
        picProfileNav = (CircleImageView) headerView.findViewById(R.id.pic_profile_drawer);

        //TAB LAYOUT
        tabLayout = (TabLayout) findViewById(R.id.mTabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Home"));
        tabLayout.addTab(tabLayout.newTab().setText("Mapa"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //VIEW PAGER
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(1);
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

        if (UsuarioApplication.getInstance().getUsuario() != null) {
            btEntrar.setVisibility(View.GONE);
            btEntrar.setClickable(false);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            toolbar.setTitle("");
            toolbar.setNavigationIcon(R.drawable.ic_drawable_menu);
            if(UsuarioApplication.getInstance().getUsuario() instanceof Empresa){
                Empresa empresa = (Empresa) UsuarioApplication.getInstance().getUsuario();
                nomeNavView.setText(empresa.getNomeFantasia());
                Glide.with(this).load(empresa.getDirFotoUsuario()).into(picProfileNav);
            } else {
                Cidadao cidadao = (Cidadao) UsuarioApplication.getInstance().getUsuario();
                nomeNavView.setText(cidadao.getNome() + " " +cidadao.getSobrenome());
                Glide.with(this).load(cidadao.getDirFotoUsuario()).into(picProfileNav);
            }
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

        menuView.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.edit_profile){
                    Intent i = new Intent(MainActivity.this,EditProfileActivity.class);
                    startActivity(i);
                    return true;
                }
                return false;

            }
        });

        menuView.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.bt_logout){
                    ProgressDialog dialog = ProgressDialog.show(MainActivity.this,"","Saindo...");
                    UsuarioApplication.getInstance().logout();
                    toolbar.setNavigationIcon(null);
                    btEntrar.setVisibility(View.VISIBLE);
                    btEntrar.setClickable(true);
                    drawerLayout.closeDrawer(Gravity.START);
                    dialog.dismiss();
                    return true;
                }
                return false;
            }
        });

        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AcessoActivity.class);
                startActivity(i);
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(UsuarioApplication.getInstance().getUsuario() == null){
            btEntrar.setVisibility(View.VISIBLE);
            btEntrar.setClickable(true);
            navDrawer.setClickable(false);
            toolbar.setNavigationIcon(null);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_drawable_menu);
            navDrawer.setClickable(true);
            btEntrar.setVisibility(View.GONE);
            btEntrar.setClickable(false);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);
            if(UsuarioApplication.getInstance().getUsuario() instanceof Empresa){
                Empresa empresa = (Empresa) UsuarioApplication.getInstance().getUsuario();
                nomeNavView.setText(empresa.getNomeFantasia());
                Glide.with(this).load(empresa.getDirFotoUsuario()).into(picProfileNav);
            } else {
                Cidadao cidadao = (Cidadao) UsuarioApplication.getInstance().getUsuario();
                nomeNavView.setText(cidadao.getNome() + " " +cidadao.getSobrenome());
                Glide.with(this).load(cidadao.getDirFotoUsuario()).into(picProfileNav);
            }
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }
}
