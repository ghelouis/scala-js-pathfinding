import org.scalajs.dom
import org.scalajs.dom.{CanvasRenderingContext2D, KeyCode, html}

import scala.util.Random

object App:
  private val width = 1000
  private val height = 800
  private val tileSize = 20

  case class Pos(x: Int, y: Int)

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

  private def initNewRandomMap(): Unit =
    val canvas = dom.document.querySelector("canvas").asInstanceOf[html.Canvas]
    val ctx = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
    canvas.width = width
    canvas.height = height
    ctx.fillStyle = "white"
    ctx.strokeStyle = "black"
    ctx.fillRect(0, 0, canvas.width, canvas.height)
    drawGrid(ctx)

    val start = Pos(0, Random.nextInt(height / tileSize))
    val goal = Pos(width / tileSize - 1, Random.nextInt(height / tileSize))
    fillTile(ctx, "green", start.x, start.y)
    fillTile(ctx, "red", goal.x, goal.y)

  private def handleKeyPressed(keyCode: Int): Unit =
    keyCode match
      case KeyCode.N =>
        initNewRandomMap()
      case _ =>
      // key not mapped to an action

  def main(args: Array[String]): Unit =
    initNewRandomMap()
    dom.window.onkeydown = mouseEvent => handleKeyPressed(mouseEvent.keyCode)
