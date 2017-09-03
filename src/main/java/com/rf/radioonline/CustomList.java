package com.rf.radioonline;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Aref on 03/12/2016.
 * *******************************
 * *******************************
 * ******---***********------*****
 * *****------*********--*********
 * *****--**--*********------*****
 * *****-----**********--*********
 * *****--**--*********--*********
 * *****--***--********--*********
 * *******************************
 */
public class CustomList extends ArrayAdapter<String> {
    Activity context;
    int[] images;
    String[] texts;
    public CustomList(Activity context,String[] texts , int[] images) {
        super(context, R.layout.drawable_items,texts);
        this.context = context;
        this.texts=texts;
        this.images = images;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.drawable_items,null,true);
        TextView textView = (TextView)view.findViewById(R.id.txt);
        textView.setText(texts[position]);
        ImageView imageView = (ImageView)view.findViewById(R.id.img);
        imageView.setImageResource(images[position]);
        return view;
    }
}
