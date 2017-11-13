package com.example.administrador.citycaremobile.Fragments;


import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrador.citycaremobile.Exceptions.APIError;
import com.example.administrador.citycaremobile.Modelo.Cidadao;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        Geocoder geo = new Geocoder(getContext());
        if(UsuarioApplication.getInstance().getUsuario() != null){
            if(UsuarioApplication.getInstance().getUsuario() instanceof Cidadao){
                try {
                    List<Address> cidade = geo.getFromLocationName(((Cidadao) UsuarioApplication.getInstance().getUsuario()).getCidade(),1);
                    CameraUpdateFactory.newLatLng(new LatLng(cidade.get(0).getLatitude(), cidade.get(0).getLongitude()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                try {
                    List<Address> cidade = geo.getFromLocationName(((Empresa)UsuarioApplication.getInstance().getUsuario()).getCidade(), 1);
                    CameraUpdateFactory.newLatLng(new LatLng(cidade.get(0).getLatitude(), cidade.get(0).getLongitude()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                List<Address> cidade = geo.getFromLocationName("Juazeiro do Norte", 1);
                gMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(cidade.get(0).getLatitude(), cidade.get(0).getLongitude())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        gMap.setMinZoomPreference(13);
        getMapsData();
    }

    public void addDenuncia(Postagem postagem){
        addMarker(postagem);
    }

    private void addMarker(Postagem postagem) {
        Marker marker = gMap.addMarker(new MarkerOptions()
                .position(new LatLng(postagem.getDenuncia().getLatitude(), postagem.getDenuncia().getLongitude())));
        marker.setTitle(postagem.getDenuncia().getCategoriaDenuncia().getDescricaoCategoria());
        if (postagem.getDenuncia().isStatusDenuncia() == 1) {
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_care));
        } else if (postagem.getDenuncia().isStatusDenuncia() == 0) {
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_done));
        }
        gMap.setInfoWindowAdapter(new InfoWindowMapsAdapter(getActivity().getLayoutInflater(), postagem));
        hashMap.put(postagem.getDenuncia().getIdDenuncia(), marker);
    }

    public void removeMarker(Postagem postagem) {
        Marker marker = hashMap.get(postagem.getDenuncia().getIdDenuncia());
        marker.remove();
        postagens.remove(postagem);
        hashMap.remove(postagem);
    }


    private class InfoWindowMapsAdapter implements GoogleMap.InfoWindowAdapter {

        private Postagem post;
        private View view;

        private TextView tvCategoria;
        private ImageView imageViewDenuncia;

        InfoWindowMapsAdapter(LayoutInflater inflater, Postagem post) {
            view = inflater.inflate(R.layout.info_window_maps, null);
            this.post = post;
            imageViewDenuncia = (ImageView) view.findViewById(R.id.info_window_image_maps);
            tvCategoria = (TextView) view.findViewById(R.id.info_window_text_maps);
        }


        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            tvCategoria.setText(post.getDenuncia().getCategoriaDenuncia().getDescricaoCategoria());
            Glide.with(view).load(post.getDenuncia().getDirFotoDenuncia()).into(imageViewDenuncia);
            return view;
        }


    }

    private void getMapsData() {
        Service service = CallService.createService(Service.class);
        final Call<ArrayList<Postagem>> getMapsData = service.mapsPostagens(UsuarioApplication.getToken());
        getMapsData.enqueue(new Callback<ArrayList<Postagem>>() {
            @Override
            public void onResponse(Call<ArrayList<Postagem>> call, Response<ArrayList<Postagem>> response) {
                if (response.isSuccessful()) {
                    gMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style));
                    setData(response.body());
                    for(Postagem p : postagens){
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
    }
}
