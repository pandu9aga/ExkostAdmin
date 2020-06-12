package com.example.exkostadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.SearchView;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.exkostadmin.Api.Url;
import com.example.exkostadmin.Model.ModelSearch;
import com.example.exkostadmin.adapter.AdapterSearch;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Search extends AppCompatActivity{

    public static String JARGON = "Cari";
    Toolbar toolbar;

    SessionManager sessionManager;
    RequestQueue queue;

    String key;

    SwipeRefreshLayout swipeSearch;
    List<ModelSearch> mItems;
    RecyclerView mRecyclerview;
    RecyclerView.LayoutManager mManager;
    RecyclerView.Adapter mAdapter;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(JARGON);

        queue = Volley.newRequestQueue(Search.this);

        mRecyclerview = (RecyclerView) findViewById(R.id.recSearch);
        pd = new ProgressDialog(Search.this);
        mItems = new ArrayList<>();
        mManager = new GridLayoutManager(Search.this,2);
        mRecyclerview.setLayoutManager(mManager);
        mAdapter = new AdapterSearch(Search.this,mItems);
        mRecyclerview.setAdapter(mAdapter);

        swipeSearch = findViewById(R.id.swipeSearch);
        swipeSearch.setColorSchemeResources(R.color.red, R.color.red2);
        swipeSearch.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                proses();
                if(swipeSearch.isRefreshing()) {
                    swipeSearch.setRefreshing(false);
                }
            }
        });

        handleIntent(getIntent());
    }

    private void loadSearch() {
        SessionManager sessionManager = new SessionManager(Search.this);
        final String id_akun = sessionManager.getIdAkun();

        pd.setMessage("Mengambil Data");
        pd.setCancelable(false);
        pd.show();
        mItems.clear();
        //mAdapter.notifyDataSetChanged();

        StringRequest reqData = new StringRequest(Request.Method.POST, Url.API_SEARCH,
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
                                    ModelSearch md = new ModelSearch();
                                    md.setIdBarang(data.getString("id_barang"));
                                    md.setNamaBarang(data.getString("nama_barang"));
                                    md.setNamaJenis(data.getString("nama_jenis_barang"));
                                    md.setHargaBarang(data.getString("harga_barang"));
                                    md.setWaktuLelang(data.getString("waktu_lelang"));
                                    md.setGambarBarang(data.getString("nama_gambar_barang"));
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
                params.put("keyword",key);
                return params;
            }
        };

        queue.add(reqData);
    }

    public void proses(){
        loadSearch();
        mItems.clear();
    }

    //searchmenu start
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchmenu, menu);

        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (TextUtils.isEmpty(s)) {
                    return false;
                } else {
                    key = s;
                    mItems.clear();
                    loadSearch();
                    return false;
                }
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return true;
            }
        });
        return true;
    }
//searchmenu end

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            // use the query to search the data somehow

            key = query;
            JARGON = query;
            loadSearch();

        }
    }

    public void goTo(View v) {
        Intent intent = new Intent(Search.this, BarangView.class);
        startActivity(intent);
    }

}