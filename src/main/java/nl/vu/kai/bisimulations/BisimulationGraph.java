package nl.vu.kai.bisimulations;

import java.util.*;

public class BisimulationGraph {
    private final Set<BisimulationNode> nodes = new HashSet<>();
    private final Map<String,BisimulationNode> name2node = new HashMap<>();

    public Collection<BisimulationNode> nodes(){
        return Collections.unmodifiableCollection(nodes);
    }

    public void restrictToLevel(int maxLevel) {
        List<BisimulationNode> toRemove = new LinkedList<>();
        nodes.stream().filter(n -> n.level()>maxLevel).forEach(toRemove::add);
        System.out.println("Nodes: "+ nodes.size());
        System.out.println("Removing "+toRemove.size());
        toRemove.forEach(nodes::remove);
        nodes.removeAll(toRemove);
    }

    public void restrictToMinSize(int minSize) {
        List<BisimulationNode> toRemove = new LinkedList<>();
        nodes.stream().filter(n -> n.size()<minSize).forEach(toRemove::add);
        nodes.removeAll(toRemove);
    }

    public void addNode(String name, BisimulationNode node){
        System.out.println(name);
        name2node.put(name,node);
        nodes.add(node);
    }

    public BisimulationNode get(String name){
        return name2node.get(name);
    }
}
