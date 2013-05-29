package com.mkwhitacre.fns.metacritic;


import com.mkwhitacre.avro.MetaCriticGame;
import org.apache.crunch.Emitter;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.TaskInputOutputContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CreateMetacriticGameFnTest {

    private static final String EXAMPLE_JSON = "[{\"gameName\":\"Ace Combat: Assault Horizon Legacy\",\"metacriticScore\":\"71\",\"userScore\":\"7.7\"}]";
    private static final String EXPECTED_NAME = "Ace Combat: Assault Horizon Legacy";
    private static final int EXPECTED_SCORE = 71;
    private static final float EXPECTED_USER_SCORE = 7.7f;

    private CreateMetacriticGameFn fn;
    private FakeEmitter emitter;

    @Mock
    private TaskInputOutputContext context;
    @Mock
    private Counter counter;

    @Before
    public void create(){

        when(context.getCounter(Mockito.<Enum>anyObject())).thenReturn(counter);
        when(context.getCounter(anyString(), anyString())).thenReturn(counter);

        fn = new CreateMetacriticGameFn();
        fn.setContext(context);
        fn.initialize();
        emitter = new FakeEmitter();
    }

    @Test
    public void process(){
        fn.process(EXAMPLE_JSON, emitter);
        assertThat(emitter.emittedValues.size(), is(1));
        MetaCriticGame game = emitter.emittedValues.get(0);
        assertThat(game.getName().toString(), is(EXPECTED_NAME));
        assertThat(game.getScore(), is(EXPECTED_SCORE));
        assertThat(game.getUserScore(), is(EXPECTED_USER_SCORE));
    }

    private static class FakeEmitter implements Emitter<MetaCriticGame> {

        List<MetaCriticGame> emittedValues = new LinkedList<MetaCriticGame>();

        @Override
        public void emit(final MetaCriticGame emitted) {
            emittedValues.add(emitted);
        }

        @Override
        public void flush() {

        }
    }


}
