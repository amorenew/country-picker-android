package com.mukesh.countrypicker;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.RawRes;
import android.util.Log;

import java.io.InputStream;
import java.util.Locale;

/**
 * Created by TCIG_PC_54 on 9/22/2016.
 */

public class Util {

    public static String getRawString(Context context, @RawRes int jsonId) {
        String json = "";
        try {
            Resources res = context.getResources();
            InputStream in_s = res.openRawResource(jsonId);
            byte[] b = new byte[in_s.available()];
            in_s.read(b);
            json = new String(b);
        } catch (Exception e) {
            Log.d("raw", e.getMessage());
        }
        return json;
    }

    /**
     * convert english number to Arabic number
     *
     * @param numberText the english number text
     * @return
     */
    public static String getLocalNumber(int numberText) {
        String number = String.valueOf(numberText);
        if (!isRTL())
            return number;
        char[] arabicChars = {'٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < number.length(); i++) {
            if (Character.isDigit(number.charAt(i))) {
                builder.append(arabicChars[(int) (number.charAt(i)) - 48]);
            } else {
                builder.append(number.charAt(i));
            }
        }
        return builder.toString();
    }

    public static boolean isRTL() {
        return isRTL(Locale.getDefault());
    }

    public static boolean isRTL(Locale locale) {
        final int directionality = Character.getDirectionality(locale.getDisplayName().charAt(0));
        return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT ||
                directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;
    }
}
