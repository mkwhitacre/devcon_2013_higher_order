games-processing-crunch
===============

The purpose of this project is to provide a runnable example of a Crunch processing job.  The processing being of video game data is does as demonstration
to help users understand how to compose complex pipelines.  The processing done in this project is not the most efficient, error proof, or what would be
considered production quality code.  The purpose of this code is to help individual easily understand the what the code is doing.

This project exists are part of the [Getting Started Guide](https://wiki.ucern.com/display/pophealth/Getting+Started+with+Apache+Crunch).
For more information on Crunch and how to get started check there.

Building
========

The project is configured to be easily built using Maven version 3.0.4.  After cloning the repository the project can be easily built
with the following command:

`mvn clean package`

The jar generated in the *target/* folder (e.g. ~/target/games-processing-crunch-1.0-SNAPSHOT.jar) can then be copied to a Hadoop node for
execution of the jobs.

Games Processing
===================

The games processing example provides a processing pipeline that reads in JSON and "enhanced HTML" to build business objects and perform
calculations on the data to answer some fictitious questions and requirements.  The output of the job is stored values in HDFS and calculations
results on the console.  The directory in HDFS should be cleaned up between runs or a new output directory should be specified.
Refer to the Cleanup section for how to remove the data when done.

Running
-------

To run the example enter the following command on the Hadoop node:

`hadoop jar game-processing-crunch-1.0-SNAPSHOT.jar com.mkwhit.Main <VgChartz HTML directory in HDFS> <Metacritic JSON directory in HDFS> <Output Destination>`

So as an example the command might be:

`hadoop jar games-processing-crunch-1.0-SNAPSHOT.jar com.mkwhit.Main /example/game_bigdata/game_bigdata/vgchartz.com/data/vgchartz.com_processed /example/game_bigdata/game_bigdata/metacritic.com/data/metacritic.com_processed /example/output`

You can use the link provided to watch the progress of the job.

It will kick off several Jobs but when completed the user will see the output of several calculations in the console as well as the following directories
created in HDFS

* <output directory>/combined
* <output directory>/metacritic
* <output directory>/vgchartz

The data in those directories are Avro files that can easily be inputs to new jobs or loaded into other tools like Hive for analysis.

Cleanup
-------

To clean up the newly created table you would use Hadoop FS command to execute the following commands:

`hadoop fs -rmr <output directory>`







hadoop jar /vagrant/games-processing-crunch/target/games-processing-crunch-1.0-SNAPSHOT.jar com.mkwhit.Main /example/game_bigdata/game_bigdata/vgchartz.com/data/vgchartz.com_processed /example/game_bigdata/game_bigdata/metacritic.com/data/metacritic.com_processed /example/output