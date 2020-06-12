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

public class JenisUpdate extends AppCompatActivity{

    ScrollView jenisView;
    TextInputLayout inputJenis;
    TextInputEditText txtinputJenis;
    SwipeRefreshLayout swipeJenis;

    Button updatej;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jenis_edit);

        inputJenis = findViewById(R.id.inputJenis);
        txtinputJenis = findViewById(R.id.inputJenisText);

        jenisView = findViewById(R.id.parent_scroll);
        queue = Volley.newRequestQueue(JenisUpdate.this);

        swipeJenis = findViewById(R.id.swipeJenis);
        swipeJenis.setColorSchemeResources(R.color.red, R.color.red2);
        swipeJenis.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewProcess();
                if(swipeJenis.isRefreshing()) {
                    swipeJenis.setRefreshing(false);
                }
            }
        });

        updatej = findViewById(R.id.updateJenis);
        updatej.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateJenis();
            }
        });

        viewProcess();
    }

    private void viewProcess() {
        Intent intent = getIntent();
        final String id_jenis = intent.getStringExtra("id_jenis");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.VIEW_JENIS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String nama = jsonObject.getString("nama_jenis_barang");

                    setdata(nama);
                } catch (Exception e) {
                    Snackbar.make(jenisView, e.toString(), Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(jenisView, error.toString(), Snackbar.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_jenis", id_jenis);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void setdata(String nama){
        TextView namav = (TextView) findViewById(R.id.namaJenis);
        namav.setText(nama);
    }

    public void backHome(View v) {
        finish();
    }

    private boolean validateJenis() {
        String jenisInput = inputJenis.getEditText().getText().toString().trim();
        if (jenisInput.isEmpty()) {
            inputJenis.setError("Nama jenis tidak boleh kosong");
            return false;
        }else {
            inputJenis.setError(null);
            updatejenisProcess();
            return true;
        }
    }

    private void updatejenisProcess() {
        final String jenisInput = inputJenis.getEditText().getText().toString().trim();
        Intent intent = getIntent();
        final String id_jenis = intent.getStringExtra("id_jenis");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.UPDATE_JENIS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String data = jsonObject.getString("pesan");

                    if (data.equals("true")){
                        viewProcess();
                        Toast.makeText(JenisUpdate.this, "Sukses Update", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(JenisUpdate.this, "Gagal Update", Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    Snackbar.make(jenisView, e.toString(), Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(jenisView, error.toString(), Snackbar.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_jenis", id_jenis);
                params.put("nama_jenis", jenisInput);
                return params;
            }
        };

        queue.add(stringRequest);
    }
}

