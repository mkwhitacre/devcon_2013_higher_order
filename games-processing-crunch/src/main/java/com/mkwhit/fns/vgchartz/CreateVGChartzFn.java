package com.mkwhit.fns.vgchartz;

import com.mkwhit.avro.VgChartzGame;
import org.apache.crunch.DoFn;
import org.apache.crunch.Emitter;

/**
 * Convert the raw HTML into row into a business object.
 */
public class CreateVGChartzFn extends DoFn <String, VgChartzGame> {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -7146427000625497789L;

    @Override
    public void process(final String input, final Emitter<VgChartzGame> emitter) {
        VgChartzGame game = parseGame(input);
        //only emit if the string represented a game.
        if(game != null){
            getCounter("VgChartz", "createdgame").increment(1);
            emitter.emit(game);
        }
    }

    /**
     * Returns model object for the game represented in the {@code rawString}.  If the string
     * does not represent a game {@code null} will be returned.
     *
     * @param rawString The raw representation in HTML.
     * @return the Avro object or {@code null}.
     */
    private VgChartzGame parseGame(String rawString){
        VgChartzGame game = null;

        if(representsGame(rawString)){
            VgChartzGame.Builder builder = VgChartzGame.newBuilder();


            //Not the cleanest way of parsing out the values from the string but effective
            String[] tds = rawString.split("</td>");
            //pull off position
            int rawPosition = Integer.valueOf(tds[0].replaceAll("<tr>", "").replaceAll("<td>", "").replaceAll("</td>", "").trim());
            builder.setPosition(rawPosition);

            //pull off name
            String name = tds[1].replaceAll("<td>", "").replaceAll("</td>", "").replaceAll("</a>", "").trim();
            name = name.substring(name.lastIndexOf(">")+1, name.length());
            builder.setName(name);

            //pull off platform
            String platform = tds[2].replaceAll("<td>", "").replaceAll("</td>", "").replaceAll("</a>", "").trim();
            platform = platform.substring(platform.lastIndexOf(">")+1, platform.length());
            builder.setPlatform(platform);

            //pull off year
            try{
                int year = Integer.valueOf(tds[3].replaceAll("<td>", "").replaceAll("</td>", "").trim());
                builder.setYear(year);
            }catch(NumberFormatException nfe){
                getCounter("VgChartz", "missingyear").increment(1);
            }

            //pull off platform
            String genre = tds[4].replaceAll("<td>", "").replaceAll("</td>", "").trim();
            builder.setGenre(genre);

            //pull off publisher
            String publisher = tds[5].replaceAll("<td>", "").replaceAll("</td>", "").trim();
            builder.setPublisher(publisher);

            //pull off NA Sales
            float naSales = extractSales(tds[6]);
            builder.setNaSales(naSales);

            //pull off EU Sales
            float euSales = extractSales(tds[7]);
            builder.setEuropeSales(euSales);

            //pull off Japan Sales
            float japanSales = extractSales(tds[8]);
            builder.setJapanSales(japanSales);

            //pull off ROW Sales
            float rowSales = extractSales(tds[9]);
            builder.setRowSales(rowSales);

            //pull off Global Sales
            float globalSales = extractSales(tds[10]);
            builder.setGlobalSales(globalSales);

            game = builder.build();
        }

        return game;
    }

    private boolean representsGame(String rawString){
        return rawString.contains("http://www.vgchartz.com/game/") && !rawString.startsWith("<!DOCTYPE html PUBLIC");
    }

    private float extractSales(String sales){
        String strippedSales = sales.replaceAll("<td>", "").replaceAll("<center>", "")
                .replaceAll("</center>","").replaceAll("</td>","").replaceAll("</tr>", "").trim();
        return Float.valueOf(strippedSales);
    }

}
