import com.google.common.annotations.VisibleForTesting;
import nl.vu.kai.bisimulations.BisimulationGraph;
import nl.vu.kai.bisimulations.BisimulationGraphParser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class TestParsing {



    @Test
    public void testParsing() throws IOException {
        BisimulationGraph graph = BisimulationGraphParser.parseGraph(new File( "family-benchmark"));
    }
}
