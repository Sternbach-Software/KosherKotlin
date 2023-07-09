/*
 * Zmanim Java API
 * Copyright (C) 2019 - 2022 Eliyahu Hershfeld
 * Copyright (C) 2019 - 2021 Y Paritcher
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful,but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA,
 * or connect to: http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 */
package com.kosherjava.zmanim.hebrewcalendar

import com.kosherjava.zmanim.util.ZmanimFormatter
import java.util.TimeZone
import java.lang.StringBuffer
import com.kosherjava.zmanim.util.Zman
import java.lang.IllegalArgumentException
import com.kosherjava.zmanim.util.GeoLocation
import java.lang.StringBuilder
import java.lang.CloneNotSupportedException
import com.kosherjava.zmanim.util.AstronomicalCalculator
import java.util.Calendar
import com.kosherjava.zmanim.util.NOAACalculator
import java.text.SimpleDateFormat
import java.text.DecimalFormat
import java.text.DateFormat
import java.util.Collections
import com.kosherjava.zmanim.util.GeoLocationUtils
import com.kosherjava.zmanim.util.SunTimesCalculator
import com.kosherjava.zmanim.hebrewcalendar.Daf
import com.kosherjava.zmanim.hebrewcalendar.JewishDate
import java.time.LocalDate
import java.util.GregorianCalendar
import com.kosherjava.zmanim.hebrewcalendar.HebrewDateFormatter
import com.kosherjava.zmanim.hebrewcalendar.JewishCalendar
import com.kosherjava.zmanim.hebrewcalendar.JewishCalendar.Parsha
import com.kosherjava.zmanim.hebrewcalendar.YomiCalculator
import com.kosherjava.zmanim.hebrewcalendar.YerushalmiYomiCalculator
import java.util.EnumMap
import com.kosherjava.zmanim.AstronomicalCalendar
import com.kosherjava.zmanim.ZmanimCalendar
import java.math.BigDecimal
import com.kosherjava.zmanim.ComplexZmanimCalendar

/**
 * Tefila Rules is a utility class that covers the various *halachos* and *minhagim* regarding
 * changes to daily *tefila* / prayers, based on the Jewish calendar. This is mostly useful for use in
 * developing *siddur* type applications, but it is also valuable for *shul* calendars that set
 * *tefila* times based on if [*tachanun*](https://en.wikipedia.org/wiki/Tachanun) is
 * recited that day. There are many settings in this class to cover the vast majority of *minhagim*, but
 * there are likely some not covered here. The source for many of the *chasidishe minhagim* can be found
 * in the [Minhag Yisrael Torah](https://www.nli.org.il/he/books/NNL_ALEPH001141272/NLI) on Orach
 * Chaim 131.
 * Dates used in specific communities such as specific *yahrzeits* or a holidays like Purim Mezhbizh
 * (Medzhybizh) celebrated on 11 [&lt;em&gt;Teves&lt;/em&gt;][JewishDate.TEVES] or [Purim Saragossa](https://en.wikipedia.org/wiki/Second_Purim#Purim_Saragossa_(18_Shevat)) celebrated on
 * the (17th or) 18th of [&lt;em&gt;Shevat&lt;/em&gt;][JewishDate.SHEVAT] are not (and likely will not be) supported by
 * this class.
 *
 * Sample code:
 * <pre style="background: #FEF0C9; display: inline-block;">
 * TefilaRules tr = new TefilaRules();
 * JewishCalendar jewishCalendar = new JewishCalendar();
 * HebrewDateFormatter hdf = new HebrewDateFormatter();
 * jewishCalendar.setJewishDate(5783, JewishDate.TISHREI, 1); // Rosh Hashana
 * System.out.println(hdf.format(jewishCalendar) + ": " + tr.isTachanunRecitedShacharis(jd));
 * jewishCalendar.setJewishDate(5783, JewishDate.ADAR, 17);
 * System.out.println(hdf.format(jewishCalendar) + ": " + tr.isTachanunRecitedShacharis(jewishCalendar));
 * tr.setTachanunRecitedWeekOfPurim(false);
 * System.out.println(hdf.format(jewishCalendar) + ": " + tr.isTachanunRecitedShacharis(jewishCalendar));</pre>
 *
 * @author  Y. Paritcher 2019 - 2021
 * @author  Eliyahu Hershfeld 2019 - 2022
 *
 * @todo The following items may be added at a future date.
 *
 *  1. *Lamnatzaiach*
 *  1. *Mizmor Lesoda*
 *  1. *Behab*
 *  1. *Selichos*
 *  1. ...
 *
 */
