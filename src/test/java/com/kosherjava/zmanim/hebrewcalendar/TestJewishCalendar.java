package com.kosherjava.zmanim.hebrewcalendar;

import org.junit.Test;

import static com.kosherjava.zmanim.hebrewcalendar.TestHelper.*;
import static org.junit.Assert.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Translated from python-zmanim
 */
public class TestJewishCalendar {
    public List<Integer> allYearTypes() {
        return Arrays.asList(
                standardMondayChaseirim(),
                standardMondayShelaimim(),
                standardTuesdayKesidran(),
                standardThursdayKesidran(),
                standardThursdayShelaimim(),
                standardShabbosChaseirim(),
                standardShabbosShelaimim(),
                leapMondayChaseirim(),
                leapMondayShelaimim(),
                leapTuesdayKesidran(),
                leapThursdayChaseirim(),
                leapThursdayShelaimim(),
                leapShabbosChaseirim(),
                leapShabbosShelaimim()
        );
    }

    public List<JewishCalendar> allRoshHashanas() {
        List<JewishCalendar> result = new ArrayList<>();
        for (Integer y : allYearTypes()) {
            result.add(new JewishCalendar(y, 7, 1));
        }
        return result;
    }

    public List<String> chanukahForChaseirim() {
        return Arrays.asList("9-25", "9-26", "9-27", "9-28", "9-29", "10-1", "10-2", "10-3");
    }

    public Map<Integer, List<String>> standardSignificantDays() {
        Map<Integer, List<String>> result = new HashMap<>();
        result.put(JewishCalendar.EREV_PESACH, Collections.singletonList("1-14"));
        result.put(JewishCalendar.PESACH, Arrays.asList("1-15", "1-16", "1-21", "1-22"));
        result.put(JewishCalendar.CHOL_HAMOED_PESACH, Arrays.asList("1-17", "1-18", "1-19", "1-20"));
        result.put(JewishCalendar.PESACH_SHENI, Collections.singletonList("2-14"));
        result.put(JewishCalendar.LAG_BAOMER, Collections.singletonList("2-18"));
        result.put(JewishCalendar.EREV_SHAVUOS, Collections.singletonList("3-5"));
        result.put(JewishCalendar.SHAVUOS, Arrays.asList("3-6", "3-7"));
        result.put(JewishCalendar.SEVENTEEN_OF_TAMMUZ, Collections.singletonList("4-17"));
        result.put(JewishCalendar.TISHA_BEAV, Collections.singletonList("5-9"));
        result.put(JewishCalendar.TU_BEAV, Collections.singletonList("5-15"));
        result.put(JewishCalendar.EREV_ROSH_HASHANA, Collections.singletonList("6-29"));
        result.put(JewishCalendar.ROSH_HASHANA, Arrays.asList("7-1", "7-2"));
        result.put(JewishCalendar.FAST_OF_GEDALYAH, Collections.singletonList("7-3"));
        result.put(JewishCalendar.EREV_YOM_KIPPUR, Collections.singletonList("7-9"));
        result.put(JewishCalendar.YOM_KIPPUR, Collections.singletonList("7-10"));
        result.put(JewishCalendar.EREV_SUCCOS, Collections.singletonList("7-14"));
        result.put(JewishCalendar.SUCCOS, Arrays.asList("7-15", "7-16"));
        result.put(JewishCalendar.CHOL_HAMOED_SUCCOS, Arrays.asList("7-17", "7-18", "7-19", "7-20"));
        result.put(JewishCalendar.HOSHANA_RABBA, Collections.singletonList("7-21"));
        result.put(JewishCalendar.SHEMINI_ATZERES, Collections.singletonList("7-22"));
        result.put(JewishCalendar.SIMCHAS_TORAH, Collections.singletonList("7-23"));
        result.put(JewishCalendar.CHANUKAH, Arrays.asList("9-25", "9-26", "9-27", "9-28", "9-29", "9-30", "10-1", "10-2"));
        result.put(JewishCalendar.TENTH_OF_TEVES, Collections.singletonList("10-10"));
        result.put(JewishCalendar.TU_BESHVAT, Collections.singletonList("11-15"));
        result.put(JewishCalendar.FAST_OF_ESTHER, Collections.singletonList("12-13"));
        result.put(JewishCalendar.PURIM, Collections.singletonList("12-14"));
        result.put(JewishCalendar.SHUSHAN_PURIM, Collections.singletonList("12-15"));
        return result;
    }

    public Map<Integer, List<String>> leapPurim() {
        Map<Integer, List<String>> result = new HashMap<>();
        result.put(JewishCalendar.PURIM_KATAN, Collections.singletonList("12-14"));
        result.put(JewishCalendar.SHUSHAN_PURIM_KATAN, Collections.singletonList("12-15"));
        result.put(JewishCalendar.FAST_OF_ESTHER, Collections.singletonList("13-13"));
        result.put(JewishCalendar.PURIM, Collections.singletonList("13-14"));
        result.put(JewishCalendar.SHUSHAN_PURIM, Collections.singletonList("13-15"));
        return result;
    }

    public Map<Integer, List<String>> leapSignificantDays() {
        Map<Integer, List<String>> result = new HashMap<>();
        result.putAll(standardSignificantDays());
        result.putAll(leapPurim());
        return result;
    }

    public Map<Integer, List<String>> israelStandardSignificantDays() {
        Map<Integer, List<String>> significantDays = new HashMap<>(standardSignificantDays());
        significantDays.put(JewishCalendar.PESACH, Arrays.asList("1-15", "1-21"));
        significantDays.put(JewishCalendar.CHOL_HAMOED_PESACH, Arrays.asList("1-16", "1-17", "1-18", "1-19", "1-20"));
        significantDays.put(JewishCalendar.SHAVUOS, Collections.singletonList("3-6"));
        significantDays.put(JewishCalendar.SUCCOS, Collections.singletonList("7-15"));
        significantDays.put(JewishCalendar.CHOL_HAMOED_SUCCOS, Arrays.asList("7-16", "7-17", "7-18", "7-19", "7-20"));
        significantDays.remove(JewishCalendar.SIMCHAS_TORAH);
        return significantDays;
    }

    public Map<Integer, List<String>> israelLeapSignificantDays() {
        Map<Integer, List<String>> result = new HashMap<>(israelStandardSignificantDays());
        result.putAll(leapPurim());
        return result;
    }

