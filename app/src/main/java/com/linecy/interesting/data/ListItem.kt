package com.linecy.interesting.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by linecy.
 */
@Parcelize
data class ListItem(val url: String?, val name: String, val detail: String) : Parcelable