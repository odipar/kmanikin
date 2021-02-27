package net.kmanikin.example

import net.kmanikin.core.Id
import net.kmanikin.core.World
import net.kmanikin.message.LocalMessage
import net.kmanikin.world.SimpleWorld

class ID: Id<Counter> {
  override fun init() = Counter()
}

data class Counter(val count: Long = 0)

class Increase<W: World<W>>: LocalMessage.LMsg<W, ID, Counter, Unit>() {
  override fun pre() = true
  override fun app() = Counter(obj().count + 1)
  override fun eff() { }
  override fun pst() = obj().count == old().count + 1
}

fun main() {
  var world = SimpleWorld()

  val id = ID()
  val msg = Increase<SimpleWorld>()

  val x = 10000000
  
  for (i in 1..x) {
    world = world.send(id, msg).world
    if ((i % 1000000) == 0) println("i: $i")
  }
}