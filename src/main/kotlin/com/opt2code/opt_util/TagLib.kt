/**
 * Copyright (C) 2022-present opt2code.com
 * All rights reserved.
 */

package com.opt2code.opt_util

import java.util.*
import java.util.concurrent.ConcurrentHashMap


private val map: MutableMap<String, List<Any?>> = ConcurrentHashMap()

interface TagLib<T: TProtocol, S> {
    fun nameToTag(s: String): T?

    fun tagToValue(t: LElement): S

    fun toProp(s: String): PropValues = nameToTag(s)?.prop ?: default

    fun tags(): List<T>

    fun lexicalProcess(text: String, replacement: EnumMap<PRSectionTag, String>? = null): List<LexerState> {
        return toFragments(text, replacement).map {
            LexerState(it) { toProp(it) }
        }
    }

    fun doLexicalProcess(text: String, replacement: EnumMap<PRSectionTag, String>? = null, cacheName: String? = null): List<S> {
        if (cacheName != null ) {
            val rr = map[cacheName]
            if (rr != null)
            return rr as List<S>
        }
        val res: List<S> =  lexicalProcess(text, replacement).flatMap { it.hText() }.map { tagToValue(it)}
        if (cacheName != null) {
            map.put(cacheName, res)
        }
        return res
    }


    companion object {
        private val default = PropValues()
    }
}