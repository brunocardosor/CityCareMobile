package com.example.administrador.citycaremobile.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.administrador.citycaremobile.Modelo.Agiliza;
import com.example.administrador.citycaremobile.Modelo.Categoria;
import com.example.administrador.citycaremobile.Modelo.Cidadao;
import com.example.administrador.citycaremobile.Modelo.Comentario;
import com.example.administrador.citycaremobile.Modelo.Denuncia;
import com.example.administrador.citycaremobile.Modelo.Empresa;
import com.example.administrador.citycaremobile.Modelo.Postagem;
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
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrador on 16/11/2017.
 */


public class CriarDenunciaFragment extends Fragment {


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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_denuncia, container, false);

        denuncia = new Denuncia();

        toolbar = (Toolbar) view.findViewById(R.id.toolbar_edt_denuncia);
        spinnerCategoria = (Spinner) view.findViewById(R.id.spinner_categoria);
        profilePicDenuncia = (CircleImageView) view.findViewById(R.id.pic_profile_denuncia);
        profileNomeDenuncia = (TextView) view.findViewById(R.id.txt_username);
        edtDescricaoDenuncia = (EditText) view.findViewById(R.id.descricao_denuncia);
        picDenuncia = (ImageView) view.findViewById(R.id.foto_denuncia);
        fabCloseImage = (FloatingActionButton) view.findViewById(R.id.fab_drop_picture);
        btGetFromCamera = (ImageButton) view.findViewById(R.id.open_camera);
        btGetLocation = (ImageButton) view.findViewById(R.id.open_placepicker);
        localizacaoDenuncia = (TextView) view.findViewById(R.id.localizacao_denuncia);
        publicar = (Button) view.findViewById(R.id.bt_publicar);

        if (UsuarioApplication.getInstance().getUsuario() instanceof Cidadao) {
            profileNomeDenuncia.setText(((Cidadao) UsuarioApplication.getInstance().getUsuario()).getNome());
            Glide.with(this)
                    .load(((Cidadao) UsuarioApplication.getInstance().getUsuario()).getDirFotoUsuario())
                    .into(profilePicDenuncia);
        } else if (UsuarioApplication.getInstance().getUsuario() instanceof Empresa) {
            profileNomeDenuncia.setText(((Empresa) UsuarioApplication.getInstance().getUsuario()).getNomeFantasia());
            Glide.with(this)
                    .load(((Empresa) UsuarioApplication.getInstance().getUsuario()).getDirFotoUsuario())
                    .into(profilePicDenuncia);
        }

        DadosUtils dadosUtils = new DadosUtils(getContext());
        List<String> categorias = dadosUtils.listarCategoria();
        ArrayAdapter<String> categoriasAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, categorias);
        spinnerCategoria.setAdapter(categoriasAdapter);

        btGetFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity(getActivity(), CriarDenunciaFragment.this);
            }
        });

        btGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(getActivity()), 125);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        localizacaoDenuncia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(getActivity()), 125);
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

            ProgressDialog dialog = new ProgressDialog(getContext());

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
                        spinnerCategoria.requestFocus();
                    }
                    if (TextUtils.isEmpty(edtDescricaoDenuncia.getText())) {
                        edtDescricaoDenuncia.setError("Precisamos da sua descrição do problema");
                        dialog.dismiss();
                        edtDescricaoDenuncia.requestFocus();
                    }
                    if (TextUtils.isEmpty(localizacaoDenuncia.getText())) {
                        localizacaoDenuncia.setError("Precisamos da localização do problema");
                        dialog.dismiss();
                        localizacaoDenuncia.requestFocus();
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
                    denuncia.setStatusDenuncia(1);

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
                        Call<Denuncia> denunciar = service.denunciar(UsuarioApplication.getInstance().getToken(),
                                imgBody,
                                jsonDenuncia);
                        denunciar.enqueue(new Callback<Denuncia>() {
                            @Override
                            public void onResponse(Call<Denuncia> call, Response<Denuncia> response) {
                                if (response.isSuccessful()) {
                                    if (response.code() != 204) {
                                        Postagem post = new Postagem(response.body(),
                                                new ArrayList<Agiliza>(0),
                                                new ArrayList<Comentario>(0));
                                        FeedFragment.getFeedAdapter().inserirPostagem(post);
                                        MapsFragment mp = MapsFragment.getInstance();
                                        mp.addDenuncia(post);
                                        dialog.dismiss();
                                        getActivity().finish();
                                    }
                                } else {
                                    dialog.dismiss();
                                    APIError error = ErrorUtils.parseError(response);
                                    Log.e("ErrorDenunciar", error.getMessage());
                                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();

                                }
                            }

                            @Override
                            public void onFailure(Call<Denuncia> call, Throwable t) {
                                dialog.dismiss();
                                Log.e("ErroDenuncia", t.getLocalizedMessage());
                                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        dialog.dismiss();
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext(), R.style.AlertDialog);
                        dialog.setTitle("Ops!")
                                .setMessage("É necessária uma imagem para prosseguir com a Denúncia")
                                .setNegativeButton("Entendi", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        btGetFromCamera.callOnClick();
                                    }
                                }).show();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                        dialog.dismiss();
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext(), R.style.AlertDialog);
                        dialog.setTitle("Ops!")
                                .setMessage("É necessária uma imagem para prosseguir com a Denúncia")
                                .setNegativeButton("Entendi", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        btGetFromCamera.callOnClick();
                                    }
                                }).show();
                    }
                }
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
                imgDenuncia = CropImage.getPickImageResultUri(getContext(), data);
                CropImage.activity(imgDenuncia)
                        .setOutputCompressQuality(70)
                        .setMaxCropResultSize(720, 720)
                        .setAspectRatio(1, 1)
                        .setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                        .start(getContext(), this);
            }
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                imgDenuncia = result.getUri();
                picDenuncia.setImageURI(imgDenuncia);
                fabCloseImage.setVisibility(View.VISIBLE);
            }
            if (requestCode == 125) {
                Place place = PlacePicker.getPlace(getContext(), data);

                double latitude = place.getLatLng().latitude;
                double longitude = place.getLatLng().longitude;

                localizacaoDenuncia.setText(place.getAddress());
                denuncia.setLatitude(latitude);
                denuncia.setLongitude(longitude);
                Geocoder geocoder = new Geocoder(getContext());
                try {
                    List<Address> local = geocoder.getFromLocation(latitude, longitude, 1);
                    denuncia.setCidade(local.get(0).getLocality());
                    denuncia.setEstado(local.get(0).getAdminArea());
                } catch (IOException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
