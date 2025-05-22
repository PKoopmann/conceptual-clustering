package nl.vu.kai.bisimulations.cmd;

import nl.vu.kai.bisimulations.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExtractHierarchy {
    public static void main(String[] args) throws OWLOntologyCreationException, IOException, OWLOntologyStorageException {
        if(args.length!=4){
            System.out.println("Usage:");
            System.out.println(ExtractHierarchy.class.getName()+" SUMMARY_PATH ONTOLOGY MAX_DEPTH ITERATIONS");
            System.exit(0);
        }

        String summaryPath = args[0];
        String ontologyPath = args[1];
        int maxDepth = Integer.parseInt(args[2]);
        int iterations = Integer.parseInt(args[3]);

        System.out.println("Summary path: "+summaryPath);
        System.out.println("Ontology path: "+ontologyPath);
        System.out.println("Max depth: "+maxDepth);
        System.out.println("Iterations: "+iterations);

        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File(ontologyPath));

        BisimulationGraphParser parser =
                new BisimulationGraphParser(
                        manager.getOWLDataFactory(),
                        ontology);
        BisimulationGraph graph = parser.parseGraph(new File(summaryPath), maxDepth);

        //graph.restrictToLevel(maxDepth);
        //graph.restrictToMinSize(10);
        System.out.println("nodes now: "+graph.nodes().size());

        Products products = new Products();
        /*products.addAllProducts(graph);
        System.out.println("nodes now: "+graph.nodes().size());
        products = new Products();
        products.addAllProducts(graph);
        System.out.println("nodes now: "+graph.nodes().size());
        products = new Products();
        products.addAllProducts(graph);
        System.out.println("nodes now: "+graph.nodes().size());
        */
        products.productsFixpoint(graph,iterations);
        System.out.println("Done with the products");
        ToOWLConverter converter = new ToOWLConverter(manager);
        OWLOntology ont2 = converter.convert(graph,ontology);
        converter.addUtility2Label(ontology, graph, new BisimulationGraphEvaluator(graph,ontology));
        //ontology.addAxioms(ont2.axioms());

        manager.saveOntology(ontology, new FileOutputStream(new File("output.owl")));
    }
}
