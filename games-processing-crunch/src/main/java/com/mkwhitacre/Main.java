package com.mkwhitacre;

import com.mkwhitacre.avro.Game;
import com.mkwhitacre.avro.MetaCriticGame;
import com.mkwhitacre.avro.VgChartzGame;
import com.mkwhitacre.fns.CalculateAvgSalesFn;
import com.mkwhitacre.fns.CalculateAvgSalesFn.SalesType;
import com.mkwhitacre.fns.CalculateAvgScoreFn;
import com.mkwhitacre.fns.CalculateAvgScoreFn.ScoreType;
import com.mkwhitacre.fns.CalculateSalesPercentFn;
import com.mkwhitacre.fns.CreateGameModelFn;
import com.mkwhitacre.fns.ExtractKeyFn;
import com.mkwhitacre.fns.ExtractKeyFn.Field;
import com.mkwhitacre.fns.metacritic.CreateMetacriticGameFn;
import com.mkwhitacre.fns.metacritic.ExtractMetacriticNameFn;
import com.mkwhitacre.fns.vgchartz.CreateVGChartzFn;
import com.mkwhitacre.fns.vgchartz.ExtractVgChartzNameFn;
import org.apache.crunch.PCollection;
import org.apache.crunch.PGroupedTable;
import org.apache.crunch.PTable;
import org.apache.crunch.Pair;
import org.apache.crunch.Pipeline;
import org.apache.crunch.impl.mr.MRPipeline;
import org.apache.crunch.io.avro.AvroFileTarget;
import org.apache.crunch.types.PType;
import org.apache.crunch.types.avro.Avros;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Main execution point of the project which kicks off the processing pipeline.
 */
public class Main implements Tool {

    private static final PType<MetaCriticGame> METACRITIC_PTYPE = Avros.records(MetaCriticGame.class);
    private static final PType<VgChartzGame> VG_CHARTZ_GAME_PTYPE_PTYPE = Avros.records(VgChartzGame.class);
    private static final PType<Game> GAME_PTYPE = Avros.records(Game.class);

    private Configuration config;

    public static void main(String[] args) throws Exception {
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
        String outputPath = args[2];

        Pipeline pipeline = new MRPipeline(Main.class, getConf());

        //read in from vgcharts
        PCollection<String> vgChartzRaw = pipeline.readTextFile(vgChartzPath);

        //read in from metacritic
        PCollection<String> metacriticRaw = pipeline.readTextFile(metacriticPath);

        //translate into model objects
        PCollection<VgChartzGame> vgChartzGames = vgChartzRaw.parallelDo("Convert HTML into VGChartz Object",
                new CreateVGChartzFn(), VG_CHARTZ_GAME_PTYPE_PTYPE);

        PCollection<MetaCriticGame> metacriticGames = metacriticRaw.parallelDo("Convert JSON into Metacritic Object",
                new CreateMetacriticGameFn(), METACRITIC_PTYPE);

        //Extract the name of the models to create a PTable keyed on the games name.
        PTable<String, MetaCriticGame> keyedMetaCriticGames = metacriticGames.by("Extract Name of MetaCritic Game", new ExtractMetacriticNameFn(), Avros.strings());
        PTable<String, VgChartzGame> keyedVgChartzGames = vgChartzGames.by("Extract Name of VgChartz Game", new ExtractVgChartzNameFn(), Avros.strings());

        //perform an inner join of the collections to use the information together.
        PTable<String, Pair<MetaCriticGame, VgChartzGame>> groupedGames = keyedMetaCriticGames.join(keyedVgChartzGames);

        //convert the separate models into a single one
        PCollection<Game> gameModels = groupedGames.parallelDo("Create Universal Model Object", new CreateGameModelFn(), GAME_PTYPE);

        //store off main results that can be analyzed later.
        pipeline.write(gameModels, new AvroFileTarget(new Path(outputPath, "combined")));
        pipeline.write(metacriticGames, new AvroFileTarget(new Path(outputPath, "metacritic")));
        pipeline.write(vgChartzGames, new AvroFileTarget(new Path(outputPath, "vgchartz")));

        //create new tables keyed off of various attributes like platform, publisher, and genre
        PTable<String, Game> platformModels = gameModels.by("Extract Platform", new ExtractKeyFn(Field.PLATFORM), Avros.strings());
        PTable<String, Game> publisherModels = gameModels.by("Extract Publisher", new ExtractKeyFn(Field.PUBLISHER), Avros.strings());
        PTable<String, Game> genreModels = gameModels.by("Extract Genre", new ExtractKeyFn(Field.GENRE), Avros.strings());

        //group by the keys so that exact values can be calculated
        PGroupedTable<String, Game> groupedByGenre = genreModels.groupByKey();
        PGroupedTable<String, Game> groupedByPublisher = publisherModels.groupByKey();
        PGroupedTable<String, Game> groupedByPlatform = platformModels.groupByKey();

        //calculate averages global sales by platform
        PTable<String, Double> avgSalesByPlatform = groupedByPlatform.parallelDo("Calculate Avg Global Sales by Platform",
                new CalculateAvgSalesFn(SalesType.GA_SALES), Avros.tableOf(Avros.strings(), Avros.doubles()));

        //calculate the average metacritic score by publisher
        PTable<String, Double> avgScoresByPublisher = groupedByPublisher.parallelDo("Calculate Avg Editor Score by Platform",
                new CalculateAvgScoreFn(ScoreType.EDITOR), Avros.tableOf(Avros.strings(), Avros.doubles()));

        //calculate the percentage of global sales done in North America
        PTable<String, Double> avgPercentSalesByGenre = groupedByGenre.parallelDo("Calculate Percentage of Global Sales Done in North America by Genre",
                new CalculateSalesPercentFn(SalesType.NA_SALES), Avros.tableOf(Avros.strings(), Avros.doubles()));

        pipeline.run();

        System.out.println("Average Global Sales By Platform");
        for(Pair<String, Double> pair: avgSalesByPlatform.materialize()){
            System.out.println(pair.first()+":"+pair.second());
        }

        System.out.println("Average Scores By Publisher");
        for(Pair<String, Double> pair: avgScoresByPublisher.materialize()){
            System.out.println(pair.first()+":"+pair.second());
        }

        System.out.println("Average Percentage North America Sales By Genre");
        for(Pair<String, Double> pair: avgPercentSalesByGenre.materialize()){
            System.out.println(pair.first()+":"+pair.second());
        }

        return 0;
    }
}
