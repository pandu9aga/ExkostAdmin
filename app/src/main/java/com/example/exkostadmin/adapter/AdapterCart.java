package com.example.exkostadmin.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.exkostadmin.BarangView;
import com.example.exkostadmin.HomeActivity;
import com.example.exkostadmin.Model.ModelCart;
import com.example.exkostadmin.R;
import com.example.exkostadmin.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterCart extends RecyclerView.Adapter<AdapterCart.HolderData> {
    private List<ModelCart> mItems ;
    private Context context;

    SessionManager sessionManager;
    private RequestQueue queue;

    public AdapterCart (Context context, List<ModelCart> items)
    {
        this.mItems = items;
        this.context = context;
    }

    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cart,parent,false);
        HolderData holderData = new HolderData(layout);
        return holderData;
    }

    @Override
    public void onBindViewHolder(HolderData holder, int position) {
        final ModelCart md  = mItems.get(position);
        holder.namaBarang.setText(md.getNamaBarang());
        holder.waktuLelang.setText(md.getWaktuLelang());
        holder.highBid.setText(md.getHighBid());
        holder.myBid.setText(md.getMyBid());
        Picasso.get().load(Url.ASSET_BARANG+md.getGambarBarang()).into(holder.gambarBarang);

        String hb = md.getHighBid();
        String mb = md.getMyBid();

        holder.delCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delProcess(md.getIdBarang());
            }
        });

        String statlelang = md.getStatBid();
        if (statlelang.equals("berlangsung")){
            if (hb.equals(mb)){
                holder.delCart.setVisibility(View.GONE);
            }
            holder.toBarang.setText("Berlangsung");
        }else if(statlelang.equals("selesai")){
            if (hb.equals(mb)){
                holder.toBarang.setText("Tunggu Dikirim");
                holder.delCart.setVisibility(View.GONE);
            }else {
                holder.toBarang.setText("Kalah");
            }
        }else if(statlelang.equals("kirim")){
            if (hb.equals(mb)) {
                holder.toBarang.setText("Sedang Dikirim");
                holder.delCart.setVisibility(View.GONE);
            }else {
                holder.toBarang.setText("Kalah");
            }
        }else if(statlelang.equals("terima")){
            if (hb.equals(mb)) {
                holder.toBarang.setText("Selesai");
                holder.delCart.setVisibility(View.GONE);
            }else {
                holder.toBarang.setText("Kalah");
            }
        }

        holder.md = md;

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private void delProcess(final String id) {
        sessionManager = new SessionManager(context);
        queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.DEL_CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    String idbarang = data.getString("id_barang");

                    Intent update = new Intent(context, HomeActivity.class);
                    context.startActivity(update);

                } catch (Exception e) {
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(),Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("id_akun", sessionManager.getIdAkun());
                return params;
            }
        };

        queue.add(stringRequest);
    }


    class HolderData extends RecyclerView.ViewHolder
    {
        TextView namaBarang, waktuLelang, highBid, myBid;
        ImageView gambarBarang;
        Button toBarang,delCart;
        ModelCart md;

        public  HolderData (View view)
        {
            super(view);

            namaBarang = (TextView) view.findViewById(R.id.namaBarang);
            waktuLelang = (TextView) view.findViewById(R.id.waktuLelang);
            highBid = (TextView) view.findViewById(R.id.highBid);
            myBid = (TextView) view.findViewById(R.id.myBid);
            gambarBarang = (ImageView) view.findViewById(R.id.gambarBarang);

            toBarang = (Button) view.findViewById(R.id.goTo);
            delCart = (Button) view.findViewById(R.id.delCart);

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
