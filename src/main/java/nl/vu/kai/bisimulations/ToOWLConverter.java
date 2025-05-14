package nl.vu.kai.bisimulations;

import org.semanticweb.owlapi.model.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ToOWLConverter {

    private final OWLOntologyManager manager;
    private final OWLDataFactory factory;

    private Map<BisimulationNode, String> shortNames = new HashMap<>();

    private int count = 0;

    public ToOWLConverter(OWLOntologyManager manager){
        this.manager=manager;
        this.factory= manager.getOWLDataFactory();
    }

    public OWLOntology convert(BisimulationGraph graph) throws OWLOntologyCreationException {
        OWLOntology result = manager.createOntology();
        graph.nodes().forEach(node -> {
            OWLClass clazz = clazz(node);
            OWLClassExpression exp = convert(node);
            result.add(factory.getOWLEquivalentClassesAxiom(clazz,exp));
            node.refines().forEach(b2 ->
                    result.add(factory.getOWLSubClassOfAxiom(clazz, clazz(b2))));
        });
        return result;
    }

    public OWLClassExpression convert(BisimulationNode node){
        if(!shortNames.containsKey(node)) {
            shortNames.put(node,"C"+count);
            count++;
        }
        List<OWLClassExpression> conjuncts = new LinkedList<>();
        conjuncts.addAll(node.classes());
        node.successors().stream()
                .map(
                pair ->
                        factory.getOWLObjectSomeValuesFrom(
                                pair.getKey(),
                                clazz(pair.getValue())))
                .forEach(conjuncts::add);

        if(conjuncts.isEmpty())
            return factory.getOWLThing();
        else if(conjuncts.size()==1)
            return conjuncts.get(0);
        else
            return factory.getOWLObjectIntersectionOf(conjuncts);
    }

    public OWLClass clazz(BisimulationNode node) {
        if(!shortNames.containsKey(node)) {
            shortNames.put(node,"C"+count);
            count++;
        }
        return factory.getOWLClass(
                IRI.create(
                        shortNames.get(node)+"_"
                                +node.level()+"_"
                                +node.size()
                )
        );
    }

}
