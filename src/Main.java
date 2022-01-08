import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        if (args.length < 1)
        {
            System.out.println("USAGE: <pathname>");
            return;
        }

        String pathToFile = args[0]; // path to trade.txt

        try (Stream<String> stream = Files.lines(Paths.get(pathToFile))) {

            Comparator<List<TradeRow>> priceGrowCmp = (list1, list2) -> {
                Double d1 = list1.get(list1.size() - 1).m_price - list1.get(0).m_price;
                Double d2 = list2.get(list2.size() - 1).m_price - list2.get(0).m_price;
                return d1.compareTo(d2);
            };

            Comparator<TradeRow> dateCmp = Comparator.comparing(trade -> trade.m_tradetime);

            var groups = stream
                    .skip(1)
                    .map(TradeRow::FromString)
                    .filter((r) -> r.m_secboard.equals("TQBR") || r.m_secboard.equals("FQBR"))
                    .collect(Collectors.groupingBy(TradeRow::SecCode))
                    .values()
                    .stream()
                    .peek((group) -> group.sort(dateCmp))
                    .sorted(priceGrowCmp)
                    .toList();

            var worstGroups = groups.subList(0, 10);
            var bestGroups = groups.subList(groups.size() - 10, groups.size());

            System.out.println("Worst:");
            printList(worstGroups);
            System.out.println("Best:");
            printList(bestGroups);

        } catch (IOException e) {
            System.out.println ("No file " + pathToFile);
            System.out.println("USAGE: <pathname>");
            e.printStackTrace();
        }
    }

    public static void printList (List<List<TradeRow>> groups) {
        System.out.println("  SecCode  | Number of trades | Total volume | Diff in %");
        groups.forEach((group) -> {
            TradeRow first = group.get(0);
            TradeRow last = group.get(group.size() - 1);

            Double priceDiffPercent = 100. * (last.m_price - first.m_price) / first.m_price;
            Double totalVolume = group.stream()
                    .map(TradeRow::Value)
                    .reduce(Double::sum)
                    .get();

            System.out.printf("%10s  %13d %18.2f %+7.2f%%\n", first.m_seccode, group.size(),
                    totalVolume, priceDiffPercent);
        });
    }
}