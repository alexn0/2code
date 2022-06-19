/**
 * Copyright (C) 2022-present opt2code.com
 * All rights reserved.
 */

package com.opt2code.opt_util


enum class PRSectionTag(
        override val title: String,
        override val prop: PropValues = PropValues(),
        override val desc: String? = null
): TProtocol {
    CONF_START("sConfStart"),
    TAG_TRANSLATION("sTagTranslation"), //for tag translation
    PROP_TRANSLATION("sPropTranslation"), // for translation property
    USER_TAG_TRANSLATION("sUserTagTranslation"),
    USER_PROP_TRANSLATION("sUserPropTranslation"),
    TAG_PROP_DECLARATION("sTagPropDeclaration"), // for user definition
    TAG_DEF("sTagDef"), // for user definition
    DEF_DESC("sDefDesc"),
    CONF_END("sConfEnd");//default sequence

    override fun toString() = title

    operator fun invoke() = title

    companion object {
        private val map: Map<String, PRSectionTag> by lazy { values().associate { Pair(it.title, it) } }

        private const val base_tag = "base"

        fun regex() = values().joinToString("|").let {"\\[\\[(${it})\\]\\]"}

        operator fun invoke(text: String): PRSectionTag? = map[text]
    }
}







