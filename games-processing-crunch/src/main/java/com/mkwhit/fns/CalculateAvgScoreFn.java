package com.mkwhit.fns;

import com.mkwhit.avro.Game;
import org.apache.crunch.MapFn;
import org.apache.crunch.Pair;


public class CalculateAvgScoreFn extends MapFn<Pair<String, Iterable<Game>>,Pair<String, Double>> {

    public static enum ScoreType{
        EDITOR,
        USER,
    }

    private final ScoreType type;

    public CalculateAvgScoreFn(ScoreType type){
        this.type = type;
    }


    @Override
    public Pair<String, Double> map(final Pair<String, Iterable<Game>> input) {
        long count = 0;
        double sum = 0;

        for(Game game: input.second()){
            float score = 0;

            //note a more functional approach would be to have this injected as a function.
            switch(type){
                case USER:
                    score = game.getUserScore();
                    break;
                case EDITOR:
                    score = game.getScore();
                    break;
                default:
            }

            if(score > 0){
                count++;
                sum+=score;
            }
        }

        return new Pair<String, Double>(input.first(), count != 0 ? sum/count: 0);
    }
}
