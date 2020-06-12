package com.example.exkostadmin;

import android.app.Activity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.exkostadmin.Api.Url;
import com.example.exkostadmin.SessionManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetpassFragment extends Fragment {

    View view;
    CardView firstButton;
    TextView firstFragment,email;
    TextInputLayout password,kode;
    RelativeLayout fragView;

    String theEmail;

    SessionManager sessionManager;

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    private RequestQueue queue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.reset_password, container, false);

        queue = Volley.newRequestQueue(getActivity());
        sessionManager = new SessionManager(getActivity());

        if (sessionManager.isLogin() == true){
            Intent main = new Intent(getActivity(), HomeActivity.class);
            startActivity(main);
        }

        firstFragment = (TextView) view.findViewById(R.id.firstFragment);
        firstFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if(activity instanceof LoginActivity){
                    LoginActivity myactivity = (LoginActivity) activity;
                    myactivity.loadFragment(new LogFirstFragment());
                }
            }
        });

        kode = view.findViewById(R.id.resetpassRegiskey);
        password = view.findViewById(R.id.resetpassPassword);
        email = view.findViewById(R.id.email);

        Bundle bundle =getArguments();
        if(null!=bundle) {
            theEmail=bundle.getString("email");
        }
        email.setText(theEmail);

        fragView = view.findViewById(R.id.resetpassFrag);

        firstButton = (CardView) view.findViewById(R.id.buttonResetPass);
        firstButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                confirmInputReset();
            }
        });

        return view;
    }

    private boolean validateKode() {
        String kodeInput = kode.getEditText().getText().toString().trim();

        if (kodeInput.isEmpty()) {
            kode.setError("Kode harus diisi");
            return false;
        } else {
            kode.setError(null);
            kode.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = password.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            password.setError("Password harus diisi");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    private void resetProcess() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.RESET_PASS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("reset");

                    if (message.equals("keyfalse")) {
                        Snackbar.make(fragView, "Kode reset salah", Snackbar.LENGTH_LONG).show();
                    }else {
                        String reset = message;

                        Intent main = new Intent(getActivity(), LoginActivity.class);
                        main.putExtra("reset", reset);
                        startActivity(main);
                    }
                } catch (Exception e) {
                    Snackbar.make(fragView, e.toString(), Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(fragView, error.toString(), Snackbar.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<String, String>();
                params.put("reset_key", kode.getEditText().getText().toString().trim());
                params.put("password", password.getEditText().getText().toString().trim());
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void confirmInputReset() {
        if (validateKode() && validatePassword()) {
            resetProcess();
        }
    }

}
