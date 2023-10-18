package com.kosherjava.zmanim

import com.kosherjava.zmanim.metadata.ZmanAuthority
import com.kosherjava.zmanim.metadata.ZmanCalculationMethod
import com.kosherjava.zmanim.metadata.ZmanDefinition
import com.kosherjava.zmanim.metadata.ZmanType

class ZmanDescriptionFormatter {
    fun formatShortDescription(zman: Zman<*>, includeElevationDescription: Boolean): String {
        val result = StringBuilder()
//        "Alos-Tzais - 19.8˚ - affected by elevation"
        val rules = zman.rules
        addShortDayDefinition(rules.mainCalculationMethodUsed as? ZmanCalculationMethod.DayDefinition, result, rules, includeElevationDescription)
        /*if (rules.zmanToCalcMethodUsed == null && rules.mainCalculationMethodUsed == null) {
            addShortDayDefinition(rules.definitionOfDayUsed!!, result, rules)
        } else if (rules.mainCalculationMethodUsed != null && rules.zmanToCalcMethodUsed == null) {
            if (rules.definitionOfDayUsed != null) {
                addShortDayDefinition(rules.definitionOfDayUsed, result, rules)
            }
        }*/
        return result.toString()
    }

    private fun addShortDayDefinition(
        mainCalculationMethodUsed: ZmanCalculationMethod.DayDefinition?,
        result: StringBuilder,
        rules: ZmanDefinition,
        includeElevationDescription: Boolean
    ) {
        /*
        *
        *
        val shaahZmanisAteretTorah: Zman.ValueBased
        get() = Zman.ValueBased(
            ZmanType.SHAA_ZMANIS,
            getTemporalHour(alos72Zmanis.momentOfOccurrence, tzaisAteretTorah.momentOfOccurrence).milliseconds,
            ZmanDefinition(
                ZmanAuthority.AteretTorah,
                mapOf(
                    ZmanType.ALOS to ZmanCalculationMethod.ZmaniyosDuration._72,
                    ZmanType.TZAIS to ZmanAuthority.AteretTorah,
                ),
                ZmanDefinition.UsesElevation.IF_SET
            )
        )
    val shaahZmanisAlos16Point1ToTzais3Point8: Zman.ValueBased
        get() = Zman.ValueBased(
            ZmanType.SHAA_ZMANIS,
            getTemporalHour(
                alos16Point1Degrees.momentOfOccurrence,
                tzaisGeonim3Point8Degrees.momentOfOccurrence
            ).milliseconds,
            ZmanDefinition(
                ZmanAuthority.AHAVAT_SHALOM,
                mapOf(
                    ZmanType.ALOS to ZmanCalculationMethod.Degrees._16_1,
                    ZmanType.TZAIS to ZmanCalculationMethod.Degrees._3_8,
                ),
                ZmanDefinition.UsesElevation.ALWAYS
            )
        )
        * */
        val startZman = mainCalculationMethodUsed?.dayStart?.type
        val endZman = mainCalculationMethodUsed?.dayEnd?.type
        val mainCalculationMethod =
            {
                if (rules.mainCalculationMethodUsed != null) "${if (result.isNotBlank()) " - " else ""}${rules.mainCalculationMethodUsed.valueToString()}"
                else null
            }

        result.apply {
            if (startZman != null && endZman != null) {
                append(startZman.friendlyNameEnglish)
                val startCalcMethod = mainCalculationMethodUsed.dayStart.mainCalculationMethodUsed
                if (startCalcMethod != null && startCalcMethod != ZmanCalculationMethod.Unspecified) {
                    append("(")
                    append(startCalcMethod.valueToString())
                    append(")")
                }
                append("-")
                append(endZman.friendlyNameEnglish)
                val endCalcMethod = mainCalculationMethodUsed.dayEnd.mainCalculationMethodUsed
                if (endCalcMethod != null && endCalcMethod != ZmanCalculationMethod.Unspecified) {
                    append("(")
                    append(endCalcMethod.valueToString())
                    append(")")
                }
            }
            val string = mainCalculationMethod()
            if (string != null) append(string)
            if (includeElevationDescription) {
                append(" - ")
                append(
                    when (rules.isElevationUsed) {
                        ZmanDefinition.UsesElevation.IF_SET -> "affected by elevation if set"
                        ZmanDefinition.UsesElevation.NEVER -> "unaffected by elevation"
                        ZmanDefinition.UsesElevation.ALWAYS -> "affected by elevation"
                        ZmanDefinition.UsesElevation.UNSPECIFIED -> "affect by elevation unspecified"
                    }
                )
            }
        }
    }

