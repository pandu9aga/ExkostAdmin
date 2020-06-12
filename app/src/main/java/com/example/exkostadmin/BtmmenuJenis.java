package com.example.exkostadmin;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.exkostadmin.Api.Url;
import com.example.exkostadmin.Model.ModelCart;
import com.example.exkostadmin.adapter.AdapterCart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class BtmmenuJenis extends Fragment {

    View view;
    private SwipeRefreshLayout swipeCart;
    SessionManager sessionManager;
    private RequestQueue queue;

    List<ModelCart> mItems;
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
        view = inflater.inflate(R.layout.content_cart, container, false);

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
        mAdapter = new AdapterCart(getActivity(),mItems);
        mRecyclerview.setAdapter(mAdapter);
    }

    private void loadCart() {
        SessionManager sessionManager = new SessionManager(getActivity());
        final String id_akun = sessionManager.getIdAkun();

        pd.setMessage("Mengambil Data");
        pd.setCancelable(false);
        pd.show();

        StringRequest reqData = new StringRequest(Request.Method.POST, Url.API_CART,
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
                                    ModelCart md = new ModelCart();
                                    md.setIdBarang(data.getString("id_barang"));
                                    md.setNamaBarang(data.getString("nama_barang"));
                                    md.setNamaJenis(data.getString("nama_jenis_barang"));
                                    md.setHargaBarang(data.getString("harga_barang"));
                                    md.setWaktuLelang(data.getString("waktu_lelang"));
                                    md.setGambarBarang(data.getString("nama_gambar_barang"));
                                    md.setHighBid(data.getString("high"));
                                    md.setMyBid(data.getString("jumlah_tawaran"));
                                    md.setStatBid(data.getString("status_lelang"));
                                    md.setStatTrans(data.getString("status_transfer"));
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
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id_akun);
                return params;
            }
        };

        queue.add(reqData);
    }

}