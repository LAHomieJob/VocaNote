package com.hfad.vocanoteapp;

import android.content.Context;
import android.util.TypedValue;

/*This class provides some common useful methods
 * to other application components*/
public class Utilities {

    public static int convertDip2Pixels(Context context, int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
    }
}
