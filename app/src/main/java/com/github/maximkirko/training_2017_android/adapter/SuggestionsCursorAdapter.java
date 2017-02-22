package com.github.maximkirko.training_2017_android.adapter;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.asynctask.ImageLoadingAsyncTask;
import com.github.maximkirko.training_2017_android.model.User;

/**
 * Created by MadMax on 22.02.2017.
 */

public class SuggestionsCursorAdapter extends CursorAdapter {

    public SuggestionsCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.suggestion_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageView = (ImageView) view.findViewById(R.id.imageview_suggestion_item_image);
        TextView textView = (TextView) view.findViewById(R.id.textview_suggestion_item);

        User user = null;
        try {
            user = parseUserCursor(cursor);
        } catch (Exception e) {
            imageView.setImageResource(R.drawable.ic_query_builder_black_24dp);
            String sugText = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_2));
            textView.setText(sugText);
            return;
        }

        ImageLoadingAsyncTask.newLoader()
                .setImageWidth(context.getResources().getDimensionPixelSize(R.dimen.size_suggestion_item_image))
                .setImageHeight(context.getResources().getDimensionPixelSize(R.dimen.size_suggestion_item_image))
                .setPlaceHolder(R.drawable.all_default_user_image)
                .setTargetView(imageView)
                .load(user.getPhoto_100());
        textView.setText(user.getFirst_name() + " " + user.getLast_name());
    }

    private User parseUserCursor(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getInt(cursor.getColumnIndex("_ID")));
        user.setFirst_name(cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1)));
        user.setLast_name(cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_2)));
        user.setPhoto_100(cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_ICON_1)));
        return user;
    }

    @Override
    public void changeCursor(Cursor cursor) {
        super.swapCursor(cursor);
    }
}
