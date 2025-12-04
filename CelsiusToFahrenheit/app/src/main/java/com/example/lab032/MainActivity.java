package com.example.lab032;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button btCalcular;
    EditText inputTemperatura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        btCalcular = findViewById(R.id.btCalcular);
        btCalcular.setOnClickListener(v -> onConvertClick());
        inputTemperatura = findViewById(R.id.inputTemperatura);


    }

    private void onConvertClick() {
        RadioButton rCelsius = findViewById(R.id.radioCelsius);
        String tempString = inputTemperatura.getText().toString();
        if (tempString.trim().isEmpty()) {
            Toast.makeText(MainActivity.this,
                    "Informe um valor!",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        double resultado;
        double temperatura = Double.parseDouble(tempString);
        if (rCelsius.isChecked()) {
            resultado = (temperatura - 32) / 1.8;
        } else {
            resultado = (temperatura * 1.8) + 32;
        }

        inputTemperatura.setText((String.valueOf(resultado)));
    }

}


