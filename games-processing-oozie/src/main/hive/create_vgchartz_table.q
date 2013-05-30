CREATE EXTERNAL TABLE vgchartz_avro
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.avro.AvroSerDe'
STORED AS
INPUTFORMAT 'org.apache.hadoop.hive.ql.io.avro.AvroContainerInputFormat'
OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.avro.AvroContainerOutputFormat'
LOCATION 'hdfs://solo-cluster/example/output/vgchartz'
TBLPROPERTIES
('avro.schema.url'='hdfs://solo-cluster/user/oozie/games-processing-oozie-1.0-SNAPSHOT/models/vgchartz_game.avsc');