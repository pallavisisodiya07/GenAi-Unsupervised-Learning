public class WeightedEdge<V, W extends Comparable<W>> implements Comparable<WeightedEdge<V,W>>{
    public W weight;
    public V destination;
    public V source;

    public WeightedEdge(V source, V destinationNode, W weight){
        this.source = source;
        this.destination = destinationNode;
        this.weight = weight;
    }

    public int compareTo(WeightedEdge<V,W> other){
        return this.weight.compareTo(other.weight);
    }

    public int hashCode(){
        return this.destination.hashCode();
    }

    public boolean equals(WeightedEdge<V,W> other){
        return this.destination.equals(other.destination);
    }

    public String toString(){
        return "(" + source + "-" + destination + ", w:" + weight + ")";
    }
}
