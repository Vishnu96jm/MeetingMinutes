package com.xome.meetingminutes.utils

sealed class SortOption(val name: String)

object ByList : SortOption("ByList")

object ByGrid : SortOption("ByGrid")

fun getSortOptionFromName(name: String): SortOption = when (name) {
    "ByList" -> ByList
    "ByGrid" -> ByGrid
    else -> ByList
}