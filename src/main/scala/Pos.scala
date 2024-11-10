import Config.{width, height, tileSize}

case class Pos(x: Int, y: Int)

object Pos:
  def getNeighbors(pos: Pos): Seq[Pos] =
    List(
      Pos(pos.x, pos.y + 1),
      Pos(pos.x + 1, pos.y),
      Pos(pos.x, pos.y - 1),
      Pos(pos.x - 1, pos.y)
    ).filter(pos => pos.x >= 0 && pos.x < width / tileSize && pos.y >= 0 && pos.y < height / tileSize)

  // Taxicab distance, see https://en.wikipedia.org/wiki/Taxicab_distance
  // a.k.a distance when moving on a 2D grid with the set of movement available being the 4 adjacent squares
  def getDist(a: Pos, b: Pos): Int =
    math.abs(a.x - b.x) + math.abs(a.y - b.y)
