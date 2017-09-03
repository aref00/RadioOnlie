package com.rf.radioonline;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Favourite extends AppCompatActivity {

    RecyclerView horizontal_recycler_view;
    static HorizontalAdapter horizontalAdapter;
    static List<Data> data;
    int last;
    SeekBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourites_ui);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        data = fill_with_data();
        Log.e("size",""+data.size());


        horizontal_recycler_view =(RecyclerView)findViewById(R.id.horizontal_recycler_view);
        horizontalAdapter = new HorizontalAdapter(data,this);

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(Favourite.this,LinearLayoutManager.HORIZONTAL,false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManager);
        horizontal_recycler_view.setAdapter(horizontalAdapter);

        bar = (SeekBar)findViewById(R.id.seekBar2);
        bar.setMax(MainActivity.mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        bar.setProgress(MainActivity.mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                MainActivity.bar.setProgress(progress);
                MainActivity.voloume = progress;
                bar.setProgress(progress);

                if (MainActivity.radioManager != null)

                    MainActivity.mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, MainActivity.voloume,0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



    }
    public List<Data> fill_with_data(){
        List<Data> data = new ArrayList<>();


        String[] faves=FileUtils.favourites(Favourite.this) ;
            Log.e("has","problem");
        if (faves != null && faves.length != 0) {
            for (int i = 0; i <faves.length; i++) {
                data.add(new Data(R.drawable.fav_not_pressed, faves[i]));
            }
        }
        return data;
    }

    public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {


        List<Data> horizontalList = Collections.emptyList();
        Context context;
        private int selected_position = 0;


        public HorizontalAdapter(List<Data> horizontalList, Context context) {
            this.horizontalList = horizontalList;
            this.context = context;
        }


        public class MyViewHolder extends RecyclerView.ViewHolder{

            View layout;
            ImageView imageView;
            TextView txtview;
            public MyViewHolder(View view) {
                super(view);
                layout = view;
                txtview=(TextView) view.findViewById(R.id.txtview);
                imageView=(ImageView)view.findViewById(R.id.imageView2);
            }

        }



        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_menu, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            //I have searched this
            //Aref Akbari
            Data item = data.get(position);
            if(selected_position == position){
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.germez));
                holder.imageView.setImageResource(R.drawable.wave);
            }else{
                holder.itemView.setBackgroundColor(Color.TRANSPARENT);
                holder.imageView.setImageResource(R.drawable.radio_khali);
            }
            holder.txtview.setText(horizontalList.get(position).txt);

            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    notifyItemChanged(selected_position);
                    selected_position = position;
                    notifyItemChanged(selected_position);


                        int temp = FileUtils.favouritesPosition(context)[position];
                        ViewFragment.page = temp;
                        MainActivity.mPager.setAdapter(new MainActivity.PagerAdapter(MainActivity.fragmentManager));
                        MainActivity.mPager.setCurrentItem(temp);
                        MainActivity.current = FileUtils.getUrls(MainActivity.context, FileUtils.getNames(MainActivity.context)[temp]);
                        MainActivity.play();



                }
            });
            holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int[] t = FileUtils.favouritesPosition(context);
                    if (t != null) {
                        int p = FileUtils.favouritesPosition(context)[position];
                        new Edit(Favourite.this).editFaves(p, getSupportFragmentManager(), position);
                    }
                    return false;
                }
            });


        }


        @Override
        public int getItemCount()
        {
            return this.horizontalList.size();
        }
    }

    public void play(View view){
        MainActivity.play();


    }
    public void pause(View view){
        if (MainActivity.radioManager.isPlaying()){

            MainActivity.radioManager.stopRadio();
        }
    }
    public void stop(View view){
        if (MainActivity.radioManager != null){
            MainActivity.radioManager.stopRadio();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
         if (keyCode == KeyEvent.KEYCODE_VOLUME_UP | keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
             MainActivity.voloume=MainActivity.mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
             new Handler().postDelayed(new Runnable() {
                 @Override
                 public void run() {
                     bar.setProgress(MainActivity.mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
                     MainActivity.bar.setProgress(MainActivity.mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
                 }
             },100);
//Aref Akbari
             return super.onKeyDown(keyCode, event);
        }else return super.onKeyDown(keyCode, event);
    }

}
