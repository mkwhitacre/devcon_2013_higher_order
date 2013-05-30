#!/bin/bash

#This script assumes it is being ran in a directory right next to the games-processing-oozie-1.0-SNAPSHOT-distribution.tar.gz file.

echo "Removing local directory"
rm -rf games-processing-oozie-1.0-SNAPSHOT
echo "Untarring distribution"
tar -xvzf games-processing-oozie-1.0-SNAPSHOT-distribution.tar.gz
echo "Removing previous value from HDFS"
hadoop fs -rmr /user/oozie/games-processing-oozie-1.0-SNAPSHOT
echo "Distributing application to HDFS"
hadoop fs -put games-processing-oozie-1.0-SNAPSHOT/ /user/oozie/games-processing-oozie-1.0-SNAPSHOT