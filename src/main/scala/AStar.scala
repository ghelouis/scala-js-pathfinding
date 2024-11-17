import scala.collection.mutable.{Map as MutableMap, Queue as MutableQueue, Set as MutableSet}

object AStar extends Algo:

  private val prev: MutableMap[Pos, Pos] = MutableMap.empty

  private val tileSet: MutableSet[Pos] = MutableSet.empty

  private val gScore: MutableMap[Pos, Int] = MutableMap.empty

  private val fScore: MutableMap[Pos, Int] = MutableMap.empty

  def getName = "A*"

  def init(): Unit =
    prev.clear()
    tileSet.clear()
    gScore.clear()
    fScore.clear()

    tileSet.add(Grid.start)
    gScore(Grid.start) = 0
    fScore(Grid.start) = Pos.getDist(Grid.start, Grid.finish)

  @throws[NoPathFoundException]("if no path is found")
  def iterate(): (Option[Pos], Boolean) =
    if tileSet.isEmpty then throw new NoPathFoundException

    val tile = tileSet
      .map(pos => (pos, fScore.getOrElse(pos, Int.MaxValue)))
      .minBy(_._2)
      ._1

    tileSet.remove(tile)

    Pos
      .getNeighbors(tile)
      .filterNot(Grid.isObstacle)
      .foreach(neighbor =>
        val tentativeGScore = gScore(tile) + 1
        if tentativeGScore < gScore.getOrElse(neighbor, Int.MaxValue) then
          prev(neighbor) = tile
          gScore(neighbor) = tentativeGScore
          fScore(neighbor) = tentativeGScore + Pos.getDist(neighbor, Grid.finish)
          if !tileSet.contains(neighbor) then tileSet.add(neighbor)
      )

    if tileSet.isEmpty then throw new NoPathFoundException

    if Grid.start == tile then (None, false)
    else (Some(tile), Grid.finish == tile)

  def getShortestPath: MutableQueue[Pos] =
    val path: MutableQueue[Pos] = MutableQueue.empty
    var prevTile = prev.get(Grid.finish)
    while prevTile.isDefined && !prevTile.contains(Grid.start) do
      path.append(prevTile.get)
      prevTile = prev.get(prevTile.get)
    path
