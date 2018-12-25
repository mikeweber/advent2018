package advent

fun main(args: Array<String>) {
  val targetNumber = "110201"
  val targetRegex = "$targetNumber\\d*$".toRegex()
  val elf1 = Elf(0)
  val elf2 = Elf(1)
  val scores = RecipeScores("37")
  var lastCheck = 0
  var lastMatch: MatchResult? = targetRegex.find(scores.scores, lastCheck)

  while (lastMatch == null) {
    scores.addRecipe(elf1.makeRecipe(scores) + elf2.makeRecipe(scores))
    if (scores.size - 5 > 0) lastCheck = scores.size - 5
    lastMatch = targetRegex.find(scores.scores, lastCheck)
    if (lastCheck % 1000 == 0) println("lastCheck @ $lastCheck")
  }
  val results = scores.scores.replace(targetRegex, "")
  println("The scores $targetNumber appeared after ${results.length} recipes")
}

class RecipeScores(var scores: String) {
  val size
    get() = scores.length
  fun addRecipe(score: Int): RecipeScores {
    scores += score.toString()
    return this
  }

  operator fun get(index: Int): Int {
    val boundIndex = index % size
    return scores.substring(boundIndex, boundIndex + 1).toInt()
  }
}

class Elf(var currentIndex: Int = 0) {
  fun makeRecipe(recipes: RecipeScores): Int {
    currentIndex %= recipes.size
    val recipeScore = recipes[currentIndex]
    currentIndex += 1 + recipeScore
    return recipeScore
  }
}
