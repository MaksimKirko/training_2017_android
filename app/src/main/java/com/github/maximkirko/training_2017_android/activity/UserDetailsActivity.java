package com.github.maximkirko.training_2017_android.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.model.User;

/**
 * Created by MadMax on 25.12.2016.
 */

public class UserDetailsActivity extends AppCompatActivity {

    private TextView tvTitle;
    private TextView tvDescription;
    private ImageView ivImage;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_details_activity);

        user = this.getIntent().getParcelableExtra(FriendsListActivity.USER_EXTRA);

        initViews();
        setViewsValues();
    }

    private void initViews() {
        tvTitle = (TextView) findViewById(R.id.textview_user_details_activity_name);
        tvDescription = (TextView) findViewById(R.id.textview_user_details_activity_description);
        ivImage = (ImageView) findViewById(R.id.imageview_user_details_activity_photo);
    }

    private void setViewsValues() {
        tvTitle.setText(user.first_name + " " + user.last_name);
        tvDescription.setText(user.online ? "Online" : "");
        ivImage.setImageBitmap(user.getUserPhoto50());
    }
}
