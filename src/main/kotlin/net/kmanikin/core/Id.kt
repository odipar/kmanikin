package net.kmanikin.core

interface Id<out O> {
  fun init(): O
}