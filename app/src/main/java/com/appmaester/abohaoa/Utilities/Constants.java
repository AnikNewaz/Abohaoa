package com.appmaester.abohaoa.Utilities;

import android.location.LocationManager;

/**
 * Created by Nik on 3/30/2018.
 */

public class Constants {
    // URL to use OpenWeather API data
    public static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather";
    // App ID to use OpenWeather data
    public static final String APP_ID = "my app id which I got from OpenWeatherMap site";
    // Time between location updates (5000 milliseconds or 5 seconds)
    public static final long MIN_TIME = 5000;
    // Distance between location updates (1000m or 1km)
    public static final float MIN_DISTANCE = 1000;
    // Request Code
    public static final int REQUEST_CODE = 123;
    // Location Provider
    public static final String LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;
}
