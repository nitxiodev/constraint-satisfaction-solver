package map;

import csp.CSP;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MapColor extends CSP<Integer> {
    public MapColor(String variable_heuristic, String domain_heuristic) {
        super(variable_heuristic, domain_heuristic);

        Map<String, List<Integer>> map_domains = Stream.
                of(
                        new AbstractMap.SimpleEntry<>("WA", IntStream.range(1, 4).boxed().collect(Collectors.toList())),
                        new AbstractMap.SimpleEntry<>("NT", IntStream.range(1, 4).boxed().collect(Collectors.toList())),
                        new AbstractMap.SimpleEntry<>("SA", IntStream.range(1, 4).boxed().collect(Collectors.toList())),
                        new AbstractMap.SimpleEntry<>("Q", IntStream.range(1, 4).boxed().collect(Collectors.toList())),
                        new AbstractMap.SimpleEntry<>("NSW", IntStream.range(1, 4).boxed().collect(Collectors.toList())),
                        new AbstractMap.SimpleEntry<>("V", IntStream.range(1, 4).boxed().collect(Collectors.toList())),
                        new AbstractMap.SimpleEntry<>("T", IntStream.range(1, 4).boxed().collect(Collectors.toList()))).
                collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));

        Map<String, Integer> map_variables = new HashMap<>();
        map_domains.keySet().forEach(x -> map_variables.put(x, null));
        BiFunction<Integer, Integer, Boolean> map_constraints = (x, y) -> !x.equals(y);

        this.setDomains(map_domains);
        this.setVariable(map_variables);
        this.setConstraints(map_constraints);
        this.build_neighbors();
    }

    @Override
    public void build_neighbors() {
        this.getNeighbors().put("WA", new ArrayList<>(Arrays.asList("NT", "SA")));
        this.getNeighbors().put("NT", new ArrayList<>(Arrays.asList("WA", "SA", "Q")));
        this.getNeighbors().put("SA", new ArrayList<>(Arrays.asList("NT", "SA", "Q", "NSW", "V")));
        this.getNeighbors().put("Q", new ArrayList<>(Arrays.asList("NT", "SA", "NSW")));
        this.getNeighbors().put("NSW", new ArrayList<>(Arrays.asList("Q", "SA", "V")));
        this.getNeighbors().put("V", new ArrayList<>(Arrays.asList("NSW", "SA")));
        this.getNeighbors().put("T", new ArrayList<>());
    }

    @Override
    public boolean solved() {
        return this.getVariable().keySet().stream().allMatch(x -> this.getVariable().get(x) != null);
    }

    @Override
    public boolean valid_assignment(String variable, Integer value) {
        return this.getNeighbors().get(variable).stream().noneMatch(x ->
                Objects.equals(this.getVariable().get(x), value));
    }
}
