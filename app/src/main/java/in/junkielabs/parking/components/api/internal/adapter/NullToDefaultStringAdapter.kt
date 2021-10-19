package com.pepstudy.components.api.internal.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson

/**
 * Created by Niraj on 24-05-2021.
 */
@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class NullToDefaultString

class NullToEmptyStringAdapter {


    @ToJson
    fun toJson(@NullToDefaultString value: String?): String? {

        return value
    }

    @FromJson
    @NullToDefaultString
    fun fromJson(@javax.annotation.Nullable data: String?): String {
        return data ?: ""
    }
}
