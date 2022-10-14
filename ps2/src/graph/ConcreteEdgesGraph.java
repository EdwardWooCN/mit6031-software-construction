/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.*;
import java.util.stream.Collectors;

/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteEdgesGraph<L> implements Graph<L> {
    
    private final Set<L> vertices = new HashSet<>();
    private final List<Edge<L>> edges = new ArrayList<>();
    
    // Abstraction function:
    //   vertices & edges represent graph
    // Representation invariant:
    //   sources and targets of all edges must be contained in vertices
    //   unique edge in edges
    //   unique node in vertices (already ensure by ADT)
    // Safety from rep exposure:
    //   Node, aka label L, is immutable (already ensure by String)
    //   Edge is immutable
    //   vertices use defensive copy to get

    ConcreteEdgesGraph() {}
    
    private void checkRep() {
        //unique edge in edges
        Set<String> edgeSet = edges.stream().map(edge -> String.format("%s -> %s", edge.getSrc(), edge.getDst())).collect(Collectors.toSet());
        assert edgeSet.size() == edges.size();

        //sources and targets of all edges must be contained in vertices
        Optional<Edge<L>> unexpectedEdgeOpt = edges.stream().filter(edge -> !vertices.containsAll(Arrays.asList(edge.getSrc(), edge.getDst()))).findAny();
        assert !unexpectedEdgeOpt.isPresent();
    }
    
    @Override public boolean add(L vertex) {
        return vertices.add(vertex);
    }
    
    @Override public int set(L source, L target, int weight) {
        int result = 0;
        int indexOfUnweightedEdge = indexOfUnweightedEdge(source, target, weight > 0 ? weight : 1);
        if (indexOfUnweightedEdge > -1) {  //change precondition or remove edge
            result = edges.get(indexOfUnweightedEdge).getW();
            edges.remove(indexOfUnweightedEdge);
            //maintain vertices
            if (sources(target).isEmpty()) {
                vertices.remove(target);
            }
            if (targets(source).isEmpty()) {
                vertices.remove(source);
            }
        }
        if (weight > 0) {  //change or add edge
            edges.add(new Edge<>(source, target, weight));
            vertices.add(source);
            vertices.add(target);
        }

        checkRep();
        return result;
    }
    
    @Override public boolean remove(L vertex) {
        boolean hasRemovedRelatedEdge = edges.removeIf(edge -> edge.getSrc().equals(vertex) || edge.getDst().equals(vertex));
        if (hasRemovedRelatedEdge) {
            vertices.remove(vertex);
        }
        boolean hasRemoveVertices = vertices.remove(vertex);
        checkRep();
        return hasRemovedRelatedEdge || hasRemoveVertices;
    }
    
    @Override public Set<L> vertices() {
        return new HashSet<>(vertices);
    }
    
    @Override public Map<L, Integer> sources(L target) {
        return edges.stream().filter(edge -> edge.getDst().equals(target)).collect(Collectors.toMap(Edge::getSrc, Edge::getW, (a, b) -> a));
    }
    
    @Override public Map<L, Integer> targets(L source) {
        return edges.stream().filter(edge -> edge.getSrc().equals(source)).collect(Collectors.toMap(Edge::getDst, Edge::getW, (a, b) -> a));
    }
    
    @Override
    public String toString() {
        return String.format("ConcreteEdgesGraph:\nVertices:%s\nEdges:%s", vertices.toString(), edges.toString());
    }

    private int indexOfUnweightedEdge(Edge<L> edge) {
        for (int index = 0; index < edges.size(); index++) {
            if (Edge.isEqualUnweightedEdge(edges.get(index), edge)) {
                return index;
            }
        }
        return -1;
    }

    private int indexOfUnweightedEdge(L source, L target, int weight) {
        return indexOfUnweightedEdge(new Edge<>(source, target, weight));
    }
}

/**
 * TODO specification
 * Immutable.
 * This class is internal to the rep of ConcreteEdgesGraph.
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Edge<L> {
    
    private final L src;
    private final L dst;
    private final int w;

    // Abstraction function:
    //   represent edge, src -> source, dst -> target, w -> weight
    // Representation invariant:
    //   src non-null, dst non-null, w > 0
    // Safety from rep exposure:
    //   immutable object
    
    Edge(L src, L dst, int w) {
        // check rep
        if (Objects.isNull(src) || Objects.isNull(dst) || w <= 0) {
            throw new RuntimeException("illegal edge init");
        }

        this.src = src;
        this.dst = dst;
        this.w = w;
    }

    public L getSrc() {
        return src;
    }

    public L getDst() {
        return dst;
    }

    public int getW() {
        return w;
    }

    /**
     * Compare if equal weighted edge
     *
     * @param e1: one edge
     * @param e2: another edge
     * @return true for equal
     */
    public static boolean isEqualWeightedEdge(Edge e1, Edge e2) {
        return Edge.isEqualUnweightedEdge(e1, e2) && e1.w == e2.w;
    }

    /**
     * Compare if equal unweighted edge
     *
     * @param e1: one edge
     * @param e2: another edge
     * @return true for equal
     */
    public static boolean isEqualUnweightedEdge(Edge e1, Edge e2) {
        return e1.src.equals(e2.src) && e1.dst.equals(e2.dst);
    }

    @Override
    public String toString() {
        return String.format("Edge=(%s -> %s, weight=%d)", src, dst, w);
    }
}
