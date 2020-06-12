package com.example.exkostadmin;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BarangView extends AppCompatActivity{

    ScrollView barangView;
    TextInputLayout inputBid;
    TextInputEditText txtinputBid;
    SwipeRefreshLayout swipeBarang;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barang_view);

        inputBid = findViewById(R.id.inputBid);
        txtinputBid = findViewById(R.id.inputBidText);
        inputBid.setVisibility(View.GONE);
        txtinputBid.setVisibility(View.GONE);

        barangView = findViewById(R.id.parent_scroll);
        queue = Volley.newRequestQueue(BarangView.this);

        Intent intent = getIntent();
        String extras = intent.getStringExtra("pesan");
        if (extras != null){
            if (extras.equals("kurang")){
                Toast.makeText(BarangView.this, "Saldo tidak cukup",
                        Toast.LENGTH_LONG).show();
            }else if (extras.equals("sukses")){
                Toast.makeText(BarangView.this, "Tawaran ditambahkan",
                        Toast.LENGTH_LONG).show();
            }else if (extras.equals("selesai")){
                Toast.makeText(BarangView.this, "Barang telah diterima",
                        Toast.LENGTH_LONG).show();
            }else if (extras.equals("gagal")){
                Toast.makeText(BarangView.this, "Konfirmasi gagal",
                        Toast.LENGTH_LONG).show();
            }
        }

        swipeBarang = findViewById(R.id.swipeBarang);
        swipeBarang.setColorSchemeResources(R.color.red, R.color.red2);
        swipeBarang.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewProcess();
                if(swipeBarang.isRefreshing()) {
                    swipeBarang.setRefreshing(false);
                }
            }
        });

        viewProcess();
    }

    private void viewProcess() {
        Intent intent = getIntent();
        final String id_barang = intent.getStringExtra("id_barang");
        SessionManager sessionManager = new SessionManager(this);
        final String id_akun = sessionManager.getIdAkun();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.API_BARANG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONObject bid = jsonObject.getJSONObject("bid");
                    JSONObject highobject = jsonObject.getJSONObject("high");

                    String nama = data.getString("nama_barang");
                    String jenis = data.getString("nama_jenis_barang");
                    String waktu = data.getString("waktu_lelang");
                    String high = highobject.getString("jumlah_tawaran");
                    String your = bid.getString("jumlah_tawaran");
                    String pelelang = data.getString("nama_akun");
                    String alamat = data.getString("alamat_akun");
                    String catatan = data.getString("info_barang");
                    String statusl = data.getString("status_lelang");
                    String gambar = data.getString("nama_gambar_barang");

                    setdata(nama,jenis,waktu,high,your,pelelang,alamat,catatan,statusl,gambar);
                } catch (Exception e) {
                    Snackbar.make(barangView, e.toString(), Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(barangView, error.toString(), Snackbar.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id_barang);
                params.put("akun", id_akun);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void setdata(String nama, String jenis, String waktu, final String high, String your, String pelelang, String alamat, String catatan, String statusl, String gambar){
        TextView namav = (TextView) findViewById(R.id.namaBarang);
        TextView jenisv = (TextView) findViewById(R.id.namaJenis);
        TextView waktuv = (TextView) findViewById(R.id.waktuLelang);
        TextView highv = (TextView) findViewById(R.id.highBid);
        TextView yourv = (TextView) findViewById(R.id.yourBid);
        TextView pelelangv = (TextView) findViewById(R.id.pelelang);
        TextView alamatv = (TextView) findViewById(R.id.alamat);
        TextView catatanv = (TextView) findViewById(R.id.catatan);
        ImageView gambarv = (ImageView) findViewById(R.id.gambarBarang);
        Button btnbid = (Button) findViewById(R.id.letBid);
        Button btnstat = (Button) findViewById(R.id.statUs);
        TextInputLayout inputbid = (TextInputLayout) findViewById(R.id.inputBid);
        TextInputEditText txtinputbid = (TextInputEditText) findViewById(R.id.inputBidText);
        namav.setText(nama);
        jenisv.setText(jenis);
        waktuv.setText(waktu);
        highv.setText(high);
        yourv.setText(your);
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

        final String min = high;

        if (statusl.equals("berlangsung")){
            inputbid.setVisibility(View.VISIBLE);
            txtinputbid.setVisibility(View.VISIBLE);
            btnstat.setVisibility(View.GONE);
            btnbid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    validateBid(min);
                }
            });
        }else if(statusl.equals("selesai")){
            if (high.equals(your)) {
                inputbid.setVisibility(View.GONE);
                txtinputbid.setVisibility(View.GONE);
                btnbid.setVisibility(View.GONE);
                btnstat.setText("Menunggu Dikirim");
            }else {
                inputbid.setVisibility(View.GONE);
                txtinputbid.setVisibility(View.GONE);
                btnbid.setVisibility(View.GONE);
                btnstat.setText("Kalah");
            }
        }else if(statusl.equals("kirim")){
            if (high.equals(your)) {
                inputbid.setVisibility(View.GONE);
                txtinputbid.setVisibility(View.GONE);
                btnstat.setText("Sedang Dikirim");
                btnbid.setText("Diterima");
                btnbid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        validateGet();
                    }
                });
            }else {
                inputbid.setVisibility(View.GONE);
                txtinputbid.setVisibility(View.GONE);
                btnbid.setVisibility(View.GONE);
                btnstat.setText("Kalah");
            }
        }else if(statusl.equals("terima")){
            if (high.equals(your)) {
                inputbid.setVisibility(View.GONE);
                txtinputbid.setVisibility(View.GONE);
                btnbid.setVisibility(View.GONE);
                btnstat.setText("Selesai");
            }else {
                inputbid.setVisibility(View.GONE);
                txtinputbid.setVisibility(View.GONE);
                btnbid.setVisibility(View.GONE);
                btnstat.setText("Kalah");
            }
        }

    }

    public void backHome(View v) {
        Intent main = new Intent(BarangView.this, HomeActivity.class);
        startActivity(main);
    }

    private boolean validateBid(String min) {
        String bidInput = inputBid.getEditText().getText().toString().trim();
        if (bidInput.isEmpty()) {
            inputBid.setError("Tawaran tidak boleh kosong");
            return false;
        }else {
            int thebid = Integer.parseInt(bidInput);
            int minim = Integer.parseInt(min);
            if (thebid <= minim){
                inputBid.setError("Tawaran harus lebih tinggi dari "+minim);
                return false;
            }else {
                inputBid.setError(null);
                bidProcess();
                return true;
            }
        }
    }

    private void validateGet(){
        Intent intent = getIntent();
        final String id_barang = intent.getStringExtra("id_barang");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.API_TERIMA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String data = jsonObject.getString("pesan");

                    if (data.equals("selesai")){
                        Intent intent = new Intent(BarangView.this, BarangView.class);
                        intent.putExtra("stat_bid","selesai");
                        intent.putExtra("id_barang",id_barang);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(BarangView.this, BarangView.class);
                        intent.putExtra("stat_bid","gagal");
                        intent.putExtra("id_barang",id_barang);
                        startActivity(intent);
                    }


                } catch (Exception e) {
                    Snackbar.make(barangView, e.toString(), Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(barangView, error.toString(), Snackbar.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_barang", id_barang);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    private void bidProcess() {
        final String bidInput = inputBid.getEditText().getText().toString().trim();
        Intent intent = getIntent();
        final String id_barang = intent.getStringExtra("id_barang");
        SessionManager sessionManager = new SessionManager(this);
        final String id_akun = sessionManager.getIdAkun();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.API_TAWAR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String data = jsonObject.getString("pesan");

                    if (data.equals("kurang")){
                        Intent intent = new Intent(BarangView.this, BarangView.class);
                        intent.putExtra("stat_bid","kurang");
                        intent.putExtra("id_barang",id_barang);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(BarangView.this, BarangView.class);
                        intent.putExtra("stat_bid","sukses");
                        intent.putExtra("id_barang",id_barang);
                        startActivity(intent);
                    }


                } catch (Exception e) {
                    Snackbar.make(barangView, e.toString(), Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(barangView, error.toString(), Snackbar.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_barang", id_barang);
                params.put("id_akun", id_akun);
                params.put("jumlah", bidInput);
                return params;
            }
        };

        queue.add(stringRequest);
    }
}

