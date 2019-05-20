package com.zendesk.test

import org.apache.spark.sql.SparkSession
import org.scalatest._

import org.scalatest.FunSuite

class  Tests extends FunSuite with BeforeAndAfterEach {

  var sparkSession : SparkSession = _
  override def beforeEach() {
    sparkSession = SparkSession.builder().appName("Unit Testing")
      .master("local[2]")
      .config("", "")
      .getOrCreate()
  }

  test("Data Search Test"){
    //your unit test assert here like below
    assert("True".toLowerCase == "true")
  }

  override def afterEach() {
    sparkSession.stop()
  }
}


