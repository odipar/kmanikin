package net.kmanikin.core

interface Pre<W: World<W>, I: Id<O>, O, R> {
  fun pre(pre: () -> Boolean): App<W, I, O, R>
}