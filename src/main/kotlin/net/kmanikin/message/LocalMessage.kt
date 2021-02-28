package net.kmanikin.message

import net.kmanikin.core.*

abstract class LocalMessage<W: World<W>, I: Id<O>, O, R>: Message<W, I, O, R> {
  fun e(): LocalEnv<W, I, O, R> = localEnv.get() as LocalEnv<W, I, O, R>

  override fun msg() = { e: Env<W, I, O, R> -> localEnv.set(Builder(e)) ; local() }
  fun pre(pre: () -> Boolean): App<W, I, O, R> = e().pre(pre)
  abstract fun local(): Msg<W, I, O, R>

  fun obj() = e().obj()
  fun old() = e().old()
  fun <O2>obj(id: Id<O2>) = e().obj(id)
  fun <O2>old(id: Id<O2>) = e().old(id)
  fun <I2: Id<O2>, O2, R2>send(id: I2, msg: Message<W, I2, O2, R2>): R2 = e().send(id, msg)

  companion object {
    protected val localEnv = ThreadLocal<Env<*, *, *, *>>()
  }

  interface LocalEnv<W: World<W>, I: Id<O>, O, R>: Env<W, I, O, R> {
    fun env(): Env<W, I, O, R>
    override fun self() = env().self()
    override fun world() = env().world()
    override fun <O2> obj(id: Id<O2>): O2 = env().obj(id)
    override fun <O2> old(id: Id<O2>): O2 = env().old(id)
    override fun <I2 : Id<O2>, O2, R2> send(id: I2, msg: Message<W, I2, O2, R2>): R2 = env().send(id, msg )
  }

  class Builder<W: World<W>, I: Id<O>, O, R>(private val env_: Env<W, I, O, R>):
    LocalEnv<W, I, O, R>, Pre<W, I, O, R>, App<W, I, O, R>, Eff<W, I, O, R>, Pst<W, I, O, R>, Msg<W, I, O, R> {

    var pre_ : (() -> Boolean)? = null
    var app_ : (() -> O)? = null
    var eff_ : (() -> R)? = null
    var pst_ : (() -> Boolean)? = null

    override fun env() = env_

    override fun pre(pre: () -> Boolean): App<W, I, O, R> { localEnv.set(this) ; pre_ = pre ; return this }
    override fun app(app: () -> O): Eff<W, I, O, R> { localEnv.set(this) ; app_ = app ; return this }
    override fun eff(eff: () -> R): Pst<W, I, O, R> { localEnv.set(this) ; eff_ = eff ; return this }
    override fun pst(pst: () -> Boolean): Msg<W, I, O, R> { localEnv.set(this) ; pst_ = pst ; return this }

    override fun pre(): () -> Boolean = pre_!!
    override fun app(): () -> O = app_!!
    override fun eff(): () -> R = eff_!!
    override fun pst(): () -> Boolean = pst_!!
  }
}