    public List<Integer> modernSignificantDays() {
        return Arrays.asList(JewishCalendar.YOM_HASHOAH, JewishCalendar.YOM_HAZIKARON, JewishCalendar.YOM_HAATZMAUT, JewishCalendar.YOM_YERUSHALAYIM);
    }

    @Test
    public void testAllLeapYearsAsExpected() {
        List<Boolean> result = new ArrayList<>();
        for (JewishCalendar c : allRoshHashanas()) {
            result.add(c.isJewishLeapYear());
        }
        List<Boolean> expected = Arrays.asList(false, false, false, false, false, false, false,
                true, true, true, true, true, true, true);
        assertEquals(expected, result);
    }

    @Test
    public void testAllDaysOfWeekAsExpected() {
        List<Integer> result = new ArrayList<>();
        for (JewishCalendar c : allRoshHashanas()) {
            result.add(c.getDayOfWeek());
        }
        List<Integer> expected = Arrays.asList(2, 2, 3, 5, 5, 7, 7, 2, 2, 3, 5, 5, 7, 7);
        assertEquals(expected, result);
    }

    @Test
    public void testAllCheshvanKislevKviahsAsExpected() {
        List<String> result = new ArrayList<>();
        for (JewishCalendar c : allRoshHashanas()) {
            int cheshvanKislevKviah = c.getCheshvanKislevKviah();
            result.add(
                    cheshvanKislevKviah == JewishDate.CHASERIM ?
                            "Chaseirim" :
                            cheshvanKislevKviah == JewishDate.KESIDRAN ?
                                    "Kesidran" :
                                    "Shelaimim"
            );
        }
        List<String> expected = Arrays.asList("Chaseirim", "Shelaimim", "Kesidran", "Kesidran",
                "Shelaimim", "Chaseirim", "Shelaimim", "Chaseirim",
                "Shelaimim", "Kesidran", "Chaseirim", "Shelaimim",
                "Chaseirim", "Shelaimim");
        assertEquals(expected, result);
    }

    @Test
    public void testSignificantDaysForStandardMondayChaseirim() {
        int year = standardMondayChaseirim();

        Map<Integer, List<String>> result = TestHelper.allDaysMatching(year, JewishCalendar::getYomTovIndex);
        result.remove(-1); //remove non-yomim-tovim
        Map<Integer, List<String>> expected = new HashMap<>(standardSignificantDays());
        expected.put(JewishCalendar.CHANUKAH, chanukahForChaseirim());
        expected.put(JewishCalendar.FAST_OF_ESTHER, Collections.singletonList("12-11"));
        assertEquals(expected, result);

        Map<Integer, List<String>> israelResult = TestHelper.allDaysMatching(year, JewishCalendar::getYomTovIndex, true);
        israelResult.remove(-1); //remove non-yomim-tovim
        Map<Integer, List<String>> israelExpected = new HashMap<>(israelStandardSignificantDays());
        israelExpected.put(JewishCalendar.CHANUKAH, chanukahForChaseirim());
        israelExpected.put(JewishCalendar.FAST_OF_ESTHER, Collections.singletonList("12-11"));
        assertEquals(israelExpected, israelResult);
    }

    @Test
    public void testSignificantDaysForStandardMondayShelaimim() {
        int year = standardMondayShelaimim();

        Map<Integer, List<String>> result = TestHelper.allDaysMatching(year, JewishCalendar::getYomTovIndex);
        result.remove(-1); //remove non-yomim-tovim

        Map<Integer, List<String>> expected = new HashMap<>(standardSignificantDays());
        assertEquals(expected, result);

        Map<Integer, List<String>> israelResult = TestHelper.allDaysMatching(year, JewishCalendar::getYomTovIndex, true);
        israelResult.remove(-1); //remove non-yomim-tovim

        Map<Integer, List<String>> israelExpected = new HashMap<>(israelStandardSignificantDays());
        assertEquals(israelExpected, israelResult);
    }

    @Test
    public void testSignificantDaysForStandardTuesdayKesidran() {
        int year = standardTuesdayKesidran();

        Map<Integer, List<String>> result = TestHelper.allDaysMatching(year, JewishCalendar::getYomTovIndex);
        result.remove(-1); //remove non-yomim-tovim
        Map<Integer, List<String>> expected = new HashMap<>(standardSignificantDays());
        assertEquals(expected, result);

        Map<Integer, List<String>> israelResult = TestHelper.allDaysMatching(year, JewishCalendar::getYomTovIndex, true);
        result.remove(-1); //remove non-yomim-tovim
        Map<Integer, List<String>> israelExpected = new HashMap<>(israelStandardSignificantDays());
        assertEquals(israelExpected, israelResult);
    }

    @Test
    public void testSignificantDaysForStandardThursdayKesidran() {
        int year = standardThursdayKesidran();

        Map<Integer, List<String>> result = TestHelper.allDaysMatching(year, JewishCalendar::getYomTovIndex);
        result.remove(-1); //remove non-yomim-tovim
        Map<Integer, List<String>> expected = new HashMap<>(standardSignificantDays());
        expected.put(JewishCalendar.FAST_OF_GEDALYAH, Collections.singletonList("7-4"));
        expected.put(JewishCalendar.SEVENTEEN_OF_TAMMUZ, Collections.singletonList("4-18"));
        expected.put(JewishCalendar.TISHA_BEAV, Collections.singletonList("5-10"));
        assertEquals(expected, result);

        Map<Integer, List<String>> israelResult = TestHelper.allDaysMatching(year, JewishCalendar::getYomTovIndex, true);
        israelResult.remove(-1); //remove non-yomim-tovim
        Map<Integer, List<String>> israelExpected = new HashMap<>(israelStandardSignificantDays());
        israelExpected.put(JewishCalendar.FAST_OF_GEDALYAH, Collections.singletonList("7-4"));
        israelExpected.put(JewishCalendar.SEVENTEEN_OF_TAMMUZ, Collections.singletonList("4-18"));
        israelExpected.put(JewishCalendar.TISHA_BEAV, Collections.singletonList("5-10"));
        assertEquals(israelExpected, israelResult);
    }

    @Test
    public void testSignificantDaysForStandardThursdayShelaimim() {
        int year = standardThursdayShelaimim();

        Map<Integer, List<String>> result = TestHelper.allDaysMatching(year, JewishCalendar::getYomTovIndex);
        result.remove(-1); //remove non-yomim-tovim
        Map<Integer, List<String>> expected = new HashMap<>(standardSignificantDays());
        expected.put(JewishCalendar.FAST_OF_GEDALYAH, Collections.singletonList("7-4"));
        assertEquals(expected, result);

        Map<Integer, List<String>> israelResult = TestHelper.allDaysMatching(year, JewishCalendar::getYomTovIndex, true);
        israelResult.remove(-1); //remove non-yomim-tovim
        Map<Integer, List<String>> israelExpected = new HashMap<>(israelStandardSignificantDays());
        israelExpected.put(JewishCalendar.FAST_OF_GEDALYAH, Collections.singletonList("7-4"));
        assertEquals(israelExpected, israelResult);
    }

