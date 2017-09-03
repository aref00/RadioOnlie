package com.rf.radioonline;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

/**
 * Created by Aref on 01/12/2016.
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
public class Edit {
    AlertDialog.Builder builder;
    AlertDialog dialog;
    AlertDialog.Builder edit_builder;
    LayoutInflater inflater;
    View view;
    Context context;
    public Edit(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
        builder =new AlertDialog.Builder(context);
        edit_builder =new AlertDialog.Builder(context);
    }

    public void create(final int arg, final FragmentManager manager){
        view = inflater.inflate(R.layout.edit_prompt,null);
//Aref Akbari
        builder.setView(view);
        Button like = (Button) view.findViewById(R.id.like);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtils.addFavourite(context,arg);

                dialog.cancel();
            }
        });

        Button delete = (Button) view.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FileUtils.delete(context,arg);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.others = FileUtils.getNames(context);
                        MainActivity.mPager.setAdapter(new MainActivity.PagerAdapter(manager));
                    }
                },1000);

                dialog.cancel();
            }
        });

        builder.setCancelable(true);
        dialog = builder.create();

        dialog.show();
    }

    public void editFaves(final int arg, final FragmentManager manager, final int position){
        view = inflater.inflate(R.layout.faves_edit,null);

        builder.setView(view);
        Button dislike = (Button) view.findViewById(R.id.dislike);
        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtils.deleteFavourit(context,arg);
                Favourite.data.remove(position);
                Favourite.horizontalAdapter.notifyItemRemoved(position);
                Favourite.horizontalAdapter.notifyItemRangeRemoved(position,Favourite.data.size());
                dialog.cancel();
            }
        });
        builder.setCancelable(true);
        dialog = builder.create();

        dialog.show();
    }

}