class TefilaRules constructor() {
    /**
     * Is *tachanun* recited at the end Of [&lt;em&gt;Tishrei&lt;/em&gt;][JewishDate.TISHREI].The Magen Avraham 669:1 and the Pri
     * Chadash 131:7 state that some places to not recite *tachanun* during this period. The Sh"UT Chasam Sofer on Choshen
     * Mishpat 77 writes that this is the *minhag* in Ashkenaz. The Shaarei Teshuva 131:19 quotes the Sheyarie Kneses
     * Hagdola who also states that it should not be recited. The Aderes wanted to institute saying *tachanun* during this
     * period, but was dissuaded from this by Rav Shmuel Salant who did not want to change the *minhag* in Yerushalayim.
     * The Aruch Hashulchan is of the opinion that that this *minhag* is incorrect, and it should be recited, and The Chazon
     * Ish also recited *tachanun* during this period. See the Dirshu edition of the Mishna Berurah for details.
     * @return If *tachanun* is set to be recited at the end of [&lt;em&gt;Tishrei&lt;/em&gt;][JewishDate.TISHREI].
     * @see .setTachanunRecitedEndOfTishrei
     */
    /**
     * Sets if *tachanun* should be recited at the end of [&lt;em&gt;Tishrei&lt;/em&gt;][JewishDate.TISHREI].
     * @param tachanunRecitedEndOfTishrei is *tachanun* recited at the end of [&lt;em&gt;Tishrei&lt;/em&gt;][JewishDate.TISHREI].
     * @see .isTachanunRecitedEndOfTishrei
     */
    /**
     * The default value is `true`.
     * @see .isTachanunRecitedEndOfTishrei
     * @see .setTachanunRecitedEndOfTishrei
     */
    var isTachanunRecitedEndOfTishrei: Boolean = true
    /**
     * Is *tachanun* recited during the week after *Shavuos*. This is the opinion of the Pri Megadim
     * quoted by the Mishna Berurah. This is since *karbanos* of *Shavuos* have *tashlumim* for
     * 7 days, it is still considered like a Yom Tov. The Chazon Ish quoted in the Orchos Rabainu vol. 1 page 68
     * recited *tachanun* during this week.
     *
     * @return If *tachanun* is set to be recited during the week after Shavuos.
     * @see .setTachanunRecitedWeekAfterShavuos
     */
    /**
     * Sets if *tachanun* should be recited during the week after *Shavuos*.
     * @param tachanunRecitedWeekAfterShavuos is *tachanun* recited during the week after Shavuos.
     * @see .isTachanunRecitedWeekAfterShavuos
     */
    /**
     * The default value is `false`.
     * @see .isTachanunRecitedWeekAfterShavuos
     * @see .setTachanunRecitedWeekAfterShavuos
     */
    var isTachanunRecitedWeekAfterShavuos: Boolean = false
    /**
     * Is *tachanun* is recited on the 13th of [&lt;em&gt;Sivan&lt;/em&gt;][JewishDate.SIVAN] ([*Yom Tov Sheni shel Galuyos*](https://en.wikipedia.org/wiki/Yom_tov_sheni_shel_galuyot) of the 7th
     * day) outside Israel. This is brought down by the Shaarie Teshuva 131:19 quoting the [Sheyarei Kneses Hagedola 131:12](https://hebrewbooks.org/pdfpager.aspx?req=41295&st=&pgnum=39)that
     * *tachanun* should not be recited on this day. Rav Shlomo Zalman Orbach in Halichos Shlomo on
     * Shavuos 12:16:25 is of the opinion that even in *chutz laaretz* it should be recited since the *yemei
     * Tashlumin* are counted based on Israel since that is where the *karbanos* are brought. Both
     * [.isTachanunRecitedShacharis] and [.isTachanunRecitedMincha]
     * only return false if the location is not set to [Israel][JewishCalendar.getInIsrael] and both
     * [.tachanunRecitedWeekAfterShavuos] and [.setTachanunRecited13SivanOutOfIsrael] are set to false.
     *
     * @return If *tachanun* is set to be recited on the 13th of [&lt;em&gt;Sivan&lt;/em&gt;][JewishDate.SIVAN] out of Israel.
     * @see .setTachanunRecited13SivanOutOfIsrael
     * @see .isTachanunRecitedWeekAfterShavuos
     */
    /**
     * Sets if *tachanun* should be recited on the 13th of [&lt;em&gt;Sivan&lt;/em&gt;][JewishDate.SIVAN] ([*Yom Tov Sheni shel Galuyos*](https://en.wikipedia.org/wiki/Yom_tov_sheni_shel_galuyot) of the 7th
     * day) outside Israel.
     * @param tachanunRecitedThirteenSivanOutOfIsrael sets if *tachanun* should be recited on the 13th of [          ][JewishDate.SIVAN] out of Israel. Both [.isTachanunRecitedShacharis] and
     * [.isTachanunRecitedMincha] only return false if the location is not set to [          ][JewishCalendar.getInIsrael] and both [.tachanunRecitedWeekAfterShavuos] and
     * [.setTachanunRecited13SivanOutOfIsrael] are set to false.
     * @see .isTachanunRecited13SivanOutOfIsrael
     */
    /**
     * The default value is `true`.
     * @see .isTachanunRecited13SivanOutOfIsrael
     * @see .setTachanunRecited13SivanOutOfIsrael
     */
    var isTachanunRecited13SivanOutOfIsrael: Boolean = true
    /**
     * Is *tachanun* recited on [&lt;em&gt;Pesach Sheni&lt;/em&gt;][JewishCalendar.PESACH_SHENI]. The Pri Chadash 131:7 states
     * that *tachanun* should not be recited. The Aruch Hashulchan states that this is the minhag of the *sephardim*.
     * the Shaarei Efraim 10:27 also mentions that it is not recited, as does the Siddur Yaavetz (Shaar Hayesod, Chodesh Iyar).
     * The Pri Megadim (Mishbetzes Hazahav 131:15) and the Chazon Ish (Erev Pesahc Shchal Beshabos, page 203 in [Rav Sheraya
 * Devlitzky's](https://he.wikipedia.org/wiki/%D7%A9%D7%A8%D7%99%D7%94_%D7%93%D7%91%D7%9C%D7%99%D7%A6%D7%A7%D7%99) comments).
     *
     * @return If *tachanun* is recited on [&lt;em&gt;Pesach Sheni&lt;/em&gt;][JewishCalendar.PESACH_SHENI].
     * @see .setTachanunRecitedPesachSheni
     */
    /**
     * Sets if *tachanun* should be recited on [&lt;em&gt;Pesach Sheni&lt;/em&gt;][JewishCalendar.PESACH_SHENI].
     * @param tachanunRecitedPesachSheni sets if *tachanun* should be recited on *Pesach Sheni*.
     * @see .isTachanunRecitedPesachSheni
     */
    /**
     * The default value is `false`.
     * @see .isTachanunRecitedPesachSheni
     * @see .setTachanunRecitedPesachSheni
     */
    var isTachanunRecitedPesachSheni: Boolean = false
    /**
     * Is *tachanun* recited on 15 [&lt;em&gt;Iyar&lt;/em&gt;][JewishDate.IYAR] (*sfaika deyoma* of [ &lt;em&gt;Pesach Sheni&lt;/em&gt;][JewishCalendar.PESACH_SHENI]) out of Israel. If [.isTachanunRecitedPesachSheni] is `true` this will be
     * ignored even if `false`.
     *
     * @return if *tachanun* is recited on 15 [&lt;em&gt;Iyar&lt;/em&gt;][JewishDate.IYAR]  (*sfaika deyoma* of [          ][JewishCalendar.PESACH_SHENI] out of Israel. If [.isTachanunRecitedPesachSheni]
     * is `true` this will be ignored even if `false`.
     * @see .setTachanunRecited15IyarOutOfIsrael
     * @see .setTachanunRecitedPesachSheni
     * @see .isTachanunRecitedPesachSheni
     */
    /**
     * Sets if *tachanun* should be recited on the 15th of [&lt;em&gt;Iyar&lt;/em&gt;][JewishDate.IYAR]  ([*Yom Tov Sheni shel Galuyos*](https://en.wikipedia.org/wiki/Yom_tov_sheni_shel_galuyot) of
     * [&lt;em&gt;Pesach Sheni&lt;/em&gt;][JewishCalendar.PESACH_SHENI]) out of Israel. Ignored if [ ][.isTachanunRecitedPesachSheni] is `true`.
     *
     * @param tachanunRecited15IyarOutOfIsrael if *tachanun* should be recited on the 15th of [          &lt;em&gt;Iyar&lt;/em&gt;][JewishDate.IYAR] (*sfaika deyoma* of [&lt;em&gt;Pesach Sheni&lt;/em&gt;][JewishCalendar.PESACH_SHENI]) out of Israel.
     * @see .isTachanunRecited15IyarOutOfIsrael
     */
    /**
     * The default value is `true`.
     * @see .isTachanunRecited15IyarOutOfIsrael
     * @see .setTachanunRecited15IyarOutOfIsrael
     */
    var isTachanunRecited15IyarOutOfIsrael: Boolean = true
    /**
     * Is *tachanun* recited on *mincha* on *erev [Lag Baomer][JewishCalendar.LAG_BAOMER]*.
     * @return if *tachanun* is recited in *mincha* on *erev*
     * [&lt;em&gt;Lag Baomer&lt;/em&gt;][JewishCalendar.LAG_BAOMER].
     * @see .setTachanunRecitedMinchaErevLagBaomer
     */
    /**
     * Sets if *tachanun* should be recited on *erev [Lag Baomer][JewishCalendar.LAG_BAOMER]*.
     * @param tachanunRecitedMinchaErevLagBaomer sets if *tachanun* should be recited on *mincha*
     * of *erev [Lag Baomer][JewishCalendar.LAG_BAOMER]*.
     * @see .isTachanunRecitedMinchaErevLagBaomer
     */
    /**
     * The default value is `false`.
     * @see .isTachanunRecitedMinchaErevLagBaomer
     * @see .setTachanunRecitedMinchaErevLagBaomer
     */
    var isTachanunRecitedMinchaErevLagBaomer: Boolean = false
    /**
     * Is *tachanun* recited during the *Shivas Yemei Hamiluim*, from the 23 of [ ][JewishDate.ADAR] on a non-leap-year or [&lt;em&gt;Adar II&lt;/em&gt;][JewishDate.ADAR_II] on a
     * leap year to the end of the month. Some *chasidishe* communities do not say *tachanun*
     * during this week. See [Darkei
 * Chaim Veshalom 191](https://hebrewbooks.org/pdfpager.aspx?req=4692&st=&pgnum=70).
     * @return if *tachanun* is recited during the *Shivas Yemei Hamiluim*, from the 23 of [          ][JewishDate.ADAR] on a non-leap-year or [&lt;em&gt;Adar II&lt;/em&gt;][JewishDate.ADAR_II]
     * on a leap year to the end of the month.
     * @see .setTachanunRecitedShivasYemeiHamiluim
     */
    /**
     * Sets if *tachanun* should be recited during the *Shivas Yemei Hamiluim*, from the 23 of
     * [&lt;em&gt;Adar&lt;/em&gt;][JewishDate.ADAR] on a non-leap-year or [&lt;em&gt;Adar II&lt;/em&gt;][JewishDate.ADAR_II]
     * on a leap year to the end of the month.
     * @param tachanunRecitedShivasYemeiHamiluim sets if *tachanun* should be recited during the
     * *Shivas Yemei Hamiluim*.
     * @see .isTachanunRecitedShivasYemeiHamiluim
     */
    /**
     * The default value is `true`.
     * @see .isTachanunRecitedShivasYemeiHamiluim
     * @see .setTachanunRecitedShivasYemeiHamiluim
     */
    var isTachanunRecitedShivasYemeiHamiluim: Boolean = true
    /**
     * Is *tachanun* recited during the *sefira* week of *Hod* (14 - 20 [&lt;em&gt;Iyar&lt;/em&gt;][JewishDate.IYAR],
     * or the 29th - 35th of the [&lt;em&gt;Omer&lt;/em&gt;][JewishCalendar.getDayOfOmer]). Some *chasidishe* communities
     * do not recite *tachanun* during this week. See Minhag Yisrael Torah 131:Iyar.
     * @return If *tachanun* is set to be recited during the *sefira* week of *Hod* (14 - 20 [         ][JewishDate.IYAR], or the 29th - 35th of the [&lt;em&gt;Omer&lt;/em&gt;][JewishCalendar.getDayOfOmer]).
     * @see .setTachanunRecitedWeekOfHod
     */
    /**
     * Sets if *tachanun* should be recited during the *sefira* week of *Hod* (14 - 20 [ &lt;em&gt;Iyar&lt;/em&gt;][JewishDate.IYAR], or the 29th - 35th of the [&lt;em&gt;Omer&lt;/em&gt;][JewishCalendar.getDayOfOmer]).
     * @param tachanunRecitedWeekOfHod Sets if *tachanun* should be recited during the *sefira* week of
     * *Hod*.
     * @see .isTachanunRecitedWeekOfHod
     */
    /**
     * The default value is `true`.
     * @see .isTachanunRecitedWeekOfHod
     * @see .setTachanunRecitedWeekOfHod
     */
    var isTachanunRecitedWeekOfHod: Boolean = true
    /**
     * Is *tachanun* recited during the week of Purim, from the 11th through the 17th of [ ][JewishDate.ADAR] (on a non-leap year, or [&lt;em&gt;Adar II&lt;/em&gt;][JewishDate.ADAR_II] on a leap year). Some
     * *chasidishe* communities do not recite *tachanun* during this period. See the [Minhag Yisrael Torah](https://www.nli.org.il/he/books/NNL_ALEPH001141272/NLI) 131 and [Darkei Chaim Veshalom 191](https://hebrewbooks.org/pdfpager.aspx?req=4692&st=&pgnum=70)who discuss the
     * *minhag* not to recite *tachanun*. Also see the [Mishmeres Shalom (Hadras Shalom)](https://hebrewbooks.org/pdfpager.aspx?req=8944&st=&pgnum=160) who discusses the
     * *minhag* of not reciting it on the 16th and 17th.
     * @return If *tachanun* is set to be recited during the week of Purim from the 11th through the 17th of [         ][JewishDate.ADAR] (on a non-leap year, or [&lt;em&gt;Adar II&lt;/em&gt;][JewishDate.ADAR_II] on a leap year).
     * @see .setTachanunRecitedWeekOfPurim
     */
    /**
     * Sets if *tachanun* should be recited during the week of Purim from the 11th through the 17th of [ ][JewishDate.ADAR] (on a non-leap year), or [&lt;em&gt;Adar II&lt;/em&gt;][JewishDate.ADAR_II] (on a leap year).
     * @param tachanunRecitedWeekOfPurim Sets if *tachanun* is to recited during the week of Purim from the 11th
     * through the 17th of [&lt;em&gt;Adar&lt;/em&gt;][JewishDate.ADAR] (on a non-leap year), or [         &lt;em&gt;Adar II&lt;/em&gt;][JewishDate.ADAR_II] (on a leap year). Some *chasidishe* communities do not recite *tachanun*
     * during this period.
     * @see .isTachanunRecitedWeekOfPurim
     */
    /**
     * The default value is `true`.
     * @see .isTachanunRecitedWeekOfPurim
     * @see .setTachanunRecitedWeekOfPurim
     */
    var isTachanunRecitedWeekOfPurim: Boolean = true
    /**
     * Is *tachanun* recited on Fridays. Some *chasidishe* communities do not recite
     * *tachanun* on Fridays. See [Likutei
 * Maharich Vol 2 Seder Hanhagos Erev Shabbos](https://hebrewbooks.org/pdfpager.aspx?req=41190&st=&pgnum=10). This is also the *minhag* in Satmar.
     * @return if *tachanun* is recited on Fridays.
     * @see .setTachanunRecitedFridays
     */
    /**
     * Sets if *tachanun* should be recited on Fridays.
     * @param tachanunRecitedFridays sets if *tachanun* should be recited on Fridays. Some *chasidishe*
     * communities do not recite *tachanun* on Fridays.
     * @see .isTachanunRecitedFridays
     */
    /**
     * The default value is `true`.
     * @see .isTachanunRecitedFridays
     * @see .setTachanunRecitedFridays
     */
    var isTachanunRecitedFridays: Boolean = true
    /**
     * Is *tachanun* recited on Sundays. Some *chasidishe* communities do not recite
     * *tachanun* on Sundays. See [Likutei
 * Maharich Vol 2 Seder Hanhagos Erev Shabbos](https://hebrewbooks.org/pdfpager.aspx?req=41190&st=&pgnum=10).
     * @return if *tachanun* is recited on Sundays.
     * @see .setTachanunRecitedSundays
     */
    /**
     * Sets if *tachanun* should be recited on Sundays.
     * @param tachanunRecitedSundays sets if *tachanun* should be recited on Sundays. Some *chasidishe*
     * communities do not recite *tachanun* on Sundays.
     * @see .isTachanunRecitedSundays
     */
    /**
     * The default value is `true`.
     * @see .isTachanunRecitedSundays
     * @see .setTachanunRecitedSundays
     */
    var isTachanunRecitedSundays: Boolean = true
    /**
     * Is *tachanun* recited in *Mincha* the entire year. Some *chasidishe* communities do not recite
     * *tachanun* by *Mincha* all year round. See[Nemukei Orach Chaim 131:3](https://hebrewbooks.org/pdfpager.aspx?req=4751&st=&pgnum=105).
     * @return if *tachanun* is recited in *Mincha* the entire year.
     * @see .setTachanunRecitedMinchaAllYear
     */
    /**
     * Sets if *tachanun* should be recited in *Mincha* the entire year.
     * @param tachanunRecitedMinchaAllYear sets if *tachanun* should be recited by *mincha* all year. If set
     * to false, [.isTachanunRecitedMincha] will always return false. If set to true (the
     * default), it will use the regular rules.
     * @see .isTachanunRecitedMinchaAllYear
     */
    /**
     * The default value is `true`.
     * @see .isTachanunRecitedMinchaAllYear
     * @see .setTachanunRecitedMinchaAllYear
     */
    var isTachanunRecitedMinchaAllYear: Boolean = true

