package com.example.rahul.networkvollyex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SpecialityAdoptor extends ArrayAdapter<String[]> {
    private final Context context;
    private final ArrayList<String[]> values;

    public SpecialityAdoptor(Context context, ArrayList<String[]> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.specialitydisplay, parent, false);
        String data[] = values.get(position);
        TextView speciality = (TextView) rowView.findViewById(R.id.epeciality);
        int i = 0;
        speciality.setText(data[i]);
        notifyDataSetChanged();
        return rowView;
    }
}

