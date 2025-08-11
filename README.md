There are two primary categories of machine learning tactics. Supervised learning involves
using labeled data to train a model that can later apply labels to new data. For example,
suppose I had a bunch of photos of Labradors (a dog breed) that I wanted to separate into black
labs, yellow labs, and chocolate labs (different varieties of Labradors different only by coat
color). If I manually pre-labelled these photos with the correct variety then I could use them to
train a supervised learning algorithm to label future photos with the correct variety of Labrador.
An unsupervised model, on the other hand, does not require any pre-labelling of the training
data. Instead, unsupervised models attempt to discern patterns in the training data by looking
only at the data itself. For example, we might be able to plot the photos of Labradors as points
in some cartesian space (as a simple example, we might have a 3-dimensional point per picture
which represents the average RGB values of the image). With these points, an unsupervised
model might identify clusters of points that are similar to one another and conclude that points in
the same cluster must be the same Labrador variety. The advantage to these "clustering"
algorithms is that we do not need to take the effort to pre-label, one disadvantage is that it does
not tell us which cluster represents which variety.

Problem Statement
The most commonly used clustering algorithm is called k-means clustering. Given a collection of
points and a number of clusters k, the k-means clustering algorithm will split those points into k
collections such that points which share a cluster are those which are nearest to the same
reference point. For this assignment we will be doing a different form of clustering that I will call
k-margin clustering. For this algorithm, we will be given a collection of items and an integer k.
We will then split the items into k collections in such a way to maximize the “gap” between the
closest two clusters. We define the “gap” size between two clusters to be the closest pair of
items between them. In other words, we want to split our collection into k subsets such that we
have maximized the closest pair of points across those subsets.
This task may seem daunting at first, but minimum spanning trees will help! To begin with, we
will represent our collection of items as a 2-d array, such that cell i,j of this array represents
the distance from item i to item j. Effectively, this 2-d array can be considered as an
undirected, weighted, complete graph. So we now have a graph where all of the items are
nodes and the weights of the edges between nodes represent their distance from each other.
From here, we will calculate a minimum spanning tree of this graph. This will be helpful
because:
1. If we consider any cluster in the graph, the edge which connects that cluster to its
closest neighboring cluster must be an edge in a minimum spanning tree of the graph.
Suppose that the cost of the clustering is the weight of edge (x, y). This follows from the
cut theorem of MSTs. We will define a cut in the graph such that our cluster is one side
of the cut and all other nodes are on the other side. In this case, any edge going from
this cluster to another cluster will cross the cut. The closest pair will then be the lightest
edge which crosses the cut, and therefore is part of a minimum spanning tree!
2. Being a spanning tree, a minimum spanning tree is connected and acyclic and contains
n − 1 edges. If any edge is removed then the graph is no longer connected, instead it will
have two separate components. If any two edges are removed then it will have three
separate components. So if we remove k-1 edges then we will have k separate
components.
Combining these two observations above, we get a clustering algorithm.

Our input will be an n x n array of doubles, representing the pairwise distances of all our
items (which we’ll just consider to be ints 0 through n-1, so the indices are the items) and
an int k for the number of clusters

We will construct a graph using this n x n array.

Next we’ll construct a minimum spanning tree on that graph

Then we remove the k-1 heaviest edges from the MST(the weight of the last edge
removed will be the distance between the closest pair of clusters)

Finally we identify which items are in which cluster by checking which items are
connected to each other using the remaining edges of the MST (e.g. by using a
breadth-first search).
Your task for this assignment is to implement that algorithm above.
Input Format: For this assignment, input will be encoded in txt files. Supposing that each test
consists of n items, the files will contain n+2 lines as follows:

The first line contains the value of k, i.e. the number of clusters to break the items into

The next line contains the value n (the number of items)

The remaining n lines contain a space-separated list of doubles which indicate the
distances between each pair of items
For example, consider a file with its contents as shown below.
This file indicates that we will be splitting 5 items into k clusters. When doing so, item 0 is
distance 18 from item 1, item 2 is distance 15 away from item 3, etc.
In this case the optimal clustering would be for the first cluster to contain items 0 and 4, the
second to contain items 2 and 3, and the third cluster to contain item 1. To see the cost of this
clustering we look at the closest pair of items for each pair of clusters, and then save the
smallest. So in this case the cost of the clustering would be 15. The following explains why:

The distance between the first cluster and the second is 15 because the closest pair of
points that crosses these clusters is items 4 and 3 which have distance 15.

The distance between the first cluster and the third is 18 because the closest pair of
points that crosses these clusters is items 0 and 1 which have distance 18.

The distance between the second cluster and the third is 30 because the closest pair of
points that crosses these clusters is items 1 and 3 which have distance 30.

The overall cost of the clustering is the smallest of these distances, and so it is 15.
