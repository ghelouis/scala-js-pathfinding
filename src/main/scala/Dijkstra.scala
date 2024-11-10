import scala.collection.mutable.Map as MutableMap
import scala.collection.mutable.Set as MutableSet
import scala.collection.mutable.Queue as MutableQueue

class NoPathFoundException extends RuntimeException

object Dijkstra:

  private val dist: MutableMap[Pos, Int] = MutableMap.empty

  private val prev: MutableMap[Pos, Pos] = MutableMap.empty

  private val q: MutableSet[Pos] = MutableSet.empty

  private var start: Pos = Pos(0, 0)

  private var finish: Pos = Pos(width - 1, 0)

  private val obstacles: MutableSet[Pos] = MutableSet.empty

  def init(map: Seq[Seq[Tile]]): Unit =
    obstacles.clear()

    map.zipWithIndex.map { case (column, x) =>
      column.zipWithIndex.map { case (tile, y) =>
        val pos = Pos(x, y)
        dist(pos) = Int.MaxValue
        q.add(pos)
        if tile == Tile.Start then start = Pos(x, y)
        if tile == Tile.Finish then finish = Pos(x, y)
        if tile == Tile.Obstacle then obstacles.add(Pos(x, y))
      }
    }

    dist(start) = 0

  @throws[NoPathFoundException]("if no path is found")
  def iterate(): (Option[Pos], Boolean) =
    if q.isEmpty then throw new NoPathFoundException

    val u = q
      .map(pos => (pos, dist.get(pos)))
      .minBy(_._2)
      ._1

    q.remove(u)

    if dist(u) == Int.MaxValue then throw new NoPathFoundException

    Pos
      .getNeighbors(u)
      .filter(q.contains)
      .filter(isAccessible)
      .foreach(v =>
        val alt = dist(u) + 1
        if alt < dist(v) then
          dist(v) = alt
          prev(v) = u
      )

    if start == u then (None, false)
    else (Some(u), u == finish)

  private def isAccessible(pos: Pos) =
    Pos.isOnMap(pos) && !obstacles.contains(pos)

  def getShortestPath: MutableQueue[Pos] =
    val path: MutableQueue[Pos] = MutableQueue.empty
    var u = prev.get(finish)
    while u.isDefined && !u.contains(start) do
      path.append(u.get)
      u = prev.get(u.get)
    path
