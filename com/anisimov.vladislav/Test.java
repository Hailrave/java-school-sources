package com.anisimov.vladislav;

import com.digdes.school.DatesToCronConvertException;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) throws DatesToCronConvertException {
        DateConverter dateConverter = new DateConverter();
        List<String> listDates = new ArrayList<>();
        listDates.add("2022-01-25T08:00:55");
        listDates.add("2022-01-25T08:00:56");
        listDates.add("2022-01-25T09:00:57");
        listDates.add("2022-01-25T09:00:58");
        listDates.add("2022-01-26T08:00:59");
        listDates.add("2022-01-26T08:01:00");
        System.out.println(dateConverter.convert(listDates));
        System.out.println(dateConverter.getImplementationInfo());
    }
}
