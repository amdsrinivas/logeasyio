package com.heisenberg.logeasy.views.logs;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public class StringToStringListConverter implements Converter<String, List<String>> {
    @Override
    public Result<List<String>> convertToModel(String s, ValueContext valueContext) {
        return Result.ok(Arrays.asList(s.split(","))) ;
    }

    @Override
    public String convertToPresentation(List<String> strings, ValueContext valueContext) {
        return StringUtils.join(strings, ",");
    }
}
