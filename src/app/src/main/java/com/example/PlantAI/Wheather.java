package com.example.PlantAI;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.PlantAI.databinding.ActivityWheatherBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Wheather extends AppCompatActivity {

    private EditText locationEditText;
    private Button fetchWeatherButton;
    private TextView recommendationTextView;
    private TextView TemperatureTextView;

    
    private ActivityWheatherBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWheatherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        locationEditText = findViewById(R.id.cityEditText);
        fetchWeatherButton = findViewById(R.id.fetchWeatherButton);
        recommendationTextView = findViewById(R.id.recommendationTextView);
        TemperatureTextView = findViewById(R.id.temperatureTextView);

        fetchWeatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = locationEditText.getText().toString();
                fetchWeatherData(location);
            }
        });

    }

    private void fetchWeatherData(String location) {
        String apiKey = "bdafaf49595a279faced88efb82f7a07";
        String weatherApiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + location + "&appid=" + apiKey;;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(weatherApiUrl)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                recommendationTextView.setText("Failed to set weather Data.");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()) {
                    String jsonData = response.body().string();
                    try {
                        JSONObject weatherData = new JSONObject(jsonData);
                        double temperatureKelvin = weatherData.getJSONObject("main").getDouble("temp");
                        double temperatureCelsius = temperatureKelvin - 273.15;
                        String formattedTemperature = String.format("%.2f", temperatureCelsius);

                        // Display temperature in the temperatureTextView
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TemperatureTextView.setText("Temperature: " + formattedTemperature + " °C");
                            }
                        });

                        // Extracting humidity
                        int humidity = weatherData.getJSONObject("main").getInt("humidity");
                        runOnUiThread(() -> {
                            // Display humidity in the humidityTextView
                            TemperatureTextView.append("\nHumidity: " + humidity + "%");
                        });

                        // Extracting rain (if available)
                        if (weatherData.has("rain") && weatherData.getJSONObject("rain").has("1h")) {
                            double rainVolume = weatherData.getJSONObject("rain").getDouble("1h");
                            runOnUiThread(() -> {
                                // Display rain volume in the rainTextView
                                TemperatureTextView.append("\nRain (1h): " + rainVolume + " mm");
                            });
                        }

                        // Extracting precipitation (if available)
                        if (weatherData.has("precipitation")) {
                            JSONObject precipitationObject = weatherData.getJSONObject("precipitation");
                            if (precipitationObject.has("1h")) {
                                double precipitationVolume = precipitationObject.getDouble("1h");
                                runOnUiThread(() -> TemperatureTextView.append("\nPrecipitation (1h): " + precipitationVolume + " mm"));
                            }
                        }

                        // Extracting wind
                        if (weatherData.has("wind")) {
                            double windSpeed = weatherData.getJSONObject("wind").getDouble("speed");
                            runOnUiThread(() -> {
                                // Display wind speed in the windTextView
                                TemperatureTextView.append("\nWind Speed: " + windSpeed + " m/s");
                            });
                        }

                        String recommendation = getCropRecommendation(temperatureCelsius);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recommendationTextView.setText("Recommendation Crop: " + recommendation);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    // Handle error response
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recommendationTextView.setText("Error: " + response.code() + " - " + response.message());
                            TemperatureTextView.setText("");
                        }
                    });
                }
            }
        });
    }

    private String getCropRecommendation(double temperature) {
        if (temperature < 10) {
            return "Wheat";
        } else if (temperature >= 10 && temperature < 25) {
            return "Corn";
        } else {
            return "Rice";
        }
    }
}