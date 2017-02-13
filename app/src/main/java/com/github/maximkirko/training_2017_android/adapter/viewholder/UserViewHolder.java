package com.github.maximkirko.training_2017_android.adapter.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.asynctask.ImageLoadingAsyncTask;
import com.github.maximkirko.training_2017_android.model.User;
import com.github.maximkirko.training_2017_android.util.ItemSizeUtils;

import java.lang.ref.WeakReference;

public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView nameView;
    private TextView onlineStatusView;
    private ImageView userPhotoView;
    private CheckBox isFavoriteView;
    private TextView raitingView;

    private int userId;
    private WeakReference<UserClickListener> userClickListenerWeakReference;
    private ImageLoadingAsyncTask imageLoadingAsyncTask;

    @Override
    public void onClick(View v) {
        UserClickListener userClickListener = this.userClickListenerWeakReference.get();
        if (userClickListener != null) {
            userClickListener.onItemClick(userId);
        }
    }

    public UserViewHolder(@NonNull View itemView, @NonNull UserClickListener userClickListener) {
        super(itemView);
        itemView.setLayoutParams(ItemSizeUtils.getLayoutParams(itemView.getContext()));
        userClickListenerWeakReference = new WeakReference<>(userClickListener);
        itemView.setOnClickListener(this);
        initViews();
    }

    private void initViews() {
        nameView = (TextView) itemView.findViewById(R.id.textview_friendslist_item_name);
        onlineStatusView = (TextView) itemView.findViewById(R.id.textview_friendslist_item_online_status);
        userPhotoView = (ImageView) itemView.findViewById(R.id.imageview_friendslist_item_photo);
        isFavoriteView = (CheckBox) itemView.findViewById(R.id.checkbox_friendslist_item_is_favorite);
        raitingView = (TextView) itemView.findViewById(R.id.textview_friendslist_item_raiting);
    }

    public void onBindData(@NonNull User user) {
        imageLoadingAsyncTask = ImageLoadingAsyncTask.newLoader()
                .setTargetView(userPhotoView)
                .setPlaceHolder(R.drawable.all_default_user_image)
                .setImageHeight(itemView.getResources().getDimensionPixelSize(R.dimen.size_friendslist_item_image))
                .setImageWidth(itemView.getResources().getDimensionPixelSize(R.dimen.size_friendslist_item_image))
                .load(user.getPhoto_100());
        this.userId = user.getId();
        setViewsValues(user);
    }

    private void setViewsValues(User user) {
        nameView.setText(user.getFirst_name() + " " + user.getLast_name());
        onlineStatusView.setText(user.isOnline() ? itemView.getResources().getString(R.string.all_online_status_true) : "");
        isFavoriteView.setChecked(user.is_favorite());
        int rating = user.getRating();
        if (rating != 0) {
            raitingView.setText(rating > 0 ? "+" + rating : rating + "");
        }
    }

    public void cancelTask() {
        imageLoadingAsyncTask.cancel(true);
    }
}