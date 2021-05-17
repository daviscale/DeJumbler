package org.daviscale.dejumbler

object Dejumbler {

  def getPermutations(word: String): Set[String] = {
    word
      .toCharArray
      .toSeq
      .permutations
      .toSeq
      .map(charSeq => charSeq.foldLeft[String]("")((acc, char) => acc + char.toString))
      .toSet[String]
  }

}
