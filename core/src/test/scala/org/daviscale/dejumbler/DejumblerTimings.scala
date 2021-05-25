package org.daviscale.dejumbler

import java.util.concurrent.Executors

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

  def getDejumbleTime(
    scrambledWord: String
  ): Long = {
     val start = System.currentTimeMillis
     findCandidatesSync(scrambledWord)
     System.currentTimeMillis - start
  }

  def main(args: Array[String]): Unit = {
    val numberWords = args.head.toInt
    val scrambledWords = getScrambledWords(numberWords)
    val timings = scrambledWords.map(getDejumbleTime)
    println(s"For sample size of $numberWords")
    println(s"Average: ${average(timings)}")
    println(s"Min: ${timings.head}")
    (5 to 95)
      .filter(_ % 5 == 0)
      .foreach(tile => println(s"$tile-ile: ${percentile(timings, tile)}"))
    println(s"Max: ${timings.last}")
  }

  def average(timings: Seq[Long]): Long = {
    val sum = timings.reduce(_ + _)
    sum / timings.size.toLong
  }

  def percentile(
    sortedTimings: Seq[Long],
    tile: Int
  ):Long = {
    val index = math.ceil((sortedTimings.length - 1) * (tile / 100.0)).toInt
    sortedTimings(index)
  }

}
