/**
 * Copyright (C) 2022-present opt2code.com
 * All rights reserved.
 */

package com.opt2code.opt_util

import java.lang.StringBuilder
import java.util.*
import kotlin.collections.LinkedHashMap

class TextBySection(replacement: EnumMap<PRSectionTag, String>? = null,
                    private val map: MutableMap<String, String> = LinkedHashMap()){

    init {
        replacement?.forEach { t, u ->
            map.put(t.title, u)
        }
    }


    fun update(key: PRSectionTag, value: String, force: Boolean = false) {
        if (force) {
            map.put(key.title, value)
        } else {
            map.putIfAbsent(key.title, value)
        }
    }


    fun get(key: PRSectionTag): String? { return map[key.title] }


    fun updateBody(value: String) { map.put("body", value) }


    fun getBody() = map["body"]


    fun print(withTag: Boolean = true, removeScreenSymbol: Boolean = false): String {
        val tbs = this
        val res = StringBuilder("").apply {
            tbs.getBody()?.let {
                append(it)
            }
            PRSectionTag.values().forEach {
                val sc = it
                tbs.get(sc)?.let {
                    if (withTag) {
                        append("[[${sc}]]")
                    }
                    append(it)
                }
            }
        }

        return res.toString().let {
            if (removeScreenSymbol){
                removeScreening(it)
            } else {
                it
            }
        }
    }


    override fun toString(): String = print()

}