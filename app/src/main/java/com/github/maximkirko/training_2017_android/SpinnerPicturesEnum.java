package com.github.maximkirko.training_2017_android;

/**
 * Created by MadMax on 21.12.2016.
 */

public enum SpinnerPicturesEnum {
    CAT(R.drawable.ic_cat),
    DOG(R.drawable.ic_dog),
    RACOON(R.drawable.ic_racoon);

    private int mResourceId;

    private SpinnerPicturesEnum(int id) {
        mResourceId = id;
    }

    public static int getResourceIdByPosition(int pos) {
        SpinnerPicturesEnum spe = values()[pos];
        return spe.mResourceId;
    }
}
