package com.example.rahul.networkvollyex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class ClinicAdopter extends ArrayAdapter<String[]> {
    private final Context context;
    private final ArrayList<String[]> values;

    public ClinicAdopter(Context context, ArrayList<String[]> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.clinic_details, parent, false);
        String data[] = values.get(position);
        TextView clinicName = (TextView) rowView.findViewById(R.id.clinicName);
        TextView clinicaddress = (TextView) rowView.findViewById(R.id.clinicaddress);
        TextView clinicarea = (TextView) rowView.findViewById(R.id.clinicarea);
        TextView cliniccontact = (TextView) rowView.findViewById(R.id.cliniccontact);
        int i = 0;
        clinicName.setText(data[i]);
        i++;
        clinicaddress.setText(data[i]);
        i++;
        clinicarea.setText(data[i]);
        i++;
        cliniccontact.setText(data[i]);
        notifyDataSetChanged();
        return rowView;
    }

}

