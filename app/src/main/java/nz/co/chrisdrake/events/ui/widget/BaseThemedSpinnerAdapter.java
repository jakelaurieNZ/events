package nz.co.chrisdrake.events.ui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public abstract class BaseThemedSpinnerAdapter extends BaseAdapter implements ThemedSpinnerAdapter {

    private final ThemedSpinnerAdapter.Helper dropDownHelper;

    public BaseThemedSpinnerAdapter(Context context) {
        dropDownHelper = new ThemedSpinnerAdapter.Helper(context);
    }

    @Override public void setDropDownViewTheme(Resources.Theme theme) {
        dropDownHelper.setDropDownViewTheme(theme);
    }

    @Nullable @Override public Resources.Theme getDropDownViewTheme() {
        return dropDownHelper.getDropDownViewTheme();
    }

    protected LayoutInflater getDropDownViewInflater() {
        return dropDownHelper.getDropDownViewInflater();
    }

    @Override public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null || !(convertView instanceof TextView)) {
            convertView =
                getDropDownViewInflater().inflate(android.R.layout.simple_spinner_dropdown_item,
                    parent, false);
        }

        ((TextView) convertView).setText(getName(position));
        return convertView;
    }

    public String getName(int position) {
        return String.valueOf(getItem(position));
    }
}
