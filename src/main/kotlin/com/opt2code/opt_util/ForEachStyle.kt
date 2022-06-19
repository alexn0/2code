/**
 * Copyright (C) 2022-present opt2code.com
 * All rights reserved.
 */

package com.opt2code.opt_util

fun <T> Iterable<T>.forEachStyle(x: WrapperForEach<T>.() -> Unit): Unit {
    val itr = this.iterator()
    reiterate(itr, x)
}


private fun <T> reiterate(itr: Iterator<T>, x: WrapperForEach<T>.() -> Unit) {
    if (itr.hasNext()) {
        var i = 0
        val w = WrapperForEachImpl(itr, itr.next(), i, true)
        w.x()
        w.first = false
        while (w.isNextSet || itr.hasNext()) {
            i++
            w.it = if (w.isNextSet) w.next() else itr.next()
            w.isNextSet = false
            w.next = null
            w.index = i
            w.x()
        }
    }
}


fun <T> Sequence<T>.forEachStyle(x: WrapperForEach<T>.() -> Unit): Unit {
    val itr = this.iterator()
    reiterate(itr, x)
}