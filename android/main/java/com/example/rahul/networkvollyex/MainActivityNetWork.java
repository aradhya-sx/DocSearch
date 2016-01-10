package com.example.rahul.networkvollyex;

import android.app.DownloadManager;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class MainActivityNetWork extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_net_work);
        //final TextView mTextView = (TextView) findViewById(R.id.autocomplete_specialitytxt);
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autocomplete_specialitytxt);
// Get the string array
        String[] specialisation = getResources().getStringArray(R.array.specialisation_array);
// Create the adapter and set it to the AutoCompleteTextView
        for(int i=0;i<specialisation.length;i++)
        {
            Log.e("111",specialisation[i]);
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, specialisation);
        textView.setAdapter(adapter);
    }

    public void search(View view) {

        Intent intent = new Intent(this, searchResults.class);
        EditText editlocation = (EditText) findViewById(R.id.locationtxt);
        String location = editlocation.getText().toString();
        EditText editspeciality = (EditText) findViewById(R.id.autocomplete_specialitytxt);
        String speciality = editspeciality.getText().toString();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).
                setContentTitle("My notification").setContentText("Hello World!");
        intent.putExtra("location", location);
        intent.putExtra("speciality", speciality);
        startActivity(intent);
    }

    void networkcall() {
        final TextView mTextView = (TextView) findViewById(R.id.tview1);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://www.google.com";
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        mTextView.setText("Response is: " + response.substring(0, 500));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity_net_work, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
