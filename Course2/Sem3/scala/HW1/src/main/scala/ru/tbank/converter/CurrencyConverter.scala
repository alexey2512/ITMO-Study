package ru.tbank.converter

import Errors._
import Currencies.SupportedCurrencies

class CurrencyConverter(ratesDictionary: Map[String, Map[String, BigDecimal]]) {
  def exchange(money: Money, toCurrency: String): Money = {
    if (money.currency == toCurrency)
      throw new SameCurrencyExchangeException
    // although tests do not check this, those conditions are necessary, cause nothing guarantee any of them
    if (
      !SupportedCurrencies.contains(toCurrency) ||
        !ratesDictionary.contains(money.currency) ||
        !ratesDictionary(money.currency).contains(toCurrency)
    )
      throw new UnsupportedCurrencyException
    Money(money.amount * ratesDictionary(money.currency)(toCurrency), toCurrency)
  }
}

object CurrencyConverter {

  def apply(ratesDictionary: Map[String, Map[String, BigDecimal]]): CurrencyConverter = {
    val fromCurrencies = ratesDictionary.keys
    val toCurrencies = ratesDictionary.values
    if (
      fromCurrencies.toSet
        .subsetOf(SupportedCurrencies) && toCurrencies.forall(_.keys.toSet.subsetOf(SupportedCurrencies))
    ) new CurrencyConverter(ratesDictionary)
    else throw new UnsupportedCurrencyException
  }
}
