import java.util.HashMap;
import java.util.Map;

public class Outcast {

    private WordNet w;

    public Outcast(WordNet wordnet) {
        // constructor takes a WordNet object
        w = wordnet;
    }

    public String outcast(String[] nouns) {
        // given an array of WordNet nouns, return an outcast
        int distances = 0;
        Map<String, Integer> map = new HashMap<>();
        for (String nounA : nouns) {
            distances = 0;
            for (String nounB : nouns) {
                distances += w.distance(nounA, nounB);
            }
            map.put(nounA, distances);
        }
        int max_dist = map.get(nouns[0]);
        String s_noun = nouns[0];
        for (String n : nouns) {
            if (max_dist < map.get(n)) {
                max_dist = map.get(n);
                s_noun = n;
            }
        }
        return s_noun;
    }

    public static void main(String[] args) {
        // see test client below
        WordNet wordnet = new WordNet("synsets.txt", "hypernyms.txt");
        Outcast outcast = new Outcast(wordnet);
        String[] nouns = { "Turing", "von_Neumann", "Mickey_Mouse" };

        System.out.println(outcast.outcast(nouns));

    }
}