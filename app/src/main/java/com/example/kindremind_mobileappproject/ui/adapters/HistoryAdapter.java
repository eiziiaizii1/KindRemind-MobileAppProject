package com.example.kindremind_mobileappproject.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kindremind_mobileappproject.R;
import com.example.kindremind_mobileappproject.model.CompletedDeed;
import com.example.kindremind_mobileappproject.model.Deed;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Custom adapter for displaying completed deeds in a ListView
 */
public class HistoryAdapter extends ArrayAdapter<CompletedDeedWithDetails> {

    private Context context;
    private List<CompletedDeedWithDetails> completedDeeds;

    public HistoryAdapter(Context context, List<CompletedDeedWithDetails> completedDeeds) {
        super(context, R.layout.list_item_deed_history, completedDeeds);
        this.context = context;
        this.completedDeeds = completedDeeds;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            // Inflate the layout
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_item_deed_history, parent, false);

            // Set up the ViewHolder
            holder = new ViewHolder();
            holder.categoryIcon = convertView.findViewById(R.id.icon_category);
            holder.deedText = convertView.findViewById(R.id.text_deed);
            holder.dateText = convertView.findViewById(R.id.text_date);
            holder.noteText = convertView.findViewById(R.id.text_note);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the deed for this position
        CompletedDeedWithDetails item = completedDeeds.get(position);

        // Set data to views
        holder.categoryIcon.setImageResource(item.getCategoryIconResourceId());
        holder.deedText.setText(item.getDeedText());

        // Format the date
        String formattedDate = formatDate(item.getDate());
        holder.dateText.setText(formattedDate);

        // Show note if available
        if (item.getNote() != null && !item.getNote().isEmpty()) {
            holder.noteText.setText(item.getNote());
            holder.noteText.setVisibility(View.VISIBLE);
        } else {
            holder.noteText.setVisibility(View.GONE);
        }

        return convertView;
    }

    /**
     * Format the date from yyyy-MM-dd to a more readable format
     */
    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
            Date date = inputFormat.parse(dateStr);
            return outputFormat.format(date);
        } catch (ParseException e) {
            // If parsing fails, return the original string
            return dateStr;
        }
    }

    /**
     * ViewHolder pattern for smooth scrolling
     */
    private static class ViewHolder {
        ImageView categoryIcon;
        TextView deedText;
        TextView dateText;
        TextView noteText;
    }
}