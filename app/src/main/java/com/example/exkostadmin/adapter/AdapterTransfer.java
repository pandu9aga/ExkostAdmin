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
import com.example.exkostadmin.TransferView;
import com.example.exkostadmin.Model.ModelTransfer;
import com.example.exkostadmin.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterTransfer extends RecyclerView.Adapter<AdapterTransfer.HolderData> {
    private List<ModelTransfer> mItems ;
    private Context context;

    public AdapterTransfer(Context context, List<ModelTransfer> items)
    {
        this.mItems = items;
        this.context = context;
    }

    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_transfer,parent,false);
        HolderData holderData = new HolderData(layout);
        return holderData;
    }

    @Override
    public void onBindViewHolder(HolderData holder, int position) {
        ModelTransfer md  = mItems.get(position);
        holder.namaBarang.setText(md.getNamaBarang());
        holder.namaAkun.setText(md.getNamaAkun());
        holder.nominal.setText(md.getNominal());
        holder.rekening.setText(md.getRekening());
        final String iD = md.getIdPemenang();
        String stattrans = md.getStatTrans();

        if(stattrans.equals("")){
            holder.toTrans.setText("Kirim");
            holder.toTrans.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent update = new Intent(context, TransferView.class);
                    update.putExtra("id_pemenang",iD);
                    //update.putExtra("stat","Tunggu");
                    context.startActivity(update);
                }
            });
        }else if(stattrans.equals("kirim")){
            Picasso.get().load(Url.ASSET_TRANSFER+md.getGambarBukti()).into(holder.gambarBukti);
            holder.toTrans.setText("Tunggu");
            holder.toTrans.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent update = new Intent(context, TransferView.class);
                    update.putExtra("id_pemenang",iD);
                    //update.putExtra("stat","Konfirm");
                    context.startActivity(update);
                }
            });
        }else if(stattrans.equals("terima")){
            Picasso.get().load(Url.ASSET_TRANSFER+md.getGambarBukti()).into(holder.gambarBukti);
            holder.toTrans.setText("Sukses");
            holder.toTrans.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent update = new Intent(context, TransferView.class);
                    update.putExtra("id_pemenang",iD);
                    //update.putExtra("stat","Selesai");
                    context.startActivity(update);
                }
            });
        }else if(stattrans.equals("gagal")){
            Picasso.get().load(Url.ASSET_TRANSFER+md.getGambarBukti()).into(holder.gambarBukti);
            holder.toTrans.setText("gagal");
            holder.toTrans.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent update = new Intent(context, TransferView.class);
                    update.putExtra("id_pemenang",iD);
                    //update.putExtra("stat","Selesai");
                    context.startActivity(update);
                }
            });
        }
        holder.md = md;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    class HolderData extends RecyclerView.ViewHolder
    {
        TextView namaBarang, namaAkun, nominal, rekening;
        ImageView gambarBukti;
        Button toTrans;
        String idPemenang;
        ModelTransfer md;

        public  HolderData (View view)
        {
            super(view);

            namaBarang = (TextView) view.findViewById(R.id.namaBarang);
            namaAkun = (TextView) view.findViewById(R.id.namaAkun);
            nominal = (TextView) view.findViewById(R.id.nominal);
            rekening = (TextView) view.findViewById(R.id.noRekening);
            gambarBukti = (ImageView) view.findViewById(R.id.gambarBukti);
            toTrans = (Button) view.findViewById(R.id.toTrans);
        }
    }
}
