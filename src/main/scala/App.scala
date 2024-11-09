import org.scalajs.dom
import org.scalajs.dom.{CanvasRenderingContext2D, KeyCode, html}

import scala.util.Random

val width = 1000
val height = 800
val tileSize = 20
val nObstacles = 15
val obstacleMinSize = 5
val obstacleMaxSize = 11

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

object App:

  private def getRandomBetween(start: Int, end: Int) =
    start + Random.nextInt(end - start + 1)

  private def drawGrid(ctx: CanvasRenderingContext2D): Unit =
    for i <- 1 until width / tileSize do
      ctx.moveTo(i * tileSize, 0)
      ctx.lineTo(i * tileSize, height)
      if i <= height / tileSize - 1 then
        ctx.moveTo(0, i * tileSize)
        ctx.lineTo(width, i * tileSize)
    ctx.stroke()

  private def fillTile(ctx: CanvasRenderingContext2D, style: String, tileX: Int, tileY: Int): Unit =
    ctx.fillStyle = style
    ctx.fillRect(tileX * tileSize + 1, tileY * tileSize + 1, tileSize - 2, tileSize - 2)

  private def drawMap(ctx: CanvasRenderingContext2D, map: Seq[Seq[Tile]]): Unit =
    map.zipWithIndex.map { case (column, x) =>
      column.zipWithIndex.map { case (tile, y) =>
        fillTile(ctx, Tile.getStyle(tile), x, y)
      }
    }

  private def buildMap(): Seq[Seq[Tile]] =
    val startY = Random.nextInt(height / tileSize)
    val finishY = Random.nextInt(height / tileSize)

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
            if x == 0 && y == startY then Tile.Start
            else if x == width / tileSize - 1 && y == finishY then Tile.Finish
            else if obstacles.contains(x, y) then Tile.Obstacle
            else Tile.Empty
          )
      )

  private def initNewRandomMap(): Unit =
    val canvas = dom.document.querySelector("canvas").asInstanceOf[html.Canvas]
    val ctx = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
    canvas.width = width
    canvas.height = height
    ctx.fillStyle = "white"
    ctx.strokeStyle = "black"
    ctx.fillRect(0, 0, canvas.width, canvas.height)
    drawGrid(ctx)

    val map = buildMap()
    drawMap(ctx, map)

  private def handleKeyPressed(keyCode: Int): Unit =
    keyCode match
      case KeyCode.N =>
        initNewRandomMap()
      case _ =>
      // key not mapped to an action

  def main(args: Array[String]): Unit =
    initNewRandomMap()
    dom.window.onkeydown = mouseEvent => handleKeyPressed(mouseEvent.keyCode)
