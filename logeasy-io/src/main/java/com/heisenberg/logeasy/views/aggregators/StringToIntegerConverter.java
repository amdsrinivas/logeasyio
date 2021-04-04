package com.heisenberg.logeasy.views.aggregators;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class StringToIntegerConverter implements Converter<String, Integer> {

    @Override
    public Result<Integer> convertToModel(String s, ValueContext valueContext) {
        return Result.ok(Integer.parseInt(s));
    }

    @Override
    public String convertToPresentation(Integer integer, ValueContext valueContext) {
        return integer.toString();
    }
}
