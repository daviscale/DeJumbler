package org.daviscale.dejumbler

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._
import scala.io.Source

object Dejumbler {

  lazy val wordList: Seq[String] =
    Source.fromResource("american-english-insane")
      .getLines
      .toSeq
      .filter(_.length > 1)

  def getPermutations(word: String): Seq[String] = {
    word
      .toCharArray
      .toSeq
      .permutations
      .toSeq
      .map(charSeq => charSeqToString(charSeq))
  }

  def charSeqToString(chars: Seq[Char]): String = {
    chars
      .foldLeft[String]("")((acc, char) => acc + char.toString)
  }

  def findCandidates(
    word: String
  )(
    implicit executionContext: ExecutionContext = ExecutionContext.global
  ): Future[Seq[String]] = {
    val futures: Seq[Future[Seq[String]]] = getPermutations(word)
      .map { perm =>
        Future {
          wordList.find(_.equalsIgnoreCase(perm)).toSeq
        }
      }
    Future
      .sequence(futures)
      .map(_.flatten)
  }
}
