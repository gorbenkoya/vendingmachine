package com.foxcommerce.model

trait ProductType {
  def getName: String = getClass.getSimpleName.toLowerCase.dropRight(1)
}

