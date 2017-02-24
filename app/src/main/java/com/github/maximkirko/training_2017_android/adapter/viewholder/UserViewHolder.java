package com.github.maximkirko.training_2017_android.adapter.viewholder;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.asynctask.ImageLoadingAsyncTask;
import com.github.maximkirko.training_2017_android.model.User;
import com.github.maximkirko.training_2017_android.util.ItemSizeUtils;

import java.lang.ref.WeakReference;

public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private TextView nameView;
    private TextView onlineStatusView;
    private ImageView userPhotoView;
    private CheckBox isFavoriteView;
    private TextView raitingView;

    private int userId;
    private WeakReference<UserClickListener> userClickListenerWeakReference;
    private WeakReference<CheckBoxOnChangeListener> checkBoxOnChangeListenerWeakReference;
    private ImageLoadingAsyncTask imageLoadingAsyncTask;
    private boolean isFavorite;

    @Override
    public void onClick(View v) {
        if (userClickListenerWeakReference != null) {
            userClickListenerWeakReference.get().onItemClick(userId);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (userClickListenerWeakReference != null) {
            userClickListenerWeakReference.get().onItemLongClick(userId);
        }
        return true;
    }

    public UserViewHolder(@NonNull View itemView, @NonNull UserClickListener userClickListener, @NonNull CheckBoxOnChangeListener checkBoxOnChangeListener) {
        super(itemView);
        itemView.setLayoutParams(ItemSizeUtils.getLayoutParams(itemView.getContext()));
        userClickListenerWeakReference = new WeakReference<>(userClickListener);
        checkBoxOnChangeListenerWeakReference = new WeakReference<>(checkBoxOnChangeListener);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        initViews();
    }

    private void initViews() {
        nameView = (TextView) itemView.findViewById(R.id.textview_friendslist_item_name);
        onlineStatusView = (TextView) itemView.findViewById(R.id.textview_friendslist_item_online_status);
        userPhotoView = (ImageView) itemView.findViewById(R.id.imageview_friendslist_item_photo);
        isFavoriteView = (CheckBox) itemView.findViewById(R.id.checkbox_friendslist_item_is_favorite);
        isFavoriteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFavoriteView.setChecked(!isFavorite);
                if (checkBoxOnChangeListenerWeakReference != null) {
                    checkBoxOnChangeListenerWeakReference.get().onChecked(userId, isFavoriteView.isChecked());
                }
            }
        });
        raitingView = (TextView) itemView.findViewById(R.id.textview_friendslist_item_raiting);
    }

    public void onBindData(@NonNull User user, @Nullable String query) {
        imageLoadingAsyncTask = ImageLoadingAsyncTask.newLoader()
                .setTargetView(userPhotoView)
                .setPlaceHolder(R.drawable.all_default_user_image)
                .setImageHeight(itemView.getResources().getDimensionPixelSize(R.dimen.size_friendslist_item_image))
                .setImageWidth(itemView.getResources().getDimensionPixelSize(R.dimen.size_friendslist_item_image))
                .load(user.getPhoto_100());
        this.userId = user.getId();
        setViewsValues(user, query);
    }

    private void setViewsValues(@NonNull User user, @Nullable String query) {
        Spannable name = new SpannableString(user.getFirst_name() + " " + user.getLast_name());
        if (query != null) {
            name = getHighlightedText(name, query);
        }
        nameView.setText(name);
        onlineStatusView.setText(user.isOnline() ? itemView.getResources().getString(R.string.all_online_status_true) : "");
        isFavorite = user.is_favorite();
        isFavoriteView.setChecked(user.is_favorite());
        int rating = user.getRating();
        if (rating != 0) {
            raitingView.setText(rating > 0 ? "+" + rating : rating + "");
        }
    }

    private Spannable getHighlightedText(@NonNull Spannable spannable, @NonNull String query) {
        int start = spannable.toString().toLowerCase().indexOf(query.toLowerCase());
        spannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(itemView.getContext(), R.color.colorPrimaryDark)), start, start + query.length(), 0);
        return spannable;
    }

    public void cancelTask() {
        imageLoadingAsyncTask.cancel(true);
    }
}