    @Test
    public void testSignificantDaysForStandardShabbosChaseirim() {
        int year = standardShabbosChaseirim();

        Map<Integer, List<String>> result = TestHelper.allDaysMatching(year, JewishCalendar::getYomTovIndex);
        result.remove(-1); //remove non-yomim-tovim
        Map<Integer, List<String>> expected = new HashMap<>(standardSignificantDays());
        expected.put(JewishCalendar.CHANUKAH, chanukahForChaseirim());
        assertEquals(expected, result);

        Map<Integer, List<String>> israelResult = TestHelper.allDaysMatching(year, JewishCalendar::getYomTovIndex, true);
        israelResult.remove(-1); //remove non-yomim-tovim
        Map<Integer, List<String>> israelExpected = new HashMap<>(israelStandardSignificantDays());
        israelExpected.put(JewishCalendar.CHANUKAH, chanukahForChaseirim());
        assertEquals(israelExpected, israelResult);
    }

    @Test
    public void testSignificantDaysForStandardShabbosShelaimim() {
        int year = standardShabbosShelaimim();

        Map<Integer, List<String>> result = TestHelper.allDaysMatching(year, JewishCalendar::getYomTovIndex);
        result.remove(-1); //remove non-yomim-tovim
        Map<Integer, List<String>> expected = new HashMap<>(standardSignificantDays());
        expected.put(JewishCalendar.FAST_OF_ESTHER, Collections.singletonList("12-11"));
        assertEquals(expected, result);

        Map<Integer, List<String>> israelResult = TestHelper.allDaysMatching(year, JewishCalendar::getYomTovIndex, true);
        israelResult.remove(-1); //remove non-yomim-tovim
        Map<Integer, List<String>> israelExpected = new HashMap<>(israelStandardSignificantDays());
        israelExpected.put(JewishCalendar.FAST_OF_ESTHER, Collections.singletonList("12-11"));
        assertEquals(israelExpected, israelResult);
    }

    @Test
    public void testSignificantDaysForLeapMondayChaseirim() {
        int year = leapMondayChaseirim();

        Map<Integer, List<String>> result = TestHelper.allDaysMatching(year, JewishCalendar::getYomTovIndex);
        result.remove(-1); //remove non-yomim-tovim
        Map<Integer, List<String>> expected = new HashMap<>(leapSignificantDays());
        expected.put(JewishCalendar.CHANUKAH, chanukahForChaseirim());
        assertEquals(expected, result);

        Map<Integer, List<String>> israelResult = TestHelper.allDaysMatching(year, JewishCalendar::getYomTovIndex, true);
        israelResult.remove(-1); //remove non-yomim-tovim
        Map<Integer, List<String>> israelExpected = new HashMap<Integer, List<String>>(israelLeapSignificantDays());
        israelExpected.put(JewishCalendar.CHANUKAH, chanukahForChaseirim());
        assertEquals(israelExpected, israelResult);
    }

    @Test
    public void testSignificantDaysForLeapMondayShelaimim() {
        int year = leapMondayShelaimim();

        Map<Integer, List<String>> result = TestHelper.allDaysMatching(year, JewishCalendar::getYomTovIndex);
        result.remove(-1); //remove non-yomim-tovim
        Map<Integer, List<String>> expected = new HashMap<>(leapSignificantDays());
        expected.put(JewishCalendar.SEVENTEEN_OF_TAMMUZ, Collections.singletonList("4-18"));
        expected.put(JewishCalendar.TISHA_BEAV, Collections.singletonList("5-10"));
        assertEquals(expected, result);

        Map<Integer, List<String>> israelResult = TestHelper.allDaysMatching(year, JewishCalendar::getYomTovIndex, true);
        israelResult.remove(-1); //remove non-yomim-tovim
        Map<Integer, List<String>> israelExpected = new HashMap<>(israelLeapSignificantDays());
        israelExpected.put(JewishCalendar.SEVENTEEN_OF_TAMMUZ, Collections.singletonList("4-18"));
        israelExpected.put(JewishCalendar.TISHA_BEAV, Collections.singletonList("5-10"));
        assertEquals(israelExpected, israelResult);
    }

    @Test
    public void testSignificantDaysForLeapTuesdayKesidran() {
        int year = leapTuesdayKesidran();

        Map<Integer, List<String>> result = TestHelper.allDaysMatching(year, JewishCalendar::getYomTovIndex);
        result.remove(-1); //remove non-yomim-tovim
        Map<Integer, List<String>> expected = new HashMap<>(leapSignificantDays());
        expected.put(JewishCalendar.SEVENTEEN_OF_TAMMUZ, Collections.singletonList("4-18"));
        expected.put(JewishCalendar.TISHA_BEAV, Collections.singletonList("5-10"));
        assertEquals(expected, result);

        Map<Integer, List<String>> israelResult = TestHelper.allDaysMatching(year, JewishCalendar::getYomTovIndex, true);
        israelResult.remove(-1); //remove non-yomim-tovim
        Map<Integer, List<String>> israelExpected = new HashMap<>(israelLeapSignificantDays());
        israelExpected.put(JewishCalendar.SEVENTEEN_OF_TAMMUZ, Collections.singletonList("4-18"));
        israelExpected.put(JewishCalendar.TISHA_BEAV, Collections.singletonList("5-10"));
        assertEquals(israelExpected, israelResult);
    }

    @Test
    public void testSignificantDaysForLeapThursdayChaseirim() {
        int year = leapThursdayChaseirim();

        Map<Integer, List<String>> result = TestHelper.allDaysMatching(year, JewishCalendar::getYomTovIndex);
        result.remove(-1); //remove non-yomim-tovim
        Map<Integer, List<String>> expected = new HashMap<>(leapSignificantDays());
        expected.put(JewishCalendar.FAST_OF_GEDALYAH, Collections.singletonList("7-4"));
        expected.put(JewishCalendar.CHANUKAH, chanukahForChaseirim());
        assertEquals(expected, result);

        Map<Integer, List<String>> israelResult = TestHelper.allDaysMatching(year, JewishCalendar::getYomTovIndex, true);
        israelResult.remove(-1); //remove non-yomim-tovim
        Map<Integer, List<String>> israelExpected = new HashMap<>(israelLeapSignificantDays());
        israelExpected.put(JewishCalendar.FAST_OF_GEDALYAH, Collections.singletonList("7-4"));
        israelExpected.put(JewishCalendar.CHANUKAH, chanukahForChaseirim());
        assertEquals(israelExpected, israelResult);
    }

