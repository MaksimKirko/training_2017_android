package com.github.maximkirko.training_2017_android.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.memorymanage.BitmapMemoryManager;
import com.github.maximkirko.training_2017_android.model.User;

/**
 * Created by MadMax on 25.12.2016.
 */

public class UserDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvTitle;
    private TextView onlineStatusView;
    private ImageView userPhotoView;
    private Button openPageButton;

    private User user;
    private BitmapMemoryManager bitmapMemoryManager;

    private static final String USER_PAGE_BASE_URL = "https://vk.com/id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_details_activity);

        getIntentExtras();
        initViews();
        setViewsValues();
    }

    private void getIntentExtras() {
        user = this.getIntent().getParcelableExtra(FriendsListActivity.USER_EXTRA);
        bitmapMemoryManager = this.getIntent().getParcelableExtra(FriendsListActivity.BMM_EXTRA);
//        bitmapMemoryManager = BitmapMemoryManager.newBuilder()
//                .setImageHeight(getResources().getDimensionPixelSize(R.dimen.size_user_details_photo))
//                .setImageWidth(getResources().getDimensionPixelSize(R.dimen.size_user_details_photo))
//                .build();
    }

    private void initViews() {
        tvTitle = (TextView) findViewById(R.id.textview_user_details_activity_name);
        onlineStatusView = (TextView) findViewById(R.id.textview_user_details_activity_description);
        userPhotoView = (ImageView) findViewById(R.id.imageview_user_details_activity_photo);
        openPageButton = (Button) findViewById(R.id.button_user_details_activity_open_page);
        openPageButton.setOnClickListener(this);
    }

    private void setViewsValues() {
        tvTitle.setText(user.first_name + " " + user.last_name);
        onlineStatusView.setText(user.online ? getResources().getString(R.string.all_online_status_true) : "");
        if (bitmapMemoryManager != null) {
            bitmapMemoryManager.setBitmap(user.photo_100, userPhotoView);
        }
    }

    @Override
    public void onClick(View view) {
        String url = USER_PAGE_BASE_URL + user.id;
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }


}
