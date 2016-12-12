package com.github.maximkirko.training_2017_android;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class Fragment1 extends Fragment {

    private TextView T1;

    public Fragment1() {
    }

    public static Fragment1 newInstance(String T1_text) {
        Fragment1 F1 = new Fragment1();
        Bundle args = new Bundle();
        args.putString("T1_text", T1_text);
        F1.setArguments(args);
        return F1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setRetainInstance(false);
        View v = inflater.inflate(R.layout.fragment_f1, container, false);

        String T1_text = getArguments().getString("T1_text");

        T1 = (TextView) v.findViewById(R.id.T1);
        T1.setText(T1_text);

        return v;
    }
}
