package com.example.exkostadmin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

public class LelangView extends AppCompatActivity{

    Button btnStat;
    String iD;
    ScrollView lelangView;
    SwipeRefreshLayout swipeLelang;

    Fragment fragment;
    FragmentTransaction transaction;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lelang_view);

        btnStat = findViewById(R.id.btnStat);

        lelangView = findViewById(R.id.parent_scroll);
        queue = Volley.newRequestQueue(LelangView.this);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            iD = extras.getString("id_barang");
        }

        swipeLelang = findViewById(R.id.swipeLelang);
        swipeLelang.setColorSchemeResources(R.color.red, R.color.red2);
        swipeLelang.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewProcess();
                if(swipeLelang.isRefreshing()) {
                    swipeLelang.setRefreshing(false);
                }
            }
        });

        viewProcess();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        viewProcess();
    }

    private void viewProcess() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.AUC_BARANG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONObject highobject = jsonObject.getJSONObject("high");

                    String nama = data.getString("nama_barang");
                    String jenis = data.getString("nama_jenis_barang");
                    String waktu = data.getString("waktu_lelang");
                    String high = highobject.getString("jumlah_tawaran");
                    String pelelang = data.getString("nama_akun");
                    String alamat = data.getString("alamat_akun");
                    String harga = data.getString("harga_barang");
                    String catatan = data.getString("info_barang");
                    String statusl = data.getString("status_lelang");
                    String statusgag = data.getString("status_gagal");
                    String statustrans = data.getString("status_transfer");
                    String gambar = data.getString("nama_gambar_barang");

                    setdata(nama,jenis,waktu,high,pelelang,alamat,catatan,statusl,gambar,harga,statusgag,statustrans);
                } catch (Exception e) {
                    Snackbar.make(lelangView, e.toString(), Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(lelangView, error.toString(), Snackbar.LENGTH_LONG).show();
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

    public void setdata(String nama, String jenis, String waktu, final String high, String pelelang, String alamat, String catatan, String statusl, String gambar, String harga, String statusgag, String statustrans){
        TextView namav = (TextView) findViewById(R.id.namaBarang);
        TextView jenisv = (TextView) findViewById(R.id.namaJenis);
        TextView waktuv = (TextView) findViewById(R.id.waktuLelang);
        TextView highv = (TextView) findViewById(R.id.highBid);
        TextView hargav = (TextView) findViewById(R.id.hargaBarang);
        TextView pelelangv = (TextView) findViewById(R.id.pelelang);
        TextView alamatv = (TextView) findViewById(R.id.alamat);
        TextView catatanv = (TextView) findViewById(R.id.catatan);
        ImageView gambarv = (ImageView) findViewById(R.id.gambarBarang);
        namav.setText(nama);
        jenisv.setText(jenis);
        waktuv.setText(waktu);
        highv.setText(high);
        hargav.setText(harga);
        pelelangv.setText(pelelang);
        alamatv.setText(alamat);
        catatanv.setText(catatan);
        Picasso.get().load(Url.ASSET_BARANG+gambar).into(gambarv);

        //get Screen Dimensions
        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        //NOTE: If you want to square, just use one of these value.
        //set as half of dimens
        gambarv.getLayoutParams().width = width/5*4;
        gambarv.getLayoutParams().height = width/2;

        if (statusgag.equals("gagal")){
            btnStat.setText("Gagal");
            btnStat.setBackgroundColor(Color.parseColor("#0066ff"));
        }else {
            if (statusl.equals("berlangsung")){
                btnStat.setText("Berlangsung");
                btnStat.setBackgroundColor(Color.parseColor("#0066ff"));
            }else if(statusl.equals("selesai")){
                btnStat.setText("Kirim");
                btnStat.setBackgroundColor(Color.parseColor("#D10024"));
                btnStat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(LelangView.this, LelangSend.class);
                        intent.putExtra("id_barang",iD);
                        startActivity(intent);
                    }
                });
            }else if(statusl.equals("kirim")){
                btnStat.setText("Dikirim");
                btnStat.setBackgroundColor(Color.parseColor("#0066ff"));
            }else if(statusl.equals("terima")){
                if(statustrans.equals("")){
                    btnStat.setText("Tunggu Transfer");
                    btnStat.setBackgroundColor(Color.parseColor("#0066ff"));
                }else if(statustrans.equals("kirim")){
                    btnStat.setText("Konfirm");
                    btnStat.setBackgroundColor(Color.parseColor("#D10024"));
                    btnStat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(LelangView.this, KonfirmTrans.class);
                            intent.putExtra("id_barang",iD);
                            startActivity(intent);
                        }
                    });
                }else if(statustrans.equals("terima")){
                    btnStat.setText("Selesai");
                    btnStat.setBackgroundColor(Color.parseColor("#0066ff"));
                }
            }
        }

    }

    public void backHome(View v) {
        finish();
    }
}
