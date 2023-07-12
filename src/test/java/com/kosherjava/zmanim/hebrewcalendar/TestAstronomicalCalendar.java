package com.kosherjava.zmanim.hebrewcalendar;

import com.kosherjava.zmanim.AstronomicalCalendar;
import com.kosherjava.zmanim.util.GeoLocation;
import org.junit.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

/**
 * Translated from python-zmanim
 * */
public class TestAstronomicalCalendar {

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
    static int HOUR_IN_MILLIS = 60 * 60 * 1000;

    @Test
    public void testSunrise() {
        List<String> expectedDates = Arrays.asList(
                "2017-10-17T07:09:11-04:00",
                "2017-10-17T06:39:32+03:00",
                "2017-10-17T07:00:25-07:00",
                "2017-10-17T05:48:20+09:00",
                null,
                "2017-10-17T06:54:18+14:00"
        );
        LocalDate date = LocalDate.of(2017, 10, 17);
        assertMatches(expectedDates, date, AstronomicalCalendar::getSunrise);
    }

    @Test
    public void testSunset() {
        List<String> expectedDates = Arrays.asList(
                "2017-10-17T18:14:38-04:00",
                "2017-10-17T18:08:46+03:00",
                "2017-10-17T18:19:05-07:00",
                "2017-10-17T17:04:46+09:00",
                null,
                "2017-10-17T19:31:07+14:00"
        );
        LocalDate date = LocalDate.of(2017, 10, 17);
        assertMatches(expectedDates, date, AstronomicalCalendar::getSunset);
    }

    @Test
    public void testSeaLevelSunrise() {
        List<String> expectedDates = Arrays.asList(
                "2017-10-17T07:09:51-04:00",
                "2017-10-17T06:43:43+03:00",
                "2017-10-17T07:01:45-07:00",
                "2017-10-17T05:49:21+09:00",
                null,
                "2017-10-17T07:00:05+14:00"
        );
        LocalDate date = LocalDate.of(2017, 10, 17);
        assertMatches(expectedDates, date, AstronomicalCalendar::getSeaLevelSunrise);
    }

    @Test
    public void testSeaLevelSunset() {
        List<String> expectedDates = Arrays.asList(
                "2017-10-17T18:13:58-04:00",
                "2017-10-17T18:04:36+03:00",
                "2017-10-17T18:17:45-07:00",
                "2017-10-17T17:03:45+09:00",
                null,
                "2017-10-17T19:25:19+14:00"
        );
        LocalDate date = LocalDate.of(2017, 10, 17);
        assertMatches(expectedDates, date, AstronomicalCalendar::getSeaLevelSunset);
    }

    @Test
    public void testUtcSunrise() {
        List<Double> expectedTimes = Arrays.asList(
                11.15327065,
                3.65893934,
                14.00708152,
                20.8057012,
                null,
                16.90510688
        );
        LocalDate date = LocalDate.of(2017, 10, 17);
        assertMatchesGeneric(expectedTimes, date, (calc, geo) -> getNullOrRound(calc.getUTCSunrise(90.0)));
    }

    @Test
    public void testUtcSunset() {
        List<Double> expectedTimes = Arrays.asList(
                22.24410903,
                15.14635336,
                1.31819979,
                8.07962871,
                null,
                5.51873532
        );
        LocalDate date = LocalDate.of(2017, 10, 17);
        assertMatchesGeneric(expectedTimes, date, (calc, geo) -> getNullOrRound(calc.getUTCSunset(90.0)));
    }

    @Test
    public void testUtcSeaLevelSunrise() {
        List<Double> expectedTimes = Arrays.asList(
                11.16434723,
                3.72862262,
                14.02926518,
                20.82268461,
                null,
                17.00158411
        );
        LocalDate date = LocalDate.of(2017, 10, 17);
        assertMatchesGeneric(expectedTimes, date, (calc, geo) -> getNullOrRound(calc.getUTCSeaLevelSunrise(90.0)));
    }

    @Test
    public void testUtcSeaLevelSunset() {
        List<Double> expectedTimes = Arrays.asList(
                22.23304301,
                15.07671429,
                1.29603174,
                8.06265871,
                null,
                5.42214918
        );
        LocalDate date = LocalDate.of(2017, 10, 17);

        assertMatchesGeneric(expectedTimes, date, (calc, geo) -> getNullOrRound(calc.getUTCSeaLevelSunset(90.0)));
    }

    @Test
    public void testSunriseOffsetByDegreesForBasicLocations() {
        List<String> expectedDates = Arrays.asList(
                "2017-10-17T06:10:57-04:00",
                "2017-10-17T05:50:43+03:00",
                "2017-10-17T06:07:22-07:00",
                "2017-10-17T04:53:55+09:00",
                "2017-10-17T04:47:28-04:00",
                "2017-10-17T06:13:13+14:00"
        );
        LocalDate date = LocalDate.of(2017, 10, 17);
        assertMatches(expectedDates, date, calc -> calc.getSunriseOffsetByDegrees(102));
    }

    @Test
    public void testSunriseOffsetByDegreesForArcticTimezoneExtremities() {
        assertMatches(
                Collections.singletonList(TestHelper.daneborg()),
                Collections.singletonList("2017-04-19T23:54:23-02:00"),
                LocalDate.of(2017, 4, 20),
                calc -> calc.getSunriseOffsetByDegrees(94)
        );
    }

