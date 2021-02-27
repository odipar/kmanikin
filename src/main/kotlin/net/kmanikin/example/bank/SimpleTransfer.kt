package net.kmanikin.example.bank

import net.kmanikin.world.SimpleWorld

fun main() {
  val a1 = Account.ID("A1")
  val a2 = Account.ID("A2")
  val t1 = Transfer.ID(1)

  val world = SimpleWorld()

  val result = world.
    send(a1, Account.Open(50.0)).
    send(a2, Account.Open(80.0)).
    send(t1, Transfer.Book(a1, a2, 30.0))

  println(result.world.state)
}