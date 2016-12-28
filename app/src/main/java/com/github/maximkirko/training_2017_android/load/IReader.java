package com.github.maximkirko.training_2017_android.load;

import android.content.Context;

import java.io.IOException;
import java.util.List;

/**
 * Created by MadMax on 28.12.2016.
 */

public interface IReader<T> {

    List<T> readToList(Context context) throws IOException;
}
