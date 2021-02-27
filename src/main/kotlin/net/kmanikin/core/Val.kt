package net.kmanikin.core

data class Val<W: World<W>, V>(val world: W, val value: V): World<W> {
  override fun <O2>obj(id: Id<O2>): Val<W, O2> = world.obj(id)
  override fun <O2>old(id: Id<O2>): Val<W, O2> = world.old(id)
  override fun <I: Id<O>, O, R>send(id: I, msg: Message<W, I, O, R>): Val<W, R> = world.send(id, msg)
}