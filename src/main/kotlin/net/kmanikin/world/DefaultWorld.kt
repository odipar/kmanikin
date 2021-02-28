package net.kmanikin.world

import net.kmanikin.core.*

abstract class DefaultWorld {
  abstract class B<W: World<W>, I: Id<O>, O, R>: Pre<W, I, O, R>, App<W, I, O, R>, Eff<W, I, O, R>, Pst<W, I, O, R>, Msg<W, I, O, R> {
    var pre_ : (() -> Boolean)? = null
    var app_ : (() -> O)? = null
    var eff_ : (() -> R)? = null
    var pst_ : (() -> Boolean)? = null

    override fun app(app: () -> O): Eff<W, I, O, R> { app_ = app ; return this }
    override fun eff(eff: () -> R): Pst<W, I, O, R> { eff_ = eff ; return this }
    override fun pst(pst: () -> Boolean): Msg<W, I, O, R> { pst_ = pst ; return this }
    override fun pre(): () -> Boolean = pre_!!
    override fun app(): () -> O = app_!!
    override fun eff(): () -> R = eff_!!
    override fun pst(): () -> Boolean = pst_!!
  }

  class E<W: World<W>, I: Id<O>, O, R>(private val s: I, var world_ : W): B<W, I, O, R>(), Env<W, I, O, R> {
    override fun self() = s
    override fun world() = world_
    override fun <O2> obj(id: Id<O2>): O2 = step { world().obj(id) }
    override fun <O2> old(id: Id<O2>): O2 = step { world().old(id) }
    override fun <I2 : Id<O2>, O2, R2> send(id: I2, msg: Message<W, I2, O2, R2>): R2 = step { world().send(id, msg ) }
    override fun pre(pre: () -> Boolean): App<W, I, O, R> { pre_ = pre ; return this }
    private fun <X> step(f: () -> Val<W, X>): X { val r = f() ; world_ = r.world ; return r.value }
  }
}