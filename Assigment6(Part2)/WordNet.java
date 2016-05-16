import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;

public class WordNet {
    private Digraph d;
    private Map<String, List<Integer>> map;
    private Map<Integer, String> idNoun;
    private SAP s;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new java.lang.NullPointerException();

        In file1 = new In(synsets);
        map = new HashMap<>();
        idNoun = new HashMap<>();
        int id = 0;
        while (file1.hasNextLine()) {
            String string = file1.readLine();
            String[] words = string.split(",");
            id = Integer.parseInt(words[0]);
            String word = words[1];
            idNoun.put(id, word);
            String[] dict_words = words[1].split(" ");
            for (String sd : dict_words) {
                // System.out.println(s);
                if (!map.containsKey(sd)) {
                    List<Integer> l = new ArrayList<>();
                    l.add(id);
                    map.put(sd, l);
                } else
                    map.get(sd).add(id);
            }
            // System.out.println(word);

        }
        d = new Digraph(id + 1);

        In file2 = new In(hypernyms);
        while (file2.hasNextLine()) {
            String string = file2.readLine();
            String[] words = string.split(",");
            id = Integer.parseInt(words[0]);

            for (int i = 1; i < words.length; i++) {
                d.addEdge(id, Integer.parseInt(words[i]));
                // System.out.println("Edge from " + words[i] + " to " + id);
            }
        }

        // check if not rooted DAG

        List<Integer> l = new ArrayList<>();
        for (int i = 0; i < d.V(); i++) {
            if (d.outdegree(i) == 0)
                l.add(i);
        }
        if (l.size() != 1) {
            throw new IllegalArgumentException();
        }
        s = new SAP(d);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return map.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new java.lang.NullPointerException();
        return map.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new java.lang.NullPointerException();
        }
        if (!map.containsKey(nounA) || !map.containsKey(nounB)) {
            // System.out.println(nounA);
            // System.out.println(nounB);
            throw new java.lang.IllegalArgumentException();
        }
        List<Integer> listV = map.get(nounA);
        List<Integer> listW = map.get(nounB);
        int p = -1;
        for (int v : listV) {
            for (int w : listW) {
                int path = s.length(v, w);
                if (p == -1)
                    p = path;
                else if (p > path) {
                    p = path;
                }
            }
        }
        return p;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of
    // nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new java.lang.NullPointerException();
        }
        if (!map.containsKey(nounA) || !map.containsKey(nounB))
            throw new java.lang.IllegalArgumentException();
        List<Integer> listV = map.get(nounA);
        List<Integer> listW = map.get(nounB);
        int p = -1;
        int v_cool = -1, w_cool = -1;
        for (int v : listV) {
            for (int w : listW) {
                int path = s.length(v, w);
                if (p == -1) {
                    p = path;
                    v_cool = v;
                    w_cool = w;

                } else if (p > path) {
                    p = path;
                    v_cool = v;
                    w_cool = w;
                }
            }
        }
        if (p == -1)
            return null;
        else
            return idNoun.get(s.ancestor(v_cool, w_cool));
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet w = new WordNet("synsets8.txt", "hypernyms8ManyAncestors.txt");
        /*
         * for(String s : w.nouns) { System.out.println(s); }
         */
        System.out.println(w.distance("a", "b"));
        System.out.println(w.sap("a", "b"));
        // System.out.println(w.sap("'hood", "1760s"));
        // System.out.println(w.sap("i", "k"));
    }
}