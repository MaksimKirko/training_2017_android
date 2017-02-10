package com.github.maximkirko.training_2017_android.reader;

import java.io.IOException;
import java.util.List;

/**
 * Created by MadMax on 28.12.2016.
 */

public interface Reader<T> {

    T read() throws IOException;

    List<T> readToList() throws IOException;
}
