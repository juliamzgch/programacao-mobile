package com.example.basiccalculator;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    Button btnSum, btnMinus, btnMulti, btnDivi;
    EditText editText1, editText2;
    TextView result;
    Double value1, value2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnSum = (Button) findViewById(R.id.btnSum);
        btnMinus = (Button) findViewById(R.id.btnMinus);
        btnMulti = (Button) findViewById(R.id.btnMulti);
        btnDivi = (Button) findViewById(R.id.btnDivi);
        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        result = (TextView) findViewById(R.id.result);

        addition(btnSum);
        subtraction(btnMinus);
        multiplication(btnMulti);
        division(btnDivi);
    }

    private boolean updateValues() {
        try {
            value1 = Double.parseDouble(editText1.getText().toString());
            value2 = Double.parseDouble(editText2.getText().toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void addition (Button btnSum) {
        btnSum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!updateValues()) {
                    Toast.makeText(MainActivity.this, "Valores inválidos", Toast.LENGTH_SHORT).show();
                    return;
                }
                double resultado = value1 + value2;
                result.setText(String.valueOf(resultado));
            }
        });
    }

    private void subtraction (Button btnMinus) {
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!updateValues()) {
                    Toast.makeText(MainActivity.this, "Valores inválidos", Toast.LENGTH_SHORT).show();
                    return;
                }
                double resultado = value1 - value2;
                result.setText(String.valueOf(resultado));
            }
        });

    }

    private void multiplication (Button btnMulti) {
        btnMulti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!updateValues()) {
                    Toast.makeText(MainActivity.this, "Valores inválidos", Toast.LENGTH_SHORT).show();
                    return;
                }
                double resultado = value1 * value2;
                result.setText(String.valueOf(resultado));
            }
        });

    }

    private void division (Button btnDivi) {
        btnDivi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!updateValues()) {
                    Toast.makeText(MainActivity.this, "Valores inválidos", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (value2 == 0) {
                    Toast.makeText(MainActivity.this, "Não é possível dividir por zero", Toast.LENGTH_SHORT).show();
                    return;
                }
                double resultado = value1 / value2;
                result.setText(String.valueOf(resultado));
            }
        });

    }
}