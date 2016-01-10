package com.example.rahul.networkvollyex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rahul.networkvollyex.R;

import java.util.ArrayList;

public class MyArrayAdapter extends ArrayAdapter<String[]> {
    private final Context context;
    private final ArrayList<String[]> values;

    public MyArrayAdapter(Context context, ArrayList<String[]> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.doctors2, parent, false);
        String data[] = values.get(position);
        ImageView doctorimg = (ImageView) rowView.findViewById(R.id.doctorimg);
        TextView doctorName = (TextView) rowView.findViewById(R.id.doctorName1);
        TextView doctorSpeciality = (TextView) rowView.findViewById(R.id.doctorSpeciality1);
        TextView doctorLocation = (TextView) rowView.findViewById(R.id.doctor_loc);
        TextView doctorrecomdation = (TextView) rowView.findViewById(R.id.doctor_recommendation);

        int i = 1;
        doctorName.setText(data[i]);
        i++;
        doctorSpeciality.setText(data[i]);
        i++;
        doctorLocation.setText(data[i]);
        i++;
        doctorrecomdation.setText(data[i]);
        notifyDataSetChanged();
        return rowView;

    }


}