import java.util.*;

public class Clusterer {
    private List<List<WeightedEdge<Integer, Double>>> adjList; // the adjacency list of the original graph
    private List<List<WeightedEdge<Integer, Double>>> mstAdjList; // the adjacency list of the minimum spanning tree
    private List<List<Integer>> clusters; // a list of k points, each representing one of the clusters.
    private double cost; // the distance between the closest pair of clusters

    public Clusterer(double[][] distances, int k){
        int n = distances.length;

        //create adjacenct list
        adjList = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            adjList.add(new ArrayList<>());
        }

        for(int i = 0; i < n; i++) {
            for(int j = i + 1; j < n; j++) {
                double wt = distances[i][j];
                adjList.get(i).add(new WeightedEdge<>(i,j,wt));
                adjList.get(j).add(new WeightedEdge<>(j,i,wt));
            }
        }

        //create MST adjacency list
        mstAdjList = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            mstAdjList.add(new ArrayList<>());
        }

        prims(0);
        makeKCluster(k);
    }

    // implement Prim's algorithm to find a MST of the graph.
    // in my implementation I used the mstAdjList field to store this.
    private void prims(int start){
        int n = adjList.size();
        boolean[] visited = new boolean[n];
        visited[start] = true;

        PriorityQueue<WeightedEdge<Integer, Double>> pq = new PriorityQueue<>();
        pq.addAll(adjList.get(start));

        while (!pq.isEmpty()) {
            WeightedEdge<Integer,Double>edge = pq.poll();
            int dest = edge.destination;
            if(visited[dest]) continue;
            mstAdjList.get(edge.source).add(edge);
            mstAdjList.get(dest).add(new WeightedEdge<>(dest, edge.source, edge.weight));
            visited[dest] = true;

            for (WeightedEdge<Integer, Double> nextEdge : adjList.get(dest)) {
                if (!visited[nextEdge.destination]) {
                    pq.add(nextEdge);
                }
            }
        }

    }

    // After making the minimum spanning tree, use this method to
    // remove its k-1 heaviest edges, then assign integers
    // to clusters based on which nodes are still connected by
    // the remaining MST edges.
    private void makeKCluster(int k){
        int n = mstAdjList.size();

        List<WeightedEdge<Integer, Double>> mstEdges = new ArrayList<>();
        boolean[][] seen = new boolean[n][n];
        for (int u = 0; u < n; u++) {
            for (WeightedEdge<Integer, Double> e : mstAdjList.get(u)) {
                int v = e.destination;
                if (!seen[u][v]) {
                    mstEdges.add(e);
                    seen[u][v] = true;
                    seen[v][u] = true;
                }
            }
        }

        mstEdges.sort((e1, e2) -> e2.weight.compareTo(e1.weight));

        for (int i = 0; i < k - 1 && i < mstEdges.size(); i++) {
            WeightedEdge<Integer, Double> e = mstEdges.get(i);
            removeEdge(mstAdjList, e.source, e.destination);
            removeEdge(mstAdjList, e.destination, e.source);
        }

        cost = (k - 1 > 0 && k - 2 < mstEdges.size()) ? mstEdges.get(k - 2).weight : 0.0;

        clusters = new ArrayList<>();
        boolean[] visited = new boolean[n];

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                List<Integer> cluster = new ArrayList<>();
                Queue<Integer> queue = new LinkedList<>();
                queue.add(i);
                visited[i] = true;

                while (!queue.isEmpty()) {
                    int u = queue.poll();
                    cluster.add(u);

                    for (WeightedEdge<Integer, Double> edge : mstAdjList.get(u)) {
                        int w = edge.destination;
                        if (!visited[w]) {
                            visited[w] = true;
                            queue.add(w);
                        }
                    }
                }
                clusters.add(cluster);
            }
        }
    }

    private void removeEdge(List<List<WeightedEdge<Integer, Double>>> graph, int u, int v){
        Iterator<WeightedEdge<Integer, Double>> it = graph.get(u).iterator();
        while (it.hasNext()) {
            WeightedEdge<Integer, Double> e = it.next();
            if (e.destination == v) {
                it.remove();
                break;
            }
        }
    }

    public List<List<Integer>> getClusters(){
        return clusters;
    }

    public double getCost(){
        return cost;
    }

}
