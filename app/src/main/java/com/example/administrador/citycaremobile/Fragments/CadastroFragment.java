package com.example.administrador.citycaremobile.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.gson.Gson;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.ProgressDialog.show;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CadastroFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CadastroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CadastroFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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

    private OnFragmentInteractionListener mListener;

    public CadastroFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CadastroFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CadastroFragment newInstance(String param1, String param2) {
        CadastroFragment fragment = new CadastroFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
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
        edtLogin = (EditText) view.findViewById(R.id.edt_login);
        edtEmail = (EditText) view.findViewById(R.id.edt_email);
        edtSenha = (EditText) view.findViewById(R.id.edt_senha);

        btCadastrar = (Button) view.findViewById(R.id.bt_cadastrar);
        //Ação do Botão de Cadastro
        btCadastrar.setOnClickListener(new View.OnClickListener() {
            ProgressDialog dialog = new ProgressDialog(getActivity());
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(edtNome.getText())) {
                    edtNome.setError(getString(R.string.campo_incorreto));
                }
                if (!rbFeminino.isChecked() && !rbMasculino.isChecked()) {
                    txtSexo.setError(getString(R.string.campo_incorreto));
                }
                if (spinnerEstado.getSelectedItemPosition() == 0) {
                    TextView erroSpinCidade = (TextView) spinnerEstado.getSelectedView();
                    erroSpinCidade.setError(getString(R.string.campo_incorreto));
                    erroSpinCidade.setTextColor(Color.RED);
                    erroSpinCidade.setText(getString(R.string.campo_incorreto));
                }
                if (spinnerCidade.getSelectedItemPosition() == 0) {
                    TextView erroSpinCidade = (TextView) spinnerCidade.getSelectedView();
                    erroSpinCidade.setError(getString(R.string.campo_incorreto));
                    erroSpinCidade.setTextColor(Color.RED);
                    erroSpinCidade.setText(getString(R.string.campo_incorreto));
                }
                if (TextUtils.isEmpty(edtLogin.getText())) {
                    edtLogin.setError(getString(R.string.campo_incorreto));
                }
                if (TextUtils.isEmpty(edtEmail.getText())) {
                    edtEmail.setError(getString(R.string.campo_incorreto));
                }
                if (TextUtils.isEmpty(edtSenha.getText())) {
                    edtSenha.setError(getString(R.string.campo_incorreto));
                } else {
                    //Instancia de Login
                    Login login = new Login();
                    login.setLogin(edtLogin.getText().toString());
                    login.setEmail(edtEmail.getText().toString());
                    login.setSenha(edtSenha.getText().toString());
                    login.setStatus_login(false);
                    login.setAdministrador(false);

                    //Instancia de Cidadao
                    Cidadao cidadao = new Cidadao();
                    cidadao.setNome(edtNome.getText().toString());
                    if (!TextUtils.isEmpty(edtSobrenome.getText())) {
                        cidadao.setSobrenome(edtSobrenome.getText().toString());
                    }
                    if (rbMasculino.isChecked()) {
                        cidadao.setSexo("Masculino");
                    } else {
                        cidadao.setSexo("Feminino");
                    }
                    cidadao.setCidade(/*spinnerCidade.getSelectedItem().toString()*/ "Ceará");
                    cidadao.setEstado(/*spinnerEstado.getSelectedItem().toString()*/"Juazeiro do Norte");
                    cidadao.setLoginCidadao(login);

                    dialog.setMessage("Cadastrando...");
                    dialog.setIndeterminate(false);
                    dialog.show();

                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            dialog.dismiss();
                        }
                    });

                    Service service = CallService.createService(Service.class);
                    Call<Integer> call = service.postCidadao(cidadao);
                        call.enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                if (response.isSuccessful()) {
                                    Integer i = response.body();
                                    if (i != null) {
                                        if (i == 1) {
                                            dialog.dismiss();
                                            Toast.makeText(getActivity(), "Cadastro completo", Toast.LENGTH_LONG).show();
                                            Log.i("SERVICE", "SUCESSO");
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {
                                dialog.dismiss();
                                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                                Log.e("SERVICE", t.getMessage());
                            }
                        });
                }
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                fm.popBackStack();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            if(requestCode == 123){
                imagemSelecionada = data.getData();
                profileImage.setImageURI(imagemSelecionada);
            }

            if(requestCode == 124){
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                profileImage.setImageBitmap(bitmap);
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
