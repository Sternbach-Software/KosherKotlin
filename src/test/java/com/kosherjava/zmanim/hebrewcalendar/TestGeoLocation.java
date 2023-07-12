package com.kosherjava.zmanim.hebrewcalendar;

import com.kosherjava.zmanim.util.GeoLocation;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.*;
import java.util.TimeZone;

public class TestGeoLocation {
    private static final long MINUTE_MILLIS = 60 * 1000;
    private static final long HOUR_MILLIS = MINUTE_MILLIS * 60;

    public static GeoLocation GMT() {
        return new GeoLocation("Greenwich, England", 51.4772, 0, TimeZone.getTimeZone("GMT"));
    }
    public float timeZoneOffsetAt(TimeZone timeZone, LocalDateTime utcTime) {
        return (float) utcTime.atZone(timeZone.toZoneId()).getOffset().getTotalSeconds() / 3600.0f;
    }
    
    @Test
    public void test_GMT() {
        GeoLocation gmt = GMT();
        assertEquals(gmt.getLocationName(), "Greenwich, England");
        assertEquals(gmt.getLongitude(), 0, 0);
        assertEquals(gmt.getLatitude(), 51.4772, 0);
        assertTrue(gmt.getTimeZone().getID().endsWith("GMT"));
        assertEquals(gmt.getElevation(), 0, 0);
    }

    @Test
    public void test_latitude_numeric() {
        GeoLocation geo = GMT();
        geo.setLatitude(33.3);
        assertEquals(geo.getLatitude(), 33.3, 0);
    }

    @Test
    public void test_latitude_cartography_north() {
        GeoLocation geo = GMT();
        geo.setLatitude(41, 7, 5.17296, "N");
        assertEquals(geo.getLatitude(), 41.1181036, 0);
    }

    @Test
    public void test_latitude_cartography_south() {
        GeoLocation geo = GMT();
        geo.setLatitude(41, 7, 5.17296, "S");
        assertEquals(geo.getLatitude(), -41.1181036, 0);
    }

    @Test
    public void test_longitude_numeric() {
        GeoLocation geo = GMT();
        geo.setLongitude(23.4);
        assertEquals(geo.getLongitude(), 23.4, 0);
    }

    @Test
    public void test_longitude_cartography_east() {
        GeoLocation geo = GMT();
        geo.setLongitude(41, 7, 5.17296, "E");
        assertEquals(geo.getLongitude(), 41.1181036, 0);
    }

    @Test
    public void test_longitude_cartography_west() {
        GeoLocation geo = GMT();
        geo.setLongitude(41, 7, 5.17296, "W");
        assertEquals(geo.getLongitude(), -41.1181036, 0);
    }

    @Test
    public void test_time_zone_with_string() {
        GeoLocation geo = GMT();
        geo.setTimeZone(TimeZone.getTimeZone(ZoneId.of("America/New_York")));
        assertTrue(geo.getTimeZone().getID().endsWith("America/New_York"));
    }

    @Test
    public void test_time_zone_with_timezone_object() {
        GeoLocation geo = GMT();
        geo.setTimeZone(TimeZone.getTimeZone(ZoneId.of("America/New_York")));
        assertTrue(geo.getTimeZone().getID().endsWith("America/New_York"));
    }

    @Test
    public void test_antimeridian_adjustment_for_gmt() {
        GeoLocation geo = GMT();
        assertEquals(geo.getAntimeridianAdjustment(), 0, 0);
    }

    @Test
    public void test_antimeridian_adjustment_for_standard_timezone() {
        GeoLocation geo = GMT();
        geo.setTimeZone(TimeZone.getTimeZone(ZoneId.of("America/New_York")));
        assertEquals(geo.getAntimeridianAdjustment(), 0, 0);
    }

    @Test
    public void test_antimeridian_adjustment_for_eastward_crossover() {
        GeoLocation geo = TestHelper.samoa();
        assertEquals(geo.getAntimeridianAdjustment(), -1, 0);
    }

