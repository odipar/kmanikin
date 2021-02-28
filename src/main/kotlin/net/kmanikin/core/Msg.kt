package net.kmanikin.core

interface Msg<W: World<W>, I: Id<O>, O, R> {
  fun pre(): () -> Boolean
  fun app(): () -> O
  fun eff(): () -> R
  fun pst(): () -> Boolean
}