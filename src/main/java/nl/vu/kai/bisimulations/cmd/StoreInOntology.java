package nl.vu.kai.bisimulations.cmd;

import nl.vu.kai.bisimulations.BisimulationGraph;
import nl.vu.kai.bisimulations.BisimulationGraphParser;
import nl.vu.kai.bisimulations.Products;
import nl.vu.kai.bisimulations.ToOWLConverter;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class StoreInOntology {
    public static void main(String[] args) throws OWLOntologyCreationException, IOException, OWLOntologyStorageException {
        if(args.length!=2 && args.length!=1){
            System.out.println("Usage:");
            System.out.println(StoreInOntology.class.getName()+" SUMMARY_PATH [ONTOLOGY]");
            System.exit(0);
        }

        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology;
        if(args.length==2){
            ontology = manager.loadOntologyFromOntologyDocument(new File(args[1]));
        } else
            ontology = manager.createOntology();

        BisimulationGraphParser parser =
                new BisimulationGraphParser(
                        manager.getOWLDataFactory(),
                        ontology);
        BisimulationGraph graph = parser.parseGraph(new File(args[0]));

        graph.restrictToLevel(1);
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
        products.productsFixpoint(graph,4);
        ToOWLConverter converter = new ToOWLConverter(manager);
        OWLOntology ont2 = converter.convert(graph,ontology);
        //ontology.addAxioms(ont2.axioms());

        manager.saveOntology(ontology, new FileOutputStream(new File("output.owl")));
    }
}
