package edu.fandm.microphone;


import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.ArrayList;

public class PlaybackAdapter extends ArrayAdapter<Playback> {

    private final Context ctx;
    private final int rsc;

    public PlaybackAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Playback> objects) {
        super(context, resource, objects);
        this.ctx = context;
        this.rsc = resource;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater li = LayoutInflater.from(ctx);
        convertView = li.inflate(rsc, parent, false);
        ImageView iv = convertView.findViewById(R.id.image);
        TextView tv = convertView.findViewById(R.id.text_view);
        iv.setImageResource(R.drawable.baseline_play_arrow_24);
        tv.setText(getItem(position).name);
        return convertView;
    }
}