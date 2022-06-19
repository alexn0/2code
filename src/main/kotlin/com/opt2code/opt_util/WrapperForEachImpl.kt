/**
 * Copyright (C) 2022-present opt2code.com
 * All rights reserved.
 */

package com.opt2code.opt_util

data class WrapperForEachImpl<T>(private val iterator: Iterator<T>,
                                 override var it: T,
                                 var index: Int,
                                 var first: Boolean = true,
                                 var next: T? = null,
                                 var isNextSet: Boolean = false
): WrapperForEach<T> {
    override fun isLast() = !(iterator.hasNext() || isNextSet)

    override fun next(): T {
        if (!isNextSet){
            next = iterator.next()
            isNextSet = true
        }
        return next as T
    }

    override fun ind(): Int = index

    override fun isFirst() = first

}