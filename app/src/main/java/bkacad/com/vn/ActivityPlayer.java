package bkacad.com.vn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageButton;

import java.io.File;
import java.util.ArrayList;

import bkacad.com.vn.adapters.AdapterSong;
import bkacad.com.vn.domains.Song;
import bkacad.com.vn.helpers.FileManager;
import bkacad.com.vn.services.ServicePlayer;

public class ActivityPlayer extends AppCompatActivity {
    ImageButton btnPlayOrPause, btnNext, btnPrev;
    RecyclerView rcListMusic;
    ArrayList<Song> songs;
    AdapterSong adapterSong;
    ServicePlayer servicePlayer;

    void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //chưa đc cấp quyền
                //yêu cầu người dùng cáp quyền ghi
                requestPermissions(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }, 2);
            } else {
                loadMusic();
            }
        }
    }

    private void loadMusic() {
        songs = FileManager.loadMusicFiles(new File(Environment.getExternalStorageDirectory().toString()));
        adapterSong = new AdapterSong(this, songs);
        LinearLayoutManager lln = new LinearLayoutManager(ActivityPlayer.this,
                RecyclerView.VERTICAL, false);
        rcListMusic.setAdapter(adapterSong);
        rcListMusic.setLayoutManager(lln);
        adapterSong.setOnItemClickListener(new AdapterSong.MyItemClickListener() {
            @Override
            public void onItemClick(Song song, int activeSongIndex) {
                System.out.println("TEST");
                if (servicePlayer != null) {
                    System.out.println("TEST");
                    servicePlayer.setPlayList(songs, activeSongIndex);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2) {
            //hiểu là phản hồi của người dùng từ yêu cầu cấp quyền có mã request code là 2
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    songs = FileManager.loadMusicFiles(new File(Environment.getExternalStorageDirectory().toString()));
                    adapterSong = new AdapterSong(this, songs);
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        rcListMusic = findViewById(R.id.rcListMusic);
        btnPlayOrPause = findViewById(R.id.btnPlayOrPause);
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);

        btnPlayOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (servicePlayer != null) {
                    if (servicePlayer.isPlaying()) {
                        servicePlayer.pause();
                    } else {
                        servicePlayer.resume();
                    }
                }
            }
        });

        //songs = FileManager.loadMusicFiles(new File(Envi†ronment.getExternalStorageDirectory().toString()));
        //adapterSong = new AdapterSong(this, songs);
        checkPermission();

        //bind service vao activity
        Intent intent = new Intent(ActivityPlayer.this, ServicePlayer.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ServicePlayer.ACTION_PLAY);
        intentFilter.addAction(ServicePlayer.ACTION_PAUSE);
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(broadcastReceiver, intentFilter);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ServicePlayer.ACTION_PLAY:
                    btnPlayOrPause.setImageDrawable(getDrawable(R.drawable.ic_baseline_pause_circle_outline_24));
                    break;

                case ServicePlayer.ACTION_PAUSE:
                    btnPlayOrPause.setImageDrawable(getDrawable(R.drawable.ic_baseline_play_circle_outline_24));
                    break;
            }
        }
    };

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ServicePlayer.MyBinder myBinder = (ServicePlayer.MyBinder) service;
            servicePlayer = (ServicePlayer) myBinder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}