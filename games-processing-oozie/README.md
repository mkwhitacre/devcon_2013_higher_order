examples-crunch-oozie
===============

The purpose of this project is to provide a runnable example of a Crunch processing job that gets executed and launched through [Oozie](http://archive.cloudera.com/cdh4/cdh/4/oozie/index.html).
The workflow is pretty trivial and is intended to be used as an example.  The example will show a [workflow](http://archive.cloudera.com/cdh4/cdh/4/oozie/WorkflowFunctionalSpec.html) ran through Oozie.
The example is expected to be ran on a [Vagrant](http://www.vagrantup.com/) image which has Oozie installed.

Building
========

The project is configured to be easily built using Maven version 3.0.4.  After cloning the repository the project can be easily built
with the following command:

`mvn clean package`

The building will generate two different endstates, one which is a standard map-reduce jar and another is an Oozie assembly for
[deployment](http://archive.cloudera.com/cdh4/cdh/4/oozie/WorkflowFunctionalSpec.html#a7_Workflow_Application_Deployment).

The assembly generated in the *games-processing-oozie/target/* folder (e.g. ~/games-processing-oozie/target/games-processing-oozie-distribution-1.0-SNAPSHOT.tar.gz) will then be used
for publishing the Oozie workflow.

Deploying Applications
==================

The workflow assembly will need to be pushed into HDFS so that Oozie will have the necessary endstates to launch the workflow.

To deploy the workflow, copy the tar.gz file out to a node that can push files into HDFS and then un-tar the file.

`tar -xvzf games-processing-oozie-distribution-1.0-SNAPSHOT.tar.gz`

This should generate a directory *games-processing-oozie-1.0-SNAPSHOT/* relative to the command.

Verify that the */user/oozie* directory already exists in HDFS

`hadoop fs -ls /user/oozie`

It should look like the following:

<pre>
18:01:14 # hadoop fs -ls /user/oozie
Found 1 items
drwxr-xr-x   - oozie oozie          0 2013-02-04 17:38 /user/oozie/share
</pre>

Push out the example workflow with the following command:

`hadoop fs -put games-processing-oozie-1.0-SNAPSHOT /user/oozie/games-processing-oozie-1.0-SNAPSHOT`

Verify that the contents were pushed out:

<pre>
18:01:14 # hadoop fs -ls /user/oozie
Found 2 items
drwxr-xr-x   - root  oozie          0 2013-02-13 14:41 /user/oozie/games-processing-oozie-1.0-SNAPSHOT
drwxr-xr-x   - oozie oozie          0 2013-02-04 17:38 /user/oozie/share
</pre>

The *deploy.sh* script (src/main/scripts) is available to handle deployments of the applications.


Running the Workflow
====================

Now that the workflow has been deployed it must be submitted to Oozie to be ran.  This can be accomplished by running the following command

`sudo -u oozie oozie job -oozie http://<oozie-server>:11000/oozie -config games-processing-oozie-1.0-SNAPSHOT/workflow/job.properties -run -auth SIMPLE`

For the Vagrant image it should be the following:

`sudo -u oozie oozie job -oozie http://localhost:11000/oozie -config games-processing-oozie-1.0-SNAPSHOT/workflow/job.properties -run -auth SIMPLE`

Where the command is ran on the node with Oozie installed.

The command has a few attributes that might vary from how it is ran on a truly distributed cluster.

1. Oozie and Hadoop must be configured to allow user impersonation and the only configured value on Vagrant is "oozie".  It also corresponds to the */user/oozie* directory
which is used to point to the deployed workflow.
1. The environment does not have Kerberos installed so that is why the *-auth* is set to *SIMPLE*
1. Values in the *job.properties* file are set to be specific to the vagrant instance.

Oozie has a webui that should be accessible at `http://<oozie-server>:11000/oozie`.  This will allow you to watch the progress of the workflow and the success or failure of the workflow.


Verifying the Workflow
====================

Successful execution of the workflow will produce the following:

* Output directories which are the [same output](https://github.com/mkwhitacre/devcon_2013_higher_order/tree/master/games-processing-crunch#running) as the individual games-processing-crunch job.
* Raw calculations in the logs
* [Hive](http://hive.apache.org/) Tables (have fun using Hive to explore this manually)
 * create-vgchartz-table
 * create-metacritic-table
 * create-game-table

Stuff to Try
====================

* Create a Coordinator Application that will kick off the processing at a regular interval.
* Change the "prep" stage before the Java action to do something more meaningful like archive previous results.
* Setup notification in the case of failure or success.
