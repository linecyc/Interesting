package com.linecy.interesting.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by linecy.
 */
@Parcelize
class ListData(val header: List<String>, val item: List<ListItem>) : Parcelable