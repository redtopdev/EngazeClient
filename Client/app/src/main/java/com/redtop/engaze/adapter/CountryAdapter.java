package com.redtop.engaze.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.redtop.engaze.R;
import com.redtop.engaze.entity.Country;
 
public class CountryAdapter extends ArrayAdapter<Country> {
    private Activity context;
    ArrayList<Country> data = null;
 
    public CountryAdapter(Activity context, int resource,
            ArrayList<Country> data) {
        super(context, resource, data);
        this.context = context;
        this.data = data;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) { 
        return super.getView(position, convertView, parent);
    }
 
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) { 
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }
 
        Country item = data.get(position);
 
        if (item != null) { // Parse the data from each object and set it.
//            TextView CountryId = (TextView) row.findViewById(R.id.item_id);
        	EditText CountryName = (EditText) row.findViewById(R.id.country_code);
            /*if (CountryId != null) {
                CountryId.setText(item.getId());
            }*/
            if (CountryName != null) {
                CountryName.setText(item.getName());
            }
 
        }
 
        return row;
    }
}
