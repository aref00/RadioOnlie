package com.rf.radioonline;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
//Aref Akbari
public class Browse extends AppCompatActivity {
    private ListView listView;
    private EditText text;
    private ImageView search_button;
    Context context;
    String[] temp3;

    private ArrayAdapter<String> adapter;
    String[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        context=this;

        listView = (ListView)findViewById(R.id.browse_list);
        text = (EditText)findViewById(R.id.search_text);
        search_button = (ImageView) findViewById(R.id.search_button);

        data = FileUtils.getNames(this);
        temp3 = data;
        adapter = new ArrayAdapter<String>(this,R.layout.search_list_item,data);
        listView.setAdapter(adapter);

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp     = text.getText().toString();
                String temp2    = "";

                for (String i:data
                     ) {
                    if (i.toLowerCase().contains(temp.toLowerCase())){
                        if (temp2 != ""){
                        temp2+=","+i;
                        }else temp2+=i;
                    }


                }
                temp3=temp2.split(",");
                adapter = new ArrayAdapter<String>(context,R.layout.search_list_item,temp3);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new ItemSelected());
            }
        });


        listView.setOnItemClickListener(new ItemSelected());

    }
    private class ItemSelected implements AdapterView.OnItemClickListener{


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.i("item","sellectd");
            for (int i=0 ; i < data.length ; i++){
                if (data[i].equals(temp3[position]) ){
                    ViewFragment.page=i;
                    MainActivity.mPager.setAdapter(new MainActivity.PagerAdapter(MainActivity.fragmentManager));
                    MainActivity.mPager.setCurrentItem(i);
                    MainActivity.current=FileUtils.getUrls(MainActivity.context,FileUtils.getNames(MainActivity.context)[i]);
                    MainActivity.play();
                   //Aref Akbari

                    break;
                }
            }
        }
    }

}
