package com.amorenew.countrypicker.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amorenew.countrypicker.R;
import com.amorenew.countrypicker.Util;
import com.amorenew.countrypicker.models.Country;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by mukesh on 25/04/16.
 */
public class CountryListAdapter extends BaseAdapter {

    List<Country> countries;
    LayoutInflater inflater;
    private Context context;
    private HashMap<String, Bitmap> cachedBitmaps = new HashMap<>();

    public CountryListAdapter(Context context, List<Country> countries) {
        super();
        this.context = context;
        this.countries = countries;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private int getResId(String drawableName) {

        try {
            Class<R.drawable> res = R.drawable.class;
            Field field = res.getField(drawableName);
            int drawableId = field.getInt(null);
            return drawableId;
        } catch (Exception e) {
            Log.e("CountryCodePicker", "Failure to get drawable id.", e);
        }
        return -1;
    }

    @Override
    public int getCount() {
        if (countries != null)
            return countries.size();
        return 0;
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View cellView = convertView;
        Cell cell;
        Country country = countries.get(position);

        if (convertView == null) {
            cell = new Cell();
            cellView = inflater.inflate(R.layout.row, null);
            cell.tvDialCode = (TextView) cellView.findViewById(R.id.tvDialCode);
            cell.tvCountryName = (TextView) cellView.findViewById(R.id.tvCountryName);
            cell.imageView = (ImageView) cellView.findViewById(R.id.row_icon);
            cellView.setTag(cell);
        } else {
            cell = (Cell) cellView.getTag();
        }

        cell.tvDialCode.setText(" (" + country.getDialCode() + ") ");
        cell.tvCountryName.setText(country.getName());

        if (country.getFlagImage() != null && !country.getFlagImage().isEmpty()) {
            int start = country.getFlagImage().lastIndexOf("/") + 1;
            int end = country.getFlagImage().lastIndexOf(".");
            String flagCode = country.getFlagImage().substring(start, end);
            String drawableName = "flag_" + flagCode.toLowerCase();
            int drawableId = getResId(drawableName);
            if (drawableId > 0) {
                country.setFlag(drawableId);
                cell.imageView.setImageResource(drawableId);
            } else {
                loadImageUrl(cell.imageView, country.getFlagImage());
            }
        } else {
            String drawableName = "flag_" + country.getCode().toLowerCase(Locale.ENGLISH);
            int drawableId = getResId(drawableName);
            country.setFlag(drawableId);
            cell.imageView.setImageResource(drawableId);
        }
        return cellView;
    }

    private void loadImageUrl(final ImageView imageView, final String imageUrl) {
        if (cachedBitmaps.get(imageUrl) != null) {
            imageView.setImageBitmap(cachedBitmaps.get(imageUrl));
        } else {
            SimpleTarget target = new SimpleTarget<GlideBitmapDrawable>() {
                @Override
                public void onResourceReady(GlideBitmapDrawable bitmap, GlideAnimation glideAnimation) {
                    // do something with the bitmap
                    // for demonstration purposes, let's just set it to an ImageView
                    imageView.setImageBitmap(bitmap.getBitmap());
                    cachedBitmaps.put(imageUrl, bitmap.getBitmap());
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);
                    imageView.setImageResource(R.drawable.error_flag);
                }
            };
            Util.getInstance().loadImage(context, imageUrl, target, R.drawable.error_flag);
        }
    }

    static class Cell {
        public TextView tvDialCode;
        public TextView tvCountryName;
        public ImageView imageView;
    }
}