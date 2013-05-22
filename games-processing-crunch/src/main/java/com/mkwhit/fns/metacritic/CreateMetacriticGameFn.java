package com.mkwhit.fns.metacritic;


import com.mkwhit.avro.MetaCriticGame;
import com.mkwhit.avro.MetaCriticGame.Builder;
import org.apache.crunch.CrunchRuntimeException;
import org.apache.crunch.DoFn;
import org.apache.crunch.Emitter;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.List;

/**
 * Parses the String JSON into an Avro Object representing the game.
 */
public class CreateMetacriticGameFn extends DoFn <String, MetaCriticGame>{

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = -3877196635421552289L;
    private ObjectMapper mapper;

    @Override
    public void initialize() {
        super.initialize();
        mapper = new ObjectMapper();
    }

    @Override
    public void process(final String input, final Emitter<MetaCriticGame> emitter) {
        try {
            List<MetaCriticGamePOJO> games = parseGames(input);
            for(MetaCriticGamePOJO pojo: games){
                emitter.emit(adaptPOJO(pojo));
            }
        } catch (IOException e) {
            throw new CrunchRuntimeException(e);
        }
    }

    private List<MetaCriticGamePOJO> parseGames(String rawJson) throws IOException {
        return mapper.readValue(rawJson, mapper.getTypeFactory().constructCollectionType(List.class, MetaCriticGamePOJO.class));
    }

    private MetaCriticGame adaptPOJO(MetaCriticGamePOJO pojo){
        Builder builder = MetaCriticGame.newBuilder();
        builder.setName(pojo.getGameName());
        builder.setScore(Integer.valueOf(pojo.getMetacriticScore()));
        builder.setUserScore(Float.valueOf(pojo.getUserScore()));
        return builder.build();
    }
}
