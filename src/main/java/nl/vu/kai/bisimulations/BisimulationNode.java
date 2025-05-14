package nl.vu.kai.bisimulations;

import nl.vu.kai.bisimulations.tools.MultiMap;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BinaryOperator;

public class BisimulationNode {
    private int size;
    private int level;
    private Set<OWLClass> classes = new HashSet<>();
    private Set<BisimulationNode> refines = new HashSet<>();
    private MultiMap<OWLObjectProperty,BisimulationNode> successors = new MultiMap<>();

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void addRefines(BisimulationNode node){
        refines.add(node);
    }

    public void addSuccessor(OWLObjectProperty property, BisimulationNode successor) {
        successors.add(property,successor);
    }

    public void addClass(OWLClass clazz){
        this.classes.add(clazz);
    }
}
