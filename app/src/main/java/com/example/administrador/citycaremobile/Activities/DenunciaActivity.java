package com.example.administrador.citycaremobile.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrador.citycaremobile.Modelo.Categoria;
import com.example.administrador.citycaremobile.Modelo.Cidadao;
import com.example.administrador.citycaremobile.Modelo.Denuncia;
import com.example.administrador.citycaremobile.Modelo.Empresa;
import com.example.administrador.citycaremobile.Modelo.UsuarioApplication;
import com.example.administrador.citycaremobile.R;
import com.example.administrador.citycaremobile.Utils.DadosUtils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DenunciaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Spinner spinCategoria;
    private CircleImageView profilePicDenuncia;
    private TextView profileNomeDenuncia, localizacaoDenuncia;
    private EditText edtDescricaoDenuncia;
    private ImageButton btGetFromCamera, btGetFromGalery, btGetLocation;
    private FloatingActionButton fabCloseImage;
    private ImageView picDenuncia;
    private Denuncia denuncia;
    private Button publicar;
    private Uri imgDenuncia;
    private File denunciaFoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denuncia);

        denuncia = new Denuncia();

        toolbar = (Toolbar) findViewById(R.id.toolbar_edt_denuncia);
        spinCategoria = (Spinner) findViewById(R.id.spinner_categoria);
        profilePicDenuncia = (CircleImageView) findViewById(R.id.pic_profile_denuncia);
        profileNomeDenuncia = (TextView) findViewById(R.id.txt_username);
        edtDescricaoDenuncia = (EditText) findViewById(R.id.descricao_denuncia);
        picDenuncia = (ImageView) findViewById(R.id.foto_denuncia);
        fabCloseImage = (FloatingActionButton) findViewById(R.id.fab_drop_picture);
        btGetFromCamera = (ImageButton) findViewById(R.id.open_camera);
        btGetFromGalery = (ImageButton) findViewById(R.id.open_galery);
        btGetLocation = (ImageButton) findViewById(R.id.open_placepicker);
        localizacaoDenuncia = (TextView) findViewById(R.id.localizacao_denuncia);
        publicar = (Button) findViewById(R.id.bt_publicar);

        if (UsuarioApplication.getInstance().getUsuario() instanceof Cidadao)
            profileNomeDenuncia.setText(((Cidadao) UsuarioApplication.getInstance().getUsuario()).getNome());
        else if (UsuarioApplication.getInstance().getUsuario() instanceof Empresa)
            profileNomeDenuncia.setText(((Empresa) UsuarioApplication.getInstance().getUsuario()).getNome_fantasia());

        DadosUtils dadosUtils = new DadosUtils(this);
        List<String> categorias = dadosUtils.listarCategoria();
        ArrayAdapter<String> categoriasAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categorias);
        spinCategoria.setAdapter(categoriasAdapter);

        btGetFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                denunciaFoto = new File(Environment.getExternalStorageDirectory(), "denunciaFoto");
                camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(denunciaFoto));
                startActivityForResult(Intent.createChooser(camera,"Capturar foto do problema"),123);
            }
        });

        btGetFromGalery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(Intent
                        .createChooser(new Intent(Intent.ACTION_PICK,
                                        MediaStore.Images.Media.INTERNAL_CONTENT_URI),
                                "Selecionar foto"), 124);
            }
        });

        btGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(DenunciaActivity.this), 125);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        fabCloseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picDenuncia.setImageBitmap(null);
                fabCloseImage.setVisibility(View.GONE);
            }
        });

        publicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinCategoria.getSelectedItemPosition() == 0 ||
                        TextUtils.isEmpty(edtDescricaoDenuncia.getText()) ||
                        TextUtils.isEmpty(localizacaoDenuncia.getText())) {
                    if (spinCategoria.getSelectedItemPosition() == 0) {

                    }
                    if (TextUtils.isEmpty(edtDescricaoDenuncia.getText())) {

                    }
                    if (TextUtils.isEmpty(localizacaoDenuncia.getText())) {

                    }

                } else {
                    denuncia.setDescricaoDenuncia(edtDescricaoDenuncia.getText().toString());
                    Categoria categoria = new Categoria(spinCategoria.getSelectedItemPosition(), spinCategoria.getSelectedItem().toString());
                    denuncia.setCategoriaDenuncia(categoria);
                    if (UsuarioApplication.getInstance().getUsuario() instanceof Cidadao)
                        denuncia.setLogin(((Cidadao) UsuarioApplication.getInstance().getUsuario()).getLoginCidadao());
                    if (UsuarioApplication.getInstance().getUsuario() instanceof Empresa)
                        denuncia.setLogin(((Empresa) UsuarioApplication.getInstance().getUsuario()).getLoginEmpresa());
                }
            }
        });


        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 123) {
                imgDenuncia = Uri.fromFile(denunciaFoto);
                picDenuncia.setImageURI(imgDenuncia);
                fabCloseImage.setVisibility(View.VISIBLE);
                fabCloseImage.setClickable(true);
            }
            if (requestCode == 124) {
                imgDenuncia = data.getData();
                picDenuncia.setImageURI(imgDenuncia);
                fabCloseImage.setVisibility(View.VISIBLE);
                fabCloseImage.setClickable(true);
            }
            if (requestCode == 125) {
                Place place = PlacePicker.getPlace(this, data);

                double latitude = place.getLatLng().latitude;
                double longitude = place.getLatLng().longitude;

                localizacaoDenuncia.setText(place.getAddress());
                denuncia.setLatitude(latitude);
                denuncia.setLongitude(longitude);
                Geocoder geocoder = new Geocoder(this);
                try {
                    List<Address> local = geocoder.getFromLocation(latitude, longitude, 1);
                    denuncia.setCidade(local.get(0).getLocality());
                    denuncia.setEstado(local.get(0).getAdminArea());
                } catch (IOException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
