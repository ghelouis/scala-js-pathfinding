import scala.collection.mutable.Map as MutableMap
import scala.collection.mutable.Set as MutableSet
import scala.collection.mutable.Queue as MutableQueue

class NoPathFoundException extends RuntimeException

object Dijkstra:

  private val dist: MutableMap[Pos, Int] = MutableMap.empty

  private val prev: MutableMap[Pos, Pos] = MutableMap.empty

  private val q: MutableSet[Pos] = MutableSet.empty

  def init(): Unit =
    dist.clear()
    prev.clear()
    q.clear()

    dist(Grid.start) = 0

    Grid.tiles.zipWithIndex.map { case (column, x) =>
      column.zipWithIndex.map { case (tile, y) =>
        q.add(Pos(x, y))
      }
    }

  @throws[NoPathFoundException]("if no path is found")
  def iterate(): (Option[Pos], Boolean) =
    if q.isEmpty then throw new NoPathFoundException

    val u = q
      .map(pos => (pos, dist.getOrElse(pos, Int.MaxValue)))
      .minBy(_._2)
      ._1

    q.remove(u)

    if !dist.contains(u) then throw new NoPathFoundException

    Pos
      .getNeighbors(u)
      .filter(q.contains)
      .filter(isAccessible)
      .foreach(v =>
        val alt = dist(u) + 1
        if alt < dist.getOrElse(v, Int.MaxValue) then
          dist(v) = alt
          prev(v) = u
      )

    if Grid.start == u then (None, false)
    else (Some(u), Grid.finish == u)

  private def isAccessible(pos: Pos) =
    Pos.isOnMap(pos) && !Grid.isObstacle(pos)

  def getShortestPath: MutableQueue[Pos] =
    val path: MutableQueue[Pos] = MutableQueue.empty
    var u = prev.get(Grid.finish)
    while u.isDefined && !u.contains(Grid.start) do
      path.append(u.get)
      u = prev.get(u.get)
    path
