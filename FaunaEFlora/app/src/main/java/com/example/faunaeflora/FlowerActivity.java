package com.example.faunaeflora;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FlowerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower);


        Bundle extras = getIntent().getExtras();
        String nome = getIntent().getExtras().getString("nome");
        String mes = getIntent().getExtras().getString("mes");
        String cuidados = getIntent().getExtras().getString("cuidados");
        int foto = extras.getInt("foto");
        TextView textoNome = findViewById(R.id.txtNome);
        TextView textoMes = findViewById(R.id.txtMes);
        TextView textoCuidados = findViewById(R.id.txtCuidados);
        ImageView imagemFoto = findViewById(R.id.imgFoto);
        textoNome.setText(nome);
        textoMes.setText(mes);
        textoCuidados.setText(cuidados);
        imagemFoto.setImageResource(foto);

        Button back = findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}