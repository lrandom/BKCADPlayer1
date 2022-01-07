package bkacad.com.vn.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;

import bkacad.com.vn.domains.Song;

public class ServicePlayer extends Service {
    public static final String ACTION_PLAY = "bkacad.com.vn.bkcad_player.ACTION_PLAY";
    public static final String ACTION_PAUSE = "bkacad.com.vn.bkcad_player.ACTION_PAUSE";
    private MediaPlayer mediaPlayer;
    ArrayList<Song> songs = new ArrayList<>();
    int activeSongIndex = 0;
    Song playingSong = new Song();


    @Override
    public void onCreate() {
        super.onCreate();

    }

    public void play() {
        if (this.songs.size() > 0) {
            playingSong = this.songs.get(this.activeSongIndex);
            String path = playingSong.getPath();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(path));
            mediaPlayer.start();
            Intent intent = new Intent(ACTION_PLAY);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        }
    }

    public void pause() {
        if (this.mediaPlayer != null && this.mediaPlayer.isPlaying()) {
            this.mediaPlayer.pause();
            Intent intent = new Intent(ACTION_PAUSE);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        }
    }


    public void resume() {
        if (this.mediaPlayer != null && !this.mediaPlayer.isPlaying()) {
            this.mediaPlayer.start();
            Intent intent = new Intent(ACTION_PLAY);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        }
    }

    public boolean isPlaying() {
        return
                this.mediaPlayer.isPlaying();
    }

    public void next() {
        if (activeSongIndex < songs.size() - 2) {
            activeSongIndex++;
            play();
        }
    }

    public void prev() {
        if (activeSongIndex >= 1) {
            activeSongIndex--;
            play();
        }
    }

    //phương thức set vào một danh sách nhạco
    public void setPlayList(ArrayList<Song> songs, int activeSongIndex) {
        this.songs = songs;
        this.activeSongIndex = activeSongIndex;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        play();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public class MyBinder extends Binder {
        public Service getService() {
            return ServicePlayer.this;
        }
    }

}
