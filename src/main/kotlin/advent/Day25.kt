package advent

import java.io.File
import java.io.StringReader
import kotlin.math.absoluteValue

fun main(args: Array<String>) {
  val map = CoordinateMap()
  val input = File("input15.txt")
  for (line in input.readLines()) {
    map.add(parseCoord(line))
  }
  val constellations = map.constellations()
  println("There are ${constellations.size} constellations")
}

fun parseCoord(line: String): Coordinate {
  val parts = line.split(",").map { it.toInt() }
  return Coordinate(parts[0], parts[1], parts[2], parts[3])
}

class CoordinateMap() {
  val map = HashMap<Int, CoordinateMap>()
  val coords = HashMap<Int, Coordinate>()
  val allCoordinates = ArrayList<Coordinate>()

  fun add(coord: Coordinate, level: Int = 0): CoordinateMap {
    if (level == 0) allCoordinates.add(coord)
    if (level >= 3) {
      coords[coord[3]] = coord
      return this
    }

    val subMap = map[coord[level]] ?: CoordinateMap()
    map[coord[level]] = subMap.add(coord, level + 1)
    return this
  }

  fun constellations(): ArrayList<Constellation> {
    val visitedCoordinates = ArrayList<Coordinate>()
    val results = ArrayList<Constellation>()

    for (coord in allCoordinates) {
      if (visitedCoordinates.contains(coord)) continue

      val constellation = buildConstellation(coord)
      if (constellation.isEmpty()) continue

      visitedCoordinates.addAll(constellation.coords)
      results.add(constellation)
    }

    return results
  }

  fun buildConstellation(coord: Coordinate, constellation: Constellation = Constellation()): Constellation {
    if (constellation.contains(coord)) return constellation

    var result = constellation
    result.add(coord)

    val neighbors = immediateNeighbors(coord)
    for (neighbor in neighbors) {
      result = buildConstellation(neighbor, result)
    }
    return result
  }

  fun immediateNeighbors(coord: Coordinate): ArrayList<Coordinate> {
    val neighbors = ArrayList<Coordinate>()
    for (dx in (-3..3)) {
      val maybeX = this[coord.x + dx] ?: continue
      for (dy in ((-3 + dx.absoluteValue)..(3 - dx.absoluteValue))) {
        val maybeY = maybeX[coord.y + dy] ?: continue
        for (dz in ((-3 + (dx.absoluteValue + dy.absoluteValue))..(3 - (dx.absoluteValue + dy.absoluteValue)))) {
          val maybeZ = maybeY[coord.z + dz] ?: continue
          for (dw in ((-3 + (dx.absoluteValue + dy.absoluteValue + dz.absoluteValue))..(3 - (dx.absoluteValue + dy.absoluteValue + dz.absoluteValue)))) {
            val other = maybeZ.getCoordinate(coord.w + dw) ?: continue

            if (coord != other) neighbors.add(other)
          }
        }
      }
    }

    return neighbors
  }

  fun getCoordinate(w: Int): Coordinate? = coords[w]
  operator fun get(index: Int): CoordinateMap? = map[index]
}

class Constellation {
  val coords = arrayListOf<Coordinate>()

  fun addAll(coords: ArrayList<Coordinate>): Constellation {
    for (coord in coords) add(coord)
    return this
  }

  fun add(coord: Coordinate): Constellation {
    if (!coords.contains(coord)) coords.add(coord)

    return this
  }

  fun isEmpty() = coords.isEmpty()
  fun contains(coord: Coordinate) = coords.contains(coord)
}

data class Coordinate(val x: Int, val y: Int, val z: Int, val w: Int) {
  operator fun get(level: Int): Int {
    return when(level) {
      0 -> x
      1 -> y
      2 -> z
      else -> w
    }
  }
}
