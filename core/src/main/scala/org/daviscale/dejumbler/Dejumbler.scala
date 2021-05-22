package org.daviscale.dejumbler

import scala.io.Source

object Dejumbler {

  lazy val wordList: Seq[String] =
    Source.fromResource("american-english-insane")
      .getLines
      .toSeq

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
