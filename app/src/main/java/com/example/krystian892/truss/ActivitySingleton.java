package com.example.krystian892.truss;

import android.app.Activity;

/**
 * Created by krystian892 on 3/1/15.
 */
public class ActivitySingleton {
    static Activity activity =null;
    public static Activity getActivity() {
        return activity;
    }

    public static void setActivity(Activity activity) {
        ActivitySingleton.activity = activity;
    }
}
