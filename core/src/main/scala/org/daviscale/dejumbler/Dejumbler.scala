package org.daviscale.dejumbler

import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source

object Dejumbler {

  val charsToExclude = Set('\'', 'è')

  lazy val wordList: Seq[String] =
    Source.fromResource("american-english-insane")
      .getLines
      .toSeq
      .filter(word => word.length >= 4 && word.length <= 6)
      .filterNot(word => charsToExclude.exists(word.contains(_)))

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
    findFn: (Seq[String], String) => Seq[String]
  )(
    word: String
  )(
    implicit executionContext: ExecutionContext = ExecutionContext.global
  ): Future[Seq[String]] = {
    val futures: Seq[Future[Seq[String]]] = getPermutations(word)
      .map { perm =>
        Future {
          findFn(wordList, perm)
        }
      }
    Future
      .sequence(futures)
      .map(_.flatten.map(_.toLowerCase))
  }

  /**
   *  An implementation of findCandidates that takes a permutation and searches thru every element
   *  of the word list with Seq.find until a match is found
   */
  def findCandidatesBruteForce(
    word: String
  )(
    implicit executionContext: ExecutionContext = ExecutionContext.global
  ): Future[Seq[String]] = {
    findCandidates((wordList, perm) => wordList.find(_.equalsIgnoreCase(perm)).toSeq)(word)(executionContext)
  }
}
