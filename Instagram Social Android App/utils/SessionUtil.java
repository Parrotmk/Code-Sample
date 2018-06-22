package com.whaddyalove.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


public class SessionUtil {
    public static final String USER_SESSION_PREF = "userSessionPref";
    public static final String USER_NAME = "userName";
    public static final String USER_ID = "userId";

    public static SharedPreferences getUserSessionPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return context.getSharedPreferences(USER_SESSION_PREF,
                Context.MODE_PRIVATE);
    }

    public static void saveUserId(long userId, Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(USER_ID, userId);
        editor.commit();
    }

    public static long getUserId(Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        return preferences.getLong(USER_ID, 0);
    }

    public static void saveUserName(String userName, Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_NAME, userName);
        editor.commit();
    }

    public static String getUserName(Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        return preferences.getString(USER_NAME, "");
    }

    public static void logOut(Context context) {
        SharedPreferences preferences = getUserSessionPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(USER_NAME);
        editor.remove(USER_ID);
        editor.commit();
    }
}
