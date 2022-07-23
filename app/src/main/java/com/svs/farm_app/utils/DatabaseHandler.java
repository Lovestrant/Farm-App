package com.svs.farm_app.utils;

/**
 * Created by wamae on 12/29/2014.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.svs.farm_app.entities.AssignedInputs;
import com.svs.farm_app.entities.AssignedTrainings;
import com.svs.farm_app.entities.CollectedInputs;
import com.svs.farm_app.entities.CollectedOrder;
import com.svs.farm_app.entities.Companies;
import com.svs.farm_app.entities.CottonDeduction;
import com.svs.farm_app.entities.Countries;
import com.svs.farm_app.entities.MappedFarm;
import com.svs.farm_app.entities.ProductPurchase;
import com.svs.farm_app.entities.RecoveredCash;
import com.svs.farm_app.entities.TransportHseToMarket;
import com.svs.farm_app.entities.Districts;
import com.svs.farm_app.entities.Farm;
import com.svs.farm_app.entities.FarmAssFormsMajor;
import com.svs.farm_app.entities.FarmAssFormsMedium;
import com.svs.farm_app.entities.FarmDataColleted;
import com.svs.farm_app.entities.FarmIncome;
import com.svs.farm_app.entities.FarmOtherCrops;
import com.svs.farm_app.entities.FarmerTime;
import com.svs.farm_app.entities.Farmers;
import com.svs.farm_app.entities.FingerFive;
import com.svs.farm_app.entities.FingerFour;
import com.svs.farm_app.entities.FingerOne;
import com.svs.farm_app.entities.FingerPrint;
import com.svs.farm_app.entities.FingerThree;
import com.svs.farm_app.entities.FingerTwo;
import com.svs.farm_app.entities.FoliarFeed;
import com.svs.farm_app.entities.Germination;
import com.svs.farm_app.entities.Herbicides;
import com.svs.farm_app.entities.InputsFromServer;
import com.svs.farm_app.entities.ManualInputFingerprintRecapture;
import com.svs.farm_app.entities.MolassesTrapCatch;
import com.svs.farm_app.entities.OfficerTraining;
import com.svs.farm_app.entities.OtherCrops;
import com.svs.farm_app.entities.Pesticides;
import com.svs.farm_app.entities.FarmProduction;
import com.svs.farm_app.entities.PlantingRains;
import com.svs.farm_app.entities.ReRegisteredFarmers;
import com.svs.farm_app.entities.Regions;
import com.svs.farm_app.entities.RegisteredFarmer;
import com.svs.farm_app.entities.Scouting;
import com.svs.farm_app.entities.ShowIntent;
import com.svs.farm_app.entities.SignedDoc;
import com.svs.farm_app.entities.SubVillage;
import com.svs.farm_app.entities.TrainingType;
import com.svs.farm_app.entities.TransportModes;
import com.svs.farm_app.entities.UpdateFarmArea;
import com.svs.farm_app.entities.UserVillage;
import com.svs.farm_app.entities.Users;
import com.svs.farm_app.entities.Village;
import com.svs.farm_app.entities.WackFarmer;
import com.svs.farm_app.entities.Ward;
import com.svs.farm_app.entities.YieldEstimate;
import com.svs.farm_app.main.registration.register.FarmHistory;
import com.svs.farm_app.main.registration.register.FarmHistoryItem;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    public static final String DATABASE_NAME = "farm_app";

    private static final String TAG = DatabaseHandler.class.getSimpleName();
    private static final String KEY_POINTS = "points";
    private static final String TABLE_SURVEY_RESPONSES = "survey_responses";
    // table names
    public final String TABLE_FARMERS = "farmers";
    public final String TABLE_FARM_INPUTS = "farm_inputs";
    public final String TABLE_ASSIGNED_INPUTS = "assigned_inputs";
    public final String TABLE_REGISTERED_FARMERS = "registered_farmers";
    public static final String TABLE_UPDATED_FARMERS = "updated_farmers";
    public final String TABLE_FARM_DATA_COLLECTED = "farm_data_collected";
    public final String TABLE_EXT_OFFICER_TRAINING = "ext_officer_training";
    private static final String TABLE_QUESTIONAIRE_STRUCTURE = "table_questionaire_structure";
    public final String TABLE_QUESTIONAIRE_ANSWERS = "table_questionaire_answers";

    //questionnaire table
    private static final String KEY_ANSWER = "answer";
    private static final String KEY_QUESTION = "question";
    private static final String KEY_ANSWER_TYPE = "answer_type";
    private static final String KEY_QUESTION_ID = "question_id";
    private static final String KEY_QUESTION_CATEGORY = "key_question_category";
    private static final String KEY_SURVEY_DATE = "survey_date";
    private static final String KEY_QUESTIONNAIRE_ID = "questionnaire_id";

    /*Farmers Table*/
    private static final String KEY_ID = "id";
    private static final String KEY_ID_NO = "id_no";
    private static final String KEY_CARD_NO = "card_no";
    public static final String KEY_FNAME = "fname";
    public static final String KEY_LNAME = "lname";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_PH_NO = "phone_number";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_POST = "post";
    private static final String KEY_VILLAGE = "village";
    private static final String KEY_SUB_VILLAGE = "sub_village";
    private static final String KEY_PIC = "pic";
    static final String KEY_INPUT_ID = "input_id";
    private static final String KEY_INPUT_CATEGORY = "input_category";
    public static final String KEY_INPUT_TYPE = "input_type";
    /*COUNTRIES*/
    public final String TABLE_COUNTRIES = "countries";
    private static final String KEY_COUNTRY_ID = "country_id";
    private static final String KEY_COUNTRY_NAME = "country_name";
    /*REGIONS*/
    private static final String TABLE_REGIONS = "regions";
    private static final String KEY_REGION_ID = "region_id";
    private static final String KEY_REGION_NAME = "region_name";
    /*DISTRICTS*/
    private static final String TABLE_DISTRICTS = "districts";
    private static final String KEY_DISTRICT_ID = "district_id";
    private static final String KEY_DISTRICT_NAME = "district_name";
    /*WARDS*/
    public final String TABLE_WARDS = "wards";
    private static final String KEY_WARD_ID = "ward_id";
    private static final String KEY_WARD_NAME = "ward_name";
    /*VILLAGES*/
    public final String TABLE_VILLAGES = "villages";
    private static final String KEY_VILLAGE_ID = "village_id";
    private static final String KEY_VILLAGE_NAME = "village_name";
    /*SUBVILLAGES*/
    public final String TABLE_SUBVILLAGES = "sub_villages";
    private static final String KEY_SUBVILLAGE_ID = "sub_village_id";
    private static final String KEY_SUBVILLAGE_NAME = "subvillage_name";
    public static final String KEY_GEN_ID = "gen_id";
    /*TRAINING*/
    public final String TABLE_TRAININGS = "trainings";
    static final String KEY_TRAIN_ID = "train_id";
    public static final String KEY_TRAIN_DATE = "train_date";
    /*ASSIGNED TRAININGS*/
    public final String TABLE_ASSIGNED_TRAININGS = "assigned_trainings";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    /*USERS*/
    public final String TABLE_USERS = "users";
    public static final String KEY_USER_ID = "user_id";
    //    public static final String KEY_CONTRACT_ID = "contract_id";
    public static final String KEY_CONTRACT_ID = "contract_number";
    private static final String KEY_USER_NAME = "name";
    private static final String KEY_USER_PWD = "pwd";
    /*USER VILLAGES*/
    public final String TABLE_USER_VILLAGE = "user_village";
    static final String KEY_COMPANY_ID = "company_id";
    private static final String KEY_COMPANY_NAME = "company_name";
    /*COMPANIES*/
    public final String TABLE_COMPANIES = "companies";

    public static final String KEY_TRAIN_CAT = "train_cat";
    public final String TABLE_TRAIN_CATEGORIES = "train_categories";
    public static final String KEY_TRAIN_CAT_ID = "train_cat_id";
    private static final String KEY_TRAIN_TYPE = "train_type";
    static final String KEY_TRAIN_STOP_TIME = "train_stop_time";
    static final String KEY_TRAIN_START_TIME = "train_start_time";
    public final String TABLE_FARMER_TIMES = "farmer_times";
    public static final String KEY_FARMER_ID = "farmer_id";
    private static final String KEY_FARMER_TIME_IN = "farmer_time_in";
    private static final String KEY_FARMER_TIME_OUT = "farmer_time_out";
    private static final String KEY_TRAIN_STATUS = "train_status";
    public final String TABLE_FINGER_ONE = "finger_one";
    public final String TABLE_FINGER_TWO = "finger_two";
    public final String TABLE_FINGER_THREE = "finger_three";
    public final String TABLE_FINGER_FOUR = "finger_four";

    public final String TABLE_FINGER_FIVE = "finger_five";
    private static final String KEY_FINGER_ID = "finger_id";
    private static final String KEY_SOIL_TYPE = "soil_type";
    private static final String KEY_CORRECT_SEED = "correct_seed";
    private static final String KEY_GAP_FILL = "gap_fill";
    private static final String KEY_FIRST_BRANCH = "first_branch";
    private static final String KEY_PEST_LEVEL = "pest_level";
    private static final String KEY_WATER_LOG_RISK = "water_log_risk";
    private static final String KEY_ROW_SPACING = "row_spacing";
    private static final String KEY_EROSION_PREVENTION = "erosion_prevension";
    private static final String KEY_CROP_ROTATION = "crop_rotation";
    private static final String KEY_RATOON = "ratoon";
    private static final String KEY_CROP_RESIDUES = "crop_residues";
    private static final String KEY_MANURE = "manure";
    private static final String KEY_LAND_PREPARATION = "land_preparation";
    private static final String KEY_SEED_BED_PREPARATION = "seed_bed_preparation";
    private static final String KEY_SEEDS_PER_STATION = "seeds_per_station";
    private static final String KEY_PLANTING_TIME = "planting_time";
    private static final String KEY_GAP_FILL_ON_EMERGENCE = "gap_fill_on_emer";
    private static final String KEY_THINNING_NUM = "thinning_number";
    private static final String KEY_THIN_AFTER_EMERGENCE = "thinning_after_emer";
    private static final String KEY_FOLIAR = "foliar";
    private static final String KEY_WEEDS = "weeds";
    private static final String KEY_PEG_BOARD_AVAILABILITY = "peg_board_availability";
    private static final String KEY_PEST_DAMAGE = "pest_damage";
    private static final String KEY_LAST_SCOUT = "last_scout";
    private static final String KEY_EMPTY_PESTICIDE_CANS = "empty_cans";
    private static final String KEY_SCOUTING_METHOD = "scount_method";
    private static final String KEY_SPRAY_TIME = "spray_time";
    private static final String KEY_PEST_ABSENSE_DURATION = "pest_abs_duration";
    private static final String KEY_CORRECT_USE_PESTICIDE = "correct_use_pesticide";
    private static final String KEY_SAFE_USAGE_CANS = "safe_usage_cans";
    /*FARMS*/
    public final String TABLE_FARMS = "farms";
    private static final String KEY_FARM_ID = "farm_id";
    private static final String KEY_FARM_NAME = "plot_name";
    private static final String KEY_FARM_SIZE = "plot_size";
    private static final String KEY_PERIMETER = "plot_peri";
    static final String KEY_TRAIN_LATITUDE = "train_latitude";
    static final String KEY_TRAIN_LONGITUDE = "train_longitude";

    public final String TABLE_SIGNED_DOCS = "signed_docs";
    //private static final String KEY_SIGN_DATE = "sign_date";
    private static final String KEY_FORM_DATE = "form_date";
    public final String TABLE_MAPPED_FARMS = "mapped_farms";
    static final String KEY_RCPT_NUM = "rcpt_num";

    public final String TABLE_TRAIN_TYPES = "train_types";
    public static String KEY_INPUT_QUANTITY = "input_quantity";
    public static String KEY_INPUT_PRICE = "input_price";
    public static String KEY_INPUT_UNIT = "input_unit";
    public static String KEY_INPUT_TOTAL = "input_total";
    static final String KEY_ASS_INPUT_ID = "assfinputs_id";

    public final String TABLE_SCOUTING = "scouting";

    private static final String KEY_BOLL_WORM = "boll_worm";

    private static final String KEY_JASSID = "jassid";

    private static final String KEY_STAINER = "stainer";

    private static final String KEY_APHID = "aphid";

    private static final String KEY_BENEFICIAL = "beneficialInsects";

    private static final String KEY_SPRAY_DECISION = "spray_decision";

    public final String TABLE_PESTICIDES = "pesticides";

    public final String TABLE_TRANSPORT_MODES = "tranport_modes";

    private static final String KEY_TRANSPORT_MODE = "transport_mode";

    private static final String KEY_PICKING_DATE = "picking_date";

    public final String TABLE_PICKINGS = "pickings";

    public final String TABLE_FARM_AREA_UPDATES = "farm_area_updates";

    private static final String KEY_FARM_ASS = "farm_ass";

    private static final String KEY_ACTUAL_AREA = "actual_area";
    // from server
    private static final String KEY_ESTIMATED_AREA = "est_farm_area";

    public final String TABLE_COLLECTED_INPUTS = "collected_inputs";

    private static final String KEY_FILE_PATH = "file_path";

    public final String TABLE_FINGERPRINTS = "fingerprints";

    public final String TABLE_SHOW_INTENT = "show_intent";

    private static final String KEY_CREDIT_STATUS = "credit_status";

    private static final String KEY_CONTRACT_STATUS = "contract_status";

    private static final String KEY_FINGERPRINT_STATUS = "fingerprint_status";

    private static final String KEY_FARM_VILLAGE_ONE = "farm_village_one";

    private static final String KEY_FARM_VILLAGE_TWO = "farm_village_two";

    private static final String KEY_FARM_VILLAGE_THREE = "farm_village_three";

    private static final String KEY_FARM_VILLAGE_FOUR = "farm_village_four";

    private static final String KEY_LEFT_THUMB = "left_thumb";

    private static final String KEY_RIGHT_THUMB = "right_thumb";

    public static final String KEY_ORDER_NUM = "order_num";

    public static final String KEY_ORDER_ID = "order_id";

    private static final String KEY_OTHER_CROP_ONE = "other_crops_once";

    // for registration form
    private String KEY_ESTIMATED_FARM_AREA = "est_farm_area";
    private String KEY_ESTIMATED_FARM_AREA2 = "est_farm_area1";
    private String KEY_ESTIMATED_FARM_AREA3 = "est_farm_area2";
    private String KEY_ESTIMATED_FARM_AREA4 = "est_farm_area3";
    /*MAJOR FORMS*/
    public String TABLE_FARM_ASS_MAJOR = "activity_log_major";
    private String KEY_FORM_TYPE_ID = "activity_type_id";
    private String KEY_ACTIVITY_METHOD = "activity_method";
    private String KEY_ACTIVITY_DATE = "activity_date";
    private String KEY_FAMILY_HOURS = "family_hours";
    private String KEY_HIRED_HOURS = "hired_hours";
    public static final String KEY_COLLECT_DATE = "collect_date";
    private String KEY_MONEY_OUT = "money_out";
    private String KEY_REMARKS = "remarks";
    /*FARMS OTHER CROPS*/
    public String TABLE_FARM_OTHER_CROPS = "farm_other_crops";
    private String KEY_CROP_ID1 = "crop_id1";
    private String KEY_CROP_ID2 = "crop_id2";
    private String KEY_CROP_ID3 = "crop_id3";
    /*PLANTING RAINS*/
    public String TABLE_PLANTING_RAINS = "planting_rains";
    private String KEY_RAIN_DATE = "rain_date";
    /*MEDIUM FORMS*/
    public String TABLE_FARM_ASS_MEDIUM = "farm_ass_medium";
    public String TABLE_GERMINATION = "germination";
    private String KEY_GERM_DATE = "germ_date";
    /*TRAP CATCHES*/
    public String TABLE_MOLASSES_TRAP_CATCHES = "molasses_trap_catches";
    private String KEY_TRAP_DATE = "trap_date";
    private String KEY_ACTION_TAKEN = "action_taken";
    private String KEY_TRAP_ONE = "trap_one";
    private String KEY_TRAP_TWO = "trap_two";
    /*YIELD ESTIMATE*/
    public String TABLE_YIELD_ESTIMATE = "yield_estimate";
    private String KEY_NUM_OF_PLANTS = "num_of_plants";
    private String KEY_NUM_OF_BOLLS = "num_of_bolls";
    private String KEY_DISTANCE_TO_LEFT = "dis_to_left";
    private String KEY_DISTANCE_TO_RIGHT = "dis_to_right";
    /*FARM PRODUCTION*/
    public String TABLE_FARM_PRODUCTION = "farm_production";
    private String KEY_GRADE_A = "grade_a";
    private final String KEY_GRADE_B = "grade_b";
    private String KEY_GRADE_C = "grade_c";
    /*TRANSPORT HOUSE TO MARKET*/
    public String TABLE_TRANS_HSE_TO_MARKET = "trans_hse_to_market";
    private String KEY_TRANS_DATE = "trans_date";
    private String KEY_TRANS_MODE_ID = "trans_mode_id";
    private String KEY_DELIVERY_DATE = "delivery_date";
    // table farm income
    public String TABLE_FARM_INCOME = "farm_income";
    // //table farm assesment form names
    // private String TABLE_FARM_ASS_FORM_NAMES = "farm_ass_form_names";
    // private String KEY_FORM_ID = "form_id";
    // private String KEY_FORM_NAME = "form_name";
    // //table form types
    // private String TABLE_FARM_ASS_FORM_TYPES = "farm_ass_form_types";
    // private String KEY_FORM_TYPE = "form_type";
    private String KEY_SPRAY_TYPE = "spray_type";
    public String TABLE_HERBICIDES = "herbicides";
    public String TABLE_FOLIAR_FEED = "foliar_feed";
    // table other crops
    public static String TABLE_OTHER_CROPS = "other_crops";
    private String KEY_CROP_ID = "crop_id";
    private String KEY_CROP_NAME = "crop_name";

    public String KEY_EXT_TRAIN_ID = "ext_train_id";

    private String KEY_FARMER_INTENT = "show_intent";

    private String KEY_OTHER_CROP_TWO = "other_crops_two";

    private String KEY_OTHER_CROP_THREE = "other_crops_three";
    public static String TABLE_WACK_FARMERS = "wack_farmers";

    private String KEY_WACK_STATUS = "wack_status";

    private String KEY_COLLECTION_METHOD = "collection_method";

    private String KEY_SAMPLING_STATION = "sampling_station";

    public String TABLE_RE_REGISTERED_FARMERS = "re_registered_farmers";

    private String KEY_PICKING_COUNT = "picking_count";

    private String KEY_TRANPORT_COUNT = "transport_count";

    private String KEY_DELIVERY_COUNT = "delivery_count";
    public String TABLE_MANUAL_INPUT_FINGERPRINT_RECAPTURE = "manual_input_fingerprint_recapture";

    private String KEY_BOLL_WEIGHT = "boll_weight";
    /*PRODUCT PURCHASES*/
    public String TABLE_PRODUCT_PURCHASES = "product_purchases";
    public String KEY_GRADE_ID = "grade_id";
    public String KEY_PRICE = "price";
    public String KEY_WEIGHT = "weight";
    public String KEY_PROPERTY_OWNED = "property_owned";
    public String KEY_RECEIPT_NO = "receipt_no";
    public String KEY_DEDUCTIONS = "deductions";
    /*COTTON DEDUCTIONS*/
    public String TABLE_COTTON_DEDUCTIONS = "cotton_deductions";
    private String KEY_DELIVERIES = "deliveries";
    private String KEY_SEASON_ID = "season_id";
    private String KEY_SEASON_NAME = "season_name";
    /*COLLECTED ORDERS*/
    public String TABLE_COLLECTED_ORDERS = "collected_orders";
    public String KEY_ORDERS_COUNT = "orders_count";
    public String KEY_TOTAL_AMOUNT = "total_amount";
    /*RECOVERED CASH*/
    public String TABLE_RECOVERED_CASH = "recovered_cash";
    public String KEY_TIMES = "times";
    /*FARM HISTORY*/
    public String TABLE_FARM_HISTORY = "farm_history";
    public String KEY_TOTAL_LAND_HOLDING_SIZE = "total_land_holding_size";
    /*FARM HISTORY ITEM*/
    public String TABLE_FARM_HISTORY_ITEMS = "farm_history_items";
    public String KEY_FARM_HISTORY_ID = "farm_history_id";
    public String KEY_ACRES = "hectares";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // farmers that have been re-registered
        String CREATE_TABLE_RE_REGISTERED_FARMERS = "CREATE TABLE IF NOT EXISTS " + TABLE_RE_REGISTERED_FARMERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_COMPANY_ID + " TEXT," + KEY_USER_ID + " TEXT," + KEY_FARMER_ID + " TEXT," + KEY_GEN_ID + " TEXT," + KEY_PIC
                + " TEXT UNIQUE," + KEY_LEFT_THUMB + " TEXT," + KEY_RIGHT_THUMB + " TEXT" + ")";
        // farmers that have collected inputs manually
        String CREATE_TABLE_MANUAL_INPUT_FINGERPRINT_RECAPTURE = "CREATE TABLE IF NOT EXISTS "
                + TABLE_MANUAL_INPUT_FINGERPRINT_RECAPTURE + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FARMER_ID + " TEXT," + KEY_GEN_ID
                + " TEXT," + KEY_LEFT_THUMB + " TEXT UNIQUE," + KEY_RIGHT_THUMB + " TEXT" + ")";
        // farmers with bad passport photos or bad fingerprints
        String CREATE_TABLE_WACK_FARMERS = "CREATE TABLE IF NOT EXISTS " + TABLE_WACK_FARMERS + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_FARMER_ID + " TEXT," + KEY_FNAME + " TEXT," + KEY_LNAME + " TEXT,"
                + KEY_GEN_ID + " TEXT," + KEY_VILLAGE_ID + " TEXT," + KEY_WACK_STATUS + " TEXT" + ")";

        String CREATE_TABLE_COUNTRIES = "CREATE TABLE IF NOT EXISTS " + TABLE_COUNTRIES + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_COUNTRY_ID + " TEXT," + KEY_COUNTRY_NAME + " TEXT" + ")";

        String CREATE_TABLE_REGIONS = "CREATE TABLE IF NOT EXISTS " + TABLE_REGIONS + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_COUNTRY_ID + " TEXT," + KEY_REGION_ID + " TEXT," + KEY_REGION_NAME
                + " TEXT" + ")";

        String CREATE_TABLE_DISTRICTS = "CREATE TABLE IF NOT EXISTS " + TABLE_DISTRICTS + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_REGION_ID + " TEXT," + KEY_DISTRICT_ID + " TEXT," + KEY_DISTRICT_NAME
                + " TEXT" + ")";

        String CREATE_TABLE_WARDS = "CREATE TABLE IF NOT EXISTS " + TABLE_WARDS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_DISTRICT_ID + " TEXT," + KEY_WARD_ID + " TEXT," + KEY_WARD_NAME + " TEXT," + KEY_BOLL_WEIGHT + " TEXT" + ")";

        String CREATE_TABLE_VILLAGES = "CREATE TABLE IF NOT EXISTS " + TABLE_VILLAGES + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_WARD_ID + " TEXT," + KEY_VILLAGE_ID + " TEXT," + KEY_VILLAGE_NAME
                + " TEXT" + ")";

        String CREATE_TABLE_SUB_VILLAGES = "CREATE TABLE IF NOT EXISTS " + TABLE_SUBVILLAGES + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_VILLAGE_ID + " TEXT," + KEY_SUBVILLAGE_ID + " TEXT,"
                + KEY_SUBVILLAGE_NAME + " TEXT" + ")";

        String CREATE_FARMERS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_FARMERS + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_FNAME + " TEXT," + KEY_LNAME + " TEXT," + KEY_GENDER + " TEXT,"
                + KEY_ID_NO + " TEXT," + KEY_PH_NO + " TEXT," + KEY_EMAIL + " TEXT," + KEY_POST + " TEXT," + KEY_VILLAGE
                + " TEXT," + KEY_SUB_VILLAGE + " TEXT," + KEY_PIC + " TEXT UNIQUE," + KEY_LEFT_THUMB + " TEXT,"
                + KEY_RIGHT_THUMB + " TEXT," + KEY_LATITUDE + " TEXT," + KEY_LONGITUDE + " TEXT," + KEY_FARMER_INTENT + " TEXT,"
                + KEY_ESTIMATED_FARM_AREA + " TEXT," + KEY_FARM_VILLAGE_ONE + " TEXT," + KEY_ESTIMATED_FARM_AREA2 + " TEXT,"
                + KEY_FARM_VILLAGE_TWO + " TEXT," + KEY_ESTIMATED_FARM_AREA3 + " TEXT," + KEY_FARM_VILLAGE_THREE + " TEXT,"
                + KEY_ESTIMATED_FARM_AREA4 + " TEXT," + KEY_FARM_VILLAGE_FOUR + " TEXT," + KEY_OTHER_CROP_ONE + " TEXT,"
                + KEY_OTHER_CROP_TWO + " TEXT," + KEY_OTHER_CROP_THREE + " TEXT,"
                + KEY_COMPANY_ID + " TEXT," + KEY_CONTRACT_ID + " TEXT," + KEY_USER_ID + " TEXT" + ")";

        String CREATE_REGISTERED_FARMERS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_REGISTERED_FARMERS + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_FARMER_ID + " TEXT," + KEY_PH_NO + " TEXT," + KEY_GENDER + " TEXT," + KEY_FNAME + " TEXT," + KEY_LNAME + " TEXT,"
                + KEY_GEN_ID + " TEXT," + KEY_CARD_NO + " TEXT," + KEY_CONTRACT_ID + " TEXT," + KEY_CREDIT_STATUS + " TEXT," + KEY_CONTRACT_STATUS + " TEXT,"
                + KEY_FINGERPRINT_STATUS + " TEXT," + KEY_VILLAGE_ID + " INTEGER," + KEY_COMPANY_ID + " TEXT" + ")";

        String CREATE_UPDATED_FARMERS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_UPDATED_FARMERS + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_FARMER_ID + " TEXT," + KEY_PH_NO + " TEXT," + KEY_GENDER + " TEXT," + KEY_FNAME + " TEXT," + KEY_LNAME + " TEXT,"
                + KEY_GEN_ID + " TEXT," + KEY_CARD_NO + " TEXT," + KEY_CONTRACT_ID + " TEXT," + KEY_CREDIT_STATUS + " TEXT," + KEY_CONTRACT_STATUS + " TEXT,"
                + KEY_FINGERPRINT_STATUS + " TEXT," + KEY_VILLAGE_ID + " INTEGER," + KEY_COMPANY_ID + " TEXT" + ")";

        String CREATE_FARMINPUTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_FARM_INPUTS + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_INPUT_ID + " TEXT," + KEY_INPUT_CATEGORY + " TEXT," + KEY_INPUT_TYPE
                + " TEXT" + ")";

        String CREATE_TABLE_ASSIGNED_INPUTS = "CREATE TABLE IF NOT EXISTS " + TABLE_ASSIGNED_INPUTS + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_ORDER_ID + " TEXT," + KEY_ASS_INPUT_ID + " TEXT," + KEY_INPUT_ID
                + " TEXT," + KEY_INPUT_TYPE + " TEXT," + KEY_INPUT_QUANTITY + " TEXT," + KEY_INPUT_UNIT + " TEXT,"
                + KEY_FARMER_ID + " TEXT," + KEY_CARD_NO + " TEXT," + KEY_ORDER_NUM + " TEXT," + KEY_INPUT_TOTAL + " TEXT"
                + ")";

        String CREATE_TABLE_TRAININGS = "CREATE TABLE IF NOT EXISTS " + TABLE_TRAININGS + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_EXT_TRAIN_ID + " TEXT," + KEY_TRAIN_ID + " TEXT," + KEY_TRAIN_DATE
                + " TEXT," + KEY_FARM_ID + " TEXT" + ")";


        String CREATE_TABLE_EXT_OFFICER_TRAINING = "CREATE TABLE IF NOT EXISTS " + TABLE_EXT_OFFICER_TRAINING + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_EXT_TRAIN_ID + " TEXT," + KEY_USER_ID + " TEXT," + KEY_TRAIN_CAT_ID + " TEXT,"
                + KEY_TRAIN_CAT + " TEXT," + KEY_TRAIN_DATE + " TEXT," + KEY_FARM_ID + " TEXT," + KEY_VILLAGE_ID + " TEXT,"
                + KEY_FARMER_ID + " TEXT" + ")";

        String CREATE_TABLE_TRAINING_CATEGORIES = "CREATE TABLE IF NOT EXISTS " + TABLE_TRAIN_CATEGORIES + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_TRAIN_CAT_ID + " TEXT," + KEY_EXT_TRAIN_ID + " TEXT," + KEY_TRAIN_CAT
                + " TEXT" + ")";

        String CREATE_TABLE_TRAINING_TYPES = "CREATE TABLE IF NOT EXISTS " + TABLE_TRAIN_TYPES + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_TRAIN_CAT_ID + " TEXT," + KEY_TRAIN_ID + " TEXT," + KEY_TRAIN_TYPE
                + " TEXT" + ")";

        String CREATE_TABLE_ASSIGNED_TRAININGS = "CREATE TABLE IF NOT EXISTS " + TABLE_ASSIGNED_TRAININGS + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_EXT_TRAIN_ID + " TEXT," + KEY_TRAIN_CAT_ID + " TEXT," + KEY_TRAIN_START_TIME
                + " TEXT UNIQUE," + KEY_TRAIN_STOP_TIME + " TEXT," + KEY_TRAIN_LATITUDE + " TEXT," + KEY_TRAIN_LONGITUDE + " TEXT,"
                + KEY_USER_ID + " TEXT," + KEY_COMPANY_ID + " TEXT" + ")";

        String CREATE_TABLE_USERS = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_USER_ID + " TEXT," + KEY_USER_NAME + " TEXT," + KEY_USER_PWD + " TEXT," + KEY_VILLAGE_ID
                + " TEXT," + KEY_COMPANY_ID + " TEXT" + ")";

        String CREATE_TABLE_USER_VILLAGE = "CREATE TABLE IF NOT EXISTS " + TABLE_USER_VILLAGE + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_USER_ID + " TEXT," + KEY_VILLAGE_ID + " TEXT" + ")";

        String CREATE_TABLE_COMPANIES = "CREATE TABLE IF NOT EXISTS " + TABLE_COMPANIES + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_COMPANY_ID + " TEXT," + KEY_COMPANY_NAME + " TEXT" + ")";

        String CREATE_TABLE_FARMER_TIMES = "CREATE TABLE IF NOT EXISTS " + TABLE_FARMER_TIMES + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_EXT_TRAIN_ID + " TEXT," + KEY_TRAIN_ID + " TEXT," + KEY_FARMER_ID + " TEXT,"
                + KEY_USER_ID + " TEXT," + KEY_FARMER_TIME_IN + " TEXT," + KEY_FARMER_TIME_OUT + " TEXT," + KEY_LATITUDE
                + " TEXT," + KEY_LONGITUDE + " TEXT," + KEY_COMPANY_ID + " TEXT" + ")";

        String CREATE_TABLE_FINGER_ONE = "CREATE TABLE IF NOT EXISTS " + TABLE_FINGER_ONE + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_FINGER_ID + " TEXT," + KEY_COMPANY_ID + " TEXT," + KEY_USER_ID
                + " TEXT," + KEY_SOIL_TYPE + " TEXT," + KEY_WATER_LOG_RISK + " TEXT," + KEY_EROSION_PREVENTION + " TEXT,"
                + KEY_CROP_ROTATION + " TEXT," + KEY_RATOON + " TEXT," + KEY_CROP_RESIDUES + " TEXT," + KEY_MANURE
                + " TEXT," + KEY_LAND_PREPARATION + " TEXT," + KEY_FARM_ID + " TEXT," + KEY_SEED_BED_PREPARATION + " TEXT,"
                + KEY_FORM_DATE + " TEXT" + ")";

        String CREATE_TABLE_FINGER_TWO = "CREATE TABLE IF NOT EXISTS " + TABLE_FINGER_TWO + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_FINGER_ID + " TEXT," + KEY_COMPANY_ID + " TEXT," + KEY_USER_ID
                + " TEXT," + KEY_CORRECT_SEED + " TEXT," + KEY_ROW_SPACING + " TEXT," + KEY_SEEDS_PER_STATION + " TEXT,"
                + KEY_FARM_ID + " TEXT," + KEY_PLANTING_TIME + " TEXT," + KEY_FORM_DATE + " TEXT" + ")";

        String CREATE_TABLE_FINGER_THREE = "CREATE TABLE IF NOT EXISTS " + TABLE_FINGER_THREE + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_FINGER_ID + " TEXT," + KEY_COMPANY_ID + " TEXT," + KEY_USER_ID
                + " TEXT," + KEY_GAP_FILL + " TEXT," + KEY_GAP_FILL_ON_EMERGENCE + " TEXT," + KEY_THINNING_NUM + " TEXT,"
                + KEY_FARM_ID + " TEXT," + KEY_THIN_AFTER_EMERGENCE + " TEXT," + KEY_FORM_DATE + " TEXT" + ")";

        String CREATE_TABLE_FINGER_FOUR = "CREATE TABLE IF NOT EXISTS " + TABLE_FINGER_FOUR + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_FINGER_ID + " TEXT," + KEY_COMPANY_ID + " TEXT," + KEY_USER_ID
                + " TEXT," + KEY_FIRST_BRANCH + " TEXT," + KEY_FOLIAR + " TEXT," + KEY_FARM_ID + " TEXT," + KEY_WEEDS
                + " TEXT," + KEY_FORM_DATE + " TEXT" + ")";

        String CREATE_TABLE_FINGER_FIVE = "CREATE TABLE IF NOT EXISTS " + TABLE_FINGER_FIVE + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_FINGER_ID + " TEXT," + KEY_COMPANY_ID + " TEXT," + KEY_USER_ID
                + " TEXT," + KEY_PEST_LEVEL + " TEXT," + KEY_PEST_DAMAGE + " TEXT," + KEY_LAST_SCOUT + " TEXT,"
                + KEY_EMPTY_PESTICIDE_CANS + " TEXT," + KEY_PEG_BOARD_AVAILABILITY + " TEXT," + KEY_SCOUTING_METHOD + " TEXT,"
                + KEY_SPRAY_TIME + " TEXT," + KEY_PEST_ABSENSE_DURATION + " TEXT," + KEY_CORRECT_USE_PESTICIDE + " TEXT,"
                + KEY_FARM_ID + " TEXT," + KEY_SAFE_USAGE_CANS + " TEXT," + KEY_FORM_DATE + " TEXT" + ")";

        String CREATE_TABLE_FARMS = "CREATE TABLE IF NOT EXISTS " + TABLE_FARMS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_FARM_ID + " TEXT," + KEY_FARMER_ID + " TEXT," + KEY_FARM_ASS + " TEXT," + KEY_FARM_NAME + " TEXT," + KEY_ACTUAL_AREA
                + " TEXT," + KEY_VILLAGE_ID + " TEXT," + KEY_ESTIMATED_AREA + " TEXT," + KEY_PERIMETER + " TEXT,"
                + KEY_LATITUDE + " TEXT," + KEY_LONGITUDE + " TEXT," + KEY_COMPANY_ID + " TEXT" + ")";

        String CREATE_TABLE_SIGNED_DOCS = "CREATE TABLE IF NOT EXISTS " + TABLE_SIGNED_DOCS + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_FARMER_ID + " TEXT," + KEY_USER_ID + " TEXT,"
                + KEY_COMPANY_ID + " TEXT" + ")";

        String CREATE_TABLE_MAPPED_FARMS = "CREATE TABLE IF NOT EXISTS " + TABLE_MAPPED_FARMS + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_FARM_ID + " TEXT," + KEY_LATITUDE + " TEXT," + KEY_LONGITUDE + " TEXT," +
                KEY_POINTS + " TEXT," + KEY_ACTUAL_AREA + " TEXT," + KEY_PERIMETER + " TEXT," +
                KEY_COMPANY_ID + " TEXT," + KEY_USER_ID + " TEXT,"
                + KEY_VILLAGE_ID + " TEXT" + ")";

        String CREATE_TABLE_UPDATE_FARM_AREA = "CREATE TABLE IF NOT EXISTS " + TABLE_FARM_AREA_UPDATES + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_FARM_ID + " TEXT," + KEY_USER_ID + " TEXT," + KEY_COMPANY_ID + " TEXT,"
                + KEY_FARM_SIZE + " TEXT" + ")";

        // String CREATE_TABLE_FARM_ASS_FORM_NAMES =
        // "CREATE TABLE IF NOT EXISTS " + TABLE_FARM_ASS_FORM_NAMES + "("
        // + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FORM_ID + " TEXT," +
        // KEY_FORM_NAME + " TEXT" + ")";
        //
        // String CREATE_TABLE_FARM_ASS_FORM_TYPES =
        // "CREATE TABLE IF NOT EXISTS " + TABLE_FARM_ASS_FORM_TYPES + "("
        // + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FORM_TYPE_ID + " TEXT," +
        // KEY_FORM_ID + " TEXT," + KEY_FORM_TYPE + " TEXT" + ")";
        String CREATE_TABLE_FARM_ASS_MAJOR = "CREATE TABLE IF NOT EXISTS " + TABLE_FARM_ASS_MAJOR + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_FORM_TYPE_ID + " TEXT," + KEY_ACTIVITY_METHOD + " TEXT," + KEY_FARM_ID
                + " TEXT," + KEY_ACTIVITY_DATE + " TEXT," + KEY_COLLECT_DATE + " TEXT," + KEY_FAMILY_HOURS + " TEXT,"
                + KEY_HIRED_HOURS + " TEXT," + KEY_MONEY_OUT + " TEXT," + KEY_REMARKS + " TEXT," + KEY_USER_ID
                + " TEXT," + KEY_LATITUDE + " TEXT," + KEY_LONGITUDE + " TEXT," + KEY_COMPANY_ID + " TEXT" + ")";

        String CREATE_TABLE_FARM_OTHER_CROPS = "CREATE TABLE IF NOT EXISTS " + TABLE_FARM_OTHER_CROPS + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_FARM_ID + " TEXT," + KEY_COLLECT_DATE + " TEXT," + KEY_CROP_ID1
                + " TEXT," + KEY_CROP_ID2 + " TEXT," + KEY_CROP_ID3 + " TEXT," + " TEXT," + KEY_USER_ID + " TEXT,"
                + KEY_LATITUDE + " TEXT," + KEY_LONGITUDE + " TEXT," + KEY_COMPANY_ID + " TEXT" + ")";

        String CREATE_TABLE_PLANTING_RAINS = "CREATE TABLE IF NOT EXISTS " + TABLE_PLANTING_RAINS + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_FARM_ID + " TEXT," + KEY_RAIN_DATE + " TEXT," + KEY_REMARKS + " TEXT, "
                + KEY_COLLECT_DATE + " TEXT," + " TEXT," + KEY_USER_ID + " TEXT," + KEY_LATITUDE + " TEXT," + KEY_LONGITUDE
                + " TEXT," + KEY_COMPANY_ID + " TEXT" + ")";

        String CREATE_TABLE_FARM_ASS_MEDIUM = "CREATE TABLE IF NOT EXISTS " + TABLE_FARM_ASS_MEDIUM + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_FARM_ID + " TEXT," + KEY_FORM_TYPE_ID + " TEXT," + KEY_ACTIVITY_DATE
                + " TEXT," + KEY_COLLECT_DATE + " TEXT," + KEY_ACTIVITY_METHOD + " TEXT, " + KEY_FAMILY_HOURS + " TEXT,"
                + KEY_HIRED_HOURS + " TEXT," + KEY_MONEY_OUT + " TEXT," + KEY_INPUT_ID + " TEXT," + KEY_INPUT_QUANTITY
                + " TEXT," + KEY_SPRAY_TYPE + " TEXT," + KEY_USER_ID + " TEXT," + KEY_LATITUDE + " TEXT," + KEY_LONGITUDE
                + " TEXT," + KEY_COMPANY_ID + " TEXT" + ")";

        String CREATE_TABLE_HERBICIDES = "CREATE TABLE IF NOT EXISTS " + TABLE_HERBICIDES + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_INPUT_ID + " TEXT," + KEY_INPUT_TYPE + " TEXT" + ")";

        String CREATE_TABLE_FOLIAR_FEED = "CREATE TABLE IF NOT EXISTS " + TABLE_FOLIAR_FEED + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_INPUT_ID + " TEXT," + KEY_INPUT_TYPE + " TEXT" + ")";

        String CREATE_TABLE_FINGERPRINTS = "CREATE TABLE IF NOT EXISTS " + TABLE_FINGERPRINTS + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_FARMER_ID + " TEXT," + KEY_GEN_ID + " TEXT," + KEY_FILE_PATH + " TEXT,"
                + KEY_COMPANY_ID + " TEXT" + ")";

        String CREATE_TABLE_PESTICIDES = "CREATE TABLE IF NOT EXISTS " + TABLE_PESTICIDES + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_INPUT_ID + " TEXT," + KEY_INPUT_TYPE + " TEXT" + ")";

        String CREATE_TABLE_TRANSPORT_MODES = "CREATE TABLE IF NOT EXISTS " + TABLE_TRANSPORT_MODES + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_TRANS_MODE_ID + " TEXT," + KEY_TRANSPORT_MODE + " TEXT" + ")";

        String CREATE_TABLE_GERMINATION = "CREATE TABLE IF NOT EXISTS " + TABLE_GERMINATION + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_FARM_ID + " TEXT," + KEY_GERM_DATE + " TEXT," + KEY_COLLECT_DATE
                + " TEXT," + KEY_REMARKS + " TEXT," + KEY_USER_ID + " TEXT," + KEY_LATITUDE + " TEXT," + KEY_LONGITUDE + " TEXT,"
                + KEY_COMPANY_ID + " TEXT" + ")";

        String CREATE_TABLE_MOLASSES_TRAP_CATCHES = "CREATE TABLE IF NOT EXISTS " + TABLE_MOLASSES_TRAP_CATCHES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FARM_ID + " TEXT," + KEY_TRAP_DATE + " TEXT,"
                + KEY_COLLECT_DATE + " TEXT," + KEY_TRAP_ONE + " TEXT," + KEY_TRAP_TWO + " TEXT," + KEY_LATITUDE + " TEXT,"
                + KEY_LONGITUDE + " TEXT," + KEY_ACTION_TAKEN + " TEXT," + KEY_USER_ID + " TEXT," + KEY_COMPANY_ID + " TEXT"
                + ")";

        String CREATE_TABLE_YIELD_ESTIMATE = "CREATE TABLE IF NOT EXISTS " + TABLE_YIELD_ESTIMATE + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_FARM_ID + " TEXT," + KEY_SAMPLING_STATION + " TEXT," + KEY_NUM_OF_PLANTS + " TEXT," + KEY_NUM_OF_BOLLS
                + " TEXT," + KEY_DISTANCE_TO_LEFT + " TEXT," + KEY_DISTANCE_TO_RIGHT + " TEXT," + KEY_COLLECT_DATE + " TEXT,"
                + KEY_LATITUDE + " TEXT," + KEY_LONGITUDE + " TEXT," + KEY_USER_ID + " TEXT," + KEY_COMPANY_ID + " TEXT" + ")";

        String CREATE_TABLE_FARM_PRODUCTION = "CREATE TABLE IF NOT EXISTS " + TABLE_FARM_PRODUCTION + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_FARM_ID + " TEXT," + KEY_PICKING_COUNT + " TEXT," + KEY_GRADE_A + " TEXT,"
                + KEY_GRADE_B + " TEXT," + KEY_GRADE_C + " TEXT," + KEY_COLLECT_DATE + " TEXT," + KEY_PICKING_DATE
                + " TEXT," + KEY_USER_ID + " TEXT," + KEY_LATITUDE + " TEXT," + KEY_LONGITUDE + " TEXT," + KEY_COMPANY_ID
                + " TEXT" + ")";

        String CREATE_TABLE_TRANS_HSE_TO_MARKET = "CREATE TABLE IF NOT EXISTS " + TABLE_TRANS_HSE_TO_MARKET + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FARM_ID + " TEXT," + KEY_TRANPORT_COUNT + " TEXT,"
                + KEY_TRANS_DATE + " TEXT," + KEY_TRANS_MODE_ID + " TEXT," + KEY_MONEY_OUT + " TEXT," + KEY_COLLECT_DATE
                + " TEXT," + KEY_USER_ID + " TEXT," + KEY_LATITUDE + " TEXT," + KEY_LONGITUDE + " TEXT," + KEY_COMPANY_ID
                + " TEXT" + ")";

        String CREATE_TABLE_FARM_INCOME = "CREATE TABLE IF NOT EXISTS " + TABLE_FARM_INCOME + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_FARM_ID + " TEXT," + KEY_DELIVERY_COUNT + " TEXT," + KEY_DELIVERY_DATE
                + " TEXT," + KEY_GRADE_A + " TEXT," + KEY_GRADE_B + " TEXT," + KEY_GRADE_C + " TEXT," + KEY_LATITUDE
                + " TEXT," + KEY_LONGITUDE + " TEXT," + KEY_COLLECT_DATE + " TEXT," + KEY_USER_ID + " TEXT,"
                + KEY_COMPANY_ID + " TEXT" + ")";

        String CREATE_TABLE_OTHER_CROPS = "CREATE TABLE IF NOT EXISTS " + TABLE_OTHER_CROPS + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_CROP_ID + " TEXT," + KEY_CROP_NAME + " TEXT" + ")";

        String CREATE_TABLE_COLLECTED_INPUTS = "CREATE TABLE IF NOT EXISTS " + TABLE_COLLECTED_INPUTS + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_ORDER_ID + " TEXT," + KEY_COLLECT_DATE + " TEXT," + KEY_USER_ID
                + " TEXT, " + KEY_COLLECTION_METHOD + " TEXT" + ")";

        String CREATE_TABLE_SCOUTING = "CREATE TABLE IF NOT EXISTS " + TABLE_SCOUTING + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_FARM_ID + " TEXT," + KEY_ACTIVITY_DATE + " TEXT," + KEY_FAMILY_HOURS
                + " TEXT," + KEY_HIRED_HOURS + " TEXT," + KEY_MONEY_OUT + " TEXT," + KEY_BOLL_WORM + " TEXT,"
                + KEY_JASSID + " TEXT," + KEY_STAINER + " TEXT," + KEY_APHID + " TEXT," + KEY_BENEFICIAL + " TEXT,"
                + KEY_SPRAY_DECISION + " TEXT," + KEY_ACTION_TAKEN + " TEXT," + KEY_LATITUDE + " TEXT," + KEY_LONGITUDE
                + " TEXT," + KEY_COLLECT_DATE + " TEXT," + KEY_USER_ID + " TEXT," + KEY_COMPANY_ID + " TEXT" + ")";

        String CREATE_TABLE_SHOW_INTENT = "CREATE TABLE IF NOT EXISTS " + TABLE_SHOW_INTENT + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_USER_ID + " TEXT," + KEY_COMPANY_ID + " TEXT," + KEY_FARMER_ID + " TEXT" + ")";

        String CREATE_TABLE_FARM_DATA_COLLECTED = "CREATE TABLE IF NOT EXISTS " + TABLE_FARM_DATA_COLLECTED + "("
                + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_FARM_ID + " TEXT, " + KEY_FORM_TYPE_ID + " TEXT" + ")";

        String CREATE_TABLE_PRODUCT_PURCHASES = "CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCT_PURCHASES + "("
                + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_FARMER_ID + " TEXT, " + KEY_GRADE_ID + " TEXT, "
                + KEY_WEIGHT + " TEXT, " + KEY_PRICE + " TEXT, " + KEY_DEDUCTIONS + " TEXT, " + KEY_RECEIPT_NO + " TEXT, "
                + KEY_USER_ID + " TEXT, " + KEY_COMPANY_ID + " TEXT" + ")";

        String CREATE_TABLE_COTTON_DEDUCTIONS = "CREATE TABLE IF NOT EXISTS " + TABLE_COTTON_DEDUCTIONS + "("
                + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_FARMER_ID + " TEXT, " + KEY_DELIVERIES + " TEXT, "
                + KEY_DEDUCTIONS + " TEXT, " + KEY_SEASON_ID + " TEXT, " + KEY_SEASON_NAME + " TEXT" + ")";

        String CREATE_TABLE_COLLECTED_ORDERS = "CREATE TABLE IF NOT EXISTS " + TABLE_COLLECTED_ORDERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_FARMER_ID + " TEXT, " + KEY_ORDERS_COUNT + " TEXT, "
                + KEY_TOTAL_AMOUNT + " TEXT, " + KEY_SEASON_ID + " TEXT, " + KEY_SEASON_NAME + " TEXT" + ")";

        String CREATE_TABLE_RECOVERED_CASH = "CREATE TABLE IF NOT EXISTS " + TABLE_RECOVERED_CASH + "("
                + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_FARMER_ID + " TEXT, " + KEY_TIMES + " TEXT, "
                + KEY_TOTAL_AMOUNT + " TEXT, " + KEY_SEASON_ID + " TEXT, " + KEY_SEASON_NAME + " TEXT" + ")";

        String CREATE_TABLE_FARM_HISTORY = "CREATE TABLE IF NOT EXISTS " + TABLE_FARM_HISTORY + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_FARMER_ID + " TEXT," + KEY_SEASON_ID + " TEXT," + KEY_SEASON_NAME + " TEXT,"
                + KEY_TOTAL_LAND_HOLDING_SIZE + " TEXT" + ")";

        String CREATE_TABLE_FARM_HISTORY_ITEM = "CREATE TABLE IF NOT EXISTS " + TABLE_FARM_HISTORY_ITEMS + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_FARM_HISTORY_ID + " TEXT," + KEY_PROPERTY_OWNED + " INTEGER," + KEY_CROP_ID + " TEXT," + KEY_WEIGHT + " TEXT,"
                + KEY_ACRES + " TEXT" + ")";

        //TABLE for farmer questionaire
        String CREATE_TABLE_FARMER_QUESTIONAIRE = "CREATE TABLE IF NOT EXISTS " + TABLE_QUESTIONAIRE_STRUCTURE + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," + KEY_QUESTION_CATEGORY + " TEXT," + KEY_QUESTION + " TEXT," +
                KEY_ANSWER_TYPE + " TEXT" + "," + KEY_ANSWER + " TEXT" + ")";//ANSWETYPES:BINARY,Text,LIST
        //TABLE for farmer questionaire answers
        String CREATE_TABLE_FARMER_QUESTIONAIRE_ANSWERS = "CREATE TABLE IF NOT EXISTS " + TABLE_QUESTIONAIRE_ANSWERS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," + KEY_FARMER_ID + " TEXT," + KEY_FARM_ID + " INTEGER,"
                + KEY_SEASON_ID + " INTEGER," + KEY_COMPANY_ID + " INTEGER," + KEY_SURVEY_DATE + " TEXT," + KEY_QUESTIONNAIRE_ID + " TEXT," + KEY_ANSWER + " TEXT" + ")";//ANSWER  will be json type



        db.execSQL(CREATE_FARMERS_TABLE);
        db.execSQL(CREATE_FARMINPUTS_TABLE);
        db.execSQL(CREATE_TABLE_COUNTRIES);
        db.execSQL(CREATE_TABLE_REGIONS);
        db.execSQL(CREATE_TABLE_DISTRICTS);
        db.execSQL(CREATE_TABLE_WARDS);
        db.execSQL(CREATE_TABLE_VILLAGES);
        db.execSQL(CREATE_TABLE_SUB_VILLAGES);
        db.execSQL(CREATE_TABLE_ASSIGNED_INPUTS);
        db.execSQL(CREATE_TABLE_TRAININGS);
        db.execSQL(CREATE_TABLE_ASSIGNED_TRAININGS);
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_USER_VILLAGE);
        db.execSQL(CREATE_TABLE_COMPANIES);
        db.execSQL(CREATE_TABLE_TRAINING_CATEGORIES);
        db.execSQL(CREATE_TABLE_TRAINING_TYPES);
        db.execSQL(CREATE_TABLE_FARMER_TIMES);
        db.execSQL(CREATE_TABLE_FINGER_ONE);
        db.execSQL(CREATE_TABLE_FINGER_TWO);
        db.execSQL(CREATE_TABLE_FINGER_THREE);
        db.execSQL(CREATE_TABLE_FINGER_FOUR);
        db.execSQL(CREATE_TABLE_FINGER_FIVE);
        db.execSQL(CREATE_TABLE_FARMS);
        db.execSQL(CREATE_TABLE_SIGNED_DOCS);
        db.execSQL(CREATE_TABLE_MAPPED_FARMS);
        // db.execSQL(CREATE_TABLE_FARM_ASS_FORM_TYPES);
        db.execSQL(CREATE_TABLE_FARM_ASS_MAJOR);
        db.execSQL(CREATE_TABLE_FARM_OTHER_CROPS);
        db.execSQL(CREATE_TABLE_PLANTING_RAINS);
        db.execSQL(CREATE_TABLE_HERBICIDES);
        db.execSQL(CREATE_TABLE_FOLIAR_FEED);
        db.execSQL(CREATE_TABLE_FARM_ASS_MEDIUM);
        db.execSQL(CREATE_TABLE_OTHER_CROPS);
        db.execSQL(CREATE_TABLE_GERMINATION);
        db.execSQL(CREATE_TABLE_MOLASSES_TRAP_CATCHES);
        db.execSQL(CREATE_TABLE_SCOUTING);
        db.execSQL(CREATE_TABLE_PESTICIDES);
        db.execSQL(CREATE_TABLE_TRANSPORT_MODES);
        db.execSQL(CREATE_TABLE_FARM_PRODUCTION);
        db.execSQL(CREATE_TABLE_TRANS_HSE_TO_MARKET);
        db.execSQL(CREATE_TABLE_FARM_INCOME);
        db.execSQL(CREATE_TABLE_YIELD_ESTIMATE);
        db.execSQL(CREATE_TABLE_UPDATE_FARM_AREA);
        db.execSQL(CREATE_TABLE_COLLECTED_INPUTS);
        db.execSQL(CREATE_TABLE_FINGERPRINTS);
        db.execSQL(CREATE_TABLE_SHOW_INTENT);
        db.execSQL(CREATE_REGISTERED_FARMERS_TABLE);
        db.execSQL(CREATE_UPDATED_FARMERS_TABLE);
        db.execSQL(CREATE_TABLE_WACK_FARMERS);
        db.execSQL(CREATE_TABLE_RE_REGISTERED_FARMERS);
        db.execSQL(CREATE_TABLE_FARM_DATA_COLLECTED);
        db.execSQL(CREATE_TABLE_MANUAL_INPUT_FINGERPRINT_RECAPTURE);
        db.execSQL(CREATE_TABLE_EXT_OFFICER_TRAINING);
        db.execSQL(CREATE_TABLE_PRODUCT_PURCHASES);
        db.execSQL(CREATE_TABLE_COTTON_DEDUCTIONS);
        db.execSQL(CREATE_TABLE_COLLECTED_ORDERS);
        db.execSQL(CREATE_TABLE_RECOVERED_CASH);
        db.execSQL(CREATE_TABLE_FARM_HISTORY);
        db.execSQL(CREATE_TABLE_FARM_HISTORY_ITEM);
        db.execSQL(CREATE_TABLE_FARMER_QUESTIONAIRE);
        db.execSQL(CREATE_TABLE_FARMER_QUESTIONAIRE_ANSWERS);


    }


    /**
     * @param farmer
     */
    public void addRegisteredFarmers(RegisteredFarmer farmer) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FARMER_ID, farmer.getFarmerId());
        values.put(KEY_GEN_ID, farmer.getGenId());
        values.put(KEY_CARD_NO, farmer.getCardNo());
        values.put(KEY_FNAME, farmer.getLastName());
        values.put(KEY_LNAME, farmer.getFirstName());
        values.put(KEY_GEN_ID, farmer.getGenId());
        values.put(KEY_PH_NO, farmer.getPhone());
        values.put(KEY_CONTRACT_ID, farmer.getContract_number());
        values.put(KEY_GENDER, farmer.getGender());
        values.put(KEY_CONTRACT_STATUS, farmer.getContractStatus());
        values.put(KEY_CREDIT_STATUS, farmer.getCreditStatus());
        values.put(KEY_VILLAGE_ID, farmer.getVillageId());
        values.put(KEY_COMPANY_ID, farmer.getCompanyId());

        db.insert(TABLE_REGISTERED_FARMERS, null, values);

    }

    public List<RegisteredFarmer> getUnFingerprintedFarmers() {
        List<RegisteredFarmer> farmerlist = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_REGISTERED_FARMERS + " WHERE " + KEY_FINGERPRINT_STATUS + " = ''";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                RegisteredFarmer farmer = new RegisteredFarmer();
                farmer.setFarmerId(cursor.getString(cursor.getColumnIndex(KEY_FARMER_ID)));
                farmer.setGenId(cursor.getString(cursor.getColumnIndex(KEY_GEN_ID)));
                farmer.setFirstName(cursor.getString(cursor.getColumnIndex(KEY_FNAME)));
                farmer.setLastName(cursor.getString(cursor.getColumnIndex(KEY_LNAME)));
                farmer.setContractStatus(cursor.getString(cursor.getColumnIndex(KEY_CONTRACT_STATUS)));
                farmer.setCreditStatus(cursor.getString(cursor.getColumnIndex(KEY_CREDIT_STATUS)));
                farmer.setCompanyId(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));

                farmerlist.add(farmer);
            } while (cursor.moveToNext());
        }
        return farmerlist;
    }

    public List<RegisteredFarmer> getFingerprintedFarmers() {
        List<RegisteredFarmer> farmerlist = new ArrayList<RegisteredFarmer>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_REGISTERED_FARMERS + " WHERE " + KEY_FINGERPRINT_STATUS
                + " != ''";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                RegisteredFarmer farmer = new RegisteredFarmer();
                farmer.setFarmerId(cursor.getString(cursor.getColumnIndex(KEY_FARMER_ID)));
                farmer.setGenId(cursor.getString(cursor.getColumnIndex(KEY_GEN_ID)));

                farmerlist.add(farmer);
            } while (cursor.moveToNext());
        }
        return farmerlist;
    }

    public List<RegisteredFarmer> getAllRegisteredFarmers() {
        List<RegisteredFarmer> registeredFarmerlist = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_REGISTERED_FARMERS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                RegisteredFarmer farmer = new RegisteredFarmer();
                farmer.setFarmerId(cursor.getString(cursor.getColumnIndex(KEY_FARMER_ID)));
                farmer.setGenId(cursor.getString(cursor.getColumnIndex(KEY_GEN_ID)));
                farmer.setCardNo(cursor.getString(cursor.getColumnIndex(KEY_CARD_NO)));
                farmer.setFirstName(cursor.getString(cursor.getColumnIndex(KEY_FNAME)));
                farmer.setLastName(cursor.getString(cursor.getColumnIndex(KEY_LNAME)));
                farmer.setCompanyId(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));
                farmer.setPhone(cursor.getString(cursor.getColumnIndex(KEY_PH_NO)));
                farmer.setContract_number(cursor.getString(cursor.getColumnIndex(KEY_CONTRACT_ID)));
                farmer.setGender(cursor.getString(cursor.getColumnIndex(KEY_GENDER)));

                registeredFarmerlist.add(farmer);
            } while (cursor.moveToNext());
        }
        return registeredFarmerlist;
    }

    public List<RegisteredFarmer> getCreditFarmers() {
        List<RegisteredFarmer> creditFarmerlist = new ArrayList<RegisteredFarmer>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_REGISTERED_FARMERS + " WHERE " + KEY_CONTRACT_STATUS
                + " = 'contracted'  AND " + KEY_CREDIT_STATUS + " = 'yes'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                RegisteredFarmer farmer = new RegisteredFarmer();
                farmer.setFarmerId(cursor.getString(cursor.getColumnIndex(KEY_FARMER_ID)));
                farmer.setGenId(cursor.getString(cursor.getColumnIndex(KEY_GEN_ID)));
                farmer.setFirstName(cursor.getString(cursor.getColumnIndex(KEY_FNAME)));
                farmer.setLastName(cursor.getString(cursor.getColumnIndex(KEY_LNAME)));
                farmer.setContractStatus(cursor.getString(cursor.getColumnIndex(KEY_CONTRACT_STATUS)));
                farmer.setCreditStatus(cursor.getString(cursor.getColumnIndex(KEY_CREDIT_STATUS)));
                farmer.setCompanyId(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));

                creditFarmerlist.add(farmer);
            } while (cursor.moveToNext());
        }
        return creditFarmerlist;
    }

    public List<RegisteredFarmer> getNotContractedFarmers() {
        List<RegisteredFarmer> notContractedFarmerlist = new ArrayList<RegisteredFarmer>();

        String selectQuery = "SELECT * FROM " + TABLE_REGISTERED_FARMERS + " WHERE " + KEY_CONTRACT_STATUS
                + " = 'not contracted'  AND " + KEY_CREDIT_STATUS + " = 'no'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                RegisteredFarmer farmer = new RegisteredFarmer();
                farmer.setFarmerId(cursor.getString(cursor.getColumnIndex(KEY_FARMER_ID)));
                farmer.setGenId(cursor.getString(cursor.getColumnIndex(KEY_GEN_ID)));
                farmer.setCardNo(cursor.getString(cursor.getColumnIndex(KEY_CARD_NO)));
                farmer.setFirstName(cursor.getString(cursor.getColumnIndex(KEY_FNAME)));
                farmer.setLastName(cursor.getString(cursor.getColumnIndex(KEY_LNAME)));
                farmer.setContractStatus(cursor.getString(cursor.getColumnIndex(KEY_CONTRACT_STATUS)));
                farmer.setCreditStatus(cursor.getString(cursor.getColumnIndex(KEY_CREDIT_STATUS)));
                farmer.setCompanyId(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));

                notContractedFarmerlist.add(farmer);
            } while (cursor.moveToNext());
        }
        return notContractedFarmerlist;
    }

    /**
     * @return
     */

    public List<RegisteredFarmer> getShowIntentAndNotContractedFarmers() {
        List<RegisteredFarmer> intentFarmerlist = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_REGISTERED_FARMERS + " WHERE (" + KEY_CONTRACT_STATUS
                + " = 'not contracted' OR " + KEY_CONTRACT_STATUS + " = 'shows intent' ) AND " + KEY_CREDIT_STATUS
                + " = 'no'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                RegisteredFarmer farmer = new RegisteredFarmer();
                farmer.setFarmerId(cursor.getString(cursor.getColumnIndex(KEY_FARMER_ID)));
                farmer.setGenId(cursor.getString(cursor.getColumnIndex(KEY_GEN_ID)));
                farmer.setCardNo(cursor.getString(cursor.getColumnIndex(KEY_CARD_NO)));
                farmer.setFirstName(cursor.getString(cursor.getColumnIndex(KEY_FNAME)));
                farmer.setLastName(cursor.getString(cursor.getColumnIndex(KEY_LNAME)));
                farmer.setContractStatus(cursor.getString(cursor.getColumnIndex(KEY_CONTRACT_STATUS)));
                farmer.setCreditStatus(cursor.getString(cursor.getColumnIndex(KEY_CREDIT_STATUS)));
                farmer.setCompanyId(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));

                intentFarmerlist.add(farmer);
            } while (cursor.moveToNext());
        }
        return intentFarmerlist;
    }

    /**
     * Method to insert data into planting germination table
     *
     * @param germination
     */
    public void addGermination(Germination germination) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FARM_ID, germination.getFarmId());
        values.put(KEY_GERM_DATE, germination.getGerminationDate());
        values.put(KEY_REMARKS, germination.getRemarks());
        values.put(KEY_COLLECT_DATE, germination.getCollectDate());
        values.put(KEY_USER_ID, germination.getUserId());
        values.put(KEY_LATITUDE, germination.getLatitude());
        values.put(KEY_LONGITUDE, germination.getLongitude());
        values.put(KEY_COMPANY_ID, germination.getCompanyId());

        db.insert(TABLE_GERMINATION, null, values);

    }

    /**
     * Method to get all data from germination table
     *
     * @return germinations list
     */
    public List<Germination> getAllGerminations() {
        List<Germination> germination_list = new ArrayList<Germination>();

        String selectQuery = "SELECT * FROM " + TABLE_GERMINATION;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Germination germ = new Germination();
                germ.setFarmId(cursor.getString(cursor.getColumnIndex(KEY_FARM_ID)));
                germ.setGerminationDate(cursor.getString(cursor.getColumnIndex(KEY_GERM_DATE)));
                germ.setRemarks(cursor.getString(cursor.getColumnIndex(KEY_REMARKS)));
                germ.setCollectDate(cursor.getString(cursor.getColumnIndex(KEY_COLLECT_DATE)));
                germ.setUserId(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                germ.setLatitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                germ.setLongt(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                germ.setCompanyId(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));

                germination_list.add(germ);
            } while (cursor.moveToNext());
        }
        return germination_list;
    }

    /**
     * Method to insert data into planting rains table
     *
     * @param rains
     */
    public void addPlantingRains(PlantingRains rains) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FARM_ID, rains.getFarmID());
        values.put(KEY_RAIN_DATE, rains.getRainDate());
        values.put(KEY_REMARKS, rains.getRemarks());
        values.put(KEY_COLLECT_DATE, rains.getCollectDate());
        values.put(KEY_USER_ID, rains.getUserID());
        values.put(KEY_LATITUDE, rains.getLatitude());
        values.put(KEY_LONGITUDE, rains.getLongitude());
        values.put(KEY_COMPANY_ID, rains.getCompanyID());

        db.insert(TABLE_PLANTING_RAINS, null, values);

    }

    public List<PlantingRains> getAllPlantingRains() {
        List<PlantingRains> plantingRainsList = new ArrayList<PlantingRains>();

        String selectQuery = "SELECT * FROM " + TABLE_PLANTING_RAINS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                PlantingRains plantingRains = new PlantingRains();
                plantingRains.setFarmID(cursor.getString(cursor.getColumnIndex(KEY_FARM_ID)));
                plantingRains.setRainDate(cursor.getString(cursor.getColumnIndex(KEY_RAIN_DATE)));
                plantingRains.setRemarks(cursor.getString(cursor.getColumnIndex(KEY_REMARKS)));
                plantingRains.setCollectDate(cursor.getString(cursor.getColumnIndex(KEY_COLLECT_DATE)));
                plantingRains.setUserID(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                plantingRains.setLatitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                plantingRains.setLongitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                plantingRains.setCompanyID(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));

                plantingRainsList.add(plantingRains);
            } while (cursor.moveToNext());
        }
        return plantingRainsList;
    }

    /**
     * Method to insert data into farm other crops table
     *
     * @param farm_other_crops
     */
    public void addFarmOtherCrops(FarmOtherCrops farm_other_crops) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FARM_ID, farm_other_crops.getFarmID());
        values.put(KEY_CROP_ID1, farm_other_crops.getCropIdOne());
        values.put(KEY_CROP_ID2, farm_other_crops.getCropIdTwo());
        values.put(KEY_CROP_ID3, farm_other_crops.getCropIdThree());
        values.put(KEY_USER_ID, farm_other_crops.getUserId());
        values.put(KEY_COLLECT_DATE, farm_other_crops.getCollectDate());
        values.put(KEY_LATITUDE, farm_other_crops.getLatitude());
        values.put(KEY_LONGITUDE, farm_other_crops.getLongitude());
        values.put(KEY_COMPANY_ID, farm_other_crops.getCompanyId());

        db.insert(TABLE_FARM_OTHER_CROPS, null, values);

    }

    /**
     * Method to get data in other crops table
     *
     * @return list of other crops
     */
    public List<OtherCrops> allOtherCrops() {
        List<OtherCrops> other_crops_list = new ArrayList<OtherCrops>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_OTHER_CROPS + " ORDER BY " + KEY_CROP_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                OtherCrops other_crops = new OtherCrops();
                other_crops.setCropID(cursor.getString(cursor.getColumnIndex(KEY_CROP_ID)));
                other_crops.setCropName(cursor.getString(cursor.getColumnIndex(KEY_CROP_NAME)));

                // Adding other crops to list
                other_crops_list.add(other_crops);
            } while (cursor.moveToNext());
        }
        return other_crops_list;
    }

    /**
     * Method to insert all data into other crops table
     *
     * @param other_crops
     */
    public void addOtherCrops(OtherCrops other_crops) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CROP_ID, other_crops.getCropID());
        values.put(KEY_CROP_NAME, other_crops.getCropName());

        db.insert(TABLE_OTHER_CROPS, null, values);

    }

    /**
     * adds data to herbicides table
     *
     * @param herbicides
     */
    public void addHerbicides(Herbicides herbicides) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_INPUT_ID, herbicides.getInputID());
        values.put(KEY_INPUT_TYPE, herbicides.getInputType());

        db.insert(TABLE_HERBICIDES, null, values);

    }

    /**
     * adds data to foliar feed table
     *
     * @param foliarfeed
     */
    public void addFoliarFeed(FoliarFeed foliarfeed) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_INPUT_ID, foliarfeed.getInputID());
        values.put(KEY_INPUT_TYPE, foliarfeed.getInputType());

        db.insert(TABLE_FOLIAR_FEED, null, values);

    }

    public void addPesticides(Pesticides pesticide) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_INPUT_ID, pesticide.getInputID());
        values.put(KEY_INPUT_TYPE, pesticide.getInputType());

        db.insert(TABLE_PESTICIDES, null, values);

    }

    /**
     * Method to get all foliar feed
     *
     * @return foliar feed list
     */
    public List<FoliarFeed> getAllFoliarFeed() {
        List<FoliarFeed> foliar_feed_list = new ArrayList<FoliarFeed>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FOLIAR_FEED;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                FoliarFeed feed = new FoliarFeed();
                feed.setInputID(cursor.getString(cursor.getColumnIndex(KEY_INPUT_ID)));
                feed.setInputType(cursor.getString(cursor.getColumnIndex(KEY_INPUT_TYPE)));

                foliar_feed_list.add(feed);
            } while (cursor.moveToNext());
        }
        return foliar_feed_list;
    }

    public List<Pesticides> getAllPesticides() {
        List<Pesticides> pesticideList = new ArrayList<Pesticides>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PESTICIDES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Pesticides pesticide = new Pesticides();
                pesticide.setInputID(cursor.getString(cursor.getColumnIndex(KEY_INPUT_ID)));
                pesticide.setInputType(cursor.getString(cursor.getColumnIndex(KEY_INPUT_TYPE)));

                pesticideList.add(pesticide);
            } while (cursor.moveToNext());
        }
        return pesticideList;
    }

    //
    // /**
    // * adds data to farm assessment form types
    // *
    // * @param form_types
    // */
    // public void addFarmAssFormTypes(FarmAssFormTypes form_types) {
    // SQLiteDatabase db = this.getReadableDatabase();
    // ContentValues values = new ContentValues();
    // values.put(KEY_FORM_TYPE_ID, form_types.getFormTypeId());
    // values.put(KEY_FORM_ID, form_types.getFormID());
    // values.put(KEY_FORM_TYPE, form_types.getFormType());
    //
    // db.insert(TABLE_FARM_ASS_FORM_TYPES, null, values);
    //
    // }

    /**
     * method to add a new activity log major in to table.
     *
     * @param major
     */
    public void addFarmAssMajor(FarmAssFormsMajor major) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FARM_ID, major.getFarmId());
        values.put(KEY_FORM_TYPE_ID, major.getFormTypeId());
        values.put(KEY_ACTIVITY_METHOD, major.getActivityMethod());
        values.put(KEY_ACTIVITY_DATE, major.getActivityDate());
        values.put(KEY_FAMILY_HOURS, major.getFamilyHours());
        values.put(KEY_HIRED_HOURS, major.getHiredHours());
        values.put(KEY_COLLECT_DATE, major.getCollectDate());
        values.put(KEY_MONEY_OUT, major.getMoneyOut());
        values.put(KEY_REMARKS, major.getRemarks());
        values.put(KEY_LATITUDE, major.getLatitude());
        values.put(KEY_LONGITUDE, major.getLongitude());
        values.put(KEY_USER_ID, major.getUserId());
        values.put(KEY_COMPANY_ID, major.getCompanyId());
        db.insert(TABLE_FARM_ASS_MAJOR, null, values);

    }

    public void addUserVillage(UserVillage user_village) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, user_village.getUserId());
        values.put(KEY_VILLAGE_ID, user_village.getVillageId());
        db.insert(TABLE_USER_VILLAGE, null, values);

    }

    /**
     * Method to add new user to users table
     *
     * @param user
     */
    public void addUser(Users user) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, user.getUserID());
        values.put(KEY_USER_NAME, user.getUserName());
        values.put(KEY_USER_PWD, user.getPassword());
        values.put(KEY_COMPANY_ID, user.getCompanyID());
        db.insert(TABLE_USERS, null, values);

    }

    public void addCompany(Companies company) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_COMPANY_ID, company.getCompanyID());
        values.put(KEY_COMPANY_NAME, company.getCompanyName());
        db.insert(TABLE_COMPANIES, null, values);

    }

    public void addAssignedInput(AssignedInputs a) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ORDER_ID, a.getOrderId());
        values.put(KEY_ASS_INPUT_ID, a.getInputAssInputId());
        values.put(KEY_INPUT_ID, a.getInputId());
        values.put(KEY_INPUT_TYPE, a.getInputType());
        values.put(KEY_INPUT_QUANTITY, a.getInputQuantity());
        values.put(KEY_INPUT_TOTAL, a.getInputTotal());
        values.put(KEY_FARMER_ID, a.getFarmerId());
        values.put(KEY_CARD_NO, a.getCardNo());
        values.put(KEY_ORDER_NUM, a.getOrderNum());

        db.insert(TABLE_ASSIGNED_INPUTS, null, values);

    }

    public void addAssignedTrainings(AssignedTrainings t) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EXT_TRAIN_ID, t.getExtTrainID());
        values.put(KEY_TRAIN_CAT_ID, t.getTrainCatID());
        values.put(KEY_TRAIN_START_TIME, t.getTStartTime());
        values.put(KEY_TRAIN_STOP_TIME, t.getTStopTime());
        values.put(KEY_TRAIN_LATITUDE, t.getLatitude());
        values.put(KEY_TRAIN_LONGITUDE, t.getLongitude());
        values.put(KEY_USER_ID, t.getUserID());
        values.put(KEY_COMPANY_ID, t.getCompanyID());

        db.insert(TABLE_ASSIGNED_TRAININGS, null, values);

    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
    // Adding new countries
    public void addCountries(Countries country) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_COUNTRY_ID, country.getCountryID());
        values.put(KEY_COUNTRY_NAME, country.getCountryName());

        db.insert(TABLE_COUNTRIES, null, values);

    }

    // Adding new regions
    public void addRegions(Regions region) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_REGION_ID, region.getRegionID());
        values.put(KEY_COUNTRY_ID, region.getCountryID());
        values.put(KEY_REGION_NAME, region.getRegionName());

        db.insert(TABLE_REGIONS, null, values);

    }

    // Adding new districts
    public void addDistricts(Districts district) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DISTRICT_ID, district.getDistrictID());
        values.put(KEY_REGION_ID, district.getRegionID());
        values.put(KEY_DISTRICT_NAME, district.getDistrictName());

        db.insert(TABLE_DISTRICTS, null, values);

    }

    /**
     * Add downloaded wards to database
     *
     * @param ward
     */
    public void addWards(Ward ward) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_WARD_ID, ward.getWardID());
        //values.put(KEY_DISTRICT_ID, ward.getDistrictID());
        values.put(KEY_WARD_NAME, ward.getWardName());
        values.put(KEY_BOLL_WEIGHT, ward.getBollWeight());

        db.insert(TABLE_WARDS, null, values);

    }

    /**
     * Add downloaded villages to database
     *
     * @param village
     */
    public void addVillages(Village village) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_VILLAGE_ID, village.getVillageID());
        //values.put(KEY_WARD_ID, village.getTimes());
        values.put(KEY_VILLAGE_NAME, village.getVillageName());

        db.insert(TABLE_VILLAGES, null, values);

    }

    /**
     * Add downloaded sub villages
     *
     * @param subvillage
     */
    public void addSubvillages(SubVillage subvillage) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SUBVILLAGE_ID, subvillage.getSubVillageID());
        values.put(KEY_VILLAGE_ID, subvillage.getVillageID());
        values.put(KEY_SUBVILLAGE_NAME, subvillage.getSubVillageName());

        db.insert(TABLE_SUBVILLAGES, null, values);

    }

    // Adding new farmer
    public void addFarmer(Farmers farmer) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_FNAME, farmer.getFName());
        values.put(KEY_FNAME, farmer.getmName());
        values.put(KEY_LNAME, farmer.getLName());
        values.put(KEY_GENDER, farmer.getGender());
        values.put(KEY_ID_NO, farmer.getIDNumber());
        values.put(KEY_PH_NO, farmer.getPhoneNumber());
        values.put(KEY_EMAIL, farmer.getEmail());
        values.put(KEY_POST, farmer.getPostAddress());
        values.put(KEY_VILLAGE, farmer.getVillageID());
        values.put(KEY_SUB_VILLAGE, farmer.getSubVillageID());
        values.put(KEY_PIC, farmer.getFarmerPicPath());
        values.put(KEY_LEFT_THUMB, farmer.getLeftThumb());
        values.put(KEY_RIGHT_THUMB, farmer.getRightThumb());
        values.put(KEY_LATITUDE, farmer.getLatitude());
        values.put(KEY_LONGITUDE, farmer.getLongitude());
        values.put(KEY_FARMER_INTENT, farmer.getShowIntent());
        values.put(KEY_ESTIMATED_FARM_AREA, farmer.getEstimatedFarmArea());
        values.put(KEY_ESTIMATED_FARM_AREA2, farmer.getEstimatedFarmAreaTwo());
        values.put(KEY_ESTIMATED_FARM_AREA3, farmer.getEstimatedFarmAreaThree());
        values.put(KEY_ESTIMATED_FARM_AREA4, farmer.getEstimatedFarmAreaFour());
        values.put(KEY_FARM_VILLAGE_ONE, farmer.getFarmVidOne());
        values.put(KEY_FARM_VILLAGE_TWO, farmer.getFarmVidTwo());
        values.put(KEY_FARM_VILLAGE_THREE, farmer.getFarmVidThree());
        values.put(KEY_FARM_VILLAGE_FOUR, farmer.getFarmVidFour());
        values.put(KEY_OTHER_CROP_ONE, farmer.getOtherCropsOne());
        values.put(KEY_OTHER_CROP_TWO, farmer.getOtherCropsTwo());
        values.put(KEY_OTHER_CROP_THREE, farmer.getOtherCropsThree());
        values.put(KEY_COMPANY_ID, farmer.getCompanyID());
        values.put(KEY_USER_ID, farmer.getUserID());
        values.put(KEY_CONTRACT_ID, farmer.getContractNo());

        db.insert(TABLE_FARMERS, null, values);
    }

    // Adding new farmer
    public void updateFarmer(RegisteredFarmer farmer) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_FNAME, farmer.getFirstName());
        values.put(KEY_LNAME, farmer.getLastName());
        values.put(KEY_GENDER, farmer.getGender());
        values.put(KEY_FARMER_ID, farmer.getFarmerId());
        values.put(KEY_PH_NO, farmer.getPhone());
        values.put(KEY_CONTRACT_ID, farmer.getContract_number());

        db.insert(TABLE_UPDATED_FARMERS, null, values);
    }

    public List<RegisteredFarmer> getAllUpdatedFarmers() {

        List<RegisteredFarmer> farmerslist = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_UPDATED_FARMERS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                RegisteredFarmer farmer = new RegisteredFarmer();
                farmer.setFarmerId(cursor.getString(cursor.getColumnIndex(KEY_FARMER_ID)));
                farmer.setFirstName(cursor.getString(cursor.getColumnIndex(KEY_FNAME)));
                farmer.setLastName(cursor.getString(cursor.getColumnIndex(KEY_LNAME)));
                farmer.setGender(cursor.getString(cursor.getColumnIndex(KEY_GENDER)));
                farmer.setPhone(cursor.getString(cursor.getColumnIndex(KEY_PH_NO)));
                farmer.setContract_number(cursor.getString(cursor.getColumnIndex(KEY_CONTRACT_ID)));

                farmerslist.add(farmer);
            } while (cursor.moveToNext());
        }
        return farmerslist;
    }

    // Adding new input
    public void addInput(InputsFromServer inputs) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_INPUT_ID, inputs.getInputId());
        values.put(KEY_INPUT_CATEGORY, inputs.getInputCat());
        values.put(KEY_INPUT_TYPE, inputs.getInputType());

        db.insert(TABLE_FARM_INPUTS, null, values);
    }

    public List<FarmOtherCrops> getAllFarmOtherCrops() {
        List<FarmOtherCrops> farm_other_crops_list = new ArrayList<FarmOtherCrops>();
        String selectQuery = "SELECT  * FROM " + TABLE_FARM_OTHER_CROPS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                FarmOtherCrops other_crops = new FarmOtherCrops();
                other_crops.setFarmID(cursor.getString(cursor.getColumnIndex(KEY_FARM_ID)));
                other_crops.setCropIdOne(cursor.getString(cursor.getColumnIndex(KEY_CROP_ID1)));
                other_crops.setCropIdTwo(cursor.getString(cursor.getColumnIndex(KEY_CROP_ID2)));
                other_crops.setCropIdThree(cursor.getString(cursor.getColumnIndex(KEY_CROP_ID3)));
                other_crops.setUserId(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                other_crops.setCollectDate(cursor.getString(cursor.getColumnIndex(KEY_COLLECT_DATE)));
                other_crops.setLatitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                other_crops.setLongitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                other_crops.setCompanyId(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));

                farm_other_crops_list.add(other_crops);
            } while (cursor.moveToNext());
        }
        return farm_other_crops_list;
    }

    // public List<FarmAssFormTypes> getAllFarmAssFormTypes() {
    // List<FarmAssFormTypes> form_types_list = new
    // ArrayList<FarmAssFormTypes>();
    // // Select All Query
    // String selectQuery = "SELECT * FROM " + TABLE_FARM_ASS_FORM_TYPES;
    //
    // SQLiteDatabase db = this.getWritableDatabase();
    // Cursor cursor = db.rawQuery(selectQuery, null);
    //
    // // looping through all rows and adding to list
    // if (cursor.moveToFirst()) {
    // do {
    // FarmAssFormTypes form_types = new FarmAssFormTypes();
    // form_types.setFormTypeId(cursor.getString(cursor.getColumnIndex(KEY_FORM_TYPE_ID)));
    // form_types.setFormID(cursor.getString(cursor.getColumnIndex(KEY_FORM_ID)));
    // form_types.setFormType(cursor.getString(cursor.getColumnIndex(KEY_FORM_TYPE)));
    //
    // // Adding farmer to list
    // form_types_list.add(form_types);
    // } while (cursor.moveToNext());
    // }
    // return form_types_list;
    // }
    public List<Users> getUserDetails(String userName) {
        List<Users> usersDetails = new ArrayList<Users>();

        String selectQuery = "SELECT  " + KEY_USER_ID + "," + KEY_COMPANY_ID + " FROM " + TABLE_USERS + " WHERE " + KEY_USER_NAME + " = '" + userName
                + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Users user = new Users();
                user.setUserID(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                // user.setUserName(cursor.getString(cursor.getColumnIndex(KEY_USER_NAME)));
                // user.setUserPwd(cursor.getString(cursor.getColumnIndex(KEY_USER_PWD)));
                user.setCompanyID(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));

                usersDetails.add(user);
            } while (cursor.moveToNext());
        }
        return usersDetails;
    }

    /**
     * Get all users
     *
     * @return
     */
    public List<Users> getAllUsers() {
        List<Users> usersList = new ArrayList<Users>();
        String selectQuery = "SELECT  * FROM " + TABLE_USERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Users user = new Users();
                user.setUserID(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                user.setUserName(cursor.getString(cursor.getColumnIndex(KEY_USER_NAME)));
                user.setUserPwd(cursor.getString(cursor.getColumnIndex(KEY_USER_PWD)));
                user.setCompanyID(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));

                usersList.add(user);
            } while (cursor.moveToNext());
        }
        return usersList;
    }

    /**
     * Get users' villages
     *
     * @param userID
     * @return
     */
    public List<UserVillage> getVillageIdByUserId(String userID) {
        List<UserVillage> usersVillageList = new ArrayList<>();
        String selectQuery = "SELECT " + KEY_VILLAGE_ID + " FROM " + TABLE_USER_VILLAGE + " WHERE " + KEY_USER_ID
                + " = " + userID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                UserVillage user = new UserVillage();
                // user.setUserId(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                user.setVillageId(cursor.getString(cursor.getColumnIndex(KEY_VILLAGE_ID)));

                usersVillageList.add(user);
            } while (cursor.moveToNext());
        }
        return usersVillageList;
    }

    /**
     * Get all userVillages
     *
     * @return
     */
    public List<UserVillage> getAllUserVillages() {
        List<UserVillage> usersVillageList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_USER_VILLAGE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                UserVillage user = new UserVillage();
                user.setUserId(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                user.setVillageId(cursor.getString(cursor.getColumnIndex(KEY_VILLAGE_ID)));

                usersVillageList.add(user);
            } while (cursor.moveToNext());
        }
        return usersVillageList;
    }


    /**
     * Get all forms of type FarmAssFormsMajor
     *
     * @return
     */
    public List<FarmAssFormsMajor> getAllFormsMajor() {
        List<FarmAssFormsMajor> activitiesList = new ArrayList<FarmAssFormsMajor>();

        String selectQuery = "SELECT  * FROM " + TABLE_FARM_ASS_MAJOR;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                FarmAssFormsMajor major = new FarmAssFormsMajor();
                major.setFarmId(cursor.getString(cursor.getColumnIndex(KEY_FARM_ID)));
                major.setFormTypeId(cursor.getString(cursor.getColumnIndex(KEY_FORM_TYPE_ID)));
                major.setActivityMethod(cursor.getString(cursor.getColumnIndex(KEY_ACTIVITY_METHOD)));
                major.setActivityDate(cursor.getString(cursor.getColumnIndex(KEY_ACTIVITY_DATE)));
                major.setFamilyHours(cursor.getString(cursor.getColumnIndex(KEY_FAMILY_HOURS)));
                major.setHiredHours(cursor.getString(cursor.getColumnIndex(KEY_HIRED_HOURS)));
                major.setCollectDate(cursor.getString(cursor.getColumnIndex(KEY_COLLECT_DATE)));
                major.setMoneyOut(cursor.getString(cursor.getColumnIndex(KEY_MONEY_OUT)));
                major.setRemarks(cursor.getString(cursor.getColumnIndex(KEY_REMARKS)));
                major.setUserId(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                major.setLatitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                major.setLongitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                major.setCompanyId(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));

                activitiesList.add(major);
            } while (cursor.moveToNext());
        }
        return activitiesList;
    }

    /**
     * Get all forms of type FarmAssFormsMedium
     *
     * @return
     */
    public List<FarmAssFormsMedium> getAllFormsMedium() {
        List<FarmAssFormsMedium> activitiesList = new ArrayList<FarmAssFormsMedium>();
        String selectQuery = "SELECT  * FROM " + TABLE_FARM_ASS_MEDIUM;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                FarmAssFormsMedium medium = new FarmAssFormsMedium();
                medium.setFarmId(cursor.getString(cursor.getColumnIndex(KEY_FARM_ID)));
                medium.setFormTypeId(cursor.getString(cursor.getColumnIndex(KEY_FORM_TYPE_ID)));
                medium.setActivityMethodId(cursor.getString(cursor.getColumnIndex(KEY_ACTIVITY_METHOD)));
                medium.setActivityDate(cursor.getString(cursor.getColumnIndex(KEY_ACTIVITY_DATE)));
                medium.setFamilyHours(cursor.getString(cursor.getColumnIndex(KEY_FAMILY_HOURS)));
                medium.setHiredHours(cursor.getString(cursor.getColumnIndex(KEY_HIRED_HOURS)));
                medium.setCollectDate(cursor.getString(cursor.getColumnIndex(KEY_COLLECT_DATE)));
                medium.setMoneyOut(cursor.getString(cursor.getColumnIndex(KEY_MONEY_OUT)));
                medium.setInputId(cursor.getString(cursor.getColumnIndex(KEY_INPUT_ID)));
                medium.setInputQuantity(cursor.getString(cursor.getColumnIndex(KEY_INPUT_QUANTITY)));
                medium.setSprayType(cursor.getString(cursor.getColumnIndex(KEY_SPRAY_TYPE)));
                medium.setLatitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                medium.setLongitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                medium.setUserId(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                medium.setCompanyId(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));

                activitiesList.add(medium);
            } while (cursor.moveToNext());
        }
        return activitiesList;
    }

    /**
     * Get all companies
     *
     * @return
     */
    public List<Companies> getAllCompanies() {
        List<Companies> companyList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_COMPANIES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Companies company = new Companies();
                company.setCompanyID(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));
                company.setCompanyName(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_NAME)));

                companyList.add(company);
            } while (cursor.moveToNext());
        }
        return companyList;
    }

    /**
     * Validates username and password
     *
     * @param user_name
     * @param pwd
     * @return
     */
    public boolean validateUser(String user_name, String pwd) {
        final String HEXES = "0123456789ABCDEF";

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        String pwd2 = pwd + "78(%#xd9_DO";

        try {
            md.update(pwd2.getBytes("UTF-8")); // Change this to "UTF-16" if needed
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] digest = md.digest();
        final StringBuilder final_pwd = new StringBuilder(2 * digest.length);
        for (final byte b : digest) {
            final_pwd.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
        }

        String selectQuery = "SELECT  * FROM " + TABLE_USERS + " WHERE " + KEY_USER_NAME + " = '" + user_name
                + "' AND trim(" + KEY_USER_PWD + ") = '" + final_pwd.toString().toLowerCase().trim() + "' AND "
                + KEY_USER_PWD
                + " != '42ad9cc8788d478fb0090e6d34d6ed722beb13fb96e100beae619dd9ce94bc43' AND " + KEY_USER_PWD
                + " != '7e8f9e308d723133252bcd4d2469b7fd0d64c80bd467116f702132b9d2645ac9'";
        Log.i("Val Pwd", final_pwd.toString().toLowerCase());

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        int count = 0;
        boolean status;
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        if (count == 1) {
            status = true;
        } else if (count == 0) {
            status = false;
        } else {
            Log.e("User count:", String.valueOf(count));
            status = false;
        }
        return status;
    }

    /**
     * Get all herbicides
     *
     * @return
     */
    public List<Herbicides> getAllHerbicides() {
        List<Herbicides> herbicidesList = new ArrayList<Herbicides>();

        String selectQuery = "SELECT  * FROM " + TABLE_HERBICIDES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Herbicides herbicides = new Herbicides();
                herbicides.setInputID(cursor.getString(cursor.getColumnIndex(KEY_INPUT_ID)));
                herbicides.setInputType(cursor.getString(cursor.getColumnIndex(KEY_INPUT_TYPE)));


                herbicidesList.add(herbicides);
            } while (cursor.moveToNext());
        }
        return herbicidesList;
    }

    /**
     * Get all farmers to be uploaded
     *
     * @return
     */
    public List<Farmers> getAllFarmers() {
        List<Farmers> contactList = new ArrayList<Farmers>();

        String selectQuery = "SELECT  * FROM " + TABLE_FARMERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Farmers farmer = new Farmers();
                farmer.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                farmer.setFName(cursor.getString(cursor.getColumnIndex(KEY_FNAME)));
                farmer.setLName(cursor.getString(cursor.getColumnIndex(KEY_LNAME)));
                farmer.setGender(cursor.getString(cursor.getColumnIndex(KEY_GENDER)));
                farmer.setIDNO(cursor.getString(cursor.getColumnIndex(KEY_ID_NO)));
                farmer.setPhoneNumber(cursor.getString(cursor.getColumnIndex(KEY_PH_NO)));
                farmer.setEmail(cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
                farmer.setPostAddress(cursor.getString(cursor.getColumnIndex(KEY_POST)));
                farmer.setVillageID(cursor.getString(cursor.getColumnIndex(KEY_VILLAGE)));
                farmer.setSubVillage(cursor.getString(cursor.getColumnIndex(KEY_SUB_VILLAGE)));
                farmer.setFarmerPhotoPath(cursor.getString(cursor.getColumnIndex(KEY_PIC)));
                farmer.setLeftThumb(cursor.getString(cursor.getColumnIndex(KEY_LEFT_THUMB)));
                farmer.setRightThumb(cursor.getString(cursor.getColumnIndex(KEY_RIGHT_THUMB)));
                farmer.setLatitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                farmer.setLongitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                farmer.setShowIntent(cursor.getString(cursor.getColumnIndex(KEY_FARMER_INTENT)));
                farmer.setEstimatedFarmArea(cursor.getString(cursor.getColumnIndex(KEY_ESTIMATED_FARM_AREA)));
                farmer.setEstimatedFarmAreaTwo(cursor.getString(cursor.getColumnIndex(KEY_ESTIMATED_FARM_AREA2)));
                farmer.setEstimatedFarmAreaThree(cursor.getString(cursor.getColumnIndex(KEY_ESTIMATED_FARM_AREA3)));
                farmer.setEstimatedFarmAreaFour(cursor.getString(cursor.getColumnIndex(KEY_ESTIMATED_FARM_AREA4)));
                farmer.setFarmVidOne(cursor.getString(cursor.getColumnIndex(KEY_FARM_VILLAGE_ONE)));
                farmer.setFarmVidTwo(cursor.getString(cursor.getColumnIndex(KEY_FARM_VILLAGE_TWO)));
                farmer.setFarmVidThree(cursor.getString(cursor.getColumnIndex(KEY_FARM_VILLAGE_THREE)));
                farmer.setFarmVidFour(cursor.getString(cursor.getColumnIndex(KEY_FARM_VILLAGE_FOUR)));
                farmer.setOtherCropsOne(cursor.getString(cursor.getColumnIndex(KEY_OTHER_CROP_ONE)));
                farmer.setOtherCropsTwo(cursor.getString(cursor.getColumnIndex(KEY_OTHER_CROP_TWO)));
                farmer.setOtherCropsThree(cursor.getString(cursor.getColumnIndex(KEY_OTHER_CROP_THREE)));
                farmer.setCompanyID(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));
                farmer.setUserID(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                farmer.setContractNo(cursor.getString(cursor.getColumnIndex(KEY_CONTRACT_ID)));

                contactList.add(farmer);
            } while (cursor.moveToNext());
        }
        return contactList;
    }

    /**
     * Get all inputs assigned to farmers
     *
     * @return Cursor
     */
    public Cursor getAllAssignedInputsCursor() {
        String selectQuery = "SELECT  *," + KEY_FNAME + "," + KEY_LNAME + "," + TABLE_ASSIGNED_INPUTS + "." + KEY_ORDER_ID
                + " as _id FROM " + TABLE_ASSIGNED_INPUTS + " join " + TABLE_REGISTERED_FARMERS + " on "
                + TABLE_ASSIGNED_INPUTS + "." + KEY_FARMER_ID + " = " + TABLE_REGISTERED_FARMERS + "." + KEY_FARMER_ID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        return cursor;
    }

    public List<AssignedInputs> getAllAssignedInputs() {
        List<AssignedInputs> assignedInputsList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_ASSIGNED_INPUTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                AssignedInputs assignedInputs = new AssignedInputs();
                assignedInputs.setOrderId(cursor.getString(cursor.getColumnIndex(KEY_ORDER_ID)));
                assignedInputs.setAssInputId(cursor.getString(cursor.getColumnIndex(KEY_ASS_INPUT_ID)));
                assignedInputs.setFarmerId(cursor.getString(cursor.getColumnIndex(KEY_FARMER_ID)));
                assignedInputs.setCardNo(cursor.getString(cursor.getColumnIndex(KEY_CARD_NO)));
                assignedInputs.setInputId(cursor.getString(cursor.getColumnIndex(KEY_INPUT_ID)));
                assignedInputs.setInputQuantity(cursor.getString(cursor.getColumnIndex(KEY_INPUT_QUANTITY)));
                assignedInputs.setInputTotal(cursor.getString(cursor.getColumnIndex(KEY_INPUT_TOTAL)));
                assignedInputs.setInputType(cursor.getString(cursor.getColumnIndex(KEY_INPUT_TYPE)));
                assignedInputs.setOrderNum(cursor.getString(cursor.getColumnIndex(KEY_ORDER_NUM)));

                assignedInputsList.add(assignedInputs);
            } while (cursor.moveToNext());
        }

        return assignedInputsList;
    }

    public List<String> getAllRegisteredFarmerSearch() {
        List<String> farmerList = new ArrayList<>();
        String selectQuery = "SELECT  " + KEY_GEN_ID + "," + KEY_ID + " as _id FROM " + TABLE_REGISTERED_FARMERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                RegisteredFarmer farmer = new RegisteredFarmer();
                farmer.setGenId(cursor.getString(cursor.getColumnIndex(KEY_GEN_ID)));

                farmerList.add(farmer.getGenId());
            } while (cursor.moveToNext());
        }

        return farmerList;
    }

    /**
     * Get a farmers details
     *
     * @param farmerID
     * @return
     */
    public RegisteredFarmer getFarmerDetails(String farmerID) {
        RegisteredFarmer farmer = new RegisteredFarmer();
        String selectQuery = "SELECT " + KEY_FNAME + "," + KEY_LNAME + " FROM " + TABLE_REGISTERED_FARMERS + " WHERE " + KEY_FARMER_ID + " = " + farmerID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                farmer.setFirstName(cursor.getString(cursor.getColumnIndex(KEY_FNAME)));
                farmer.setLastName(cursor.getString(cursor.getColumnIndex(KEY_LNAME)));
            } while (cursor.moveToNext());
        }

        return farmer;
    }

    /**
     * Get completed trainings
     *
     * @return
     */
    public List<AssignedTrainings> getAllAssignedTrainings() {
        List<AssignedTrainings> assignedTrainingList = new ArrayList<AssignedTrainings>();
        String selectQuery = "SELECT * FROM " + TABLE_ASSIGNED_TRAININGS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                AssignedTrainings assignedTrainings = new AssignedTrainings();

                assignedTrainings.setExtTrainID(cursor.getString(cursor.getColumnIndex(KEY_EXT_TRAIN_ID)));
                assignedTrainings.setTrainCatID(cursor.getString(cursor.getColumnIndex(KEY_TRAIN_CAT_ID)));
                assignedTrainings.setTStartTime(cursor.getString(cursor.getColumnIndex(KEY_TRAIN_START_TIME)));
                assignedTrainings.setTStopTime(cursor.getString(cursor.getColumnIndex(KEY_TRAIN_STOP_TIME)));
                assignedTrainings.setLatitude(cursor.getString(cursor.getColumnIndex(KEY_TRAIN_LATITUDE)));
                assignedTrainings.setLongitude(cursor.getString(cursor.getColumnIndex(KEY_TRAIN_LONGITUDE)));
                assignedTrainings.setUserID(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                assignedTrainings.setCompanyID(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));

                assignedTrainingList.add(assignedTrainings);
            } while (cursor.moveToNext());
        }
        return assignedTrainingList;
    }

    /**
     * Get sub villages by villageID
     *
     * @param villageID
     * @return
     */
    public List<SubVillage> getDynamicSubVillages(String villageID) {
        List<SubVillage> subVillageList = new ArrayList<SubVillage>();

        String selectQuery = "SELECT  * FROM " + TABLE_SUBVILLAGES + " WHERE " + KEY_VILLAGE_ID + " = '" + villageID
                + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                SubVillage subVillage = new SubVillage();
                subVillage.setSubVillageID(cursor.getString(cursor.getColumnIndex(KEY_SUBVILLAGE_ID)));
                subVillage.setSubVillageName(cursor.getString(cursor.getColumnIndex(KEY_SUBVILLAGE_NAME)));

                subVillageList.add(subVillage);
            } while (cursor.moveToNext());
        }

        return subVillageList;
    }

    /**
     * Get all sub villages
     *
     * @return
     */
    public List<SubVillage> getAllSubVillages() {
        List<SubVillage> subVillageList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_SUBVILLAGES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                SubVillage subVillage = new SubVillage();
                subVillage.setVillageID(cursor.getString(cursor.getColumnIndex(KEY_VILLAGE_ID)));
                subVillage.setSubVillageID(cursor.getString(cursor.getColumnIndex(KEY_SUBVILLAGE_ID)));
                subVillage.setSubVillageName(cursor.getString(cursor.getColumnIndex(KEY_SUBVILLAGE_NAME)));

                subVillageList.add(subVillage);
            } while (cursor.moveToNext());
        }

        return subVillageList;
    }

    public int getUserCount() {
        int count = 0;
        String countQuery = "SELECT  * FROM " + TABLE_USERS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    /**
     * Get count for different tables
     *
     * @param tableName
     * @return
     */
    public int getTableCount(String tableName) {
        int count = 0;
        String countQuery = "SELECT  * FROM " + tableName;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        } else {
            Log.e(TAG, "getTableCount: cursor is null");
        }
//        Log.e(TAG, "getTableCount: "+count );
        return count;
    }

    /**
     * Get villages
     *
     * @return
     */
    public List<Village> getVillages() {
        List<Village> villageList = new ArrayList<Village>();

        String selectQuery = "SELECT  * FROM " + TABLE_VILLAGES;
        Log.e("Q Villages: ", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Village village = new Village();
                village.setVillageID(cursor.getString(cursor.getColumnIndex(KEY_VILLAGE_ID)));
                village.setVillageName(cursor.getString(cursor.getColumnIndex(KEY_VILLAGE_NAME)));

                villageList.add(village);
            } while (cursor.moveToNext());
        }

        return villageList;
    }

    public Cursor fetchGroup() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  " + TABLE_TRAIN_CATEGORIES + ".tcatid," + TABLE_TRAININGS
                + ".ext_train_id,train_cat," + TABLE_TRAIN_CATEGORIES + "." + KEY_ID + " as _id FROM "
                + TABLE_TRAIN_CATEGORIES + " JOIN " + TABLE_TRAININGS + " ON " + TABLE_TRAIN_CATEGORIES + "."
                + KEY_TRAIN_CAT_ID + " = " + TABLE_TRAININGS + "." + KEY_TRAIN_ID + " WHERE DATE (" + TABLE_TRAININGS
                + "." + KEY_TRAIN_DATE + ") = DATE('NOW')";
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.e("", selectQuery);
        return cursor;

    }

    public Cursor fetchTrainTypeDetails(String trainCatID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  " + TABLE_TRAININGS + "." + KEY_EXT_TRAIN_ID + ", " + TABLE_TRAIN_TYPES + "."
                + KEY_TRAIN_ID + "," + "" + TABLE_TRAIN_TYPES + "." + KEY_TRAIN_TYPE + "," + "" + TABLE_TRAIN_TYPES
                + "." + KEY_ID + " as _id " + "FROM " + TABLE_TRAIN_TYPES + "" + " INNER JOIN " + TABLE_TRAININGS + " "
                + "ON " + TABLE_TRAIN_TYPES + "." + KEY_TRAIN_ID + " = " + TABLE_TRAININGS + "." + KEY_TRAIN_ID + ""
                + " INNER JOIN " + TABLE_TRAIN_CATEGORIES + " " + "ON " + TABLE_TRAIN_TYPES + "." + KEY_TRAIN_CAT_ID
                + " = " + TABLE_TRAIN_CATEGORIES + ".'" + KEY_TRAIN_CAT_ID + "'" + " WHERE " + TABLE_TRAIN_TYPES + "."
                + KEY_TRAIN_CAT_ID + " = '" + trainCatID + "' ";
        Cursor cursor = db.rawQuery(selectQuery, null);

        return cursor;
    }


    /**
     * Add AssignedTrainings types
     *
     * @param training
     */
    public void addTrainingType(TrainingType training) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TRAIN_CAT_ID, training.getTcatID());
        values.put(KEY_TRAIN_ID, training.getTID());
        values.put(KEY_TRAIN_TYPE, training.getTType());

        db.insert(TABLE_TRAIN_TYPES, null, values);

    }

    public void addFarmerTimes(FarmerTime farmerTime) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FARMER_ID, farmerTime.getFarmerID());
        values.put(KEY_TRAIN_ID, farmerTime.getTrainCatID());
        values.put(KEY_EXT_TRAIN_ID, farmerTime.getExtTrainID());
        values.put(KEY_LATITUDE, farmerTime.getLatitude());
        values.put(KEY_LONGITUDE, farmerTime.getLongitude());
        values.put(KEY_FARMER_TIME_IN, farmerTime.getFarmerTimeIn());
        values.put(KEY_FARMER_TIME_OUT, farmerTime.getFarmerTimeOut());
        values.put(KEY_USER_ID, farmerTime.getUserID());
        values.put(KEY_COMPANY_ID, farmerTime.getCompanyID());

        db.insert(TABLE_FARMER_TIMES, null, values);

    }

    public List<FarmerTime> getAllFarmerTimes() {
        List<FarmerTime> allFarmerTimes = new ArrayList<FarmerTime>();

        String selectQuery = "SELECT  * FROM " + TABLE_FARMER_TIMES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                FarmerTime times = new FarmerTime();
                times.setFarmerID(cursor.getString(cursor.getColumnIndex(KEY_FARMER_ID)));
                times.setTrainCatID(cursor.getString(cursor.getColumnIndex(KEY_TRAIN_ID)));
                times.setLatitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                times.setLongitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                times.setExTrainId(cursor.getString(cursor.getColumnIndex(KEY_EXT_TRAIN_ID)));
                times.setFarmerTimeIn(cursor.getString(cursor.getColumnIndex(KEY_FARMER_TIME_IN)));
                times.setFarmerTimeOut(cursor.getString(cursor.getColumnIndex(KEY_FARMER_TIME_OUT)));
                times.setUserId(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                times.setCompanyID(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));

                allFarmerTimes.add(times);
            } while (cursor.moveToNext());
        }

        return allFarmerTimes;
    }

    public int getTrainingCountToday() {
        int count = 0;

        String countQuery = "SELECT  * FROM " + TABLE_EXT_OFFICER_TRAINING +
                " WHERE DATE (" + TABLE_EXT_OFFICER_TRAINING + "."
                + KEY_TRAIN_DATE + ") = DATE('NOW')";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }

        return count;
    }

    /**
     * Add farms to database
     *
     * @param farm
     */
    public void addFarm(Farm farm) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FARM_ID, farm.getFarmID());
        values.put(KEY_FARMER_ID, farm.getFarmerID());
        values.put(KEY_FARM_NAME, farm.getFarmName());
        values.put(KEY_ACTUAL_AREA, farm.getActualFarmArea());
        values.put(KEY_ESTIMATED_AREA, farm.getEstimatedFarmArea());
        values.put(KEY_VILLAGE_ID, farm.getVillageID());
        values.put(KEY_PERIMETER, farm.getFarmPerimeter());
        values.put(KEY_LATITUDE, farm.getLatitude());
        values.put(KEY_LONGITUDE, farm.getLongitude());
        values.put(KEY_FARM_ASS, farm.getFarmAss());
        values.put(KEY_COMPANY_ID, farm.getCompanyID());

        db.insert(TABLE_FARMS, null, values);

    }

    /**
     * Add finger one form
     *
     * @param finger
     */
    public void addFingerOne(FingerOne finger) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_COMPANY_ID, finger.getCompanyId());
        values.put(KEY_USER_ID, finger.getUserId());
        values.put(KEY_FARM_ID, finger.getFarmId());
        values.put(KEY_SOIL_TYPE, finger.getSoilType());
        values.put(KEY_WATER_LOG_RISK, finger.getWaterLogRisk());
        values.put(KEY_EROSION_PREVENTION, finger.getErosionPrevention());
        values.put(KEY_CROP_ROTATION, finger.getCropRotation());
        values.put(KEY_RATOON, finger.getRatoon());
        values.put(KEY_CROP_RESIDUES, finger.getCropResidues());
        values.put(KEY_MANURE, finger.getManure());
        values.put(KEY_LAND_PREPARATION, finger.getLandPreparation());
        values.put(KEY_SEED_BED_PREPARATION, finger.getSeedBedPreparation());
        //values.put(KEY_FORM_DATE, finger.getCollectionDate());

        db.insert(TABLE_FINGER_ONE, null, values);
    }

    /**
     * Add finger two form
     *
     * @param finger
     */
    public void addFingerTwo(FingerTwo finger) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_COMPANY_ID, finger.getCompanyId());
        values.put(KEY_USER_ID, finger.getUserId());
        values.put(KEY_FARM_ID, finger.getFarmId());
        values.put(KEY_CORRECT_SEED, finger.getCorrectSeedPlanting());
        values.put(KEY_ROW_SPACING, finger.getRowSpacing());
        values.put(KEY_SEEDS_PER_STATION, finger.getSeedsPerStation());
        values.put(KEY_PLANTING_TIME, finger.getPlantingTime());

        db.insert(TABLE_FINGER_TWO, null, values);
    }

    /**
     * Add finger three form
     *
     * @param finger
     */
    public void addFingerThree(FingerThree finger) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_COMPANY_ID, finger.getCompanyId());
        values.put(KEY_USER_ID, finger.getUserId());
        values.put(KEY_FARM_ID, finger.getFarmId());
        values.put(KEY_GAP_FILL, finger.getGapFilling());
        values.put(KEY_GAP_FILL_ON_EMERGENCE, finger.getFillAfterEmergence());
        values.put(KEY_THINNING_NUM, finger.getRecommendedPlantsPerStation());
        values.put(KEY_THIN_AFTER_EMERGENCE, finger.getThinningAfterEmergence());

        db.insert(TABLE_FINGER_THREE, null, values);
    }

    /**
     * Add finger four form
     *
     * @param finger
     */
    public void addFingerFour(FingerFour finger) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_COMPANY_ID, finger.getCompanyId());
        values.put(KEY_USER_ID, finger.getUserId());
        values.put(KEY_FARM_ID, finger.getFarmId());
        values.put(KEY_FIRST_BRANCH, finger.getFirstBranch());
        values.put(KEY_FOLIAR, finger.getFoliar());
        values.put(KEY_WEEDS, finger.getWeeds());
        //values.put(KEY_FORM_DATE, finger.getCollectionDate());

        db.insert(TABLE_FINGER_FOUR, null, values);
    }

    /**
     * Add finger five form
     *
     * @param finger
     */
    public void addFingerFive(FingerFive finger) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_COMPANY_ID, finger.getCompanyId());
        values.put(KEY_USER_ID, finger.getUserId());
        values.put(KEY_FARM_ID, finger.getFarmId());
        values.put(KEY_PEST_LEVEL, finger.getPestLevel());
        values.put(KEY_PEST_DAMAGE, finger.getPestDamage());
        values.put(KEY_LAST_SCOUT, finger.getLastScout());
        values.put(KEY_EMPTY_PESTICIDE_CANS, finger.getIfEmptyCansOnFarm());
        values.put(KEY_PEG_BOARD_AVAILABILITY, finger.getPegBoardAvailability());
        values.put(KEY_SCOUTING_METHOD, finger.getScoutMethod());
        values.put(KEY_SPRAY_TIME, finger.getSprayTime());
        values.put(KEY_PEST_ABSENSE_DURATION, finger.getPestAbsenceDuration());
        values.put(KEY_CORRECT_USE_PESTICIDE, finger.getCorrectPesticideUse());
        values.put(KEY_SAFE_USAGE_CANS, finger.getSafeUsageCans());
        values.put(KEY_FORM_DATE, finger.getCollectionDate());

        db.insert(TABLE_FINGER_FIVE, null, values);
    }

    /**
     * Get all finger one  forms
     *
     * @return
     */
    public List<FingerOne> getAllFingerOne() {
        List<FingerOne> allFingerOnes = new ArrayList<FingerOne>();

        String selectQuery = "SELECT  * FROM " + TABLE_FINGER_ONE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                FingerOne finger = new FingerOne();
                finger.setCompanyId(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));
                finger.setUserId(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                finger.setFarmId(cursor.getString(cursor.getColumnIndex(KEY_FARM_ID)));
                finger.setSoilType(cursor.getString(cursor.getColumnIndex(KEY_SOIL_TYPE)));
                finger.setWaterLogRisk(cursor.getString(cursor.getColumnIndex(KEY_WATER_LOG_RISK)));
                finger.setErosionPrevention(cursor.getString(cursor.getColumnIndex(KEY_EROSION_PREVENTION)));
                finger.setCropRotation(cursor.getString(cursor.getColumnIndex(KEY_CROP_ROTATION)));
                finger.setRatoon(cursor.getString(cursor.getColumnIndex(KEY_RATOON)));
                finger.setCropResidues(cursor.getString(cursor.getColumnIndex(KEY_CROP_RESIDUES)));
                finger.setManure(cursor.getString(cursor.getColumnIndex(KEY_MANURE)));
                finger.setLandPrep(cursor.getString(cursor.getColumnIndex(KEY_LAND_PREPARATION)));
                finger.setSeeBedPreparation(cursor.getString(cursor.getColumnIndex(KEY_SEED_BED_PREPARATION)));

                allFingerOnes.add(finger);
            } while (cursor.moveToNext());
        }

        return allFingerOnes;
    }

    /**
     * Get all finger two forms
     *
     * @return
     */
    public List<FingerTwo> getAllFingerTwo() {
        List<FingerTwo> allFingerOnes = new ArrayList<FingerTwo>();

        String selectQuery = "SELECT  * FROM " + TABLE_FINGER_TWO;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                FingerTwo finger = new FingerTwo();
                finger.setCompanyId(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));
                finger.setUserId(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                finger.setFarmId(cursor.getString(cursor.getColumnIndex(KEY_FARM_ID)));
                finger.setCorrectSeedPlanting(cursor.getString(cursor.getColumnIndex(KEY_CORRECT_SEED)));
                finger.setRowSpacing(cursor.getString(cursor.getColumnIndex(KEY_ROW_SPACING)));
                finger.setSeedsPerStation(cursor.getString(cursor.getColumnIndex(KEY_SEEDS_PER_STATION)));
                finger.setPlantingTime(cursor.getString(cursor.getColumnIndex(KEY_PLANTING_TIME)));
                allFingerOnes.add(finger);
            } while (cursor.moveToNext());
        }

        return allFingerOnes;
    }

    /**
     * Get all finger three  forms
     *
     * @return
     */
    public List<FingerThree> getAllFingerThree() {
        List<FingerThree> allFingerThree = new ArrayList<FingerThree>();

        String selectQuery = "SELECT  * FROM " + TABLE_FINGER_THREE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                FingerThree finger = new FingerThree();
                finger.setCompanyId(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));
                finger.setUserId(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                finger.setFarmId(cursor.getString(cursor.getColumnIndex(KEY_FARM_ID)));
                finger.setGapFilling(cursor.getString(cursor.getColumnIndex(KEY_GAP_FILL)));
                finger.setFillingAfterEmergence(cursor.getString(cursor.getColumnIndex(KEY_GAP_FILL_ON_EMERGENCE)));
                finger.setRecommendedPlantsPerStation(cursor.getString(cursor.getColumnIndex(KEY_THINNING_NUM)));
                finger.setThinningAfterEmergence(cursor.getString(cursor.getColumnIndex(KEY_THIN_AFTER_EMERGENCE)));
                allFingerThree.add(finger);
            } while (cursor.moveToNext());
        }

        return allFingerThree;
    }

    /**
     * Get all finger four  forms
     *
     * @return
     */
    public List<FingerFour> getAllFingerFour() {
        List<FingerFour> allFingerFour = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_FINGER_FOUR;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                FingerFour finger = new FingerFour();
                finger.setCompanyId(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));
                finger.setUserId(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                finger.setFarmId(cursor.getString(cursor.getColumnIndex(KEY_FARM_ID)));
                finger.setFirstBranching(cursor.getString(cursor.getColumnIndex(KEY_FIRST_BRANCH)));
                finger.setFoliar(cursor.getString(cursor.getColumnIndex(KEY_FOLIAR)));
                finger.setWeeds(cursor.getString(cursor.getColumnIndex(KEY_WEEDS)));
                allFingerFour.add(finger);
            } while (cursor.moveToNext());
        }

        return allFingerFour;
    }

    /**
     * Get all finger five forms
     *
     * @return
     */
    public List<FingerFive> getAllFingerFive() {
        List<FingerFive> allFingerFive = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_FINGER_FIVE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                FingerFive finger = new FingerFive();
                finger.setCompanyId(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));
                finger.setUserId(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                finger.setFarmId(cursor.getString(cursor.getColumnIndex(KEY_FARM_ID)));
                finger.setPestLevel(cursor.getString(cursor.getColumnIndex(KEY_PEST_LEVEL)));
                finger.setPestDamage(cursor.getString(cursor.getColumnIndex(KEY_PEST_DAMAGE)));
                finger.setLastScout(cursor.getString(cursor.getColumnIndex(KEY_LAST_SCOUT)));
                finger.setEmptyCans(cursor.getString(cursor.getColumnIndex(KEY_EMPTY_PESTICIDE_CANS)));
                finger.setPegBoardAvail(cursor.getString(cursor.getColumnIndex(KEY_PEG_BOARD_AVAILABILITY)));
                finger.setScoutMethod(cursor.getString(cursor.getColumnIndex(KEY_SCOUTING_METHOD)));
                finger.setSprayTime(cursor.getString(cursor.getColumnIndex(KEY_SPRAY_TIME)));
                finger.setPestAbsenceDuration(cursor.getString(cursor.getColumnIndex(KEY_PEST_ABSENSE_DURATION)));
                finger.setCorrectPesticideUsage(cursor.getString(cursor.getColumnIndex(KEY_CORRECT_USE_PESTICIDE)));
                finger.setSafeCanUsage(cursor.getString(cursor.getColumnIndex(KEY_SAFE_USAGE_CANS)));

                allFingerFive.add(finger);
            } while (cursor.moveToNext());
        }

        return allFingerFive;
    }

    /**
     * Get farms by farmerId
     *
     * @param farmerID
     * @return
     */
    public List<Farm> getFarmsByFarmer(String farmerID) {
        List<Farm> farmsList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_FARMS + " WHERE " + KEY_FARMER_ID + " = '" + farmerID + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Farm farm = new Farm();
                farm.setFarmID(cursor.getString(cursor.getColumnIndex(KEY_FARM_ID)));
                farm.setFarmerID(cursor.getString(cursor.getColumnIndex(KEY_FARMER_ID)));
                farm.setFarmName(cursor.getString(cursor.getColumnIndex(KEY_FARM_NAME)));
                farm.setActualFarmArea(cursor.getString(cursor.getColumnIndex(KEY_ACTUAL_AREA)));
                farm.setEstimatedFarmArea(cursor.getString(cursor.getColumnIndex(KEY_ESTIMATED_AREA)));
                farm.setFarmPeri(cursor.getString(cursor.getColumnIndex(KEY_PERIMETER)));
                farm.setCompanyID(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));

                farmsList.add(farm);
            } while (cursor.moveToNext());
        }

        return farmsList;
    }

    /**
     * Get all farms marked for assesment
     *
     * @param villages
     * @return
     */
    public List<Farm> getAllFarmsForAss(String villages) {
        List<Farm> allFarms = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_FARMS + " WHERE " + KEY_FARM_ASS + " = 'yes' AND "
                + KEY_VILLAGE_ID + " IN(" + villages + ") ORDER BY " + KEY_FARM_NAME + " ASC";
        Log.e("ASSFarms SQL: ", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Farm farm = new Farm();
                farm.setFarmID(cursor.getString(cursor.getColumnIndex(KEY_FARM_ID)));
                farm.setFarmName(cursor.getString(cursor.getColumnIndex(KEY_FARM_NAME)));
                farm.setEstimatedFarmArea(cursor.getString(cursor.getColumnIndex(KEY_ESTIMATED_AREA)));
                farm.setIsForAss(cursor.getString(cursor.getColumnIndex(KEY_FARM_ASS)));
                farm.setCompanyID(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));

                allFarms.add(farm);
            } while (cursor.moveToNext());
        }

        return allFarms;
    }

    /**
     * Get officers' training by date
     *
     * @param trainDate
     * @param userID
     * @return
     */
    public List<OfficerTraining> getUserTrainingsByDate(String trainDate, String userID) {
        List<OfficerTraining> trainingsList = new ArrayList<>();

        String selectQuery = "SELECT " + KEY_TRAIN_CAT + "," + KEY_FARM_ID + "," + KEY_FARMER_ID + "" +
                " FROM " + TABLE_EXT_OFFICER_TRAINING + " WHERE DATE(" + TABLE_EXT_OFFICER_TRAINING + "." + KEY_TRAIN_DATE + ") = Date('" + trainDate + "')" +
                " AND " + KEY_USER_ID + " = '" + userID + "'";

        Log.i(TAG, selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                OfficerTraining officerTraining = new OfficerTraining();
                officerTraining.setTrainCat(cursor.getString(cursor.getColumnIndex(KEY_TRAIN_CAT)));
                officerTraining.setFarmID(cursor.getString(cursor.getColumnIndex(KEY_FARM_ID)));
                officerTraining.setFarmerID(cursor.getString(cursor.getColumnIndex(KEY_FARMER_ID)));
                trainingsList.add(officerTraining);
            } while (cursor.moveToNext());
        }

        return trainingsList;
    }


    /**
     * Get farms latitude and longitude
     *
     * @param farmID
     * @return
     */
    public Farm getFarmsLatLng(String farmID) {
        Farm farm = new Farm();

        String farmsQuery = "SELECT " + KEY_LATITUDE + "," + KEY_LONGITUDE + " FROM " + TABLE_FARMS + " WHERE "
                + KEY_FARM_ID + " = '" + farmID + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor2 = db.rawQuery(farmsQuery, null);
        if (cursor2.moveToFirst()) {
            do {
                farm.setLatitude(cursor2.getString(cursor2.getColumnIndex(KEY_LATITUDE)));
                farm.setLongitude(cursor2.getString(cursor2.getColumnIndex(KEY_LONGITUDE)));

            } while (cursor2.moveToNext());
        }
        return farm;
    }

    /**
     * Add signed docs to database
     *
     * @param signed_doc
     */
    public void addSignedDoc(SignedDoc signed_doc) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FARMER_ID, signed_doc.getFarmerId());
        values.put(KEY_USER_ID, signed_doc.getUserId());
        values.put(KEY_COMPANY_ID, signed_doc.getCompanyId());

        db.insert(TABLE_SIGNED_DOCS, null, values);

    }

    /**
     * Get all signed docs
     *
     * @return
     */
    public List<SignedDoc> getAllSignedDocs() {
        List<SignedDoc> allSignedDocs = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_SIGNED_DOCS;
        Log.e("SIGNED SQL:", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                SignedDoc doc = new SignedDoc();
                doc.setFarmerId(cursor.getString(cursor.getColumnIndex(KEY_FARMER_ID)));
                doc.setUserId(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                doc.setCompanyId(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));

                allSignedDocs.add(doc);
            } while (cursor.moveToNext());
        }

        return allSignedDocs;
    }

    /**
     * Add(mapped farm details) to database
     *
     * @param mappedFarm
     */
    public void addMappedFarm(MappedFarm mappedFarm) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_USER_ID, mappedFarm.getUserId());
        values.put(KEY_COMPANY_ID, mappedFarm.getCompanyId());
        values.put(KEY_FARM_ID, mappedFarm.getFarmId());
        values.put(KEY_LATITUDE, mappedFarm.getLatitude());
        values.put(KEY_LONGITUDE, mappedFarm.getLongitude());
        values.put(KEY_POINTS, mappedFarm.getPoints());
        values.put(KEY_ACTUAL_AREA, mappedFarm.getActualFarmArea());
        values.put(KEY_PERIMETER, mappedFarm.getPerimeter());

        db.insert(TABLE_MAPPED_FARMS, null, values);
    }

    /**
     * Get all mapped farms
     *
     * @return
     */
    public List<MappedFarm> getAllMappedFarms() {
        List<MappedFarm> mappedFarms = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_MAPPED_FARMS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MappedFarm mappedFarm = new MappedFarm();
                mappedFarm.setUserId(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                mappedFarm.setCompanyId(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));
                mappedFarm.setFarmId(cursor.getString(cursor.getColumnIndex(KEY_FARM_ID)));
                mappedFarm.setLatitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                mappedFarm.setLongitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                mappedFarm.setActualFarmArea(cursor.getString(cursor.getColumnIndex(KEY_ACTUAL_AREA)));
                mappedFarm.setPerimeter(cursor.getString(cursor.getColumnIndex(KEY_PERIMETER)));
                mappedFarm.setPoints(cursor.getString(cursor.getColumnIndex(KEY_POINTS)));

                mappedFarms.add(mappedFarm);
            } while (cursor.moveToNext());
        }

        return mappedFarms;
    }

    /**
     * Get farm area by farmID
     *
     * @param farmID
     * @return
     */
    public String getFarmArea(String farmID) {
        String farm_area = null;

        String selectQuery = "SELECT  " + KEY_ESTIMATED_AREA + ", " + KEY_ACTUAL_AREA + "  FROM " + TABLE_FARMS
                + " WHERE " + KEY_FARM_ID + " = '" + farmID + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Farm farm = new Farm();
                farm.setActualFarmArea(cursor.getString(cursor.getColumnIndex(KEY_ACTUAL_AREA)));
                farm.setEstimatedFarmArea(cursor.getString(cursor.getColumnIndex(KEY_ESTIMATED_AREA)));

                if (Float.parseFloat(farm.getActualFarmArea()) > 0) {
                    farm_area = farm.getActualFarmArea();
                } else {
                    farm_area = farm.getEstimatedFarmArea();
                }

            } while (cursor.moveToNext());
        }
        return farm_area;
    }

    /**
     * get assigned inputs by generated farmer id
     * (Used by list view filter when collecting inputs)
     *
     * @param query
     * @return
     */
    public Cursor fetchInputsByCardNo(String query) {
        Cursor mCursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if (query == null || query.length() == 0) {
                mCursor = db.query(TABLE_ASSIGNED_INPUTS + " LEFT_THUMB JOIN " + TABLE_REGISTERED_FARMERS + " on "
                                + TABLE_ASSIGNED_INPUTS + "." + KEY_FARMER_ID + " = " + TABLE_REGISTERED_FARMERS + "." + KEY_FARMER_ID,
                        new String[]{TABLE_ASSIGNED_INPUTS + "." + KEY_ORDER_ID + " as _id ",
                                TABLE_REGISTERED_FARMERS + "." + KEY_FARMER_ID, KEY_ORDER_ID, KEY_ASS_INPUT_ID,
                                TABLE_ASSIGNED_INPUTS + "." + KEY_CARD_NO, KEY_INPUT_TYPE, KEY_INPUT_QUANTITY,
                                KEY_ORDER_NUM, KEY_INPUT_TOTAL, KEY_FNAME, KEY_LNAME},
                        null, null, null, null, null, null);

            } else {
//                mCursor = db.query(true, TABLE_ASSIGNED_INPUTS + " LEFT_THUMB JOIN " + TABLE_REGISTERED_FARMERS + " on "
//                                + TABLE_ASSIGNED_INPUTS + "." + KEY_FARMER_ID + " = " + TABLE_REGISTERED_FARMERS + "." + KEY_FARMER_ID,
//                        new String[]{TABLE_ASSIGNED_INPUTS + "." + KEY_ORDER_ID + " as _id ",
//                                TABLE_REGISTERED_FARMERS + "." + KEY_FARMER_ID, KEY_ORDER_ID, KEY_ASS_INPUT_ID,
//                                TABLE_ASSIGNED_INPUTS + "." + KEY_GEN_ID, KEY_INPUT_TYPE, KEY_INPUT_QUANTITY,
//                                KEY_ORDER_NUM, KEY_INPUT_TOTAL, KEY_FNAME, KEY_LNAME},
//                        TABLE_REGISTERED_FARMERS + "." + KEY_CARD_NO + " like '%" + query + "%' OR "
//                                + TABLE_REGISTERED_FARMERS + "." + KEY_FNAME + " like '%" + query + "%' OR "
//                                + TABLE_REGISTERED_FARMERS + "." + KEY_LNAME + " like '%" + query + "%'", null, null, null,
//                        null, null);
                mCursor = db.query(true, TABLE_ASSIGNED_INPUTS + " LEFT OUTER JOIN " + TABLE_REGISTERED_FARMERS + " on "
                                + TABLE_ASSIGNED_INPUTS + "." + KEY_FARMER_ID + " = " + TABLE_REGISTERED_FARMERS + "." + KEY_FARMER_ID,
                        new String[]{TABLE_ASSIGNED_INPUTS + "." + KEY_ORDER_ID + " as _id ",
                                TABLE_REGISTERED_FARMERS + "." + KEY_FARMER_ID,
                                TABLE_ASSIGNED_INPUTS + "." + KEY_ORDER_ID, TABLE_ASSIGNED_INPUTS + "." + KEY_ASS_INPUT_ID,
                                TABLE_REGISTERED_FARMERS + "." + KEY_GEN_ID, TABLE_ASSIGNED_INPUTS + "." + KEY_INPUT_TYPE,
                                TABLE_ASSIGNED_INPUTS + "." + KEY_INPUT_QUANTITY,
                                TABLE_ASSIGNED_INPUTS + "." + KEY_ORDER_NUM, TABLE_ASSIGNED_INPUTS + "." + KEY_INPUT_TOTAL,
                                TABLE_REGISTERED_FARMERS + "." + KEY_FNAME, TABLE_REGISTERED_FARMERS + "." + KEY_LNAME},
                        TABLE_REGISTERED_FARMERS + "." + KEY_CARD_NO + " like '%" + query + "%' OR "
                                + TABLE_REGISTERED_FARMERS + "." + KEY_FNAME + " like '%" + query + "%' OR "
                                + TABLE_REGISTERED_FARMERS + "." + KEY_LNAME + " like '%" + query + "%'", null, null, null,
                        null, null);
            }
            if (mCursor != null) {
                mCursor.moveToFirst();
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
        return mCursor;
    }

    /**
     * Add form type FarmAssMedium to database
     *
     * @param farmAssMedium
     */
    public void addFarmAssMedium(FarmAssFormsMedium farmAssMedium) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FARM_ID, farmAssMedium.getFarmId());
        values.put(KEY_FORM_TYPE_ID, farmAssMedium.getFormTypeId());
        values.put(KEY_ACTIVITY_METHOD, farmAssMedium.getActivityMethod());
        values.put(KEY_ACTIVITY_DATE, farmAssMedium.getActivityDate());
        values.put(KEY_FAMILY_HOURS, farmAssMedium.getFamilyHours());
        values.put(KEY_HIRED_HOURS, farmAssMedium.getHiredHours());
        values.put(KEY_COLLECT_DATE, farmAssMedium.getCollectDate());
        values.put(KEY_MONEY_OUT, farmAssMedium.getMoneyOut());
        values.put(KEY_INPUT_ID, farmAssMedium.getInputId());
        values.put(KEY_INPUT_QUANTITY, farmAssMedium.getInputQuantity());
        values.put(KEY_SPRAY_TYPE, farmAssMedium.getSprayType());
        values.put(KEY_LATITUDE, farmAssMedium.getLatitude());
        values.put(KEY_LONGITUDE, farmAssMedium.getLongitude());
        values.put(KEY_USER_ID, farmAssMedium.getUserId());
        values.put(KEY_COMPANY_ID, farmAssMedium.getCompanyId());

        db.insert(TABLE_FARM_ASS_MEDIUM, null, values);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // throw new UnsupportedOperationException("Not supported yet."); //To
        // change body of generated methods, choose Tools | Templates.
        if (newVersion == 2) {
            db.execSQL("ALTER TABLE " + TABLE_FARM_HISTORY_ITEMS + " ADD COLUMN " + KEY_PROPERTY_OWNED + " BOOLEAN DEFAULT 0");
            db.execSQL("ALTER TABLE " + TABLE_FARMERS + " ADD COLUMN " + KEY_CONTRACT_ID + " TEXT DEFAULT '0'");
        }
    }


    public void updateFarmerTimes(String farmerID, String extTrainID, String trainID, String farmerTimeOut, String userID, String companyID) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE " + TABLE_FARMER_TIMES + " set " + KEY_FARMER_TIME_OUT + " = '" + farmerTimeOut
                + "' WHERE " + KEY_FARMER_ID + " = " + farmerID + " AND " + KEY_EXT_TRAIN_ID + " = " + extTrainID + " AND " + KEY_TRAIN_ID + " = " + trainID + " AND " + KEY_USER_ID
                + " = " + userID + " AND " + KEY_FARMER_TIME_OUT + " = '0' AND " + KEY_COMPANY_ID + " = " + companyID
                + " AND DATE(" + KEY_FARMER_TIME_IN + ") = DATE('" + farmerTimeOut + "')");
    }

    /**
     * Auto tap out farmers when training ends
     *
     * @param trainCatID
     * @param trainStopTime
     */
    public void autoTapOut(String trainCatID, String trainStopTime) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE " + TABLE_FARMER_TIMES + " set " + KEY_FARMER_TIME_OUT + " = '" + trainStopTime + "' "
                + " WHERE " + KEY_TRAIN_ID + " = " + trainCatID + " AND " + KEY_FARMER_TIME_OUT + " = '0'");
    }

    /**
     * Get all farmers that have not been tapped out at the end of training
     *
     * @param trainID
     * @return
     */
    public List<String> getAllFarmersNotTappedOut(String trainID) {
        List<String> farmerIDList = new ArrayList<String>();

        String selectQuery = "SELECT  " + KEY_FARMER_ID + " FROM " + TABLE_FARMER_TIMES + " WHERE " + KEY_TRAIN_ID + " = '"
                + trainID + "' AND " + KEY_FARMER_TIME_OUT + " = '0'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                FarmerTime farmer_time = new FarmerTime();
                farmer_time.setFarmerID(cursor.getString(cursor.getColumnIndex(KEY_FARMER_ID)));

                farmerIDList.add(farmer_time.getFarmerID());
            } while (cursor.moveToNext());
        }

        return farmerIDList;
    }

    /**
     * Retap in the next successive training
     *
     * @param notTappedOut
     * @param extTrainID
     * @param trainID
     * @param trainStartTime
     * @param userID
     * @param companyID
     */
    public void reTapFarmers(String notTappedOut, String extTrainID, String trainID, String trainStartTime,
                             String userID, String companyID) {

        String[] farmerIDs = TextUtils.split(notTappedOut, ",");

        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        for (String cn : farmerIDs) {
            values.put(KEY_FARMER_ID, cn);
            values.put(KEY_TRAIN_ID, trainID);
            values.put(KEY_EXT_TRAIN_ID, extTrainID);
            values.put(KEY_FARMER_TIME_IN, trainStartTime);
            values.put(KEY_FARMER_TIME_OUT, "0");
            values.put(KEY_USER_ID, userID);
            values.put(KEY_COMPANY_ID, companyID);
        }
        db.insert(TABLE_FARMER_TIMES, null, values);
    }

    /**
     * Method to add molasses trap catch data to  database
     *
     * @param molassesTrapCatch
     */
    public void addMolassesTrapCatch(MolassesTrapCatch molassesTrapCatch) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FARM_ID, molassesTrapCatch.getFarmId());
        values.put(KEY_TRAP_DATE, molassesTrapCatch.getTrapDate());
        values.put(KEY_TRAP_ONE, molassesTrapCatch.getTrapOne());
        values.put(KEY_TRAP_TWO, molassesTrapCatch.getTrapTwo());
        values.put(KEY_ACTION_TAKEN, molassesTrapCatch.getAction());
        values.put(KEY_USER_ID, molassesTrapCatch.getUserId());
        values.put(KEY_LATITUDE, molassesTrapCatch.getLatitude());
        values.put(KEY_LONGITUDE, molassesTrapCatch.getLongitude());
        values.put(KEY_COLLECT_DATE, molassesTrapCatch.getCollectDate());
        values.put(KEY_COMPANY_ID, molassesTrapCatch.getCompanyId());

        db.insert(TABLE_MOLASSES_TRAP_CATCHES, null, values);
    }

    /**
     * Get all mollases trap catch form data
     *
     * @return
     */
    public List<MolassesTrapCatch> getAllMolassesTrapCatches() {
        List<MolassesTrapCatch> catchList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_MOLASSES_TRAP_CATCHES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MolassesTrapCatch bugCatch = new MolassesTrapCatch();

                bugCatch.setFarmId(cursor.getString(cursor.getColumnIndex(KEY_FARM_ID)));
                bugCatch.setTrapDate(cursor.getString(cursor.getColumnIndex(KEY_TRAP_DATE)));
                bugCatch.setCollectDate(cursor.getString(cursor.getColumnIndex(KEY_COLLECT_DATE)));
                bugCatch.setTrapOne(cursor.getString(cursor.getColumnIndex(KEY_TRAP_ONE)));
                bugCatch.setTrapTwo(cursor.getString(cursor.getColumnIndex(KEY_TRAP_TWO)));
                bugCatch.setLatitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                bugCatch.setLongitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                bugCatch.setAction(cursor.getString(cursor.getColumnIndex(KEY_ACTION_TAKEN)));
                bugCatch.setUserId(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                bugCatch.setCompanyId(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));

                catchList.add(bugCatch);
            } while (cursor.moveToNext());
        }

        return catchList;
    }

    /**
     * Add scouting data
     *
     * @param scouting
     */
    public void addScouting(Scouting scouting) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FARM_ID, scouting.getFarmId());
        values.put(KEY_ACTIVITY_DATE, scouting.getActivityDate());
        values.put(KEY_FAMILY_HOURS, scouting.getFamilyHours());
        values.put(KEY_HIRED_HOURS, scouting.getHiredHours());
        values.put(KEY_COLLECT_DATE, scouting.getCollectDate());
        values.put(KEY_MONEY_OUT, scouting.getMoneyOut());
        values.put(KEY_BOLL_WORM, scouting.getBollWorm());
        values.put(KEY_JASSID, scouting.getJassid());
        values.put(KEY_STAINER, scouting.getStainer());
        values.put(KEY_APHID, scouting.getAphid());
        values.put(KEY_BENEFICIAL, scouting.getBeneficialInsects());
        values.put(KEY_SPRAY_DECISION, scouting.getSprayDecision());
        values.put(KEY_LATITUDE, scouting.getLatitude());
        values.put(KEY_LONGITUDE, scouting.getLongitude());
        values.put(KEY_USER_ID, scouting.getUserId());
        values.put(KEY_COMPANY_ID, scouting.getCompanyId());

        db.insert(TABLE_SCOUTING, null, values);
    }

    /**
     * Get all scouting data
     *
     * @return
     */
    public List<Scouting> getAllScoutings() {
        List<Scouting> scoutingList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_SCOUTING;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Scouting scouting = new Scouting();

                scouting.setFarmId(cursor.getString(cursor.getColumnIndex(KEY_FARM_ID)));
                scouting.setActivityDate(cursor.getString(cursor.getColumnIndex(KEY_ACTIVITY_DATE)));
                scouting.setCollectDate(cursor.getString(cursor.getColumnIndex(KEY_COLLECT_DATE)));
                scouting.setFamilyHours(cursor.getString(cursor.getColumnIndex(KEY_FAMILY_HOURS)));
                scouting.setHiredHours(cursor.getString(cursor.getColumnIndex(KEY_HIRED_HOURS)));
                scouting.setMoneyOut(cursor.getString(cursor.getColumnIndex(KEY_MONEY_OUT)));
                scouting.setBollWorm(cursor.getString(cursor.getColumnIndex(KEY_BOLL_WORM)));
                scouting.setJassid(cursor.getString(cursor.getColumnIndex(KEY_JASSID)));
                scouting.setStainer(cursor.getString(cursor.getColumnIndex(KEY_STAINER)));
                scouting.setAphid(cursor.getString(cursor.getColumnIndex(KEY_APHID)));
                scouting.setBeneficialInsects(cursor.getString(cursor.getColumnIndex(KEY_BENEFICIAL)));
                scouting.setSprayDecision(cursor.getString(cursor.getColumnIndex(KEY_SPRAY_DECISION)));
                scouting.setLatitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                scouting.setLongitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                //scouting.setUserId(cursor.getString(cursor.getColumnIndex(KEY_ACTION_TAKEN)));//TODO: Benson, Why is action taken here? Confirm with DOCs
                scouting.setUserId(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                scouting.setCompanyId(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));

                scoutingList.add(scouting);
            } while (cursor.moveToNext());
        }

        return scoutingList;
    }

    /**
     * Add transport modes
     *
     * @param transportModes
     */
    public void addTransportModes(TransportModes transportModes) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TRANS_MODE_ID, transportModes.getTransportModeId());
        values.put(KEY_TRANSPORT_MODE, transportModes.getTransportMode());
        db.insert(TABLE_TRANSPORT_MODES, null, values);


    }

    /**
     * Get all transport modes
     *
     * @return
     */

    public List<TransportModes> getAllTransportModes() {
        List<TransportModes> transModeList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_TRANSPORT_MODES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TransportModes transModes = new TransportModes();

                transModes.setTransportModeId(cursor.getString(cursor.getColumnIndex(KEY_TRANS_MODE_ID)));
                transModes.setTransportMode(cursor.getString(cursor.getColumnIndex(KEY_TRANSPORT_MODE)));

                transModeList.add(transModes);
            } while (cursor.moveToNext());
        }
        return transModeList;
    }

    /**
     * Add transportHseToMarket data
     *
     * @param transportHseToMarket
     */
    public void addTransportHseToMarket(TransportHseToMarket transportHseToMarket) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FARM_ID, transportHseToMarket.getFarmId());
        values.put(KEY_TRANPORT_COUNT, transportHseToMarket.getTransportCount());
        values.put(KEY_TRANS_DATE, transportHseToMarket.getDeliveryDate());
        values.put(KEY_COLLECT_DATE, transportHseToMarket.getCollectDate());
        values.put(KEY_MONEY_OUT, transportHseToMarket.getMoneyOut());
        //values.put(KEY_TRANS_MODE_ID, transportHseToMarket.getTransModeId());
        values.put(KEY_LATITUDE, transportHseToMarket.getLatitude());
        values.put(KEY_LONGITUDE, transportHseToMarket.getLongitude());
        values.put(KEY_USER_ID, transportHseToMarket.getUserId());
        values.put(KEY_COMPANY_ID, transportHseToMarket.getCompanyId());
        db.insert(TABLE_TRANS_HSE_TO_MARKET, null, values);

    }

    /**
     * Get all transports house to market
     *
     * @return
     */
    public List<TransportHseToMarket> getAllTransPortHseToMarket() {
        List<TransportHseToMarket> deliveriesList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_TRANS_HSE_TO_MARKET;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TransportHseToMarket deliveries = new TransportHseToMarket();

                deliveries.setFarmId(cursor.getString(cursor.getColumnIndex(KEY_FARM_ID)));
                deliveries.setTransportCount(cursor.getString(cursor.getColumnIndex(KEY_TRANPORT_COUNT)));
                deliveries.setDeliveryDate(cursor.getString(cursor.getColumnIndex(KEY_TRANS_DATE)));
                deliveries.setCollectDate(cursor.getString(cursor.getColumnIndex(KEY_COLLECT_DATE)));
                deliveries.setMoneyOut(cursor.getString(cursor.getColumnIndex(KEY_MONEY_OUT)));
                //deliveries.setTransModeId(cursor.getString(cursor.getColumnIndex(KEY_TRANS_MODE_ID)));
                deliveries.setLatitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                deliveries.setLongitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                deliveries.setUserId(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                deliveries.setCompanyId(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));

                deliveriesList.add(deliveries);
            } while (cursor.moveToNext());
        }
        return deliveriesList;
    }

    /**
     * Add pickings
     *
     * @param farmProduction
     */
    public void addFarmProduction(FarmProduction farmProduction) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FARM_ID, farmProduction.getFarmId());
        values.put(KEY_PICKING_COUNT, farmProduction.getPickingCount());
        values.put(KEY_GRADE_A, farmProduction.getGradeA());
        values.put(KEY_GRADE_B, farmProduction.getGradeB());
        values.put(KEY_GRADE_C, farmProduction.getGradeC());
        values.put(KEY_PICKING_DATE, farmProduction.getPickingDate());
        values.put(KEY_COLLECT_DATE, farmProduction.getCollectDate());
        values.put(KEY_LATITUDE, farmProduction.getLatitude());
        values.put(KEY_LONGITUDE, farmProduction.getLongitude());
        values.put(KEY_USER_ID, farmProduction.getUserId());
        values.put(KEY_COMPANY_ID, farmProduction.getCompanyId());
        db.insert(TABLE_FARM_PRODUCTION, null, values);

    }

    /**
     * Get all pickings
     *
     * @return
     */
    public List<FarmProduction> getAllFarmProductions() {
        List<FarmProduction> pickingsList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_FARM_PRODUCTION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                FarmProduction pickings = new FarmProduction();

                pickings.setFarmId(cursor.getString(cursor.getColumnIndex(KEY_FARM_ID)));
                pickings.setPickingCount(cursor.getString(cursor.getColumnIndex(KEY_PICKING_COUNT)));
                pickings.setGradeA(cursor.getString(cursor.getColumnIndex(KEY_GRADE_A)));
                pickings.setGradeB(cursor.getString(cursor.getColumnIndex(KEY_GRADE_B)));
                pickings.setGradeC(cursor.getString(cursor.getColumnIndex(KEY_GRADE_C)));
                pickings.setCollectDate(cursor.getString(cursor.getColumnIndex(KEY_COLLECT_DATE)));
                pickings.setPickingDate(cursor.getString(cursor.getColumnIndex(KEY_PICKING_DATE)));
                pickings.setLatitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                pickings.setLongitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                pickings.setUserId(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                pickings.setCompanyId(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));

                pickingsList.add(pickings);
            } while (cursor.moveToNext());
        }
        return pickingsList;
    }

    /**
     * Add yield estimate
     *
     * @param yieldEstimate
     */
    public void addYieldEstimate(YieldEstimate yieldEstimate) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FARM_ID, yieldEstimate.getFarmId());
        values.put(KEY_SAMPLING_STATION, yieldEstimate.getSamplingStation());
        values.put(KEY_NUM_OF_PLANTS, yieldEstimate.getNumOfPlants());
        values.put(KEY_NUM_OF_BOLLS, yieldEstimate.getNumOfBolls());
        values.put(KEY_DISTANCE_TO_LEFT, yieldEstimate.getDistanceToLeft());
        values.put(KEY_DISTANCE_TO_RIGHT, yieldEstimate.getDistanceToRight());
        values.put(KEY_COLLECT_DATE, yieldEstimate.getCollectDate());
        values.put(KEY_LATITUDE, yieldEstimate.getLatitude());
        values.put(KEY_LONGITUDE, yieldEstimate.getLongitude());
        values.put(KEY_USER_ID, yieldEstimate.getUserId());
        values.put(KEY_COMPANY_ID, yieldEstimate.getCompanyId());

        db.insert(TABLE_YIELD_ESTIMATE, null, values);
    }

    /**
     * Get yield estimates
     *
     * @return
     */
    public List<YieldEstimate> getAllYieldEstimates() {
        List<YieldEstimate> yieldEstimatesList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_YIELD_ESTIMATE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                YieldEstimate estimate = new YieldEstimate();

                estimate.setFarmId(cursor.getString(cursor.getColumnIndex(KEY_FARM_ID)));
                estimate.setSamplingStation(cursor.getString(cursor.getColumnIndex(KEY_SAMPLING_STATION)));
                estimate.setNumOfPlants(cursor.getString(cursor.getColumnIndex(KEY_NUM_OF_PLANTS)));
                estimate.setNumOfBolls(cursor.getString(cursor.getColumnIndex(KEY_NUM_OF_BOLLS)));
                estimate.setDistanceToLeft(cursor.getString(cursor.getColumnIndex(KEY_DISTANCE_TO_LEFT)));
                estimate.setDistanceToRight(cursor.getString(cursor.getColumnIndex(KEY_DISTANCE_TO_RIGHT)));
                estimate.setCollectDate(cursor.getString(cursor.getColumnIndex(KEY_COLLECT_DATE)));
                estimate.setLatitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                estimate.setLongitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                estimate.setUserId(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                estimate.setCompanyId(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));

                yieldEstimatesList.add(estimate);
            } while (cursor.moveToNext());
        }
        return yieldEstimatesList;
    }

    /**
     * Get yield estimates by farmID
     *
     * @param farmID
     * @return
     */
    public List<YieldEstimate> getAllYieldEstimatesForFarm(String farmID) {
        List<YieldEstimate> yieldEstimatesList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_YIELD_ESTIMATE + " WHERE " + KEY_FARM_ID + " = " + farmID + " group by " + KEY_SAMPLING_STATION + " order by " + KEY_ID + " DESC LIMIT 5";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                YieldEstimate estimate = new YieldEstimate();

                estimate.setFarmId(cursor.getString(cursor.getColumnIndex(KEY_FARM_ID)));
                estimate.setSamplingStation(cursor.getString(cursor.getColumnIndex(KEY_SAMPLING_STATION)));
                estimate.setNumOfPlants(cursor.getString(cursor.getColumnIndex(KEY_NUM_OF_PLANTS)));
                estimate.setNumOfBolls(cursor.getString(cursor.getColumnIndex(KEY_NUM_OF_BOLLS)));
                estimate.setDistanceToLeft(cursor.getString(cursor.getColumnIndex(KEY_DISTANCE_TO_LEFT)));
                estimate.setDistanceToRight(cursor.getString(cursor.getColumnIndex(KEY_DISTANCE_TO_RIGHT)));
                estimate.setCollectDate(cursor.getString(cursor.getColumnIndex(KEY_COLLECT_DATE)));
                estimate.setLatitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                estimate.setLongitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                estimate.setUserId(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                estimate.setCompanyId(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));

                yieldEstimatesList.add(estimate);
            } while (cursor.moveToNext());
        }
        return yieldEstimatesList;
    }

    /**
     * Add farm income
     *
     * @param farmIncome
     */
    public void addFarmIncome(FarmIncome farmIncome) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FARM_ID, farmIncome.getFarmId());
        values.put(KEY_DELIVERY_COUNT, farmIncome.getDeliveryCount());
        values.put(KEY_GRADE_A, farmIncome.getGradeA());
        values.put(KEY_GRADE_B, farmIncome.getGradeB());
        values.put(KEY_GRADE_C, farmIncome.getGradeC());
        values.put(KEY_DELIVERY_DATE, farmIncome.getDeliveryDate());
        values.put(KEY_COLLECT_DATE, farmIncome.getCollectDate());
        values.put(KEY_LATITUDE, farmIncome.getLatitude());
        values.put(KEY_LONGITUDE, farmIncome.getLongitude());
        values.put(KEY_USER_ID, farmIncome.getUserId());
        values.put(KEY_COMPANY_ID, farmIncome.getCompanyId());
        db.insert(TABLE_FARM_INCOME, null, values);

    }

    /**
     * Get farm incomes
     *
     * @return
     */
    public List<FarmIncome> getAllFarmIncome() {
        List<FarmIncome> farmIncomeList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_FARM_INCOME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                FarmIncome farmIncome = new FarmIncome();

                farmIncome.setFarmId(cursor.getString(cursor.getColumnIndex(KEY_FARM_ID)));
                farmIncome.setDeliveryCount(cursor.getString(cursor.getColumnIndex(KEY_DELIVERY_COUNT)));
                farmIncome.setGradeA(cursor.getString(cursor.getColumnIndex(KEY_GRADE_A)));
                farmIncome.setGradeB(cursor.getString(cursor.getColumnIndex(KEY_GRADE_B)));
                farmIncome.setGradeC(cursor.getString(cursor.getColumnIndex(KEY_GRADE_C)));
                farmIncome.setCollectDate(cursor.getString(cursor.getColumnIndex(KEY_COLLECT_DATE)));
                farmIncome.setDeliveryDate(cursor.getString(cursor.getColumnIndex(KEY_DELIVERY_DATE)));
                farmIncome.setLatitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                farmIncome.setLongitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                farmIncome.setUserId(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                farmIncome.setCompanyId(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));

                farmIncomeList.add(farmIncome);
            } while (cursor.moveToNext());
        }
        return farmIncomeList;
    }

    /**
     * Add new estimated farm area
     *
     * @param updateFarmArea
     */
    public void addUpdateFarmArea(UpdateFarmArea updateFarmArea) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FARM_ID, updateFarmArea.getFarmID());
        values.put(KEY_FARM_SIZE, updateFarmArea.getNewArea());
        values.put(KEY_USER_ID, updateFarmArea.getUserID());
        values.put(KEY_COMPANY_ID, updateFarmArea.getCompanyID());
        db.insert(TABLE_FARM_AREA_UPDATES, null, values);
    }

    /**
     * Get new estimated farm area
     *
     * @return
     */
    public List<UpdateFarmArea> getAllUpdateFarmArea() {
        List<UpdateFarmArea> farmAreaList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_FARM_AREA_UPDATES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                UpdateFarmArea farmArea = new UpdateFarmArea();

                farmArea.setFarmID(cursor.getString(cursor.getColumnIndex(KEY_FARM_ID)));
                farmArea.setNewArea(cursor.getString(cursor.getColumnIndex(KEY_FARM_SIZE)));
                farmArea.setUserID(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                farmArea.setCompanyID(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));

                farmAreaList.add(farmArea);
            } while (cursor.moveToNext());
        }
        return farmAreaList;
    }

    /**
     * Add collected inputs
     *
     * @param collectedInputs
     */
    public void addCollectedInputs(CollectedInputs collectedInputs) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ORDER_ID, collectedInputs.getOrderId());
        values.put(KEY_COLLECT_DATE, collectedInputs.getCollectDate());
        values.put(KEY_USER_ID, collectedInputs.getUserId());
        values.put(KEY_COLLECTION_METHOD, collectedInputs.getCollectionMethod());
        db.insert(TABLE_COLLECTED_INPUTS, null, values);

    }

    /**
     * Get all inputs collected
     *
     * @return
     */
    public List<CollectedInputs> getAllCollectedInputs() {
        List<CollectedInputs> collectedInputList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_COLLECTED_INPUTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CollectedInputs inputs = new CollectedInputs();

                inputs.setOrderId(cursor.getString(cursor.getColumnIndex(KEY_ORDER_ID)));
                inputs.setCollectDate(cursor.getString(cursor.getColumnIndex(KEY_COLLECT_DATE)));
                inputs.setUserId(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                inputs.setCollectionMethod(cursor.getString(cursor.getColumnIndex(KEY_COLLECTION_METHOD)));

                collectedInputList.add(inputs);
            } while (cursor.moveToNext());
        }
        return collectedInputList;
    }

    public List<FingerPrint> getAllFingerprints() {
        List<FingerPrint> fingerprintList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_FINGERPRINTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                FingerPrint fingerprint = new FingerPrint();

                fingerprint.setFarmerID(cursor.getString(cursor.getColumnIndex(KEY_FARMER_ID)));
                fingerprint.setGenID(cursor.getString(cursor.getColumnIndex(KEY_GEN_ID)));
                fingerprint.setFilePath(cursor.getString(cursor.getColumnIndex(KEY_FILE_PATH)));
                fingerprint.setCompanyID(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));

                fingerprintList.add(fingerprint);
            } while (cursor.moveToNext());
        }
        return fingerprintList;
    }

    /**
     * Add farmers that have shown intent to be contracted
     *
     * @param showIntent
     */
    public void addShowIntent(ShowIntent showIntent) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FARMER_ID, showIntent.getFarmerId());
        values.put(KEY_USER_ID, showIntent.getUserId());
        values.put(KEY_COMPANY_ID, showIntent.getCompanyId());
        db.insert(TABLE_SHOW_INTENT, null, values);

    }

    /**
     * Get all farmers that have shown intent to be contracted
     *
     * @return
     */
    public List<ShowIntent> getAllShowIntents() {
        List<ShowIntent> showIntentList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_SHOW_INTENT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ShowIntent showIntent = new ShowIntent();

                showIntent.setFarmerID(cursor.getString(cursor.getColumnIndex(KEY_FARMER_ID)));
                showIntent.setUserID(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                showIntent.setCompanyID(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));

                showIntentList.add(showIntent);
            } while (cursor.moveToNext());
        }
        return showIntentList;
    }

    /**
     * Remove assigned input after collection
     * (Avoid input being given twice)
     *
     * @param orderID
     */
    public void deleteAssignedInput(String orderID) {
        SQLiteDatabase db = this.getReadableDatabase();

        db.execSQL("delete from " + TABLE_ASSIGNED_INPUTS + " WHERE " + KEY_ORDER_ID + " = " + orderID);

    }

    public String getFName(String farmName) {
        String fName = null;
        String selectQuery = "SELECT  " + KEY_FNAME + " FROM " + TABLE_REGISTERED_FARMERS + " WHERE " + KEY_GEN_ID
                + " = '" + farmName + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Farmers farmer = new Farmers();
                farmer.setFName(cursor.getString(cursor.getColumnIndex(KEY_FNAME)));
                fName = farmer.getFName();

            } while (cursor.moveToNext());
        }
        return fName;
    }

    public String getLName(String farmName) {
        String lName = null;
        String selectQuery = "SELECT  " + KEY_FNAME + " FROM " + TABLE_REGISTERED_FARMERS + " WHERE " + KEY_GEN_ID
                + " = '" + farmName + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Farmers farmer = new Farmers();
                farmer.setLName(cursor.getString(cursor.getColumnIndex(KEY_LNAME)));
                lName = farmer.getFName();

            } while (cursor.moveToNext());
        }
        return lName;
    }

    /**
     * Add farmers with invalid passport photos,fingerprints
     *
     * @param wackFarmer
     */
    public void addWackFarmers(WackFarmer wackFarmer) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FARMER_ID, wackFarmer.getFarmerId());
        values.put(KEY_FNAME, wackFarmer.getFirstName());
        values.put(KEY_LNAME, wackFarmer.getLastName());
        values.put(KEY_GEN_ID, wackFarmer.getGenId());
        values.put(KEY_VILLAGE_ID, wackFarmer.getVillageId());
        values.put(KEY_WACK_STATUS, wackFarmer.getWackStatus());
        db.insert(TABLE_WACK_FARMERS, null, values);


    }

    /**
     * Get farmers with invalid passport photos,fingerprints
     */
    public List<WackFarmer> getWackFarmers() {
        List<WackFarmer> farmerlist = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_WACK_FARMERS + " WHERE " + KEY_WACK_STATUS + " = 'invalid'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                WackFarmer farmer = new WackFarmer();
                farmer.setFarmerID(cursor.getString(cursor.getColumnIndex(KEY_FARMER_ID)));
                farmer.setFirstName(cursor.getString(cursor.getColumnIndex(KEY_FNAME)));
                farmer.setLastName(cursor.getString(cursor.getColumnIndex(KEY_LNAME)));
                farmer.setGenID(cursor.getString(cursor.getColumnIndex(KEY_GEN_ID)));
                farmer.setVillageId(cursor.getString(cursor.getColumnIndex(KEY_VILLAGE_ID)));
                farmer.setWackStatus(cursor.getString(cursor.getColumnIndex(KEY_WACK_STATUS)));

                farmerlist.add(farmer);
            } while (cursor.moveToNext());
        }
        return farmerlist;
    }

    /**
     * Add registered farmers (Passport photo and fingerprints fixed)
     *
     * @param reRegisteredFarmers
     */
    public void addReRegisteredFarmer(ReRegisteredFarmers reRegisteredFarmers) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_COMPANY_ID, reRegisteredFarmers.getCompanyId());
        values.put(KEY_USER_ID, reRegisteredFarmers.getUserId());
        values.put(KEY_FARMER_ID, reRegisteredFarmers.getFarmerId());
        values.put(KEY_GEN_ID, reRegisteredFarmers.getGenId());
        values.put(KEY_PIC, reRegisteredFarmers.getFarmerPic());
        values.put(KEY_LEFT_THUMB, reRegisteredFarmers.getLeftThumb());
        values.put(KEY_RIGHT_THUMB, reRegisteredFarmers.getRightThumb());
        db.insert(TABLE_RE_REGISTERED_FARMERS, null, values);


    }

    public void addManualInputFingerprintRecapture(ManualInputFingerprintRecapture reRegisteredFarmers) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FARMER_ID, reRegisteredFarmers.getfId());
        values.put(KEY_GEN_ID, reRegisteredFarmers.getGenId());
        values.put(KEY_LEFT_THUMB, reRegisteredFarmers.getLeftThumb());
        values.put(KEY_RIGHT_THUMB, reRegisteredFarmers.getRightThumb());
        db.insert(TABLE_MANUAL_INPUT_FINGERPRINT_RECAPTURE, null, values);
    }

    /**
     * Get registered farmers (Passport photo and fingerprints fixed)
     */
    public List<ReRegisteredFarmers> getReRegisteredFarmers() {
        List<ReRegisteredFarmers> farmerslist = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_RE_REGISTERED_FARMERS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ReRegisteredFarmers farmer = new ReRegisteredFarmers();
                farmer.setRowId(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                farmer.setCompanyId(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));
                farmer.setUserId(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                farmer.setFarmerId(cursor.getString(cursor.getColumnIndex(KEY_FARMER_ID)));
                farmer.setGenId(cursor.getString(cursor.getColumnIndex(KEY_GEN_ID)));
                farmer.setFarmerPic(cursor.getString(cursor.getColumnIndex(KEY_PIC)));
                farmer.setLeftThumb(cursor.getString(cursor.getColumnIndex(KEY_LEFT_THUMB)));
                farmer.setRightThumb(cursor.getString(cursor.getColumnIndex(KEY_RIGHT_THUMB)));

                farmerslist.add(farmer);
            } while (cursor.moveToNext());
        }
        return farmerslist;
    }

    public List<ManualInputFingerprintRecapture> getManualInputFingerprintRecapture() {
        List<ManualInputFingerprintRecapture> farmerList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_MANUAL_INPUT_FINGERPRINT_RECAPTURE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ManualInputFingerprintRecapture farmer = new ManualInputFingerprintRecapture();
                farmer.setFarmerID(cursor.getString(cursor.getColumnIndex(KEY_FARMER_ID)));
                farmer.setGenID(cursor.getString(cursor.getColumnIndex(KEY_GEN_ID)));
                farmer.setLeftThumb(cursor.getString(cursor.getColumnIndex(KEY_LEFT_THUMB)));
                farmer.setRightThumb(cursor.getString(cursor.getColumnIndex(KEY_RIGHT_THUMB)));

                farmerList.add(farmer);
            } while (cursor.moveToNext());
        }
        return farmerList;
    }

    /**
     * Remove wack farmer after photo and finger prints retaken
     *
     * @param farmerID
     */
    public void removeWackFarmer(String farmerID) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_WACK_FARMERS, KEY_FARMER_ID + " = ?", new String[]{farmerID});
    }

    /**
     * Delete all items from a table
     *
     * @param tableName
     */
    public void clearTable(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete FROM " + tableName);
    }

    /**
     * remove specific item from table
     * e.g. uploading registered farmer one by one
     *
     * @param id
     * @param tableName
     */
    public void removeItemFromTable(String id, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(tableName, KEY_ID + " = ?", new String[]{id});
    }

    /**
     * Get farmers name
     *
     * @param genID
     * @return
     */
    public String getFarmerName(String genID) {
        String farmerName = null;

        String selectQuery = "SELECT  " + KEY_FNAME + "," + KEY_LNAME + " FROM " + TABLE_REGISTERED_FARMERS + " WHERE "
                + KEY_GEN_ID + " = '" + genID + "' ORDER BY " + KEY_FNAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                RegisteredFarmer farmer = new RegisteredFarmer();
                farmer.setFirstName(cursor.getString(cursor.getColumnIndex(KEY_FNAME)));
                farmer.setLastName(cursor.getString(cursor.getColumnIndex(KEY_LNAME)));

                farmerName = farmer.getLastName() + " " + farmer.getFirstName();
            } while (cursor.moveToNext());
        }
        cursor.close();
        return farmerName;
    }

    public void deleteFromTable(String farmID) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from " + TABLE_MAPPED_FARMS + " WHERE " + KEY_FARM_ID + " = " + farmID);
    }

    public String getFarmerIdByGenId(String genID) {
        String farmerID = null;
        String selectQuery = "SELECT  " + KEY_FARMER_ID + " FROM " + TABLE_REGISTERED_FARMERS + " WHERE " + KEY_GEN_ID
                + " = '" + genID + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                RegisteredFarmer farmer = new RegisteredFarmer();
                farmer.setFarmerId(cursor.getString(cursor.getColumnIndex(KEY_FARMER_ID)));

                farmerID = farmer.getFarmerId();
            } while (cursor.moveToNext());
        }
        cursor.close();
        return farmerID;
    }

    public RegisteredFarmer getFarmerByCardNo(String cardNo) {
        String farmerID = null;
        String selectQuery = "SELECT  " + KEY_FARMER_ID + "," + KEY_GEN_ID + " FROM " + TABLE_REGISTERED_FARMERS + " WHERE " + KEY_CARD_NO
                + " = '" + cardNo + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        RegisteredFarmer farmer = new RegisteredFarmer();

        if (cursor.moveToFirst()) {
            do {

                farmer.setFarmerId(cursor.getString(cursor.getColumnIndex(KEY_FARMER_ID)));
                farmer.setGenId(cursor.getString(cursor.getColumnIndex(KEY_GEN_ID)));

            } while (cursor.moveToNext());
        }
        cursor.close();
        return farmer;
    }

    public RegisteredFarmer getFarmerById(int farmerId) {
        String farmerID = null;
        String selectQuery = "SELECT  " + KEY_FARMER_ID + "," + KEY_VILLAGE_ID + "," + KEY_GEN_ID + " FROM " + TABLE_REGISTERED_FARMERS + " WHERE " + KEY_FARMER_ID
                + " = '" + farmerId + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        RegisteredFarmer farmer = new RegisteredFarmer();

        if (cursor.moveToFirst()) {
            do {

                farmer.setFarmerId(cursor.getString(cursor.getColumnIndex(KEY_FARMER_ID)));
                farmer.setGenId(cursor.getString(cursor.getColumnIndex(KEY_GEN_ID)));
                farmer.setVillageId(cursor.getInt(cursor.getColumnIndex(KEY_VILLAGE_ID)));

            } while (cursor.moveToNext());
        }
        cursor.close();
        return farmer;
    }

    /**
     * Add all data collected per farm
     *
     * @param farmDataColleted
     */
    public void addFarmDataCollected(FarmDataColleted farmDataColleted) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FARM_ID, farmDataColleted.getFarmID());
        values.put(KEY_FORM_TYPE_ID, farmDataColleted.getFormTypeID());
        db.insert(TABLE_FARM_DATA_COLLECTED, null, values);

    }

    /**
     * Get all farm data collected by farm
     *
     * @param farmID
     * @return
     */
    public List<FarmDataColleted> getAllFarmDataCollectedByFarmId(String farmID) {
        List<FarmDataColleted> farmDataList = new ArrayList<>();

        String selectQuery = "SELECT  " + KEY_FORM_TYPE_ID + " FROM " + TABLE_FARM_DATA_COLLECTED + " WHERE "
                + KEY_FARM_ID + " = " + farmID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                FarmDataColleted farmData = new FarmDataColleted();

                // farmData.setFarmId(cursor.getString(cursor.getColumnIndex(KEY_FARM_ID)));
                farmData.setFormTypeId(cursor.getString(cursor.getColumnIndex(KEY_FORM_TYPE_ID)));

                farmDataList.add(farmData);
            } while (cursor.moveToNext());
        }
        return farmDataList;

    }

    /**
     * Get all farm data collected
     *
     * @return
     */
    public List<FarmDataColleted> getAllFarmDataCollected() {
        List<FarmDataColleted> farmDataColletedList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_FARM_DATA_COLLECTED;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                FarmDataColleted farmDataColleted = new FarmDataColleted();

                farmDataColleted.setFarmId(cursor.getString(cursor.getColumnIndex(KEY_FARM_ID)));
                farmDataColleted.setFormTypeId(cursor.getString(cursor.getColumnIndex(KEY_FORM_TYPE_ID)));

                farmDataColletedList.add(farmDataColleted);
            } while (cursor.moveToNext());
        }
        return farmDataColletedList;

    }

    /**
     * Get cotton boll weight for a farm
     *
     * @param farmID
     * @return
     */
    public Double getBollWeightForFarm(String farmID) {
        String bollWeight = null;

        String selectQuery = "SELECT  " + KEY_BOLL_WEIGHT + " FROM " + TABLE_WARDS + " INNER JOIN " + TABLE_VILLAGES + " "
                + " ON " + TABLE_VILLAGES + "." + KEY_WARD_ID + " = " + TABLE_WARDS + "." + KEY_WARD_ID + " INNER JOIN " + TABLE_FARMS + " "
                + " ON " + TABLE_VILLAGES + "." + KEY_VILLAGE_ID + " = " + TABLE_FARMS + "." + KEY_VILLAGE_ID + " WHERE " + KEY_FARM_ID + " = " + farmID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Ward ward = new Ward();
                ward.setBollWeight(cursor.getString(cursor.getColumnIndex(KEY_BOLL_WEIGHT)));
                bollWeight = ward.getBollWeight();
            } while (cursor.moveToNext());
        }

        return Double.valueOf(bollWeight);
    }

    /**
     * Add extension officers' training data
     *
     * @param officerTraining
     */
    public void addOfficerTraining(OfficerTraining officerTraining) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EXT_TRAIN_ID, officerTraining.getExtTrainID());
        values.put(KEY_USER_ID, officerTraining.getUserID());
        values.put(KEY_TRAIN_DATE, officerTraining.getTrainDate());
        values.put(KEY_TRAIN_CAT_ID, officerTraining.getTrainCatID());
        values.put(KEY_TRAIN_CAT, officerTraining.getTrainCat());
        values.put(KEY_FARM_ID, officerTraining.getFarmID());
        values.put(KEY_VILLAGE_ID, officerTraining.getVillageID());
        values.put(KEY_FARMER_ID, officerTraining.getFarmerID());

        db.insert(TABLE_EXT_OFFICER_TRAINING, null, values);
    }

    /**
     * Add product purchase
     *
     * @param productPurchase
     */
    public void addProductPurchase(ProductPurchase productPurchase) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_COMPANY_ID, productPurchase.getCompanyId());
        values.put(KEY_USER_ID, productPurchase.getUserId());
        values.put(KEY_RECEIPT_NO, productPurchase.getReceiptNumber());
        values.put(KEY_DEDUCTIONS, productPurchase.getDeductions());
        values.put(KEY_PRICE, productPurchase.getPrice());
        values.put(KEY_WEIGHT, productPurchase.getWeight());
        values.put(KEY_GRADE_ID, productPurchase.getGradeId());
        values.put(KEY_FARMER_ID, productPurchase.getFarmerId());

        db.insert(TABLE_PRODUCT_PURCHASES, null, values);
    }

    public List<ProductPurchase> getAllProductPurchases() {
        List<ProductPurchase> productPurchases = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_PRODUCT_PURCHASES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ProductPurchase productPurchase = new ProductPurchase();
                productPurchase.setUserId(cursor.getInt(cursor.getColumnIndex(KEY_USER_ID)));
                productPurchase.setReceiptNumber(cursor.getString(cursor.getColumnIndex(KEY_RECEIPT_NO)));
                productPurchase.setCompanyId(cursor.getInt(cursor.getColumnIndex(KEY_COMPANY_ID)));
                productPurchase.setDeductions(cursor.getFloat(cursor.getColumnIndex(KEY_DEDUCTIONS)));
                productPurchase.setPrice(cursor.getFloat(cursor.getColumnIndex(KEY_PRICE)));
                productPurchase.setWeight(cursor.getFloat(cursor.getColumnIndex(KEY_WEIGHT)));
                productPurchase.setGradeId(cursor.getInt(cursor.getColumnIndex(KEY_GRADE_ID)));
                productPurchase.setFarmerId(cursor.getInt(cursor.getColumnIndex(KEY_FARMER_ID)));

                productPurchases.add(productPurchase);
            } while (cursor.moveToNext());
        }
        return productPurchases;
    }

    /**
     * Get extension officers' training
     *
     * @param userId
     * @return
     */
    public List<OfficerTraining> getOfficerTrainingByUserId(String userId) {
        List<OfficerTraining> trainingList = new ArrayList<>();

        String selectQuery = "SELECT *,DATE(" + KEY_TRAIN_DATE + ") AS " + KEY_TRAIN_DATE
                + " FROM " + TABLE_EXT_OFFICER_TRAINING
                + " WHERE " + KEY_USER_ID + " = " + userId
                + " AND DATE (" + KEY_TRAIN_DATE + ") = DATE('NOW')";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                OfficerTraining training = new OfficerTraining();

                training.setExtTrainID(cursor.getString(cursor.getColumnIndex(KEY_EXT_TRAIN_ID)));
                training.setTrainDate(cursor.getString(cursor.getColumnIndex(KEY_TRAIN_DATE)));
                training.setTrainCatID(cursor.getString(cursor.getColumnIndex(KEY_TRAIN_CAT_ID)));
                training.setTrainCat(cursor.getString(cursor.getColumnIndex(KEY_TRAIN_CAT)));
                training.setFarmID(cursor.getString(cursor.getColumnIndex(KEY_FARM_ID)));
                training.setVillageID(cursor.getString(cursor.getColumnIndex(KEY_VILLAGE_ID)));
                training.setFarmerID(cursor.getString(cursor.getColumnIndex(KEY_FARMER_ID)));

                trainingList.add(training);
            } while (cursor.moveToNext());
        }
        return trainingList;
    }


    /**
     * Get all extension officers training
     *
     * @return
     */
    public List<OfficerTraining> getAllOfficerTraining() {
        List<OfficerTraining> trainingList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_EXT_OFFICER_TRAINING;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                OfficerTraining training = new OfficerTraining();
                training.setUserID(cursor.getString(cursor.getColumnIndex(KEY_USER_ID)));
                training.setExtTrainID(cursor.getString(cursor.getColumnIndex(KEY_EXT_TRAIN_ID)));
                training.setTrainDate(cursor.getString(cursor.getColumnIndex(KEY_TRAIN_DATE)));
                training.setTrainCatID(cursor.getString(cursor.getColumnIndex(KEY_TRAIN_CAT_ID)));
                training.setTrainCat(cursor.getString(cursor.getColumnIndex(KEY_TRAIN_CAT)));
                training.setFarmID(cursor.getString(cursor.getColumnIndex(KEY_FARM_ID)));
                training.setVillageID(cursor.getString(cursor.getColumnIndex(KEY_VILLAGE_ID)));
                training.setFarmerID(cursor.getString(cursor.getColumnIndex(KEY_FARMER_ID)));

                trainingList.add(training);
            } while (cursor.moveToNext());
        }
        return trainingList;
    }

    /**
     * Get all farms
     *
     * @return
     */
    public List<Farm> getAllFarms() {
        List<Farm> farmsList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_FARMS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Farm farm = new Farm();
                farm.setFarmID(cursor.getString(cursor.getColumnIndex(KEY_FARM_ID)));
                farm.setVillageID(cursor.getString(cursor.getColumnIndex(KEY_VILLAGE_ID)));
                farm.setIsForAss(cursor.getString(cursor.getColumnIndex(KEY_FARM_ASS)));
                farm.setLatitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                farm.setLongitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                farm.setFarmID(cursor.getString(cursor.getColumnIndex(KEY_FARM_ID)));
                farm.setFarmerID(cursor.getString(cursor.getColumnIndex(KEY_FARMER_ID)));
                farm.setFarmName(cursor.getString(cursor.getColumnIndex(KEY_FARM_NAME)));
                farm.setActualFarmArea(cursor.getString(cursor.getColumnIndex(KEY_ACTUAL_AREA)));
                farm.setEstimatedFarmArea(cursor.getString(cursor.getColumnIndex(KEY_ESTIMATED_AREA)));
                farm.setFarmPeri(cursor.getString(cursor.getColumnIndex(KEY_PERIMETER)));
                farm.setCompanyID(cursor.getString(cursor.getColumnIndex(KEY_COMPANY_ID)));

                farmsList.add(farm);
            } while (cursor.moveToNext());
        }

        return farmsList;
    }

    /**
     * Converts user village ids to CSV
     *
     * @param temp
     * @param string
     * @return
     */
    public String userVillageIdsToCsv(List<UserVillage> temp, String string) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < temp.size(); i++) {
            sb.append(temp.get(i).getVillageId());
            sb.append(string);
        }

        if (temp.size() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }

    /**
     * Add Cotton Deduction
     *
     * @param cottonDeduction
     */
    public void addCottonDeduction(CottonDeduction cottonDeduction) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DELIVERIES, cottonDeduction.getDeliveries());
        values.put(KEY_FARMER_ID, cottonDeduction.getFarmerId());
        values.put(KEY_DEDUCTIONS, cottonDeduction.getDeductions());
        values.put(KEY_SEASON_ID, cottonDeduction.getSeasonId());
        values.put(KEY_SEASON_NAME, cottonDeduction.getSeasonName());

        db.insert(TABLE_COTTON_DEDUCTIONS, null, values);
    }

    /**
     * Get all cotton deductions
     *
     * @return
     */
    public List<CottonDeduction> getAllCottonDeductions() {
        List<CottonDeduction> cottonDeductions = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_COTTON_DEDUCTIONS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CottonDeduction cottonDeduction = new CottonDeduction();
                cottonDeduction.setDeliveries(cursor.getInt(cursor.getColumnIndex(KEY_DELIVERIES)));
                cottonDeduction.setFarmerId(cursor.getInt(cursor.getColumnIndex(KEY_FARMER_ID)));
                cottonDeduction.setDeductions(cursor.getFloat(cursor.getColumnIndex(KEY_DEDUCTIONS)));
                cottonDeduction.setSeasonId(cursor.getInt(cursor.getColumnIndex(KEY_SEASON_ID)));
                cottonDeduction.setSeasonName(cursor.getString(cursor.getColumnIndex(KEY_SEASON_NAME)));

                cottonDeductions.add(cottonDeduction);
            } while (cursor.moveToNext());
        }
        return cottonDeductions;
    }

    /**
     * Add collected order
     *
     * @param collectedOrder
     */
    public void addCollectedOrder(CollectedOrder collectedOrder) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ORDERS_COUNT, collectedOrder.getOrders());
        values.put(KEY_FARMER_ID, collectedOrder.getFarmerId());
        values.put(KEY_TOTAL_AMOUNT, collectedOrder.getTotalAmount());
        values.put(KEY_SEASON_ID, collectedOrder.getSeasonId());
        values.put(KEY_SEASON_NAME, collectedOrder.getSeasonName());

        db.insert(TABLE_COLLECTED_ORDERS, null, values);
    }

    /**
     * Get all collected orders
     *
     * @return
     */
    public List<CollectedOrder> getAllCollectedOrders() {
        List<CollectedOrder> collectedOrders = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_COLLECTED_ORDERS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CollectedOrder collectedOrder = new CollectedOrder();
                collectedOrder.setOrders(cursor.getInt(cursor.getColumnIndex(KEY_ORDERS_COUNT)));
                collectedOrder.setFarmerId(cursor.getInt(cursor.getColumnIndex(KEY_FARMER_ID)));
                collectedOrder.setTotalAmount(cursor.getFloat(cursor.getColumnIndex(KEY_TOTAL_AMOUNT)));
                collectedOrder.setSeasonId(cursor.getInt(cursor.getColumnIndex(KEY_SEASON_ID)));
                collectedOrder.setSeasonName(cursor.getString(cursor.getColumnIndex(KEY_SEASON_NAME)));

                collectedOrders.add(collectedOrder);
            } while (cursor.moveToNext());
        }
        return collectedOrders;
    }

    /**
     * Add collected order
     *
     * @param recoveredCash
     */
    public void addRecoveredCash(RecoveredCash recoveredCash) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TIMES, recoveredCash.getTimes());
        values.put(KEY_FARMER_ID, recoveredCash.getFarmerId());
        values.put(KEY_TOTAL_AMOUNT, recoveredCash.getTotalAmount());
        values.put(KEY_SEASON_ID, recoveredCash.getSeasonId());
        values.put(KEY_SEASON_NAME, recoveredCash.getSeasonName());

        db.insert(TABLE_RECOVERED_CASH, null, values);
    }

    /**
     * Get all recovered cash
     *
     * @return
     */
    public List<RecoveredCash> getAllRecoveredCash() {
        List<RecoveredCash> recoveredCashes = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_RECOVERED_CASH;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                RecoveredCash recoveredCash = new RecoveredCash();
                recoveredCash.setTimes(cursor.getInt(cursor.getColumnIndex(KEY_TIMES)));
                recoveredCash.setFarmerId(cursor.getInt(cursor.getColumnIndex(KEY_FARMER_ID)));
                recoveredCash.setTotalAmount(cursor.getFloat(cursor.getColumnIndex(KEY_TOTAL_AMOUNT)));
                recoveredCash.setSeasonId(cursor.getInt(cursor.getColumnIndex(KEY_SEASON_ID)));
                recoveredCash.setSeasonName(cursor.getString(cursor.getColumnIndex(KEY_SEASON_NAME)));

                recoveredCashes.add(recoveredCash);
            } while (cursor.moveToNext());
        }
        return recoveredCashes;
    }

    /**
     * Add farm history
     *
     * @param farmHistory
     */
    public void addFarmHistory(FarmHistory farmHistory) {

        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FARMER_ID, farmHistory.getFarmerId());
        values.put(KEY_SEASON_ID, farmHistory.getSeasonId());
        values.put(KEY_SEASON_NAME, farmHistory.getSeasonName());
        values.put(KEY_TOTAL_LAND_HOLDING_SIZE, farmHistory.getTotalLandHoldingSize());

        db.insert(TABLE_FARM_HISTORY, null, values);
    }

    /**
     * Get all farm histories
     *
     * @return
     */
    public List<FarmHistory> getAllFarmHistories() {
        List<FarmHistory> farmHistories = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_FARM_HISTORY;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                FarmHistory farmHistory = new FarmHistory();
                farmHistory.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                farmHistory.setFarmerId(cursor.getInt(cursor.getColumnIndex(KEY_FARMER_ID)));
                farmHistory.setTotalLandHoldingSize(cursor.getDouble(cursor.getColumnIndex(KEY_TOTAL_LAND_HOLDING_SIZE)));
                farmHistory.setSeasonId(cursor.getInt(cursor.getColumnIndex(KEY_SEASON_ID)));
                farmHistory.setSeasonName(cursor.getString(cursor.getColumnIndex(KEY_SEASON_NAME)));

                farmHistories.add(farmHistory);
            } while (cursor.moveToNext());
        }
        return farmHistories;
    }

    /**
     * Add farm history item
     *
     * @param farmHistoryItem
     */
    public void addFarmHistoryItem(FarmHistoryItem farmHistoryItem) {

        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FARM_HISTORY_ID, farmHistoryItem.getFarmHistoryId());
        values.put(KEY_CROP_ID, farmHistoryItem.getCropId());
        values.put(KEY_ACRES, farmHistoryItem.getAcres());
        values.put(KEY_WEIGHT, farmHistoryItem.getWeight());
        values.put(KEY_PROPERTY_OWNED, farmHistoryItem.getLandOwned() ? 1 : 0);

        db.insert(TABLE_FARM_HISTORY_ITEMS, null, values);
    }

    /**
     * Get all farm histories
     *
     * @return
     */
    public List<FarmHistoryItem> getAllFarmHistoryItems() {
        List<FarmHistoryItem> farmHistoryItems = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_FARM_HISTORY_ITEMS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                FarmHistoryItem farmHistoryItem = new FarmHistoryItem();
                farmHistoryItem.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                farmHistoryItem.setFarmHistoryId(cursor.getInt(cursor.getColumnIndex(KEY_FARM_HISTORY_ID)));
                farmHistoryItem.setCropId(cursor.getInt(cursor.getColumnIndex(KEY_CROP_ID)));
                farmHistoryItem.setAcres(cursor.getDouble(cursor.getColumnIndex(KEY_ACRES)));
                farmHistoryItem.setWeight(cursor.getDouble(cursor.getColumnIndex(KEY_WEIGHT)));
                farmHistoryItem.setLandOwned(cursor.getInt(cursor.getColumnIndex(KEY_PROPERTY_OWNED)) == 1);

                farmHistoryItems.add(farmHistoryItem);
            } while (cursor.moveToNext());
        }
        return farmHistoryItems;
    }


}
