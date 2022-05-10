package com.svs.farm_app.utils;

/**
 * Created by ADMIN on 14-Sep-17.
 */

public class CardUtils {

    public static String cleanCardNo(String dirtyCardNo) {
        return dirtyCardNo.replace("X", "").trim();
    }

    public static String hyphenate(String cardNo) {
        String token1 = cardNo.substring(0, 4);
        String token2 = cardNo.substring(4, 8);
//        String token3 = cardNo.substring(8, 12);

        return token1 + "-" + token2 ;
    }
}
