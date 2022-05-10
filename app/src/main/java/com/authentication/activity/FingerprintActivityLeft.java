package com.authentication.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.authentication.asynctask.AsyncFingerprint;
import com.authentication.asynctask.AsyncFingerprint.OnCalibrationListener;
import com.authentication.asynctask.AsyncFingerprint.OnEmptyListener;
import com.authentication.utils.ToastUtil;
import com.svs.farm_app.R;
import com.svs.farm_app.entities.CollectedInputs;
import com.svs.farm_app.utils.Config;
import com.svs.farm_app.utils.ConnectionDetector;
import com.svs.farm_app.utils.DatabaseHandler;
import com.svs.farm_app.utils.MyAlerts;
import com.svs.farm_app.utils.MyPrefrences;
import com.svs.farm_app.utils.Preferences;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android_serialport_api.FingerprintAPI;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FingerprintActivityLeft extends BaseActivity implements
		OnClickListener {

	private static final String TAG = FingerprintActivityLeft.class.getSimpleName();
	@BindView(R.id.spinner)
	Spinner spinner;
	@BindView(R.id.register)
	Button register;
	@BindView(R.id.validate)
	Button validate;
	@BindView(R.id.bManualCollection)
	Button manualCollection;
	@BindView(R.id.tvThumbChooser)
	TextView tvThumbChooser;
	@BindView(R.id.rgSelectThumb)
	RadioGroup rgSelectThumb;
	@BindView(R.id.rbRightThumb)
	RadioButton rbRightThumb;
	@BindView(R.id.rbLeftThumb)
	RadioButton rbLeftThumb;
	@BindView(R.id.register2)
	Button register2;
	@BindView(R.id.validate2)
	Button validate2;
	@BindView(R.id.clear_flash)
	Button clear;
	@BindView(R.id.calibration)
	Button calibration;
	@BindView(R.id.backRegister)
	Button back;
	@BindView(R.id.upfail)
	TextView upfail;
	@BindView(R.id.fingerprintImage)
	ImageView fingerprintImage;
	@BindView(R.id.toolbar)
	Toolbar toolbar;
	private String[] m;

	private AsyncFingerprint asyncFingerprint;

	private ArrayAdapter<String> adapter;

	private ProgressDialog progressDialog;

	private byte[] model;

	private String path = Environment.getExternalStorageDirectory()
			+ File.separator + "fingerprint_image";

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case AsyncFingerprint.SHOW_PROGRESSDIALOG:
				cancleProgressDialog();
				showProgressDialog((Integer) msg.obj);
				break;
			case AsyncFingerprint.SHOW_FINGER_IMAGE:
				// imageNum++;
				// upfail.setText("�ϴ��ɹ���" + imageNum + "\n" + "�ϴ�ʧ�ܣ�"
				// + failTime+ "\n" + "��������" + missPacket);
				showFingerImage(msg.arg1, (byte[]) msg.obj);
				break;
			case AsyncFingerprint.SHOW_FINGER_MODEL:
				FingerprintActivityLeft.this.model = (byte[]) msg.obj;
				if (FingerprintActivityLeft.this.model != null) {
					Log.i("whw", "#################model.length="
							+ FingerprintActivityLeft.this.model.length);
				}
				cancleProgressDialog();
				break;
			case AsyncFingerprint.REGISTER_SUCCESS:
				if (whereTo.equals("validation")) {
					cancleProgressDialog();
				} else if (whereTo.equals(Config.FINGERPRINT_CAPTURE)) {
					cancleProgressDialog();
					if (msg.obj != null) {
						Integer id = (Integer) msg.obj;
						ToastUtil.showToast(FingerprintActivityLeft.this,
								getString(R.string.register_success)
										+ "  pageId=" + id);
					} else {
						ToastUtil.showToast(FingerprintActivityLeft.this,
								"Left Thumb Scanned");
						Intent intent = getIntent();
						String farmerID = intent.getStringExtra("farmer_id");
						String genID = intent.getStringExtra(DatabaseHandler.KEY_GEN_ID);
						Log.d("FingerprintActivity:",farmerID + "|" +genID + "|"
								+ newFingerprintPath.getAbsolutePath());

						MyPrefrences.savePrefrence(mContext, Config.LEFT_THUMB, newFingerprintPath.getAbsolutePath());

					}
				} else if (whereTo.equals(Config.RECAPTURE_FARMER_DETAILS)) {
					cancleProgressDialog();
					if (msg.obj != null) {
						Integer id = (Integer) msg.obj;
						ToastUtil.showToast(FingerprintActivityLeft.this,
								getString(R.string.register_success)
										+ "  pageId=" + id);
					} else {
						ToastUtil.showToast(FingerprintActivityLeft.this,
								"Left Thumb Scanned");
						Intent intent = getIntent();
						String farmerID = intent.getStringExtra("farmer_id");
						String genID = intent.getStringExtra(DatabaseHandler.KEY_GEN_ID);
						Log.d("FingerprintActivity:",farmerID + "|" +genID + "|"
								+ newFingerprintPath.getAbsolutePath());

						MyPrefrences.savePrefrence(mContext, Config.NEW_LEFT_THUMB, newFingerprintPath.getAbsolutePath());

						startActivity(new Intent(mContext,
								FingerprintActivityRight.class).putExtra(Config.TO_ACTIVITY, Config.RECAPTURE_FARMER_DETAILS));
					}
				}else if (whereTo.equals(Config.MANUAL_INPUT_COLLECTION)) {
					cancleProgressDialog();
					if (msg.obj != null) {
						Integer id = (Integer) msg.obj;
						ToastUtil.showToast(FingerprintActivityLeft.this,
								getString(R.string.register_success)+""
										+ "  pageId=" + id);
					} else {
						ToastUtil.showToast(FingerprintActivityLeft.this,
								"Left Thumb Scanned");
						Intent intent = getIntent();
						String farmerID = intent.getStringExtra("farmer_id");
						String genID = intent.getStringExtra(DatabaseHandler.KEY_GEN_ID);
						Log.d("FingerprintActivity:",farmerID + "|" +genID + "|"
								+ newFingerprintPath.getAbsolutePath());

						MyPrefrences.savePrefrence(mContext, Config.NEW_LEFT_THUMB, newFingerprintPath.getAbsolutePath());

						startActivity(new Intent(mContext,
								FingerprintActivityRight.class).putExtra(Config.TO_ACTIVITY, Config.MANUAL_INPUT_COLLECTION));
					}
				}

				break;
			case AsyncFingerprint.REGISTER_FAIL:
				cancleProgressDialog();
				ToastUtil.showToast(FingerprintActivityLeft.this,
						"Registrration Failed");
				break;
			case AsyncFingerprint.VALIDATE_RESULT1:
				cancleProgressDialog();
				showValidateResult((Boolean) msg.obj);
				break;
			case AsyncFingerprint.VALIDATE_RESULT2:
				cancleProgressDialog();
				Integer r = (Integer) msg.obj;
				if (r != -1) {
					ToastUtil.showToast(FingerprintActivityLeft.this,
							getString(R.string.verifying_through) + "  pageId="
									+ r);
				} else {
					showValidateResult(false);
				}
				break;
			case AsyncFingerprint.UP_IMAGE_RESULT:
				cancleProgressDialog();
				ToastUtil
						.showToast(FingerprintActivityLeft.this, (Integer) msg.obj);
				// failTime++;
				// upfail.setText("�ϴ��ɹ���" + imageNum + "\n" + "�ϴ�ʧ�ܣ�"
				// + failTime+ "\n" + "��������" + missPacket);
				break;
			default:
				break;
			}
		}

	};

	private String whereTo;

	private String assfid;

	private String collectDate;

	private String genId;

	private String loadedFingerprintPath;

	private String tempGenId;

	private String orderID;
	private Context mContext;
	private DatabaseHandler db;
	private ConnectionDetector cd;
	private String userID;
	private SimpleDateFormat dateFormat;
	private String companyID;

	// int imageNum = 0;
	// int failTime = 0;
	// int missPacket = 0;
	private void showValidateResult(boolean matchResult) {
		if (matchResult) {
			ToastUtil.showToast(FingerprintActivityLeft.this,
					"Verification Successful Data Saved");

			if (cd.isConnectingToInternet()) {
				new UploadCollectedInputs().execute();
			} else {

				Date date = new Date();

				String collectDate = dateFormat.format(date);
				db.addCollectedInputs(new CollectedInputs(orderID, collectDate,
						userID,Config.FINGERPRINT));

				db.deleteAssignedInput(orderID);

				MyAlerts.backToFarmInputsDialog(mContext,R.string.saved_offline);
			}
		} else {
			ToastUtil.showToast(FingerprintActivityLeft.this,
					R.string.verifying_fail);
			MyAlerts.genericDialog(mContext,R.string.fail_fingerprint_verification);
		}
	}

	private void showFingerImage(int fingerType, byte[] data) {
		Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);
		if (whereTo.equals(Config.FINGERPRINT_CAPTURE)||whereTo.equals(Config.RECAPTURE_FARMER_DETAILS)||whereTo.equals(Config.MANUAL_INPUT_COLLECTION)) {
			saveImage(data);
		}
		fingerprintImage.setBackgroundDrawable(new BitmapDrawable(image));
		writeToFile(data);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fingerprint);
		ButterKnife.bind(this);

		mContext = FingerprintActivityLeft.this;

		initView();
		initViewListener();
		initData();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_fingerprint_capture, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                if(newFingerprintPath != null){

                    startActivity(new Intent(mContext,
                            FingerprintActivityRight.class).putExtra(Config.TO_ACTIVITY, Config.FINGERPRINT_CAPTURE));

                }else{
                    MyAlerts.genericDialog(mContext,"Capture fingerprint first!");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

	private void initView() {
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(true);

		Intent intent = getIntent();
		whereTo = intent.getStringExtra(Config.TO_ACTIVITY);
		// save for async
		MyPrefrences.savePrefrence(mContext,
				"fingerprint_class", whereTo);
		if (whereTo.equals("validation")) {
			calibration.setVisibility(View.VISIBLE);
			
			rgSelectThumb.setVisibility(View.VISIBLE);
			rbRightThumb.setVisibility(View.VISIBLE);
			rbLeftThumb.setVisibility(View.VISIBLE);
			manualCollection.setVisibility(View.VISIBLE);
			rbRightThumb.setChecked(true);

			orderID = intent.getStringExtra("order_id");

			Log.i(TAG, "assfid: "+assfid);
			collectDate = intent.getStringExtra(DatabaseHandler.KEY_COLLECT_DATE);
			genId = intent.getStringExtra(DatabaseHandler.KEY_GEN_ID);
			
			tempGenId = genId+"R";
			// register.setVisibility(View.GONE);
			register.setText("Load Stored Fingerprint");

			loadedFingerprintPath = Config.FINGERPRINTS_PATH+ tempGenId.trim()+".bmp";

		} else if (whereTo.equals(Config.FINGERPRINT_CAPTURE)) {
			validate.setVisibility(View.GONE);
			manualCollection.setVisibility(View.GONE);
			tvThumbChooser.setVisibility(View.GONE);
			rgSelectThumb.setVisibility(View.GONE);
			rbRightThumb.setVisibility(View.GONE);
			rbLeftThumb.setVisibility(View.GONE);
			register.setText("Register Left Thumb");

		}
		 else if (whereTo.equals(Config.RECAPTURE_FARMER_DETAILS)||whereTo.equals(Config.MANUAL_INPUT_COLLECTION)) {
				validate.setVisibility(View.GONE);
				manualCollection.setVisibility(View.GONE);
				tvThumbChooser.setVisibility(View.GONE);
				rgSelectThumb.setVisibility(View.GONE);
				rbRightThumb.setVisibility(View.GONE);
				rbLeftThumb.setVisibility(View.GONE);
				register.setText("Register Left Thumb");

			}
	}

	private String rootPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath();

	private String fileName;

	private File newFingerprintPath;

	private void writeToFile(byte[] data) {
		String dir = rootPath + File.separator + "fingerprint_image";
		File dirPath = new File(dir);
		if (!dirPath.exists()) {
			dirPath.mkdir();
		}

		String filePath = dir + "/" + System.currentTimeMillis() + ".bmp";
		File newFingerprintPath = new File(filePath);
		if (newFingerprintPath.exists()) {
			newFingerprintPath.delete();
		}
		FileOutputStream fos = null;
		try {
			newFingerprintPath.createNewFile();
			fos = new FileOutputStream(newFingerprintPath);
			fos.write(data);
			fos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void initData() {
		db = new DatabaseHandler(mContext);
		cd = new ConnectionDetector(mContext);

		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		userID = Preferences.USER_ID;
		companyID = Preferences.COMPANY_ID;

		m = this.getResources().getStringArray(R.array.fingerprint_size);

		// ����ѡ������ArrayAdapter��������
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, m);

		// ���������б�ķ��
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// ��adapter ��ӵ�spinner��
		spinner.setAdapter(adapter);

		// ����¼�Spinner�¼�����
		spinner.setOnItemSelectedListener(new SpinnerSelectedListener());

	}

	// ʹ��������ʽ����
	class SpinnerSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> arg0, View arg1,
				int position, long arg3) {
			Log.i("whw", "position=" + position);
			switch (position) {
			case 0:
				asyncFingerprint
						.setFingerprintType(FingerprintAPI.SMALL_FINGERPRINT_SIZE);
				break;
			case 1:
				asyncFingerprint
						.setFingerprintType(FingerprintAPI.BIG_FINGERPRINT_SIZE);
				break;
			default:
				break;
			}
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	private void initData2() {
		asyncFingerprint = new AsyncFingerprint(handlerThread.getLooper(),
				mHandler);

		asyncFingerprint.setOnEmptyListener(new OnEmptyListener() {

			@Override
			public void onEmptySuccess() {
				ToastUtil.showToast(FingerprintActivityLeft.this,
						R.string.clear_flash_success);

			}

			@Override
			public void onEmptyFail() {
				ToastUtil.showToast(FingerprintActivityLeft.this,
						R.string.clear_flash_fail);

			}
		});

		asyncFingerprint.setOnCalibrationListener(new OnCalibrationListener() {

			@Override
			public void onCalibrationSuccess() {
				Log.i("whw", "onCalibrationSuccess");
				ToastUtil.showToast(FingerprintActivityLeft.this,
						R.string.calibration_success);
			}

			@Override
			public void onCalibrationFail() {
				Log.i("whw", "onCalibrationFail");
				ToastUtil.showToast(FingerprintActivityLeft.this,
						R.string.calibration_fail);

			}
		});

	}

	private void initViewListener() {
		register.setOnClickListener(this);
		validate.setOnClickListener(this);
		manualCollection.setOnClickListener(this);
		register2.setOnClickListener(this);
		validate2.setOnClickListener(this);
		calibration.setOnClickListener(this);
		clear.setOnClickListener(this);
		back.setOnClickListener(this);
		
		rgSelectThumb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
		{
		    public void onCheckedChanged(RadioGroup rGroup, int checkedId)
		    {
		    	if(rbLeftThumb.isChecked()){
					String leftFingerprint = genId+"L";
					loadedFingerprintPath = Config.FINGERPRINTS_PATH+ leftFingerprint.trim()+".bmp";

					File newFingerprintPath = new File(loadedFingerprintPath);
					Log.i(TAG, " "+newFingerprintPath.exists()+" N: "+newFingerprintPath.getName());

				}else if(rbRightThumb.isChecked()){
					String rightFingerprint = genId+"R";
					loadedFingerprintPath = Config.FINGERPRINTS_PATH + rightFingerprint.trim()+".bmp";

					File newFingerprintPath = new File(loadedFingerprintPath);
					Log.i(TAG, ""+newFingerprintPath.exists()+" N: "+newFingerprintPath.getName());

				}
		    }
		});
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register:

			if (whereTo.equals("validation")) {
				// load fingerprint image into flash
				if(new File(loadedFingerprintPath).exists()){
				asyncFingerprint.register(loadedFingerprintPath);
				}else{
					ToastUtil.showToast(mContext, "Fingerprint is not on Tab");
				}
			} else {
				asyncFingerprint.register();
			}
			break;
		case R.id.validate:
			if (model != null) {
				asyncFingerprint.validate(model);
			} else {
				if (whereTo.equals("validation")) {
					ToastUtil.showToast(FingerprintActivityLeft.this,
							"Load fingerprint first");
				}else{
				ToastUtil.showToast(FingerprintActivityLeft.this,
						R.string.first_register);
				}
			}
			break;
		case R.id.bManualCollection:

			Date date = new Date();

			String collectDate = dateFormat.format(date);

			db.addCollectedInputs(new CollectedInputs(orderID, collectDate,
					userID,Config.MANUAL));
			db.deleteAssignedInput(orderID);

			Intent intent = new Intent(mContext,
					FingerprintActivityLeft.class)
					.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra(Config.TO_ACTIVITY, Config.MANUAL_INPUT_COLLECTION);
			
			MyAlerts.toActivityDialog(mContext,R.string.saved_offline, intent);

			break;
		case R.id.register2:
			asyncFingerprint.register2();
			break;
		case R.id.validate2:
			asyncFingerprint.validate2();
			break;
		case R.id.calibration:
			Log.i("whw", "calibration start");
			asyncFingerprint.PS_Calibration();
			break;
		case R.id.clear_flash:
			asyncFingerprint.PS_Empty();
			break;
		case R.id.backRegister:
			finish();
			break;

		default:
			break;
		}
	}
	

	private void saveImage(byte[] data) {
		File filePath = new File(path);
		if (!filePath.exists()) {
			filePath.mkdir();
		}

		fileName = String.valueOf(System.currentTimeMillis());

		newFingerprintPath = new File(path + File.separator + fileName + ".bmp");
		if (!newFingerprintPath.exists()) {
			try {
				newFingerprintPath.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(newFingerprintPath);
			fos.write(data);
			fos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private void showProgressDialog(String message) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(message);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setCancelable(false);
		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
	}

	private void showProgressDialog(int resId) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(getResources().getString(resId));
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (KeyEvent.KEYCODE_BACK == keyCode) {
					asyncFingerprint.setStop(true);
				}
				return false;
			}
		});
		progressDialog.show();
	}

	private void cancleProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.cancel();
			progressDialog = null;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		cancleProgressDialog();
		super.onDestroy();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.i("whw", "onRestart");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i("whw", "onStop");
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData2();
		Log.i("whw", "onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		cancleProgressDialog();
		asyncFingerprint.setStop(true);
		Log.i("whw", "onPause");
	}

	private class UploadCollectedInputs extends AsyncTask<String, String, String> {

		public UploadCollectedInputs() {
		}

		private String result2;
		ConnectionDetector cd2;

		@Override
		protected String doInBackground(String... arg0) {

			cd2 = new ConnectionDetector(mContext);
			if (cd2.isConnectingToInternet()) {
				// download all inputs from server
				
				// if (db.getSignedDocsCount() > 0) {
				InputStream is = null;
				// Log.e("companyID: " + doc.getCompanyId() + "FID: " + doc.getFarmerId(),
				// "Date: " + doc.getSignDate() + "UID: " + doc.getUserId());
				try {
					HttpClient httpclient = new DefaultHttpClient();
					Log.e("", Config.UPLOAD_COLLECTED_INPUTS);
					HttpPost httppost = new HttpPost(
							Config.UPLOAD_COLLECTED_INPUTS);

					List<NameValuePair> nameValuePairs = null;
					nameValuePairs = new ArrayList<>();
					nameValuePairs
							.add(new BasicNameValuePair("order_id", orderID));
					nameValuePairs.add(new BasicNameValuePair(DatabaseHandler.KEY_USER_ID,
							userID));
					Date date = new Date();

					nameValuePairs.add(new BasicNameValuePair(DatabaseHandler.KEY_COLLECT_DATE,
							dateFormat.format(date)));
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					HttpResponse response = httpclient.execute(httppost);
					HttpEntity entity = response.getEntity();

					is = entity.getContent();
				} catch (Exception e) {
					Log.e("Exception: ", e.toString());
				}
				result2 = null;
				try {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(is, "iso-8859-1"), 8);
					StringBuilder sb = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
					is.close();
					result2 = sb.toString();
					Log.e(TAG, result2);
				} catch (Exception e) {
					Log.e("Ex Sign: ", e.toString());
				}

			} else {

			}
			return result2;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(result.contains("ok")){

			db.deleteAssignedInput(orderID);

				MyAlerts.backToFarmInputsDialog(mContext,R.string.saved_offline);
			}else{

				Date date = new Date();

				String collectDate = dateFormat.format(date);

				db.addCollectedInputs(new CollectedInputs(orderID, collectDate,
						userID,Config.MANUAL));
				db.deleteAssignedInput(orderID);

				MyAlerts.backToFarmInputsDialog(mContext,R.string.try_save_offline);
			}
			
		}

	}

}
