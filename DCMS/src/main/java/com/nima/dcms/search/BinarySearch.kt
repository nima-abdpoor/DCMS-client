package com.nima.dcms.search


class BinarySearch {

    fun findIdFromArray(longArray: LongArray, element: Long): Int {
        return longArray.binarySearch(element)
    }
}