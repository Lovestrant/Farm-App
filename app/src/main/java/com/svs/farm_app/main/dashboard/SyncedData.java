package com.svs.farm_app.main.dashboard;

import android.app.Activity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.svs.farm_app.R;
import com.svs.farm_app.utils.BaseClass;
import com.svs.farm_app.utils.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SyncedData extends BaseClass {
	private DatabaseHandler db;
	private SyncedArray syncedArray;
	List<SyncedArray> list;
	ListView listView;
	@BindView(R.id.toolbar)
	Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_synced_data);
		ButterKnife.bind(this);

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(true);

		db = new DatabaseHandler(getApplicationContext());
		list = new ArrayList<>();
		CreateList();

		CustomListAdapter adapter = new CustomListAdapter(this, list);
		listView = (ListView) findViewById(R.id.list);
		(listView).setAdapter(adapter);
	}

	public void CreateList() {
		CreateItem("Farmers", db.getTableCount(db.TABLE_FARMERS));
		CreateItem("Mapped Farms", db.getTableCount(db.TABLE_MAPPED_FARMS));
		CreateItem("Assigned AssignedTrainings", db.getTableCount(db.TABLE_ASSIGNED_TRAININGS));
		CreateItem("Collected Inputs", db.getTableCount(db.TABLE_COLLECTED_INPUTS));
		CreateItem("Intent Shown", db.getTableCount(db.TABLE_SHOW_INTENT));
		CreateItem("Updated Farm areas",db.getTableCount(db.TABLE_FARM_AREA_UPDATES));
		CreateItem("Tranport Hse - Market", db.getTableCount(db.TABLE_TRANS_HSE_TO_MARKET));
		CreateItem("Signed Documents", db.getTableCount(db.TABLE_SIGNED_DOCS));
		CreateItem("Farmers Time", db.getTableCount(db.TABLE_FARMER_TIMES));
		CreateItem("Farmers Income", db.getTableCount(db.TABLE_FARM_INCOME));
		CreateItem("Farm Production", db.getTableCount(db.TABLE_FARM_PRODUCTION));
		CreateItem("Other Crops",db.getTableCount(db.TABLE_FARM_OTHER_CROPS));
		CreateItem("Finger One", db.getTableCount(db.TABLE_FINGER_ONE));
		CreateItem("Finger Two", db.getTableCount(db.TABLE_FINGER_TWO));
		CreateItem("Finger Three", db.getTableCount(db.TABLE_FINGER_THREE));
		CreateItem("Finger Four", db.getTableCount(db.TABLE_FINGER_FOUR));
		CreateItem("Finger Five", db.getTableCount(db.TABLE_FINGER_FIVE));		
		CreateItem("Assment Forms(Major)", db.getTableCount(db.TABLE_FARM_ASS_MAJOR));
		CreateItem("Assment Forms(Minor)", db.getTableCount(db.TABLE_FARM_ASS_MEDIUM));
		CreateItem("Germination", db.getTableCount(db.TABLE_GERMINATION));
		CreateItem("Molasses", db.getTableCount(db.TABLE_MOLASSES_TRAP_CATCHES));
		CreateItem("Scouting", db.getTableCount(db.TABLE_SCOUTING));
		CreateItem("Yield", db.getTableCount(db.TABLE_YIELD_ESTIMATE));
		CreateItem("Planting Rain", db.getTableCount(db.TABLE_PLANTING_RAINS));
		CreateItem("Re Registered Farmers", db.getTableCount(db.TABLE_RE_REGISTERED_FARMERS));
	}

	public void CreateItem(String name, int count) {
		Log.e("Data", name + " " + count);
		syncedArray = new SyncedArray();
		syncedArray.setCount(count);
		syncedArray.setName(name);
		list.add(syncedArray);
	}

	private class SyncedArray
	{
		String Name;
		int Count;

		public String getName() {
			return Name;
		}

		public void setName(String name) {
			Name = name;
		}

		public int getCount() {
			return Count;
		}

		public void setCount(int count) {
			Count = count;
		}
	}

	public class CustomListAdapter extends ArrayAdapter<SyncedArray> {

		private final Activity context;
		private final List<SyncedArray> items;

		public CustomListAdapter(Activity context, List<SyncedArray> items) {
			super(context, R.layout.synced_data_list, items);
			this.context = context;
			this.items = items;
		}

		public View getView(int position, View view, ViewGroup parent) {
			LayoutInflater inflater = context.getLayoutInflater();
			View rowView = inflater.inflate(R.layout.synced_data_list, null,
					true);

			TextView tvName = (TextView) rowView.findViewById(R.id.tvName);
			TextView tvCount = (TextView) rowView.findViewById(R.id.tvCount);

			tvName.setText(items.get(position).getName());
			tvCount.setText(items.get(position).getCount()+"");
			return rowView;

		}
	}
}
