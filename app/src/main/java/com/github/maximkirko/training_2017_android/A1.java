package com.github.maximkirko.training_2017_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.github.maximkirko.training_2017_android.util.StringUtils;

public class A1 extends AppCompatActivity implements View.OnClickListener {

    //   region Views
    private EditText E1;
    private EditText E2;
    private Spinner S;
    private CheckBox C;
    private Button B;
    // endregion

    //    region intent  extras
    public static final String E1_EXTRA = "E1_EXTRA";
    public static final String E2_EXTRA = "E2_EXTRA";
    public static final String S_EXTRA = "S_EXTRA";
    public static final String S_POS_EXTRA = "S_POS_EXTRA";
    public static final String C_EXTRA = "C_EXTRA";
    // endregion

    //    region for spinner strings
    private ArrayAdapter<String> adapter;
    // endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a1);

        initViews();
        initAdapter();
    }

    private void initViews() {
        E1 = (EditText) findViewById(R.id.E1);
        E2 = (EditText) findViewById(R.id.E2);
        S = (Spinner) findViewById(R.id.S);
        C = (CheckBox) findViewById(R.id.C);
        B = (Button) findViewById(R.id.B);
        B.setOnClickListener(this);
    }

    private void initAdapter() {
        adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.S_values));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        S.setAdapter(adapter);
    }

    private void setEditTextErrorStatus(EditText view) {
        view.setHint(R.string.error_hint);
        view.setHintTextColor(ContextCompat.getColor(this, R.color.colorError));
    }

    private void setValuesToDefault() {
        E1.setHint(R.string.E1_hint);
        E2.setHint(null);
    }

    private void setIntentExtras(Intent intent) {
        intent.putExtra(E1_EXTRA, E1.getText().toString());
        intent.putExtra(E2_EXTRA, E2.getText().toString());
        intent.putExtra(S_EXTRA, S.getSelectedItem().toString());
        intent.putExtra(S_POS_EXTRA, getResourceFromSpinnerValue(S, S.getSelectedItem().toString()));
        intent.putExtra(C_EXTRA, C.isChecked());
    }

    private int getResourceFromSpinnerValue(Spinner spinner, String selected) {

        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        int position = adapter.getPosition(selected);
        return SpinnerPicturesEnum.getResourceIdByPosition(position);
    }

    @Override
    public void onClick(View view) {
        if (isValid()) {
            setValuesToDefault();
            Intent intent = new Intent(this, A2.class);
            setIntentExtras(intent);
            startActivity(intent);
        }
    }

    private boolean isValid() {

        boolean flag = true;

        if (StringUtils.isEmpty(E1.getText().toString())) {
            setEditTextErrorStatus(E1);
            flag = false;
        }
        if (StringUtils.isEmpty(E2.getText().toString())) {
            setEditTextErrorStatus(E2);
            flag = false;
        }

        return flag;
    }

}
