package net.kmanikin.world

import net.kmanikin.core.*

class SimpleWorld: World<SimpleWorld> {
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
    val self = Val(this, id)
    val old = Pair(id, obj(id).value)

    return if (!msg.preCondition()(self).value) throw RuntimeException("Pre-condition failed") else {
      try {
        state[id] = msg.apply()(self).value!!

        oldState = old
        val eff = msg.effect()(self)
        oldState = old

        if (!msg.postCondition()(self).value) throw RuntimeException("Post-condition failed") else eff
      } finally { oldState = null }
    }
  }
}