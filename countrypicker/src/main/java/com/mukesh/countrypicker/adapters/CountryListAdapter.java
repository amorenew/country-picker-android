package com.mukesh.countrypicker.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mukesh.countrypicker.R;
import com.mukesh.countrypicker.models.Country;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

/**
 * Created by mukesh on 25/04/16.
 */
public class CountryListAdapter extends BaseAdapter {

    List<Country> countries;
    LayoutInflater inflater;
    private Context context;

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

        cell.tvDialCode.setText(country.getDialCode());
        cell.tvCountryName.setText(country.getName());

        String drawableName = "flag_" + country.getCode().toLowerCase(Locale.ENGLISH);
        int drawableId = getResId(drawableName);

        country.setFlag(drawableId);
        cell.imageView.setImageResource(drawableId);
        return cellView;
    }

    static class Cell {
        public TextView tvDialCode;
        public TextView tvCountryName;
        public ImageView imageView;
    }
}