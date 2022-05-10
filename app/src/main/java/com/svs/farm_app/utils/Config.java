package com.svs.farm_app.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by benson wamae on 12/27/2014.`567890-=
 */
public class Config {


//    final static String IP = "http://95.216.48.56/";//new

   final static String IP = "http://95.217.63.201/";

    public final static String API_KEY = "YfY7ZufLMff5c4VtE9CH82NfwBBtTB8h";

    final static String BASE_URL = IP + "farm_app/";
    public final static String API_URL = BASE_URL + "index.php/api/";

    public static final String SD_CARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static final String NEW_FINGERPRINTS_PATH = Environment.getExternalStorageDirectory()
            + File.separator + "fingerprint_image";

    public static final String WHICH_THUMB = "which_thumb";
    public static final String LEFT_THUMB = "left_thumb";
    public static final String RIGHT_THUMB = "right_thumb";

    public static final String NEW_LEFT_THUMB = "new_left_thumb";
    public static final String NEW_RIGHT_THUMB = "new_right_thumb";

    public static final String FINGERPRINTS_PATH = SD_CARD_PATH + "/farmer_fingerprints/";
    /*Uploads*/
    public static final String UPLOAD_PLANTING_RAINS = API_URL + "planting_rains";

    public static final String UPLOAD_FARM_OTHER_CROPS = API_URL + "farm_other_crops";

    public static final String UPLOAD_GERMINATIONS = API_URL + "germination";

    public static final String UPLOAD_MOLASSES_TRAP_CATCHES = API_URL + "molasses_trap_catches";

    public static final String UPLOAD_FARM_INCOME = API_URL + "farm_income";

    public static final String UPLOAD_FARM_PRODUCTION = API_URL + "farm_production";

    public static final String UPLOAD_TRANS_HSE_TO_MARKET = API_URL + "trans_hse_market";

    public static final String UPLOAD_YIELD_ESTIMATE = API_URL + "yield_estimate";

    public static final String SURVEY_RESPONSE = API_URL + "save_survey";

    public static final String UPLOAD_SCOUTING = API_URL + "scouting";

    public static final String FINGERPRINT_UPLOAD_URL = API_URL + "fingerprint";

    public static String SURVEY_QUESTIONS = API_URL + "surveys_questions";
    public static String FARMER_UPLOAD_URL = API_URL + "farmer_register";
    public static String FARMER_UPDATE_URL = API_URL + "update_farmer_details";

    public static String FARMER_UPDATE = API_URL + "farmer_update";

    public static String UPLOAD_MAPPED_FARMS = API_URL + "mapped_farms";

    public static String ASSIGN_TRAININGS_UPLOAD_URL = API_URL + "assign_training";

    public static String FARMER_TIME_UPLOAD_URL = API_URL + "farmer_times";

    public static String UPLOAD_FINGER_ONE_FORM = API_URL + "finger_one";

    public static String UPLOAD_FINGER_TWO_FORM = API_URL + "finger_two";

    public static String UPLOAD_FINGER_THREE_FORM = API_URL + "finger_three";

    public static String UPLOAD_FINGER_FOUR_FORM = API_URL + "finger_four";

    public static String UPLOAD_FINGER_FIVE_FORM = API_URL + "finger_five";

    public static String UPLOAD_SIGNATURES = API_URL + "signatures";

    public static String UPLOAD_SHOWS_INTENT = API_URL + "show_intent";

    public static String UPLOAD_COLLECTED_INPUTS = API_URL + "collect_inputs";

    public static String UPLOAD_FARM_ASS_FORM_MAJOR = API_URL + "farm_ass_major";

    public static String UPLOAD_FARM_ASS_FORM_MEDIUM = API_URL + "farm_ass_medium";
    public static String UPLOAD_PRODUCT_PURCHASE = API_URL + "purchase_product";

    /*Downloads*/
    public static final String DOWNLOAD_PESTICIDES = API_URL + "all_pesticides";

    public static final String DOWNLOAD_TRANSPORT_MODES = API_URL + "transport_modes";

    public static final String DOWNLOAD_FARMER_PICS = API_URL + "farmer_pics";

    public static final String DOWNLOAD_FINGERPRINTS = API_URL + "farmer_fingerprints";

    public static String DOWNLOAD_LIBRARY = API_URL + "all_pdfs";

    public static String TRAIN_MATS = BASE_URL + "assets/uploads/train_library";
    public static String DOWNLOAD_REGIONS = API_URL + "all_regions/format/json";
    public static String DOWNLOAD_DISTRICTS = API_URL + "all_district/format/json";
    public static String FARM_DOWNLOAD_WARDS = API_URL + "all_wards/format/json";
    public static String DOWNLOAD_VILLAGES = API_URL + "all_villages/format/json";
    public static String DOWNLOAD_SUB_VILLAGES = API_URL + "all_subvillages/format/json";
    public static String DOWNLOAD_OFFICER_TRAININGS = API_URL + "all_exts_training";
    public static String DOWNLOAD_TRAINING_CATEGORIES = API_URL + "all_train_cats";
    public static String DOWNLOAD_TRAINING_TYPES = API_URL + "all_train_types";
    public static String DOWNLOAD_COMPANIES_URL = API_URL + "all_companies";
    public static String DOWNLOAD_USERS_URL = API_URL + "all_extension_officers";
    public static String DOWNLOAD_REGISTERED_FARMERS = API_URL + "all_registered_farmers";
    public static String WACK_FARMERS_LIST = API_URL + "all_wack_farmers";

    public static String DOWNLOAD_FARM_LIST = API_URL + "farms";
    public static String CONNECTION_CHECK = API_URL + "check_connection";
    public static String UPDATE_FARM_AREA = API_URL + "update_farm_area";
    public static String DOWNLOAD_ASSIGNED_INPUTS = API_URL + "assigned_inputs";
    public static String DOWNLOAD_HERBICIDES = API_URL + "all_herbicides";
    public static String DOWNLOAD_FOLIAR_FEED = API_URL + "all_foliar_feeds";
    public static String DOWNLOAD_OTHER_CROPS = API_URL + "all_other_crops";
    public static String DOWNLOAD_USER_VILLAGES = API_URL + "all_user_villages";
    public static String DOWNLOAD_DOWNLOAD_COUNTS = API_URL + "download_counts";
    public static String DOWNLOAD_COTTON_DEDUCTIONS = API_URL + "cotton_deductions";
    public static String DOWNLOAD_COLLECTED_ORDERS = API_URL + "collected_orders";
    public static String DOWNLOAD_RECOVERED_CASH = API_URL + "recovered_cash";
    public static String DOWNLOAD_COTTON_PRICES = API_URL + "product_grades";

    public static String RE_REGISTER_URL = API_URL + "re_register";
    public static String DATA_COLLECTED_URL = API_URL + "data_collected";

    public static String INPUT_COLLECTION_ERROR_URL = API_URL + "input_collection_error";

    /*Supported Devices*/
    public static final String TPS350 = "ALPSTPS350A";
    public static final String COREWISE_V0 = "COREWISE_V0";
    public static final String FP05 = "FP05";
    public static final String TPS900 = "UNKNOWNTPS900";

    /*Recapturing of farmers details*/
    public static final String NEW_FARMER_PICTURE = "new_farmer_picture";

    /*Miscellaneous constants*/
    public static final String TO_ACTIVITY = "to_activity";
    public static final String SIGN_ACTIVITY = "sign_activity";

    public static final String FINGERPRINT_CAPTURE = "fingerprint_capture";
    public static final String FINGERPRINT_VERIFICATION = "fingerprint_verification";

    public static final String RECAPTURE_FARMER_DETAILS = "recapture_farmer_details";

    public static final String MANUAL_INPUT_COLLECTION = "manual_input_collection";

    public static String COTTON_DEDUCTIONS = "cotton_deductions";
    public static String COLLECTED_ORDERS = "collected_orders";
    public static final String RECOVERED_CASH = "collected_cash";
    public static final String PRODUCT_GRADES = "product_grades";

    /*Farmer ID length i.e. GEN_ID*/
    public static final int FARMER_ID_LENGTH = 16;
    public static final int CARD_NO_LENGTH = 8;

    /*Inputs collection methods types*/
    public static final String MANUAL = "manual";
    public static final String FINGERPRINT = "fingerprint";
    /*Image directories on device*/
    public static String IMAGE_DIRECTORY_NAME = "pic-uploads";
    public static String IMAGE_DIRECTORY_NAME2 = "pic-uploads-re-reg";
}
