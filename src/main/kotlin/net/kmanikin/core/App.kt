package net.kmanikin.core

interface App<W: World<W>, I: Id<O>, O, R> {
  fun app(app: () -> O): Eff<W, I, O, R>
}