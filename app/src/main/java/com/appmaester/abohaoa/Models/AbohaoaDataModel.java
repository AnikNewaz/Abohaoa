package com.appmaester.abohaoa.Models;

import org.json.JSONException;
import org.json.JSONObject;

public class AbohaoaDataModel {

    private String mTemperature;
    private String mCity;
    private String mIconName;
    private int mCondition;


    // An AbohaoaDataModel from a JSON
    public static AbohaoaDataModel fromJson(JSONObject jsonObject){

        AbohaoaDataModel abohaoaData = new AbohaoaDataModel();

        try {
            abohaoaData.mCity = jsonObject.getString("name");
            abohaoaData.mCondition = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            abohaoaData.mIconName = updateWeatherIcon(abohaoaData.mCondition);

            double tempResult = jsonObject.getJSONObject("main").getDouble("temp") - 273.16;
            int roundedTempResult = (int) Math.rint(tempResult);

            abohaoaData.mTemperature = Integer.toString(roundedTempResult);

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return abohaoaData;
    }


    // Get the weather image name from the condition
    private static String updateWeatherIcon(int condition) {

        if (condition >= 0 && condition < 300) {
            return "tstorm1";
        } else if (condition >= 300 && condition < 500) {
            return "light_rain";
        } else if (condition >= 500 && condition < 600) {
            return "shower3";
        } else if (condition >= 600 && condition <= 700) {
            return "snow4";
        } else if (condition >= 701 && condition <= 771) {
            return "fog";
        } else if (condition >= 772 && condition < 800) {
            return "tstorm3";
        } else if (condition == 800) {
            return "sunny";
        } else if (condition >= 801 && condition <= 804) {
            return "cloudy2";
        } else if (condition >= 900 && condition <= 902) {
            return "tstorm3";
        } else if (condition == 903) {
            return "snow5";
        } else if (condition == 904) {
            return "sunny";
        } else if (condition >= 905 && condition <= 1000) {
            return "tstorm3";
        }

        return "dunno";
    }

    // Getter methods for temperature, city, and icon name:
    public String getmTemperature() {
        return mTemperature + "°";
    }

    public String getmCity() {
        return mCity;
    }

    public String getmIconName() {
        return mIconName;
    }
}
