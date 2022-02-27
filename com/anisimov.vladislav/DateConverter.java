package com.anisimov.vladislav;

import com.digdes.school.DatesToCronConvertException;
import com.digdes.school.DatesToCronConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class DateConverter implements DatesToCronConverter {

    @Override
    public String convert(List<String> dates) throws DatesToCronConvertException {
        ArrayList<Date> parsedDates = parseDates(dates); //преобразует list строк с датами в arraylist объектов даты
        ArrayList<Integer> seconds = new ArrayList<>();
        ArrayList<Integer> minutes = new ArrayList<>();
        ArrayList<Integer> hours = new ArrayList<>();
        for (Date date: parsedDates) {
            seconds.add(date.getSeconds());
            minutes.add(date.getMinutes());
            hours.add(date.getHours());
        }
        String cronSeconds = defineSeconds(seconds);
        String cronMinutes = defineHours(minutes);
        String cronHours = defineHours(hours);
        String cronData = defineData(parsedDates);
        return cronSeconds + " " + cronMinutes + " " + cronHours + " " +
                cronData;
    }

    private ArrayList<Date> parseDates(List<String> dates) {
        ArrayList<Date> parsedDates = new ArrayList<>();
        for (String date: dates) {
            try {
                parsedDates.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(date));
            } catch (ParseException e) {
                System.out.println("Неудачный парсинг строки с датой");
                e.printStackTrace();
            }
        }
        return parsedDates;
    }

    private String defineSeconds(ArrayList<Integer> seconds) throws DatesToCronConvertException {
        String cronSecond = null;
        boolean zeroDiffFlag = true;                                //флаг - значения не меняются
        boolean singleDiffFlag = true;                              //флаг - значения меняются постоянно на один
        boolean equalDiffFlag = true;
        int difference = seconds.get(1) - seconds.get(0);
        for (int i = 2; i < seconds.size(); i++) {
            int tempDiff = seconds.get(i) - seconds.get(i-1);

            if (tempDiff < 0)                                       //ситуация, когда секунды обнулились, начался новый счет
                tempDiff = seconds.get(i) + 60 - seconds.get(i-1);
            if (tempDiff != 0)                                      //значения изменяются
                zeroDiffFlag = false;
            //значения изменются с разной постоянной разницей
            if (tempDiff == difference) {
                if (tempDiff != 1 && tempDiff != 0) singleDiffFlag = false;
            }
            else {
                equalDiffFlag = false;
            }

            difference = tempDiff;
        }

        if (zeroDiffFlag) cronSecond = String.valueOf(seconds.get(0));
        else {
            if (!equalDiffFlag) throw new DatesToCronConvertException();
            if (singleDiffFlag) cronSecond = "*";
            else {
                cronSecond = seconds.get(0) + "/" + difference;
            }
        }
        return cronSecond;
    }

    private String defineHours(ArrayList<Integer> hours) throws DatesToCronConvertException {
        String cronHours = null;
        ArrayList<Integer> hoursWithoutDubl = (ArrayList<Integer>) hours.stream().distinct().collect(Collectors.toList());
        if (hoursWithoutDubl.size() > 1) {
            if (hoursWithoutDubl.size() < hours.size()) {
                boolean lessOneDiff = true;
                int diff = hoursWithoutDubl.get(1) - hoursWithoutDubl.get(0);
                for (int i = 2; i < hoursWithoutDubl.size(); i++) {
                    if (diff > 1) lessOneDiff = false;
                    diff = hoursWithoutDubl.get(i) - hoursWithoutDubl.get(i - 1);
                }
                if (lessOneDiff) {
                    cronHours = Collections.min(hoursWithoutDubl) + "-" + Collections.max(hoursWithoutDubl);
                }

            }
        }
        else {
            cronHours = defineSeconds(hours);
        }
        return cronHours;
    }

    private String defineData(ArrayList<Date> parsedDates) {
        String cronDayMonth = null;
        String cronMonth = null;
        String cronDayWeek = null;
        boolean equalDayMonth = true;
        boolean equalMonth = true;
        boolean equalDayWeek = true;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parsedDates.get(0));
        int dayMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int dayWeek = calendar.get(Calendar.DAY_OF_WEEK);
        for (Date date: parsedDates) {
            calendar.setTime(date);
            if (dayMonth != calendar.get(Calendar.DAY_OF_MONTH)) {
                equalDayMonth = false;
                dayMonth = calendar.get(Calendar.DAY_OF_MONTH);
            }
            if (month != calendar.get(Calendar.MONTH)) {
                equalMonth = false;

            }
            if (dayWeek != calendar.get(Calendar.DAY_OF_WEEK)) {
                equalDayWeek = false;
                dayWeek = calendar.get(Calendar.DAY_OF_WEEK);
            }
        }
        if (equalDayMonth) {
            cronDayMonth = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        }
        else {
            cronDayMonth = "?";
        }

        if (equalMonth) {
            cronMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        }
        else {
            cronMonth = "*";
        }

        if (equalDayWeek) {
            DateFormat formatter =new SimpleDateFormat("MMM", Locale.US);
            cronDayWeek = formatter.format(parsedDates.get(0)).toUpperCase();
        }
        else {
            cronDayWeek = "?";
        }

        return cronDayMonth + " " + cronMonth + " " + cronDayWeek;
    }

    @Override
    public String getImplementationInfo() {
        return new StringBuilder()
                .append("Anisimov Vladislav Jurevich, ")
                .append("DateConverter, ")
                .append("com.anisimov.vladislav, ")
                .append("https://github.com/Hailrave")
                .toString();
    }
}

