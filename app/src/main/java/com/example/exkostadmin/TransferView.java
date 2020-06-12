package com.example.exkostadmin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class TransferView extends AppCompatActivity {

    private RequestQueue queue;
    SessionManager sessionManager;

    ProgressDialog progressDialog;

    SwipeRefreshLayout swipeBarang;

    private final int IMG_REQUEST = 1;
    private String mBitmapName;
    Bitmap bitmap;

    String idTrans;
    String idBarang,theTransid;
    LinearLayout detilView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer_view);

        queue = Volley.newRequestQueue(TransferView.this);
        sessionManager = new SessionManager(TransferView.this);

        detilView = findViewById(R.id.detailToplayout);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            idTrans = extras.getString("id_pemenang");
        }

        swipeBarang = findViewById(R.id.swipeDetop);
        swipeBarang.setColorSchemeResources(R.color.red, R.color.red2);
        swipeBarang.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cekTopup();
                if(swipeBarang.isRefreshing()) {
                    swipeBarang.setRefreshing(false);
                }
            }
        });

        cekTopup();
    }

    private void cekTopup() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.DETIL_TRANS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");

                    String namabarang = data.getString("nama_barang");
                    String namaakun = data.getString("nama_akun");
                    String nominal = data.getString("jumlah_tawaran");
                    String rekening = data.getString("rekening_akun");
                    String bukti = data.getString("bukti_transfer");
                    String status = data.getString("status_transfer");
                    String alamat = data.getString("alamat_akun");
                    String notelp = data.getString("no_telp_akun");
                    idBarang = data.getString("id_barang");
                    theTransid = data.getString("id_transfer");

                    setdata(namabarang,namaakun,nominal,rekening,bukti,status,alamat,notelp);
                } catch (Exception e) {
                    Snackbar.make(detilView, e.toString(), Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TransferView.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", idTrans);

                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void setdata(String namabarang,String namaakun,String nominal,String rekening,String bukti,String status,String alamat,String notelp){
        TextView txtstatus = (TextView) findViewById(R.id.status);
        TextView namapelelang = (TextView) findViewById(R.id.namaPelelang);
        TextView alamatpelelang = (TextView) findViewById(R.id.alamatPelelang);
        TextView notelppelelang = (TextView) findViewById(R.id.notelpPelelang);
        TextView baranglelang = (TextView) findViewById(R.id.barangLelang);
        TextView norekpelelang = (TextView) findViewById(R.id.norek);
        TextView nom = (TextView) findViewById(R.id.nomTrans);
        ImageView gambarbukti = (ImageView) findViewById(R.id.imageUpload);
        Button upload = (Button) findViewById(R.id.upBukti);
        Button pilih = (Button) findViewById(R.id.pilihBukti);
        namapelelang.setText(namaakun);
        alamatpelelang.setText(alamat);
        notelppelelang.setText(notelp);
        baranglelang.setText(namabarang);
        norekpelelang.setText(rekening);
        nom.setText("Rp. "+nominal);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmInputBukti();
            }
        });
        pilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        if (status.equals("")){
            txtstatus.setText("Belum Upload Bukti Transfer");
        }else if (status.equals("kirim")){
            upload.setVisibility(View.GONE);
            pilih.setVisibility(View.GONE);
            txtstatus.setText("Tunggu Konfirmasi");
            Picasso.get().load(Url.ASSET_TRANSFER + bukti).into(gambarbukti);
        }else if (status.equals("terima")){
            upload.setVisibility(View.GONE);
            pilih.setVisibility(View.GONE);
            txtstatus.setTextColor(Color.parseColor("#3385ff"));
            txtstatus.setText("Sukses");
            Picasso.get().load(Url.ASSET_TRANSFER + bukti).into(gambarbukti);
        }else if (status.equals("gagal")){
            upload.setVisibility(View.GONE);
            pilih.setVisibility(View.GONE);
            txtstatus.setTextColor(Color.parseColor("#d10024"));
            txtstatus.setText("Gagal");
            Picasso.get().load(Url.ASSET_TRANSFER + bukti).into(gambarbukti);
        }

        //get Screen Dimensions
        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        //NOTE: If you want to square, just use one of these value.
        //set as half of dimens
        gambarbukti.getLayoutParams().width = width/5*4;
        gambarbukti.getLayoutParams().height = width/2;

    }

    //    fungsi untuk memilih gambar dari galery
    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
    }

    //    konversi gambar menjadi string
    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void confirmInputBukti() {
        if (validateBukti()) {
            sendBuktiTrans();
        }
    }
    private boolean validateBukti() {
        if (bitmap==null) {
            Toast.makeText(TransferView.this, "Foto bukti harus dipilih", Toast.LENGTH_LONG).show();
            return false;
        }else {
            return true;
        }
    }

    private void sendBuktiTrans() {
        progressDialog = new ProgressDialog(TransferView.this);
        progressDialog.setMessage("Proses");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.UP_TRANS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("msg");

                    progressDialog.dismiss();
                    if(status.equals("sukses")){
                        Toast.makeText(TransferView.this, "Upload Bukti Sukses", Toast.LENGTH_LONG).show();
                        cekTopup();
                    }
                    else{
                        Toast.makeText(TransferView.this, "Error", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Snackbar.make(detilView, e.toString(), Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TransferView.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("idtrans",theTransid);
                params.put("idbar",idBarang);
                params.put("foto", imageToString(bitmap));
                return params;
            }
        };

        queue.add(stringRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQUEST && resultCode == TransferView.this.RESULT_OK && data != null) {
//            mengambil alamat file gambar
            Uri path = data.getData();

            try {
                InputStream inputStream = TransferView.this.getContentResolver().openInputStream(path);
                mBitmapName = path.getPath();
                bitmap = BitmapFactory.decodeStream(inputStream);

                ImageView gambarbukti = (ImageView) findViewById(R.id.imageUpload);
                gambarbukti.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Toast.makeText(TransferView.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void pindahhome(View v){
        finish();
    }


    public void confirkecheckout(View v){
        Intent i = new Intent(TransferView.this, CheckoutActivity.class);
        startActivity(i);
    }

}
