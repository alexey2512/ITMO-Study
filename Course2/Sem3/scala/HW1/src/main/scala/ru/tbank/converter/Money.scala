package ru.tbank.converter

import Errors._

case class Money private (amount: BigDecimal, currency: String) {
  def +(other: Money): Money = {
    if (currency != other.currency)
      throw new CurrencyMismatchException
    Money(amount + other.amount, currency)
  }

  def -(other: Money): Money = {
    if (currency != other.currency)
      throw new CurrencyMismatchException
    val out = amount - other.amount
    if (out < 0)
      throw new MoneyAmountShouldBeNonNegativeException
    Money(out, currency)
  }

  def isSameCurrency(other: Money): Boolean = currency == other.currency
}

object Money {
  def apply(amount: BigDecimal, currency: String): Money = {
    if (amount < 0)
      throw new MoneyAmountShouldBeNonNegativeException
    if (!Currencies.SupportedCurrencies.contains(currency))
      throw new UnsupportedCurrencyException
    new Money(amount, currency)
  }
}
