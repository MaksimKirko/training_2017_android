package com.github.maximkirko.training_2017_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.maximkirko.training_2017_android.util.StringUtils;

public class A1 extends AppCompatActivity {

    //    views
    private EditText E1;
    private EditText E2;
    private Spinner S;
    private CheckBox C;
    private Button B;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a1);

        E1 = (EditText) findViewById(R.id.E1);
        E2 = (EditText) findViewById(R.id.E2);
        S = (Spinner) findViewById(R.id.S);
        C = (CheckBox) findViewById(R.id.C);
        B = (Button) findViewById(R.id.B);

        E1.setText("text e1");
        E2.setText("text e2");
    }

    private boolean validate() {

        boolean flag = true;

        if (StringUtils.isEmpty(E1.getText().toString())) {
            E1.setHint(R.string.error_hint);
            E1.setHintTextColor(getResources().getColor(R.color.colorError));
            flag = false;
        }

        if (StringUtils.isEmpty(E2.getText().toString())) {
            E2.setHint(R.string.error_hint);
            E2.setHintTextColor(getResources().getColor(R.color.colorError));
            flag = false;
        }

        return flag;
    }

    private void setToDefault() {
        E1.setHint(R.string.E1_hint);
        E2.setHint(null);
    }

    public void onBClick(View view) {

        if (validate()) {
            setToDefault();
            Intent intent = new Intent(A1.this, A2.class);

            intent.putExtra("E1", E1.getText().toString());
            intent.putExtra("E2", E2.getText().toString());
            intent.putExtra("S", S.getSelectedItem().toString());
            intent.putExtra("S_pos", S.getSelectedItemPosition());
            intent.putExtra("C", C.isChecked());

            startActivity(intent);
        }
    }
}