    @Test
    public void testSignificantDaysForLeapThursdayShelaimim() {
        int year = leapThursdayShelaimim();

        Map<Integer, List<String>> result = TestHelper.allDaysMatching(year, JewishCalendar::getYomTovIndex);
        result.remove(-1); //remove non-yomim-tovim
        Map<Integer, List<String>> expected = new HashMap<>(leapSignificantDays());
        expected.put(JewishCalendar.FAST_OF_GEDALYAH, Collections.singletonList("7-4"));
        expected.put(JewishCalendar.FAST_OF_ESTHER, Collections.singletonList("13-11"));
        assertEquals(expected, result);

        Map<Integer, List<String>> israelResult = TestHelper.allDaysMatching(year, JewishCalendar::getYomTovIndex, true);
        israelResult.remove(-1); //remove non-yomim-tovim
        Map<Integer, List<String>> israelExpected = new HashMap<>(israelLeapSignificantDays());
        israelExpected.put(JewishCalendar.FAST_OF_GEDALYAH, Collections.singletonList("7-4"));
        israelExpected.put(JewishCalendar.FAST_OF_ESTHER, Collections.singletonList("13-11"));
        assertEquals(israelExpected, israelResult);
    }

    @Test
    public void testSignificantDaysForLeapShabbosChaseirim() {
        int year = leapShabbosChaseirim();

        Map<Integer, List<String>> result = TestHelper.allDaysMatching(year, JewishCalendar::getYomTovIndex);
        result.remove(-1); //remove non-yomim-tovim
        Map<Integer, List<String>> expected = new HashMap<>(leapSignificantDays());
        expected.put(JewishCalendar.CHANUKAH, chanukahForChaseirim());
        expected.put(JewishCalendar.FAST_OF_ESTHER, Collections.singletonList("13-11"));
        assertEquals(expected, result);

        Map<Integer, List<String>> israelResult = TestHelper.allDaysMatching(year, JewishCalendar::getYomTovIndex, true);
        israelResult.remove(-1); //remove non-yomim-tovim
        Map<Integer, List<String>> israelExpected = new HashMap<>(israelLeapSignificantDays());
        israelExpected.put(JewishCalendar.CHANUKAH, chanukahForChaseirim());
        israelExpected.put(JewishCalendar.FAST_OF_ESTHER, Collections.singletonList("13-11"));
        assertEquals(israelExpected, israelResult);
    }

    @Test
    public void testSignificantDaysForLeapShabbosShelaimim() {
        int year = leapShabbosShelaimim();

        Map<Integer, List<String>> result = TestHelper.allDaysMatching(year, JewishCalendar::getYomTovIndex);
        result.remove(-1); //remove non-yomim-tovim
        Map<Integer, List<String>> expected = new HashMap<>(leapSignificantDays());
        assertEquals(expected, result);

        Map<Integer, List<String>> israelResult = TestHelper.allDaysMatching(year, JewishCalendar::getYomTovIndex, true);
        israelResult.remove(-1); //remove non-yomim-tovim
        Map<Integer, List<String>> israelExpected = new HashMap<>(israelLeapSignificantDays());
        assertEquals(israelExpected, israelResult);
    }

    @Test
    public void testModernHolidaysForNissanStartingSunday() {
        int year = standardThursdayShelaimim();

        assertEquals(1, new JewishCalendar(year, 1, 1).getDayOfWeek());
        Map<Integer, List<String>> allDays = allDaysMatching(year, JewishCalendar::getYomTovIndex, true, true);
        allDays.remove(-1); //remove non-yomim-tovim
        Map<Integer, List<String>> expected = new HashMap<>(standardSignificantDays());
        expected.put(JewishCalendar.CHANUKAH, chanukahForChaseirim());
        expected.put(JewishCalendar.FAST_OF_ESTHER, Collections.singletonList("12-11"));
        expected.put(JewishCalendar.YOM_HASHOAH, Collections.singletonList("1-26"));
        expected.put(JewishCalendar.YOM_HAZIKARON, Collections.singletonList("2-2"));
        expected.put(JewishCalendar.YOM_HAATZMAUT, Collections.singletonList("2-3"));
        expected.put(JewishCalendar.YOM_YERUSHALAYIM, Collections.singletonList("2-28"));
        Map<Integer, List<String>> result = specificDaysMatching(allDays, new ArrayList<>(expected.keySet()));
        assertEquals(expected, result);
    }

    @Test
    public void testModernHolidaysForNissanStartingTuesday() {
        int year = standardMondayChaseirim();

        assertEquals(3, new JewishCalendar(year, 1, 1).getDayOfWeek());
        Map<Object, List<String>> allDays = TestHelper.allDaysMatching(year, JewishCalendar::getYomTovIndex, true, true);
        allDays.remove(-1); //remove non-yomim-tovim
        Map<Integer, List<String>> expected = new HashMap<>(standardSignificantDays());
        expected.put(JewishCalendar.CHANUKAH, chanukahForChaseirim());
        expected.put(JewishCalendar.FAST_OF_ESTHER, Collections.singletonList("12-11"));
        expected.put(JewishCalendar.YOM_HASHOAH, Collections.singletonList("1-28"));
        expected.put(JewishCalendar.YOM_HAZIKARON, Collections.singletonList("2-5"));
        expected.put(JewishCalendar.YOM_HAATZMAUT, Collections.singletonList("2-6"));
        expected.put(JewishCalendar.YOM_YERUSHALAYIM, Collections.singletonList("2-28"));
        Map<Object, List<String>> result = specificDaysMatching(allDays, new ArrayList<>(expected.keySet()));
        assertEquals(expected, result);
    }

