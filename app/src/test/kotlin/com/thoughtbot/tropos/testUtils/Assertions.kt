package com.thoughtbot.tropos.testUtils

import kotlin.test.assertEquals

fun assertStringEquals(expected: CharSequence, actual: CharSequence) =
    assertEquals(expected.toString(), actual.toString())