    /**
     * Returns if *tachanun* is recited during *shacharis* on the day in question. See the many
     * *minhag* based settings that are available in this class.
     *
     * @param jewishCalendar the Jewish calendar day.
     * @return if *tachanun* is recited during *shacharis*.
     * @see .isTachanunRecitedMincha
     */
    fun isTachanunRecitedShacharis(jewishCalendar: JewishCalendar): Boolean {
        val holidayIndex = jewishCalendar.yomTovIndex
        val day = jewishCalendar.jewishDayOfMonth
        val month = jewishCalendar.jewishMonth
        if (((jewishCalendar.dayOfWeek == Calendar.SATURDAY
                    ) || (!isTachanunRecitedSundays && jewishCalendar.dayOfWeek == Calendar.SUNDAY)
                    || (!isTachanunRecitedFridays && jewishCalendar.dayOfWeek == Calendar.FRIDAY)
                    || (month == JewishDate.NISSAN
                    ) || (month == JewishDate.TISHREI && (((!isTachanunRecitedEndOfTishrei && day > 8)
                    || (isTachanunRecitedEndOfTishrei && (day > 8 && day < 22)))))
                    || (month == JewishDate.SIVAN && ((isTachanunRecitedWeekAfterShavuos && day < 7
                    || !isTachanunRecitedWeekAfterShavuos && day < (if (!jewishCalendar.inIsrael
                && !isTachanunRecited13SivanOutOfIsrael
            ) 14 else 13))))
                    || (jewishCalendar.isYomTov && ((!jewishCalendar.isTaanis
                    || (!isTachanunRecitedPesachSheni && holidayIndex == JewishCalendar.PESACH_SHENI)))) // Erev YT is included in isYomTov()
                    || ((!jewishCalendar.inIsrael && !isTachanunRecitedPesachSheni && !isTachanunRecited15IyarOutOfIsrael
                    && (jewishCalendar.jewishMonth == JewishDate.IYAR) && (day == 15)))
                    || (holidayIndex == JewishCalendar.TISHA_BEAV) || jewishCalendar.isIsruChag
                    || jewishCalendar.isRoshChodesh
                    || ((!isTachanunRecitedShivasYemeiHamiluim &&
                    (((!jewishCalendar.isJewishLeapYear && month == JewishDate.ADAR)
                            || (jewishCalendar.isJewishLeapYear && month == JewishDate.ADAR_II))) && (day > 22)))
                    || ((!isTachanunRecitedWeekOfPurim &&
                    (((!jewishCalendar.isJewishLeapYear && month == JewishDate.ADAR)
                            || (jewishCalendar.isJewishLeapYear && month == JewishDate.ADAR_II))) && (day > 10) && (day < 18)))
                    || ((jewishCalendar.isUseModernHolidays
                    && (holidayIndex == JewishCalendar.YOM_HAATZMAUT || holidayIndex == JewishCalendar.YOM_YERUSHALAYIM)))
                    || (!isTachanunRecitedWeekOfHod && (month == JewishDate.IYAR) && (day > 13) && (day < 21)))
        ) {
            return false
        }
        return true
    }

