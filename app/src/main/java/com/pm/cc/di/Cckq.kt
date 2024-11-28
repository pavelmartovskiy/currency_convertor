package com.pm.cc.di

import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.definition.Definition
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.named
import org.koin.mp.KoinPlatformTools

interface CcComponent : KoinComponent

class Cckq<T>(name: String) { // Currency Convertor Koin Qualifier
    val koinQualifier = named(name = name)
}

inline fun <reified T : Any> CcComponent.inject(
    qualifier: Cckq<T>? = null,
    mode: LazyThreadSafetyMode = KoinPlatformTools.defaultLazyMode(),
    noinline parameters: ParametersDefinition? = null,
): Lazy<T> = inject<T>(qualifier = qualifier?.koinQualifier, mode = mode, parameters = parameters)

inline fun <reified T : Any> CcComponent.get(
    qualifier: Cckq<T>? = null,
    noinline parameters: ParametersDefinition? = null,
): T = get(qualifier = qualifier?.koinQualifier, parameters = parameters)

inline fun <reified T : Any> q(name: String) = Cckq<T>(name = name)

inline fun <reified T> Module.single(
    qualifier: Cckq<T>,
    createdAtStart: Boolean = false,
    noinline definition: Definition<T>,
): KoinDefinition<T> = single(
    qualifier = qualifier.koinQualifier,
    createdAtStart = createdAtStart,
    definition = definition
)

inline fun <reified T> Module.factory(
    qualifier: Cckq<T>,
    noinline definition: Definition<T>,
): KoinDefinition<T> = factory(
    qualifier = qualifier.koinQualifier,
    definition = definition
)
