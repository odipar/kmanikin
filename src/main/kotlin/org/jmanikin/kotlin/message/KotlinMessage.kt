package org.jmanikin.kotlin.message

import org.jmanikin.core.*
import org.jmanikin.message.LocalMessage

interface KotlinMessage<W: World<W>, I: Id<O>, O, R>: LocalMessage<W, I, O, R> {
  override fun local(): Msg<W, I, O, R> = kotlin()
  fun kotlin(): Msg<W, I, O, R>
}