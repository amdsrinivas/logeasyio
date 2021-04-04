package com.heisenberg.logeasy.views.logs;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StringToIntegerStringsConverter implements Converter<String, List<Integer>> {
    @Override
    public Result<List<Integer>> convertToModel(String s, ValueContext valueContext) {
        return Result.ok(
                Arrays.asList(s.split(",")).stream().
                        map(Integer::parseInt).collect(Collectors.toList())
        );
    }

    @Override
    public String convertToPresentation(List<Integer> integers, ValueContext valueContext) {
        return StringUtils.join(integers.stream().map(String::valueOf).collect(Collectors.toList()), ",");
    }
}
