package csp;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class AC3 {
    public AC3() {
        //
    }

    public boolean ac3(CSP csp) {
        Queue<Tuple> queue = (Queue<Tuple>) csp.getVariable().keySet().stream().map(e ->
                ((List) csp.getNeighbors().get(e)).stream().map(o -> new Tuple<>(e, o)).
                        collect(Collectors.toList())).flatMap(e -> ((List) e).stream()).
                collect(Collectors.toCollection(LinkedList::new));

        while (!queue.isEmpty()) {
            Tuple t = queue.poll();
            String x_i = (String) t.getX();
            String x_j = (String) t.getY();
            if (this.revise(csp, x_i, x_j)) {
                if (((List) csp.getDomains().get(x_i)).isEmpty()) {
                    return false;
                }
                ((List) csp.getNeighbors().get(x_i)).stream().filter(x -> !x.equals(x_j)).
                        forEach(x_k -> queue.add(new Tuple<>(x_k, x_i)));
            }
        }
        return true;
    }

    private boolean revise(CSP csp, String x_i, String x_j) {
        boolean revised = false;
        Iterator it = ((List) csp.getDomains().get(x_i)).iterator();
        while (it.hasNext()) {
            Object x = it.next();
            if (((List) csp.getDomains().get(x_j)).stream().noneMatch(y -> (Boolean) csp.getConstraints().apply(x, y))) {
                it.remove();
                revised = true;
            }

        }

        return revised;
    }
}