    /**
     * Returns if *tachanun* is recited during *mincha* on the day in question.
     *
     * @param jewishCalendar the Jewish calendar day.
     * @return if *tachanun* is recited during *mincha*.
     * @see .isTachanunRecitedShacharis
     */
    fun isTachanunRecitedMincha(jewishCalendar: JewishCalendar): Boolean {
        var tomorrow: JewishCalendar = JewishCalendar()
        tomorrow = jewishCalendar.clone() as JewishCalendar
        tomorrow.forward(Calendar.DATE, 1)
        return !(
                !isTachanunRecitedMinchaAllYear ||
                        jewishCalendar.dayOfWeek == Calendar.FRIDAY ||
                        !isTachanunRecitedShacharis(jewishCalendar) ||
                        (
                                (!isTachanunRecitedShacharis(tomorrow) &&
                                        tomorrow.yomTovIndex != JewishCalendar.EREV_ROSH_HASHANA &&
                                        tomorrow.yomTovIndex != JewishCalendar.EREV_YOM_KIPPUR &&
                                        tomorrow.yomTovIndex != JewishCalendar.PESACH_SHENI)
                                ) || 
                        (!isTachanunRecitedMinchaErevLagBaomer && tomorrow.yomTovIndex == JewishCalendar.LAG_BAOMER)
                )
    }

