package com.mkwhit.fns.vgchartz;

import com.mkwhit.ModelHelper;
import com.mkwhit.avro.VgChartzGame;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ExtractVgChartzNameFnTest {

    private ExtractVgChartzNameFn fn;

    @Before
    public void create(){
        fn = new ExtractVgChartzNameFn();
    }

    @Test
    public void getName(){
        String name = "gameName";
        VgChartzGame game = ModelHelper.createVgChartz(name, "asdf", "hello");
        String key = fn.map(game);
        assertThat(key, is(name.toLowerCase(Locale.US)));
    }

}
