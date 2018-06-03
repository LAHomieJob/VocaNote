package com.hfad.vocanoteapp;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.util.TypedValue;
import android.widget.Toast;

import java.util.Locale;

/*This class provides some common useful methods
 * to other application components*/
public class Utilities {

    public static int convertDip2Pixels(Context context, int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
    }

    public static Toast initializeToast(Context context, Toast toast, String res) {
        cancelToast(toast);
        return Toast.makeText(context, res, Toast.LENGTH_SHORT);
    }

    public static void cancelToast(Toast toast) {
        if (toast != null) {
            toast.cancel();
        }
    }

    public static Locale chooseLang(Context context, String language){
        ArrayMap<String, Locale> availableLangs = new ArrayMap<>();
        availableLangs.put(context.getResources().getStringArray(R.array.spinner_values)[0], Locale.UK);
        availableLangs.put(context.getResources().getStringArray(R.array.spinner_values)[1], new Locale("spa", "ESP"));
        availableLangs.put(context.getResources().getStringArray(R.array.spinner_values)[2], Locale.ITALY);
        availableLangs.put(context.getResources().getStringArray(R.array.spinner_values)[3], Locale.FRANCE);
        availableLangs.put(context.getResources().getStringArray(R.array.spinner_values)[4], Locale.GERMANY);
        return availableLangs.get(language);
    }
}
