package com.tuanha.adapter.entities

import androidx.annotation.Keep

@Keep
interface ViewItem {

    fun areItemsTheSame(): List<Any>

    fun getContentsCompare(): List<Pair<Any, String>> = listOf()
}