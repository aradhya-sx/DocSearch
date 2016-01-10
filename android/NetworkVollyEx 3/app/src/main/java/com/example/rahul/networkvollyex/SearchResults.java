package com.example.rahul.networkvollyex;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchResults extends ActionBarActivity {
    ArrayList<String[]> myStringArray = new ArrayList<>();
    String location;
    String speciality;
    int start_page = -2;
    int result_perpage = 5;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Intent intent = getIntent();
        location = intent.getStringExtra("location");
        speciality = intent.getStringExtra("speciality");
        getDataFromElastic();
    }

    void getDataFromElastic() {

        pd = new ProgressDialog(SearchResults.this);
        pd.setMessage("loading");
        pd.show();
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "http://192.168.10.57:5000/elastic/";

        final MyArrayAdapter adapter = new MyArrayAdapter(this, myStringArray);
        ListView listViewItems = (ListView) findViewById(R.id.list1);
        listViewItems.setAdapter(adapter);
        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View view, int i, long l) {
                String values[] = myStringArray.get(i);
                doctorDetail(values[5]);
            }
        });
        listViewItems.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;
                if (lastInScreen == totalItemCount) {
                    start_page += result_perpage;
                    String url1 = url + start_page + "/" + location + "/" + speciality;
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url1,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        JSONArray jsonArray = new JSONObject("{\"doctors\" :" + response + "}")
                                                .optJSONArray("doctors");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            String doctorsData[] = {"imgurl",
                                                    "Doctor Name",
                                                    " Speciality",
                                                    "Location ",
                                                    "recom",
                                                    "id"
                                            };
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            String name = jsonObject.optString("name");
                                            String recommendations = jsonObject.optString("recommendations");
                                            String id = jsonObject.optString("id");
                                            doctorsData[1] = name;
                                            doctorsData[3] = location;
                                            doctorsData[2] = speciality;
                                            doctorsData[4] = recommendations;
                                            doctorsData[5] = id;
                                            myStringArray.add(doctorsData);
                                        }

                                        adapter.notifyDataSetChanged();
                                        pd.dismiss();
                                    } catch (Exception e) {
                                        Log.e("exception", e.toString());
                                        pd.dismiss();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("VolleyError", error.toString());
                            pd.dismiss();
                        }
                    });
                    queue.add(stringRequest);

                }

            }
        });


        start_page += result_perpage;
        String url1 = url + start_page + "/" + location + "/" + speciality;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONObject("{\"doctors\" :" + response + "}")
                                    .optJSONArray("doctors");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String doctorsData[] = {"imgurl",
                                        "Doctor Name",
                                        " Speciality",
                                        "Location ",
                                        "recom",
                                        "id"
                                };
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String name = jsonObject.optString("name").toString();
                                String recommendations = jsonObject.optString("recommendations");
                                String id = jsonObject.optString("id");
                                doctorsData[1] = name;
                                doctorsData[3] = location;
                                doctorsData[2] = speciality;
                                doctorsData[4] = recommendations;
                                doctorsData[5] = id;
                                myStringArray.add(doctorsData);
                            }
                            adapter.notifyDataSetChanged();
                            pd.dismiss();
                        } catch (Exception e) {
                            Log.e("exception", e.toString());
                            pd.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
                pd.dismiss();
            }
        });
        queue.add(stringRequest);
    }

    public void doctorDetail(String id) {

        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url = "http://192.168.10.57:5000/getDoctorDetails/";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent intent = new Intent(SearchResults.this, DoctorDetails.class);
                        intent.putExtra("response", response);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }
}