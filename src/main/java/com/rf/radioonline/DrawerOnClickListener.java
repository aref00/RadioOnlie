package com.rf.radioonline;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by Aref on 02/12/2016.
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
public class DrawerOnClickListener implements ListView.OnItemClickListener {
    NewUrl newUrl;
    Context context;
            public DrawerOnClickListener(Context context, FragmentManager fragmentManager){
                this.context = context;
                newUrl = new NewUrl(context,fragmentManager);
            }
//Aref Akbari
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        setItem(position);

    }
    private void setItem(int position){
        switch (position){
            case 0:
                context.startActivity(new Intent(context,Browse.class));
                break;

            case 1:
                context.startActivity(new Intent(context,Favourite.class));
                break;

            case 2:
                newUrl.createAbout();
                break;
            case 3:
                System.exit(0);
                break;


        }
    }
}
