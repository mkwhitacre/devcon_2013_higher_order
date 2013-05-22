package com.mkwhit;

import com.mkwhit.avro.Game;
import com.mkwhit.avro.MetaCriticGame;
import com.mkwhit.avro.VgChartzGame;
import com.mkwhit.fns.metacritic.CreateMetacriticGameFn;
import com.mkwhit.fns.metacritic.ExtractMetacriticNameFn;
import com.mkwhit.fns.vgchartz.CreateVGChartzFn;
import com.mkwhit.fns.vgchartz.ExtractVgChartzNameFn;
import org.apache.crunch.PCollection;
import org.apache.crunch.PTable;
import org.apache.crunch.Pair;
import org.apache.crunch.Pipeline;
import org.apache.crunch.impl.mr.MRPipeline;
import org.apache.crunch.types.PType;
import org.apache.crunch.types.avro.Avros;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.util.Collection;

public class Main implements Tool{

    private static final PType<MetaCriticGame> METACRITIC_PTYPE = Avros.records(MetaCriticGame.class);
    private static final PType<VgChartzGame> VG_CHARTZ_GAME_PTYPE_PTYPE = Avros.records(VgChartzGame.class);
    private static final PType<Game> GAME_PTYPE = Avros.records(Game.class);

    private Configuration config;

    public static void main(String[] args) throws Exception {
        Configuration config = HBaseConfiguration.create();
        int result = ToolRunner.run(HBaseConfiguration.create(), new Main(), args);
        System.exit(result);
    }

    @Override
    public void setConf(Configuration conf) {
        config = conf;
    }

    @Override
    public Configuration getConf() {
        return config;
    }

    @Override
    public int run(String[] args) throws Exception {

        String vgChartzPath = args[0];
        String metacriticPath = args[1];


        Pipeline pipeline = new MRPipeline(Main.class, getConf());

        //read in from vgcharts
        PCollection<String> vgChartzRaw = pipeline.readTextFile(vgChartzPath);

        //read in from metacritic
        PCollection<String> metacriticRaw = pipeline.readTextFile(metacriticPath);

        //translate into model objects
        PCollection<VgChartzGame> vgChartzGames = vgChartzRaw.parallelDo("Convert HTML into VGChartz Object",
                new CreateVGChartzFn(), VG_CHARTZ_GAME_PTYPE_PTYPE);

        PCollection<MetaCriticGame> metacriticGames = metacriticRaw.parallelDo("Convert JSON into Metacritic Object",
                new CreateMetacriticGameFn(), Avros.records(MetaCriticGame.class));

        PTable<String, MetaCriticGame> keyedMetaCriticGames = metacriticGames.by(new ExtractMetacriticNameFn(), Avros.strings());
        PTable<String, VgChartzGame> keyedVgChartzGames = vgChartzGames.by(new ExtractVgChartzNameFn(), Avros.strings());

        //cogroup the values.
        PTable<String, Pair<Collection<MetaCriticGame>, Collection<VgChartzGame>>> groupedGames = keyedMetaCriticGames.cogroup(keyedVgChartzGames);


        //store off main results


        //Filter out missing sales

        //filter out unranked games


        ///do the special calculations



        pipeline.run();



        return 0;
    }
}
