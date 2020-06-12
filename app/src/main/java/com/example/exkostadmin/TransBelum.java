package com.example.exkostadmin;

import android.app.ProgressDialog;
import android.os.Bundle;

//import android.app.Fragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.example.exkostadmin.Model.ModelTransfer;
import com.example.exkostadmin.adapter.AdapterTransfer;

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
public class TransBelum extends Fragment {

    View view;
    private SwipeRefreshLayout swipeLb;
    SessionManager sessionManager;
    private RequestQueue queue;

    List<ModelTransfer> mItems;
    RecyclerView mRecyclerview;
    RecyclerView.LayoutManager mManager;
    RecyclerView.Adapter mAdapter;
    ProgressDialog pd;

    public TransBelum() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.transfer_layout, container, false);

        queue = Volley.newRequestQueue(getActivity());

        swipeLb = view.findViewById(R.id.swipeL);
        swipeLb.setColorSchemeResources(R.color.red, R.color.red2);
        swipeLb.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload();
                if(swipeLb.isRefreshing()) {
                    swipeLb.setRefreshing(false);
                }
            }
        });

        reload();

        return view;
    }

    public void reload(){
        mRecyclerview = (RecyclerView) view.findViewById(R.id.recL);
        pd = new ProgressDialog(getActivity());
        mItems = new ArrayList<>();

        loadTb();

        mManager = new GridLayoutManager(getActivity(),2);
        mRecyclerview.setLayoutManager(mManager);
        mAdapter = new AdapterTransfer(getActivity(),mItems);
        mRecyclerview.setAdapter(mAdapter);
    }

    private void loadTb() {
        SessionManager sessionManager = new SessionManager(getActivity());
        mItems.clear();
        pd.setMessage("Mengambil Data");
        pd.setCancelable(false);
        //pd.show();

        StringRequest reqData = new StringRequest(Request.Method.POST, Url.API_TRANS,
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
                                    ModelTransfer md = new ModelTransfer();
                                    md.setIdPemenang(data.getString("id_pemenang"));
                                    md.setNamaBarang(data.getString("nama_barang"));
                                    md.setNamaAkun(data.getString("nama_akun"));
                                    md.setNominal(data.getString("jumlah_tawaran"));
                                    md.setRekening(data.getString("rekening_akun"));
                                    md.setGambarBukti(data.getString("bukti_transfer"));
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
                        //pd.cancel();
                        Log.d("volley", "error : " + error.getMessage());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("key", "");
                return params;
            }
        };

        queue.add(reqData);
    }



}