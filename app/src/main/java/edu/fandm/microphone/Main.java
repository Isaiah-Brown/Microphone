package edu.fandm.microphone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Main extends AppCompatActivity {

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    MediaRecorder mr;
    //MediaPlayer mp;
    String currentFilePath;
    String currentFileName;

    ArrayList<Playback> playbackList = new ArrayList<>();

    int playButton = R.drawable.play_button_image;

    ListView lv;

    String playerFilePath;

    String currentPlayBackFile;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeListView();

        Button record = (Button) findViewById(R.id.recordButton);
        record.setOnClickListener(view -> handleRecordButton(record));



        if(isMicPresent()) {
            getMicPermission();
        }

        lv = findViewById(R.id.playbacks);
        lv.setOnItemClickListener((adapterView, view, i, l) -> {
            currentPlayBackFile = playbackList.get(i).filePath;
            PlayThread thread = new PlayThread(currentPlayBackFile);
            thread.start();
        });


        lv.setOnItemLongClickListener((adapterView, view, i, l) -> {
            currentPlayBackFile = playbackList.get(i).filePath;
            playbackList.remove(i);
            deleteRecording(currentPlayBackFile);
            return true;
        });

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
            PlaybackAdapter pa = new PlaybackAdapter(this, R.layout.list_row, playbackList);
            lv.setAdapter(pa);

        }
    }

    public void updateListView() {
        lv = (ListView) findViewById(R.id.playbacks);
        PlaybackAdapter pa = new PlaybackAdapter(this, R.layout.list_row, playbackList);
        lv.setAdapter(pa);
    }

    public void deleteRecording(String filePath) {
        File file = new File(currentPlayBackFile);
        if(file.exists()) {
            file.delete();
        }
        updateListView();

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
            playbackList.add(pb);
            updateListView();
            Log.d("Play", currentFilePath.toString());
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
        StringBuilder copy = new StringBuilder();
        for (int i = 0; i < currentFileName.length(); i++) {
            char c = currentFileName.charAt(i);
            if (c == ':') {
                copy.append('|');
            } else {
                copy.append(c);
            }
        }
        currentFileName = copy.toString();

        //currentFileName = time.toString();
        File recordingDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS + "/" + recordings);
        File file = new File(recordingDir, currentFileName + ".awb");

        return file.getPath();
    }



}