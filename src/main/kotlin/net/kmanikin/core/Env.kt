package net.kmanikin.core

interface Env<W: World<W>, I: Id<O>, O, R>: Pre<W, I, O, R> {
  fun self(): I
  fun world(): W

  fun <O2>obj(id: Id<O2>): O2
  fun <O2>old(id: Id<O2>): O2
  fun <I2: Id<O2>, O2, R2>send(id: I2, msg: Message<W, I2, O2, R2>): R2

  fun obj(): O = obj(self())
  fun old(): O = old(self())
}