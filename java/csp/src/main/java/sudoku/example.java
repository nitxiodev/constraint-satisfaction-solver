package sudoku;

import java.util.HashMap;

public class example {
    private static void run(String sudoku, HashMap<String, String> sudokus) {
        Sudoku s = new Sudoku(sudokus.get(sudoku), "mrv", "lcv");
        long before = System.currentTimeMillis();
        if (s.backtracking_search()) {
            System.out.println(s.pretty_print());
        } else {
            System.out.println("No solution :(");
        }
        double now = (System.currentTimeMillis() - before) / 1000.0;
        System.out.printf("RAW SUDOKU [%s]: %s (%.3fs)\n\n", sudoku.substring(0, 1).toUpperCase() + sudoku.substring(1),
                sudokus.get(sudoku), now);
    }

    public static void main(String[] args) {
        HashMap<String, String> sudokus = new HashMap<>();
        sudokus.put("easy", "000030407570980360040706001000005043050803070380200000800401020012079038905020000");
        sudokus.put("medium", "000050000004700080200048030045000328900000001826000940060930002080002600000060000");
        sudokus.put("hard", "004009270000001003000000910080420050090070080060058030015000000900800000038700400");
        sudokus.put("evil", "003500400000400050000060109020009004908000703100700020301080000080001000006002300");

        sudokus.keySet().forEach(x -> {
            example.run(x, sudokus);
        });
    }
}
