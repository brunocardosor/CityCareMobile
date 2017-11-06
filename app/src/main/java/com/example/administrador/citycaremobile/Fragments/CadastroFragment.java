package com.example.administrador.citycaremobile.Fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrador.citycaremobile.Exceptions.APIError;
import com.example.administrador.citycaremobile.Modelo.Cidadao;
import com.example.administrador.citycaremobile.Modelo.Login;
import com.example.administrador.citycaremobile.Modelo.UsuarioApplication;
import com.example.administrador.citycaremobile.R;
import com.example.administrador.citycaremobile.Services.CallService;
import com.example.administrador.citycaremobile.Services.Service;
import com.example.administrador.citycaremobile.Utils.DadosUtils;
import com.example.administrador.citycaremobile.Utils.ErrorUtils;
import com.example.administrador.citycaremobile.Utils.PatternUtils;
import com.example.administrador.citycaremobile.Utils.SystemUtils;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroFragment extends Fragment {

    //Atributos da View
    private Toolbar toolbar;
    private CircleImageView profileImage;
    private TextView txtSexo;
    private FloatingActionButton fabGetPicture;
    private EditText edtNome, edtSobrenome, edtEmail, edtLogin, edtSenha;
    private Spinner spinnerCidade, spinnerEstado;
    private RadioButton rbMasculino, rbFeminino;
    private Button btCadastrar;
    private Uri imageUri;
    private File file;
    private ProgressBar pbLogin, pbEmail;
    private Drawable iconUncheck;
    private Drawable iconCheck;
    private Login login;

    private boolean emailValido = false;
    private boolean loginValido = false;

    private PatternUtils patternUtils = new PatternUtils();


    public CadastroFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastro, container, false);
        //Relacionando Atributos de View com a View
        toolbar = (Toolbar) view.findViewById(R.id.toolbar_transparente);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        login = new Login();

        iconCheck = getResources().getDrawable(R.drawable.ic_check_black, null);

        pbEmail = (ProgressBar) view.findViewById(R.id.progress_bar_email);
        pbLogin = (ProgressBar) view.findViewById(R.id.progress_bar_login);

        profileImage = (CircleImageView) view.findViewById(R.id.pic_profile);
        fabGetPicture = (FloatingActionButton) view.findViewById(R.id.fab_get_picture);
        //Ação do Botão para adicionar foto
        fabGetPicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity(getActivity(), CadastroFragment.this);
            }
        });

        edtNome = (EditText) view.findViewById(R.id.edt_nome);
        edtSobrenome = (EditText) view.findViewById(R.id.edt_sobrenome);
        txtSexo = (TextView) view.findViewById(R.id.radial_group);
        rbMasculino = (RadioButton) view.findViewById(R.id.radio_masculino);
        rbFeminino = (RadioButton) view.findViewById(R.id.radio_feminino);
        spinnerEstado = (Spinner) view.findViewById(R.id.spin_estado);
        spinnerCidade = (Spinner) view.findViewById(R.id.spin_cidade);

        ArrayAdapter<String> estadoAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, new DadosUtils(getContext()).listarEstados());
        spinnerEstado.setAdapter(estadoAdapter);
        spinnerEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    ArrayAdapter<String> cidadeAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, new DadosUtils(getContext()).listarCidades(position));
                    spinnerCidade.setAdapter(cidadeAdapter);
                    spinnerCidade.setClickable(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        edtLogin = (EditText) view.findViewById(R.id.edt_cadastro_login);
        edtLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                edtLogin.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                pbLogin.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(s.toString())) {
                    pbLogin.setVisibility(View.GONE);
                    edtLogin.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                } else {
                    login.setLogin(s.toString());
                    Service service = CallService.createService(Service.class);
                    Call<Void> callLogin = service.verificarLogin(UsuarioApplication.getInstance().getToken(), login);
                    callLogin.enqueue(new Callback<Void>() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            switch (response.code()) {
                                case 202:
                                    pbLogin.setVisibility(View.GONE);
                                    edtLogin.setError("Este login já está em uso");
                                    loginValido = false;
                                    return;
                                case 204:
                                    if(TextUtils.isEmpty(s.toString())){
                                        edtLogin.setError(getString(R.string.campo_incorreto));
                                        pbLogin.setVisibility(View.GONE);
                                    } else {
                                        pbLogin.setVisibility(View.GONE);
                                        edtLogin.setCompoundDrawablesWithIntrinsicBounds(null, null, iconCheck, null);
                                        loginValido = true;
                                        return;
                                    }
                                default:
                                    APIError error = ErrorUtils.parseError(response);
                                    Toasty.error(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("Erro na conexão", t.getMessage());
                            if (!new SystemUtils().verificaConexao(getContext())) {
                                Toasty.error(getContext(), "Sem conexão com a internet", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtEmail = (EditText) view.findViewById(R.id.edt_cadastro_email);
        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                edtEmail.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                pbEmail.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(s.toString())) {
                    pbEmail.setVisibility(View.GONE);
                    edtEmail.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                } else {
                    login.setEmail(s.toString());
                    Service service = CallService.createService(Service.class);
                    Call<Void> callLogin = service.verificarEmail(UsuarioApplication.getInstance().getToken(), login);
                    callLogin.enqueue(new Callback<Void>() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.isSuccessful()){
                                switch (response.code()) {
                                    case 202:
                                        pbEmail.setVisibility(View.GONE);
                                        edtEmail.setError("Este e-mail já tem uma conta vinculada");
                                        emailValido = false;
                                        return;
                                    case 204:
                                        if(TextUtils.isEmpty(s.toString())){
                                            edtEmail.setError(getString(R.string.campo_incorreto));
                                            pbEmail.setVisibility(View.GONE);
                                            return;
                                        } else {
                                            pbEmail.setVisibility(View.GONE);
                                            edtEmail.setCompoundDrawablesWithIntrinsicBounds(null, null, iconCheck, null);
                                            emailValido = true;
                                            return;
                                        }
                                    default:
                                        APIError error = ErrorUtils.parseError(response);
                                        Toasty.error(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("Erro na conexão", t.getMessage());
                            if (!new SystemUtils().verificaConexao(getContext())) {
                                Toasty.error(getContext(), "Sem conexão com a internet", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        edtSenha = (EditText) view.findViewById(R.id.edt_cadastro_senha);

        btCadastrar = (Button) view.findViewById(R.id.bt_cadastrar);


        //Ação do Botão de Cadastro
        btCadastrar.setOnClickListener(new View.OnClickListener() {
            ProgressDialog dialog = new ProgressDialog(getActivity());

            @Override
            public void onClick(View v) {

                dialog.setMessage("Cadastrando...");
                dialog.setIndeterminate(false);
                dialog.show();


                if (TextUtils.isEmpty(edtNome.getText())) {
                    edtNome.setError(getString(R.string.campo_incorreto));
                    dialog.dismiss();
                }
                if (!rbFeminino.isChecked() && !rbMasculino.isChecked()) {
                    txtSexo.setError(getString(R.string.campo_incorreto));
                    dialog.dismiss();
                }
                if (spinnerEstado.getSelectedItemPosition() == 0) {
                    TextView erroSpinCidade = (TextView) spinnerEstado.getSelectedView();
                    erroSpinCidade.setError(getString(R.string.campo_incorreto));
                    erroSpinCidade.setTextColor(Color.RED);
                    erroSpinCidade.setText(getString(R.string.campo_incorreto));
                    dialog.dismiss();
                }
                if (spinnerCidade.getSelectedItemPosition() == 0) {
                    TextView erroSpinCidade = (TextView) spinnerCidade.getSelectedView();
                    erroSpinCidade.setError(getString(R.string.campo_incorreto));
                    erroSpinCidade.setTextColor(Color.RED);
                    erroSpinCidade.setText(getString(R.string.campo_incorreto));
                    dialog.dismiss();
                }
                if (TextUtils.isEmpty(edtLogin.getText())) {
                    edtLogin.setError(getString(R.string.campo_incorreto));
                    dialog.dismiss();
                }
                if (TextUtils.isEmpty(edtEmail.getText())) {
                    edtEmail.setError(getString(R.string.campo_incorreto));
                    dialog.dismiss();
                } else if (!patternUtils.emailValido(edtEmail.getText().toString())) {
                    edtEmail.setError("E-mail Inválido");
                }
                if (TextUtils.isEmpty(edtSenha.getText())) {
                    edtSenha.setError(getString(R.string.campo_incorreto));
                    dialog.dismiss();
                } else if (edtSenha.getText().length() < 8) {
                    edtSenha.setError("A senha deve ter 8 ou mais dígitos");
                    dialog.dismiss();
                }
                if (!emailValido) {
                    edtEmail.setError("Este e-mail já tem uma conta vinculada");
                }
                if (!loginValido) {
                    edtLogin.setError("Este login já está em uso");
                }
                if (!new SystemUtils().verificaConexao(getContext())) {
                    Toasty.error(getContext(), "Sem conexão com a internet").show();
                    dialog.dismiss();
                }
                if (!TextUtils.isEmpty(edtNome.getText()) && (rbFeminino.isChecked() || rbMasculino.isChecked())
                        && spinnerEstado.getSelectedItemPosition() != 0 && spinnerCidade.getSelectedItemPosition() != 0
                        && patternUtils.emailValido(edtEmail.getText().toString())
                        && !TextUtils.isEmpty(edtLogin.getText()) && !TextUtils.isEmpty(edtEmail.getText())
                        && !TextUtils.isEmpty(edtSenha.getText()) && edtSenha.getText().length() >= 8
                        && emailValido && loginValido && new SystemUtils().verificaConexao(getContext())) {
                    //Instancia de Login
                    login.setLogin(edtLogin.getText().toString());
                    login.setEmail(edtEmail.getText().toString());
                    login.setSenha(edtSenha.getText().toString());
                    login.setStatus_login(true);
                    login.setAdministrador(false);

                    //Instancia de Cidadao
                    final Cidadao cidadao = new Cidadao();
                    cidadao.setNome(edtNome.getText().toString());
                    if (!TextUtils.isEmpty(edtSobrenome.getText())) {
                        cidadao.setSobrenome(edtSobrenome.getText().toString());
                    }
                    if (rbMasculino.isChecked()) {
                        cidadao.setSexo("Masculino");
                    } else {
                        cidadao.setSexo("Feminino");
                    }

                    cidadao.setCidade(spinnerCidade.getSelectedItem().toString());
                    cidadao.setEstado(spinnerEstado.getSelectedItem().toString());
                    cidadao.setLoginCidadao(login);
                    try {
                        String path = imageUri.toString();
                        file = new File(new URI(path));
                        RequestBody requestFile = RequestBody.create(
                                MediaType.parse("multipart/form-data"),
                                file
                        );

                        MultipartBody.Part fotoBody = MultipartBody.Part.createFormData("foto", file.getName(), requestFile);

                        Service service = CallService.createService(Service.class);
                        Call<Void> cadastrarCidadao = service.postCidadao(UsuarioApplication.getInstance().getToken(),
                                cidadao,
                                fotoBody);
                        cadastrarCidadao.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    if (response.code() == 201) {
                                        dialog.dismiss();
                                        FragmentManager fm = getFragmentManager();
                                        fm.popBackStack();
                                    }
                                } else {
                                    APIError error = ErrorUtils.parseError(response);
                                    new Throwable(error.getMessage());
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("ErrorCadastro", t.getLocalizedMessage());
                                Toasty.error(getContext(), "Erro na conexão").show();
                            }
                        });
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        cidadao.setDirFotoUsuario("link de foto genérica");
                        Service service = CallService.createService(Service.class);
                        Call<Void> callCadastro = service.postCidadao(UsuarioApplication.getInstance().getToken(),
                                cidadao, null);
                        callCadastro.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    if (response.code() == 201) {
                                        dialog.dismiss();
                                        FragmentManager fm = getFragmentManager();
                                        fm.popBackStack();
                                    }
                                } else {
                                    APIError error = ErrorUtils.parseError(response);
                                    Log.e("Erro ao Cadastrar", error.getMessage());
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("ErrorCadastro", t.getLocalizedMessage());
                                Toasty.error(getContext(), "Erro na conexão").show();
                            }
                        });
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                        cidadao.setDirFotoUsuario("link de foto genérica");
                        Service service = CallService.createService(Service.class);
                        Call<Void> callCadastro = service.postCidadao(UsuarioApplication.getInstance().getToken(),
                                cidadao, null);
                        callCadastro.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    if (response.code() == 201) {
                                        dialog.dismiss();
                                        FragmentManager fm = getFragmentManager();
                                        fm.popBackStack();
                                    }
                                } else {
                                    APIError error = ErrorUtils.parseError(response);
                                    Log.e("Erro ao Cadastrar", error.getMessage());
                                    Toasty.error(getContext(), "Erro", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("ErrorCadastro", t.getLocalizedMessage());
                                Toasty.error(getContext(), "Erro na conexão").show();
                            }
                        });
                    }
                }
            }
        });

        edtNome.requestFocus();
        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        FragmentManager fm = getFragmentManager();
        fm.popBackStack();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
                Uri imageUri = CropImage.getPickImageResultUri(getContext(), data);
                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .setRequestedSize(1024, 1024)
                        .setOutputCompressQuality(70)
                        .setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                        .start(getContext(), this);
            }
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                imageUri = result.getUri();
                profileImage.setImageURI(imageUri);
            }
            if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Exception error = result.getError();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