    @Test
    public void testSunsetOffsetByDegreesForBasicLocations() {
        List<String> expectedDates = Arrays.asList(
                "2017-10-17T19:12:49-04:00",
                "2017-10-17T18:57:33+03:00",
                "2017-10-17T19:12:05-07:00",
                "2017-10-17T17:59:08+09:00",
                "2017-10-17T19:15:04-04:00",
                "2017-10-17T20:12:15+14:00"
        );
        LocalDate date = LocalDate.of(2017, 10, 17);
        assertMatches(expectedDates, date, calc -> calc.getSunsetOffsetByDegrees(102));
    }

    @Test
    public void testSunsetOffsetByDegreesForArcticTimezoneExtremities() {
        assertMatches(
                Collections.singletonList(TestHelper.hooperBay()),
                Collections.singletonList("2017-06-22T02:00:16-08:00"),
                LocalDate.of(2017, 6, 21),
                calc -> calc.getSunsetOffsetByDegrees(94)
        );
    }

    @Test
    public void testTemporalHour() {
        List<Double> expectedLengths = Stream.of(
                        0.92239132,
                        0.94567431,
                        0.93889721,
                        0.93666451,
                        null,
                        1.03504709
                )
                .map(value -> value == null ? null : roundTo8DecimalPlaces(value * HOUR_IN_MILLIS))
                .collect(Collectors.toList());
        LocalDate date = LocalDate.of(2017, 10, 17);
        assertMatchesGeneric(expectedLengths, date, (calc, geo) -> {
            long temporalHour = calc.getTemporalHour();
            return temporalHour == Long.MIN_VALUE ? null : roundTo8DecimalPlaces(temporalHour);
        });
    }

    @Test
    public void testSunTransit() {
        List<String> expectedDates = Arrays.asList(
                "2017-10-17T12:41:55-04:00",
                "2017-10-17T12:24:09+03:00",
                "2017-10-17T12:39:45-07:00",
                "2017-10-17T11:26:33+09:00",
                null,
                "2017-10-17T13:12:42+14:00"
        );
        LocalDate date = LocalDate.of(2017, 10, 17);
        assertMatches(expectedDates, date, AstronomicalCalendar::getSunTransit);
    }

    //Helpers:


    public static double roundTo8DecimalPlaces(double number) {
        if (Double.isNaN(number)) return number;
        double factor = Math.pow(10, 8);
        return Math.round(number * factor) / factor;
    }

    //@Nullable
    private Double getNullOrRound(double value) {
        return value == 0.0 || Double.isNaN(value) ? null : roundTo8DecimalPlaces(value);
    }

    private static <T> void assertMatchesGeneric(
            List<T> expected,
            LocalDate date,
            BiFunction<AstronomicalCalendar, GeoLocation, ? extends T> performCalc
    ) {

        List<GeoLocation> locations = TestHelper.basicLocations();
        assertMatchesGeneric(locations, expected, date, performCalc);
    }

    private static <T> void assertMatchesGeneric(List<GeoLocation> locations, List<T> expected, LocalDate date, BiFunction<AstronomicalCalendar, GeoLocation, ? extends T> performCalc) {
        AstronomicalCalendar calc = new AstronomicalCalendar();

        for (int i = 0; i < locations.size(); i++) {
            GeoLocation geo = locations.get(i);

            // Set the calendar to the specified date and time zone
            Calendar instance = Calendar.getInstance(geo.getTimeZone());
            instance.set(Calendar.YEAR, date.getYear());
            instance.set(Calendar.MONTH, date.getMonthValue() - 1); // Month is 0-based
            instance.set(Calendar.DAY_OF_MONTH, date.getDayOfMonth());

            calc.setCalendar(instance);
            calc.setGeoLocation(geo);
            T result = performCalc.apply(calc, geo);

            T expectedResult = expected.get(i);
            /*if(result instanceof Double) assertEquals((Double) expectedResult, (Double) result, 1);*/
            /*else*/ assertEquals(expectedResult, result);
        }
    }

    private static void assertMatches(
            List<String> expectedDates,
            LocalDate date,
            Function<AstronomicalCalendar, Date> performCalc
    ) {
        assertMatchesGeneric(
                expectedDates,
                date,
                (calc, geo) -> getFormattedString(date, performCalc, calc, geo)
        );
    }

    private static void assertMatches(
            List<GeoLocation> locations,
            List<String> expectedDates,
            LocalDate date,
            Function<AstronomicalCalendar, Date> performCalc
    ) {
        assertMatchesGeneric(
                locations,
                expectedDates,
                date,
                (calc, geo) -> getFormattedString(date, performCalc, calc, geo)
        );
    }

    private static String getFormattedString(LocalDate date, Function<AstronomicalCalendar, Date> performCalc, AstronomicalCalendar calc, GeoLocation geo) {
        Date result = performCalc.apply(calc);


        // Format the result with the proper offset and compare with the expected result
        return result == null ? null :
                ZonedDateTime
                        .ofInstant(
                                result.toInstant(),
                                ZoneId.ofOffset(
                                        "",
                                        date
                                                .atStartOfDay()
                                                .atZone(geo.getTimeZone().toZoneId())
                                                .getOffset()
                                )
                        )
                        .format(formatter);
    }
}
