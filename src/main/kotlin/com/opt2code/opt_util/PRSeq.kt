/**
 * Copyright (C) 2022-present opt2code.com
 * All rights reserved.
 */

package com.opt2code.opt_util

enum class PRSeq(val title: String, val s: String) {
    OB("[[", "\\[\\["), CB("]]", "\\]\\]"),
    LR("][", "\\]\\["), RE("]=", "\\]=");


    override fun toString() = title

    operator fun invoke() = title
}