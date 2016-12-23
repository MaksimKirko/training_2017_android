package com.github.maximkirko.training_2017_android;

/**
 * Created by MadMax on 21.12.2016.
 */

public enum SpinnerPicturesEnum {
    CAT(R.drawable.cat),
    DOG(R.drawable.dog),
    RACOON(R.drawable.racoon);

    private int mResourceId;

    private SpinnerPicturesEnum(int id) {
        mResourceId = id;
    }

    public static int getResourceIdByPosition(int pos) {
        SpinnerPicturesEnum spe = values()[pos];
        return spe.mResourceId;
    }
}