    fun formatLongDescription(zman: Zman<*>): String {
        val result = StringBuilder()
        if (zman is Zman.DateBased) {
            val rules = zman.rules
            if (rules.mainCalculationMethodUsed != null) {
                if (rules.mainCalculationMethodUsed is ZmanCalculationMethod.DayDefinition) {
                    val startZman = rules.mainCalculationMethodUsed.dayStart.type
                    val endZman = rules.mainCalculationMethodUsed.dayEnd.type
                    result.appendLine("Day starts at ${startZman?.friendlyNameEnglish} and ends at ${endZman?.friendlyNameEnglish}")
                    result.appendLine("${startZman?.friendlyNameEnglish} is defined as")
                    if(rules.mainCalculationMethodUsed.dayStart.mainCalculationMethodUsed != null) {
                        result.append(rules.mainCalculationMethodUsed.dayStart.mainCalculationMethodUsed.format())
                    }else {
                        result.append(rules.mainCalculationMethodUsed.format(startZman.friendlyNameEnglish, endZman.friendlyNameEnglish))
                    }
                    /*
                    getTemporalHour(
                        alos19Point8Degrees.momentOfOccurrence,
                        tzais19Point8Degrees.momentOfOccurrence
                    ).milliseconds,
                    ZmanDefinition(
                        ZmanCalculationMethod.Degrees._19_8,
                        null,
                        ZmanDefinition.UsesElevation.ALWAYS,
                        ZmanDefinition.DayDefinition.DAWN_TO_DUSK,
                    )
                ) //Long description:
                  // "Day is defined as the time between Alos and Tzais.
                  // "Alos is defined as when the sun is 19.8˚ below the eastern geometric horizon and Tzais when it is 19.8˚ below the western geometric horizon."
                  //Short description:
                  // "Alos-Tzais - 19.8˚ - affected by elevation"
                */
                    when (rules.mainCalculationMethodUsed) {
                        ZmanCalculationMethod.Unspecified -> {

                        }
                        is ZmanCalculationMethod.Degrees -> {

                        }
                        is ZmanCalculationMethod.FixedDuration -> {
//                            result.append(rules.mainCalculationMethodUsed.format(zman.type.friendlyNameEnglish))
                        }
                        ZmanCalculationMethod.FixedLocalChatzos -> {

                        }
                        is ZmanCalculationMethod.FixedMinutesFloat -> {

                        }
                        is ZmanAuthority -> {

                        }
                        is ZmanCalculationMethod.ZmaniyosDuration -> {

                        }
                    }
                    result.append(rules.mainCalculationMethodUsed.format())
                }
            }
        }/* else {
            zman as Zman.ValueBased

            result.appendLine("Day is defined as when the sun is ")
        }*/
        if(zman.rules.supportingAuthorities.isNotEmpty()) {
            result.append("Supporting authorities: ")
            for(authority in zman.rules.supportingAuthorities) {
                result.append(authority.name)
                result.append(", ")
            }
        }
        when (zman.rules.isElevationUsed) {
            ZmanDefinition.UsesElevation.IF_SET -> result.append("- affected by elevation if set")
            ZmanDefinition.UsesElevation.NEVER -> result.append("- unaffected by elevation")
            ZmanDefinition.UsesElevation.ALWAYS -> result.append("- affected by elevation")
            ZmanDefinition.UsesElevation.UNSPECIFIED -> {}
        }
        return result.toString()
    }

    private fun tryAddDescription(start: ZmanDefinition?, result: StringBuilder) {
        val startPrefix = "Day starts at "
        val endPrefix = "and ends at"
        val tryAddDescription = { zman: ZmanType, string: StringBuilder, prefix: String ->
            //start?.type?.equals(zman) ?: null
            val startCalculationMethod = start?.mainCalculationMethodUsed
            val notNuln = startCalculationMethod != null
            if (notNuln) {
                string.append("$startPrefix${zman.friendlyNameEnglish} ${startCalculationMethod?.format()}")
            }
            notNuln
        }
        var startZman = ZmanType.ALOS
        var endZman = ZmanType.TZAIS

        if (!tryAddDescription(startZman, result, startPrefix)) {
            startZman = ZmanType.HANAITZ
            if (!tryAddDescription(startZman, result, startPrefix)) {
                startZman = ZmanType.CHATZOS_HAYOM
                tryAddDescription(startZman, result, startPrefix)
            }
        }
        if (!tryAddDescription(endZman, result, endPrefix)) {
            endZman = ZmanType.SHKIAH
            tryAddDescription(endZman, result, endPrefix)
        }
    }
    companion object {
        fun shortDescriptionAteretTorah(minuteOffset: Double) = "${ZmanAuthority.Strings.ATERET_TORAH} (${if(minuteOffset % 1 == 0.0) minuteOffset.toInt()/*don't add .0*/ else minuteOffset} minutes)"
    }
}