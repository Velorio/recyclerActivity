package com.example.recycleractivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.ViewHolder> {
    private List<Evento> mData;
    private LayoutInflater mInflater;
    private Context context;
    private ItemClickListener mitemClickListener;

    public EventoAdapter(List<Evento> items, Context context, ItemClickListener itemClickListener){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = items;
        this.mitemClickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {return mData.size();}

    public interface ItemClickListener{
        void onItemClick(Evento evento);
    }

    @Override
    public EventoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.item_evento, null);
        return new EventoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EventoAdapter.ViewHolder holder, final int position){
        holder.bindData(mData.get(position));

        holder.itemView.setOnClickListener(view -> {
            mitemClickListener.onItemClick(mData.get(position));
        });
    }

    public void setItems(List<Evento> items){mData=items;}

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView icono;
        TextView deporteTextView, organizadorTextView;

        ViewHolder(View itemView){
            super(itemView);
            icono = itemView.findViewById(R.id.itemimage);
            deporteTextView = itemView.findViewById(R.id.itemTVDeporte);
            organizadorTextView = itemView.findViewById(R.id.itemTVOrganizador);
        }

        void bindData(final Evento item){
            deporteTextView.setText(item.getDeporte());
            organizadorTextView.setText(item.getOrganizador());
        }
    }
}
