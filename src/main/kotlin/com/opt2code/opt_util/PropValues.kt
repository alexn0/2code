/**
 * Copyright (C) 2022-present opt2code.com
 * All rights reserved.
 */

package com.opt2code.opt_util

import com.opt2code.core_metadata.core.kName


private const val textLevelInt = 100


class PropValues private constructor(
      val map: MutableMap<String, String?>
) {
    var sTagId: String? by map
    var sPriorityLevel: String? by map
    var sPrevSiblingId: String? by map
    var sParentTagId: String? by map
    var sOnlyPrevElemByPriority: String? by map
    var sOnlyChildren: String? by map
    var sHierarchyIgnored: String? by map
    var sToTopLevel: String? by map
    var sToSubLevel: String? by map
    var sToTheSameLevel: String? by map
    var sToSupLevel: String? by map
    var sNoChild: String? by map
    var sValue: String? by map


    fun priorityLevel(): Int = try {sPriorityLevel?.toInt() } catch (e: Throwable) {
        textLevelInt
    } ?: textLevelInt


    fun onlyPrevElems(): List<String> = sOnlyPrevElemByPriority?.split(",") ?: emptyList()
    fun onlyChildren(): List<String> = sOnlyChildren?.split(",") ?: emptyList()
    fun hierarchyIgnored(): Boolean = sHierarchyIgnored == "true"
    fun toTopLevel(): Boolean = sToTopLevel == "true"
    fun toSubLevel(): Boolean = sToSubLevel == "true"
    fun toSupLevel(): Boolean = sToSupLevel == "true"
    fun toTheSameLevel(): Boolean = sToTheSameLevel == "true"
    fun noChild(): Boolean = sNoChild == "true"


    fun copy(tagId: String? = this.sTagId,
             priorityLevel: String? = this.sPriorityLevel,
             prevSiblingId: String? = this.sPrevSiblingId,
             parentTagId: String? = this.sParentTagId,
             onlyPrevElemByPriority: String? = this.sOnlyPrevElemByPriority,
             onlyChildren: String? = this.sOnlyChildren,
             hierarchyIgnored: String? = this.sHierarchyIgnored,
             toTopLevel: String? = this.sToTopLevel,
             toSubLevel: String? = this.sToSubLevel,
             toTheSameLevel: String? = this.sToTheSameLevel,
             toSupLevel: String? = this.sToSupLevel,
             noChild: String? = this.sNoChild,
             value: String? = this.sValue) =
            invoke(tagId, priorityLevel, prevSiblingId, parentTagId,
                    onlyPrevElemByPriority, onlyChildren, hierarchyIgnored, toTopLevel,
                    toSubLevel, toTheSameLevel, toSupLevel, noChild, value)


    companion object {
        operator fun invoke(tagId: String? = null,
                            priorityLevel: String? = null,
                            prevSiblingId: String? = null,
                            parentTagId: String? = null,
                            onlyPrevElemByPriority: String? = null,
                            onlyChildren: String? = null,
                            hierarchyIgnored: String? = null,
                            toTopLevel: String? = null,
                            toSubLevel: String? = null,
                            toTheSameLevel: String? = null,
                            toSupLevel: String? = null,
                            noChild: String? = null,
                            value: String? = null,
                            map: MutableMap<String, String?> = LinkedHashMap()
        ): PropValues {
            map.putIfAbsent(PropValues::sTagId.name, tagId)
            map.putIfAbsent(kName(PropValues::sPriorityLevel), priorityLevel)
            map.putIfAbsent(PropValues::sPrevSiblingId.name, prevSiblingId)
            map.putIfAbsent(PropValues::sParentTagId.name, parentTagId)
            map.putIfAbsent(kName(PropValues::sOnlyPrevElemByPriority), onlyPrevElemByPriority)
            map.putIfAbsent(kName(PropValues::sOnlyChildren), onlyChildren)
            map.putIfAbsent(kName(PropValues::sHierarchyIgnored), hierarchyIgnored)
            map.putIfAbsent(kName(PropValues::sToTopLevel), toTopLevel)
            map.putIfAbsent(kName(PropValues::sToSubLevel), toSubLevel)
            map.putIfAbsent(kName(PropValues::sToTheSameLevel), toTheSameLevel)
            map.putIfAbsent(kName(PropValues::sToSupLevel), toSupLevel)
            map.putIfAbsent(kName(PropValues::sNoChild), noChild)
            map.putIfAbsent(PropValues::sValue.name, value)
            return PropValues(map)
        }
    }
}