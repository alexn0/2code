/**
 * Copyright (C) 2022-present opt2code.com
 * All rights reserved.
 */

package com.opt2code.opt_util

import java.util.*



fun LexicalProcess(
        text: String,
        replacement: EnumMap<PRSectionTag, String>? = null,
        toDef: (String) -> PropValues
): List<LexerState> {
    return toFragments(text, replacement).map {
        LexerState(it, toDef)
    }
}


fun <T> LexicalProcess(text: String,
                       toDef: (String) -> PropValues,
                       replacement: EnumMap<PRSectionTag, String>? = null,
                       transform: (LElement) -> T): List<T> {
    return LexicalProcess(text, replacement, toDef).first().hText().map { transform(it)}
}

