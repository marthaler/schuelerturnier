package com.googlecode.mad_schuelerturnier.web.converter;

import org.springframework.binding.convert.ConversionExecutionException;
import org.springframework.binding.convert.converters.StringToObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringToDateTime extends StringToObject {

    public StringToDateTime() {
        super(Date.class);
    }

    @Override
    protected Object toObject(String string, Class targetClass) {
        try {
            SimpleDateFormat fm = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
            return fm.parse(string);
        } catch (ParseException ex) {
            throw new ConversionExecutionException(string, String.class, Date.class, "fehler beim konvertieren");
        }
    }

    @Override
    protected String toString(Object object) {
        Date d = (Date) object;
        SimpleDateFormat fm = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        return fm.format(object);
    }
}