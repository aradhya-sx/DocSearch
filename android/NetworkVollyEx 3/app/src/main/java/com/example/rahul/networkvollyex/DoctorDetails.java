package com.example.rahul.networkvollyex;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class DoctorDetails extends ActionBarActivity {
    ArrayList<String[]> clinicArrayList = new ArrayList<>();
    ArrayList<String[]> specialityArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_details);
        Intent intent = getIntent();
        String message = intent.getStringExtra("response");
        final ClinicAdopter clinic_adapter = new ClinicAdopter(this, clinicArrayList);
        ListView cilic_listViewItems = (ListView) findViewById(R.id.clinicList);
        cilic_listViewItems.setAdapter(clinic_adapter);
        final SpecialityAdoptor speciality_adapter = new SpecialityAdoptor(this, specialityArrayList);
        ListView speciality_listViewItems = (ListView) findViewById(R.id.specialityList);
        speciality_listViewItems.setAdapter(speciality_adapter);
        try {
            JSONArray jsonArray = new JSONObject(message).optJSONArray("data");
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String name = jsonObject.optString("name").toString();
            String email = jsonObject.optString("email").toString();
            String experience = jsonObject.optString("experience").toString();
            String recommendations = jsonObject.optString("recommendations").toString();
            String education = jsonObject.optString("education").toString();
            TextView nameview = (TextView) findViewById(R.id.doctorName2);
            nameview.setText(name);
            TextView emailview = (TextView) findViewById(R.id.doctorEmail1);
            emailview.setText(email);
            TextView exprienceview = (TextView) findViewById(R.id.doctorExperience1);
            exprienceview.setText(experience);
            TextView recommendationsview = (TextView) findViewById(R.id.doctor_recommendation);
            recommendationsview.setText(recommendations);
            TextView educationview = (TextView) findViewById(R.id.doctoreducation);
            educationview.setText(education);
            JSONArray jsonArrayclinics = jsonObject.getJSONArray("clinicList");
            for (int i = 0; i < jsonArrayclinics.length(); i++) {
                JSONObject jsonObjectclinic = jsonArrayclinics.getJSONObject(i);
                String clinics[] = new String[4];
                clinics[0] = jsonObjectclinic.optString("clinicName");
                clinics[1] = jsonObjectclinic.optString("clinicAdddress");
                clinics[2] = jsonObjectclinic.optString("clinicArea");
                clinics[3] = jsonObjectclinic.optString("clinicContact");
                clinicArrayList.add(clinics);
                clinic_adapter.notifyDataSetChanged();
            }
            JSONArray jsonArrayspecialities = jsonObject.getJSONArray("specList");
            String allSpecialities = "";
            for (int i = 0; i < jsonArrayspecialities.length(); i++) {
                JSONObject jsonArrayspeciality = jsonArrayspecialities.getJSONObject(i);
                allSpecialities += jsonArrayspeciality.optString("specName") + ",";
            }
            TextView speciality = (TextView) findViewById(R.id.specialityString);
            speciality.setText(allSpecialities.substring(0, allSpecialities.length() - 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_doctors_details, menu);
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
