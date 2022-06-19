/**
 * Copyright (C) 2022-present opt2code.com
 * All rights reserved.
 */

package com.opt2code.opt_util

import java.lang.StringBuilder

fun removeScreening(text: String): String {
    val res = StringBuilder("")
    var prevStart = 0
    Regex("\\|+").findAll(text).forEach {
        res.append(text.substring(prevStart, it.range.first))
        prevStart = it.range.last + 1
        res.append(it.value.substring(0, it.value.length/2))
    }
    if (prevStart < text.length) {
        res.append(text.substring(prevStart))
    }
    return res.toString()
}