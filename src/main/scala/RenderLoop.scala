import org.scalajs.dom

import scala.scalajs.js.Date

object RenderLoop:
  private val fpsInterval = 1000 / Config.fps
  private var before = 0.0
  private var stopped = false

  private def animate(seconds: Double, update: () => Unit): Unit =
    if !stopped then
      val requestAnimationId = dom.window.requestAnimationFrame(_ => animate(seconds, update))
      val now = Date.now
      val elapsed = now - before

      if elapsed > fpsInterval then
        before = now - (elapsed % fpsInterval)
        update()

  def start(update: () => Unit): Unit =
    stopped = false
    before = Date.now
    animate(before, update)

  def stop(): Unit = stopped = true

  def toggle(update: () => Unit): Unit =
    if stopped then start(update)
    else stop()
