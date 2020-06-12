package com.example.exkostadmin;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.exkostadmin.Api.Url;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LogSecondFragment extends Fragment {

    View view;
    CardView secondButton;
    TextView secondFragment;
    TextInputLayout passwordr,namar;
    RelativeLayout fragViewr;

    SessionManager sessionManager;

    private RequestQueue queue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_fragment_two, container, false);

        queue = Volley.newRequestQueue(getActivity());
        sessionManager = new SessionManager(getActivity());

        secondFragment = (TextView) view.findViewById(R.id.secondFragment);
        secondFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if(activity instanceof LoginActivity){
                    LoginActivity myactivity = (LoginActivity) activity;
                    myactivity.loadFragment(new LogFirstFragment());
                }
            }
        });

        namar = view.findViewById(R.id.regisNama);
        passwordr = view.findViewById(R.id.regisPassword);

        fragViewr = view.findViewById(R.id.regisFrag);

        secondButton = (CardView) view.findViewById(R.id.buttonDaftar);
        secondButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                confirmInputRegister();
            }
        });

        return view;

    }
    private boolean validateNama() {
        String emailInput = namar.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            namar.setError("Nama harus diisi");
            return false;
        } else {
            namar.setError(null);
            namar.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validatePassword() {
        String emailInput = passwordr.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            passwordr.setError("Password harus diisi");
            return false;
        } else {
            passwordr.setError(null);
            passwordr.setErrorEnabled(false);
            return true;
        }
    }

    private void registerProcess() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.API_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if (status.equals("fail")) {
                        Snackbar.make(fragViewr, message, Snackbar.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Anda Berhasil Mendaftar", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    Snackbar.make(fragViewr, e.toString(), Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(fragViewr, error.toString(), Snackbar.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nama", namar.getEditText().getText().toString().trim());
                params.put("password", passwordr.getEditText().getText().toString().trim());
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void confirmInputRegister() {
        if (validatePassword() && validateNama()) {
            registerProcess();
        }
    }
}
