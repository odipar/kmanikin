# Manikin

The Kotlin version of [Manikin](https://github.com/odipar/jmanikin).
See also the Scala [version](https://github.com/odipar/smanikin).

### Bank example
```kotlin
object Bank {
  data class AccountId(val id: String): Id<Account> { override fun init() = Account() }
  data class Account(val balance: Double = 0.0)
  interface AccountMsg: LocalMessage<AccountId, Account, Unit>

  data class Open(val initial: Double): AccountMsg {
    override fun local() =
      pre { initial > 0.0 }.
      app { Account(initial) }.
      eff { }.
      pst { obj().balance == initial }!!
  }

  data class Deposit(val amount: Double): AccountMsg {
    override fun local() =
      pre { amount > 0.0 }.
      app { obj().copy(balance = obj().balance + amount) }.
      eff { }.
      pst { obj().balance == old().balance + amount }!!
  }

  data class Withdraw(val amount: Double): AccountMsg {
    override fun local() =
      pre { amount > 0.0 }.
      app { obj().copy(balance = obj().balance - amount) }.
      eff { }.
      pst { obj().balance == old().balance - amount }!!
  }

  data class TransferId(val id: Long): Id<Transfer> { override fun init() = Transfer() }
  data class Transfer(val from: AccountId? = null, val to: AccountId? = null, val amount: Double = 0.0)
  interface Msg: LocalMessage<TransferId, Transfer, Unit>

  data class Book(val from: AccountId, val to: AccountId, val amount: Double): Msg {
    override fun local() =
      pre { amount > 0 && from != to }.
      app { Transfer(from, to, amount) }.
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

  println(result.obj(a1).value().balance) // 20.0
  println(result.obj(a2).value().balance) // 110.
}
```


