package com.svs.farm_app.main.dashboard;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.svs.farm_app.R;

/**
 * Created by Benson on 3/16/2015.
 */

public class ImageAdapter extends BaseAdapter {

    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        ImageView imageView;
//        if (convertView == null) {
//            imageView = new ImageView(mContext);
//
//
//            DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
//            int width = metrics.widthPixels;
//            int height = metrics.heightPixels;
//            Log.i("Height:", "" + height);
//            Log.i("Width:", "" + width);
//            imageView.setLayoutParams(new GridView.LayoutParams((width - 32) / 3, (width - 32) / 3));
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageView.setPadding(2, 2, 2, 2);
//        } else {
//            imageView = (ImageView) convertView;
//        }

        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.dashboard_item, parent, false);

            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        /*DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();

        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        holder.ivIcon.setLayoutParams(new GridView.LayoutParams((width - 32) / 3, (width - 32) / 3));*/

        holder.ivIcon.setImageResource(mThumbIds[position]);
        holder.tvTitle.setText(mIconTitles[position]);
        return convertView;
    }

//    public Integer[] mThumbIds = {
//            R.drawable.news, R.drawable.show_intent, R.drawable.sign_doc,
//            R.drawable.mapping, R.drawable.update_area, R.drawable.calender,
//            R.drawable.train_mat, R.drawable.train, R.drawable.inputs,
//            R.drawable.forms, R.drawable.soda, R.drawable.bio_recapture
//    };

    public Integer[] mThumbIds = {
            R.drawable.ic_registration, R.drawable.ic_baseline_edit_24, R.drawable.ic_show_intent, R.drawable.ic_sign_doc,
            R.drawable.ic_mapping, R.drawable.ic_update_farm_area, R.drawable.ic_calendar,
            R.drawable.ic_training_materials, R.drawable.ic_training_attendance, R.drawable.ic_farm_inputs,
            R.drawable.ic_farm_assessment, R.drawable.ic_soda, R.drawable.ic_bio_recapture,
            R.drawable.ic_recovery, R.drawable.ic_sign_doc
    };

    public Integer[] mIconTitles = {
            R.string.registration, R.string.edit_farmer, R.string.show_intent, R.string.sign_documents,
            R.string.mapping, R.string.update_farm_area, R.string.calendar,
            R.string.training_materials, R.string.training, R.string.farm_inputs,
            R.string.farm_assessment, R.string.soda, R.string.bio_recapture,R.string.recovery,R.string.Survey
    };

    private class ViewHolder {
        private ImageView ivIcon;
        private TextView tvTitle;

        public ViewHolder(View v) {
            ivIcon = (ImageView) v.findViewById(R.id.ivIcon);
            tvTitle = (TextView) v.findViewById(R.id.tvTitle);
            tvTitle.setSelected(true);
        }
    }
}
