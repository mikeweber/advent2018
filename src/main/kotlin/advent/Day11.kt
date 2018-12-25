package advent

import kotlinx.coroutines.*

fun main(args: Array<String>) {
  val g = if (args.isNotEmpty()) {
    Grid(args[0].toInt())
  } else {
    Grid(8561)
  }

  val result = g.scanGrid()
  println("Best spot is @ <${result.x},${result.y},${result.square}> with value ${result.value}")
}

class Grid(private val serialNumber: Int) {
  private val cols = Array(300) { Array(300) { 0 } }
  init {
    build()
  }

  private fun build() {
    for (x in 1..300) {
      for (y in 1..300) {
        val rackID = x  + 10
        var powerLevel = rackID * y
        powerLevel += serialNumber
        powerLevel *= rackID
        if (powerLevel < 100) continue

        this[x, y] = (powerLevel / 100).rem(10) - 5
      }
    }
  }

  operator fun set(x: Int, y: Int, value: Int) {
    cols[x - 1][y - 1] = value
  }

  fun scanGrid(): Result {
    val size = 300
    val results = Array(size) { Result(0, 0, 0, 0) }
    val deferments = Array<Deferred<Result>?>(size) { null }
    for (square in 1..size) {
      deferments[square - 1] = GlobalScope.async {
        maxGrid(square)
      }
    }
    runBlocking {
      for ((i, def) in deferments.withIndex()) {
        results[i] = def?.await()!!
        println("Finished $i")
      }
    }

    val sortedResults = results.sortedByDescending { selector(it) }

    return sortedResults.first()
  }

  fun selector(r: Result) = r.value

  fun maxGrid(square: Int): Result {
    var maxX = 0
    var maxY = 0
    var maxValue = 0

    for (x in 0..(300 - square )) {
      for (y in 0..(300 - square)) {
        val neighbors = cols.slice(x..(x + square - 1)).map { rows ->
          rows.slice(y..(y + square - 1))
        }
        val sum = neighbors.flatten().sum()
        if (sum > maxValue) {
          maxX = x
          maxY = y
          maxValue = sum
        }
      }
    }

    return Result(maxX + 1, maxY + 1, maxValue, square)
  }
}

data class Result(val x: Int, val y: Int, val value: Int, val square: Int)