    @Test
    public void testModernHolidaysForNissanStartingThursday() {
        int year = standardMondayShelaimim();

        assertEquals(5, new JewishCalendar(year, 1, 1).getDayOfWeek());
        Map<Object, List<String>> allDays = TestHelper.allDaysMatching(year, JewishCalendar::getYomTovIndex, true, true);
        allDays.remove(-1); //remove non-yomim-tovim
        Map<Integer, List<String>> expected = new HashMap<>(standardSignificantDays());
        expected.put(JewishCalendar.CHANUKAH, chanukahForChaseirim());
        expected.put(JewishCalendar.FAST_OF_ESTHER, Collections.singletonList("12-11"));
        expected.put(JewishCalendar.YOM_HASHOAH, Collections.singletonList("1-27"));
        expected.put(JewishCalendar.YOM_HAZIKARON, Collections.singletonList("2-4"));
        expected.put(JewishCalendar.YOM_HAATZMAUT, Collections.singletonList("2-5"));
        expected.put(JewishCalendar.YOM_YERUSHALAYIM, Collections.singletonList("2-28"));
        Map<Object, List<String>> result = specificDaysMatching(allDays, new ArrayList<>(expected.keySet()));
        assertEquals(expected, result);
    }

    @Test
    public void testModernHolidaysForNissanStartingShabbos() {
        int year = standardThursdayKesidran();

        assertEquals(7, new JewishCalendar(year, 1, 1).getDayOfWeek());
        Map<Object, List<String>> allDays = TestHelper.allDaysMatching(year, JewishCalendar::getYomTovIndex, true, true);
        allDays.remove(-1); //remove non-yomim-tovim
        Map<Integer, List<String>> expected = new HashMap<>(standardSignificantDays());
        expected.put(JewishCalendar.CHANUKAH, chanukahForChaseirim());
        expected.put(JewishCalendar.FAST_OF_ESTHER, Collections.singletonList("12-11"));
        expected.put(JewishCalendar.YOM_HASHOAH, Collections.singletonList("1-27"));
        expected.put(JewishCalendar.YOM_HAZIKARON, Collections.singletonList("2-3"));
        expected.put(JewishCalendar.YOM_HAATZMAUT, Collections.singletonList("2-4"));
        expected.put(JewishCalendar.YOM_YERUSHALAYIM, Collections.singletonList("2-28"));
        Map<Object, List<String>> result = specificDaysMatching(allDays, new ArrayList<>(expected.keySet()));
        assertEquals(expected, result);
    }

    @Test
    public void testIsYomTovOutsideIsrael() {
        int year = leapShabbosShelaimim();

        Map<Object, List<String>> allDays = TestHelper.allDaysMatching(year, JewishCalendar::isYomTov);
//        List<String> allDaysList = allDays.values().stream().flatMap(List::stream).collect(Collectors.toList());
        List<String> expected = Arrays.asList(
                "7-1", "7-2", "7-10", "7-15", "7-16",
                "7-17", "7-18", "7-19", "7-20", "7-21", "7-22", "7-23",
                "9-25", "9-26", "9-27", "9-28", "9-29", "9-30", "10-1", "10-2",
                "11-15", "12-14", "12-15", "13-14", "13-15",
                "1-15", "1-16", "1-17", "1-18", "1-19", "1-20", "1-21", "1-22",
                "2-14", "2-18", "3-6", "3-7", "5-15"
        );
        List<String> allDaysList = allDays.get(Boolean.TRUE);
        expected.sort(String.CASE_INSENSITIVE_ORDER);
        allDaysList.sort(String.CASE_INSENSITIVE_ORDER);
        assertEquals(expected, allDaysList);
    }

    @Test
    public void testIsYomTovInIsrael() {
        int year = leapShabbosShelaimim();

        Map<Object, List<String>> allDays = TestHelper.allDaysMatching(year, JewishCalendar::isYomTov, true);
        List<String> allDaysList = allDays.get(Boolean.TRUE);
        List<String> expected = Arrays.asList(
                "7-1", "7-2", "7-10", "7-15", "7-16",
                "7-17", "7-18", "7-19", "7-20", "7-21", "7-22",
                "9-25", "9-26", "9-27", "9-28", "9-29", "9-30", "10-1", "10-2",
                "11-15", "12-14", "12-15", "13-14", "13-15",
                "1-15", "1-16", "1-17", "1-18", "1-19", "1-20", "1-21",
                "2-14", "2-18", "3-6", "5-15"
        );
        expected.sort(String.CASE_INSENSITIVE_ORDER);
        allDaysList.sort(String.CASE_INSENSITIVE_ORDER);
        assertEquals(expected, allDaysList);
    }

    @Test
    public void testIsYomTovWithModernHolidays() {
        int year = leapShabbosShelaimim();

        Map<Object, List<String>> allDays = TestHelper.allDaysMatching(year, JewishCalendar::isYomTov, true, true);
//        List<String> allDaysList = allDays.values().stream().flatMap(List::stream).collect(Collectors.toList());
        List<String> allDaysList = allDays.get(Boolean.TRUE);
        List<String> expected = Arrays.asList(
                "7-1", "7-2", "7-10", "7-15", "7-16", "7-22", "7-23",
                "1-15", "1-16", "1-21", "1-22", "3-6", "3-7"
        );
        expected.sort(String.CASE_INSENSITIVE_ORDER);
        allDaysList.sort(String.CASE_INSENSITIVE_ORDER);
        assertEquals(expected, allDaysList);
    }

    @Test
    public void testIsYomTovAssurBemelachaOutsideIsrael() {
        int year = leapShabbosShelaimim();

        Map<Object, List<String>> allDays = TestHelper.allDaysMatching(year, JewishCalendar::isYomTovAssurBemelacha);
        List<String> allDaysList = allDays.get(Boolean.TRUE);
        List<String> expected = Arrays.asList(
                "7-1", "7-2", "7-10", "7-15", "7-16", "7-22", "7-23",
                "1-15", "1-16", "1-21", "1-22", "3-6", "3-7"
        );
        expected.sort(String.CASE_INSENSITIVE_ORDER);
        allDaysList.sort(String.CASE_INSENSITIVE_ORDER);
        assertEquals(expected, allDaysList);
    }

    @Test
    public void testIsYomTovAssurBemelachaInIsrael() {
        int year = leapShabbosShelaimim();

        Map<Object, List<String>> allDays = TestHelper.allDaysMatching(year, JewishCalendar::isYomTovAssurBemelacha, true);
        List<String> allDaysList = allDays.get(Boolean.TRUE);
        List<String> expected = Arrays.asList(
                "7-1", "7-2", "7-10", "7-15", "7-22", "1-15", "1-21", "3-6"
        );
        expected.sort(String.CASE_INSENSITIVE_ORDER);
        allDaysList.sort(String.CASE_INSENSITIVE_ORDER);
        assertEquals(expected, allDaysList);
    }

