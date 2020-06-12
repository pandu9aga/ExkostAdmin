package com.example.exkostadmin;

import android.os.Bundle;
import android.util.DisplayMetrics;
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

public class KonfirmTrans extends AppCompatActivity {
    Button btnKon;
    String iD;
    ScrollView ktView;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.konfirm_transfer);

        btnKon = findViewById(R.id.btnKonfirm);

        ktView = findViewById(R.id.parent_scroll);
        queue = Volley.newRequestQueue(KonfirmTrans.this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            iD = extras.getString("id_barang");
        }

        btnKon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confProcess();
            }
        });

        viewProcess();
    }
    private void viewProcess() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.TRANS_VIEW, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONObject highobject = jsonObject.getJSONObject("bid");

                    String nominal = highobject.getString("jumlah_tawaran");
                    String gambar = data.getString("bukti_transfer");

                    setdata(nominal,gambar);
                } catch (Exception e) {
                    Snackbar.make(ktView, e.toString(), Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(ktView, error.toString(), Snackbar.LENGTH_LONG).show();
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

    public void setdata(String nominal, String gambar){
        TextView nominalv = (TextView) findViewById(R.id.nominal);
        ImageView gambarv = (ImageView) findViewById(R.id.gambarBukti);
        nominalv.setText(nominal);
        Picasso.get().load(Url.ASSET_TRANSFER+gambar).into(gambarv);
        //get Screen Dimensions
        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        //NOTE: If you want to square, just use one of these value.
        //set as half of dimens
        gambarv.getLayoutParams().width = width/5*4;
        gambarv.getLayoutParams().height = width/2;
    }

    private void confProcess() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.TRANS_PROC, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    String idbarang = data.getString("id_barang");

                    finish();

                } catch (Exception e) {
                    Snackbar.make(ktView, e.toString(), Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(ktView, error.toString(), Snackbar.LENGTH_LONG).show();
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
