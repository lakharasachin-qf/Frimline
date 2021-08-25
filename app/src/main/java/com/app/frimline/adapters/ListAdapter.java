package com.app.frimline.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.List;

public class ListAdapter<T>
        extends ArrayAdapter<T> {
    public List<T> items;
    private final Filter filter = new KNoFilter();

    public ListAdapter(Context context, int textViewResourceId,
                       List<T> objects) {
        super(context, textViewResourceId, objects);

        items = objects;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public void updateReceiptsList(List<T> newlist) {
        items.clear();
        items.addAll(newlist);
        this.notifyDataSetChanged();
    }

    private class KNoFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence arg0) {
            FilterResults result = new FilterResults();
            result.values = items;
            result.count = items.size();
            return result;
        }

        @Override
        protected void publishResults(CharSequence arg0, FilterResults arg1) {
            notifyDataSetChanged();
        }
    }
}