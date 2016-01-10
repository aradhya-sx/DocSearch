package com.example.rahul.networkvollyex;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivityNetWork extends ActionBarActivity {

    ArrayList<String[]> specialisationlocation;
    String[] location;
    String[] spec;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapterlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_net_work);
        specialisationlocation = new ArrayList<String[]>();
        getSpecialisationLocation();
    }


    public void getSpecialisationLocation() {
        JsonObjectRequest jsrequest;
        try {
            final RequestQueue queue = Volley.newRequestQueue(this);
            final String url = "http://192.168.10.57:5000/allLocalitySpeciality/";
            Log.e("error", "error");
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                System.out.println(response);
                                JSONObject jobj = new JSONObject(response);
                                List<String> locations = new ArrayList<String>();
                                JSONArray jsonArray = new JSONObject(response).optJSONArray("data");
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                JSONArray jsonArraylocation = jsonObject.getJSONArray("clinicList");
                                Log.e("len", "" + jsonArraylocation.length());
                                for (int i = 0; i < jsonArraylocation.length(); i++) {
                                    JSONObject jsonObjectclinic = jsonArraylocation.getJSONObject(i);
                                    String locationName = jsonObjectclinic.getString("area");
                                    Log.e("name", locationName);
                                    locations.add(locationName);

                                }
                                List<String> specialisation = new ArrayList<String>();
                                JSONArray jsonArrayspecialisation = jsonObject.getJSONArray("specList");
                                Log.e("len", "" + jsonArrayspecialisation.length());
                                for (int i = 0; i < jsonArrayspecialisation.length(); i++) {
                                    JSONObject jsonObjectspecilisation = jsonArrayspecialisation.getJSONObject(i);
                                    String specialisationName = jsonObjectspecilisation.getString("name");
                                    Log.e("name", specialisationName);
                                    specialisation.add(specialisationName);

                                }

                                location = new String[locations.size()];
                                for (int i = 0; i < locations.size(); i++) {
                                    location[i] = new String(locations.get(i));
                                }
                                specialisationlocation.add(location);
                                spec = new String[specialisation.size()];
                                for (int i = 0; i < specialisation.size(); i++) {
                                    spec[i] = new String(specialisation.get(i));
                                }
                                AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(
                                        R.id.autocomplete_specialitytxt);
                                adapter = new ArrayAdapter<String>(MainActivityNetWork.this,
                                        android.R.layout.simple_list_item_1, spec);
                                textView.setAdapter(adapter);

                                AutoCompleteTextView textViewloc = (AutoCompleteTextView) findViewById(
                                        R.id.autocomplete_locationtxt);

                                adapterlocation =
                                        new ArrayAdapter<String>(MainActivityNetWork.this,
                                                android.R.layout.simple_list_item_1, location);
                                textViewloc.setAdapter(adapterlocation);
                            } catch (Exception e) {
                                Log.e("exception", e.toString());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("error");
                    Log.e("VolleyError", error.toString());
                }
            });

            queue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.print("escaped");
    }

    public void search(View view) {

        Intent intent = new Intent(this, SearchResults.class);
        EditText editlocation = (EditText) findViewById(R.id.autocomplete_locationtxt);
        String location = editlocation.getText().toString();
        EditText editspeciality = (EditText) findViewById(R.id.autocomplete_specialitytxt);
        String speciality = editspeciality.getText().toString();
        intent.putExtra("location", location);
        intent.putExtra("speciality", speciality);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity_net_work, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
