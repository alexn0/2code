/**
 * Copyright (C) 2022-present opt2code.com
 * All rights reserved.
 */

package com.opt2code.opt_util

data class LexerState(
        val textBySection: TextBySection,
        val prefix: String?,
        var pText: List<LElement>?,
        val userTags: LinkedHashMap<String, String> = LinkedHashMap(),
        val tagTranslation: LinkedHashMap<String, String> = LinkedHashMap(),
        val propTranslation: LinkedHashMap<String, String> = LinkedHashMap(),
        val userTagTranslation: LinkedHashMap<String, String> = LinkedHashMap(),
        val userPropTranslation: LinkedHashMap<String, String> = LinkedHashMap(),
        val tagDef: LinkedHashMap<String, String> = LinkedHashMap(),
        val dirtyState: DirtyState = DirtyState(),
        var hText: List<LElement>?
) {


    var prevNotIgnoredElem: LElement? = null


    fun hText(): MutableList<LElement> = hText as MutableList<LElement>


    fun pText(): MutableList<LElement> = pText as MutableList<LElement>


    companion object {

        operator fun invoke(text: TextBySection, toDef: (String) -> PropValues): LexerState {

            val pText = text.getBody()?.let { preProcessBody(it) }

            val lexer = LexerState(text, pText?.get(0)?.opValue, pText, hText = null)

            extractKeyValues(text.get(PRSectionTag.TAG_TRANSLATION), PRSeq.OB(), PRSeq.RE(), lexer.tagTranslation)
            extractKeyValues(text.get(PRSectionTag.PROP_TRANSLATION), PRSeq.LR(), PRSeq.RE(), lexer.propTranslation)
            extractKeyValues(text.get(PRSectionTag.USER_TAG_TRANSLATION), PRSeq.OB(), PRSeq.RE(), lexer.userTagTranslation)
            extractKeyValues(text.get(PRSectionTag.USER_PROP_TRANSLATION), PRSeq.LR(), PRSeq.RE(), lexer.userPropTranslation)
            extractKeyValues(text.get(PRSectionTag.TAG_DEF), PRSeq.OB(), PRSeq.RE(), lexer.tagDef)

            val userToDef: (String) -> PropValues? = extractToDef(text.get(PRSectionTag.TAG_PROP_DECLARATION))

            val fToUser: (String) -> PropValues = { it ->
                var res = userToDef(it)
                if (res == null) {
                    res = toDef(it)
                }
                res
            }

            lexer.hText = lexer.postProcess(fToUser)

            return lexer
        }


        private fun preProcessBody(text: String): List<LElement> {
            val start = PRSeq.OB()
            val end = PRSeq.CB()

            val lElems = ArrayList<LElement>()
            var curr = LElement()
            lElems.add(curr)

            text.split(end).forEachStyle{
                val index = it.indexOf(start)
                when  {
                    index == -1 || isLast() -> {
                        curr.addValue(it)
                    }
                    index == 0 -> {
                        curr = LElement(it.substring(start.length))
                        lElems.add(curr)
                    }
                    else -> {
                        curr.addValue(it.substring(0, index))
                        curr = LElement(it.substring(index + start.length))
                        lElems.add(curr)
                    }
                }
            }
            return lElems

        }


        private fun LexerState.postProcess(toDef: (String) -> PropValues): List<LElement>? {
            val lexer = this
            val res = ArrayList<LElement>()
            pText?.forEachStyle {
                val elem: LElement = it
                elem.seqNumber = this.ind()
                elem.generalName?.let {
                    val x = it.split(PRSeq.LR())
                    x.forEachStyle {
                        val it = this.it
                        if (isFirst()) {
                            elem.processElem(lexer, it)
                        } else {
                            val prop = LProperty(it, this.ind())
                            elem.props.add(prop)
                            prop.parent = elem
                            prop.processProp(lexer)
                        }
                    }
                }
                elem.postProcessElem(lexer, toDef)
                if (elem.hLevel() == 0) {
                    res.add(elem)
                }
                if (!elem.propData.hierarchyIgnored()) {
                    lexer.prevNotIgnoredElem = elem
                }
            }
            return res
        }


        private fun extractToDef(text: String?): (String) -> PropValues? {
            val input = LinkedHashMap<String, String>()
            extractKeyValues(text, PRSeq.OB(), PRSeq.CB(), input)
            val r = LinkedHashMap<String, PropValues>()
            input.keys.forEachStyle {
                val x = it.split(PRSeq.LR())
                var name = ""
                val map = HashMap<String,  String?>()
                x.forEachStyle {
                    val it = this.it.trim()
                    if (isFirst()) {
                        name = it
                    } else {
                        val i = it.indexOf("=")
                        when (i) {
                            -1 -> {
                                map.put(this.ind().toString(), it)
                            }
                            0 -> {
                                map.put(this.ind().toString(), it.substring(1).trim())
                            }
                            else -> {
                                map.put(it.substring(0, i).trim(), it.substring(i+1).trim())
                            }
                        }
                    }
                }
                r.putIfAbsent(name, PropValues(map = map))
            }
            return {
                r[it]
            }
        }

    }


}


