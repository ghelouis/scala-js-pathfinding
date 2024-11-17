import scala.collection.mutable.Queue as MutableQueue

trait Algo:

  def getName: String

  def init(): Unit

  def iterate(): (Option[Pos], Boolean)

  def getShortestPath: MutableQueue[Pos]
