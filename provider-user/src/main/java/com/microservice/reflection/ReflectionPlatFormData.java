package com.microservice.reflection;

import com.microservice.entity.PlatFormData;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * Created by cmonkey on 17-3-28.
 */
public class ReflectionPlatFormData {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionPlatFormData.class);

    private final static DateTime defaultDt = new DateTime().withTime(0,0,0,000);
    private final static DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-hh HH:mm:ss");

    public static void main(String[] args){

        PlatFormData data = new PlatFormData();

        data.setId(10);
        data.setRegCount(20);
        data.setCreateTime(defaultDt.toDate());

        reflectionClass(data);
    }

    private static void reflectionClass(Object object){

        Class<?> clazz = object.getClass();

        Field[] fields = clazz.getDeclaredFields();

        for (Field field: fields) {

            String fieldName = field.getName();

            try {
                String getterMethodName = "get" + StringUtils.capitalize(fieldName);

                Method method = clazz.getDeclaredMethod(getterMethodName, new Class<?>[]{});
                method.setAccessible(true);

                Class<?> returnType = method.getReturnType();

                Object result = method.invoke(object, new Object[]{});

                if(null != result){

                    String v = String.valueOf(result);

                    if(returnType.equals(Date.class)){

                        Date date = (Date)result;
                        DateTime dateTime = new DateTime(date.getTime());

                        v = dateTime.toString(fmt);
                    }

                    logger.info("method = {}, fieldName = {}, v = {}", method.getName(), fieldName, v);
                }

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

    }
}
