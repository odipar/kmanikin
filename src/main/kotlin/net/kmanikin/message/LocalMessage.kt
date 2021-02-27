package net.kmanikin.message

import net.kmanikin.core.*

object LocalMessage {
  val local = ThreadLocal<Val<*, *>>()

  interface IMsg<W : World<W>, I : Id<O>, O, R> : Message<W, I, O, R> {
    fun pre2(): Boolean
    fun app2(): O
    fun eff2(): R
    fun pst2(): Boolean

    override fun preCondition(): (Val<W, I>) -> Val<W, Boolean> = { id -> step(id) { pre2() } }
    override fun apply(): (Val<W, I>) -> Val<W, O> = { id -> step(id) { app2() } }
    override fun effect(): (Val<W, I>) -> Val<W, R> = { id -> step(id) { eff2() } }
    override fun postCondition(): (Val<W, I>) -> Val<W, Boolean> = { id -> step(id) { pst2() } }

    @Suppress("UNCHECKED_CAST")
    fun get(): Val<W, I> = local.get() as Val<W, I>
    fun set(c: Val<W, I>) = local.set(c)
    fun self(): I = get().value
    fun obj2(): O = obj2(self())
    fun old2(): O = old2(self())
    fun world(): W = get().world
    fun <O2> obj2(id: Id<O2>): O2 = eval{ world().obj(id) }
    fun <O2> old2(id: Id<O2>): O2 = eval{ world().old(id) }

    fun <I2: Id<O2>, O2, R2> send(id: I2, msg: Message<W, I2, O2, R2>): R2 = eval { world().send(id, msg) }
    fun <X> eval(f: () -> Val<W, X>): X { val s = self() ; val r = f() ; set(Val(r.world, s)) ; return r.value }
    fun <X> step(id: Val<W, I>, f: () -> X): Val<W, X> { set(id) ; val r = f() ; return Val(world(), r)  }
  }

  abstract class LMsg<W: World<W>, I: Id<O>, O, R>: IMsg<W, I, O, R> {
    abstract fun pre(): Boolean
    abstract fun app(): O
    abstract fun eff(): R
    abstract fun pst(): Boolean

    fun obj() = obj2()
    fun old() = old2()
    fun <O2> obj(id: Id<O2>): O2 = obj2(id)
    fun <O2> old(id: Id<O2>): O2 = old2(id)

    override fun pre2() = pre()
    override fun app2() = app()
    override fun eff2() = eff()
    override fun pst2() = pst()
  }
}