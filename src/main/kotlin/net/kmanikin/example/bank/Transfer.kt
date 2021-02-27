package net.kmanikin.example.bank

import net.kmanikin.core.Id
import net.kmanikin.core.World
import net.kmanikin.message.LocalMessage

object Transfer {
  data class ID(val id: Long): Id<Data> {
    override fun init() = Data()
  }

  data class Data(val from: Account.ID? = null, val to: Account.ID? = null, val amount: Double = 0.0)
  abstract class Msg<W: World<W>>: LocalMessage.LMsg<W, ID, Data, Unit>()

  data class Book<W: World<W>>(val from: Account.ID, val to: Account.ID, val amount: Double): Msg<W>() {
    override fun pre() = amount > 0 && from != to
    override fun app() = Data(from, to, amount)
    override fun eff() { send(from, Account.Withdraw(amount)); send(to, Account.Deposit(amount)) }
    override fun pst() = obj(from).balance + obj(to).balance == old(from).balance + old(to).balance
  }
}