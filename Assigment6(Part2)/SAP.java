import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;

public class SAP {

    private Digraph G;
    private int anc;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        // check bounds
        if (v < 0 || v > G.V() - 1 || w < 0 || w > G.V() - 1) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        // for directed paths
        BreadthFirstDirectedPaths bp1 = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bp2 = new BreadthFirstDirectedPaths(G, w);
        int shortest_path = -1;
        anc = -1;
        if (bp1.hasPathTo(w)) {
            shortest_path = bp1.distTo(w);
            anc = w;
        }
        if (bp2.hasPathTo(v)) {
            int path = bp2.distTo(v);
            if (shortest_path == -1) {
                shortest_path = path;
                anc = v;
            } else if (shortest_path > path) {
                shortest_path = path;
                anc = v;
            }
        }
        // search others paths
        for (int i = 0; i < G.V(); i++) {
            if (bp1.hasPathTo(i) && bp2.hasPathTo(i)) {
                int path = bp1.distTo(i) + bp2.distTo(i);
                if (shortest_path == -1) {
                    shortest_path = path;
                    anc = i;
                } else if (shortest_path > path) {
                    shortest_path = path;
                    anc = i;
                }
            }
        }
        return shortest_path;
    }

    // a common ancestor of v and w that participates in a shortest ancestral
    // path; -1 if no such path
    public int ancestor(int v, int w) {
        this.length(v, w);
        // return ancestor;
        return anc;
    }

    // length of shortest ancestral path between any vertex in v and any vertex
    // in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new java.lang.NullPointerException();
        BreadthFirstDirectedPaths bp1 = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bp2 = new BreadthFirstDirectedPaths(G, w);
        int shortest_path = -1;
        anc = -1;
        
        for (int i : w) {
            if (bp1.hasPathTo(i)) {
                int path = bp1.distTo(i);
                if (shortest_path == -1 || shortest_path > path) {
                    shortest_path = path;
                    anc = i;
                }
            }
        }

        for (int j : v) {
            if (bp2.hasPathTo(j)) {
                int path = bp2.distTo(j);
                if (shortest_path == -1 || shortest_path > path) {
                    shortest_path = path;
                    anc = j;
                }
            }
        }
            
        // search others paths
        for (int i = 0; i < G.V(); i++) {
            if (bp1.hasPathTo(i) && bp2.hasPathTo(i)) {
                int path = bp1.distTo(i) + bp2.distTo(i);
                if (shortest_path == -1 || shortest_path > path) {
                    shortest_path = path;
                    anc = i;
                }
            }
        }
        
        return shortest_path;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no
    // such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new java.lang.NullPointerException();
        this.length(v, w);
        return anc;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In("digraph4.txt");
        Digraph G = new Digraph(in);
        SAP s = new SAP(G);
        List<Integer> l1 = new ArrayList<>();
        l1.add(9);
        List<Integer> l2 = new ArrayList<>();
        l2.add(3);
        System.out.println(s.ancestor(l1, l2));
        System.out.println(s.length(l1, l2));
    }
}
