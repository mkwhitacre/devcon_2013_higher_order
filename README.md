Cerner DevCon 2013 Higher Order Data Processing
========================

This project serves as a companion to a presentation given at Cerner DevCon 2013.  It is meant to give an example of how
to build natural processing workflows and pipelines for MapReduce using with higher order libraries and frameworks
such as [Oozie](http://archive.cloudera.com/cdh4/cdh/4/oozie/) and [Crunch](http://crunch.apache.org/).  This could also be accomplished
with alternate technologies such as [Cascading](http://www.cascading.org/) and [Azkaban 2](https://github.com/azkaban/azkaban2).

The example takes you through some artificially created scenarios of video game data obtained from VgChartz.com and metacritic.com.
It is not meant to be an exemplary example of a finely tuned or highly efficient processing code but instead to help illustrate
the natural way in which you can describe a problem with these tools instead of plain MapReduce.