package edu.fandm.microphone;

import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Button;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Playback {
        String name;
        String filePath;
        Playback(String filePath) {
            File f = new File(filePath);
            String n = f.getName();
            n = n.substring(0, n.length() - 4);
            this.name = n;
            this.filePath = filePath;
        }

    }
