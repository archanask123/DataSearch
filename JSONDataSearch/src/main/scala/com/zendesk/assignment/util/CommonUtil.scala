package com.zendesk.assignment.util

import java.io.FileInputStream
import java.util.Properties
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.file.{Files, Paths}

import org.apache.spark.sql.functions.array_contains
import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.util.Try


class InvalidArgException(s:String) extends Exception(s){}

class InputException{
  @throws(classOf[InvalidArgException])
  def validateInputArgs(arguments:Int){
    if(arguments!=3){
      throw new InvalidArgException("Insufficient information Provided. Requires SearchOption, SearchTerm & SearchValue")
    }
  }
}

object CommonUtil {

  /**
    *
    * @param spark
    * @return properties
    */
  def readConfig(spark: SparkSession): Properties ={

    val properties = new Properties()

    try {

      properties.load(new FileInputStream("config.properties"))


    } catch {

      case ex: FileNotFoundException => {

        println("Config File Not Found !!!")
      }
      case ioEx: IOException => {

        ioEx.printStackTrace()
        println("Input /Output Exception")

      }
    }

    properties

  }

  /**
    *
    * @param properties
    * @param filePath
    * @return isFileExists
    */
  def isFileFound(properties: Properties, filePath: String): Boolean ={

    var isFileExists = false

    try{

      Files.exists(Paths.get(filePath))
      isFileExists = true
    } catch {
      case ex: FileNotFoundException => {

        println("File Not Found In Specified Location !!!")
      }
    }
    isFileExists

  }

  /**
    *
    * @param df
    * @param searchTerm
    * @param searchValue
    * @param dataSetName
    * @return searchResultDF
    */
  def searchOutcome(df: DataFrame, searchTerm: String, searchValue: String, dataSetName: String): DataFrame ={
    val searchResultDF = df.filter(df(searchTerm) === searchValue)
    if (searchResultDF.head(1).isEmpty){
      println("No matching record found in available dataset: " + dataSetName)
    } else {
      println("Data Found in " + dataSetName)
      searchResultDF.show(Int.MaxValue, false)
      //Alternate option to display result in JSON format
      //println("Displaying in JSON format")
      //searchResultDF.toJSON.collect.foreach(println)
    }

    searchResultDF
  }

  /**
    *
    * @param df
    * @param searchTerm
    * @param searchValue
    * @param searchOption
    * @return arrayDataSearchResult
    */
  def searchArrayData(df: DataFrame, searchTerm: String, searchValue: String, searchOption: String): DataFrame ={
    val arrayDataSearchResult = df.select("*").where(array_contains(df(searchTerm), searchValue))
    if (arrayDataSearchResult.head(1).isEmpty){
      println("No matching record found in available dataset: " + searchOption)
    } else{
      arrayDataSearchResult.show(5000, false)
      //println("Displaying in JSON format")
      //arrayDataSearchResult.toJSON.collect.foreach(println)
    }

    arrayDataSearchResult
  }

  /**
    *
    * @param df
    * @param colValue
    * @return isSuccess
    */
  def hasColumn(df: DataFrame, colValue: String) = Try(df(colValue)).isSuccess

}
