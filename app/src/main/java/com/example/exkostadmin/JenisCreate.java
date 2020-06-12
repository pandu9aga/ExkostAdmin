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

public class JenisCreate extends AppCompatActivity{

    ScrollView jenisView;
    TextInputLayout inputJenis;
    TextInputEditText txtinputJenis;
    SwipeRefreshLayout swipeJenis;

    Button updatej;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jenis_create);

        inputJenis = findViewById(R.id.inputJenis);
        txtinputJenis = findViewById(R.id.inputJenisText);

        jenisView = findViewById(R.id.parent_scroll);
        queue = Volley.newRequestQueue(JenisCreate.this);

        swipeJenis = findViewById(R.id.swipeJenis);
        swipeJenis.setColorSchemeResources(R.color.red, R.color.red2);
        swipeJenis.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
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
            addjenisProcess();
            return true;
        }
    }

    private void addjenisProcess() {
        final String jenisInput = inputJenis.getEditText().getText().toString().trim();
        Intent intent = getIntent();
        final String id_jenis = intent.getStringExtra("id_jenis");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.CREATE_JENIS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String msg = jsonObject.getString("pesan");

                    if (msg.equals("fail")){
                        Toast.makeText(JenisCreate.this, "Jenis sudah ada", Toast.LENGTH_SHORT).show();
                    }else {
                        JSONObject data = jsonObject.getJSONObject("data");
                        String id = data.getString("id_jenis_barang");
                        Intent update = new Intent(JenisCreate.this, JenisUpdate.class);
                        update.putExtra("id_jenis",id);
                        JenisCreate.this.startActivity(update);
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
                params.put("nama_jenis", jenisInput);
                return params;
            }
        };

        queue.add(stringRequest);
    }
}

