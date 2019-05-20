package com.zendesk.assignment.driver


import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession, _}
import com.zendesk.assignment.util.CommonUtil._
import com.zendesk.assignment.util.InputException
import java.io.{FileNotFoundException, FileOutputStream, PrintStream}

object DataSearch {

  def main(args: Array[String]): Unit = {


    // Initializing Spark Session
    val spark = SparkSession.builder().appName("Data Search").master("local[*]").getOrCreate()

    // Read properties file
    val properties = readConfig(spark)

    System.setOut(new PrintStream(new FileOutputStream(properties.getProperty("file.output.path"))))

    println("Starting Data Search")


    var inpExpCheck = new InputException()
    try{
      inpExpCheck.validateInputArgs(args.length)
    }catch{
      case inpExp : Exception => println("Exception Occurred : "+ inpExp)
        System.exit(1)
    }

    // Storing the input args
    val searchOption = args(0)
    val searchTerm = args(1)
    val searchValue = args(2)

    val organizationJsonFilePath = properties.getProperty("spark.organizationsJsonFilePath")
    val ticketJsonFilePath = properties.getProperty("spark.ticketsJsonFilePath")
    val userJsonFilePath = properties.getProperty("spark.usersJsonFilePath")

    if((isFileFound(properties, organizationJsonFilePath)) &&((isFileFound(properties, ticketJsonFilePath))) && ((isFileFound(properties, userJsonFilePath)))){

      val organizationDF= spark.read.json(organizationJsonFilePath)
      organizationDF.createOrReplaceTempView("organization")

      val ticketDF= spark.read.json(ticketJsonFilePath)
      ticketDF.createOrReplaceTempView("ticket")

      val userDF= spark.read.json(userJsonFilePath)
      userDF.createOrReplaceTempView("user")

      // Creating a map to relate searchOption and corresponding DataFrame
      val searchOptionMap = Map("Users" -> userDF, "Tickets" -> ticketDF , "Organizations" -> organizationDF)
      val relatedDataMap = Map("_id" -> "organization_id")

      //Check if searchOption value is correct
      if(searchOptionMap.keySet.exists(_ == searchOption)){
        println("Search Option: " + searchOption)


        //Check if searchTerm is valid
        if(hasColumn(searchOptionMap(searchOption), searchTerm)){

          println("Search Term: " + searchTerm)
          println("Search Value: " + searchValue)
          //Check if searchValue is present
          val searchResultDF = searchOutcome(searchOptionMap(searchOption), searchTerm, searchValue, searchOption)

          /* Below code can be used if we need the output to be displayed in JSON format
             Currently commenting it out as data is displayed in tabular format
             // searchResultDF.toJSON.collect.foreach(println)
           */


          if ((searchOption == "Organizations") && (searchTerm == "_id")){
            // Check for related data in userDF & ticketDF too
            println("Displaying related data")
            searchOutcome(userDF, relatedDataMap(searchTerm), searchValue, "users")
            searchOutcome(ticketDF, relatedDataMap(searchTerm), searchValue, "tickets")

          }


        } else {

          println("Search Term Not Found In selected Search Option. Check Main Option #2 for list of available search keys")
        }

      } else {
        println("Unknown Search Option")

      }


    }







  }


}
