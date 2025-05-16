package nl.vu.kai.bisimulations;

import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLProperty;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class BisimulationNodeOptimizer {
    public static void optimizeNode(BisimulationNode node){
        for(OWLObjectProperty property:node.properties()){
            List<BisimulationNode> toRemove = new LinkedList<>();
            node.successors(property)
                    .stream()
                    .filter(p1 ->
                            node.successors(property)
                                    .stream()
                                    .filter(p2 -> !p1.equals(p2))
                                    .anyMatch(p1::refines))
                    .forEach(toRemove::add);
            node.removeSuccessors(property,toRemove);
        }
    }
}
