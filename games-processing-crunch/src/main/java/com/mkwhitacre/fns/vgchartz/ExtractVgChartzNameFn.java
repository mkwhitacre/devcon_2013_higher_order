package com.mkwhitacre.fns.vgchartz;

import com.mkwhitacre.avro.VgChartzGame;
import org.apache.crunch.MapFn;

import java.util.Locale;

/**
 * Extracts the names of the game in a normalized form to match more easily.
 */
public class ExtractVgChartzNameFn extends MapFn<VgChartzGame, String> {
    @Override
    public String map(final VgChartzGame input) {
        //normalize the name to lowercase to ensure matches
        //TODO add in smarts to remove spaces + non alpha-numeric characters
        return input.getName().toString().toLowerCase(Locale.US);
    }
}