    /**
     * Returns if it is the Jewish day (starting the evening before) to start reciting *Vesein Tal Umatar Livracha*
     * (*Sheailas Geshamim*). In Israel this is the 7th day of [&lt;em&gt;Marcheshvan&lt;/em&gt;][JewishDate.CHESHVAN].
     * Outside Israel recitation starts on the evening of December 4th (or 5th if it is the year before a civil leap year)
     * in the 21st century and shifts a day forward every century not evenly divisible by 400. This method will return true
     * if *vesein tal umatar* on the current Jewish date that starts on the previous night, so Dec 5/6 will be
     * returned by this method in the 21st century. *vesein tal umatar* is not recited on *Shabbos* and the
     * start date will be delayed a day when the start day is on a *Shabbos* (this can only occur out of Israel).
     *
     * @param jewishCalendar the Jewish calendar day.
     *
     * @return true if it is the first Jewish day (starting the prior evening of reciting *Vesein Tal Umatar Livracha*
     * (*Sheailas Geshamim*).
     *
     * @see .isVeseinTalUmatarStartingTonight
     * @see .isVeseinTalUmatarRecited
     */
    fun isVeseinTalUmatarStartDate(jewishCalendar: JewishCalendar): Boolean {
        if (jewishCalendar.inIsrael) {
            // The 7th Cheshvan can't occur on Shabbos, so always return true for 7 Cheshvan
            if (jewishCalendar.jewishMonth == JewishDate.CHESHVAN && jewishCalendar.jewishDayOfMonth == 7) {
                return true
            }
        } else {
            if (jewishCalendar.dayOfWeek == Calendar.SATURDAY) { //Not recited on Friday night
                return false
            }
            if (jewishCalendar.dayOfWeek == Calendar.SUNDAY) { // When starting on Sunday, it can be the start date or delayed from Shabbos
                return jewishCalendar.tekufasTishreiElapsedDays == 48 || jewishCalendar.tekufasTishreiElapsedDays == 47
            } else {
                return jewishCalendar.tekufasTishreiElapsedDays == 47
            }
        }
        return false // keep the compiler happy
    }

