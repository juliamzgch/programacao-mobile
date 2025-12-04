package com.example.bmi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat; // Importação recomendada para cores

public class ResultActivity extends AppCompatActivity {

    public static final String VALUE_HEIGHT = "VALUE_HEIGHT";
    public static final String VALUE_WEIGHT = "VALUE_WEIGHT";
    private double imc;
    // Não precisamos de declarar o botão como uma variável de classe se só o usamos no onCreate
    // Button btRecalculate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) { // Mudei de 'public' para 'protected'
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // A linha abaixo pode causar um erro se a Toolbar não estiver configurada.
        // Se a app crashar aqui, adicione uma Toolbar ao seu XML e configure-a.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // É melhor inicializar e configurar o botão aqui
        Button btRecalculate = findViewById(R.id.btRecalculate);
        btRecalculate.setOnClickListener(v -> finish());

        // 1. Obter os extras do Intent
        Bundle extras = getIntent().getExtras();

        // 2. Chamar o método para calcular, passando os 'extras' como argumento
        calcularMostrarResultado(extras);
    }

    private void calcularMostrarResultado(Bundle extras) { // O método agora recebe os 'extras'
        if (extras == null) {
            // Se por alguma razão não houver extras, paramos a execução para evitar erros
            return;
        }

        double altura = extras.getDouble(VALUE_HEIGHT);
        double peso = extras.getDouble(VALUE_WEIGHT);

        if (altura > 0 && peso > 0) {
            imc = peso / (altura * altura);
        }

        // Definir o resultado do IMC no TextView
        TextView txtResultado = findViewById(R.id.bmiResult);
        txtResultado.setText(String.format("%.2f", imc));

        // Alterar categoria de IMC
        TextView txtCategoria = findViewById(R.id.bmiCategory);
        String texto;
        int cor;

        if (imc < 18.5) {
            texto = getString(R.string.underweight);
            cor = ContextCompat.getColor(this, R.color.underweight); // Forma mais segura de obter cores
        } else if (imc < 25) {
            texto = getString(R.string.normalweight);
            cor = ContextCompat.getColor(this, R.color.normalweight);
        } else {
            texto = getString(R.string.overweight);
            cor = ContextCompat.getColor(this, R.color.overweight);
        }
        txtCategoria.setText(texto);
        txtCategoria.setBackgroundColor(cor);
    }

    // O método calcularIMC não estava a ser usado,
    //  mas se o quiser manter, está correto.
    // private double calcularIMC(double height, double weight) {
    //     return (weight) / (height * height);
    // }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_help) {
            Intent intent = new Intent(ResultActivity.this, HelpActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_send_sms) {
            enviarSMS();
            return true;
        } else if (id == android.R.id.home) {
            finish(); // Isto trata o clique na seta de "voltar" na barra de ferramentas
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void enviarSMS() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("sms:"));
        // Usamos 'imc' que já foi calculado e guardado na variável de classe
        intent.putExtra("sms_body", String.format("My BMI result is %.2f", imc));
        startActivity(intent);
    }
}
