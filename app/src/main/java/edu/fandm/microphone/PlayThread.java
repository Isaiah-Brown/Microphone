package edu.fandm.microphone;

import android.media.MediaPlayer;

public class PlayThread extends Thread {

    MediaPlayer mp = new MediaPlayer();
    String filePath;

    PlayThread(String path) {
        this.filePath = path;
    }

    @Override
    public void run() {
        prepareMediaPlayer();
        try {
            mp.setDataSource(filePath);
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void prepareMediaPlayer() {
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.reset();
                mediaPlayer.release();
            }
        });
    }

}
