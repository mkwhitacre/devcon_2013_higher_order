package com.mkwhitacre.fns.metacritic;

import com.mkwhitacre.avro.MetaCriticGame;
import org.apache.crunch.MapFn;

import java.util.Locale;

/**
 * Extracts the name of the {@link MetaCriticGame} to serve as a key.
 */
public class ExtractMetacriticNameFn extends MapFn<MetaCriticGame, String> {
    @Override
    public String map(final MetaCriticGame input) {
        //TODO add in smarts to remove spaces + non alpha-numeric characters
        return input.getName().toString().toLowerCase(Locale.US);
    }
}
