package org.daviscale.dejumbler

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._
import scala.util.Random
import scala.util.chaining._

object DejumblerTimings {

  import Dejumbler._

  def getScrambledWords(numberWords: Int): Seq[String] = {
    // jumble puzzles usually consists of words that are 7 letters or less
    val filteredWordList = wordList.filter(_.length <= 7)
    val wordListSize = filteredWordList.size
    def randomIndex(): Int = {
      Random.between(0, wordListSize)
    }

    (1 to numberWords)
      .map { _ =>
        val word = filteredWordList(randomIndex())
        Random
          .shuffle(word.toCharArray)
          .toSeq
          .pipe(charSeqToString)
      }
  }

  def getDejumbleTime(
    findFn: (Seq[String], String) => Seq[String]
  )(
    scrambledWord: String
  )(
    implicit executionContext: ExecutionContext
  ): Future[Long] = {
    val find = findCandidates(findFn) _
    for {
      start <- Future { System.currentTimeMillis }
      _ <- find(scrambledWord)
    } yield {
      System.currentTimeMillis - start
    }
  }

  def main(args: Array[String]): Unit = {
    val numberWords = args.head.toInt
    val scrambledWords = getScrambledWords(numberWords)
    println(s"Scrambled Words: ${scrambledWords.mkString(", ")}")
    implicit val executionContext = ExecutionContext.global
    val timingFutures = scrambledWords.map(getDejumbleTime((wordList, perm) => wordList.find(_.equalsIgnoreCase(perm)).toSeq))
    val timings = Await.result(Future.sequence(timingFutures), 10.minutes).sorted
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
