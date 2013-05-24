package com.mkwhit.fns;

import com.mkwhit.avro.Game;
import org.apache.crunch.MapFn;
import org.apache.crunch.Pair;


public class CalculateAvgSalesFn extends MapFn<Pair<String, Iterable<Game>>,Pair<String, Double>> {

    public static enum SalesType{
        GA_SALES,
        NA_SALES,
        EU_SALES,
        ROW_SALES,
        JP_SALES,
    }

    private final SalesType type;

    public CalculateAvgSalesFn(SalesType type){
        this.type = type;
    }


    @Override
    public Pair<String, Double> map(final Pair<String, Iterable<Game>> input) {
        long count = 0;
        double sum = 0;

        for(Game game: input.second()){
            float sales = 0;

            //note a more functional approach would be to have this injected as a function.
            switch(type){
                case GA_SALES:
                    sales = game.getGlobalSales();
                    break;
                case NA_SALES:
                    sales = game.getNaSales();
                    break;
                case EU_SALES:
                    sales = game.getEuropeSales();
                    break;
                case ROW_SALES:
                    sales = game.getRowSales();
                    break;
                case JP_SALES:
                    sales = game.getJapanSales();
                    break;
                default:
            }

            if(sales > 0){
                count++;
                sum+=sales;
            }
        }

        return new Pair<String, Double>(input.first(), count != 0 ? sum/count: 0);
    }
}
