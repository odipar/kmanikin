package net.kmanikin.world

import net.kmanikin.core.*

class SimpleWorld: DefaultWorld(), World<SimpleWorld> {
  val state = mutableMapOf<Any, Any>()
  private var oldState: Pair<*, *>? = null

  @Suppress("UNCHECKED_CAST")
  override fun <O> obj(id: Id<O>): Val<SimpleWorld, O> {
    return Val(this, state.getOrDefault(id, id.init()) as O)
  }

  @Suppress("UNCHECKED_CAST")
  override fun <O> old(id: Id<O>): Val<SimpleWorld, O> {
    return if (oldState!!.first === id) Val(this, oldState!!.second as O) else obj(id)
  }

  override fun <I : Id<O>, O, R> send(id: I, msg: Message<SimpleWorld, I, O, R>): Val<SimpleWorld, R> {
    val env = E<SimpleWorld, I, O, R>(id, this)
    val old = Pair(id, obj(id).value)
    val msg2 = msg.msg()(env)

    return if (!msg2.pre()()) throw RuntimeException("Pre-condition failed") else {
      try {
        state[id] = msg2.app()()!!

        oldState = old
        val eff = msg2.eff()()
        oldState = old

        if (!msg2.pst()()) throw RuntimeException("Post-condition failed") else Val(env.world(), eff)
      } finally { oldState = null }
    }
  }
}