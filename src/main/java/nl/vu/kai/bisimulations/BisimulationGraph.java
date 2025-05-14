package nl.vu.kai.bisimulations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BisimulationGraph {
    private final Set<BisimulationNode> nodes = new HashSet<>();
    private final Map<String,BisimulationNode> name2node = new HashMap<>();

    public void addGraph(String name, BisimulationNode node){
        name2node.put(name,node);
        nodes.add(node);
    }

    public BisimulationNode get(String name){
        return name2node.get(name);
    }
}
