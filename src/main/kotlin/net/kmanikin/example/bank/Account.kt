package net.kmanikin.example.bank

import net.kmanikin.core.*
import net.kmanikin.message.LocalMessage

object Account {
  data class ID(val id: String): Id<Data> {
    override fun init() = Data()
  }

  data class Data(val balance: Double = 0.0)
  abstract class Msg<W: World<W>>: LocalMessage.LMsg<W, ID, Data, Unit>()

  data class Open<W: World<W>>(val initial: Double): Msg<W>() {
    override fun pre() = initial > 0
    override fun app() = Data(initial)
    override fun eff() { }
    override fun pst() = obj().balance == initial
  }

  data class Deposit<W: World<W>>(val amount: Double): Msg<W>() {
    override fun pre() = amount > 0
    override fun app() = Data(obj().balance + amount)
    override fun eff() { }
    override fun pst() = obj().balance == old().balance + amount
  }

  data class Withdraw<W: World<W>>(val amount: Double): Msg<W>() {
    override fun pre() = amount > 0
    override fun app() = Data(obj().balance - amount)
    override fun eff() { }
    override fun pst() = obj().balance == old().balance - amount
  }
}