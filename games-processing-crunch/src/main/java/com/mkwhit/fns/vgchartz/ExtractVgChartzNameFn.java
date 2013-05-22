package com.mkwhit.fns.vgchartz;

import com.mkwhit.avro.VgChartzGame;
import org.apache.crunch.MapFn;

import java.util.Locale;

/**
 * Extracts the names of the game in a normalized form to match more easily.
 */
public class ExtractVgChartzNameFn extends MapFn<VgChartzGame, String> {
    @Override
    public String map(final VgChartzGame input) {
        //normalize the name to lowercase to ensure matches
        return input.getName().toString().toLowerCase(Locale.US);
    }
}
