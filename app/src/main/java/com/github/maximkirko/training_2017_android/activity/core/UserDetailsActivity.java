package com.github.maximkirko.training_2017_android.activity.core;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.contentprovider.FriendsContentProvider;
import com.github.maximkirko.training_2017_android.asynctask.ImageLoadingAsyncTask;
import com.github.maximkirko.training_2017_android.mapper.UserMapper;
import com.github.maximkirko.training_2017_android.model.User;

/**
 * Created by MadMax on 25.12.2016.
 */

public class UserDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    // region Views
    private Toolbar toolbar;
    private TextView tvTitle;
    private TextView onlineStatusView;
    private ImageView userPhotoView;
    private Button openPageButton;
    // endregion

    private User user;

    private static final String USER_PAGE_BASE_URL = "https://vk.com/id";
    public static final String USER_EXTRA = "USER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_details_activity);
        initToolbar();
        getIntentExtras();
        initViews();
        setViewsValues();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_user_details_activity);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void getIntentExtras() {
        int id = this.getIntent().getIntExtra(USER_EXTRA, -1);
        initUser(id);
    }

    private void initUser(int id) {
        Uri uri = ContentUris.withAppendedId(FriendsContentProvider.FRIENDS_CONTENT_URI, id);
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor.moveToNext()) {
            user = UserMapper.convert(cursor);
        }
    }

    private void initViews() {
        tvTitle = (TextView) findViewById(R.id.textview_user_details_activity_name);
        onlineStatusView = (TextView) findViewById(R.id.textview_user_details_activity_description);
        userPhotoView = (ImageView) findViewById(R.id.imageview_user_details_activity_photo);
        openPageButton = (Button) findViewById(R.id.button_user_details_activity_open_page);
        openPageButton.setOnClickListener(this);
    }

    private void setViewsValues() {
        ImageLoadingAsyncTask.newLoader()
                .setTargetView(userPhotoView)
                .setPlaceHolder(R.drawable.all_default_user_image)
                .setImageHeight(userPhotoView.getHeight())
                .setImageWidth(userPhotoView.getWidth())
                .load(user.getPhoto_100());

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            startFriendsListActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startFriendsListActivity() {
        Intent intent = new Intent(this, FriendsListActivity.class);
        startActivity(intent);
    }
}
