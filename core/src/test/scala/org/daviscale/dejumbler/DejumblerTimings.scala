package org.daviscale.dejumbler

import scala.util.Random
import scala.util.chaining._

object DejumblerTimings {

  import Dejumbler._

  def getScrambledWords(numberWords: Int): Seq[String] = {
    val wordListSize = wordList.size
    def randomIndex(): Int = {
      Random.between(0, wordListSize)
    }

    (1 to numberWords)
      .map { _ =>
        val word = wordList(randomIndex())
        Random
          .shuffle(word.toCharArray)
          .toSeq
          .pipe(charSeqToString)
      }
  }

  def main(args: Array[String]): Unit = {

  }

}
