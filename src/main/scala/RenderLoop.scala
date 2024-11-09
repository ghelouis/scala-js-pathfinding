import org.scalajs.dom

import scala.scalajs.js.Date

object RenderLoop:
  private val fpsInterval = 1000 / fps
  private var before = 0.0

  private def animate(seconds: Double, update: () => Unit): Unit =
    dom.window.requestAnimationFrame(_ => animate(seconds, update))
    val now = Date.now
    val elapsed = now - before

    if elapsed > fpsInterval then
      before = now - (elapsed % fpsInterval)
      update()

  def start(update: () => Unit): Unit =
    before = Date.now
    animate(before, update)
