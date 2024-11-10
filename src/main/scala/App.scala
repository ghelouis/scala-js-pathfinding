import org.scalajs.dom
import org.scalajs.dom.KeyCode

val width = 1000
val height = 800
val tileSize = 20
val nObstacles = 15
val obstacleMinSize = 5
val obstacleMaxSize = 11
val fps = 60

object App:

  private def initNewRandomMap(): Unit =
    Grid.generate()
    Canvas.drawGrid()
    Dijkstra.init()
    RenderLoop.start(update)

  private def handleKeyPressed(keyCode: Int): Unit =
    keyCode match
      case KeyCode.N =>
        Canvas.clearMessages()
        initNewRandomMap()
      case _ =>
      // key not mapped to an action

  private def update(): Unit =
    try
      val (visitedPosition, isFinished) = Dijkstra.iterate()
      if isFinished then
        RenderLoop.stop()
        Dijkstra.getShortestPath.foreach(Canvas.drawShortestPathPosition)
        Canvas.drawPathFoundMessage()
      else visitedPosition.foreach(Canvas.drawVisitedPosition)
    catch
      case e: NoPathFoundException =>
        RenderLoop.stop()
        Canvas.drawPathNotFoundMessage()

  def main(args: Array[String]): Unit =
    Canvas.init()
    Canvas.drawGridBackground()
    initNewRandomMap()
    dom.window.onkeydown = mouseEvent => handleKeyPressed(mouseEvent.keyCode)
