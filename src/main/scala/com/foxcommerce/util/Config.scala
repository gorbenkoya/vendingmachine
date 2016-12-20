package com.foxcommerce.util

import com.typesafe.config.ConfigFactory

trait Config {
  private val config = ConfigFactory.load()
  val httpHost = config.getString("http.interface")
  val httpPort = config.getInt("http.port")
}


