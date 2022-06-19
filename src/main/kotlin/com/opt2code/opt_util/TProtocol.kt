/**
 * Copyright (C) 2022-present opt2code.com
 * All rights reserved.
 */

package com.opt2code.opt_util

interface TProtocol: TProtocolProp {
    val prop: PropValues

    companion object {
        val default = PropValues()
    }
}

