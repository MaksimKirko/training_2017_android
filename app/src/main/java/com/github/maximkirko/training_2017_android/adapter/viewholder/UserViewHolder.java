package com.github.maximkirko.training_2017_android.adapter.viewholder;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.model.User;
import com.github.maximkirko.training_2017_android.util.ItemSizeUtils;

import java.lang.ref.WeakReference;

public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView nameView;
    private TextView onlineStatusView;
    private ImageView userPhotoView;

    private WeakReference<UserClickListener> userClickListener;

    @Override
    public void onClick(View v) {
        UserClickListener userClickListener = this.userClickListener.get();
        if (userClickListener != null) {
            userClickListener.onItemClick(getUserPositionByClick());
        }
    }

    private int getUserPositionByClick() {
        return getAdapterPosition() - 1;
    }

    public UserViewHolder(@NonNull View itemView, @NonNull UserClickListener userClickListener) {
        super(itemView);

        itemView.setLayoutParams(ItemSizeUtils.getLayoutParams(itemView.getContext()));
        this.userClickListener = new WeakReference<>(userClickListener);
        itemView.setOnClickListener(this);

        this.nameView = (TextView) itemView.findViewById(R.id.textview_friendslist_item_name);
        this.onlineStatusView = (TextView) itemView.findViewById(R.id.textview_friendslist_item_online_status);
        this.userPhotoView = (ImageView) itemView.findViewById(R.id.imageview_friendslist_item_photo);
    }

    public void onBindData(@NonNull String first_name, @NonNull String last_name, @NonNull boolean online, @Nullable Bitmap userPhoto) {
        this.nameView.setText(first_name + " " + last_name);
        this.onlineStatusView.setText(online ? "Online" : "");
        this.userPhotoView.setImageBitmap(userPhoto);
    }
}