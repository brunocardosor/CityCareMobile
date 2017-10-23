package com.example.administrador.citycaremobile.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrador.citycaremobile.Exceptions.APIError;
import com.example.administrador.citycaremobile.Modelo.Categoria;
import com.example.administrador.citycaremobile.Modelo.Cidadao;
import com.example.administrador.citycaremobile.Modelo.Denuncia;
import com.example.administrador.citycaremobile.Modelo.Empresa;
import com.example.administrador.citycaremobile.Modelo.UsuarioApplication;
import com.example.administrador.citycaremobile.R;
import com.example.administrador.citycaremobile.Services.CallService;
import com.example.administrador.citycaremobile.Services.Service;
import com.example.administrador.citycaremobile.Utils.DadosUtils;
import com.example.administrador.citycaremobile.Utils.ErrorUtils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;

import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class DenunciaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Spinner spinnerCategoria;
    private CircleImageView profilePicDenuncia;
    private TextView profileNomeDenuncia, localizacaoDenuncia;
    private EditText edtDescricaoDenuncia;
    private ImageButton btGetFromCamera, btGetLocation;
    private FloatingActionButton fabCloseImage;
    private ImageView picDenuncia;
    private Denuncia denuncia;
    private Button publicar;
    private Uri imgDenuncia;
    private File file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denuncia);

        denuncia = new Denuncia();

        toolbar = (Toolbar) findViewById(R.id.toolbar_edt_denuncia);
        spinnerCategoria = (Spinner) findViewById(R.id.spinner_categoria);
        profilePicDenuncia = (CircleImageView) findViewById(R.id.pic_profile_denuncia);
        profileNomeDenuncia = (TextView) findViewById(R.id.txt_username);
        edtDescricaoDenuncia = (EditText) findViewById(R.id.descricao_denuncia);
        picDenuncia = (ImageView) findViewById(R.id.foto_denuncia);
        fabCloseImage = (FloatingActionButton) findViewById(R.id.fab_drop_picture);
        btGetFromCamera = (ImageButton) findViewById(R.id.open_camera);
        btGetLocation = (ImageButton) findViewById(R.id.open_placepicker);
        localizacaoDenuncia = (TextView) findViewById(R.id.localizacao_denuncia);
        publicar = (Button) findViewById(R.id.bt_publicar);

        if (UsuarioApplication.getInstance().getUsuario() instanceof Cidadao) {
            profileNomeDenuncia.setText(((Cidadao) UsuarioApplication.getInstance().getUsuario()).getNome());
            Glide.with(this)
                    .load(((Cidadao) UsuarioApplication.getInstance().getUsuario()).getDir_foto_usuario())
                    .into(profilePicDenuncia);
        } else if (UsuarioApplication.getInstance().getUsuario() instanceof Empresa) {
            profileNomeDenuncia.setText(((Empresa) UsuarioApplication.getInstance().getUsuario()).getNomeFantasia());
            Glide.with(this)
                    .load(((Empresa) UsuarioApplication.getInstance().getUsuario()).getDirFotoUsuario())
                    .into(profilePicDenuncia);
        }

        DadosUtils dadosUtils = new DadosUtils(this);
        List<String> categorias = dadosUtils.listarCategoria();
        ArrayAdapter<String> categoriasAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categorias);
        spinnerCategoria.setAdapter(categoriasAdapter);

        btGetFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity(DenunciaActivity.this);
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
                picDenuncia.setImageURI(null);
                imgDenuncia = null;
                fabCloseImage.setVisibility(View.GONE);
            }
        });

        publicar.setOnClickListener(new View.OnClickListener() {

            ProgressDialog dialog = new ProgressDialog(DenunciaActivity.this);

            @Override
            public void onClick(View v) {
                dialog.setMessage("Enviando denuncia...");
                dialog.setIndeterminate(false);
                dialog.show();
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
                if (spinnerCategoria.getSelectedItemPosition() == 0 ||
                        TextUtils.isEmpty(edtDescricaoDenuncia.getText()) ||
                        TextUtils.isEmpty(localizacaoDenuncia.getText())) {
                    if (spinnerCategoria.getSelectedItemPosition() == 0) {
                        TextView erroSpinCidade = (TextView) spinnerCategoria.getSelectedView();
                        erroSpinCidade.setError(getString(R.string.campo_incorreto));
                        erroSpinCidade.setTextColor(Color.RED);
                        erroSpinCidade.setText(getString(R.string.campo_incorreto));
                        dialog.dismiss();
                    }
                    if (TextUtils.isEmpty(edtDescricaoDenuncia.getText())) {
                        edtDescricaoDenuncia.setError("Precisamos da sua descrição do problema");
                    }
                    if (TextUtils.isEmpty(localizacaoDenuncia.getText())) {
                        localizacaoDenuncia.setError("Precisamos da localização do problema");
                    }

                } else {
                    denuncia.setDescricaoDenuncia(edtDescricaoDenuncia.getText().toString());
                    Categoria categoria = new Categoria(spinnerCategoria.getSelectedItemPosition(), spinnerCategoria.getSelectedItem().toString());
                    denuncia.setCategoriaDenuncia(categoria);
                    if (UsuarioApplication.getInstance().getUsuario() instanceof Cidadao) {
                        denuncia.setLogin(((Cidadao) UsuarioApplication.getInstance().getUsuario()).getLoginCidadao());
                    } else if (UsuarioApplication.getInstance().getUsuario() instanceof Empresa) {
                        denuncia.setLogin(((Empresa) UsuarioApplication.getInstance().getUsuario()).getLoginEmpresa());
                    }
                    denuncia.setCategoriaDenuncia(categoria);
                    denuncia.setDataDenuncia(DateTime.now().toString());
                    denuncia.setStatusDenuncia(false);

                    RequestBody jsonDenuncia = RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(denuncia));

                    try {
                        String path = imgDenuncia.toString();
                        file = new File(new URI(path));

                        RequestBody requestFile = RequestBody.create(
                                MediaType.parse("multipart/form-data"),
                                file
                        );

                        MultipartBody.Part imgBody = MultipartBody.Part.createFormData("foto", file.getName(), requestFile);

                        Service service = CallService.createService(Service.class);
                        Call<Void> denunciar = service.denunciar(UsuarioApplication.getInstance().getToken().getToken(),
                                imgBody,
                                jsonDenuncia);
                        denunciar.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                                if (response.isSuccessful()) {
                                    if (response.code() != 204)
                                        dialog.dismiss();
                                    Toast.makeText(DenunciaActivity.this, "Sucesso", Toast.LENGTH_LONG).show();
                                } else {
                                    dialog.dismiss();
                                    APIError error = ErrorUtils.parseError(response);
                                    Log.e("ErrorDenunciar", error.getMessage());
                                    Toast.makeText(DenunciaActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();

                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                dialog.dismiss();
                                Log.e("ErroDenuncia", t.getLocalizedMessage());
                                Toast.makeText(DenunciaActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        Service service = CallService.createService(Service.class);
                        Call<Void> denunciar = service.denunciar(UsuarioApplication.getInstance().getToken().getToken(),
                                null,
                                jsonDenuncia);
                        denunciar.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                                if (response.isSuccessful()) {
                                    if (response.code() != 204)
                                        dialog.dismiss();
                                    Toast.makeText(DenunciaActivity.this, "Sucesso", Toast.LENGTH_LONG).show();
                                } else {
                                    dialog.dismiss();
                                    APIError error = ErrorUtils.parseError(response);
                                    Log.e("ErrorDenunciar", error.getMessage());
                                    Toast.makeText(DenunciaActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();

                                }

                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                dialog.dismiss();
                                Log.e("ErroDenuncia", t.getLocalizedMessage());
                                Toast.makeText(DenunciaActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                        Service service = CallService.createService(Service.class);
                        Call<Void> denunciar = service.denunciar(UsuarioApplication.getInstance().getToken().getToken(),
                                null,
                                jsonDenuncia);
                        denunciar.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                                if (response.isSuccessful()) {
                                    if (response.code() != 204)
                                        dialog.dismiss();
                                    Toast.makeText(DenunciaActivity.this, "Sucesso", Toast.LENGTH_LONG).show();
                                } else {
                                    dialog.dismiss();
                                    APIError error = ErrorUtils.parseError(response);
                                    Log.e("ErrorDenunciar", error.getMessage());
                                    Toast.makeText(DenunciaActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();

                                }

                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                dialog.dismiss();
                                Log.e("ErroDenuncia", t.getLocalizedMessage());
                                Toast.makeText(DenunciaActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }
        });

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
            imgDenuncia = CropImage.getPickImageResultUri(this, data);
            CropImage.activity(imgDenuncia)
                    .setOutputCompressQuality(70)
                    .setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imgDenuncia = result.getUri();
            picDenuncia.setImageURI(imgDenuncia);
            fabCloseImage.setVisibility(View.VISIBLE);
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
