import org.scalajs.dom

import scala.scalajs.js.Date

object RenderLoop:
  private val fpsInterval = 1000 / fps
  private var before = 0.0

  private def animate(seconds: Double, update: (Seq[Seq[Tile]]) => Unit, map: Seq[Seq[Tile]]): Unit =
    dom.window.requestAnimationFrame(_ => animate(seconds, update, map))
    val now = Date.now
    val elapsed = now - before

    if elapsed > fpsInterval then
      before = now - (elapsed % fpsInterval)
      update(map)

  def start(update: (Seq[Seq[Tile]]) => Unit, map: Seq[Seq[Tile]]): Unit =
    before = Date.now
    animate(before, update, map)
