package com.example.crono.programacotalker.data;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.crono.programacotalker.R;

import java.util.ArrayList;

/**
 * Created by crono on 11-11-17.
 */

public class listAdapter extends ArrayAdapter<String> {




    private Context context;


    public listAdapter(Context context, ArrayList<String> dataItem) {
        super(context, R.layout.list_item, dataItem);
        this.context = context;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.list_item, null);
        viewHolder = new ViewHolder();
        viewHolder.text = (TextView) convertView.findViewById(R.id.chilList);



        final String temp = getItem(position);
        viewHolder.text.setText(temp);

        return convertView;
    }

    public class ViewHolder {
        TextView text;
    }
}
