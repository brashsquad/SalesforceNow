package com.blntsoft.salesforcenow.util;

import android.util.Log;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by arnaud on 11/20/13.
 */
public class StringUtils {

    public static String formatPrice(int price, String currencySymbol) {
        DecimalFormat df = new DecimalFormat("#,###,##0.00");
        return currencySymbol + df.format(price);
    }

    public static String formatDate(String dateStr, String inputFormat, String outputFormat) {

        try {
            DateFormat inFormat = new SimpleDateFormat(inputFormat);
            Date date = inFormat.parse(dateStr);

            DateFormat outformat = new SimpleDateFormat(outputFormat);
            return outformat.format(date);
        } catch (ParseException e) {
            Log.e("StringUtils", null, e);
        }
        return null;
    }
}
