package com.amorenew.countrypicker;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.DrawableRes;
import android.support.annotation.RawRes;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.InputStream;
import java.util.Locale;

/**
 * Created by TCIG_PC_54 on 9/22/2016.
 */

public class Util {
    private static Util instance;

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

    public static Util getInstance() {
        if (instance == null)
            instance = new Util();
        return instance;
    }

    /**
     * load image from url and fill it in image view
     *
     * @param context
     * @param imageUrl    the image link
     * @param target
     * @param imageHolder this drawable for place holder image
     */
    public Request loadImage(Context context, final String imageUrl, SimpleTarget target, @DrawableRes int imageHolder) {

        if (imageUrl != null) {
            Log.e("loadImage", "imageUrl:" + imageUrl);
//            if (cachedBitmaps.containsKey(imageUrl)) {
//                imageView.setImageBitmap(cachedBitmaps.get(imageUrl));
//                return null;
//            }
        }
        GlideUrl glideUrl = new GlideUrl(imageUrl, new LazyHeaders.Builder()
                //.addHeader("Authorization", Util.getInstance().getAuth())
                .build());
        if (imageHolder == 0)
            imageHolder = R.drawable.error_flag;
//        Drawable drawableHolder = Util.getVectorDrawable(context, R.drawable.ic_maff);

//        SSLCertifications.trustAll();
//        SimpleTarget target = new SimpleTarget<GlideBitmapDrawable>() {
//            @Override
//            public void onResourceReady(GlideBitmapDrawable bitmap, GlideAnimation glideAnimation) {
//                // do something with the bitmap
//                // for demonstration purposes, let's just set it to an ImageView
//                imageView.setImageBitmap(bitmap.getBitmap());
//                cachedBitmaps.put(imageUrl, bitmap.getBitmap());
//            }
//
//            @Override
//            public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                super.onLoadFailed(e, errorDrawable);
//                imageView.setImageResource(R.drawable.ic_maff);
//            }
//        };
        return Glide.with(context)
                .load(glideUrl).diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(imageHolder)
                .into(target).getRequest();
    }
}
