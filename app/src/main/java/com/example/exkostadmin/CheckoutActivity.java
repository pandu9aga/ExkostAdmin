package com.example.exkostadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    String nom;
    TextView nomtot,subtot,tot;
    Spinner spbnkadmin;

    private RequestQueue queue;
    SessionManager sessionManager;

    ProgressDialog progressDialog;

    LinearLayout chkView;
    TextInputLayout namal;

    Button check;

    String bank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        queue = Volley.newRequestQueue(CheckoutActivity.this);
        sessionManager = new SessionManager(CheckoutActivity.this);

        nomtot = findViewById(R.id.nomtot);
        subtot = findViewById(R.id.subtot);
        tot = findViewById(R.id.tot);

        namal = findViewById(R.id.namaPengirim);
        chkView = findViewById(R.id.checkview);
        check = findViewById(R.id.checkout);

        spbnkadmin = findViewById(R.id.spBank);
        addItemsOnSpinner();

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            nom = extras.getString("nom");
            if (nom.equals("10000")){
                nomtot.setText("Rp. 10.000");
                subtot.setText("Rp. 10.000");
                tot.setText("Rp. 10.000");
            }else if (nom.equals("20000")){
                nomtot.setText("Rp. 20.000");
                subtot.setText("Rp. 20.000");
                tot.setText("Rp. 20.000");
            }else if (nom.equals("50000")){
                nomtot.setText("Rp. 50.000");
                subtot.setText("Rp. 50.000");
                tot.setText("Rp. 50.000");
            }else if (nom.equals("100000")){
                nomtot.setText("Rp. 100.000");
                subtot.setText("Rp. 100.000");
                tot.setText("Rp. 100.000");
            }else if (nom.equals("500000")){
                nomtot.setText("Rp. 500.000");
                subtot.setText("Rp. 500.000");
                tot.setText("Rp. 500.000");
            }else if (nom.equals("1000000")){
                nomtot.setText("Rp. 1.000.000");
                subtot.setText("Rp. 1.000.000");
                tot.setText("Rp. 1.000.000");
            }
        }

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });

    }

    ArrayList<String> list;

    public void addItemsOnSpinner() {
        list = new ArrayList<String>();
        //list.add("semua");

        StringRequest reqData = new StringRequest(Request.Method.GET, Url.BANK_ADMIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //pd.cancel();
                        Log.d("volley","response : " + response.toString());
                        try {
                            JSONArray j = new JSONArray(response);
                            for(int i = 0 ; i < response.length(); i++)
                            {
                                try {
                                    JSONObject data = j.getJSONObject(i);
                                    list.add(data.getString("nama_bank_admin")+" ("+data.getString("no_rek_admin")+")");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            setSpinnerAdapter();
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //pd.cancel();
                        Log.d("volley", "error : " + error.getMessage());
                    }
                });
        queue.add(reqData);
    }

    private void setSpinnerAdapter(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(CheckoutActivity.this, android.R.layout.simple_spinner_dropdown_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spbnkadmin.setAdapter(adapter);
        spbnkadmin.setSelection(0);
        spbnkadmin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bank = spbnkadmin.getItemAtPosition(i).toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void goTopup() {
        final String iD = sessionManager.getIdAkun();
        final TextInputEditText namap = (TextInputEditText) findViewById(R.id.namapengirimText);
        progressDialog = new ProgressDialog(CheckoutActivity.this);
        progressDialog.setMessage("Proses");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.API_CHECK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("stat");
                    String id = jsonObject.getString("id");

                    progressDialog.dismiss();
                    if(status.equals("sukses")){
                        Intent intent = new Intent(CheckoutActivity.this, Buktitrans.class);
                        intent.putExtra("from","checkout");
                        intent.putExtra("idtopup",id);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(CheckoutActivity.this, "Error", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Snackbar.make(chkView, e.toString(), Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CheckoutActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", iD);
                params.put("nama_pembayar", namap.getText().toString().trim());
                params.put("nominal", nom);
                params.put("rek_admin", bank);

                return params;
            }
        };

        queue.add(stringRequest);
    }
    private boolean validateNama() {
        String namaInput = namal.getEditText().getText().toString().trim();

        if (namaInput.isEmpty()) {
            namal.setError("Nama pengirim harus diisi");
            return false;
        } else {
            namal.setError(null);
            namal.setErrorEnabled(false);
            return true;
        }
    }
    public void next() {
        if (validateNama()) {
            goTopup();
        }
    }

    public void pindahrekening(View v){
        Intent i = new Intent(CheckoutActivity.this, Rekening.class); //LoginActivity adalah aktivity awal ,praktikum1Activity activity yang akan di tuju
        startActivity(i);
    }

    public void pindahbukti(View v){
        Intent i = new Intent(CheckoutActivity.this, Buktitrans.class); //LoginActivity adalah aktivity awal ,praktikum1Activity activity yang akan di tuju
        startActivity(i);
    }

    public void pindahtopup(View v){
        Intent i = new Intent(CheckoutActivity.this, Topup.class); //LoginActivity adalah aktivity awal ,praktikum1Activity activity yang akan di tuju
        startActivity(i);
    }

    public void backHome(View v) {
        finish();
    }
}
