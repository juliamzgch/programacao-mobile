package com.example.eurodolarconvert;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ConversorActivity extends AppCompatActivity {
    RadioButton radioEuro, radioDolar;
    EditText inputValue;
    Double euro, dolar, resultado;

    ImageView imgInput, imgResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversor);

        Button btnBack = findViewById(R.id.btBack);
        btnBack.setOnClickListener(v -> {
            finish();
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        inputValue = findViewById(R.id.inputValue);

        Button btnConvert = findViewById(R.id.btnConvert);
        btnConvert.setOnClickListener(v -> converter());
        imgInput = findViewById(R.id.imgInputValue);
        radioDolar =findViewById(R.id.radioDolar);
        radioEuro =findViewById(R.id.radioEuro);
        radioDolar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgInput.setImageResource(R.drawable.usa);
            }
        });
        radioEuro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgInput.setImageResource(R.drawable.eu);
            }
        });

    }



    private void converter() {
        TextView textResult = findViewById(R.id.resultValue);
        imgResult = findViewById(R.id.imgResultValue);
        TextView unit = findViewById(R.id.unitValue);
        if (radioDolar.isChecked()) {
            try {
                euro = Double.parseDouble(inputValue.getText().toString());
                dolar = euro / 0.87;
                textResult.setText(String.valueOf(dolar));
                unit.setText(R.string.unit_dolar);
                imgResult.setImageResource(R.drawable.usa);
            } catch (Exception e) {
                Toast.makeText(this, "dados inválidos!", Toast.LENGTH_LONG).show();
            }
        } else {
            try {
                dolar = Double.parseDouble(inputValue.getText().toString());
                euro = dolar * (dolar * 0.87);
                textResult.setText(String.valueOf(dolar));
                unit.setText(R.string.unit_euro);
                imgResult.setImageResource(R.drawable.eu);
            } catch (Exception e) {
                Toast.makeText(this, "dados inválidos!", Toast.LENGTH_LONG).show();
            }
        }
    }
}