package com.rf.radioonline;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import co.mobiwise.library.RadioListener;
import co.mobiwise.library.RadioManager;

/**
 * Created by RF on 9/3/17.
 * PCHA is a name for underground
 * coders
 */

public class MainActivity extends AppCompatActivity implements RadioListener {

    static ViewPager mPager;
    private ProgressDialog dialog;
    static String current;
    static String[] others;
    SharedPreferences preferences;
    private NewUrl newUrl;
    static Context context;
    static SeekBar bar;
    private ImageView volume_state;
    static int voloume=1;
    static Edit edit;
    static FragmentManager fragmentManager;
    private WifiManager.WifiLock wifiLock;
    static RadioManager radioManager;
    static AudioManager mAudioManager;
    LayoutInflater inflater;
    AlertDialog.Builder alertDialog;
    AlertDialog alert;
    View distroy;
    static String pack;
    MenuDrawer drawer;
    int max;
    private Button play;
    private Button pause;
    private Button stop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        drawer = MenuDrawer.attach(this, MenuDrawer.Type.OVERLAY, Position.TOP);
        drawer.setContentView(R.layout.drawer_main);
        drawer.setMenuView(R.layout.top_menu);
        drawer.peekDrawer(1,1);

        pack=getPackageName();

        radioManager= RadioManager.with(getApplicationContext());
        radioManager.registerListener(this);
        radioManager.enableNotification(true);
        mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        inflater = LayoutInflater.from(this);
        alertDialog =new AlertDialog.Builder(this);

        context = this ;
        preferences = getSharedPreferences("urls",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        newUrl = new NewUrl(context,getSupportFragmentManager());
        edit = new Edit(this);

        if (!preferences.contains("names")) {
            editor.putString("names", "Radio Shahab,420 Radio,Moodian Radio,Rasht Radio Farsi,Psychedelic Radio,SoundTrack Radio,Comedypipe (Comedy English),Radio Hamrah (Educational Farsi),Tapesh.com (Farsi),Radio 608 (Farsi),Alex Jones (English),Radio 70s Persian");
            editor.putString("Radio Shahab","http://5.160.219.244:9300/");
            editor.putString("420 Radio", "http://5.160.219.244:9306/");
            editor.putString("Moodian Radio", "http://5.160.219.244:9302/");
            editor.putString("Rasht Radio Farsi", "http://5.160.219.244:9304/");
            editor.putString("Psychedelic Radio", "http://5.160.219.244:9310/");
            editor.putString("SoundTrack Radio", "http://5.160.219.244:9300/");
            editor.putString("Comedypipe (Comedy English)","http://100.4.213.227:80/");
            editor.putString("Radio Hamrah (Educational Farsi)","http://38.99.146.102:8000/");
            editor.putString("Tapesh.com (Farsi)","http://198.255.95.36:8888/");
            editor.putString("Radio 608 (Farsi)","http://37.187.48.29:8000/");
            editor.putString("Alex Jones (English)","http://50.7.130.93:80/");
            editor.putString("Radio 70s Persian","http://38.99.146.102:8000/");
            editor.apply();

        }
        wifiLock = ((WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");

        wifiLock.acquire();
        others = FileUtils.getNames(context);
        mPager = (ViewPager) findViewById(R.id.gallery);
        PagerAdapter mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        current = FileUtils.getUrls(MainActivity.context,FileUtils.getNames(MainActivity.context)[ViewFragment.page]);

        play = (Button) findViewById(R.id.button2);
        pause = (Button) findViewById(R.id.button);
        stop = (Button) findViewById(R.id.button3);

        bar = (SeekBar)findViewById(R.id.seekBar);
        bar.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volume_state = (ImageView) findViewById(R.id.audio_state);
        //textView=(TextView) findViewById(R.id.buffering);
        //progressBar = (ProgressBar)findViewById(R.id.buffer);

        bar.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                play();

            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pause();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                stop();
            }
        });

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                max = bar.getMax();
                voloume =  progress;

                if (progress == 0){
                    volume_state.setImageResource(R.drawable.ic_av_volume_off);
                }else if (progress >0 & progress <= max/4){
                    volume_state.setImageResource(R.drawable.ic_av_volume_mute);
                }else if (progress > max/4 & progress < (max*2)/3){
                    volume_state.setImageResource(R.drawable.ic_av_volume_down);
                }else {
                    volume_state.setImageResource(R.drawable.ic_av_volume_up);
                }
                if (radioManager != null)

                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, voloume,0);
            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        fragmentManager = getSupportFragmentManager();
        dialog = new ProgressDialog(context);
        dialog.setMessage(getString(R.string.preparation));
        dialog.show();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        };

        new Handler().postDelayed(runnable,2000);

    }

    @Override
    public void onRadioConnected() {

    }

    @Override
    public void onRadioStarted() {

    }

    @Override
    public void onRadioStopped() {

    }

    @Override
    public void onMetaDataReceived(String s, String s1) {
    }

    public static class PagerAdapter extends FragmentStatePagerAdapter {
        LayoutInflater inflater;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public Fragment getItem(int position) {
            return ViewFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return others.length;
        }
    }

    public static void play(){
        if (MainActivity.radioManager == null)
            MainActivity.radioManager = RadioManager.with(MainActivity.context);
        if (radioManager.isPlaying())
            radioManager.stopRadio();

        final String t = FileUtils.getNames(MainActivity.context)[ViewFragment.page];

                String c = current;


                if (c != null){

                    Log.i("current",current);
                    radioManager.startRadio(current);


            }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                radioManager.updateNotification("now playing",t,R.drawable.default_art);
            }
        },2000);

    }

    private void pause() {

        radioManager.stopRadio();
    }

    private void stop() {

        radioManager.stopRadio();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
                newUrl.create();
                break;
            case R.id.about:
                newUrl.createAbout();
                break;
            case R.id.help:
                startActivity(new Intent(MainActivity.this,Help.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void next(View view){
        mPager.setCurrentItem(mPager.getCurrentItem()+1,true);
    }

    public void before(View view){
        mPager.setCurrentItem(mPager.getCurrentItem()-1,true);
    }


    public void add(View view){
        newUrl.create();
    }
    public void see_current(View view){
        mPager.setCurrentItem(ViewFragment.page);
    }

   @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
       boolean b =false;
        if (keyCode == KeyEvent.KEYCODE_BACK){
            b=true;
            distroy = inflater.inflate(R.layout.distroy_layout,null);
            alertDialog.setView(distroy);
            alertDialog.setCancelable(true).setPositiveButton(getString(R.string.keep_radio), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    finish();
                }
            })
                    .setNegativeButton(getString(R.string.shutdown), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            wifiLock.release();

                            radioManager.disconnect();
                            onDestroy();
                            System.exit(0);
                        }
                    });
            alert = alertDialog.create();
            alert.show();
            return true;

        }else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP | keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bar.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
                }
            },100);
            return super.onKeyDown(keyCode, event);
        }else return super.onKeyDown(keyCode, event);

   }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        radioManager.connect();
        super.onResume();
    }

    @Override
    protected void onStart() {
        radioManager.connect();
        super.onStart();
    }
    public void browse(View view){
        context.startActivity(new Intent(context,Browse.class));
    }
    public void fave(View view){
        context.startActivity(new Intent(context,Favourite.class));

    }
    public void about(View view){
        newUrl.createAbout();
    }
    public void exit(View view){
        System.exit(0);
    }

}
