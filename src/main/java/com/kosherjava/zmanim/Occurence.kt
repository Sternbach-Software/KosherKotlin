package com.kosherjava.zmanim

import kotlin.math.absoluteValue

data class Occurence<T>(val subject: ZmanType, val calculationMethod: ZmanCalculationMethod<T>) {

    infix fun after(zmanType: ZmanType): ZmanRelationship<T> = ZmanRelationship(
        subject, +calculationMethod, zmanType
    )

    infix fun before(zmanType: ZmanType): ZmanRelationship<T> = ZmanRelationship(
        subject, -calculationMethod, zmanType
    )
    infix fun before(zmanDefinition: ZmanDefinition): ZmanRelationship<T> = ZmanRelationship(
        subject, -calculationMethod, relativeToZman = zmanDefinition
    )

    /**
     * Returns a [ZmanCalculationMethod] with a [ZmanCalculationMethod.value] that is the negative of [this.value].
     * If [this.value] is already negative, it will remain negative, unlike a regular unary minus operator.
     * */
    private operator fun <T> ZmanCalculationMethod<T>.unaryMinus(): ZmanCalculationMethod<T> =
        when (this) {
            is ZmanCalculationMethod.Degrees -> ZmanCalculationMethod.Degrees(-(degrees.absoluteValue))
            is ZmanCalculationMethod.FixedDuration -> ZmanCalculationMethod.FixedDuration(-(duration.absoluteValue))
            is ZmanCalculationMethod.ZmaniyosDuration -> ZmanCalculationMethod.ZmaniyosDuration(-(duration.absoluteValue))
            is ZmanCalculationMethod.FixedMinutesFloat -> ZmanCalculationMethod.FixedMinutesFloat(-(minutes.absoluteValue))
            is ZmanAuthority,
            ZmanCalculationMethod.FixedLocalChatzos,
            ZmanCalculationMethod.Unspecified,
            ZmanCalculationMethod.DayDefinition -> this
        } as ZmanCalculationMethod<T>
    private operator fun <T> ZmanCalculationMethod<T>.unaryPlus(): ZmanCalculationMethod<T> =
        when (this) {
            is ZmanCalculationMethod.Degrees -> ZmanCalculationMethod.Degrees(degrees.absoluteValue)
            is ZmanCalculationMethod.FixedDuration -> ZmanCalculationMethod.FixedDuration(duration.absoluteValue)
            is ZmanCalculationMethod.ZmaniyosDuration -> ZmanCalculationMethod.ZmaniyosDuration(duration.absoluteValue)
            is ZmanCalculationMethod.FixedMinutesFloat -> ZmanCalculationMethod.FixedMinutesFloat(minutes.absoluteValue)
            is ZmanAuthority,
            ZmanCalculationMethod.FixedLocalChatzos,
            ZmanCalculationMethod.Unspecified,
            ZmanCalculationMethod.DayDefinition -> this
        } as ZmanCalculationMethod<T>
}
