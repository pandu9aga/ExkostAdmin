package com.example.exkostadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.exkostadmin.Api.Url;
import com.example.exkostadmin.Model.ModelJenis;
import com.example.exkostadmin.adapter.AdapterJenis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BtmmenuJenis extends Fragment {

    View view;
    private SwipeRefreshLayout swipeCart;
    SessionManager sessionManager;
    private RequestQueue queue;
    Button addJenis;

    List<ModelJenis> mItems;
    RecyclerView mRecyclerview;
    RecyclerView.LayoutManager mManager;
    RecyclerView.Adapter mAdapter;
    ProgressDialog pd;

    public BtmmenuJenis() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.content_jenis, container, false);

        queue = Volley.newRequestQueue(getActivity());

        swipeCart = view.findViewById(R.id.swipeCart);

        swipeCart.setColorSchemeResources(R.color.red, R.color.red2);

        swipeCart.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload();
                if(swipeCart.isRefreshing()) {
                    swipeCart.setRefreshing(false);
                }
            }
        });

        addJenis = view.findViewById(R.id.addJenis);
        addJenis.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent add = new Intent(getActivity(), JenisCreate.class);
                getActivity().startActivity(add);
            }
        });

        reload();

        return view;
    }

    public void reload(){
        mRecyclerview = (RecyclerView) view.findViewById(R.id.recCart);
        pd = new ProgressDialog(getActivity());
        mItems = new ArrayList<>();

        loadCart();

        mManager = new LinearLayoutManager(getActivity());
        mRecyclerview.setLayoutManager(mManager);
        mAdapter = new AdapterJenis(getActivity(),mItems);
        mRecyclerview.setAdapter(mAdapter);
    }

    private void loadCart() {
        SessionManager sessionManager = new SessionManager(getActivity());
        final String id_akun = sessionManager.getIdAkun();

        pd.setMessage("Mengambil Data");
        pd.setCancelable(true);
        pd.show();

        StringRequest reqData = new StringRequest(Request.Method.GET, Url.API_JENIS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.cancel();
                        Log.d("volley","response : " + response.toString());
                        try {
                            JSONArray j = new JSONArray(response);
                            for(int i = 0 ; i < response.length(); i++)
                            {
                                try {
                                    JSONObject data = j.getJSONObject(i);
                                    ModelJenis md = new ModelJenis();
                                    md.setIdJenis(data.getString("id_jenis_barang"));
                                    md.setNamaJenis(data.getString("nama_jenis_barang"));
                                    mItems.add(md);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            mAdapter.notifyDataSetChanged();
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        Log.d("volley", "error : " + error.getMessage());
                    }
                });

        queue.add(reqData);
    }

}