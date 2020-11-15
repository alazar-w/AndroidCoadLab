package com.example.asynctaskandasynctaskloader;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.example.asynctaskandasynctaskloader.util.NetworkUtils;


//When you use an AsyncTask to perform operations in the background,
// that background thread can't update the UI if a configuration change occurs while the background task is running.
//To address this situation, use the AsyncTaskLoader class.
//
//AsyncTaskLoader loads data in the background and reassociates background tasks with the Activity,
//even after a configuration change. With an AsyncTaskLoader, if you rotate the device while the task is running,
//the results are still displayed correctly in the Activity.
public class BookLoader extends AsyncTaskLoader<String> {
    private String mQueryString;

    BookLoader(Context context, String queryString) {
        super(context);
        mQueryString = queryString;
    }

    @Nullable
    @Override
    public String loadInBackground() {
        return NetworkUtils.getBookInfo(mQueryString);
    }

    //The system calls this method when you start the loader.
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
    }
}
