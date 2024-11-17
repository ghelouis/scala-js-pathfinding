import scala.collection.mutable.{Map as MutableMap, Queue as MutableQueue, Set as MutableSet}

object Greedy extends Algo:

  private val dist: MutableMap[Pos, Int] = MutableMap.empty

  private val prev: MutableMap[Pos, Pos] = MutableMap.empty

  private val tileSet: MutableSet[Pos] = MutableSet.empty

  private val visitedSet: MutableSet[Pos] = MutableSet.empty

  override def init(): Unit =
    dist.clear()
    prev.clear()
    tileSet.clear()
    visitedSet.clear()

    tileSet.add(Grid.start)
    dist(Grid.start) = Pos.getDist(Grid.start, Grid.finish)

  @throws[NoPathFoundException]("if no path is found")
  override def iterate(): (Option[Pos], Boolean) =
    if tileSet.isEmpty then throw new NoPathFoundException

    val tile = tileSet
      .map(pos => (pos, dist.getOrElse(pos, Int.MaxValue)))
      .minBy(_._2)
      ._1

    tileSet.remove(tile)

    Pos
      .getNeighbors(tile)
      .filterNot(Grid.isObstacle)
      .filterNot(visitedSet.contains)
      .foreach(neighbor =>
        prev(neighbor) = tile
        dist(neighbor) = Pos.getDist(neighbor, Grid.finish)
        visitedSet.add(neighbor)
        tileSet.add(neighbor)
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
