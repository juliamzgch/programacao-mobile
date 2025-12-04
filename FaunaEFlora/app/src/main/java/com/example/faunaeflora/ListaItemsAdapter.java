package com.example.faunaeflora;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.faunaeflora.model.Animal;
import com.example.faunaeflora.model.Flower;

import java.util.List;

public class ListaItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(Object item);
    }

    class AnimalViewHolder extends RecyclerView.ViewHolder {
        TextView nome;
        ImageView foto;
        AnimalViewHolder(View view) {
            super(view);
            nome = view.findViewById(R.id.txtNome);
            foto = view.findViewById(R.id.imgItem);
        }
        public void bind(final Object item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {

                    listener.onItemClick(item);
                }
            });
        }
    }

    class FlorViewHolder extends AnimalViewHolder {
        TextView mesCultivo;
        FlorViewHolder(View view) {
            super(view);
            mesCultivo = view.findViewById(R.id.txtMes);
        }
    }
    private final List<Object> items;
    private final OnItemClickListener itemClickListener;

    public ListaItemsAdapter(List<Object> items, OnItemClickListener clickListener) {
        this.items = items;
        this.itemClickListener = clickListener;
    }

    @Override
    public int getItemViewType(int position) {
        Object currObject = items.get(position);
        if (currObject instanceof Animal) {
            return 1;
        }
        if (currObject instanceof Flower) {
            return 2;
        }
        return 0;
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case 0:
            case 1:
            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_animal, parent, false);
                return new AnimalViewHolder(itemView);
            case 2:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_flower, parent, false);
                return new FlorViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:
            case 1:
            default:
                AnimalViewHolder animalViewHolder = (AnimalViewHolder) holder;
                animalViewHolder.nome.setText(((Animal) items.get(position)).getName());
                animalViewHolder.foto.setImageResource(((Animal) items.get(position)).getPhoto());
                animalViewHolder.bind(items.get(position), itemClickListener);
                break;
            case 2:
                FlorViewHolder florViewHolder = (FlorViewHolder) holder;
                florViewHolder.nome.setText(((Flower) items.get(position)).getName());
                florViewHolder.foto.setImageResource(((Flower) items.get(position)).getPhoto());
                florViewHolder.bind(items.get(position), itemClickListener);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
