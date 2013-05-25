package com.mkwhit.fns.metacritic;

import com.mkwhit.avro.MetaCriticGame;
import org.apache.crunch.MapFn;
import org.apache.crunch.Pair;

import java.util.Locale;

public class ExtractMetacriticNameFn extends MapFn<MetaCriticGame, String> {
    @Override
    public String map(final MetaCriticGame input) {
        //TODO add in smarts to remove spaces + non alpha-numeric characters
        return input.getName().toString().toLowerCase(Locale.US);
    }
}
