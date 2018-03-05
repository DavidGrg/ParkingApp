package com.example.davidg.parkingapp.api;

import com.example.davidg.parkingapp.Model.ParkingResponse;


import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by DavidG on 04/03/2018.
 */

public interface ApiObservableParkingInterface {

    @GET ("api/v1/parkinglocations/search?lat=51.508530&lng=-0.076132")
    Observable<List<ParkingResponse>> getinfo();

}
