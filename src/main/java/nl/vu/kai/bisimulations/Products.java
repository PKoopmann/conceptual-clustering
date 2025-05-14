package nl.vu.kai.bisimulations;

import nl.vu.kai.bisimulations.tools.Pair;
import nl.vu.kai.bisimulations.tools.UnorderedPair;
import org.semanticweb.owlapi.model.OWLClass;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Products {

    private Set<BisimulationNode> knownNodes = new HashSet<>();

    private final Map<UnorderedPair<BisimulationNode>,BisimulationNode> cache = new HashMap<>();

    int count = 0;
    public void addAllProducts(BisimulationGraph graph){
        knownNodes.addAll(graph.nodes());
        Set<BisimulationNode> newNodes = new HashSet<>();
        graph.nodes().forEach(n1 ->
                graph.nodes().forEach(n2 -> {
                        BisimulationNode product = product(n1,n2);
                        if(!newNodes.stream().anyMatch(product::deepEquals)) {
                            newNodes.add(product);
                            System.out.println(newNodes.size());
                        }
                }
                ));
        newNodes.forEach(n -> graph.addNode("Product"+(count++), n));
    }

    public BisimulationNode product(BisimulationNode n1, BisimulationNode n2) {
        UnorderedPair<BisimulationNode> pair = new UnorderedPair<>(n1,n2);
        if(cache.containsKey(pair))
            return cache.get(pair);
        else if(n1.refines(n2))
            return n2;
        else if(n2.refines(n1))
            return n1;
        else {
            BisimulationNode result = new BisimulationNode();
            cache.put(pair,result);
            Set<OWLClass> clazzes = new HashSet<>(n1.classes());
            clazzes.retainAll(n2.classes());
            result.addClasses(clazzes);
            n1.successors()
                    .forEach(sucPair ->
                        n2.successors(sucPair.getKey())
                                .forEach(suc2 ->
                                        result.addSuccessor(
                                                sucPair.getKey(),
                                                product(sucPair.getValue(), suc2)))
            );
            return knownNodes.stream()
                    .filter(result::deepEquals)
                    .findAny()
                    .orElse(result);
        }
    }
}
