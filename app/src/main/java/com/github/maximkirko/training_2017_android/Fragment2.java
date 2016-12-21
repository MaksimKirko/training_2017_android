package com.github.maximkirko.training_2017_android;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class Fragment2 extends Fragment {

    private TextView T2;
    private ImageView I;

    public static final String S_EXTRA = "S_TEXT";
    public static String IMAGE_ID_EXTRA = "IMAGE_ID";

    public static Fragment2 newInstance(String S_text, int imageId) {
        Fragment2 F2 = new Fragment2();
        Bundle args = new Bundle();
        args.putString(S_EXTRA, S_text);
        args.putInt(IMAGE_ID_EXTRA, imageId);
        F2.setArguments(args);
        return F2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setRetainInstance(false);
        View v = inflater.inflate(R.layout.fragment_f2, container, false);

        T2 = (TextView) v.findViewById(R.id.T2);
        I = (ImageView) v.findViewById(R.id.I);

        String S_text = getArguments().getString(S_EXTRA);
        int imageId = getArguments().getInt(IMAGE_ID_EXTRA);

        T2.setText(S_text);
        I.setImageResource(imageId);
        return v;
    }
}