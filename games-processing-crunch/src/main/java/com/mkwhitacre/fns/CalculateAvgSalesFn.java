package com.mkwhitacre.fns;

import com.mkwhitacre.avro.Game;
import org.apache.crunch.MapFn;
import org.apache.crunch.Pair;


/**
 * Function which calculates the average sales per entry based on the specified {@link SalesType}.
 */
public class CalculateAvgSalesFn extends MapFn<Pair<String, Iterable<Game>>, Pair<String, Double>> {

    /**
     * Enumeration of Sales Types
     */
    public static enum SalesType {
        GA_SALES,
        NA_SALES,
        EU_SALES,
        ROW_SALES,
        JP_SALES,
    }

    private final SalesType type;

    /**
     * Creates a function which will calculate the average sales of the specified {@code type}
     *
     * @param type the type of sales to average.
     */
    public CalculateAvgSalesFn(SalesType type) {
        this.type = type;
    }


    @Override
    public Pair<String, Double> map(final Pair<String, Iterable<Game>> input) {
        long count = 0;
        double sum = 0;

        for (Game game : input.second()) {
            float sales = 0;

            switch (type) {
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

            if (sales > 0) {
                count++;
                sum += sales;
            }else{
                getCounter("AvgSales", "missingsales").increment(1);
            }
        }

        return new Pair<String, Double>(input.first(), count != 0 ? sum / count : 0);
    }
}
