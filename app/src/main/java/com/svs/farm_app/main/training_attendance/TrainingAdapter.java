package com.svs.farm_app.main.training_attendance;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.svs.farm_app.R;
import com.svs.farm_app.entities.OfficerTraining;

import java.util.List;

/**
 * Created by ADMIN on 23-Feb-17.
 */
public class TrainingAdapter extends ArrayAdapter<OfficerTraining> {

    private Context mContext;
    private List<OfficerTraining> trainingList;

    public TrainingAdapter(Context context, int resource, List<OfficerTraining> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.trainingList = objects;
    }

    @Override
    public int getCount() {
        return trainingList.size();
    }

    @Override
    public OfficerTraining getItem(int position) {
        return trainingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_training, parent, false);
            
            holder = new ViewHolder(convertView);
            
            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvTraining.setText(getItem(position).getTrainCat());

        String firstLetter = String.valueOf(getItem(position).getTrainCat().charAt(0));

        ColorGenerator generator = ColorGenerator.MATERIAL;
        
        int color = generator.getColor(getItem(position));

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(firstLetter, color);

        holder.ivTrainingIcon.setImageDrawable(drawable);

        return convertView;
    }

    private class ViewHolder {
        private ImageView ivTrainingIcon;
        private TextView tvTraining;

        public ViewHolder(View v) {
            ivTrainingIcon = (ImageView) v.findViewById(R.id.ivTrainingIcon);
            tvTraining = (TextView) v.findViewById(R.id.tvTraining);
        }
    }
}
