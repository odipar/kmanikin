package net.kmanikin.core

interface Message<W: World<W>, I: Id<O>, O, R> {
  fun msg(): (Env<W, I, O, R>) -> Msg<W, I, O, R>
}