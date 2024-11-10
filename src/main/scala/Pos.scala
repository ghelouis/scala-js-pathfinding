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
