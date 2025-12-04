package com.example.faunaeflora;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import com.example.faunaeflora.model.ListInitializer;
import com.example.faunaeflora.model.Animal;
import com.example.faunaeflora.model.Flower;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.concurrent.Flow;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.lista);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        ListaItemsAdapter adapter = new ListaItemsAdapter(ListInitializer.getList(), new
                ListaItemsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Object item) {
                        if (item instanceof  Animal) {
                            Animal animal = (Animal) item;
                            Bundle bundle = new Bundle();
                            bundle.putString("nome", animal.getName());
                            bundle.putString("habitat", animal.getHabitat());
                            bundle.putInt("foto", animal.getPhoto());
                            Intent intent = new Intent(MainActivity.this, AnimalActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else if (item instanceof Flower) {
                            Flower flower = (Flower) item;
                            Bundle bundle = new Bundle();
                            bundle.putString("nome", flower.getName());
                            bundle.putString("mes", flower.getSeedingMonth());
                            bundle.putString("cuidados", flower.getSeedingWarnings());
                            bundle.putInt("foto", flower.getPhoto());
                            Intent intent = new Intent(MainActivity.this, FlowerActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }
                });
        recyclerView.setAdapter(adapter);

    }
}