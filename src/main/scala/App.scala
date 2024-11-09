import org.scalajs.dom
import org.scalajs.dom.KeyCode

import scala.util.Random

val width = 1000
val height = 800
val tileSize = 20
val nObstacles = 15
val obstacleMinSize = 5
val obstacleMaxSize = 11
val fps = 60

object App:

  private def getRandomBetween(start: Int, end: Int) =
    start + Random.nextInt(end - start + 1)

  private def buildMap(): Seq[Seq[Tile]] =
    val start = Pos(0, Random.nextInt(height / tileSize))
    val finish = Pos(width / tileSize - 1, Random.nextInt(height / tileSize))
    val obstacles = (0 until nObstacles)
      .flatMap(_ =>
        val x = getRandomBetween(1, width / tileSize - obstacleMaxSize - 1)
        val y = getRandomBetween(0, height / tileSize - obstacleMaxSize)
        val size = getRandomBetween(obstacleMinSize, obstacleMaxSize)
        (x until x + size).flatMap(x => (y until y + size).map(y => (x, y)))
      )
      .toSet

    (0 until width / tileSize)
      .map(x =>
        (0 until height / tileSize)
          .map(y =>
            if x == start.x && y == start.y then Tile.Start
            else if x == finish.x && y == finish.y then Tile.Finish
            else if obstacles.contains(x, y) then Tile.Obstacle
            else Tile.Empty
          )
      )

  private def initNewRandomMap(): Unit =
    val map = buildMap()
    Dijkstra.init(map)
    Canvas.drawMap(map)

  private def handleKeyPressed(keyCode: Int): Unit =
    keyCode match
      case KeyCode.N =>
        initNewRandomMap()
      case _ =>
      // key not mapped to an action

  private def update(): Unit =
    Dijkstra.iterate().foreach(Canvas.drawVisitedPosition)

  def main(args: Array[String]): Unit =
    Canvas.init()
    Canvas.drawGrid()
    initNewRandomMap()
    dom.window.onkeydown = mouseEvent => handleKeyPressed(mouseEvent.keyCode)
    RenderLoop.start(update)
