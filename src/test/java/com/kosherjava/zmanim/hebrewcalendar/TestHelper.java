package com.kosherjava.zmanim.hebrewcalendar;

import com.kosherjava.zmanim.util.GeoLocation;

import java.util.*;
import java.util.function.Function;

public class TestHelper {

    public static GeoLocation lakewood() {
        return new GeoLocation("Lakewood, NJ", 40.0721087, -74.2400243, 15, TimeZone.getTimeZone("America/New_York"));
    }

    public static GeoLocation samoa() {
        return new GeoLocation("Apia, Samoa", -13.8599098, -171.8031745, 1858, TimeZone.getTimeZone("Pacific/Apia"));
    }

    public static GeoLocation jerusalem() {
        return new GeoLocation("Jerusalem, Israel", 31.7781161, 35.233804, 740, TimeZone.getTimeZone("Asia/Jerusalem"));
    }

    public static GeoLocation losAngeles() {
        return new GeoLocation("Los Angeles, CA", 34.0201613, -118.6919095, 71, TimeZone.getTimeZone("America/Los_Angeles"));
    }

    public static GeoLocation tokyo() {
        return new GeoLocation("Tokyo, Japan", 35.6733227, 139.6403486, 40, TimeZone.getTimeZone("Asia/Tokyo"));
    }

    public static GeoLocation arcticNunavut() {
        return new GeoLocation("Fort Conger, NU Canada", 81.7449398, -64.7945858, 127, TimeZone.getTimeZone("America/Toronto"));
    }

    public static List<GeoLocation> basicLocations() {
        List<GeoLocation> locations = new ArrayList<>();
        locations.add(lakewood());
        locations.add(jerusalem());
        locations.add(losAngeles());
        locations.add(tokyo());
        locations.add(arcticNunavut());
        locations.add(samoa());
        return locations;
    }

    public static GeoLocation hooperBay() {
        return new GeoLocation("Hooper Bay, Alaska", 61.520182, -166.1740437, 8, TimeZone.getTimeZone("America/Anchorage"));
    }

    public static GeoLocation daneborg() {
        return new GeoLocation("Daneborg, Greenland", 74.2999996, -20.2420877, 0, TimeZone.getTimeZone("America/Godthab"));
    }

    public static int standardMondayChaseirim() {
        return 5777;
    }

    public static int standardMondayShelaimim() {
        return 5759;
    }

    public static int standardTuesdayKesidran() {
        return 5762;
    }

    public static int standardThursdayKesidran() {
        return 5778;
    }

    public static int standardThursdayShelaimim() {
        return 5754;
    }

    public static int standardShabbosChaseirim() {
        return 5781;
    }

    public static int standardShabbosShelaimim() {
        return 5770;
    }

    public static int leapMondayChaseirim() {
        return 5749;
    }

    public static int leapMondayShelaimim() {
        return 5776;
    }

    public static int leapTuesdayKesidran() {
        return 5755;
    }

    public static int leapThursdayChaseirim() {
        return 5765;
    }

    public static int leapThursdayShelaimim() {
        return 5774;
    }

    public static int leapShabbosChaseirim() {
        return 5757;
    }

    public static int leapShabbosShelaimim() {
        return 5763;
    }

    public static <T> Map<T, List<String>> allDaysMatching(int year, Function<JewishCalendar, T> matcher) {
        return allDaysMatching(year, matcher, false, false);
    }
    public static <T> Map<T, List<String>> allDaysMatching(int year, Function<JewishCalendar, T> matcher, boolean inIsrael) {
        return allDaysMatching(year, matcher, inIsrael, false);
    }

    public static <T> Map<T, List<String>> allDaysMatching(int year, Function<JewishCalendar, T> matcher, boolean inIsrael, boolean useModernHolidays) {
        JewishCalendar calendar = new JewishCalendar(year, 7, 1);
        calendar.setInIsrael(inIsrael);
        calendar.setUseModernHolidays(useModernHolidays);
        Map<T, List<String>> collection = new HashMap<>();
        while (calendar.getJewishYear() == year) {
            T sd = matcher.apply(calendar);
//            System.out.println("SD: " + sd);
            if (
                    (sd instanceof Integer && ((Integer) sd) >= 0) ||
                    (sd instanceof Boolean && (Boolean) sd) ||
                            sd != null
            ) {
//                System.out.println("Entered if for value " + sd);
                if (!collection.containsKey(sd)) {
                    collection.put(sd, new ArrayList<>());
                }
                List<String> strings = collection.get(sd);
//                System.out.println("Previous values: " + strings);
                String e = calendar.getJewishMonth() + "-" + calendar.getJewishDayOfMonth();
//                System.out.println("Adding new value: " + e);
                strings.add(e);
            }
            calendar.forward(Calendar.DATE, 1);
        }
        return collection;
    }

    public static <T, R> Map<T, List<R>> specificDaysMatching(Map<T, List<R>> collection, List<T> days) {
        Map<T, List<R>> specificDays = new HashMap<>();
        for (T day : days) {
            if (collection.containsKey(day)) {
                specificDays.put(day, collection.get(day));
            }
        }
        return specificDays;
    }

    public static void main(String[] args) {

    }
}
