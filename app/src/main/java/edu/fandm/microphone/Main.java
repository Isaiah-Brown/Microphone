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
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class Main extends AppCompatActivity {

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;
    private int count = 1;
    MediaRecorder mr = null;
    MediaPlayer mp = null;

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
                mr.setOutputFile(makeRecordingFilePath());
                mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mr.prepare();
                mr.start();
                Toast.makeText(this, "Recording started", Toast.LENGTH_LONG).show();
                b.setText("Stop");
                Log.d("Main", makeRecordingFilePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mr.stop();
            mr.release();
            mr = null;
            b.setText("Record");
        }

    }

    public void handlePlayButton(Button b) {
        try {
            mp = new MediaPlayer();
            mp.setDataSource(makeRecordingFilePath());
            Log.d("MAIN", makeRecordingFilePath());
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void getMicPermission() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
        }

    }


    private boolean isMicPresent() {
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)) {
            return true;
        } else {
            return false;
        }
    }

    private String makeRecordingFilePath() {

        String state = Environment.getExternalStorageState();
        if(!state.equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(getApplicationContext(), "External Storage not Available!", Toast.LENGTH_SHORT).show();
            return null;
        }
        String recordings = "recordings";
        File parentDirectory = new File(Environment.DIRECTORY_DOCUMENTS, recordings);
        if (!parentDirectory.exists()) {
            parentDirectory.mkdir();
        }
        File recordingDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS + "/" + recordings);
        File file = new File(recordingDir, "recordingFile.mp3");
        return file.getPath();
    }



}