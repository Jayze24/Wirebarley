package space.jay.wirebarley.core.common.format

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

fun Double.toPriceFormat(pattern : String = "#,##0.00") : String = DecimalFormat(pattern).format(this)

fun Long.toDataFormat(pattern : String = "yyyy-MM-dd HH:mm") : String = SimpleDateFormat(pattern, Locale.getDefault()).format(Date(this))