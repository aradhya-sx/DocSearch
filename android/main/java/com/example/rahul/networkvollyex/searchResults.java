package com.example.rahul.networkvollyex;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class searchResults extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Intent intent = getIntent();
        String location = intent.getStringExtra("location");
        String speciality=intent.getStringExtra("speciality");

    //jSonParsing();
    getDataFromUrl();
    }

    void getDataFromUrl()
    {
        final TextView mTextView = (TextView) findViewById(R.id.textTest11);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.11.7:5000";
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        mTextView.setText("Response is: " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work!");
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    void jSonParsing()
    {


       JSONObject student1 = new JSONObject();
        try {
            student1.put("id", "3");
            student1.put("name", "NAME OF STUDENT");
            student1.put("year", "3rd");
            student1.put("curriculum", "Arts");
            student1.put("birthday", "5/5/1993");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JSONObject student2 = new JSONObject();
        try {
            student2.put("id", "2");
            student2.put("name", "NAME OF STUDENT2");
            student2.put("year", "4rd");
            student2.put("curriculum", "scicence");
            student2.put("birthday", "5/5/1993");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {

        JSONArray jsonArray1 = new JSONArray();

        jsonArray1.put(student1);
        jsonArray1.put(student2);

        JSONObject studentsObj = new JSONObject();
        studentsObj.put("Students", jsonArray1);


        String strJson="{\"Employee\" :[{\"id\":\"01\",\"name\":\"Gopal Varma\",\"salary\":\"500000\"},{\"id\":\"02\",\"name\":\"Sairamkrishna\",\"salary\":\"500000\"}, {\"id\":\"03\",\"name\":\"Sathish kallakuri\",\"salary\":\"600000\"} ]}";
        String data = "";

            JSONObject jsonRootObject = new JSONObject(strJson);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.optJSONArray("Employee");

            //Iterate the jsonArray and print the info of JSONObjects
            for(int i=0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int id = Integer.parseInt(jsonObject.optString("id").toString());
                String name = jsonObject.optString("name").toString();
                float salary = Float.parseFloat(jsonObject.optString("salary").toString());

                data += "\n Node"+i+" : \n id= "+ id +" \n Name= "+ name +" \n Salary= "+ salary +" \n ";
            }
            System.out.println("Data is :---------------" + data);
            Log.e("Data is :--------------", data);
            TextView output = (TextView) findViewById(R.id.textTest11);
           // textView.setText(data);
            output.setText("CHAL RAHA HAI ABA TAKA TO......."+data);
        } catch (JSONException e) {e.printStackTrace();}

    }
void getResults(String location,String speciality)
{
    RequestQueue queue = Volley.newRequestQueue(this);
    String url ="http://www.google.com";

// Request a string response from the provided URL.
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Display the first 500 characters of the response string.
                   // mTextView.setText("Response is: "+ response.substring(0,500));
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
           // mTextView.setText("That didn't work!");
        }
    });
// Add the request to the RequestQueue.
    queue.add(stringRequest);
}
}