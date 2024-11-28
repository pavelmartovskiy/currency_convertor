package com.pm.cc.domain

import java.util.Date

data class Rate(
    val id: Long,
    val date: Date,
    val src: Currency,
    val dst: Currency,
    val rate: Double

)
