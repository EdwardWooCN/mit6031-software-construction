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
public class ConcreteVerticesGraph implements Graph<String> {
    
    private final List<Vertex> vertices = new ArrayList<>();
    
    // Abstraction function:
    //   Vertex represents vertices of graph and (Vertex.source -> Vertex.this) && (Vertex.this -> Vertex.target) represents edge
    // Representation invariant:
    //   unique Vertex.id in vertices
    // Safety from rep exposure:
    //   vertices use unmodifiable view
    
    ConcreteVerticesGraph() {}
    
    private void checkRep() {
        assert new HashSet<Vertex>(vertices).size() == vertices().size();
    }
    
    @Override public boolean add(String vertex) {
        if (Objects.nonNull(findVertexInList(vertex))) {
            checkRep();
            return false;
        }

        vertices.add(Vertex.getInstance(vertex));
        checkRep();
        return true;
    }
    
    @Override public int set(String source, String target, int weight) {
        //remove edge
        if (weight == 0) {
            Vertex srcVertex = findVertexInList(source);
            int res1 = 0;
            if (Objects.nonNull(srcVertex)) {
                res1 = srcVertex.removeEdgeFromTarget(target);
            }
            Vertex trgVertex = findVertexInList(target);
            if (Objects.nonNull(trgVertex) && !source.equals(target)) {
                int res2 = trgVertex.removeEdgeFromSource(source);
                assert res1 == res2;
            }
            checkRep();
            return res1;
        }

        //add or change edge
        add(source);
        add(target);
        int res1 = findVertexInList(target).updateEdgeFromSource(source, weight);
        if (!source.equals(target)) {
            int res2 = findVertexInList(source).updateEdgeFromTarget(target, weight);
            assert res2 == res1;
        }
        checkRep();
        return res1;
    }
    
    @Override public boolean remove(String vertex) {
        //remove vertex
        Vertex removedVertex = findVertexInList(vertex);
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
    
    @Override public Set<String> vertices() {
        return vertices.stream().map(Vertex::getId).collect(Collectors.toSet());
    }
    
    @Override public Map<String, Integer> sources(String target) {
        return Optional.ofNullable(findVertexInList(target)).map(Vertex::getSourceEdges).orElse(new HashMap<>());
    }
    
    @Override public Map<String, Integer> targets(String source) {
        return Optional.ofNullable(findVertexInList(source)).map(Vertex::getTargetEdges).orElse(new HashMap<>());
    }
    
    private Vertex findVertexInList(String id) {
        return vertices.stream().filter(item -> item.getId().equals(id)).findAny().orElse(null);
    }

    private boolean deleteVertexInList(String id) {
        Iterator<Vertex> vertexIterator = vertices.listIterator();
        while (vertexIterator.hasNext()) {
            Vertex vertex = vertexIterator.next();
            if (vertex.getId().equals(id)) {
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
class Vertex {
    private final String id;

    //key is the source of edge, target is this vertex, i.e. (source -> this, weight)
    private final Map<String, Integer> sourceEdges = new HashMap<>();

    //key is the target of edge, source is this vertex, i.e. (this -> target, weight)
    private final Map<String, Integer> targetEdges = new HashMap<>();

    // Abstraction function:
    //   id is the node, (source -> this, weight) && (this -> target, weight) are edge related to this node
    // Representation invariant:
    //   this node cannot be contained in sourcesEdge
    // Safety from rep exposure:
    //   sourcesEdge, targetsEdge use unmodifiable view
    
    private Vertex(String id) {
        assert Objects.nonNull(id);
        this.id = id;
    }

    public static Vertex getInstance(String id) {
        return new Vertex(id);
    }

    private void checkRep() {
        //this node cannot be contained in sourcesEdge
        assert !sourceEdges.containsKey(id);
    }

    public String getId() {
        return id;
    }

    public Map<String, Integer> getSourceEdges() {
        return Collections.unmodifiableMap(sourceEdges);
    }

    public Map<String, Integer> getTargetEdges() {
        return Collections.unmodifiableMap(targetEdges);
    }

    /**
     * update edge (source -> this, weight)
     * @param source: id of source vertex
     * @param weight: edge weight, has positive value
     * @return: previous weight of this edge, zero if non-existed before
     */
    public int updateEdgeFromSource(String source, int weight) {
        assert weight > 0 : "invalid weight";
        assert !source.equals(id) : "this node cannot be contained in sourceEdges, should be in targetEdges";

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
    public int updateEdgeFromTarget(String target, int weight) {
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
    public int removeEdgeFromSource(String source) {
        return Optional.ofNullable(sourceEdges.remove(source)).orElse(0);
    }

    /**
     * remove edge (this -> target, weight)
     * @param target: id of target vertex
     * @return: previous weight of this edge, zero if non-existed before
     */
    public int removeEdgeFromTarget(String target) {
        return Optional.ofNullable(targetEdges.remove(target)).orElse(0);
    }

    @Override
    public String toString() {
        return String.format("Vertex:\nsourcesEdge:%s\ntargetsEdge:%s", edgesExpressionOf(sourceEdges, true), edgesExpressionOf(targetEdges, false));
    }

    private String edgesExpressionOf(Map<String, Integer> edges, boolean isSourceEdge) {
        List<String> result = new LinkedList<>();
        for (Map.Entry<String, Integer> entry : edges.entrySet()) {
            String edgeExpr;
            if (isSourceEdge) {
                edgeExpr = edgeExpressionOf(entry.getKey(), this.id, entry.getValue());
            } else {
                edgeExpr = edgeExpressionOf(this.id, entry.getKey(), entry.getValue());
            }
            result.add(edgeExpr);
        }
        return result.toString();
    }

    private String edgeExpressionOf(String source, String target, int weight) {
        return String.format("Edge=(%s -> %s, weight=%d)", source, target, weight);
    }
}
