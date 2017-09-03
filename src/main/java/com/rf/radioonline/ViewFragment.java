package com.rf.radioonline;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Aref on 30/11/2016.
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
public class ViewFragment extends Fragment {
    static int page;

    public static ViewFragment newInstance(int position){
        ViewFragment fragment = new ViewFragment();
        Bundle bundle = new Bundle(2);
        bundle.putInt("pos",position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final int i = getArguments().getInt("pos");
        final View root = (View) inflater.inflate(R.layout.view,container,false);
        final TextView textView = (TextView) root.findViewById(R.id.others_name);
        textView.setText(MainActivity.others[i]);
        if (i == page){
        ImageView imageView = (ImageView)root.findViewById(R.id.nowPlaying);
        imageView.setVisibility(View.VISIBLE);}
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = i;
                MainActivity.mPager.setAdapter(new MainActivity.PagerAdapter(getFragmentManager()));
                MainActivity.mPager.setCurrentItem(i);
                MainActivity.current=FileUtils.getUrls(MainActivity.context,FileUtils.getNames(MainActivity.context)[i]);
                MainActivity.play();



            }
        });
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MainActivity.edit.create(i,getFragmentManager());
                return false;
            }
        });

        return root;


    }
}
