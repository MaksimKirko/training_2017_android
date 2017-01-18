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
import com.github.maximkirko.training_2017_android.bitmapmemorymanager.BitmapMemoryManagerConfigurator;
import com.github.maximkirko.training_2017_android.loader.ImageLoader;
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
    private BitmapMemoryManagerConfigurator bitmapMemoryManagerConfigurator;

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
    }

    private void initViews() {
        tvTitle = (TextView) findViewById(R.id.textview_user_details_activity_name);
        onlineStatusView = (TextView) findViewById(R.id.textview_user_details_activity_description);
        userPhotoView = (ImageView) findViewById(R.id.imageview_user_details_activity_photo);
        openPageButton = (Button) findViewById(R.id.button_user_details_activity_open_page);
        openPageButton.setOnClickListener(this);
    }

    private void setViewsValues() {
        ImageLoader imageLoader = ImageLoader.newBuilder()
                .setTargetView(userPhotoView)
                .setPlaceHolder(R.drawable.all_default_user_image)
                .setImageHeight(userPhotoView.getHeight())
                .setImageWidth(userPhotoView.getWidth())
                .build();
        imageLoader.execute(new String[]{user.getPhoto_100()});

        tvTitle.setText(user.getFirst_name() + " " + user.getLast_name());
        onlineStatusView.setText(user.isOnline() ? getResources().getString(R.string.all_online_status_true) : "");
    }

    @Override
    public void onClick(View view) {
        String url = USER_PAGE_BASE_URL + user.getId();
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }
}
