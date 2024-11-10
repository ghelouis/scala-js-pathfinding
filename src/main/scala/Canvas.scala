import org.scalajs.dom
import org.scalajs.dom.{CanvasRenderingContext2D, html}
import org.scalajs.dom.html.Canvas
import Config.{width, height, tileSize}

object Canvas:
  private lazy val canvas: Canvas = dom.document.querySelector("canvas").asInstanceOf[html.Canvas]
  private lazy val ctx: CanvasRenderingContext2D =
    canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

  private val fillStyle = "white"
  private val strokeStyle = "black"
  private val visitedPositionStyle = "tomato"
  private val shortedPathPositionStyle = "yellow"

  def init(): Unit =
    canvas.width = width
    canvas.height = height
    ctx.fillStyle = fillStyle
    ctx.strokeStyle = strokeStyle
    ctx.fillRect(0, 0, canvas.width, canvas.height)

  def drawGridBackground(): Unit =
    for i <- 1 until width / tileSize do
      ctx.moveTo(i * tileSize, 0)
      ctx.lineTo(i * tileSize, height)
      if i <= height / tileSize - 1 then
        ctx.moveTo(0, i * tileSize)
        ctx.lineTo(width, i * tileSize)
    ctx.stroke()

  def drawGrid(): Unit =
    Grid.tiles.zipWithIndex.map { case (column, x) =>
      column.zipWithIndex.map { case (tile, y) =>
        fillTile(ctx, Tile.getStyle(tile), x, y)
      }
    }

  private def fillTile(ctx: CanvasRenderingContext2D, style: String, tileX: Int, tileY: Int): Unit =
    ctx.fillStyle = style
    ctx.fillRect(tileX * tileSize + 1, tileY * tileSize + 1, tileSize - 2, tileSize - 2)

  def drawVisitedPosition(pos: Pos): Unit =
    fillTile(ctx, visitedPositionStyle, pos.x, pos.y)

  def drawShortestPathPosition(pos: Pos): Unit =
    fillTile(ctx, shortedPathPositionStyle, pos.x, pos.y)

  def clearTile(pos: Pos): Unit =
    fillTile(ctx, Tile.getStyle(Tile.Empty), pos.x, pos.y)
