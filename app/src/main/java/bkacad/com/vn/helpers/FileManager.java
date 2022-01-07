package bkacad.com.vn.helpers;

import java.io.File;
import java.util.ArrayList;

import bkacad.com.vn.domains.Song;

public class FileManager {
    static ArrayList<Song> songs = new ArrayList<>();

    public static ArrayList<Song> loadMusicFiles(File rootDir) {
        File[] files = rootDir.listFiles();
        if(files!=null) {
            for (int i = 0; i < files.length; i++) {
                System.out.println(i);
                File file = files[i];
                if (file.isDirectory()) {
                    loadMusicFiles(file);
                } else if (file.getName().endsWith(".wav") || file.getName().endsWith(".mp3")
                        || file.getName().endsWith(".ogg")
                ) {
                    Song song = new Song();
                    song.setName(file.getName());
                    song.setPath(file.getPath());
                    System.out.println(file.getPath());
                    songs.add(song);
                }
            }
        }
        return songs;
    }
}
