import org.scalajs.dom
import org.scalajs.dom.{CanvasRenderingContext2D, KeyCode, console, html}

import scala.util.Random

val width = 1000
val height = 800
val tileSize = 20

enum Tile:
  case Empty, Start, Finish, Obstacle

object App:

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
        tile match
          case Tile.Empty =>
            fillTile(ctx, "aliceblue", x, y)
          case Tile.Start =>
            fillTile(ctx, "green", x, y)
          case Tile.Finish =>
            fillTile(ctx, "red", x, y)
          case Tile.Obstacle =>
      }
    }

  private def buildMap(): Seq[Seq[Tile]] =
    val startY = Random.nextInt(height / tileSize)
    val finishY = Random.nextInt(height / tileSize)

    (0 until width / tileSize)
      .map(x =>
        (0 until height / tileSize)
          .map(y =>
            if x == 0 && y == startY then Tile.Start
            else if x == width / tileSize - 1 && y == finishY then Tile.Finish
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
