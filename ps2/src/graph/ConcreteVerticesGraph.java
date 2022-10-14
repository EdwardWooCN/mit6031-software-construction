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
public class ConcreteVerticesGraph<L> implements Graph<L> {
    
    private final List<Vertex<L>> vertices = new ArrayList<>();
    
    // Abstraction function:
    //   Vertex represents vertices of graph and (Vertex.source -> Vertex.this) && (Vertex.this -> Vertex.target) represents edge
    // Representation invariant:
    //   unique Vertex.id in vertices
    //   edge should be recorded in both vertices.
    //      e.g. edge(v1 -> v2), sources(v2).contains(v1) && targets(v1).contains(v2) == true
    // Safety from rep exposure:
    //   vertices use unmodifiable view
    
    ConcreteVerticesGraph() {}
    
    private void checkRep() {
        //unique Vertex.id in vertices
        assert new HashSet<>(vertices).size() == vertices().size();

        //edge should be recorded in both vertices.
        for (Vertex<L> source : vertices) {
            Map<L, Integer> targetEdges = source.getTargetEdges();
            for (L target : targetEdges.keySet()) {
                Map<L, Integer> sourceEdges = findVertexInList(target).getSourceEdges();
                assert sourceEdges.containsKey(source.getNode());
            }
        }
    }
    
    @Override public boolean add(L vertex) {
        if (Objects.nonNull(findVertexInList(vertex))) {
            checkRep();
            return false;
        }

        vertices.add(Vertex.getInstance(vertex));
        checkRep();
        return true;
    }
    
    @Override public int set(L source, L target, int weight) {
        //remove edge
        if (weight == 0) {
            Vertex<L> srcVertex = findVertexInList(source);
            int res1 = -1, res2 = -1;
            if (Objects.nonNull(srcVertex)) {
                res1 = srcVertex.removeEdgeFromTarget(target);
            }
            Vertex<L> trgVertex = findVertexInList(target);
            if (Objects.nonNull(trgVertex)) {
                res2 = trgVertex.removeEdgeFromSource(source);
            }
            assert res1 == res2;
            checkRep();
            return res1;
        }

        //add or change edge
        add(source);
        add(target);
        int res1 = findVertexInList(source).updateEdgeFromTarget(target, weight);
        int res2 = findVertexInList(target).updateEdgeFromSource(source, weight);
        assert res2 == res1;
        checkRep();
        return res1;
    }
    
    @Override public boolean remove(L vertex) {
        //remove vertex
        Vertex<L> removedVertex = findVertexInList(vertex);
        if (Objects.isNull(removedVertex)) {
            checkRep();
            return false;
        }

        //delete edge(other -> vertex)
        removedVertex.getSourceEdges().keySet().forEach(other -> findVertexInList(other).removeEdgeFromTarget(vertex));
        //delete edge(vertex -> other)
        removedVertex.getTargetEdges().keySet().forEach(other -> findVertexInList(other).removeEdgeFromSource(vertex));
        assert deleteVertexInList(vertex);
        checkRep();
        return true;
    }
    
    @Override public Set<L> vertices() {
        return vertices.stream().map(Vertex::getNode).collect(Collectors.toSet());
    }
    
    @Override public Map<L, Integer> sources(L target) {
        return Optional.ofNullable(findVertexInList(target)).map(Vertex::getSourceEdges).orElse(new HashMap<>());
    }
    
    @Override public Map<L, Integer> targets(L source) {
        return Optional.ofNullable(findVertexInList(source)).map(Vertex::getTargetEdges).orElse(new HashMap<>());
    }
    
    private Vertex<L> findVertexInList(L id) {
        return vertices.stream().filter(item -> item.getNode().equals(id)).findAny().orElse(null);
    }

