package net.kmanikin.example.bank

import net.kmanikin.core.*
import net.kmanikin.message.LocalMessage

object Account {
  data class ID(val id: String): Id<Data> {
    override fun init() = Data()
  }

  data class Data(@JvmField val balance: Double = 0.0)
  abstract class Msg<W: World<W>>: LocalMessage<W, ID, Data, Unit>()

  data class Open<W: World<W>>(val initial: Double): Msg<W>() {
    override fun local() = e().
      pre { initial > 0.0 }.
      app { Data(initial) }.
      eff { }.
      pst { obj().balance == initial }
  }

  data class Deposit<W: World<W>>(val amount: Double): Msg<W>() {
    override fun local() = e().
      pre { amount > 0.0 }.
      app { obj().copy(balance = obj().balance + amount) }.
      eff { }.
      pst { obj().balance == old().balance + amount }
  }

  data class Withdraw<W: World<W>>(val amount: Double): Msg<W>() {
    override fun local() = e().
      pre { amount > 0.0 }.
      app { obj().copy(balance = obj().balance + amount) }.
      eff { }.
      pst { obj().balance == old().balance + amount }
  }
}