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
import com.example.exkostadmin.LelangView;
import com.example.exkostadmin.Model.ModelLelang;
import com.example.exkostadmin.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterLelang extends RecyclerView.Adapter<AdapterLelang.HolderData> {
    private List<ModelLelang> mItems ;
    private Context context;

    public AdapterLelang (Context context, List<ModelLelang> items)
    {
        this.mItems = items;
        this.context = context;
    }

    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_lelang,parent,false);
        HolderData holderData = new HolderData(layout);
        return holderData;
    }

    @Override
    public void onBindViewHolder(HolderData holder, int position) {
        ModelLelang md  = mItems.get(position);
        holder.namaBarang.setText(md.getNamaBarang());
        holder.namaJenis.setText(md.getNamaJenis());
        holder.hargaBarang.setText(md.getHargaBarang());
        holder.waktuLelang.setText(md.getWaktuLelang());
        Picasso.get().load(Url.ASSET_BARANG+md.getGambarBarang()).into(holder.gambarBarang);
        final String iD = md.getIdBarang();
        String statlel = md.getStatBid();
        String statgag = md.getStatFail();
        String stattrans = md.getStatTrans();

        if (statgag.equals("gagal")){
            holder.toAuc.setText("Gagal");
            holder.toAuc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent update = new Intent(context, LelangView.class);
                    update.putExtra("id_barang",iD);
                    //update.putExtra("stat","Gagal");
                    context.startActivity(update);
                }
            });
        }else {
            if (statlel.equals("berlangsung")){
                holder.toAuc.setText("Lihat");
                holder.toAuc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent update = new Intent(context, LelangView.class);
                        update.putExtra("id_barang",iD);
                        //update.putExtra("stat","Berlangsung");
                        context.startActivity(update);
                    }
                });
            }else if(statlel.equals("selesai")){
                holder.toAuc.setText("Kirim");
                holder.toAuc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent update = new Intent(context, LelangView.class);
                        update.putExtra("id_barang",iD);
                        //update.putExtra("stat","Kirim");
                        context.startActivity(update);
                    }
                });
            }else if(statlel.equals("kirim")){
                holder.toAuc.setText("Dikirim");
                holder.toAuc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent update = new Intent(context, LelangView.class);
                        update.putExtra("id_barang",iD);
                        //update.putExtra("stat","Dikirim");
                        context.startActivity(update);
                    }
                });
            }else if(statlel.equals("terima")){
                if(stattrans.equals("")){
                    holder.toAuc.setText("Tunggu");
                    holder.toAuc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent update = new Intent(context, LelangView.class);
                            update.putExtra("id_barang",iD);
                            //update.putExtra("stat","Tunggu");
                            context.startActivity(update);
                        }
                    });
                }else if(stattrans.equals("kirim")){
                    holder.toAuc.setText("Konfirm");
                    holder.toAuc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent update = new Intent(context, LelangView.class);
                            update.putExtra("id_barang",iD);
                            //update.putExtra("stat","Konfirm");
                            context.startActivity(update);
                        }
                    });
                }else if(stattrans.equals("terima")){
                    holder.toAuc.setText("Selesai");
                    holder.toAuc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent update = new Intent(context, LelangView.class);
                            update.putExtra("id_barang",iD);
                            //update.putExtra("stat","Selesai");
                            context.startActivity(update);
                        }
                    });
                }
            }
        }


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
        Button toAuc;
        String idBarang;
        ModelLelang md;

        public  HolderData (View view)
        {
            super(view);

            namaBarang = (TextView) view.findViewById(R.id.namaBarang);
            namaJenis = (TextView) view.findViewById(R.id.namaJenis);
            hargaBarang = (TextView) view.findViewById(R.id.hargaBarang);
            waktuLelang = (TextView) view.findViewById(R.id.waktuLelang);
            gambarBarang = (ImageView) view.findViewById(R.id.gambarBarang);
            toAuc = (Button) view.findViewById(R.id.toAuc);
        }
    }
}
