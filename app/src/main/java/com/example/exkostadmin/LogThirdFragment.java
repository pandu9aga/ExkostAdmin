package com.example.exkostadmin;

import android.app.Activity;
import android.app.Fragment;
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
//import com.android.volley.DefaultRetryPolicy;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
//import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.exkostadmin.Api.Url;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.http.params.HttpConnectionParams;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.DefaultRetryPolicy.DEFAULT_TIMEOUT_MS;

public class LogThirdFragment extends Fragment {

    View view;
    CardView thirdButton;
    TextView thirdFragment;
    TextInputLayout email;
    RelativeLayout fragView;

    SessionManager sessionManager;
    private RequestQueue queue;

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.login_fragment_three, container, false);

        queue = Volley.newRequestQueue(getActivity());
        sessionManager = new SessionManager(getActivity());

        if (sessionManager.isLogin() == true){
            Intent main = new Intent(getActivity(), HomeActivity.class);
            startActivity(main);
        }

        thirdFragment = (TextView) view.findViewById(R.id.firstFragment);
        thirdFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if(activity instanceof LoginActivity){
                    LoginActivity myactivity = (LoginActivity) activity;
                    myactivity.loadFragment(new LogFirstFragment());
                }
            }
        });

        email = view.findViewById(R.id.lupapassEmail);

        fragView = view.findViewById(R.id.lupapassFrag);

        thirdButton = (CardView) view.findViewById(R.id.buttonLupaPass);
        thirdButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                confirmInputLupapass();
            }
        });

        return view;
    }

    private boolean validateEmail() {
        String emailInput = email.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            email.setError("Email harus diisi");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private void lupapassProcess() {
        //RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url.LUPA_PASS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("msg");

                    if (message.equals("failedsend")) {
                        Snackbar.make(fragView, "Gagal mengirim kode verifikasi ke email anda", Snackbar.LENGTH_LONG).show();
                    }else if(message.equals("noemail")){
                        Snackbar.make(fragView, "Email tidak terdaftar", Snackbar.LENGTH_LONG).show();
                    }else {
                        String emails = message;

                        Bundle bundleobj = new Bundle();
                        bundleobj.putString("email", emails);

                        ResetpassFragment fragobj = new ResetpassFragment();
                        fragobj.setArguments(bundleobj);
                        mFragmentTransaction.addToBackStack(null);
                        mFragmentTransaction.replace(R.id.frameLayout, fragobj).commit();
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
                params.put("email", email.getEditText().getText().toString().trim());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    public void confirmInputLupapass() {
        if (validateEmail()) {
            lupapassProcess();
        }
    }

}
