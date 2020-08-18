package com.example.robby.test1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsTest1 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;


    private ArrayList<Address> homeList = new ArrayList<Address>();
    //private ArrayList<ArrayList<Address>> addressMatrix = new ArrayList<ArrayList<Address>>();
    private ArrayList<ArrayList<Double>> timeMatrix = new ArrayList<ArrayList<Double>>();
    private ArrayList<Double> SSEList = new ArrayList<Double>();
    private ArrayList<Double> meanList = new ArrayList<Double>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_test1);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, );
            return;
        }
        mMap.setMyLocationEnabled(true);

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
       // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        //PolygonOptions pO1 = new PolygonOptions();



        //mMap.addPolygon(pO1)

    }


    //adding location to homeList
    public void onClick(View v){
        if (v.getId() ==  R.id.add_loc){
            EditText address = (EditText)findViewById(R.id.address);
            String location = address.getText().toString();
            List<Address> addressList = null;
            System.out.println("Registered 'click' ");

            if (!location.equals("")){
                Geocoder geocoder =  new Geocoder(this);
                try {
                    System.out.println("starting try block...");
                    System.out.println("trying to get location...");
                    //just get first result
                    addressList = geocoder.getFromLocationName(location,1);
                    if (addressList.size() > 0) {
                        System.out.println("location recieved");
                        Address myAddress = addressList.get(0);
                        if (!homeList.contains(myAddress)) {
                            //actually add to homeList
                            homeList.add(myAddress);
                            LatLng latLng = new LatLng(myAddress.getLatitude(), myAddress.getLongitude());
                            //place marker on map
                            mMap.addMarker(new MarkerOptions().position(latLng).title(myAddress.getAddressLine(0)));

                            System.out.println("Successfully added marker");
                        }
                    }
                    else{
                        System.out.println("not a valid address");
                    }
                } catch (IOException e) {
                    System.out.println("error");
                    e.printStackTrace();
                }


            }


        }
    }



    public void findMP(View v){
        if (v.getId() ==  R.id.find_midpoint)   {
            /*
            this.fillMatrix();
            this.buildSSEList();
            Address midpointAddress = this.findLSE();
            System.out.println("MidPoint is: " + midpointAddress.getAddressLine(0));
            //add marker
            LatLng latLng = new LatLng(midpointAddress.getLatitude(), midpointAddress.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("MidPoint"));
            **/
            System.out.println("Find MidPoint has been clicked");
            for (int i = 0; i < homeList.size(); i++){
                System.out.println(homeList.get(i).getAddressLine(0));
            }
        }
    }

    private void buildSSEList(){
        for (int i = 0; i < timeMatrix.size(); i++){
            SSEList.add(i,findSSE(timeMatrix.get(i)));
        }
    }

    private void buildMeanList(){
        for (int i = 0; i < timeMatrix.size(); i++){
            meanList.add(i,findMean(timeMatrix.get(i)));
        }
    }

    private void fillMatrix(){

    }

    private Address findLSE(){
        int index = findMinIndex(SSEList);
        Address midpointAddress = homeList.get(index);
        return midpointAddress;
    }

    private int findMinIndex(List<Double> list){
        int minIndex = 0;
        double minValue = list.get(0);
        for (int i = 0; i < list.size(); i++){
            if (list.get(i) < minValue){
                minValue = list.get(i);
                minIndex = i;
            }
        }
        return minIndex;

    }

    private double findSSE(List<Double> list){
        double SSE = 0;
        double mean = findMean(list);
        for (int i = 0; i < list.size(); i++){
            SSE = SSE + Math.pow((mean - list.get(i)),2);
        }
        return SSE;
    }

    private double findMean(List<Double> list){
        double mean;
        double sum = 0;
        for (int i = 0; i < list.size(); i++){
            sum = sum + list.get(i);
        }
        mean = sum / list.size();
        return mean;
    }




}