    @Test
    public void testIsYomTovAssurBemelachaWithModernHolidays() {
        int year = leapShabbosShelaimim();

        Map<Object, List<String>> allDays = TestHelper.allDaysMatching(year, JewishCalendar::isYomTovAssurBemelacha, true, true);
        List<String> allDaysList = allDays.get(Boolean.TRUE);
        assertFalse(allDaysList.contains("1-27"));
        assertFalse(allDaysList.contains("2-4"));
        assertFalse(allDaysList.contains("2-5"));
        assertFalse(allDaysList.contains("2-28"));
    }

    @Test
    public void testIsAssurBemelachaOutsideIsrael() {
        int year = leapShabbosShelaimim();

        Map<Object, List<String>> allDays = TestHelper.allDaysMatching(year, JewishCalendar::isAssurBemelacha);
        List<String> allDaysList = allDays.get(Boolean.TRUE);
        List<String> expectedYomTov = Arrays.asList(
                "7-1", "7-2", "7-10", "7-15", "7-16", "7-22", "7-23",
                "1-15", "1-16", "1-21", "1-22", "3-6", "3-7"
        );
        List<String> expectedShabbosos = TestHelper.allDaysMatching(year, c -> c.getDayOfWeek() == 7).get(Boolean.TRUE);
        List<String> expected = expectedYomTov.stream().distinct().collect(Collectors.toList());
        expected.addAll(expectedShabbosos.stream().distinct().collect(Collectors.toList()));
        expected.sort(String.CASE_INSENSITIVE_ORDER);
        allDaysList.sort(String.CASE_INSENSITIVE_ORDER);
        assertEquals(expected, allDaysList);
    }


    @Test
    public void testIsTomorrowAssurBemelachaInIsrael() {
        int year = leapShabbosShelaimim();

        Map<Object, List<String>> allDays = TestHelper.allDaysMatching(year, JewishCalendar::isTomorrowShabbosOrYomTov, true);
        List<String> flattenedDays = allDays.get(Boolean.TRUE);

        List<String> expectedErevYomTov = Arrays.asList("7-9", "7-14", "7-21", "1-14", "1-20", "3-5", "6-29");
        List<String> expectedErevYomTovSheni = Collections.singletonList("7-1");

        Map<Object, List<String>> expectedErevShabbos = TestHelper.allDaysMatching(year, c -> c.getDayOfWeek() == 6);
        List<String> flattenedErevShabbos = expectedErevShabbos.get(Boolean.TRUE);

        List<String> expected = new ArrayList<>(expectedErevYomTov);
        expected.addAll(expectedErevYomTovSheni);
        expected.addAll(flattenedErevShabbos);

        Collections.sort(flattenedDays);
        Collections.sort(expected);

        assertEquals(flattenedDays, expected);
    }

    @Test
    public void testHasDelayedCandleLightingForNonCandleLightingDay() {
        String date = "2018-09-13";
        JewishCalendar subject = new JewishCalendar(parseDate(date));
        assertFalse(subject.hasDelayedCandleLighting());
    }

    @Test
    public void testHasDelayedCandleLightingForStandardErevShabbos() {
        String date = "2018-09-14";
        JewishCalendar subject = new JewishCalendar(parseDate(date));
        assertFalse(subject.hasDelayedCandleLighting());
    }

    @Test
    public void testHasDelayedCandleLightingForStandardErevYomTov() {
        String date = "2018-09-30";
        JewishCalendar subject = new JewishCalendar(parseDate(date));
        assertFalse(subject.hasDelayedCandleLighting());
    }

    @Test
    public void testHasDelayedCandleLightingForStandardFirstDayYomTov() {
        String date = "2018-10-01";
        JewishCalendar subject = new JewishCalendar(parseDate(date));
        assertTrue(subject.hasDelayedCandleLighting());
    }

    @Test
    public void testHasDelayedCandleLightingForYomTovErevShabbos() {
        String date = "2019-04-26";
        JewishCalendar subject = new JewishCalendar(parseDate(date));
        assertFalse(subject.hasDelayedCandleLighting());
    }

    @Test
    public void testHasDelayedCandleLightingForShabbosFollowedByYomTov() {
        String date = "2019-06-08";
        JewishCalendar subject = new JewishCalendar(parseDate(date));
        assertTrue(subject.hasDelayedCandleLighting());
    }

    @Test
    public void testIsYomTovSheniOutsideIsrael() {
        int year = TestHelper.leapShabbosShelaimim();

        Map<Object, List<String>> allDays = TestHelper.allDaysMatching(year, c -> c.isYomTovSheni());
        List<String> flattenedDays = allDays.get(Boolean.TRUE);

        List<String> expectedDays = Arrays.asList("7-2", "7-16", "7-23", "1-16", "1-22", "3-7");

        assertEquals(flattenedDays, expectedDays);
    }

    @Test
    public void testIsYomTovSheniInIsrael() {
        int year = TestHelper.leapShabbosShelaimim();

        Map<Object, List<String>> allDays = TestHelper.allDaysMatching(year, c -> c.isYomTovSheni(), true);
        List<String> flattenedDays = allDays.get(Boolean.TRUE);


        List<String> expectedDays = Collections.singletonList("7-2");

        assertEquals(flattenedDays, expectedDays);
    }

    @Test
    public void testIsErevYomTovSheniOutsideIsrael() {
        int year = TestHelper.leapShabbosShelaimim();

        Map<Object, List<String>> allDays = TestHelper.allDaysMatching(year, JewishCalendar::isErevYomTovSheni);
        List<String> flattenedDays = allDays.get(Boolean.TRUE);


        List<String> expectedDays = Arrays.asList("7-1", "7-15", "7-22", "1-15", "1-21", "3-6");

        assertEquals(flattenedDays, expectedDays);
    }

    @Test
    public void testIsErevYomTovSheniInIsrael() {
        int year = TestHelper.leapShabbosShelaimim();

        Map<Object, List<String>> allDays = TestHelper.allDaysMatching(year, JewishCalendar::isErevYomTovSheni, true);
        List<String> flattenedDays = allDays.get(Boolean.TRUE);


        List<String> expectedDays = Collections.singletonList("7-1");

        assertEquals(flattenedDays, expectedDays);
    }

