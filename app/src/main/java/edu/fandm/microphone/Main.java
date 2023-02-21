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
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Main extends AppCompatActivity {

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;
    MediaRecorder mr = null;
    MediaPlayer mp = null;
    String currentFilePath;
    String currentFileName;

    ArrayList<Playback> playbackList = new ArrayList<>();

    int playButton = R.drawable.play_button_image;

    ListView lv;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initializeListView();

        Button record = (Button) findViewById(R.id.recordButton);
        record.setOnClickListener(view -> handleRecordButton(record));

        Button play = (Button) findViewById(R.id.playButton);
        play.setOnClickListener(view -> handlePlayButton(play));

        if(isMicPresent()) {
            getMicPermission();
        }



    }

    public void initializeListView() {
        String recordings = "recordings";
        File parentDirectory = new File(Environment.DIRECTORY_DOCUMENTS, recordings);
        if (!parentDirectory.exists()) {
            parentDirectory.mkdir();
        }
        File recordingDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS + "/" + recordings);

        File[] directoryListing = recordingDir.listFiles();             https://stackoverflow.com/questions/4917326/how-to-iterate-over-the-files-of-a-certain-directory-in-java
        if (directoryListing != null) {
            for (File recording : directoryListing) {
                // Do something with child
                String FilePath = recording.getPath();
                Log.d("filepath", FilePath);
                Playback pb = new Playback(FilePath);
                playbackList.add(pb);
            }
            lv = (ListView) findViewById(R.id.playbacks);
            PlaybackAdapter playbackAdapter = new PlaybackAdapter(getApplicationContext(), playbackList, playButton);
            lv.setAdapter(playbackAdapter);
        }

    }




    public void handleRecordButton(Button b) {
        Date time = Calendar.getInstance().getTime();
        Log.d("Time", time.toString());

        if (b.getText().toString().equals("Record")) {
            try {
                mr = new MediaRecorder();
                mr.setAudioSource(MediaRecorder.AudioSource.MIC);
                mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                currentFilePath = makeRecordingFilePath();
                mr.setOutputFile(currentFilePath);
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
            Playback pb = new Playback(currentFilePath);
            Log.d("Play", currentFilePath.toString());
        }

    }

    public void handlePlayButton(Button b) {
        try {
            mp = new MediaPlayer();
            mp.setDataSource(makeRecordingFilePath());
            Log.d("Play", makeRecordingFilePath());
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
        Date time = Calendar.getInstance().getTime();
        currentFileName = time.toString();
        File recordingDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS + "/" + recordings);
        File file = new File(recordingDir, currentFileName + ".mp3");

        return file.getPath();
    }



}