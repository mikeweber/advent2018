package advent

import java.io.StringReader

fun main(args: Array<String>) {

}

interface MapLocation {
  val x: Int
  val y: Int

  fun isPassable(): Boolean
}

data class Wall(override val x: Int, override val y: Int): MapLocation {
  override fun isPassable() = false
}

data class Opening(override val x: Int, override val y: Int): MapLocation {
  override fun isPassable() = true
}

class Map(lines: StringReader) {
  init {
    for (line in lines.readLines()) {
      for (char in line.chars()) {

      }
    }
  }
}

interface Creature {
  var health: Int
  var power: Int
}

class BattleElf(override var health: Int = 200, override var power: Int = 3): Creature

class Goblin(override var health: Int = 200, override var power: Int = 3): Creature
