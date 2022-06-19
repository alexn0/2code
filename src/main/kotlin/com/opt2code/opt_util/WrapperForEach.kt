/**
 * Copyright (C) 2022-present opt2code.com
 * All rights reserved.
 */

package com.opt2code.opt_util

interface WrapperForEach<T> {
    val it: T

    fun isLast(): Boolean

    fun isFirst(): Boolean

    fun next(): T

    fun ind(): Int
}