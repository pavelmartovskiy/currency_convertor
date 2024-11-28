package com.pm.cc.core

interface Mapper<S, D> {
    fun map(src: S): D
}