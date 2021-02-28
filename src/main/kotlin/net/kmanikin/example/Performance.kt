package net.kmanikin.example

import net.kmanikin.core.*
import net.kmanikin.message.LocalMessage
import net.kmanikin.world.SimpleWorld

class ID: Id<Counter> {
  override fun init() = Counter()
}

data class Counter(val count: Long = 0)

class Increase<W: World<W>>: LocalMessage<W, ID, Counter, Unit>() {
  override fun local() = e().
    pre { true }.
    app { Counter(obj().count + 1) }.
    eff { }.
    pst { obj().count == old().count + 1 }
}

fun <R> time(block: () -> R): R {
  val t0 = System.currentTimeMillis().toDouble()
  val result = block()
  val t1 = System.currentTimeMillis().toDouble()
  println("elapsed time: " + (t1 - t0) + "ms")
  return result
}

fun main() {
  var world = SimpleWorld()

  val id = ID()
  val msg = Increase<SimpleWorld>()

  val x = 100000000
  
  time {
    for (i in 1..x) {
      world = world.send(id, msg).world
      if ((i % (x / 10)) == 0) println("i: $i")
    }
  }
}