package com.pm.ce.domain

import com.pm.ce.core.CurrencyCode


data class Rates(val data: Map<CurrencyCode, Map<CurrencyCode, Double>>)
