package com.example.bmi;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    EditText inputWeight;
    EditText inputHeight;


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

        inputWeight = findViewById(R.id.input_weight);
        inputHeight = findViewById(R.id.input_height);

        Button btCalculate = findViewById(R.id.button_calculate);
        btCalculate.setOnClickListener(v -> calculate());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.result, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_help) {
            Intent intent = new Intent(MainActivity.this, HelpActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void calculate() {
        //criar uma nova Intent para chamar a ResultActivity
        //passando os valores recebidos para a atividade
        String weightString = inputWeight.getText().toString();
        String heightString = inputHeight.getText().toString();
        double weight = 0.0;
        double height = 0.0;

        if (!weightString.isEmpty() && !heightString.isEmpty()) {
            try {
                weight = Double.parseDouble(weightString);
                height = Double.parseDouble(heightString);

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter a valid input", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
        intent.putExtra(ResultActivity.VALUE_HEIGHT, height);
        intent.putExtra(ResultActivity.VALUE_WEIGHT, weight);
        //lançar a ResultActivity
        startActivity(intent);

    }
}