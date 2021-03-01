# Manikin

The Kotlin version of [Manikin](https://github.com/odipar/jmanikin).
See also the Scala [version](https://github.com/odipar/smanikin).

### Bank example
```kotlin
package net.manikin.kotlin.example.bank

import org.jmanikin.core.*
import org.jmanikin.message.LocalMessage
import org.jmanikin.world.SimpleWorld

object Bank {
  data class AccountId(val id: String): Id<Account> { override fun init() = Account() }
  data class Account(val balance: Double = 0.0)
  interface AccountMsg<W: World<W>>: LocalMessage<W, AccountId, Account, Unit>

  data class Open<W: World<W>>(val initial: Double): AccountMsg<W> {
    override fun local() =
      pre { initial > 0.0 }.
      app { Account(initial) }.
      eff { }.
      pst { obj().balance == initial }!!
  }

  data class Deposit<W: World<W>>(val amount: Double): AccountMsg<W> {
    override fun local() =
      pre { amount > 0.0 }.
      app { obj().copy(balance = obj().balance + amount) }.
      eff { }.
      pst { obj().balance == old().balance + amount }!!
  }

  data class Withdraw<W: World<W>>(val amount: Double): AccountMsg<W> {
    override fun local() =
      pre { amount > 0.0 }.
      app { obj().copy(balance = obj().balance - amount) }.
      eff { }.
      pst { obj().balance == old().balance - amount }!!
  }

  data class TransferId(val id: Long): Id<Data> { override fun init() = Data() }
  data class Data(val from: AccountId? = null, val to: AccountId? = null, val amount: Double = 0.0)
  interface Msg<W: World<W>>: LocalMessage<W, TransferId, Data, Unit>

  data class Book<W: World<W>>(val from: AccountId, val to: AccountId, val amount: Double): Msg<W> {
    override fun local() =
      pre { amount > 0 && from != to }.
      app { Data(from, to, amount) }.
      eff { send(from, Withdraw(amount)); send(to, Deposit(amount)) }.
      pst { obj(from).balance + obj(to).balance == old(from).balance + old(to).balance }!!
  }
}

fun main() {
  val a1 = Bank.AccountId("A1")
  val a2 = Bank.AccountId("A2")
  val t1 = Bank.TransferId(1)

  val world = SimpleWorld()

  val result = world.
  send(a1, Bank.Open(50.0)).
  send(a2, Bank.Open(80.0)).
  send(t1, Bank.Book(a1, a2, 30.0))

  println(result.world.obj(a1).value);
  println(result.world.obj(a2).value);
}
```


