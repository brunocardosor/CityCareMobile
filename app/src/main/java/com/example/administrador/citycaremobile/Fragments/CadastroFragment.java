package com.example.administrador.citycaremobile.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrador.citycaremobile.Modelo.Cidadao;
import com.example.administrador.citycaremobile.Modelo.Login;
import com.example.administrador.citycaremobile.R;
import com.example.administrador.citycaremobile.Services.CallService;
import com.example.administrador.citycaremobile.Services.Service;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.ProgressDialog.show;

public class CadastroFragment extends DialogFragment {

    //Atributos da View
    private Toolbar toolbar;
    private CircleImageView profileImage;
    private TextView txtSexo;
    private FloatingActionButton fabGetPicture, fabCamera, fabGalery;
    private EditText edtNome, edtSobrenome, edtEmail, edtLogin, edtSenha;
    private Spinner spinnerCidade, spinnerEstado;
    private RadioButton rbMasculino, rbFeminino;
    private Button btCadastrar;

    private boolean open;
    private Uri imagemSelecionada;


    public CadastroFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cadastro,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Relacionando Atributos de View com a View
        toolbar = (Toolbar) view.findViewById(R.id.toolbar_transparente);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        profileImage = (CircleImageView) view.findViewById(R.id.pic_profile);
        fabGetPicture = (FloatingActionButton) view.findViewById(R.id.fab_get_picture);
        //Ação do Botão para adicionar foto
        fabGetPicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(open){
                    fabCamera.setVisibility(View.GONE);
                    fabCamera.setClickable(false);

                    fabGalery.setVisibility(View.GONE);
                    fabGalery.setClickable(false);

                    open = false;

                } else {
                    fabCamera.setVisibility(View.VISIBLE);
                    fabCamera.setClickable(true);

                    fabGalery.setVisibility(View.VISIBLE);
                    fabGalery.setClickable(true);

                    open = true;
                }
            }
        });

        fabCamera = (FloatingActionButton) view.findViewById(R.id.fab_camera);
        //Ação do Botão para abrir a camera
        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(Intent.createChooser(i,"Selecionar Foto"), 124);
            }
        });

        fabGalery = (FloatingActionButton) view.findViewById(R.id.fab_galery);
        //Ação do Botão para abri a galeria
        fabGalery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(i, "Selecionar Foto"), 123);
            }
        });
        edtNome = (EditText) view.findViewById(R.id.edt_nome);
        edtSobrenome = (EditText) view.findViewById(R.id.edt_sobrenome);
        txtSexo = (TextView) view.findViewById(R.id.radial_group);
        rbMasculino = (RadioButton) view.findViewById(R.id.radio_masculino);
        rbFeminino = (RadioButton) view.findViewById(R.id.radio_feminino);
        spinnerEstado = (Spinner) view.findViewById(R.id.spin_estado);
        spinnerCidade = (Spinner) view.findViewById(R.id.spin_cidade);
        spinnerCidade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinnerEstado.getSelectedItemPosition() != 0){

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
                }
                if (TextUtils.isEmpty(edtSenha.getText())) {
                    edtSenha.setError(getString(R.string.campo_incorreto));
                    dialog.dismiss();
                } else {
                    //Instancia de Login
                    Login login = new Login();
                    login.setLogin(edtLogin.getText().toString());
                    login.setEmail(edtEmail.getText().toString());
                    login.setSenha(edtSenha.getText().toString());
                    login.setStatus_login(false);
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
                    cidadao.setCidade(/*spinnerCidade.getSelectedItem().toString()*/"Juazeiro do Norte");
                    cidadao.setEstado(/*spinnerEstado.getSelectedItem().toString()*/"Ceará");
                    cidadao.setDir_foto_usuario("www.google.com");
                    cidadao.setLoginCidadao(login);

                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            dialog.dismiss();
                        }
                    });

                    Service service = CallService.createService(Service.class);
                    Call<Object> call = service.postCidadao(cidadao);
                    call.enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            try{
                                if(response.body() instanceof Boolean){
                                    Boolean retorno = (Boolean) response.body();
                                    Toast.makeText(getActivity(), String.valueOf(retorno), Toast.LENGTH_SHORT).show();}
                                dialog.dismiss();
                            } catch (Exception ex){
                                Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {
                            Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
        edtNome.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FragmentManager fm = getChildFragmentManager();
        LoginFragment lf = new LoginFragment();
        lf.show(fm, "LoginDialog");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 123) {
                imagemSelecionada = data.getData();
                CropImage.activity(imagemSelecionada).setAspectRatio(1,1).start(getActivity());
            }

            if (requestCode == 124) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
            }
            if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                imagemSelecionada = result.getUri();
                profileImage.setImageURI(imagemSelecionada);
            }
        }
    }
}