    @Test
    public void test_antimeridian_adjustment_for_westward_crossover() {
        GeoLocation geo = GMT();
        geo.setLongitude(179);
        geo.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Etc/GMT+12")));
        assertEquals(geo.getAntimeridianAdjustment(), 1, 0);
    }

    @Test
    public void test_local_mean_time_offset_for_gmt() {
        GeoLocation geo = GMT();
        assertEquals(geo.getLocalMeanTimeOffset(), 0, 0);
    }

    @Test
    public void test_local_mean_time_offset_on_center_meridian() {
        GeoLocation geo = new GeoLocation("Sample", 40, -75, TimeZone.getTimeZone(ZoneId.of("America/New_York")));
        assertEquals(geo.getLocalMeanTimeOffset(), 0, 0);
    }

    @Test
    public void test_local_mean_time_offset_east_of_center_meridian() {
        GeoLocation geo = new GeoLocation("Sample", 40, -74, TimeZone.getTimeZone(ZoneId.of("America/New_York")));
        assertEquals(geo.getLocalMeanTimeOffset(), 4 * MINUTE_MILLIS, 0);
    }

    @Test
    public void test_local_mean_time_offset_west_of_center_meridian() {
        GeoLocation geo = new GeoLocation("Sample", 40, -76.25, TimeZone.getTimeZone(ZoneId.of("America/New_York")));
        assertEquals(geo.getLocalMeanTimeOffset(), -1.25 * 4 * MINUTE_MILLIS, 0);
    }

    @Test
    public void test_standard_time_offset_for_gmt() {
        GeoLocation geo = GMT();
        assertEquals(getStandardTimeOffset(geo.getTimeZone()), 0, 0);
    }
    public int getStandardTimeOffset(TimeZone timeZone) {
        ZonedDateTime now = ZonedDateTime.now(timeZone.toZoneId());
        ZoneOffset utcOffset = now.getOffset();
        Duration dstOffset = Duration.ZERO;


        if (now.getZone().getRules().isDaylightSavings(now.toInstant())) {
            dstOffset = Duration.ofSeconds(now.getZone().getRules().getDaylightSavings(now.toInstant()).getSeconds());
        }

        Duration offset = Duration.ofSeconds(utcOffset.getTotalSeconds() - dstOffset.getSeconds());
        return (int) (offset.getSeconds() * 1000);
    }

    @Test
    public void test_standard_time_offset_for_standard_timezone() {
        GeoLocation geo = GMT();
        geo.setTimeZone(TimeZone.getTimeZone(ZoneId.of("America/New_York")));
        assertEquals(getStandardTimeOffset(geo.getTimeZone()), -5 * HOUR_MILLIS, 0);
    }

    @Test
    public void test_time_zone_offset_at() {
        String[][] expected = {
                {"2017-03-12T06:30:00Z", "US/Eastern", "-5"},
                {"2017-03-12T07:00:00Z", "US/Eastern", "-4"},
                {"2017-03-12T09:30:00Z", "US/Pacific", "-8"},
                {"2017-03-12T10:00:00Z", "US/Pacific", "-7"},
                {"2017-03-23T23:30:00Z", "Asia/Jerusalem", "2"},
                {"2017-03-24T00:00:00Z", "Asia/Jerusalem", "3"}
        };

        for (String[] entry : expected) {
            String time = entry[0];
            String tz = entry[1];
            float expectedOffset = Float.parseFloat(entry[2]);
            TimeZone timeZone = TimeZone.getTimeZone(tz);
            GeoLocation geo = new GeoLocation("Sample", 0, 0, timeZone);
            Instant instant = Instant.parse(time);
            ZonedDateTime utcTime = ZonedDateTime.ofInstant(instant, timeZone.toZoneId());
            float offset = timeZoneOffsetAt(geo.getTimeZone(), utcTime.toLocalDateTime());
            assertEquals(time + " - " + tz, expectedOffset, offset, 0.001);
        }
    }

}