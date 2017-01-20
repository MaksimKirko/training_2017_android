package com.github.maximkirko.training_2017_android.adapter.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.loader.ImageLoader;
import com.github.maximkirko.training_2017_android.model.User;
import com.github.maximkirko.training_2017_android.util.ItemSizeUtils;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;

public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView nameView;
    private TextView onlineStatusView;
    private ImageView userPhotoView;

    private int position;
    private WeakReference<UserClickListener> userClickListenerWeakReference;
    private ImageLoader imageLoader;
    private ExecutorService executorService;

    @Override
    public void onClick(View v) {
        UserClickListener userClickListener = this.userClickListenerWeakReference.get();
        if (userClickListener != null) {
            userClickListener.onItemClick(getUserPositionByClick());
        }
    }

    private int getUserPositionByClick() {
        return position;
    }

    public UserViewHolder(@NonNull View itemView, @NonNull UserClickListener userClickListener, @NonNull ExecutorService executorService) {
        super(itemView);

        itemView.setLayoutParams(ItemSizeUtils.getLayoutParams(itemView.getContext()));
        userClickListenerWeakReference = new WeakReference<>(userClickListener);
        itemView.setOnClickListener(this);
        this.executorService = executorService;

        nameView = (TextView) itemView.findViewById(R.id.textview_friendslist_item_name);
        onlineStatusView = (TextView) itemView.findViewById(R.id.textview_friendslist_item_online_status);
        userPhotoView = (ImageView) itemView.findViewById(R.id.imageview_friendslist_item_photo);
    }

    public void onBindData(@NonNull User user, @NonNull int position) {
        imageLoader = ImageLoader.newLoader()
                .setTargetView(userPhotoView)
                .setExecutorService(executorService)
                .setPlaceHolder(R.drawable.all_default_user_image)
                .setImageHeight(itemView.getResources().getDimensionPixelSize(R.dimen.size_friendslist_item_image))
                .setImageWidth(itemView.getResources().getDimensionPixelSize(R.dimen.size_friendslist_item_image))
                .load(user.getPhoto_100());

        nameView.setText(user.getFirst_name() + " " + user.getLast_name());
        onlineStatusView.setText(user.isOnline() ? itemView.getResources().getString(R.string.all_online_status_true) : "");
        this.position = position;
    }

    public void cancelTask() {
        imageLoader.cancel(true);
    }
}