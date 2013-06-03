package com.mkwhitacre.fns;


import com.mkwhitacre.avro.Game;
import org.apache.crunch.MapFn;

import java.util.Locale;

/**
 * Pulls a key value off of a {@link Game} instance based on one of its fields.  The field to use is specified by the {@link Field}.
 */
public class ExtractKeyFn extends MapFn<Game, String> {

    private static final long serialVersionUID = 5935475957482738801L;

    public static enum Field {
        PLATFORM,
        PUBLISHER,
        GENRE,
    }

    private final Field keyField;

    public ExtractKeyFn(Field keyField) {
        this.keyField = keyField;
    }

    @Override
    public String map(final Game input) {

        String key = null;
        switch (keyField) {
            case PLATFORM:
                key = input.getPlatform().toString();
                break;
            case PUBLISHER:
                key = input.getPublisher().toString();
                break;
            case GENRE:
                key = input.getGenre().toString();
                break;
            default:
                key = input.getName().toString();
        }

        return key.toLowerCase(Locale.US);
    }
}
