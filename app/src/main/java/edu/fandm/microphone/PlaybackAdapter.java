package edu.fandm.microphone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PlaybackAdapter extends ArrayAdapter<Playback> {

    private Context ctx;
    private int rsc;

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

        iv.setImageResource(R.drawable.play_button_image);
        tv.setText(getItem(position).name);

        return convertView;
    }
}
