package com.example.davidg.parkingapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.davidg.parkingapp.Model.ParkingResponse;
import com.example.davidg.parkingapp.api.ApiCallParkingInterface;
import com.example.davidg.parkingapp.api.ApiObservableParkingInterface;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap map;
    private ParkingResponse parkresponse;
    double longitude, latitude;
    Button btnRes, btnSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById( R.id.map );
        mapFragment.getMapAsync( this );


        btnRes = (Button) findViewById( R.id.btnReservation );
        btnRes.setOnClickListener( this );
        btnSearch = (Button) findViewById( R.id.btnSearch );
        btnSearch.setOnClickListener( this );


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
//
    }



//        LatLng sydney = new LatLng( 13, -52);
//
//
//        MarkerOptions markerOptions = new MarkerOptions().position( sydney ).title( "You are Here" ).draggable( true );
//        map.addMarker( markerOptions );

//
//        map.setOnMarkerClickListener(this);


    private Boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected())
            return true;


        return false;
    }



    public void setupMap(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( "http://ridecellparking.herokuapp.com/" )
                .addCallAdapterFactory( RxJava2CallAdapterFactory.create() )
                .addConverterFactory( GsonConverterFactory.create() )
                .build();

        ApiObservableParkingInterface service = retrofit.create( ApiObservableParkingInterface.class );
        service.getinfo()
                .subscribeOn( Schedulers.io() )
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe( new Consumer<List<ParkingResponse>>() {
                    @Override
                    public void accept(List<ParkingResponse> responses) throws Exception {
                        try {
                            map.clear();
                            for (int i = 0; i < responses.size(); i++) {
                                Double lat = Double.valueOf(  responses.get( i ).getLat());
                                Double lng = Double.valueOf(  responses.get( i ).getLng());


                                if (responses.get( i ).getIsReserved() == true) {

                                    String placeName = responses.get( i ).getName();
                                    String snippet = "Reserved Until " + responses.get( i ).getReservedUntil() + "\n" +
                                            "Reservation Information" + "\n" +
                                            "Minimum Time  " + " Maximum Time   " + "Cost" + "\n" +
                                            responses.get( i ).getMinReserveTimeMins() + " Minutes" + "          " +
                                            responses.get( i ).getMaxReserveTimeMins() + " Minutes" + "         " +
                                            "$";

                                    MarkerOptions markerOptions = new MarkerOptions();
                                    LatLng latLng = new LatLng( lat, lng );
                                    markerOptions.position( latLng );
                                    markerOptions.title( placeName );
                                    markerOptions.snippet( snippet );
                                    markerOptions.icon( BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_RED ) );
                                    Marker m = map.addMarker( markerOptions );
                                    map.moveCamera( CameraUpdateFactory.newLatLng( latLng ) );
                                    map.animateCamera( CameraUpdateFactory.zoomTo( 15 ) );

                                } else if (responses.get( i ).getIsReserved() == false) {

                                    String placeName = responses.get( i ).getName();
                                    String snippet = "Not Reserved , Tab to Reserve" + "\n" +
                                            "Minimum Time  " + " Maximum Time   " + "Cost" + "\n" +
                                            responses.get( i ).getMinReserveTimeMins() + " Minutes" + "          " +
                                            responses.get( i ).getMaxReserveTimeMins() + " Minutes" + "         " +
                                            "$";

                                    MarkerOptions markerOptions = new MarkerOptions();
                                    LatLng latLng = new LatLng( lat, lng );
                                    markerOptions.position( latLng );
                                    markerOptions.title( placeName );
                                    markerOptions.snippet( snippet );
                                    markerOptions.icon( BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_GREEN ) );
                                    map.addMarker( markerOptions );
                                    map.moveCamera( CameraUpdateFactory.newLatLng( latLng ) );
                                    map.animateCamera( CameraUpdateFactory.zoomTo( 15 ) );

                                }
                            }
                        } catch (Exception e) {
                            Log.d( "onResponse", "There is an error" );
                            e.printStackTrace();
                        }
                        Toast.makeText( MainActivity.this, "Available Parking Shown in Green Marker", Toast.LENGTH_SHORT ).show();
                    }
                } );
                    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnReservation:
                Intent in = new Intent( MainActivity.this, Reservation.class );
                startActivity( in );
                break;


            case R.id.btnSearch:
                setupMap();
                break;

        }
    }
}

