package com.mkwhit.fns.metacritic;

import com.mkwhit.ModelHelper;
import com.mkwhit.avro.MetaCriticGame;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ExtractMetaCriticNameFnTest {

    private ExtractMetacriticNameFn fn;

    @Before
    public void create(){
        fn = new ExtractMetacriticNameFn();
    }

    @Test
    public void getName(){
        String name = "gameName";
        MetaCriticGame game = ModelHelper.createMetaCriticGame(name, "xbox1", 100, 15.0f);
        String key = fn.map(game);
        assertThat(key, is(name.toLowerCase(Locale.US)));
    }
}
