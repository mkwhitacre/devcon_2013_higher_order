package com.mkwhit.fns.vgchartz;

import com.mkwhit.avro.VgChartzGame;
import org.apache.crunch.Emitter;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.LinkedList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CreateVgChartzFnTest {

    private static final String EXAMPLE_ROW = "<tr> <td>1</td> <td><a href=\"http://www.vgchartz.com/game/50395/super-mario-3d-land/\">Super Mario 3D Land</a></td> <td><a href=\"http://www.vgchartz.com/platform/42/nintendo-3ds/\">3DS</a></td> <td>2011</td> <td>Platform</td> <td>Nintendo</td> <td><center>3.68</center></td> <td><center>2.13</center></td> <td><center>1.83</center></td> <td><center>0.54</center></td> <td><center>8.18</center></td> </tr>";


    private static final String EXPECTED_PLATFORM = "3DS";
    private static final String EXPECTED_NAME = "Super Mario 3D Land";
    private static final String EXPECTED_GENRE = "Platform";
    private static final String EXPECTED_PUBLISHER = "Nintendo";
    private static final int EXPECTED_YEAR = 2011;
    private static final int EXPECTED_POSITION = 1;
    private static final float EXPECTED_NA_SALES = 3.68f;
    private static final float EXPECTED_EU_SALES = 2.13f;
    private static final float EXPECTED_JP_SALES = 1.83f;
    private static final float EXPECTED_ROW_SALES = 0.54f;
    private static final float EXPECTED_GLOBAL_SALES = 8.18f;
    private CreateVGChartzFn fn;
    private FakeEmitter emitter;

    @Before
    public void createFn(){
        fn = new CreateVGChartzFn();
        emitter = new FakeEmitter();
    }

    @Test
    public void createGame(){
        fn.process(EXAMPLE_ROW, emitter);

        assertThat(emitter.emittedValues.size(), is(1));

        VgChartzGame game = emitter.emittedValues.get(0);
        assertThat(game.getName().toString(), is(EXPECTED_NAME));
        assertThat(game.getPlatform().toString(), is(EXPECTED_PLATFORM));
        assertThat(game.getPublisher().toString(), is(EXPECTED_PUBLISHER));
        assertThat(game.getGenre().toString(), is(EXPECTED_GENRE));
        assertThat(game.getPosition(), is(EXPECTED_POSITION));
        assertThat(game.getYear(), is(EXPECTED_YEAR));
        assertThat(game.getNaSales(), is(EXPECTED_NA_SALES));
        assertThat(game.getJapanSales(), is(EXPECTED_JP_SALES));
        assertThat(game.getEuropeSales(), is(EXPECTED_EU_SALES));
        assertThat(game.getRowSales(), is(EXPECTED_ROW_SALES));
        assertThat(game.getGlobalSales(), is(EXPECTED_GLOBAL_SALES));
    }

    private static class FakeEmitter implements Emitter<VgChartzGame>{

        List<VgChartzGame> emittedValues = new LinkedList<VgChartzGame>();

        @Override
        public void emit(final VgChartzGame emitted) {
            emittedValues.add(emitted);
        }

        @Override
        public void flush() {

        }
    }

}
