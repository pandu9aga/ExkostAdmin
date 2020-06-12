package com.example.exkostadmin.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.exkostadmin.JenisUpdate;
import com.example.exkostadmin.HomeActivity;
import com.example.exkostadmin.Model.ModelJenis;
import com.example.exkostadmin.R;
import com.example.exkostadmin.SessionManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterJenis extends RecyclerView.Adapter<AdapterJenis.HolderData> {
    private List<ModelJenis> mItems ;
    private Context context;

    SessionManager sessionManager;
    private RequestQueue queue;

    public AdapterJenis(Context context, List<ModelJenis> items)
    {
        this.mItems = items;
        this.context = context;
    }

    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_jenis,parent,false);
        HolderData holderData = new HolderData(layout);
        return holderData;
    }

    @Override
    public void onBindViewHolder(HolderData holder, int position) {
        final ModelJenis md  = mItems.get(position);
        holder.namaJenis.setText(md.getNamaJenis());

        holder.delJenis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delProcess(md.getIdJenis());
            }
        });

        holder.md = md;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private void delProcess(final String id) {
        queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.DELETE_JENIS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String msg = jsonObject.getString("pesan");

                    if (msg.equals("true")){
                        Toast.makeText(context, "Sukses menghapus", Toast.LENGTH_SHORT).show();
                        Intent update = new Intent(context, HomeActivity.class);
                        context.startActivity(update);
                    }else {
                        Toast.makeText(context, "Gagal menghapus", Toast.LENGTH_SHORT).show();
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
                params.put("id_jenis", id);
                return params;
            }
        };

        queue.add(stringRequest);
    }


    class HolderData extends RecyclerView.ViewHolder
    {
        TextView namaJenis;
        Button editJenis,delJenis;
        ModelJenis md;

        public  HolderData (View view)
        {
            super(view);

            namaJenis = (TextView) view.findViewById(R.id.namaJenis);

            editJenis = (Button) view.findViewById(R.id.goTo);
            delJenis = (Button) view.findViewById(R.id.delJenis);

            editJenis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent update = new Intent(context, JenisUpdate.class);
                    update.putExtra("id_jenis",md.getIdJenis());

                    context.startActivity(update);
                }
            });
        }
    }
}
