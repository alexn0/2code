/**
 * Copyright (C) 2022-present opt2code.com
 * All rights reserved.
 */

package com.opt2code.opt_util

enum class TagProp(
        override val title: String,
        override val desc: String? = null
        ): TProtocolProp {
    TAG_ID(PropValues::sTagId.name),
    PRIORITY_LEVEL(PropValues::sPriorityLevel.name),
    PREV_SIBLING_ID(PropValues::sPrevSiblingId.name),
    PARENT_TAG_ID(PropValues::sParentTagId.name),
    ONLY_PREV_ELEM_BY_PRIORITY(PropValues::sOnlyPrevElemByPriority.name),
    ONLY_CHILDREN(PropValues::sOnlyChildren.name),
    HIERARCHY_IGNORED(PropValues::sHierarchyIgnored.name),
    TO_TOP_LEVEL(PropValues::sToTopLevel.name),
    TO_SUB_LEVEL(PropValues::sToSubLevel.name),
    TO_THE_SAME_LEVEL(PropValues::sToTheSameLevel.name),
    NO_CHILD(PropValues::sNoChild.name),
    VALUE(PropValues::sValue.name);

    override fun toString(): String {
        return title
    }
}