package csp;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public abstract class CSP<X> {
    private Map<String, X> variable;
    private Map<String, List<X>> domains;
    private BiFunction<X, X, Boolean> constraints;
    private String variable_heuristic;
    private String domain_heuristic;
    private Map<String, List<String>> neighbors;
    private Map<String, List> removed;
    private AC3 ac3;

    public CSP(String variable_heuristic, String domain_heuristic) {
        this.variable_heuristic = variable_heuristic;
        this.domain_heuristic = domain_heuristic;

        this.removed = new HashMap<>();
        this.neighbors = new HashMap<>();
        this.ac3 = new AC3();
    }

    public CSP(Map<String, X> variable, Map<String, List<X>> domains, BiFunction<X, X, Boolean> constraints,
               String variable_heuristic, String domain_heuristic) {
        this(variable_heuristic, domain_heuristic);
        this.variable = variable;
        this.domains = domains;
        this.constraints = constraints;
    }

    public Map<String, X> getVariable() {
        return variable;
    }

    public void setVariable(Map<String, X> variable) {
        this.variable = variable;
    }

    public Map<String, List<X>> getDomains() {
        return domains;
    }

    public void setDomains(Map<String, List<X>> domains) {
        this.domains = domains;
    }

    public BiFunction<X, X, Boolean> getConstraints() {
        return constraints;
    }

    public void setConstraints(BiFunction<X, X, Boolean> constraints) {
        this.constraints = constraints;
    }

    public Map<String, List<String>> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(Map<String, List<String>> neighbors) {
        this.neighbors = neighbors;
    }

    public abstract void build_neighbors();

    public abstract boolean solved();

    public abstract boolean valid_assignment(String variable, X value);

    public boolean backtracking_search() {
        boolean has_solution;
        this.ac3.ac3(this); // preprocessing step - arc consistency
        if (this.consistent()) {
            this.getDomains().keySet().forEach(x -> {
                this.getVariable().put(x, this.getDomains().get(x).get(0));
            });
            has_solution = true;
        } else {
            this.getVariable().keySet().forEach(v -> {
                this.removed.put(v, new ArrayList<>());
            });
            has_solution = this.backtrack();
        }
        return has_solution;
    }

    private boolean backtrack() {
        if (this.solved()) {
            return true;
        }

        String var = this.select_unassigned_variable(this.variable_heuristic);
        for (Object value : this.order_domain_values(var, this.domain_heuristic)) {
            if (this.valid_assignment(var, (X) value)) {
                this.getVariable().put(var, (X) value);
                this.forward_check(var, (X) value);

                boolean result = this.backtrack();
                if (result) {
                    return true;
                }

                this.getVariable().put(var, null);
                this.removed.get(var).stream().forEach(x -> {
                    this.getDomains().get(((List) x).get(0)).add((X) ((List) x).get(1));
                });
                this.removed.put(var, new ArrayList<>());
            }
        }

        return false;

    }

    private void forward_check(String var, X value) {
        this.getNeighbors().get(var).stream().filter(x -> this.getVariable().get(x) == null).forEach(x -> {
            if (this.getDomains().get(x).stream().anyMatch(value::equals)) {
                this.getDomains().get(x).remove(value);
                this.removed.get(var).add(Arrays.asList(x, value));
            }
        });
    }

    private String minimum_remaining_values() {
        return this.getDomains().keySet().stream().filter(key -> this.getVariable().get(key) == null).
                min(Comparator.comparing(x -> this.getDomains().get(x).size())).get();
    }

    private String next_uassigned_variable() {
        return this.getVariable().keySet().stream().filter(x -> this.getVariable().get(x) == null).findFirst().get();
    }

    public String select_unassigned_variable(String heuristic) {
        if (heuristic.equals("mrv")) {
            return this.minimum_remaining_values();
        }

        return this.next_uassigned_variable();
    }

    private List<X> no_heuristic_values(String variable) {
        return this.getDomains().get(variable);
    }

    private List<X> least_constraining_values(String variable) {
        Map<X, Integer> domain = new HashMap<>();
        this.getDomains().get(variable).forEach(x -> domain.put(x, 0));
        if (this.getDomains().get(variable).size() > 1) {
            this.getNeighbors().get(variable).forEach(neighbor -> {
                this.getDomains().get(neighbor).forEach(x -> {
                    if (domain.keySet().stream().anyMatch(x::equals)) {
                        domain.put(x, domain.get(x) + 1);
                    }
                });
            });
        }
        return domain.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).
                map(Map.Entry::getKey).collect(Collectors.toList());
    }

    public List<X> order_domain_values(String variable, String heuristic) {
        if (heuristic.equals("lcv")) {
            return this.least_constraining_values(variable);
        }
        return this.no_heuristic_values(variable);
    }


    public boolean consistent() {
        return this.getDomains().keySet().stream().allMatch(x -> this.getDomains().get(x).size() == 1);
    }
}