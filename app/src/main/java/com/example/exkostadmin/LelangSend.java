package com.example.exkostadmin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.exkostadmin.Api.Url;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LelangSend extends AppCompatActivity{

    Button btnSend;
    String iD;
    ScrollView lsView;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_winner);

        queue = Volley.newRequestQueue(LelangSend.this);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            iD = extras.getString("id_barang");
        }

        lsView = findViewById(R.id.parent_scroll);
        btnSend = findViewById(R.id.btnKirim);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendProcess();
            }
        });

        viewProcess();

    }

    private void viewProcess() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.AUC_SEND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONObject highobject = jsonObject.getJSONObject("bid");
                    JSONObject win = jsonObject.getJSONObject("winner");

                    String nama = data.getString("nama_barang");
                    String jenis = data.getString("nama_jenis_barang");
                    String waktu = data.getString("waktu_lelang");
                    String high = highobject.getString("jumlah_tawaran");
                    String harga = data.getString("harga_barang");
                    String gambar = data.getString("nama_gambar_barang");
                    String pemenang = win.getString("nama_akun");
                    String alamatwin = win.getString("alamat_akun");


                    setdata(nama,jenis,waktu,high,gambar,harga,pemenang,alamatwin);
                } catch (Exception e) {
                    Snackbar.make(lsView, e.toString(), Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(lsView, error.toString(), Snackbar.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", iD);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void setdata(String nama, String jenis, String waktu, final String high, String gambar, String harga, String pemenang, String alamatwin){
        TextView namav = (TextView) findViewById(R.id.namaBarang);
        TextView jenisv = (TextView) findViewById(R.id.namaJenis);
        TextView waktuv = (TextView) findViewById(R.id.waktuLelang);
        TextView highv = (TextView) findViewById(R.id.highBid);
        TextView hargav = (TextView) findViewById(R.id.hargaBarang);
        TextView pemenangv = (TextView) findViewById(R.id.pemenang);
        TextView addWin = (TextView) findViewById(R.id.addWinner);
        ImageView gambarv = (ImageView) findViewById(R.id.gambarBarang);
        namav.setText(nama);
        jenisv.setText(jenis);
        waktuv.setText(waktu);
        highv.setText(high);
        hargav.setText(harga);
        pemenangv.setText(pemenang);
        addWin.setText(alamatwin);
        Picasso.get().load(Url.ASSET_BARANG+gambar).into(gambarv);
    }

    private void sendProcess() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.TO_SEND, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    String idbarang = data.getString("id_barang");

                    finish();

                } catch (Exception e) {
                    Snackbar.make(lsView, e.toString(), Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(lsView, error.toString(), Snackbar.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", iD);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void backHome(View v) {
        finish();
    }
}
