package com.basudev.onlinewallpaper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basudev.onlinewallpaper.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageAdapter wallpaperAdapter;
    BottomNavigationView bottomNavigationView;
    List<ImageModel> wallpaperModelList;
    int pageNumber = 1;

    Boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.imagerecycler);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.Home);
        wallpaperModelList = new ArrayList<>();
        wallpaperAdapter = new ImageAdapter(this, wallpaperModelList);

        recyclerView.setAdapter(wallpaperAdapter);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = gridLayoutManager.getChildCount();
                totalItems = gridLayoutManager.getItemCount();
                scrollOutItems = gridLayoutManager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false;
                    fetchWallpaper();
                }


            }
        });


        fetchWallpaper();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.Suggestions:
                        startActivity(new Intent(getApplicationContext(),ImageActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });
    }

    public void fetchWallpaper() {

        StringRequest request = new StringRequest(Request.Method.GET, "https://api.pexels.com/v1/search/?page="+pageNumber+"&per_page=80&query=editors+choice",
                new Response.Listener<String>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray jsonArray = jsonObject.getJSONArray("photos");

                            int length = jsonArray.length();

                            for (int i = 0; i < length; i++) {

                                JSONObject object = jsonArray.getJSONObject(i);

                                int id = object.getInt("id");

                                JSONObject objectImages = object.getJSONObject("src");

                                String orignalUrl = objectImages.getString("original");
                                String mediumUrl = objectImages.getString("medium");

                                ImageModel wallpaperModel = new ImageModel(id, orignalUrl, mediumUrl);
                                wallpaperModelList.add(wallpaperModel);
                            }
                            wallpaperAdapter.notifyDataSetChanged();
                            pageNumber++;

                        } catch (JSONException e) {
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "563492ad6f91700001000001f4889bbd173d4db0b4714f1ad160e881");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);

    }

}

//    private void getManageData() {
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url = "https://api.pexels.com/v1/search/?page=1&per_page=16&";
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.pexels.com/v1/search/?page="+pageNumber+"&per_page=80&query=editors+choice",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//
//                            JSONObject object = new JSONObject(response);
//                            JSONArray array = object.getJSONArray("photos");
//                            for (int i = 0; i < array.length(); i++) {
//                                JSONObject object1 = array.getJSONObject(i);
//                                JSONObject img = object1.getJSONObject("src");
//                                String url = img.getString("original");
//                                iModel.add(url);
//                                ImageModels.add(new ImageModel(img.getString("original")));
////                                ImageModels.add(new ImageModel(object1.getString("url")));
//                            }
//                            adapter.notifyDataSetChanged();
//                            pageNumber++;
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
////                        layoutManager = new LinearLayoutManager(getApplicationContext());
//                        layoutManager = new GridLayoutManager(MainActivity.this,2);
//                        layoutManager.setOrientation(RecyclerView.VERTICAL);
//                        binding.imagerecycler.setLayoutManager(layoutManager);
//
//                        binding.imagerecycler.setAdapter(adapter);
//                        adapter.OnRecyclerViewClickListener(new ImageAdapter.OnRecyclerViewClickListener() {
//                            @Override
//                            public void OnItemClick(int position) {
//                                Intent intent= new Intent(MainActivity.this,FullscreenImage.class);
//                                intent.putExtra("url",iModel.get(position));
//                                startActivity(intent);
//                            }
//                        });
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
//            }
//        }){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Authorization", "563492ad6f91700001000001f4889bbd173d4db0b4714f1ad160e881");
//                return params;
//            }
//        };
//        queue.add(stringRequest);
//    }
