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
        holder.nominal.setText(md.getNominal());
        Picasso.get().load(Url.ASSET_TOPUP+md.getBukti()).into(holder.gambarBukti);

        holder.delTopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delProcess(md.getId());
                //AppCompatActivity activity = (AppCompatActivity) view.getContext();
                //Fragment myFragment = new BtmmenuTopup();
                //activity.getSupportFragmentManager().beginTransaction().replace(R.id.pager, myFragment).addToBackStack(null).commit();
            }
        });

        String stattop = md.getStatus();
        if (stattop.equals("belum")){
            holder.status.setText("Belum Upload Bukti");
            holder.toDetail.setText("Upload");
        }else if(stattop.equals("menunggu")){
            holder.delTopup.setVisibility(View.GONE);
            holder.status.setText("Tunggu Konfirmasi");
            holder.toDetail.setText("Upload");
        }else if(stattop.equals("sukses")){
            holder.delTopup.setVisibility(View.GONE);
            holder.status.setText("Sukses");
            holder.toDetail.setText("Lihat");
        }else if(stattop.equals("gagal")){
            holder.status.setText("Gagal");
            holder.toDetail.setText("Upload");
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

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.DEL_TOPUP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String stat = jsonObject.getString("stat");

                    if (stat.equals("sukses")) {
                        Toast.makeText(context, "Topup Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                    }

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
                return params;
            }
        };

        queue.add(stringRequest);
    }


    class HolderData extends RecyclerView.ViewHolder
    {
        TextView nominal,status;
        ImageView gambarBukti;
        Button toDetail,delTopup;
        ModelTopup md;

        public  HolderData (View view)
        {
            super(view);

            nominal = (TextView) view.findViewById(R.id.nominalTopup);
            status = (TextView) view.findViewById(R.id.statusTopup);
            gambarBukti = (ImageView) view.findViewById(R.id.gambarBukti);

            toDetail = (Button) view.findViewById(R.id.goDetail);
            delTopup = (Button) view.findViewById(R.id.delTop);

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
