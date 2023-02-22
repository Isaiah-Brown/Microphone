package edu.fandm.microphone;

import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Button;

import java.io.File;

    public class Playback {

        MediaPlayer mp = new MediaPlayer();
        String name;
        String filePath;

        Playback(String filePath) {
            StringBuilder s = new StringBuilder();
            int i = filePath.length() - 1;
            char c = filePath.charAt(i);
            while(c != '/') {
                c = filePath.charAt(i);
                s.append(c);
                i -= 1;
            }

            s.deleteCharAt(s.length() - 1);
            this.name = s.reverse().toString();
            Log.d("NAME", name);

            this.filePath = filePath;

            try {
                this.mp.setDataSource(filePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
