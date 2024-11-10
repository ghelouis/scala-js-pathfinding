import scala.util.Random
import Config.{width, height, tileSize, nObstacles, obstacleMinSize, obstacleMaxSize}

object Grid:

  var tiles: Seq[Seq[Tile]] = Seq.empty

  var start: Pos = Pos(0, 0)

  var finish: Pos = Pos(0, 0)

  private def getRandomBetween(a: Int, b: Int) =
    a + Random.nextInt(b - a + 1)

  def generate(): Unit =
    start = Pos(0, Random.nextInt(height / tileSize))
    finish = Pos(width / tileSize - 1, Random.nextInt(height / tileSize))

    val obstacles = (0 until nObstacles)
      .flatMap(_ =>
        val x = getRandomBetween(1, width / tileSize - obstacleMaxSize - 1)
        val y = getRandomBetween(0, height / tileSize - obstacleMaxSize)
        val size = getRandomBetween(obstacleMinSize, obstacleMaxSize)
        (x until x + size).flatMap(x => (y until y + size).map(y => (x, y)))
      )
      .toSet

    tiles = (0 until width / tileSize)
      .map(x =>
        (0 until height / tileSize)
          .map(y =>
            if x == start.x && y == start.y then Tile.Start
            else if x == finish.x && y == finish.y then Tile.Finish
            else if obstacles.contains(x, y) then Tile.Obstacle
            else Tile.Empty
          )
      )
