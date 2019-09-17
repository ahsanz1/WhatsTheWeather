package com.example.ahsanz.whatstheweather;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView enterCity, result;
    TextView cityName, description;
    Button weatherButton, againButton;
    String getcity;
    RelativeLayout currentLayout;
    MediaPlayer mediaPlayer;
    Toast toast;

    public void checkAnother(View view)
    {
        enterCity.animate().alpha(1f).setDuration(1000);
        cityName.setText(" ");
        cityName.animate().alpha(1f).setDuration(1000);
        weatherButton.animate().alpha(1f).setDuration(1000);
        currentLayout.setBackgroundResource(R.drawable.backgroundd);
        mediaPlayer.release();

        result.animate().alpha(0f).setDuration(1000);
        againButton.setVisibility(View.INVISIBLE);
    }

    public void getWeather(View view) {
        DownloadWeatherData downloadWeatherData = new DownloadWeatherData();
        if (cityName.getText().toString().equals(" "))
            Toast.makeText(getApplicationContext(), "Invalid City Name!", Toast.LENGTH_LONG).show();
        else
            downloadWeatherData.execute("http://api.openweathermap.org/data/2.5/weather?q=" + cityName.getText().toString() + "&appid=1ee8c97699d040c6a2da8b04aa9a7b38");
    }

    public class DownloadWeatherData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            URL url;
            HttpURLConnection httpURLConnection;
            InputStream inputStream;
            InputStreamReader inputStreamReader;
            String result = "";

            try {
                url = new URL(params[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                inputStream = httpURLConnection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = inputStreamReader.read();
                }
                return result;
            } catch (IOException e) {

                //Toast.makeText(MainActivity.this, "City NOT Found!", Toast.LENGTH_SHORT).show();

                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {

                JSONObject jsonMainObject = new JSONObject(s);
                String weatherInfo = jsonMainObject.getString("weather");
                JSONArray jsonArray = new JSONArray(weatherInfo);

                String getWeatherInfoMain = jsonArray.getJSONObject(0).getString("main");
                String getWeatherInfoDescription = jsonArray.getJSONObject(0).getString("description");
                String getTemperature = jsonMainObject.getJSONObject("main").getString("temp");
                String getHumidity = jsonMainObject.getJSONObject("main").getString("humidity");
                String getCountry = jsonMainObject.getJSONObject("sys").getString("country");
                String getMaxTemp = jsonMainObject.getJSONObject("main").getString("temp_max");
                String getMinTemp = jsonMainObject.getJSONObject("main").getString("temp_min");

                double inKelvin = Double.valueOf(getTemperature);
                double inDegrees = (inKelvin - 273.15f);
                DecimalFormat tempFormat = new DecimalFormat("#.00");
                getTemperature = String.valueOf(tempFormat.format(inDegrees));

                inKelvin = Double.valueOf(getMinTemp);
                inDegrees = (inKelvin - 273.15f);
                DecimalFormat tempFormatMin = new DecimalFormat("#.00");
                getMinTemp = String.valueOf(tempFormatMin.format(inDegrees));

                inKelvin = Double.valueOf(getMaxTemp);
                inDegrees = (inKelvin - 273.15f);
                DecimalFormat tempFormatMax = new DecimalFormat("#.00");
                getMaxTemp = String.valueOf(tempFormatMax.format(inDegrees));

                result.setText(getWeatherInfoMain + "\n(" + getWeatherInfoDescription + ")" + "\n" + "Current Temperature: " +
                        getTemperature + "°C" + "\n" + "Min: " + getMinTemp + "°C" + "\n" + "Max: " +
                        getMaxTemp + "°C" + "\n" + "Humidity: " + getHumidity +
                        "%" + "\n" + "Country: " + getCountry);

                enterCity.animate().alpha(0f).setDuration(1500);
                cityName.animate().alpha(0f).setDuration(1500);
                weatherButton.animate().alpha(0f).setDuration(1500);

                if (getWeatherInfoMain.equals("Clear")) {
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sunnyday);
                    currentLayout.setBackgroundResource(R.drawable.clear);
                    mediaPlayer.start();
                }
                else if (getWeatherInfoMain.equals("Haze")) {
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.smokenhaze);
                    currentLayout.setBackgroundResource(R.drawable.haze);
                    mediaPlayer.start();
                }
                else if (getWeatherInfoMain.equals("Rain")) {
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.rainthunder);
                    currentLayout.setBackgroundResource(R.drawable.raindrops);
                    mediaPlayer.start();
                }
                else if (getWeatherInfoMain.equals("Smoke")) {
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.smokenhaze);
                    currentLayout.setBackgroundResource(R.drawable.smoky);
                    mediaPlayer.start();
                }
                else if (getWeatherInfoMain.equals("Mist")) {
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.smokenhaze);
                    currentLayout.setBackgroundResource(R.drawable.mistt);
                    mediaPlayer.start();
                }
                else if (getWeatherInfoMain.equals("Clouds")) {
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.cloudandraindistan);
                    currentLayout.setBackgroundResource(R.drawable.clouds);
                    mediaPlayer.start();
                }
                else if (getWeatherInfoMain.equals("Thunderstorm")) {
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.rainthunder);
                    currentLayout.setBackgroundResource(R.drawable.thunder);
                    mediaPlayer.start();
                }
                else if (getWeatherInfoMain.equals("Fog")) {
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.fogsound);
                    currentLayout.setBackgroundResource(R.drawable.fog);
                    mediaPlayer.start();
                }

                result.animate().alphaBy(1f).setDuration(1500);
                againButton.setVisibility(View.VISIBLE);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enterCity = (TextView) findViewById(R.id.entercity);
        cityName = (TextView) findViewById(R.id.cityname);
        cityName.setText(" ");
        weatherButton = (Button) findViewById(R.id.weatherButton);
        result = (TextView) findViewById(R.id.results);
        currentLayout = (RelativeLayout) findViewById(R.id.activity_main);
        againButton = (Button) findViewById(R.id.again);
    }
}

/**Log.i("info", getWeatherInfoMain);
Log.i("description", getWeatherInfoDescription);
        Log.i("temperature", getTemperature);
        Log.i("humidity", getHumidity);
        Log.i("country", getCountry);
        Log.i("Data: ", s);
        Log.i("min", getMinTemp);
        Log.i("max", getMaxTemp);
 Log.i("min", getMinTemp);
 Log.i("max", getMaxTemp);*/