package com.example.exkostadmin.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.exkostadmin.Api.Url;
import com.example.exkostadmin.BarangView;
import com.example.exkostadmin.Model.ModelSearch;
import com.example.exkostadmin.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterSearch extends RecyclerView.Adapter<AdapterSearch.HolderData> {
    private List<ModelSearch> mItems ;
    private Context context;

    public AdapterSearch (Context context, List<ModelSearch> items)
    {
        this.mItems = items;
        this.context = context;
    }

    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_home,parent,false);
        HolderData holderData = new HolderData(layout);
        return holderData;
    }

    @Override
    public void onBindViewHolder(HolderData holder, int position) {
        ModelSearch md  = mItems.get(position);
        holder.namaBarang.setText(md.getNamaBarang());
        holder.namaJenis.setText(md.getNamaJenis());
        holder.hargaBarang.setText(md.getHargaBarang());
        holder.waktuLelang.setText(md.getWaktuLelang());
        Picasso.get().load(Url.ASSET_BARANG+md.getGambarBarang()).into(holder.gambarBarang);

        holder.md = md;


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    class HolderData extends RecyclerView.ViewHolder
    {
        TextView namaBarang, namaJenis, hargaBarang, waktuLelang;
        ImageView gambarBarang;
        Button toBarang;
        String idBarang;
        ModelSearch md;

        public  HolderData (View view)
        {
            super(view);

            namaBarang = (TextView) view.findViewById(R.id.namaBarang);
            namaJenis = (TextView) view.findViewById(R.id.namaJenis);
            hargaBarang = (TextView) view.findViewById(R.id.hargaBarang);
            waktuLelang = (TextView) view.findViewById(R.id.waktuLelang);
            gambarBarang = (ImageView) view.findViewById(R.id.gambarBarang);

            toBarang = (Button) view.findViewById(R.id.toBarang);

            toBarang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent update = new Intent(context, BarangView.class);
                    update.putExtra("id_barang",md.getIdBarang());

                    context.startActivity(update);
                }
            });
        }
    }
}
