package com.example.exkostadmin.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.exkostadmin.Api.Url;
import com.example.exkostadmin.Buktitrans;
import com.example.exkostadmin.Model.ModelTopup;
import com.example.exkostadmin.R;
import com.example.exkostadmin.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterTopup extends RecyclerView.Adapter<AdapterTopup.HolderData> {
    private List<ModelTopup> mItems ;
    private Context context;

    SessionManager sessionManager;
    private RequestQueue queue;

    public AdapterTopup (Context context, List<ModelTopup> items)
    {
        this.mItems = items;
        this.context = context;
    }

    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_topup,parent,false);
        HolderData holderData = new HolderData(layout);
        return holderData;
    }

    @Override
    public void onBindViewHolder(HolderData holder, int position) {
        final ModelTopup md  = mItems.get(position);
        holder.nominal.setText("Rp. "+md.getNominal());
        //Picasso.get().load(Url.ASSET_TOPUP+md.getBukti()).into(holder.gambarBukti);

        String stattop = md.getStatus();
        if (stattop.equals("belum")){
            holder.status.setText("Belum Upload Bukti");
            holder.toDetail.setText("Belum Upload");
        }else if(stattop.equals("menunggu")){
            Picasso.get().load(Url.ASSET_TOPUP+md.getBukti()).into(holder.gambarBukti);
            holder.status.setText("Tunggu Konfirmasi");
            holder.toDetail.setText("Konfirmasi");
        }else if(stattop.equals("sukses")){
            Picasso.get().load(Url.ASSET_TOPUP+md.getBukti()).into(holder.gambarBukti);
            holder.status.setText("Sukses");
            holder.toDetail.setText("Sukses");
        }else if(stattop.equals("gagal")){
            Picasso.get().load(Url.ASSET_TOPUP+md.getBukti()).into(holder.gambarBukti);
            holder.status.setText("Gagal");
            holder.toDetail.setText("Gagal");
        }

        //get Screen Dimensions
        DisplayMetrics metrics = holder.gambarBukti.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        //NOTE: If you want to square, just use one of these value.
        //set as half of dimens
        holder.gambarBukti.getLayoutParams().width = width/3;
        holder.gambarBukti.getLayoutParams().height = width/5;

        holder.md = md;

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    class HolderData extends RecyclerView.ViewHolder
    {
        TextView nominal,status;
        ImageView gambarBukti;
        Button toDetail;
        ModelTopup md;

        public  HolderData (View view)
        {
            super(view);

            nominal = (TextView) view.findViewById(R.id.nominalTopup);
            status = (TextView) view.findViewById(R.id.statusTopup);
            gambarBukti = (ImageView) view.findViewById(R.id.gambarBukti);

            toDetail = (Button) view.findViewById(R.id.goDetail);
            toDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent update = new Intent(context, Buktitrans.class);
                    update.putExtra("idtopup",md.getId());

                    context.startActivity(update);
                }
            });
        }
    }
}
