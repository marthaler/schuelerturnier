package com.googlecode.mad_schuelerturnier.web.converter;

import org.springframework.binding.convert.converters.StringToDate;
import org.springframework.binding.convert.service.DefaultConversionService;

public class ApplicationConversionService extends DefaultConversionService {

    @SuppressWarnings("deprecation")
    @Override
    protected void addDefaultConverters() {
        super.addDefaultConverters();
        addDefaultAliases();
        StringToDate dateConverter = new StringToDate();
        dateConverter.setPattern("yyyy-MM-dd");
        this.addConverter(dateConverter);
        this.addConverter("customConverter", new StringToDateTime());
    }
}

