package com.example.exkostadmin;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
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
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuProfileFragment extends Fragment {

    private RequestQueue queue;
    View view;
    String iD;
    SessionManager sessionManager;
    ScrollView profilView;
    Button saveP;
    ProgressDialog progressDialog;

    public MenuProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.menu_fragment_profile, container, false);
        profilView = view.findViewById(R.id.viewProfil);

        queue = Volley.newRequestQueue(getActivity());
        sessionManager = new SessionManager(getActivity());

        viewProfile();

        saveP = view.findViewById(R.id.saveProfil);
        saveP.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Profile");

    }
    private void viewProfile() {
        iD = sessionManager.getIdAkun();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.API_PROFIL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");

                    String nama = data.getString("nama_admin");
                    String password = data.getString("pass_admin");

                    setdata(nama,password);
                } catch (Exception e) {
                    Snackbar.make(profilView, e.toString(), Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(profilView, error.toString(), Snackbar.LENGTH_LONG).show();
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

    public void setdata(String nama, String password){
        TextInputEditText namav = (TextInputEditText) view.findViewById(R.id.namaText);
        TextInputEditText passwordv = (TextInputEditText) view.findViewById(R.id.passwordText);
        namav.setText(nama);
        passwordv.setText(password);
    }

    private void updateProfile() {
        iD = sessionManager.getIdAkun();
        final TextInputEditText namav = (TextInputEditText) view.findViewById(R.id.namaText);
        final TextInputEditText passwordv = (TextInputEditText) view.findViewById(R.id.passwordText);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Proses");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.UPDATE_PROFIL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("stat");

                    progressDialog.dismiss();
                    if(status.equals("true")){
                        //Intent intent = getActivity().getIntent();
                        //getActivity().finish();
                        //getActivity().startActivity(intent);
                        Toast.makeText(getActivity(), "Update Sukses", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Snackbar.make(profilView, e.toString(), Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", iD);
                params.put("nama", namav.getText().toString().trim());
                params.put("password", passwordv.getText().toString().trim());
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void reLoadFragment()
    {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }
}
