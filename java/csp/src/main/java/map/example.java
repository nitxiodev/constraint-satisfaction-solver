package map;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class example {
    public static void main(String[] args) {
        Map<Integer, String> colors = Stream.of(
                new AbstractMap.SimpleEntry<>(1, "red"),
                new AbstractMap.SimpleEntry<>(2, "blue"),
                new AbstractMap.SimpleEntry<>(3, "green")).
                collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));

        MapColor map = new MapColor("mrv", "lcv");
        if(map.backtracking_search()) {
            map.getVariable().entrySet().stream().forEach(entry -> {
                System.out.println(String.format("[%s] = %s", entry.getKey(), colors.get(entry.getValue())));
            });
        } else {
            System.out.println("No solution :(");
        }
    }
}
