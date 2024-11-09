import org.scalajs.dom.console

import scala.collection.mutable.Map as MutableMap
import scala.collection.mutable.Set as MutableSet

object Dijkstra:

  private val dist: MutableMap[Pos, Int] = MutableMap.empty

  private val prev: MutableMap[Pos, Pos] = MutableMap.empty

  private val q: MutableSet[Pos] = MutableSet.empty

  private var start: Pos = Pos(0, 0)

  private var finish: Pos = Pos(width - 1, 0)

  def init(map: Seq[Seq[Tile]]): Unit =
    map.zipWithIndex.map { case (column, x) =>
      column.zipWithIndex.map { case (tile, y) =>
        val pos = Pos(x, y)
        dist(pos) = Int.MaxValue
        q.add(pos)
        if tile == Tile.Start then start = Pos(x, y)
        if tile == Tile.Finish then finish = Pos(x, y)
      }
    }

    dist(start) = 0

  def iterate(): Option[Pos] =
    if q.isEmpty then
      console.log("no path found")
      throw new RuntimeException("no path found")

    val u = q
      .map(pos => (pos, dist.get(pos)))
      .minBy(_._2)
      ._1

    q.remove(u)

    Pos
      .getNeighbors(u)
      .filter(q.contains)
      .foreach(v =>
        val alt = dist(u) + 1
        if alt < dist(v) then
          dist(v) = alt
          prev(v) = u
      )

    if Set(start, finish).contains(u) then None
    else Some(u)
