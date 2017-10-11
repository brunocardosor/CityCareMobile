package com.example.administrador.citycaremobile.Fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

    private PatternUtils patternUtils = new PatternUtils();


    public CadastroFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastro, container, false);

        //Relacionando Atributos de View com a View
        toolbar = (Toolbar) view.findViewById(R.id.toolbar_transparente);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

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

        final DadosUtils dadosUtils = new DadosUtils(getContext());
        List<String> estados = dadosUtils.listarEstados();
        ArrayAdapter<String> estadoAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, estados);
        spinnerEstado.setAdapter(estadoAdapter);
        spinnerEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    List<String> cidades = dadosUtils.listarCidades(position);
                    ArrayAdapter<String> cidadeAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, cidades);
                    spinnerCidade.setAdapter(cidadeAdapter);
                    spinnerCidade.setClickable(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        edtLogin = (EditText) view.findViewById(R.id.edt_cadastro_login);
        edtEmail = (EditText) view.findViewById(R.id.edt_cadastro_email);
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
                } if (!new SystemUtils().verificaConexao(getContext())){
                    Toasty.error(getContext(),"Sem conexão com a internet").show();
                } else {
                    //Instancia de Login
                    Login login = new Login();
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

                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            dialog.dismiss();
                        }
                    });

                    try {
                        String path = "file:" + imageUri.getPath();
                        file = new File(new URI(path));
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                    final RequestBody requestFile = RequestBody.create(
                            MediaType.parse("multipart/form-data"),
                            file
                    );

                    String json = new Gson().toJson(cidadao);
                    RequestBody requestJson = RequestBody.create(MediaType.parse("multipart/form-data"),json);

                    MultipartBody.Part fotoBody = MultipartBody.Part.createFormData("foto", file.getName(), requestFile);


                    MultipartBody.Part jsonBody = MultipartBody.Part.createFormData(
                            "cidadao","JsonCidadao",
                            requestJson);

                    Service service = CallService.createService(Service.class);
                    Call<Void> cadastrarCidadao = service.postCidadao(UsuarioApplication.getInstance().getToken().getToken(),
                            fotoBody,
                            jsonBody);
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
                        }
                    });
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
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
            Uri imageUri = CropImage.getPickImageResultUri(getContext(), data);
            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .setRequestedSize(1024, 1024)
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
