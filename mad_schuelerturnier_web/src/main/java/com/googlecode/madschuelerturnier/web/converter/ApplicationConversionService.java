package com.googlecode.madschuelerturnier.web.converter;

import org.springframework.binding.convert.converters.StringToDate;
import org.springframework.binding.convert.service.DefaultConversionService;

@Deprecated
public class ApplicationConversionService extends DefaultConversionService {

    @SuppressWarnings("deprecation")
    @Override

    // todo pruefen ob noch noetig

    protected void addDefaultConverters() {
        super.addDefaultConverters();
        addDefaultAliases();
        StringToDate dateConverter = new StringToDate();
        dateConverter.setPattern("yyyy-MM-dd");
        this.addConverter(dateConverter);
        this.addConverter("customConverter", new StringToDateTime());
    }
}

