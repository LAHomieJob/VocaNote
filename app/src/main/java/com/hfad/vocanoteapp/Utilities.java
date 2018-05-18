package com.hfad.vocanoteapp;

import android.content.Context;
import android.util.TypedValue;
import android.widget.Toast;

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
}
