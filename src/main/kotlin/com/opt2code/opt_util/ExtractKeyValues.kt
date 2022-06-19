/**
 * Copyright (C) 2022-present opt2code.com
 * All rights reserved.
 */

package com.opt2code.opt_util



fun extractKeyValues(text: String?, del1: String, del2: String, map: LinkedHashMap<String, String> = LinkedHashMap()): MutableMap<String, String> {
    text ?: return map
    text.split(del2).forEachStyle {
        if (!isLast()) {
            val lastIndex = it.lastIndexOf(del1)
            if (lastIndex > -1) {
                val beginExp = lastIndex + del1.length
                val key = it.substring(beginExp).trim()
                val next = next().lastIndexOf(del1)
                val value = if (next > -1) {
                    next().substring(0, next).trim()
                } else {
                    next().substring(0).trim()
                }
                map.put(removeScreening(key), removeScreening(value))
            }
        }
    }
    return map
}