    /**
     * Returns if true if tonight is the first night to start reciting *Vesein Tal Umatar Livracha* (
     * *Sheailas Geshamim*). In Israel this is the 7th day of [ &lt;em&gt;Marcheshvan&lt;/em&gt;][JewishDate.CHESHVAN] (so the 6th will return true). Outside Israel recitation starts on the evening
     * of December 4th (or 5th if it is the year before a civil leap year) in the 21st century and shifts a
     * day forward every century not evenly divisible by 400. *Vesein tal umatar* is not recited on
     * *Shabbos* and the start date will be delayed a day when the start day is on a *Shabbos*
     * (this can only occur out of Israel).
     *
     * @param jewishCalendar the Jewish calendar day.
     *
     * @return true if it is the first Jewish day (starting the prior evening of reciting *Vesein Tal Umatar
     * Livracha* (*Sheailas Geshamim*).
     *
     * @see .isVeseinTalUmatarStartDate
     * @see .isVeseinTalUmatarRecited
     */
    fun isVeseinTalUmatarStartingTonight(jewishCalendar: JewishCalendar): Boolean {
        if (jewishCalendar.inIsrael) {
            // The 7th Cheshvan can't occur on Shabbos, so always return true for 6 Cheshvan
            if (jewishCalendar.jewishMonth == JewishDate.CHESHVAN && jewishCalendar.jewishDayOfMonth == 6) {
                return true
            }
        } else {
            if (jewishCalendar.dayOfWeek == Calendar.FRIDAY) { //Not recited on Friday night
                return false
            }
            if (jewishCalendar.dayOfWeek == Calendar.SATURDAY) { // When starting on motzai Shabbos, it can be the start date or delayed from Friday night
                return jewishCalendar.tekufasTishreiElapsedDays == 47 || jewishCalendar.tekufasTishreiElapsedDays == 46
            } else {
                return jewishCalendar.tekufasTishreiElapsedDays == 46
            }
        }
        return false
    }

