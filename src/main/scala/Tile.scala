enum Tile:
  case Empty, Start, Finish, Obstacle

object Tile:
  def getStyle(tile: Tile): String = tile match
    case Tile.Empty =>
      "aliceblue"
    case Tile.Start =>
      "green"
    case Tile.Finish =>
      "red"
    case Tile.Obstacle =>
      "darkslateblue"
