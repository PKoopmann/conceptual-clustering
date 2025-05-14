package nl.vu.kai.bisimulations;


import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BisimulationGraphParser {

    public static final String FOLDER = "rdf_summary_graph";

    public static BisimulationGraph parseGraph(File folder) throws IOException {
        OWLDataFactory factory = OWLManager.getOWLDataFactory();

        File sizesFile = new File(folder.getAbsolutePath() + File.separator + FOLDER+File.separator+"sizes.nt");
        File dataFile = new File(folder.getAbsolutePath() + File.separator +  FOLDER+File.separator+"data.nt");
        File refinesFile = new File(folder.getAbsolutePath() + File.separator +  FOLDER+File.separator+"refines.nt");

        BisimulationGraph result = new BisimulationGraph();

        BufferedReader reader = new BufferedReader(new FileReader(sizesFile));
        for (String line; (line = reader.readLine()) != null; ) {
            String components[] = line.split(" ");
            BisimulationNode node = new BisimulationNode();
            node.setSize(Integer.parseInt(components[2].substring(1, components[2].length()-1)));
            result.addGraph(components[0].substring(1, components[2].length()-1), node);
        }

        reader = new BufferedReader(new FileReader(dataFile));
        for (String line; (line = reader.readLine()) != null; ) {
            System.out.println(line);
            Arrays.stream(line.substring(0, line.length()-2)
                    .split(" "))
                    .map(x ->"["+x+"]").forEach(System.out::println);
            List<String> components = Arrays
                    .stream(line
                            .substring(0, line.length()-2)
                            .split(" "))
                    .map(x -> x.substring(1, x.length()-1))
                    .collect(Collectors.toList());
            BisimulationNode node = result.get(components.get(0));
            OWLObjectProperty property = factory.getOWLObjectProperty(IRI.create(components.get(1)));
            BisimulationNode successor = result.get(components.get(2));

            node.addSuccessor(property,successor);
        }



        return result;
    }
}
