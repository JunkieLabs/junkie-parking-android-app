package `in`.junkielabs.parking.components.api.models.reports

import com.squareup.moshi.JsonClass

/**
 * Created by Niraj on 08-12-2021.
 */
@JsonClass(generateAdapter = true)
data class ParamReport (
    var amount:  Double,
    var total:  Int,
    var startTime: Long,
    var wheelers: List<ParamReportWheeler>
        ) {
}