//       ___       ___       ___       ___       ___
//      /\  \     /\__\     /\__\     /\  \     /\__\
//     /::\  \   /:/ _/_   /:| _|_   /::\  \   /:/  /
//    /::\:\__\ /::-"\__\ /::|/\__\ /::\:\__\ /:/__/
//    \;:::/  / \;:;-",-" \/|::/  / \;:::/  / \:\  \
//     |:\/__/   |:|  |     |:/  /   |:\/__/   \:\__\
//      \|__|     \|__|     \/__/     \|__|     \/__/

package ru.rknrl.logging

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.event.LoggingAdapter

trait ShortActorLogging extends ActorLogging {
  this: Actor ⇒
  val logFilter: Any ⇒ Boolean = any ⇒ true

  def logged(r: Receive) = new ShortLoggingReceive(r, log, logFilter)

  def send(to: ActorRef, msg: Any): Unit = {
    log.debug("→ " + msg)
    to ! msg
  }

  def forward(to: ActorRef, msg: Any): Unit = {
    log.debug("→→ " + msg)
    to forward msg
  }

  def become(behavior: Receive, name: String): Unit = {
    log.debug("become " + name)
    context become behavior
  }
}

class ShortLoggingReceive(r: Receive,
                          log: LoggingAdapter,
                          filter: Any ⇒ Boolean) extends Receive {

  override def isDefinedAt(o: Any): Boolean = {
    val handled = r.isDefinedAt(o)
    if (filter(o))
      log debug (if (handled) "✓" else "✖") + " " + o
    handled
  }

  def apply(o: Any): Unit = r(o)
}