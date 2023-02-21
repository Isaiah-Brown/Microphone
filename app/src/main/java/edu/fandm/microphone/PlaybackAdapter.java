package edu.fandm.microphone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PlaybackAdapter extends BaseAdapter {

    Context context;
    ArrayList<Playback> playbackList;

    int playButton;
    LayoutInflater inflater;

    PlaybackAdapter(Context ctx, ArrayList<Playback> pbl, int pbn) {
        this.context = ctx;
        this.playbackList = pbl;
        this.playButton = pbn;
        this.inflater = LayoutInflater.from(ctx);
    }


    @Override
    public int getCount() {
        return playbackList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.activity_main, null);
        TextView tv = (TextView) view.findViewById(R.id.textView);
        ImageView iv = (ImageView) view.findViewById(R.id.playImage);
        Log.d("NAME", playbackList.get(i).name);
        tv.setText(playbackList.get(i).name);
        iv.setImageResource(playButton);
        return view;
    }
}
