package com.rf.radioonline;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

/**
 * Created by Aref on 28/11/2016.
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
public class FileUtils {
    static String temp="";

    public static String[] getNames(Context context){
        SharedPreferences preferences = context.getSharedPreferences("urls",Context.MODE_PRIVATE);
        return preferences.getString("names",null).split(",");
    }


    public static String getUrls(Context context,String name){
        SharedPreferences preferences = context.getSharedPreferences("urls",Context.MODE_PRIVATE);
        return preferences.getString(name,null);
    }



    public static void addName(Context context,String name){
        Boolean state = true;
        SharedPreferences preferences = context.getSharedPreferences("urls",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String[] names = getNames(context);
        for (int i =0 ; i < names.length ; i++){
            if (names[i].equals(name)) {
                state = false;
                break;
            }else if (i == (names.length-1) & state ){
                String temp = preferences.getString("names",null);
                temp+=","+name;
                editor.putString("names",temp);
                editor.apply();
            }

        }
    }

    public static void addUrl(Context context , String arg0 , String arg1){
        SharedPreferences preferences = context.getSharedPreferences("urls",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(arg0,arg1);
        editor.apply();
    }
    public static void delete(Context context , int arg0){
        deleteFavourit(context,arg0);
        SharedPreferences preferences = context.getSharedPreferences("urls",Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        String[] names = FileUtils.getNames(context);
        editor.remove(names[arg0]);
        temp="";
        for (int i = 0 ; i < names.length ; i++ ){
            if ( i == arg0){

            }else if (temp == ""){
                temp +=names[i];
            }else {
                temp += ","+names[i];
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                editor.putString("names",temp);
                editor.apply();

            }
        },2000);



    }
    public static String[] favourites(Context context){
        Log.i("hhhhm","for test");
        SharedPreferences preferences = context.getSharedPreferences("fav",Context.MODE_PRIVATE);
        if (preferences.contains("favourite_names")) {
            String favs = preferences.getString("favourite_names", null);
            Log.i("problemBase","base");
            if (favs != null & favs!= "") {
                Log.e("problemBase","null");
                return favs.split(",");
            } else {
                Log.e("fucking","fuck"+" :"+favs);
                return null;
            }
        }else{
            Log.i("fuck","fuck");
            return null;
            //Aref Akbari
        }
    }
    public static int[] favouritesPosition(Context context){
        String[] faves = FileUtils.favourites(context);
        String[] names = FileUtils.getNames(context);
        if (faves != null & names != null) {
            int[] ints = new int[faves.length];

            for (int i = 0; i < faves.length; i++) {
                for (int j = 0; j < names.length; j++) {
                    if (faves[i].equals(names[j]))
                        ints[i] = j;
                }
            }
            return ints;
        }else return null;
    }

    public static void addFavourite(Context context,int arg){
        String[] all = FileUtils.getNames(context);
        String[] faves=FileUtils.favourites(context);
        boolean temp =true;
        SharedPreferences preferences = context.getSharedPreferences("fav",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if (preferences.contains("favourite_names")){
            if (faves != null) {
                for (String i : faves) {
                    if (i.equals(all[arg])) {
                        temp = false;
                    }
                }
            }

            if (temp) {
                if (faves != null) {
                    String names = preferences.getString("favourite_names", null);
                    names += "," + (all[arg]);
                    editor.putString("favourite_names", names);
                    editor.apply();
                }else {
                    String names = preferences.getString("favourite_names", null);
                    names += all[arg];
                    editor.putString("favourite_names", names);
                    editor.apply();
                }
            }

        }else {
            editor.putString("favourite_names",all[arg]);
            editor.apply();
        }
    }
    public static void deleteFavourit(Context context,int arg){
        String name = FileUtils.getNames(context)[arg];
        int position=0;
        boolean found=false;
        SharedPreferences preferences = context.getSharedPreferences("fav",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        if (preferences.contains("favourite_names")){
            String[] faves = favourites(context);
            if (faves != null){
                for (int i=0 ; i<faves.length ; i++) {
                    if (faves[i].equals(name)){
                        position=i;
                        found=true;
                        break;
                    }
                }
                if (found){
                    String temp;
                    if (position == 0) {
                        if (faves.length > 1) {
                            temp = faves[1];
                            Log.i("condition","first");
                            for (int i = 2; i < faves.length; i++) {
                                temp += "," + faves[i];
                            }
                            editor.putString("favourite_names",temp);
                        }else {
                            Log.i("condition","second");
                            temp="";
                            editor.remove("favourite_names");
                        }

                    }else{
                        Log.i("condition","third");
                        temp = faves[0];
                        for (int i = 1; i < faves.length; i++){
                            temp += "," + faves[i];
                        }
//Aref Akbari
                    }
                    editor.apply();
                }
            }
        }
    }

}
