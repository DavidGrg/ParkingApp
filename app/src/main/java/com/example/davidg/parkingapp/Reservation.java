package com.example.davidg.parkingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.davidg.parkingapp.api.ApiObservableParkingInterface;

/**
 * Created by DavidG on 04/03/2018.
 */

public class Reservation extends AppCompatActivity {

    private ApiObservableParkingInterface reqInterface;
    private RecyclerView recyclerView;
    View view;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_reservation );


    }

//    public void doNetworkCall() {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl( "http://ridecellparking.herokuapp.com/" )
//                .addCallAdapterFactory( RxJava2CallAdapterFactory.create() )
//                .addConverterFactory( GsonConverterFactory.create() )
//                .build();
//
//        reqInterface = retrofit.create( ApiObservableParkingInterface.class );
//        reqInterface.getinfo()
//                .subscribeOn( Schedulers.io() )
//                .observeOn( AndroidSchedulers.mainThread() )
//                .subscribe( new Consumer<ParkingResponse>() {
//                    @Override
//                    public void accept(ParkingResponse response) throws Exception {
//
//                        recyclerView.setAdapter( new ReservationAdapter( response.getLocation() ) );
////                        Toast.makeText( getActivity(), "Classic", Toast.LENGTH_SHORT ).show();
//
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
////                        Toast.makeText( getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT ).show();
//                    }
//                } );
//
//
//    }
}
