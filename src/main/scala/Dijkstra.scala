import scala.collection.mutable.{Map as MutableMap, Queue as MutableQueue, Set as MutableSet}

class NoPathFoundException extends RuntimeException

object Dijkstra:

  private val dist: MutableMap[Pos, Int] = MutableMap.empty

  private val prev: MutableMap[Pos, Pos] = MutableMap.empty

  private val tileSet: MutableSet[Pos] = MutableSet.empty

  def init(): Unit =
    dist.clear()
    prev.clear()
    tileSet.clear()

    dist(Grid.start) = 0

    Grid.tiles.zipWithIndex.map { case (column, x) =>
      column.zipWithIndex.map { case (tile, y) =>
        if tile != Tile.Obstacle then tileSet.add(Pos(x, y))
      }
    }

  @throws[NoPathFoundException]("if no path is found")
  def iterate(): (Option[Pos], Boolean) =
    if tileSet.isEmpty then throw new NoPathFoundException

    val tile = tileSet
      .map(pos => (pos, dist.getOrElse(pos, Int.MaxValue)))
      .minBy(_._2)
      ._1

    tileSet.remove(tile)

    if !dist.contains(tile) then throw new NoPathFoundException

    Pos
      .getNeighbors(tile)
      .filter(tileSet.contains)
      .foreach(neighbor =>
        val distToNeighborViaTile = dist(tile) + 1
        if distToNeighborViaTile < dist.getOrElse(neighbor, Int.MaxValue) then
          dist(neighbor) = distToNeighborViaTile
          prev(neighbor) = tile
      )

    if Grid.start == tile then (None, false)
    else (Some(tile), Grid.finish == tile)

  def getShortestPath: MutableQueue[Pos] =
    val path: MutableQueue[Pos] = MutableQueue.empty
    var prevTile = prev.get(Grid.finish)
    while prevTile.isDefined && !prevTile.contains(Grid.start) do
      path.append(prevTile.get)
      prevTile = prev.get(prevTile.get)
    path
