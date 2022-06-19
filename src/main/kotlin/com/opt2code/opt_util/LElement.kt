/**
 * Copyright (C) 2022-present opt2code.com
 * All rights reserved.
 */

package com.opt2code.opt_util

import java.lang.StringBuilder

data class LElement(
        val generalName: String? = null,
        var opValue: String = ""
) {
    var encTagNameTrim: String? = null
    var encodedTagName: String? = null
    var decodedTagName: String? = null
    var tagName: String? = null
    val kids: MutableList<LElement> by lazy { ArrayList() }
    val props: MutableList<LProperty> by lazy { ArrayList()}
    val propData: PropValues = PropValues()
    var parent: LElement? = null


    var hLevel: Int? = null

    fun hLevel() = hLevel!!


    var pLevel: Int? = null

    fun pLevel() = pLevel!!


    var seqNumber: Int? = null

    fun seqNumber() = seqNumber!!


    fun opValue() = opValue.let {
        val res = StringBuilder("")
        var hasPattern = false
        Regex("\\|+").findAll(it).forEachStyle {

            val range = this.it.range
            if (this.isFirst()) {
                hasPattern = true
                if (range.first > 0) {
                    res.append(it.subSequence(0, range.first))
                }
            }

            var size = (range.last + 1 - range.first)/2
            while (size > 0) {
                res.append("|")
                size--
            }

            if (!this.isLast()) {
                res.append(it.substring(range.last + 1, next().range.first))
            } else if (range.last + 1 < it.length) {
                res.append(it.substring(range.last + 1))
            }

        }
        if (hasPattern) {
            res.toString()
        } else {
            it
        }
    }


    fun prev(lexer: LexerState): LElement? = if (seqNumber() > 0) lexer.pText()[seqNumber()-1] else null


    fun addChild(elem: LElement) {
        kids.add(elem)
        elem.parent = this
    }


    fun processElem(lexer: LexerState, encTagName: String) {
        if (generalName != null) {
            encTagNameTrim = encTagName.trim()
            encodedTagName = if (lexer.tagTranslation.contains(encTagNameTrim)) encTagNameTrim else null
            decodedTagName = encodedTagName?.let{lexer.tagTranslation[it]} ?: encTagNameTrim
            tagName = lexer.userTagTranslation[decodedTagName] ?: decodedTagName
        }
    }


    fun postProcessElem(lexer: LexerState, toDef: (String) -> PropValues) {
        when(seqNumber!!) {
            0 -> {
                hLevel = 0
                pLevel = tagName?.let{ toDef(it).priorityLevel() } ?: 0
            }
            else -> {
                val notSet = toDef(tagName!!).map.filter { it.value != null && propData.map[it.key] == null }
                propData.map.putAll(notSet)

                val prevElem = lexer.pText()[seqNumber() -1]
                defineLevel(prevElem, toDef, lexer)
            }
        }

        val tagId = propData.sTagId

        tagId?.let {
            lexer.dirtyState.tagIdToLElement.put(it, this)
        }
    }


    private fun defineLevel(prevElem: LElement, toDef: (String) -> PropValues, lexer: LexerState) {
        if (prevElem.tagName == null) {
            hLevel = 0
            pLevel = tagName?.let { toDef(it).priorityLevel() } ?: 0
        } else {
            val parent = propData.sParentTagId?.let { lexer.dirtyState.tagIdToLElement[it] }
            val neighbor = propData.sPrevSiblingId?.let { lexer.dirtyState.tagIdToLElement[it] }
            val currPLevel = propData.priorityLevel().let { if (it > -1) it else toDef(tagName!!).priorityLevel() }.also {
                pLevel = it
            }
            when {
                parent != null -> {
                    hLevel = parent.hLevel() + 1
                    parent.addChild(this)
                }

                neighbor != null -> {
                    hLevel = neighbor.hLevel()
                    neighbor.parent?.addChild(this)
                }

                propData.toTopLevel() -> { hLevel = 0   }

                prevElem.propData.noChild()  -> { hLevel = 0 }

                prevElem.propData.onlyChildren().let {
                    it.isNotEmpty() && !it.contains(tagName()) }  -> { hLevel = 0 }

                propData.onlyPrevElems()
                        .let { it.isNotEmpty() && !it.contains(prevElem.tagName()) } -> { hLevel = 0 }

                else -> {

                    val prevElemVar: LElement? = lexer.prevNotIgnoredElem

                    if (prevElemVar == null) {
                        hLevel = 0
                    } else {
                        val prevPLevelVar = prevElemVar.pLevel()
                        val prevHLevel = prevElemVar.hLevel()
                        when {
                            propData.toSubLevel() -> {
                                hLevel = prevHLevel + 1
                                prevElemVar.addChild(this)
                            }

                            propData.toTheSameLevel() -> {
                                hLevel = prevHLevel
                                prevElemVar.parent?.addChild(this)
                            }

                            propData.toSupLevel() -> {
                                val prt = prevElemVar.parent
                                if (prt != null) {
                                    updateByPLevel(prt.pLevel(), currPLevel, prt)
                                } else {
                                    hLevel = 0
                                }
                            }

                            prevPLevelVar < currPLevel -> {
                                hLevel = prevHLevel + 1
                                prevElemVar.addChild(this)
                            }

                            prevPLevelVar == currPLevel -> {
                                hLevel = prevHLevel
                                prevElemVar.parent?.addChild(this)
                            }

                            else -> {
                                updateByPLevel(prevPLevelVar, currPLevel, prevElemVar)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun updateByPLevel(prevPLevelVr: Int, currPLevel: Int, prevElemVr: LElement?) {
        var prevPLevelVar = prevPLevelVr
        var prevElemVar = prevElemVr
        while (prevPLevelVar > currPLevel) {
            prevElemVar = prevElemVar!!.parent

            if (prevElemVar != null) {
                prevPLevelVar = prevElemVar.pLevel()
            } else {
                break
            }
        }

        when {
            prevElemVar == null ->  hLevel = 0

            prevPLevelVar == currPLevel ->  {
                hLevel = prevElemVar.hLevel()
                prevElemVar.parent?.addChild(this)
            }

            else -> {
                hLevel = prevElemVar.hLevel() + 1
                prevElemVar.addChild(this)
            }
        }
    }


    fun tagName() = tagName ?: decodedTagName ?: encodedTagName ?: generalName


    fun addValue(inc: String) {
        opValue += inc
    }


}