package nl.vu.kai.bisimulations;

import nl.vu.kai.bisimulations.tools.MultiMap;
import nl.vu.kai.bisimulations.tools.Pair;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLProperty;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/**
 * TODO add name as key for hashcode and equals
 */

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

    public void setLevel(int level) {
        this.level=level;
    }

    public void addRefines(BisimulationNode node){
        if(node == null)
            throw new IllegalArgumentException();
        refines.add(node);
    }

    public void addSuccessor(OWLObjectProperty property, BisimulationNode successor) {
        successors.add(property,successor);
    }

    public void addClass(OWLClass clazz){
        this.classes.add(clazz);
    }

    public void addClasses(Set<OWLClass> classes){
        this.classes.addAll(classes);
    }

    public int size(){
        return size;
    }

    public int level(){
        return level;
    }

    public Collection<BisimulationNode> refines() {
        return Collections.unmodifiableCollection(refines);
    }

    public boolean refines(BisimulationNode other) {
        return refines.contains(other);
    }

    public Collection<OWLClass> classes() {
        return Collections.unmodifiableCollection(classes);
    }

    public Collection<Pair<OWLObjectProperty,BisimulationNode>> successors() {
        return successors.keys()
                .stream()
                .flatMap(key ->
                        successors.get(key)
                                .stream()
                                .map(value -> new Pair<OWLObjectProperty,BisimulationNode>(key,value)))
                .collect(Collectors.toSet());
    }

    public Collection<BisimulationNode> successors(OWLObjectProperty property) {
        return successors.get(property);
    }


    /*@Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BisimulationNode that = (BisimulationNode) o;
        return Objects.equals(classes, that.classes) && Objects.equals(successors, that.successors);
    }*/

    public boolean deepEquals(BisimulationNode other) {
        if(!equals(other))
            return false;
        if(!classes.containsAll(other.classes) || !other.classes.containsAll(classes))
            return false;
        return successors.keys().stream().allMatch(k1 -> {
            Collection<BisimulationNode> otherSuc = other.successors(k1);
            return otherSuc!=null &&
                    successors(k1).containsAll(otherSuc) &&
                    otherSuc.containsAll(successors(k1));
        });
    }

    /*
    @Override
    public int hashCode() {
        return Objects.hash(classes, successors);
    }*/
}
