package net.kmanikin.core

interface Eff<W: World<W>, I: Id<O>, O, R> {
  fun eff(eff: () -> R): Pst<W, I, O, R>
}