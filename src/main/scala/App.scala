import org.scalajs.dom
import org.scalajs.dom.KeyCode

object App:

  private lazy val pathNotFoundNode = dom.document.createTextNode("No path found!")

  private lazy val pathFoundNode = dom.document.createTextNode("Path found!")

  private val algos = List(Dijkstra, AStar, Greedy)

  private var currentAlgo: Algo = Dijkstra

  private def rotateAlgo(): Unit =
    if algos.indexOf(currentAlgo) + 1 < algos.size then currentAlgo = algos(algos.indexOf(currentAlgo) + 1)
    else currentAlgo = algos.head

  private def initNewRandomMap(): Unit =
    Grid.generate()
    Canvas.drawGrid()
    currentAlgo.init()
    RenderLoop.start(update)

  private def clearMessages() =
    if dom.document.body.contains(pathNotFoundNode) then dom.document.body.removeChild(pathNotFoundNode)
    if dom.document.body.contains(pathFoundNode) then dom.document.body.removeChild(pathFoundNode)

  private def restart(): Unit =
    clearMessages()
    Canvas.drawGrid()
    currentAlgo.init()
    RenderLoop.start(update)

  private def handleKeyPressed(keyCode: Int): Unit =
    keyCode match
      case KeyCode.N =>
        clearMessages()
        initNewRandomMap()
      case KeyCode.R =>
        restart()
      case KeyCode.A =>
        rotateAlgo()
        restart()
        dom.document.getElementById("algo").textContent = currentAlgo.getName
      case _ =>
      // key not mapped to an action

  private def update(): Unit =
    try
      val (visitedPosition, isFinished) = currentAlgo.iterate()
      if isFinished then
        RenderLoop.stop()
        currentAlgo.getShortestPath.foreach(Canvas.drawShortestPathPosition)
        dom.document.body.appendChild(pathFoundNode)
      else visitedPosition.foreach(Canvas.drawVisitedPosition)
    catch
      case e: NoPathFoundException =>
        RenderLoop.stop()
        dom.document.body.appendChild(pathNotFoundNode)

  def main(args: Array[String]): Unit =
    Canvas.init()
    Canvas.drawGridBackground()
    initNewRandomMap()
    dom.window.onkeydown = mouseEvent => handleKeyPressed(mouseEvent.keyCode)
