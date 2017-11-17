package com.example.administrador.citycaremobile.Fragments;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrador.citycaremobile.Exceptions.APIError;
import com.example.administrador.citycaremobile.Modelo.Cidadao;
import com.example.administrador.citycaremobile.Modelo.Denuncia;
import com.example.administrador.citycaremobile.Modelo.Empresa;
import com.example.administrador.citycaremobile.Modelo.Postagem;
import com.example.administrador.citycaremobile.Modelo.UsuarioApplication;
import com.example.administrador.citycaremobile.Services.CallService;
import com.example.administrador.citycaremobile.Services.Service;
import com.example.administrador.citycaremobile.Utils.ErrorUtils;
import com.example.administrador.citycaremobile.Utils.SystemUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.example.administrador.citycaremobile.R;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private SupportMapFragment mapFragment;
    private static ArrayList<Postagem> postagens;

    public static MapsFragment getInstance() {
        return instance;
    }

    private static MapsFragment instance = null;
    private static GoogleMap gMap;
    private HashMap<Integer, Marker> hashMap;
    private Map<Marker, Postagem> postagemMap = new HashMap<>();
    private InfoWindowMapsAdapter infoWindowMapsAdapter;

    public MapsFragment() {
        if(instance == null){
            instance = this;
            hashMap = new HashMap<>();
        } else {
            getInstance();
        }
    }

    public void setData(ArrayList<Postagem> postagens) {
        this.postagens = postagens;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        infoWindowMapsAdapter = new InfoWindowMapsAdapter(getActivity().getLayoutInflater(),postagemMap);
        Geocoder geo = new Geocoder(getContext());
        if(UsuarioApplication.getInstance().getUsuario() != null){
            if(UsuarioApplication.getInstance().getUsuario() instanceof Cidadao){
                try {
                    Address cidade = geo.getFromLocationName(((Cidadao) UsuarioApplication.getInstance().getUsuario()).getCidade(),1).get(0);
                    gMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(cidade.getLatitude(), cidade.getLongitude())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Address cidade = geo.getFromLocationName(((Empresa) UsuarioApplication.getInstance().getUsuario()).getCidade(),1).get(0);
                    gMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(cidade.getLatitude(), cidade.getLongitude())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                Address cidade = geo.getFromLocationName("Juazeiro do Norte", 1).get(0);
                gMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(cidade.getLatitude(), cidade.getLongitude())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        gMap.setInfoWindowAdapter(infoWindowMapsAdapter);
        gMap.setMinZoomPreference(14);
        getMapsData();
    }

    public void addDenuncia(Postagem postagem){
        addMarker(postagem);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void atualizarDenuncia(Denuncia den){
        Marker marker = hashMap.get(den.getIdDenuncia());
        infoWindowMapsAdapter.atualizarPostagem(marker, den);
    }

    private void addMarker(Postagem postagem) {
        Marker marker = gMap.addMarker(new MarkerOptions()
                .position(new LatLng(postagem.getDenuncia().getLatitude(), postagem.getDenuncia().getLongitude())));
        marker.setTitle(postagem.getDenuncia().getCategoriaDenuncia().getDescricaoCategoria());
        if (postagem.getDenuncia().getStatusDenuncia() == 1) {
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_care));
        } else if (postagem.getDenuncia().getStatusDenuncia() == 0) {
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_done));
        }
        hashMap.put(postagem.getDenuncia().getIdDenuncia(), marker);
        infoWindowMapsAdapter.addPostagem(marker,postagem);
    }

    public void removeMarker(Postagem postagem) {
        Marker marker = hashMap.get(postagem.getDenuncia().getIdDenuncia());
        marker.remove();
        postagens.remove(postagem);
        hashMap.remove(postagem);
        infoWindowMapsAdapter.removePostagem(marker);
    }


    private static class InfoWindowMapsAdapter implements GoogleMap.InfoWindowAdapter {
        private View view;

        private TextView tvCategoria;
        private TextView tvAgilizas;
        private TextView tvComentarios;
        private static Map<Marker,Postagem> mapPostagem;

        InfoWindowMapsAdapter(LayoutInflater inflater, Map<Marker, Postagem> postagemMap) {
            view = inflater.inflate(R.layout.info_window_maps, null);
            tvAgilizas = (TextView) view.findViewById(R.id.tv_agilizas);
            tvComentarios = (TextView) view.findViewById(R.id.tv_comentarios);
            tvCategoria = (TextView) view.findViewById(R.id.info_window_text_maps);
            mapPostagem = postagemMap;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            Postagem post = mapPostagem.get(marker);
            tvCategoria.setText(post.getDenuncia().getCategoriaDenuncia().getDescricaoCategoria());
            tvAgilizas.setText(String.valueOf(post.getAgilizas().size()));
            tvComentarios.setText(String.valueOf(post.getComentarios().size()));
            return view;
        }

        public void addPostagem(Marker marker, Postagem post){
            mapPostagem.put(marker,post);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void atualizarPostagem(Marker marker, Denuncia denuncia){
            Postagem post = mapPostagem.get(marker);
            post.setDenuncia(denuncia);
            mapPostagem.replace(marker, post);
        }

        public void removePostagem(Marker marker){
            mapPostagem.remove(marker);
        }
    }
        private void getMapsData() {
            Service service = CallService.createService(Service.class);
            Call<ArrayList<Postagem>> getMapsData = service.mapsPostagens(UsuarioApplication.getToken());
            getMapsData.enqueue(new Callback<ArrayList<Postagem>>() {
                @Override
                public void onResponse(Call<ArrayList<Postagem>> call, Response<ArrayList<Postagem>> response) {
                    if (response.isSuccessful()) {
                        gMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style));
                        setData(response.body());
                        for (Postagem p : postagens) {
                            addMarker(p);
                        }
                        return;
                    } else if (response.code() == 401) {
                        new SystemUtils().authenticateToken(getContext());
                        getMapsData();
                    } else {
                        APIError error = ErrorUtils.parseError(response);
                        Log.e("getToken", error.getMessage());
                        Toasty.error(getContext(), "Erro na conexão", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Postagem>> call, Throwable t) {
                    Log.e("getToken", t.getMessage());
                    Toasty.error(getContext(), "Erro na conexão", Toast.LENGTH_LONG).show();
                }
            });
        }

    @Override
    public void onResume() {
        super.onResume();
        mapFragment.getMapAsync(this);
    }
}
