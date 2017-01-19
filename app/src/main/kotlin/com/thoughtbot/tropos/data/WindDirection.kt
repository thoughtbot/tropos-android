package com.thoughtbot.tropos.data

import com.thoughtbot.tropos.R
import com.thoughtbot.tropos.data.WindDirection.EAST
import com.thoughtbot.tropos.data.WindDirection.NONE
import com.thoughtbot.tropos.data.WindDirection.NORTH
import com.thoughtbot.tropos.data.WindDirection.NORTH_EAST
import com.thoughtbot.tropos.data.WindDirection.NORTH_WEST
import com.thoughtbot.tropos.data.WindDirection.SOUTH
import com.thoughtbot.tropos.data.WindDirection.SOUTH_EAST
import com.thoughtbot.tropos.data.WindDirection.SOUTH_WEST
import com.thoughtbot.tropos.data.WindDirection.WEST

enum class WindDirection {
  NONE, NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST;
}

fun WindDirection.labelResId(): Int {
  return when (this) {
    NORTH -> R.string.north_abbrev
    NORTH_EAST -> R.string.north_east_abbrev
    EAST -> R.string.east_abbrev
    SOUTH_EAST -> R.string.south_east_abbrev
    SOUTH -> R.string.south_abbrev
    SOUTH_WEST -> R.string.south_west_abrrev
    WEST -> R.string.west_abbrev
    NORTH_WEST -> R.string.north_west_abbrev
    NONE -> return R.string.no_direction
  }

}

fun WindDirection(windBearing: Double): WindDirection {
  return when (windBearing) {
    in 0.0..22.5 -> NORTH
    in 22.5..67.5 -> NORTH_EAST
    in 67.5..112.5 -> EAST
    in 112.5..157.5 -> SOUTH_EAST
    in 157.5..202.5 -> SOUTH
    in 202.5..247.5 -> SOUTH_WEST
    in 247.5..292.5 -> WEST
    in 292.5..337.5 -> NORTH_WEST
    else -> return NONE
  }
}
