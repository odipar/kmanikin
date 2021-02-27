package net.kmanikin.core

interface Message<W: World<W>, I: Id<O>, O, R> {
  fun preCondition(): (Val<W, I>) -> Val<W, Boolean>
  fun apply(): (Val<W, I>) -> Val<W, O>
  fun effect(): (Val<W, I>) -> Val<W, R>
  fun postCondition(): (Val<W, I>) -> Val<W, Boolean>
}