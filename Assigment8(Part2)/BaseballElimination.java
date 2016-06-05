import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class BaseballElimination {
    
    private int[] wins;
    private int[] loss;
    private int[] left;
    private int[][] games;
    private int number; //number of Teams
    private Map<String, Integer> teams;
    private String[] teams2;
    
    
    public BaseballElimination(String filename) {
        // create a baseball division from given filename in format specified below
        if (filename == null) throw new java.lang.NullPointerException();
        In file = new In(filename);
        number = file.readInt();
        teams = new HashMap<>(number);
        wins = new int[number];
        loss = new int[number];
        left = new int[number];
        games = new int[number][number];
        teams2 = new String[number];
        for (int i = 0; i < number; i++) {
            String s = file.readString();
            //System.out.println(s);
            teams.put(s, i);
            teams2[i] = s;
            wins[i] = file.readInt();
            loss[i] = file.readInt();
            left[i] = file.readInt();
            for (int j = 0; j < number; j++) {
                games[i][j] = file.readInt();
            }
        }
        
        
    }
    public int numberOfTeams() {
        // number of teams
        return number;
    }
    public Iterable<String> teams() {
        // all teams
        return teams.keySet();
    }
    public int wins(String team) {
        // number of wins for given team
        if (!teams.containsKey(team)) 
            throw new java.lang.IllegalArgumentException();
        return wins[teams.get(team)];
    }
    public int losses(String team) {
        // number of losses for given team
        if (!teams.containsKey(team)) 
            throw new java.lang.IllegalArgumentException();
        return loss[teams.get(team)];
    }
    public int remaining(String team) {
        // number of remaining games for given team
        if (!teams.containsKey(team)) 
            throw new java.lang.IllegalArgumentException();
        return left[teams.get(team)];
    }
    public int against(String team1, String team2) {
        if (!teams.containsKey(team1) || !teams.containsKey(team2)) 
            throw new java.lang.IllegalArgumentException();
        // number of remaining games between team1 and team2
        return games[teams.get(team1)][teams.get(team2)];
    }
    public boolean isEliminated(String team) {
        if (!teams.containsKey(team)) 
            throw new java.lang.IllegalArgumentException();
        //trivial check
        int x = teams.get(team);
        for (int i = 0; i < number; i++) {
            if (wins[x] + left[x] < wins[i])
                return true;
        }
        //nontrivial check
        //create edges to t, t = x
        int n = number - 2;
        n = n * (n + 1) / 2; 
        //System.out.println("n = " + n);
        int V = number + n + 1;
        //System.out.println("x = " + x);
        FlowNetwork fn = new FlowNetwork(V);
        for (int i = 0; i < number; i++) {
            if (i != x) {
                FlowEdge fe = new FlowEdge(i, x, wins[x] + left[x] - wins[i]);
                fn.addEdge(fe);
            }
        }
        int v = number; // this is s vertex
        int w = number + 1; // this will be verteces 0-1, 0-2, ... etc.
        for (int i = 0; i < number; i++) {
            for (int j = i + 1; j < number; j++) {
                if (i != x && j != x) {
                    FlowEdge fe = new FlowEdge(v, w, games[i][j]);
                    fn.addEdge(fe);
                    FlowEdge fei = new FlowEdge(w, i, Double.POSITIVE_INFINITY);
                    fn.addEdge(fei);
                    FlowEdge fej = new FlowEdge(w, j, Double.POSITIVE_INFINITY);
                    fn.addEdge(fej);
                    w++;
                }
            }
        }
        //System.out.println(fn.toString());
        //System.out.println("s = " + v + " t = " + x);
        
        FordFulkerson ff = new FordFulkerson(fn, v, x);
        
        //System.out.println(fn.toString());
        for (int i = 0; i < number; i++) {
            if (ff.inCut(i)) {
                return true;
            }
        }
        return false;
        // is given team eliminated?
    }
    public Iterable<String> certificateOfElimination(String team) {
        if (!teams.containsKey(team)) 
            throw new java.lang.IllegalArgumentException();
        // subset R of teams that eliminates given team; null if not eliminated
        List<String> answer = new ArrayList<>();
        //trivial check
        int x = teams.get(team);
        for (int i = 0; i < number; i++) {
            if (wins[x] + left[x] < wins[i]) {
                answer.add(teams2[i]);
                return answer;
            }
        }

        //nontrivial check
        //create edges to t, t = x
        int n = number - 2;
        n = n * (n + 1) / 2; 
        //System.out.println("n = " + n);
        int V = number + n + 1;
        //System.out.println("x = " + x);
        FlowNetwork fn = new FlowNetwork(V);
        for (int i = 0; i < number; i++) {
            if (i != x) {
                FlowEdge fe = new FlowEdge(i, x, wins[x] + left[x] - wins[i]);
                fn.addEdge(fe);
            }
        }
        int v = number; // this is s vertex
        int w = number + 1; // this will be verteces 0-1, 0-2, ... etc.
        for (int i = 0; i < number; i++) {
            for (int j = i + 1; j < number; j++) {
                if (i != x && j != x) {
                    FlowEdge fe = new FlowEdge(v, w, games[i][j]);
                    fn.addEdge(fe);
                    FlowEdge fei = new FlowEdge(w, i, Double.POSITIVE_INFINITY);
                    fn.addEdge(fei);
                    FlowEdge fej = new FlowEdge(w, j, Double.POSITIVE_INFINITY);
                    fn.addEdge(fej);
                    w++;
                }
            }
        }
        //System.out.println(fn.toString());
        //System.out.println("s = " + v + " t = " + x);
        FordFulkerson ff = new FordFulkerson(fn, v, x);
        
        //System.out.println(fn.toString());
        for (int i = 0; i < number; i++) {
            if (ff.inCut(i)) {
                answer.add(teams2[i]);
            }
        }
        
        //for (FlowEdge fe : fn.edges()) {
        //    if (fe.from() == v && fe.flow() != fe.capacity())
        //        ;
        //}
        if (answer.isEmpty())
            return null;
        return answer;
    }
    
    public static void main(String[] args) {
        // unit testing of the methods (optional) 
        BaseballElimination division = new BaseballElimination("teams4.txt");
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }

           // System.out.println(b.isEliminated("Atlanta"));
            //System.out.println(b.isEliminated("Philadelphia"));
           // System.out.println(b.isEliminated("New_York"));
           // System.out.println(b.isEliminated("Montreal"));
        } 
    }
}
