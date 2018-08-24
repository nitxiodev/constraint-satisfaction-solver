package sudoku;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class example_scraping {
    public static void main(String[] args) {
        Map<String, String> sudokus = Stream.
                of(new AbstractMap.SimpleEntry<>("easy", ""),
                        new AbstractMap.SimpleEntry<>("medium", ""),
                        new AbstractMap.SimpleEntry<>("hard", ""),
                        new AbstractMap.SimpleEntry<>("evil", "")).
                collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
        try (Fetcher fetcher = new Fetcher()) {
            // Get sudokus
            sudokus.keySet().forEach(sudoku -> {
                sudokus.put(sudoku, fetcher.fetch_sudoku(sudoku.toUpperCase()));
            });

            // Solve sudokus
            sudokus.keySet().forEach(sudoku -> {
                Sudoku s = new Sudoku(sudokus.get(sudoku), "mrv", "lcv");
                long before = System.currentTimeMillis();
                if(s.backtracking_search()) {
                    System.out.println(s.pretty_print());
                } else {
                    System.out.println("No solution :(");
                }
                double now = (System.currentTimeMillis() - before) / 1000.0;
                System.out.println(String.format("RAW SUDOKU [%s]: %s (%.3fs)\n", sudoku.toUpperCase(),
                        sudokus.get(sudoku), now));
            });
        }
    }
}