    /**
     * Returns if *Vesein Tal Umatar Livracha* (*Sheailas Geshamim*) is recited. This will return
     * true for the entire season, even on *Shabbos* when it is not recited.
     *
     * @param jewishCalendar the Jewish calendar day.
     *
     * @return true if *Vesein Tal Umatar Livracha* (*Sheailas Geshamim*) is recited.
     *
     * @see .isVeseinTalUmatarStartDate
     * @see .isVeseinTalUmatarStartingTonight
     */
    fun isVeseinTalUmatarRecited(jewishCalendar: JewishCalendar): Boolean {
        if (jewishCalendar.jewishMonth == JewishDate.NISSAN && jewishCalendar.jewishDayOfMonth < 15) {
            return true
        }
        if (jewishCalendar.jewishMonth < JewishDate.CHESHVAN) {
            return false
        }
        if (jewishCalendar.inIsrael) {
            return jewishCalendar.jewishMonth != JewishDate.CHESHVAN || jewishCalendar.jewishDayOfMonth >= 7
        } else {
            return jewishCalendar.tekufasTishreiElapsedDays >= 47
        }
    }

    /**
     * Returns if *Vesein Beracha* is recited. It is recited from 15 [&lt;em&gt;Nissan&lt;/em&gt;][JewishDate.NISSAN] to the
     * point that [&lt;em&gt;vesein tal umatar&lt;/em&gt; is recited][.isVeseinTalUmatarRecited].
     *
     * @param jewishCalendar the Jewish calendar day.
     * @return true if *Vesein Beracha* is recited.
     * @see .isVeseinTalUmatarRecited
     */
    fun isVeseinBerachaRecited(jewishCalendar: JewishCalendar): Boolean {
        return !isVeseinTalUmatarRecited(jewishCalendar)
    }

    /**
     * Returns if the date is the start date for reciting *Mashiv Haruach Umorid Hageshem*. The date is 22
     * [&lt;em&gt;Tishrei&lt;/em&gt;][JewishDate.TISHREI].
     *
     * @param jewishCalendar the Jewish calendar day.
     * @return true if the date is the start date for reciting *Mashiv Haruach Umorid Hageshem*.
     * @see .isMashivHaruachEndDate
     * @see .isMashivHaruachRecited
     */
    fun isMashivHaruachStartDate(jewishCalendar: JewishCalendar): Boolean {
        return jewishCalendar.jewishMonth == JewishDate.TISHREI && jewishCalendar.jewishDayOfMonth == 22
    }

    /**
     * Returns if the date is the end date for reciting *Mashiv Haruach Umorid Hageshem*. The date is 15
     * [&lt;em&gt;Nissan&lt;/em&gt;][JewishDate.NISSAN].
     *
     * @param jewishCalendar the Jewish calendar day.
     * @return true if the date is the end date for reciting *Mashiv Haruach Umorid Hageshem*.
     * @see .isMashivHaruachStartDate
     * @see .isMashivHaruachRecited
     */
    fun isMashivHaruachEndDate(jewishCalendar: JewishCalendar): Boolean {
        return jewishCalendar.jewishMonth == JewishDate.NISSAN && jewishCalendar.jewishDayOfMonth == 15
    }

    /**
     * Returns if *Mashiv Haruach Umorid Hageshem* is recited. This period starts on 22 [ ][JewishDate.TISHREI] and ends on the 15th day of [&lt;em&gt;Nissan&lt;/em&gt;][JewishDate.NISSAN].
     *
     * @param jewishCalendar the Jewish calendar day.
     * @return true if *Mashiv Haruach Umorid Hageshem* is recited.
     * @see .isMashivHaruachStartDate
     * @see .isMashivHaruachEndDate
     */
    fun isMashivHaruachRecited(jewishCalendar: JewishCalendar): Boolean {
        val startDate: JewishDate = JewishDate(jewishCalendar.jewishYear, JewishDate.TISHREI, 22)
        val endDate: JewishDate = JewishDate(jewishCalendar.jewishYear, JewishDate.NISSAN, 15)
        return jewishCalendar.compareTo(startDate) > 0 && jewishCalendar.compareTo(endDate) < 0
    }

