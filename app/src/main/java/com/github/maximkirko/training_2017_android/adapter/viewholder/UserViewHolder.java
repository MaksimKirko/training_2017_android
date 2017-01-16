package com.github.maximkirko.training_2017_android.adapter.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.memorymanage.BitmapMemoryManager;
import com.github.maximkirko.training_2017_android.model.User;
import com.github.maximkirko.training_2017_android.util.ItemSizeUtils;

import java.lang.ref.WeakReference;

public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView nameView;
    private TextView onlineStatusView;
    private ImageView userPhotoView;

    private int position;
    private BitmapMemoryManager bitmapMemoryManager;

    private WeakReference<UserClickListener> userClickListenerWeakReference;


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

    public UserViewHolder(@NonNull View itemView, @NonNull UserClickListener userClickListener, @NonNull BitmapMemoryManager bitmapMemoryManager) {
        super(itemView);

        itemView.setLayoutParams(ItemSizeUtils.getLayoutParams(itemView.getContext()));
        userClickListenerWeakReference = new WeakReference<>(userClickListener);
        itemView.setOnClickListener(this);

        nameView = (TextView) itemView.findViewById(R.id.textview_friendslist_item_name);
        onlineStatusView = (TextView) itemView.findViewById(R.id.textview_friendslist_item_online_status);
        userPhotoView = (ImageView) itemView.findViewById(R.id.imageview_friendslist_item_photo);

        this.bitmapMemoryManager = bitmapMemoryManager;
    }

    public void onBindData(@NonNull User user, @NonNull int position) {
        bitmapMemoryManager.setBitmap(user.photo_100, userPhotoView);
        nameView.setText(user.first_name + " " + user.last_name);
        onlineStatusView.setText(user.online ? itemView.getResources().getString(R.string.all_online_status_true) : "");
        this.position = position;
    }
}