package com.kosherjava.zmanim.hebrewcalendar;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

public class YomiCalculatorTest {
	private static HebrewDateFormatter hdf = new HebrewDateFormatter();
	static {
		hdf.setHebrewFormat(true);		
	}
	 
	@Test
	public void testCorrectDaf1() {
		JewishCalendar jewishCalendar = new JewishCalendar(5685, JewishDate.KISLEV, 12);
		Daf daf = YomiCalculator.getDafYomiBavli(jewishCalendar);
		Assert.assertEquals(5, daf.getMasechtaNumber());
		Assert.assertEquals(2, daf.getDaf());
		System.out.println(hdf.formatDafYomiBavli(jewishCalendar.getDafYomiBavli()));
	}

	@Test
	public void testCorrectDaf2() {
		JewishCalendar jewishCalendar = new JewishCalendar(5736, JewishDate.ELUL, 26);
		Daf daf = YomiCalculator.getDafYomiBavli(jewishCalendar);
		Assert.assertEquals(4, daf.getMasechtaNumber());
		Assert.assertEquals(14, daf.getDaf());
		System.out.println(hdf.formatDafYomiBavli(jewishCalendar.getDafYomiBavli()));
	}
	
	@Test
	public void testCorrectDaf3() {
		JewishCalendar jewishCalendar = new JewishCalendar(5777, JewishDate.ELUL, 10);
		Daf daf = YomiCalculator.getDafYomiBavli(jewishCalendar);
		Assert.assertEquals(23, daf.getMasechtaNumber());
		Assert.assertEquals(47, daf.getDaf());
		System.out.println(hdf.formatDafYomiBavli(jewishCalendar.getDafYomiBavli()));
	}

	//From python-zmanim:


	@Test
	public void testSimpleDate() {
		assertMatches(LocalDate.of(2017, 12, 28), 25, 30);
	}

	@Test
	public void testBeforeCycleBegan() {
		try { //assertThrows
			assertMatches(LocalDate.of(1920,1,1), Integer.MIN_VALUE, Integer.MIN_VALUE);
			assert false;//should throw
		} catch (Throwable t) {
			assert true;
		}
	}
	@Test
	public void testFirstDayOfCycle() {
		assertMatches(LocalDate.of(2012, 8, 3), 0, 2);
	}
	@Test
	public void testLastDayOfCycle() {
		assertMatches(LocalDate.of(2020, 1, 4), 39, 73);
	}
	@Test
	public void testBeforeShekalimTransitionEndOfShekalim() {
		assertMatches(LocalDate.of(1969, 4, 28), 4, 13);
	}
	@Test
	public void testBeforeShekalimTransitionBeginningOfYoma() {
		assertMatches(LocalDate.of(1969, 4, 29), 5, 2);
	}
	@Test
	public void testEndOfMeilah() {
		assertMatches(LocalDate.of(2019, 10, 9), 35, 22);
	}
	@Test
	public void testBeginningOfKinnim() {
		assertMatches(LocalDate.of(2019, 10, 10), 36, 23);
	}
	@Test
	public void testBeginningOfTamid() {
		assertMatches(LocalDate.of(2019, 10, 13), 37, 26);
	}
	@Test
	public void testBeginningOfMiddos() {
		assertMatches(LocalDate.of(2019, 10, 22), 38, 35);
	}
	@Test
	public void testAfterMiddos() {
		assertMatches(LocalDate.of(2019, 10, 25), 39, 2);
	}

	private void assertMatches(LocalDate date, int masechtaNumber, int dafNum) {
		JewishCalendar cal = new JewishCalendar(date);
		Daf daf = cal.getDafYomiBavli();
		Assert.assertEquals(daf.getMasechtaNumber(), masechtaNumber);
		Assert.assertEquals(daf.getDaf(), dafNum);
	}
}
