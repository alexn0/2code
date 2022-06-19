/**
 * Copyright (C) 2022-present opt2code.com
 * All rights reserved.
 */

package com.opt2code.opt_util



data class LProperty(
        val generalName: String,
        var seqNumber: Int? = null
) {
    var decodedProp: String? = null
    var userProp: String? = null
    var propName: String? = null
    var propValue: String? = null
    var regExpValueFormat: String? = null
    var parent: LElement? = null


    fun processProp(lexer: LexerState) {
        decodedProp = (lexer.propTranslation[generalName.trim()] ?: generalName).trim()
        userProp = (lexer.userPropTranslation[decodedProp] ?: decodedProp)!!.trim()
        val i = userProp!!.indexOf("=")
        when (i) {
            -1 -> {
                propName = seqNumber.toString()
                propValue = userProp
            }
            0 -> {
                propName = seqNumber.toString()
                propValue = userProp!!.substring(1).trim()
            }
            else -> {
                propName = userProp!!.substring(0, i).trim()
                propValue = userProp!!.substring(i+1).trim()
            }
        }
        parent?.propData?.map?.put(propName!!, propValue)
    }

}