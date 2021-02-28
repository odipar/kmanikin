package net.kmanikin.core

interface Pst<W: World<W>, I: Id<O>, O, R> {
  fun pst(pst: () -> Boolean): Msg<W, I, O, R>
}