    /**
     * Returns if *Morid Hatal* (or the lack of reciting *Mashiv Haruach* following *nussach Ashkenaz*) is
     * recited. This period starts on the 15th day of [&lt;em&gt;Nissan&lt;/em&gt;][JewishDate.NISSAN] and ends on 22 [ ][JewishDate.TISHREI].
     *
     * @param jewishCalendar the Jewish calendar day.
     *
     * @return true if *Morid Hatal* (or the lack of reciting *Mashiv Haruach* following *nussach Ashkenaz*) is recited.
     */
    fun isMoridHatalRecited(jewishCalendar: JewishCalendar): Boolean {
        return !isMashivHaruachRecited(jewishCalendar) || isMashivHaruachStartDate(jewishCalendar) || isMashivHaruachEndDate(
            jewishCalendar
        )
    }

    /**
     * Returns if *Hallel* is recited on the day in question. This will return true for both *Hallel shalem*
     * and *Chatzi Hallel*. See [.isHallelShalemRecited] to know if the complete *Hallel*
     * is recited.
     *
     * @param jewishCalendar the Jewish calendar day.
     * @return if *Hallel* is recited.
     * @see .isHallelShalemRecited
     */
    fun isHallelRecited(jewishCalendar: JewishCalendar): Boolean {
        val day = jewishCalendar.jewishDayOfMonth
        val month = jewishCalendar.jewishMonth
        val holidayIndex = jewishCalendar.yomTovIndex
        val inIsrael: Boolean = jewishCalendar.inIsrael
        if (jewishCalendar.isRoshChodesh) { //RH returns false for RC
            return true
        }
        if (jewishCalendar.isChanukah) {
            return true
        }
        when (month) {
            JewishDate.NISSAN -> if (day >= 15 && ((inIsrael && day <= 21) || (!inIsrael && day <= 22))) {
                return true
            }
            JewishDate.IYAR -> if (jewishCalendar.isUseModernHolidays && ((holidayIndex == JewishCalendar.YOM_HAATZMAUT
                        || holidayIndex == JewishCalendar.YOM_YERUSHALAYIM))
            ) {
                return true
            }
            JewishDate.SIVAN -> if (day == 6 || (!inIsrael && (day == 7))) {
                return true
            }
            JewishDate.TISHREI -> if (day >= 15 && (day <= 22 || (!inIsrael && (day <= 23)))) {
                return true
            }
        }
        return false
    }

    /**
     * Returns if *hallel shalem* is recited on the day in question. This will always return false if [ ][.isHallelRecited] returns false.
     *
     * @param jewishCalendar the Jewish calendar day.
     * @return if *hallel shalem* is recited.
     * @see .isHallelRecited
     */
    fun isHallelShalemRecited(jewishCalendar: JewishCalendar): Boolean {
        val day = jewishCalendar.jewishDayOfMonth
        val month = jewishCalendar.jewishMonth
        val inIsrael: Boolean = jewishCalendar.inIsrael
        return isHallelRecited(jewishCalendar)
                &&
                !(
                        (jewishCalendar.isRoshChodesh && !jewishCalendar.isChanukah)
                                ||
                                (
                                        month == JewishDate.NISSAN &&
                                                (
                                                        (inIsrael && day > 15)
                                                                || (!inIsrael && day > 16)
                                                        )
                                        )
                        )
    }

    /**
     * Returns if [*Al HaNissim*](https://en.wikipedia.org/wiki/Al_HaNissim) is recited on the day in question.
     *
     * @param jewishCalendar the Jewish calendar day.
     * @return if *al hanissim* is recited.
     * @see JewishCalendar.isChanukah
     * @see JewishCalendar.isPurim
     * @see JewishCalendar.getIsMukafChoma
     */
    fun isAlHanissimRecited(jewishCalendar: JewishCalendar): Boolean {
        return jewishCalendar.isPurim || jewishCalendar.isChanukah
    }

    /**
     * Returns if *Yaaleh Veyavo* is recited on the day in question.
     *
     * @param jewishCalendar the Jewish calendar day.
     * @return if *Yaaleh Veyavo* is recited.
     * @see JewishCalendar.isPesach
     * @see JewishCalendar.isShavuos
     * @see JewishCalendar.isRoshHashana
     * @see JewishCalendar.isYomKippur
     * @see JewishCalendar.isSuccos
     * @see JewishCalendar.isShminiAtzeres
     * @see JewishCalendar.isSimchasTorah
     * @see JewishCalendar.isRoshChodesh
     */
    fun isYaalehVeyavoRecited(jewishCalendar: JewishCalendar): Boolean {
        return jewishCalendar.isPesach ||
                jewishCalendar.isShavuos ||
                jewishCalendar.isRoshHashana ||
                jewishCalendar.isYomKippur ||
                jewishCalendar.isSuccos ||
                jewishCalendar.isShminiAtzeres ||
                jewishCalendar.isSimchasTorah ||
                jewishCalendar.isRoshChodesh
    }
}