    @Test
    public void testIsCholHamoedOutsideIsrael() {
        int year = TestHelper.leapShabbosShelaimim();

        Map<Object, List<String>> allDays = TestHelper.allDaysMatching(year, JewishCalendar::isCholHamoed);
        List<String> flattenedDays = allDays.get(Boolean.TRUE);


        List<String> expected = Arrays.asList("7-17", "7-18", "7-19", "7-20", "7-21", "1-17", "1-18", "1-19", "1-20");

        assertEquals(flattenedDays, expected);
    }

    @Test
    public void testIsCholHamoedInIsrael() {
        int year = TestHelper.leapShabbosShelaimim();

        Map<Object, List<String>> allDays = TestHelper.allDaysMatching(year, JewishCalendar::isCholHamoed, true);
        List<String> flattenedDays = allDays.get(Boolean.TRUE);


        List<String> expected = Arrays.asList("7-16", "7-17", "7-18", "7-19", "7-20", "7-21", "1-16", "1-17", "1-18", "1-19", "1-20");

        assertEquals(flattenedDays, expected);
    }

    @Test
    public void testIsErevYomTovOutsideIsrael() {
        int year = TestHelper.leapShabbosShelaimim();

        Map<Object, List<String>> allDays = TestHelper.allDaysMatching(year, JewishCalendar::isErevYomTov);
        List<String> flattenedDays = allDays.get(Boolean.TRUE);


        List<String> expected = Arrays.asList("7-9", "7-14", "7-21", "1-14", "1-20", "3-5", "6-29");

        assertEquals(flattenedDays, expected);
    }

    @Test
    public void testIsErevYomTovInIsrael() {
        int year = TestHelper.leapShabbosShelaimim();

        Map<Object, List<String>> allDays = TestHelper.allDaysMatching(year, JewishCalendar::isErevYomTov, true);
        List<String> flattenedDays = allDays.get(Boolean.TRUE);


        List<String> expected = Arrays.asList("7-9", "7-14", "7-21", "1-14", "1-20", "3-5", "6-29");

        assertEquals(flattenedDays, expected);
    }

    @Test
    public void testIsTaanis() {
        int year = TestHelper.leapShabbosShelaimim();

        Map<Object, List<String>> allDays = TestHelper.allDaysMatching(year, JewishCalendar::isTaanis);
        List<String> flattenedDays = allDays.get(Boolean.TRUE);


        List<String> expected = Arrays.asList("7-3", "7-10", "10-10", "13-13", "4-17", "5-9");

        assertEquals(flattenedDays, expected);
    }

    @Test
    public void testIsTaanisBechorim() {
        int year = TestHelper.leapShabbosShelaimim();

        Map<Object, List<String>> allDays = TestHelper.allDaysMatching(year, c -> c.isTaanisBechoros());
        List<String> flattenedDays = allDays.get(Boolean.TRUE);


        List<String> expected = Collections.singletonList("1-14");

        assertEquals(flattenedDays, expected);
    }

    @Test
    public void testIsTaanisBechorimForNonStandardYear() {
        int year = TestHelper.standardShabbosChaseirim();

        Map<Object, List<String>> allDays = TestHelper.allDaysMatching(year, JewishCalendar::isTaanisBechoros);
        List<String> flattenedDays = allDays.get(Boolean.TRUE);


        List<String> expected = Collections.singletonList("1-12");

        assertEquals(flattenedDays, expected);
    }

    @Test
    public void testIsShabbosMevorchim() {
        int year = TestHelper.leapMondayShelaimim();

        Map<Object, List<String>> allDays = TestHelper.allDaysMatching(year, JewishCalendar::isShabbosMevorchim);
        List<String> flattenedDays = allDays.get(Boolean.TRUE);


        List<String> expected = Arrays.asList("7-27", "8-25", "9-23", "10-28", "11-27", "12-25", "13-23", "1-29", "2-27", "3-26", "4-24", "5-23");

        assertEquals(flattenedDays, expected);
    }

    @Test
    public void testIsRoshChodesh() {
        int year = TestHelper.leapShabbosShelaimim();

        Map<Object, List<String>> allDays = TestHelper.allDaysMatching(year, JewishCalendar::isRoshChodesh);
        List<String> flattenedDays = allDays.get(Boolean.TRUE);


        List<String> expected = Arrays.asList("7-30", "8-1", "8-30", "9-1", "9-30", "10-1", "11-1", "11-30", "12-1", "12-30", "13-1", "1-1", "1-30", "2-1", "3-1", "3-30", "4-1", "5-1", "5-30", "6-1");

        assertEquals(flattenedDays, expected);
    }

    @Test
    public void testIsErevRoshChodesh() {
        int year = TestHelper.leapShabbosShelaimim();

        Map<Object, List<String>> allDays = TestHelper.allDaysMatching(year, JewishCalendar::isErevRoshChodesh);
        List<String> flattenedDays = allDays.get(Boolean.TRUE);


        List<String> expected = Arrays.asList("7-29", "8-29", "9-29", "10-29", "11-29", "12-29", "13-29", "1-29", "2-29", "3-29", "4-29", "5-29");

        assertEquals(flattenedDays, expected);
    }

    @Test
    public void testIsChanukahForChaseirimYears() {
        int year = TestHelper.standardMondayChaseirim();

        Map<Object, List<String>> allDays = TestHelper.allDaysMatching(year, JewishCalendar::isChanukah);
        List<String> flattenedDays = allDays.get(Boolean.TRUE);


        List<String> expected = chanukahForChaseirim();

        assertEquals(flattenedDays, expected);
    }

    @Test
    public void testIsChanukahForNonChaseirimYears() {
        int year = TestHelper.standardTuesdayKesidran();

        Map<Object, List<String>> allDays = TestHelper.allDaysMatching(year, JewishCalendar::isChanukah);
        List<String> flattenedDays = allDays.get(Boolean.TRUE);


        List<String> expected = standardSignificantDays().get(JewishCalendar.CHANUKAH);

        assertEquals(flattenedDays, expected);
    }

    @Test
    public void testDayOfChanukahForChaseirimYears() {
        int year = TestHelper.standardMondayChaseirim();

        List<String> expectedDays = chanukahForChaseirim();
        List<Integer> result = new ArrayList<>();
        for (String date : expectedDays) {
            String[] parts = date.split("-");
            result.add(new JewishCalendar(year, Integer.parseInt(parts[0]), Integer.parseInt(parts[1])).getDayOfChanukah());
        }

        assertEquals(result, IntStream.rangeClosed(1, 8).boxed().collect(Collectors.toList()));
    }

