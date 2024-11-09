import org.scalajs.dom.console

import scala.collection.mutable.Map as MutableMap
import scala.collection.mutable.Set as MutableSet

object Dijkstra:

  private val dist: MutableMap[Pos, Int] = MutableMap.empty

  private val prev: MutableMap[Pos, Pos] = MutableMap.empty

  private val q: MutableSet[Pos] = MutableSet.empty

  def init(map: Seq[Seq[Tile]], start: Pos, finish: Pos): Unit =
    map.zipWithIndex.map { case (column, x) =>
      column.zipWithIndex.map { case (tile, y) =>
        val pos = Pos(x, y)
        dist(pos) = Int.MaxValue
        q.add(pos)
      }
    }

    dist(start) = 0

  def iterate(): Unit =
    console.log("dijkstra iterate")