    private boolean deleteVertexInList(L id) {
        Iterator<Vertex<L>> vertexIterator = vertices.listIterator();
        while (vertexIterator.hasNext()) {
            Vertex<L> vertex = vertexIterator.next();
            if (vertex.getNode().equals(id)) {
                vertexIterator.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "ConcreteVerticesGraph{" +
                "vertices=" + vertices +
                '}';
    }
}

/**
 * TODO specification
 * Mutable.
 * This class is internal to the rep of ConcreteVerticesGraph.
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Vertex<L> {
    private final L node;

    //key is the source of edge, target is this vertex, i.e. (source -> this, weight)
    private final Map<L, Integer> sourceEdges = new HashMap<>();

    //key is the target of edge, source is this vertex, i.e. (this -> target, weight)
    private final Map<L, Integer> targetEdges = new HashMap<>();

    // Abstraction function:
    //   id is the node, (source -> this, weight) && (this -> target, weight) are edge related to this node
    // Representation invariant:
    //   edge should be recorded in both vertices.
    //      e.g. edge(v1 -> v1), sources(v1).contains(v1) && targets(v1).contains(v1) == true
    // Safety from rep exposure:
    //   sourcesEdge, targetsEdge use unmodifiable view
    
    private Vertex(L node) {
        assert Objects.nonNull(node);
        this.node = node;
    }

    public static <L> Vertex<L> getInstance(L id) {
        return new Vertex<>(id);
    }

    private void checkRep() {
        assert  sourceEdges.containsKey(node) == targetEdges.containsKey(node);
    }

    public L getNode() {
        return node;
    }

    public Map<L, Integer> getSourceEdges() {
        return Collections.unmodifiableMap(sourceEdges);
    }

    public Map<L, Integer> getTargetEdges() {
        return Collections.unmodifiableMap(targetEdges);
    }

    /**
     * update edge (source -> this, weight)
     * @param source: id of source vertex
     * @param weight: edge weight, has positive value
     * @return: previous weight of this edge, zero if non-existed before
     */
    public int updateEdgeFromSource(L source, int weight) {
        assert weight > 0 : "invalid weight";

        Integer oldWeight = sourceEdges.get(source);
        sourceEdges.put(source, weight);
        return Optional.ofNullable(oldWeight).orElse(0);
    }

    /**
     * update edge (this -> target, weight)
     * @param target: id of target vertex
     * @param weight: edge weight, has positive value
     * @return: previous weight of this edge, zero if non-existed before
     */
    public int updateEdgeFromTarget(L target, int weight) {
        assert weight > 0 : "invalid weight";

        Integer oldWeight = targetEdges.get(target);
        targetEdges.put(target, weight);
        return Optional.ofNullable(oldWeight).orElse(0);
    }

    /**
     * remove edge (source -> this, weight)
     * @param source: id of source vertex
     * @return: previous weight of this edge, zero if non-existed before
     */
    public int removeEdgeFromSource(L source) {
        return Optional.ofNullable(sourceEdges.remove(source)).orElse(0);
    }

    /**
     * remove edge (this -> target, weight)
     * @param target: id of target vertex
     * @return: previous weight of this edge, zero if non-existed before
     */
    public int removeEdgeFromTarget(L target) {
        return Optional.ofNullable(targetEdges.remove(target)).orElse(0);
    }

    @Override
    public String toString() {
        return String.format("Vertex(id: %s, sourcesEdge: %s, targetsEdge: %s)", node, edgesExpressionOf(sourceEdges, true), edgesExpressionOf(targetEdges, false));
    }

    private String edgesExpressionOf(Map<L, Integer> edges, boolean isSourceEdge) {
        List<String> result = new LinkedList<>();
        for (Map.Entry<L, Integer> entry : edges.entrySet()) {
            String edgeExpr;
            if (isSourceEdge) {
                edgeExpr = edgeExpressionOf(entry.getKey(), this.node, entry.getValue());
            } else {
                edgeExpr = edgeExpressionOf(this.node, entry.getKey(), entry.getValue());
            }
            result.add(edgeExpr);
        }
        return result.toString();
    }

    private String edgeExpressionOf(L source, L target, int weight) {
        return String.format("Edge=(%s -> %s, weight=%d)", source, target, weight);
    }
}