    @Test
    public void testDayOfChanukahForNonChaseirimYears() {
        int year = TestHelper.standardTuesdayKesidran();

        List<String> expectedDays = standardSignificantDays().get(JewishCalendar.CHANUKAH);
        List<Integer> result = new ArrayList<>();
        for (String date : expectedDays) {
            String[] parts = date.split("-");
            result.add(new JewishCalendar(year, Integer.parseInt(parts[0]), Integer.parseInt(parts[1])).getDayOfChanukah());
        }

        assertEquals(result, IntStream.rangeClosed(1, 8).boxed().collect(Collectors.toList()));
    }

    @Test
    public void testDayOfChanukahForNonChanukahDate() {
        JewishCalendar calendar = new JewishCalendar(TestHelper.standardMondayChaseirim(), 7, 1);
        assertEquals(-1, calendar.getDayOfChanukah());
    }

    @Test
    public void testDayOfOmer() {
        JewishCalendar calendar = new JewishCalendar(TestHelper.standardMondayChaseirim(), 1, 16);
        List<Integer> foundDays = new ArrayList<>();

        for (int n = 1; n < 50; n++) {
            foundDays.add(calendar.getDayOfOmer());
            calendar.forward(Calendar.DATE, 1);
        }

        assertEquals(foundDays, IntStream.rangeClosed(1, 49).boxed().collect(Collectors.toList()));
    }

    @Test
    public void testDayOfOmerOutsideOfOmer() {
        JewishCalendar calendar = new JewishCalendar(TestHelper.standardMondayChaseirim(), 7, 1);
        assertEquals(-1, calendar.getDayOfOmer());
    }

    @Test
    public void testSignificantShabbosForStandardMondayChaseirim() {
        int year = TestHelper.standardMondayChaseirim();

        Map<Object, List<String>> result = TestHelper.allDaysMatching(year, JewishCalendar::getSpecialShabbos);
        Map<Object, List<String>> expected = new HashMap<>();
        expected.put(JewishCalendar.Parsha.SHUVA, Collections.singletonList("7-6"));
        expected.put(JewishCalendar.Parsha.SHKALIM, Collections.singletonList("11-29"));
        expected.put(JewishCalendar.Parsha.ZACHOR, Collections.singletonList("12-13"));
        expected.put(JewishCalendar.Parsha.PARA, Collections.singletonList("12-20"));
        expected.put(JewishCalendar.Parsha.HACHODESH, Collections.singletonList("12-27"));
        expected.put(JewishCalendar.Parsha.HAGADOL, Collections.singletonList("1-12"));

        assertEquals(result, expected);
    }

    @Test
    public void testSignificantShabbosForStandardMondayShelaimim() {
        int year = TestHelper.standardMondayShelaimim();

        Map<Object, List<String>> result = TestHelper.allDaysMatching(year, JewishCalendar::getSpecialShabbos);
        Map<Object, List<String>> expected = new HashMap<>();
        expected.put(JewishCalendar.Parsha.SHUVA, Collections.singletonList("7-6"));
        expected.put(JewishCalendar.Parsha.SHKALIM, Collections.singletonList("11-27"));
        expected.put(JewishCalendar.Parsha.ZACHOR, Collections.singletonList("12-11"));
        expected.put(JewishCalendar.Parsha.PARA, Collections.singletonList("12-18"));
        expected.put(JewishCalendar.Parsha.HACHODESH, Collections.singletonList("12-25"));
        expected.put(JewishCalendar.Parsha.HAGADOL, Collections.singletonList("1-10"));

        assertEquals(result, expected);
    }

    @Test
    public void testSignificantShabbosForStandardTuesdayKesidran() {
        int year = TestHelper.standardTuesdayKesidran();

        Map<Object, List<String>> result = TestHelper.allDaysMatching(year, JewishCalendar::getSpecialShabbos);
        Map<Object, List<String>> expected = new HashMap<>();
        expected.put(JewishCalendar.Parsha.SHUVA, Collections.singletonList("7-5"));
        expected.put(JewishCalendar.Parsha.SHKALIM, Collections.singletonList("11-27"));
        expected.put(JewishCalendar.Parsha.ZACHOR, Collections.singletonList("12-11"));
        expected.put(JewishCalendar.Parsha.PARA, Collections.singletonList("12-18"));
        expected.put(JewishCalendar.Parsha.HACHODESH, Collections.singletonList("12-25"));
        expected.put(JewishCalendar.Parsha.HAGADOL, Collections.singletonList("1-10"));

        assertEquals(result, expected);
    }

    @Test
    public void testSignificantShabbosForStandardThursdayKesidran() {
        int year = TestHelper.standardThursdayKesidran();

        Map<Object, List<String>> result = TestHelper.allDaysMatching(year, JewishCalendar::getSpecialShabbos);
        Map<Object, List<String>> expected = new HashMap<>();
        expected.put(JewishCalendar.Parsha.SHUVA, Collections.singletonList("7-3"));
        expected.put(JewishCalendar.Parsha.SHKALIM, Collections.singletonList("11-25"));
        expected.put(JewishCalendar.Parsha.ZACHOR, Collections.singletonList("12-9"));
        expected.put(JewishCalendar.Parsha.PARA, Collections.singletonList("12-23"));
        expected.put(JewishCalendar.Parsha.HACHODESH, Collections.singletonList("1-1"));
        expected.put(JewishCalendar.Parsha.HAGADOL, Collections.singletonList("1-8"));

        assertEquals(result, expected);
    }

    @Test
    public void testSignificantShabbosForStandardThursdayShelaimim() {
        int year = TestHelper.standardThursdayShelaimim();

        Map<Object, List<String>> result = TestHelper.allDaysMatching(year, JewishCalendar::getSpecialShabbos);
        Map<Object, List<String>> expected = new HashMap<>();
        expected.put(JewishCalendar.Parsha.SHUVA, Collections.singletonList("7-3"));
        expected.put(JewishCalendar.Parsha.SHKALIM, Collections.singletonList("12-1"));
        expected.put(JewishCalendar.Parsha.ZACHOR, Collections.singletonList("12-8"));
        expected.put(JewishCalendar.Parsha.PARA, Collections.singletonList("12-22"));
        expected.put(JewishCalendar.Parsha.HACHODESH, Collections.singletonList("12-29"));
        expected.put(JewishCalendar.Parsha.HAGADOL, Collections.singletonList("1-5"));

        assertEquals(result, expected);
    }

    private Date parseDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        ZonedDateTime dateTime = ZonedDateTime.parse(dateString + "T00:00:00Z");
        return Date.from(dateTime.toInstant());
    }
}