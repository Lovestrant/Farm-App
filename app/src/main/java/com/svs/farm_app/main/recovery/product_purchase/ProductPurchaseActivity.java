package com.svs.farm_app.main.recovery.product_purchase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.authentication.activity.BaseActivity;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.svs.farm_app.R;
import com.svs.farm_app.asynctask.uploads.UploadProductPurchase;
import com.svs.farm_app.entities.ProductGrade;
import com.svs.farm_app.entities.ProductPurchase;
import com.svs.farm_app.entities.RegisteredFarmer;
import com.svs.farm_app.farmersearch.FarmerSearchActivity;
import com.svs.farm_app.utils.Config;
import com.svs.farm_app.utils.MyAlerts;
import com.svs.farm_app.utils.MyPrefrences;
import com.svs.farm_app.utils.Preferences;

import java.util.ArrayList;
import java.util.List;

public class ProductPurchaseActivity extends BaseActivity {

    private Spinner spCottonGrade;
    private TextView tvPrice;
    private TextView tvCurrency;
    private TextView tvTotal;
    private EditText etWeight;
    private Button bPurchase;
    private Toolbar toolbar;
    private Context mContext;
    private int farmerId;
    private String TAG = ProductPurchaseActivity.class.getSimpleName();
    private List<ProductGrade> productGradeList;
    private ArrayAdapter<MyData> cottonPricesAdapter;
    private MyData[] productGradesData;
    private int gradeId;
    private float price = 0;
    private float weight = 0;
    private float totalValue = 0;
    private EditText etReceipt;
    private TextInputLayout inputLayoutWeight;
    private TextInputLayout inputLayoutReceipt;
    private float deductions = 0;
    private EditText etDeductions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cotton_purchase);
        initView();
        initData();
        initListeners();
    }

    private void initListeners() {
        spCottonGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gradeId = productGradesData[position].getGradeId();
                price = productGradesData[position].getPrice();
                tvPrice.setText(String.valueOf(price));

                totalValue = calculateCottonValue(price, weight);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        etWeight.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String weightText = etWeight.getText().toString().trim();
                if (!weightText.isEmpty()) {
                    weight = Float.parseFloat(weightText);
                    totalValue = calculateCottonValue(price, weight);
                    tvTotal.setText(String.format("%,.2f", totalValue));
                } else {
                    weight = 0;
                }
                return false;
            }
        });

        bPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputLayoutReceipt.setError(null);
                inputLayoutWeight.setError(null);

                totalValue = calculateCottonValue(price, weight);

                try {
                    deductions = Float.parseFloat(etDeductions.getText().toString().trim());
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }

                String receiptNumber = etReceipt.getText().toString().trim();
                boolean isValid = validate(gradeId, weight, receiptNumber);

                if (isValid) {
                    confirmPurchase(farmerId, gradeId, weight, price, totalValue, deductions, receiptNumber, Preferences.USER_ID, Preferences.COMPANY_ID);
                }
            }
        });
    }

    /**
     * Confirm purchase details
     *
     * @param farmerId
     * @param gradeId
     * @param weight
     * @param price
     * @param totalValue
     * @param deductions
     * @param receiptNumber
     * @param userId
     */
    private void confirmPurchase(final int farmerId, final int gradeId, final float weight, final float price, float totalValue, final float deductions, final String receiptNumber, final String userId, final String companyId) {
        float afterDeductions = totalValue - deductions;

        //TODO: Inflate view
        View productPurchaseView = getLayoutInflater().inflate(R.layout.product_purchase_dialog,null);

        TextView tvWeight = (TextView) productPurchaseView.findViewById(R.id.tvWeight);
        TextView tvPrice = (TextView) productPurchaseView.findViewById(R.id.tvPrice);
        TextView tvDeductions = (TextView) productPurchaseView.findViewById(R.id.tvDeductions);
        TextView tvTotalValue = (TextView) productPurchaseView.findViewById(R.id.tvTotalValue);
        TextView tvAfterDeductions = (TextView) productPurchaseView.findViewById(R.id.tvAfterDeductions);
        TextView tvReceipt = (TextView) productPurchaseView.findViewById(R.id.tvReceipt);

        tvWeight.setText(String.valueOf(weight));
        tvPrice.setText(String.valueOf(price));
        tvDeductions.setText(String.valueOf(deductions));
        tvTotalValue.setText(String.valueOf(totalValue));
        tvAfterDeductions.setText(String.valueOf(afterDeductions));
        tvReceipt.setText(receiptNumber);

            new MaterialStyledDialog.Builder(ProductPurchaseActivity.this)
                .setTitle(R.string.app_name)
                .setStyle(Style.HEADER_WITH_TITLE)
                    .setCustomView(productPurchaseView)
                .setCancelable(false)
                .setNegativeText(R.string.cancel)
                .setPositiveText(R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        ProductPurchase productPurchase = new ProductPurchase(farmerId, gradeId, weight, price, deductions, receiptNumber, Integer.parseInt(userId), Integer.parseInt(companyId));
                        if(cd.isConnectingToInternet()) {
                            new UploadProductPurchase(ProductPurchaseActivity.this, productPurchase,db).execute();
                        }else{
                            db.addProductPurchase(productPurchase);

                            MyAlerts.toActivityDialog(mContext,R.string.saved_offline,new Intent(mContext, FarmerSearchActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra(Config.TO_ACTIVITY, "recovery"));
                        }
                    }
                }).show();

    }


    /**
     * Check all values
     *
     * @param gradeId
     * @param weight
     * @param receiptNumber
     */
    private boolean validate(int gradeId, float weight, String receiptNumber) {
        boolean valid = true;
        if (gradeId == 0) {
            //TODO: set error
            valid = false;
        }

        if (weight == 0) {
            inputLayoutWeight.setError(getString(R.string.enter_valid_weight));
            valid = false;
        }

        if (receiptNumber.isEmpty()) {
            inputLayoutReceipt.setError(getString(R.string.enter_receipt_number));
            valid = false;
        }

        return valid;
    }

    private float calculateCottonValue(float price, float weight) {
        return price * weight;
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spCottonGrade = (Spinner) findViewById(R.id.spCottonGrade);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvCurrency = (TextView) findViewById(R.id.tvCurrency);
        tvTotal = (TextView) findViewById(R.id.tvTotal);
        inputLayoutWeight = (TextInputLayout) findViewById(R.id.inputLayoutWeight);
        etWeight = (EditText) findViewById(R.id.etWeight);
        etDeductions = (EditText) findViewById(R.id.etDeductions);
        etReceipt = (EditText) findViewById(R.id.etReceipt);
        inputLayoutReceipt = (TextInputLayout) findViewById(R.id.inputLayoutReceipt);
        bPurchase = (Button) findViewById(R.id.bPurchase);
    }

    private void initData() {

        mContext = ProductPurchaseActivity.this;

        Intent intent = getIntent();
        try {
            farmerId = intent.getIntExtra("farmer_id", 0);
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }

        RegisteredFarmer farmer = db.getFarmerById(farmerId);
        int villageId = farmer.getVillageId();

        /*Get all farmers deductions for each season*/
        Gson gson = new GsonBuilder().create();

        String productPricesJSON = MyPrefrences.getPrefrence(mContext, Config.PRODUCT_GRADES);

        List<ProductGrade> productGrades = gson.fromJson(productPricesJSON, new TypeToken<List<ProductGrade>>() {
        }.getType());

        Log.i(TAG, "FARMER ID: " + farmerId + " " + productGrades.size());

        productGradeList = getProductGrades(productGrades, villageId);

        loadProductGrades(productGradeList);

    }

    private void loadProductGrades(List<ProductGrade> productGrades) {

        cottonPricesAdapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_item);
        productGradesData = new MyData[productGrades.size()];

        int i = 0;
        for (ProductGrade productGrade : productGrades) {
            Log.i(TAG, "Grade name: " + productGrade.getGradeName());
            productGradesData[i] = new MyData(productGrade.getProductGradeId(), productGrade.getGradeName(), productGrade.getPrice());
            i++;
        }

        Log.i(TAG, "My data length" + productGradesData.length);

        cottonPricesAdapter.addAll(productGradesData);
        cottonPricesAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cottonPricesAdapter.notifyDataSetChanged();
        spCottonGrade.setAdapter(cottonPricesAdapter);
    }

    private List<ProductGrade> getProductGrades(List<ProductGrade> productGrades, int villageId) {
        List<ProductGrade> productGradeList = new ArrayList<>();
        for (ProductGrade productGrade : productGrades) {
            if (villageId == productGrade.getVillageId()) {
                productGradeList.add(productGrade);
            }
        }
        return productGradeList;
    }

    private class MyData {

        float price;
        String grade;
        int gradeId;

        public MyData(int gradeId, String gradeName, float price) {
            this.gradeId = gradeId;
            this.grade = gradeName;
            this.price = price;

        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public int getGradeId() {
            return gradeId;
        }

        @Override
        public String toString() {
            return "GRADE: " + grade;
        }
    }
}
