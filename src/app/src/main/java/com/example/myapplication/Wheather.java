package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.myapplication.databinding.ActivityWheatherBinding;

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

    private AppBarConfiguration appBarConfiguration;
    private ActivityWheatherBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWheatherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        locationEditText = findViewById(R.id.cityEditText);
        fetchWeatherButton = findViewById(R.id.fetchWeatherButton);
        recommendationTextView = findViewById(R.id.recommendationTextView);

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
                        double temperature = weatherData.getJSONObject("main").getDouble("temp");

                        String recommendation = getCropRecommendation(temperature);
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