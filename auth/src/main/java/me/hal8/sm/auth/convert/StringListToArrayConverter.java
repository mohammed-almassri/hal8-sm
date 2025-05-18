package me.hal8.sm.auth.convert;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Converter
public class StringListToArrayConverter implements AttributeConverter<List<String>, String[]> {
    @Override
    public String[] convertToDatabaseColumn(List<String> list) {
        return list != null ? list.toArray(new String[0]) : null;
    }

    @Override
    public List<String> convertToEntityAttribute(String[] array) {
        return array != null ? Arrays.asList(array) : new ArrayList<>();
    }
}
