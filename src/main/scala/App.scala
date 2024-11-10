import org.scalajs.dom
import org.scalajs.dom.KeyCode

import scala.collection.immutable.SortedMap

object App:

  private lazy val pathNotFoundNode = dom.document.createTextNode("No path found!")

  private lazy val pathFoundNode = dom.document.createTextNode("Path found!")

  private val algos = SortedMap("Dijkstra" -> "todo", "A*" -> "todo")

  private var algo = "Dijkstra"

  private def rotateAlgo(): Unit =
    val it = algos.keysIteratorFrom(algo)
    it.next()
    it.nextOption() match
      case Some(nextAlgo) =>
        algo = nextAlgo
      case None =>
        algo = algos.keySet.head

  private def initNewRandomMap(): Unit =
    Grid.generate()
    Canvas.drawGrid()
    Dijkstra.init()
    RenderLoop.start(update)

  private def clearMessages() =
    if dom.document.body.contains(pathNotFoundNode) then dom.document.body.removeChild(pathNotFoundNode)
    if dom.document.body.contains(pathFoundNode) then dom.document.body.removeChild(pathFoundNode)

  private def handleKeyPressed(keyCode: Int): Unit =
    keyCode match
      case KeyCode.N =>
        clearMessages()
        initNewRandomMap()
      case KeyCode.R =>
        clearMessages()
        Canvas.drawGrid()
        Dijkstra.init()
        RenderLoop.start(update)
      case KeyCode.A =>
        rotateAlgo()
        dom.document.getElementById("algo").textContent = algo
      case _ =>
      // key not mapped to an action

  private def update(): Unit =
    try
      val (visitedPosition, isFinished) = Dijkstra.iterate()
      if isFinished then
        RenderLoop.stop()
        Dijkstra.getShortestPath.foreach(Canvas.drawShortestPathPosition)
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
