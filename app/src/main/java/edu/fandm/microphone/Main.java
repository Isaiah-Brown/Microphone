package edu.fandm.microphone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class Main extends AppCompatActivity {

    private static int MIC_PERMISSION_CODE = 200;
    private int count = 1;
    MediaRecorder mr;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button record = (Button) findViewById(R.id.recordButton);
        record.setOnClickListener(view -> handleRecordButton(record));

        Button play = (Button) findViewById(R.id.playButton);
        play.setOnClickListener(view -> handlePlayButton(play));

        if(isMicPresent()) {
            getMicPermission();
        }
    }




    public void handleRecordButton(Button b) {

        if (b.getText().toString().equals("Record")) {
            try {
                mr = new MediaRecorder();
                mr.setAudioSource(MediaRecorder.AudioSource.MIC);
                mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mr.setOutputFile(getRecordingFilePath());
                mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mr.prepare();
                mr.start();
                Toast.makeText(this, "Recording started", Toast.LENGTH_LONG).show();
                b.setText("Stop");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mr.stop();
            mr.release();
            mr = null;
        }

    }

    public void handlePlayButton(Button b) {
        try {
            mp = new MediaPlayer();
            mp.setDataSource(getRecordingFilePath());
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void getMicPermission() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.RECORD_AUDIO}, MIC_PERMISSION_CODE);
        }

    }


    private boolean isMicPresent() {
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)) {
            return true;
        } else {
            return false;
        }
    }

    private String getRecordingFilePath() {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File recordingDir = cw.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(recordingDir, "recordingFile" + String.valueOf(count) + ".mp3");
        count += 1;
        return file.getPath();
    }



}