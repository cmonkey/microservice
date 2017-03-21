package com.microservice.converter;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.locale.BaseLocaleConverter;
import org.apache.commons.beanutils.locale.converters.DateLocaleConverter;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by cmonkey on 17-3-21.
 */
public class DateConverter extends BaseLocaleConverter {
    private static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String DATETIME_ZONE_PATTERN = "yyyy-MM-dd HH:mm:ss.Z";

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    private static final String MONTH_PATTERN = "yyyy-MM";

    protected DateConverter(Locale locale, String pattern) {
        super(locale, pattern);
    }

    @Override
    protected Object parse(Object value, String pattern) throws ParseException {
        boolean ismatch = pattern.matches(DATETIME_PATTERN);
        if(ismatch){

            DateTime dt = new DateTime(value);

            return dt.toDate();
        }

        return value;
    }
}
