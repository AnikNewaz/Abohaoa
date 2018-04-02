package com.appmaester.abohaoa.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appmaester.abohaoa.Models.AbohaoaDataModel;
import com.appmaester.abohaoa.R;
import com.appmaester.abohaoa.Utilities.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class AbohaoaController extends AppCompatActivity {

    // Member Variables:
    TextView mCityLabel;
    ImageView mWeatherImage;
    TextView mTemperatureLabel;

    // LocationManager and a LocationListener
    LocationManager locationManager;
    LocationListener locationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_controller_layout);

        // Linking the elements in the layout to Java code
        mCityLabel = (TextView) findViewById(R.id.locationTV);
        mWeatherImage = (ImageView) findViewById(R.id.weatherSymbolIV);
        mTemperatureLabel = (TextView) findViewById(R.id.tempTV);
        ImageButton changeCityButton = (ImageButton) findViewById(R.id.changeCityButton);

        // Sends to Change City Activity
        changeCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(AbohaoaController.this, ChangeCityController.class);
                startActivity(myIntent);
            }
        });

    }


    // onResume()
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Abohaoa", "onResume() called.");
        Intent myIntent = getIntent();
        String city = myIntent.getStringExtra("City");

        if(city != null){
            getWeatherForNewCity(city);
        } else {
            Log.d("Abohaoa", "Getting Weather for Current Location!");
            getWeatherForCurrentLocation();
        }
    }


    // Get Weather Data for Given City Name
    private void getWeatherForNewCity(String city){
        RequestParams params = new RequestParams();
        params.put("q", city);
        params.put("appid", Constants.APP_ID);
        letsDoSomeNetworking(params);

    }


    // Get Weather Data for Current Location
    private void getWeatherForCurrentLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("Abohaoa", "onLocationChanged() callback received!");

                String latitude = String.valueOf(location.getLatitude());
                String longitude = String.valueOf(location.getLongitude());

                Log.d("Abohaoa", "Latitude is: "+latitude);
                Log.d("Abohaoa", "Longitude is: "+longitude);

                RequestParams params = new RequestParams();
                params.put("lat", latitude);
                params.put("lon", longitude);
                params.put("appid", Constants.APP_ID);
                letsDoSomeNetworking(params);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Log.d("Abohaoa", "onProviderDisabled() callback received!");
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, Constants.REQUEST_CODE);
            return;
        }
        locationManager.requestLocationUpdates(Constants.LOCATION_PROVIDER, Constants.MIN_TIME, Constants.MIN_DISTANCE, locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Constants.REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("Abohaoa", "onRequestPermissionResult(): Permission Granted!");
                getWeatherForCurrentLocation();
            } else {
                Log.d("Abohaoa", "onRequestPermissionResult(): Permission Denied! :( ");
            }
        }
    }

    // Get the JSON data using the Params
    private void letsDoSomeNetworking(RequestParams params){
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(Constants.WEATHER_URL, params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                Log.d("Abohaoa", "Success! JSON: "+ response.toString());

                AbohaoaDataModel abohaoaData = AbohaoaDataModel.fromJson(response);
                updateUI(abohaoaData);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response){
                Log.d("Abohaoa", "Failed. "+ e.toString());
                Log.d("Abohaoa", "Status Code: "+ statusCode);
                Toast.makeText(AbohaoaController.this, "Request Failed!", Toast.LENGTH_SHORT).show();
            }

        });
    }



    // UI updates with the Data from the model
    private void updateUI(AbohaoaDataModel abohaoa){
        mCityLabel.setText(abohaoa.getmCity());
        mTemperatureLabel.setText(abohaoa.getmTemperature());

        int imgResourceID = getResources().getIdentifier(abohaoa.getmIconName(), "drawable", getPackageName());
        mWeatherImage.setImageResource(imgResourceID);
    }

    // onPause()


    @Override
    protected void onPause() {
        super.onPause();

        if (locationManager != null) locationManager.removeUpdates(locationListener);
    }
}
