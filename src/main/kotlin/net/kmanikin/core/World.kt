package net.kmanikin.core

interface World<W: World<W>> {
  fun <O2>obj(id: Id<O2>): Val<W, O2>
  fun <O2>old(id: Id<O2>): Val<W, O2>
  fun <I: Id<O>, O, R>send(id: I, msg: Message<W, I, O, R>): Val<W, R>
}