

Zendesk Data Search
================

Zendesk Data Search tool provides the option to search for information within the available dataset. 

## Installation
```bash
Install JDK 8 or above
Install Apache Spark
Install Scala
Install Sbt (Currently used sbt tool to build project. Can also use maven)
```


**Wrapper Script**

search.sh script acts as the wrapper script that provides the Selection Menu for searching the data. Script then invokes Spark Scala code to return the search result.

## Usage

```bash
sh search.sh 
```

`More Information:`

Wrapper script currently uses local mode of spark execution, if more data is expected and/or needs to be distributed, use cluster mode for execution.

**Before Execution:**

1. Edit Wrapper Script

Open wrapper script
Update directory JOB_HOME_DIR specific to your location. All the other directories names are built from it
NB: If you wish to update it otherwise, ensure that the corresponding paths are updated in wrapper script
 Output of search is also written to a file. The path to this file is specified in wrapper script and config.properties file

2. Edit config.properties

Paths to various JSON files and the search result output file is specified in this file
Update to match the file paths

NB: Currently JSON files are put in as a single line. If you wish to process a pretty formatted JSON, update the code to read JSON file as below:
```scala
val df = spark.read.option("multiline", "true").json("filename.json")

```

**Build Project**

```bash
    sbt clean
    sbt package
```

Once the project is built, latest jar file will be present in project location “JSONDataSearch/target/scala-2.11”

*Copy jar file to Job execution home directory(specified in wrapper script). 
If you wish to run from a different location and/or need to change the jar name, **update the JAR file path in wrapper script** 

