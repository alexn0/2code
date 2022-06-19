/**
 * Copyright (C) 2022-present opt2code.com
 * All rights reserved.
 */

package com.opt2code.opt_util

import java.util.*


fun toFragments(text: String, replacement: EnumMap<PRSectionTag, String>? = null): List<TextBySection> {
    val res = ArrayList<TextBySection>()

    var curr  = TextBySection(replacement)
    var currState: PRSectionTag? = null
    var prevStart = 0
    var backupPrevStart: Int? = null

    Regex(PRSectionTag.regex()).findAll(text).forEach {

        val range = it.range
        var nextState = PRSectionTag(it.groups[1]!!.value)!!
        fun sub() = text.substring(prevStart, range.first)

        when (currState) {
            null -> { curr.updateBody(sub()) }
            PRSectionTag.CONF_END -> {
                if (nextState == PRSectionTag.CONF_START) {
                    curr.update(currState!!, "")
                    res.add(curr)
                    curr = TextBySection(replacement)
                    curr.updateBody(sub())
                    backupPrevStart = null
                } else {
                    if (backupPrevStart == null) { backupPrevStart = prevStart }
                    nextState = PRSectionTag.CONF_END
                }
            }
            else -> { curr.update(currState!!, sub()) }

        }

        prevStart = range.last + 1
        currState = nextState
    }

    if (backupPrevStart != null){ prevStart = backupPrevStart!! }

    if (currState == null){
        curr.updateBody(text)
    } else {
        curr.update(currState!!, if (prevStart<text.length) text.substring(prevStart) else "")
    }

    res.add(curr)
    return res
}