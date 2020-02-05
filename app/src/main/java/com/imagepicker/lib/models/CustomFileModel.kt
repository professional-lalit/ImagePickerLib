package com.imagepicker.lib.models

import java.io.File

/**
 * This file is created by Lalit N. Hajare on 1/30/2020.
 */
class CustomFileModel(parent: File?, child: String) : File(parent, child) {
    var isSelected: Boolean = false

    override fun equals(other: Any?): Boolean {
        return this.name == (other as CustomFileModel).name
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + isSelected.hashCode()
        return result
    }
}