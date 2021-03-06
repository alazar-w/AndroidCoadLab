package com.example.asynctaskandasynctaskloader;

import android.os.AsyncTask;
import android.widget.TextView;

import com.example.asynctaskandasynctaskloader.util.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;


//USING ASYNC TASK
// <String, Void, String>.(String because the query is a string, Void because there is no progress indicator, and String because the JSON response is a string.)
public class FetchBook extends AsyncTask<String, Void, String> {
    //The weak references prevent memory leaks by allowing the object held by that reference to be garbage-collected if necessary.
    private WeakReference<TextView> mTitleText;
    private WeakReference<TextView> mAuthorText;

    FetchBook(TextView titleText, TextView authorText) {
        this.mTitleText = new WeakReference<>(titleText);
        this.mAuthorText = new WeakReference<>(authorText);
    }

    @Override
    protected String doInBackground(String... strings) {
        //The search term is the first value in the strings array.
        return NetworkUtils.getBookInfo(strings[0]);

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            //classes JSONObject and JSONArray are to obtain the JSON array of items from the result string.
            JSONObject jsonObject = new JSONObject(s);
            JSONArray itemsArray = jsonObject.getJSONArray("items");

            //Initialize the variables used for the parsing loop.
            int i = 0;
            String title = null;
            String authors = null;

            //Iterate through the itemsArray array, checking each book for title and author information
            while (i < itemsArray.length() &&
                    (authors == null && title == null)) {
                // Get the current item information.
                JSONObject book = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                // Try to get the author and title from the current item,
                // catch if either field is empty and move on.
                try {
                    title = volumeInfo.getString("title");
                    authors = volumeInfo.getString("authors");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Move to the next item.
                i++;
            }

            // If both are found, display the result.
            if (title != null && authors != null) {
                mTitleText.get().setText(title);
                mAuthorText.get().setText(authors);
            } else {
                // If none are found, update the UI to
                // show failed results.
                mTitleText.get().setText(R.string.no_results);
                mAuthorText.get().setText("");
            }

        } catch (JSONException e) {
            // If onPostExecute does not receive a proper JSON string,
            // update the UI to show failed results.
            mTitleText.get().setText(R.string.no_results);
            mAuthorText.get().setText("");
        }
    }
}