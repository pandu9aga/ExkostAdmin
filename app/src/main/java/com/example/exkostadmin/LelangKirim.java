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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.exkostadmin.Api.Url;
import com.example.exkostadmin.Model.ModelLelang;
import com.example.exkostadmin.adapter.AdapterLelang;

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
public class LelangKirim extends Fragment {

    View view;
    Spinner splelangk;
    private SwipeRefreshLayout swipeLk;
    SessionManager sessionManager;
    private RequestQueue queue;

    List<ModelLelang> mItems;
    RecyclerView mRecyclerview;
    RecyclerView.LayoutManager mManager;
    RecyclerView.Adapter mAdapter;
    ProgressDialog pd;

    String[] kategori = {
            "semua",
            "perlu dikirim",
            "sedang dikirim"
    };

    String key;

    public LelangKirim() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.lelang_layout, container, false);

        queue = Volley.newRequestQueue(getActivity());

        swipeLk = view.findViewById(R.id.swipeL);
        swipeLk.setColorSchemeResources(R.color.red, R.color.red2);
        swipeLk.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload();
                if(swipeLk.isRefreshing()) {
                    swipeLk.setRefreshing(false);
                }
            }
        });

        reload();

        splelangk = (Spinner) view.findViewById(R.id.spLelang);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, kategori);
        splelangk.setAdapter(adapter);
        splelangk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                key = splelangk.getItemAtPosition(i).toString();
                //Toast.makeText(getActivity(), ""+ adapter.getItem(i), Toast.LENGTH_SHORT).show();
                //Toast.makeText(getActivity(), ""+ key, Toast.LENGTH_SHORT).show();
                reload();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        return view;
    }

    public void reload(){
        mRecyclerview = (RecyclerView) view.findViewById(R.id.recL);
        pd = new ProgressDialog(getActivity());
        mItems = new ArrayList<>();

        loadLb();

        mManager = new GridLayoutManager(getActivity(),2);
        mRecyclerview.setLayoutManager(mManager);
        mAdapter = new AdapterLelang(getActivity(),mItems);
        mRecyclerview.setAdapter(mAdapter);
    }

    private void loadLb() {
        SessionManager sessionManager = new SessionManager(getActivity());
        final String id_akun = sessionManager.getIdAkun();

        pd.setMessage("Mengambil Data");
        pd.setCancelable(false);
        //pd.show();

        StringRequest reqData = new StringRequest(Request.Method.POST, Url.AUC_KIRIM,
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
                                    ModelLelang md = new ModelLelang();
                                    md.setIdBarang(data.getString("id_barang"));
                                    md.setNamaBarang(data.getString("nama_barang"));
                                    md.setNamaJenis(data.getString("nama_jenis_barang"));
                                    md.setHargaBarang(data.getString("harga_barang"));
                                    md.setWaktuLelang(data.getString("waktu_lelang"));
                                    md.setGambarBarang(data.getString("nama_gambar_barang"));
                                    md.setStatBid(data.getString("status_lelang"));
                                    md.setStatTrans(data.getString("status_transfer"));
                                    md.setStatFail(data.getString("status_gagal"));
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
                if (key.equals("semua")){
                    params.put("keyword", "semua");
                }else if (key.equals("perlu dikirim")){
                    params.put("keyword", "selesai");
                }else if(key.equals("sedang dikirim")){
                    params.put("keyword", "kirim");
                }
                params.put("id", id_akun);
                return params;
            }
        };

        queue.add(reqData);
    }

}