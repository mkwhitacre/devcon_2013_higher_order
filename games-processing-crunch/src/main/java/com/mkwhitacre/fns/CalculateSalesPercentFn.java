package com.mkwhitacre.fns;

import com.mkwhitacre.avro.Game;
import com.mkwhitacre.fns.CalculateAvgSalesFn.SalesType;
import org.apache.crunch.MapFn;
import org.apache.crunch.Pair;

/**
 * Calculates the ratio of sales for a specific location to the overall global sales.
 */
public class CalculateSalesPercentFn extends MapFn<Pair<String, Iterable<Game>>,Pair<String, Double>> {

    private final SalesType type;

    /**
     * The type of sales to calculate the ratio of.
     */
    public CalculateSalesPercentFn(SalesType type){
        this.type = type;
    }


    @Override
    public Pair<String, Double> map(final Pair<String, Iterable<Game>> input) {
        double denominator = 0;
        double numerator = 0;

        for(Game game: input.second()){

            float sales = 0;
            float globalSales = game.getGlobalSales();

            switch(type){
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

            if(sales > 0 && globalSales > 0){
                numerator += sales;
                denominator += globalSales;
            }
        }

        return new Pair<String, Double>(input.first(), denominator != 0 ? numerator/denominator: 0);
    }
}
