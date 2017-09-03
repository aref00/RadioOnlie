package com.rf.radioonline;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Aref on 26/11/2016.
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
public class NewUrl {
    Context context;
    View view;
    LayoutInflater inflater;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    AlertDialog.Builder about_builder;
    AlertDialog about_dialog;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String namesBuilder;
    String arg0;
    String arg1;
    Boolean condition;
    FragmentManager manager;

    public NewUrl(Context context, FragmentManager manager){
        this.context=context;
        this.manager = manager;
        inflater = LayoutInflater.from(this.context);
        builder =new AlertDialog.Builder(this.context);
        about_builder =new AlertDialog.Builder(this.context);
    }

    public void create(){
        view = inflater.inflate(R.layout.prompt,null);
        builder.setView(view);
        builder.setCancelable(true).setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                EditText name = (EditText) view.findViewById(R.id.name);
                EditText url = (EditText) view.findViewById(R.id.url);

                //todo : return url and name to main to add to shared prefrense
                String n =name.getText().toString();
                String u =url.getText().toString();
                Log.i("u lenght" , String.valueOf(u.length()));
                Log.i("n lenght" , String.valueOf(n.length()));
                if (n.length() >= 1 & u.length() >= 4){
                    String arg0 = name.getText().toString();
                    String arg1 = url.getText().toString();
                    save(arg0,arg1);}
            }
        })
                .setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        dialog = builder.create();
        dialog.show();


    }


    public void createAbout(){
        view = inflater.inflate(R.layout.about_prompt,null);
        about_builder.setView(view);
        about_builder.setCancelable(true).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog=about_builder.create();
        dialog.show();
    }


    public void save(@NonNull String name ,@NonNull String url){
        arg0 = name;
        arg1 = url;
        arg0=arg0.replace(","," ");
        arg1=arg1.replace(","," ");
        condition = true;
        preferences = context.getSharedPreferences("urls",Context.MODE_PRIVATE);
        editor = preferences.edit();
        namesBuilder = preferences.getString("names",null);
        String[] all = FileUtils.getNames(context);  //(preferences.getString("names",null)).split(",");

        for (String anAll : all) {
            if (anAll.equals(name)) {
                editor = editor.putString(arg0, arg1);
                editor.apply();
                condition = false;
                break;
            }
        }
        new Handler().postDelayed(r,500);


    }
    public void updateView (String[] data){
        MainActivity.others = FileUtils.getNames(context);
        MainActivity.mPager.setAdapter(new MainActivity.PagerAdapter(manager));
    }
    Runnable r = new Runnable() {
        @Override
        public void run() {
            if (condition) {
                namesBuilder +=","+arg0;
                editor = editor.putString("names", namesBuilder);
                editor = editor.putString(arg0, arg1);
                condition = true;
                editor.apply();
                String[] data = FileUtils.getNames(context);
            updateView(data);
            }
        }
    };


}
