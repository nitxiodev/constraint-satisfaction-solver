package sudoku;

import csp.CSP;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Sudoku extends CSP<Integer> {
    public Sudoku(String sudoku, String variable_heuristic, String domain_heuristic) {
        super(variable_heuristic, domain_heuristic);
        Map<String, List<Integer>> sudoku_domains = new HashMap<>();

        for (int i = 0; i < sudoku.length(); i++) {
            int value = Integer.parseInt(String.valueOf(sudoku.charAt(i)));
            int row = i / 9;
            int col = i % 9;
            sudoku_domains.put(String.valueOf((char) (row + 65)).toUpperCase() + "_" + String.valueOf(col), value > 0 ?
                    new ArrayList<>(Collections.singletonList(value)) : IntStream.range(1, 10).boxed().collect(Collectors.toList()));
        }

        Map<String, Integer> sudoku_variables = new HashMap<>();
        sudoku_domains.keySet().forEach(x -> sudoku_variables.put(x, null));
        BiFunction<Integer, Integer, Boolean> sudoku_constraints = (x, y) -> !x.equals(y);

        this.setDomains(sudoku_domains);
        this.setVariable(sudoku_variables);
        this.setConstraints(sudoku_constraints);
        this.build_neighbors();
    }

    @Override
    public void build_neighbors() {
        this.getVariable().keySet().forEach(var -> {
            List<String> concatenation = Stream.of(this.row_neighbors(var), this.col_neighbors(var), this.box_neighbors(var)).
                    flatMap(Collection::stream).collect(Collectors.toList());
            this.getNeighbors().put(var, concatenation.stream().distinct().collect(Collectors.toList()));
        });
    }

    @Override
    public boolean solved() {
        return this.getVariable().keySet().stream().allMatch(x -> this.getVariable().get(x) != null);
    }

    @Override
    public boolean valid_assignment(String variable, Integer value) {
        return this.getNeighbors().get(variable).stream().noneMatch(x -> Objects.equals(this.getVariable().get(x), value));
    }


    /*Helper functions*/
    private List<String> row_neighbors(String variable) {
        String[] splitted_var = variable.split("_");
        List<String> neighbors = new ArrayList<>();
        IntStream.range(0, 8).boxed().forEach(x -> {
            if (!String.valueOf((char) (x + 65)).equals(splitted_var[0])) {
                neighbors.add(((char) (x + 65)) + "_" + splitted_var[1]);
            }
        });
        return neighbors;
    }

    private List<String> col_neighbors(String variable) {
        String[] splitted_var = variable.split("_");
        List<String> neighbors = new ArrayList<>();
        IntStream.range(0, 8).boxed().forEach(x -> {
            if (x != Integer.parseInt(splitted_var[1])) {
                neighbors.add(splitted_var[0] + "_" + x);
            }
        });
        return neighbors;
    }

    private List<String> box_neighbors(String variable) {
        String[] splitted_var = variable.split("_");
        int row = ((int) splitted_var[0].charAt(0) - 65) / 3;
        int col = Integer.parseInt(splitted_var[1]) / 3;
        List<String> neighbors = new ArrayList<>();
        for (int i = 3 * row; i < 3 * row + 3; i++) {
            for (int j = 3 * col; j < 3 * col + 3; j++) {
                String cur_cell = String.valueOf((char) (i + 65)) + "_" + j;
                if (!variable.equals(cur_cell)) {
                    neighbors.add(cur_cell);
                }
            }
        }
        return neighbors;
    }

    public String pretty_print() {
        StringBuilder pretty_sol = new StringBuilder("    \033[4m1\033[0m \033[4m2\033[0m \033[4m3\033[0m \033[4m4\033[0m " +
                "\033[4m5\033[0m \033[4m6\033[0m \033[4m7\033[0m \033[4m8\033[0m \033[4m9\033[0m\n");

        List<String> ordered_variables = this.getVariable().entrySet().stream().
                sorted(Map.Entry.comparingByKey()).map(Map.Entry::getKey).collect(Collectors.toList());

        for (int i = 0; i < ordered_variables.size(); i++) {
            if (((i + 1) % 9) == 1) {
                pretty_sol.append(ordered_variables.get(i).split("_")[0]).append(" | ");
            }
            Integer value = this.getVariable().get(ordered_variables.get(i));
            value = value == null ? 0 : value;
            pretty_sol.append(value).append(" ");
            if (((i + 1) % 9) == 0) {
                pretty_sol.append("\n");
            }
        }
        return pretty_sol.toString();
    }
}
