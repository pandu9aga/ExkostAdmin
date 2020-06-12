package com.example.exkostadmin;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Tambah extends AppCompatActivity{

    ImageView gambarv;

    private RequestQueue queue;
    SessionManager sessionManager;

    TextInputLayout namal,hargal,waktul,jaml,catatanl;
    TextInputEditText nama,harga,waktu,jam,catatan;
    Button gambar,upload;
    ImageButton picktime;
    String iD;

    ScrollView tambahView;

    //String tanggal,bulan,tahun,jam,menit;

    private int mYear, mMonth, mDay, mHour, mMinute;

    String kategori;
    Spinner spupload;

    private final int IMG_REQUEST = 1;
    private String mBitmapName;
    Bitmap bitmap;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_barang);

        queue = Volley.newRequestQueue(Tambah.this);
        sessionManager = new SessionManager(Tambah.this);

        tambahView = findViewById(R.id.parent_scroll);

        namal = findViewById(R.id.namaBarang);
        hargal = findViewById(R.id.hargaBarang);
        waktul = findViewById(R.id.waktuLelang);
        jaml = findViewById(R.id.jamLelang);
        catatanl = findViewById(R.id.catatan);

        nama = findViewById(R.id.namabarangText);
        harga = findViewById(R.id.hargabarangText);
        waktu = findViewById(R.id.waktulelangText);
        jam = findViewById(R.id.jamlelangText);
        catatan = findViewById(R.id.catatanText);
        gambar = findViewById(R.id.uploadButton);
        upload = findViewById(R.id.tambahBarang);
        picktime = findViewById(R.id.pickDate);


        spupload = (Spinner) findViewById(R.id.spUpload);
        addItemsOnSpinner();

        gambarv = findViewById(R.id.imageUpload);
        //get Screen Dimensions
        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        //NOTE: If you want to square, just use one of these value.
        //set as half of dimens
        gambarv.getLayoutParams().width = width/5*4;
        gambarv.getLayoutParams().height = width/2;

        picktime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Tambah.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                //tanggal = Integer.toString(dayOfMonth);
                                //bulan = Integer.toString(monthOfYear+1);
                                //tahun = Integer.toString(year);
                                waktu.setText(date);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

                c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(Tambah.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                             String time = hourOfDay + ":" + minute;
                             //jam = Integer.toString(hourOfDay);
                             //menit = Integer.toString(minute);
                             jam.setText(time);
                            }
                        }, mHour, mMinute, true);
                timePickerDialog.show();
            }
        });

        gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmInputUpload();
            }
        });

    }

    ArrayList<String> list;

    public void addItemsOnSpinner() {
        list = new ArrayList<String>();
        //list.add("semua");

        StringRequest reqData = new StringRequest(Request.Method.GET, Url.API_JENIS,
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
                                    list.add(data.getString("nama_jenis_barang"));
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Tambah.this, android.R.layout.simple_spinner_dropdown_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spupload.setAdapter(adapter);
        spupload.setSelection(0);
        spupload.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                kategori = spupload.getItemAtPosition(i).toString();
                //Toast.makeText(getActivity(), ""+ kategori, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadBarang() {
        iD = sessionManager.getIdAkun();
        final TextInputEditText namab = (TextInputEditText) findViewById(R.id.namabarangText);
        final TextInputEditText hargab = (TextInputEditText) findViewById(R.id.hargabarangText);
        final TextInputEditText catatanb = (TextInputEditText) findViewById(R.id.catatanText);
        final TextInputEditText tanggalb = (TextInputEditText) findViewById(R.id.waktulelangText);
        final TextInputEditText jamb = (TextInputEditText) findViewById(R.id.jamlelangText);
        progressDialog = new ProgressDialog(Tambah.this);
        progressDialog.setMessage("Proses");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.ADD_BARANG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("stat");
                    String id = jsonObject.getString("id_barang");

                    progressDialog.dismiss();
                    if(status.equals("true")){
                        Intent intent = new Intent(Tambah.this, TransferView.class);
                        intent.putExtra("id_barang",id);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(Tambah.this, "Error", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Snackbar.make(tambahView, e.toString(), Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Tambah.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_pelelang", iD);
                params.put("nama_barang", namab.getText().toString().trim());
                params.put("harga_barang", hargab.getText().toString().trim());
                params.put("tanggal_lelang", tanggalb.getText().toString().trim());
                params.put("jam_lelang", jamb.getText().toString().trim());
                params.put("catatan", catatanb.getText().toString().trim());
                params.put("jenis_barang", kategori);
                params.put("status", "berlangsung");
                params.put("foto", imageToString(bitmap));

                return params;
            }
        };

        queue.add(stringRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQUEST && resultCode == Tambah.this.RESULT_OK && data != null) {
//            mengambil alamat file gambar
            Uri path = data.getData();

            try {
                InputStream inputStream = Tambah.this.getContentResolver().openInputStream(path);
                mBitmapName = path.getPath();
                bitmap = BitmapFactory.decodeStream(inputStream);

                ImageView imgup = (ImageView) findViewById(R.id.imageUpload);
                imgup.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Toast.makeText(Tambah.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void backHome(View v) {
        finish();
    }

    private boolean validateNama() {
        String namaInput = namal.getEditText().getText().toString().trim();

        if (namaInput.isEmpty()) {
            namal.setError("Nama harus diisi");
            return false;
        } else {
            namal.setError(null);
            namal.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateHarga() {
        String hargaInput = hargal.getEditText().getText().toString().trim();

        if (hargaInput.isEmpty()) {
            hargal.setError("Nama harus diisi");
            return false;
        } else {
            hargal.setError(null);
            hargal.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateCatatan() {
        String catatanInput = catatanl.getEditText().getText().toString().trim();

        if (catatanInput.isEmpty()) {
            catatanl.setError("Nama harus diisi");
            return false;
        } else {
            catatanl.setError(null);
            catatanl.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateWaktu() {
        String waktuInput = waktul.getEditText().getText().toString().trim();

        if (waktuInput.isEmpty()) {
            waktul.setError("Nama harus diisi");
            return false;
        } else {
            waktul.setError(null);
            waktul.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateJam() {
        String jamInput = jaml.getEditText().getText().toString().trim();

        if (jamInput.isEmpty()) {
            jaml.setError("Nama harus diisi");
            return false;
        } else {
            jaml.setError(null);
            jaml.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateFoto() {
        if (bitmap==null) {
            Toast.makeText(Tambah.this, "Gambar harus dipilih", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }
    public void confirmInputUpload() {
        if (validateNama() && validateHarga() && validateCatatan() && validateWaktu() && validateJam() && validateFoto()) {
            uploadBarang();
        }
    }
}
