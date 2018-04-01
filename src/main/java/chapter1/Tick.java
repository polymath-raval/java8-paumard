package chapter1;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;
import org.apache.commons.lang3.tuple.Pair;

public class Tick {


  private static final Pair<String, BigDecimal>[] notationValues = getNotationValues();

  private static final Pair<String, BigDecimal>[] getNotationValues() {
    BigDecimal sixtyFour = new BigDecimal(64);
    return IntStream.range(0, 64).boxed()
        .map(i -> Pair.of((i / 2) + "" + (i % 2 == 0 ? "" : "+"),
            new BigDecimal(i).divide(sixtyFour)))
        .toArray(Pair[]::new);
  }


  public static void main(String[] args) {
    BigDecimal value = new BigDecimal("0.00");
    BigDecimal hundred = new BigDecimal("100");
    BigDecimal pointZeroOne = new BigDecimal("0.001");
    Map<String, Set<BigDecimal>> mapFromBinarySearch = new HashMap<>();
    Map<String, Set<BigDecimal>> mapFromMinimum = new HashMap<>();

    while (value.compareTo(hundred) <= 0) {
      String notationFromBinarySearch = getNotation(value, true);
      String notationFromMinimum = getNotation(value, false);
      mapFromBinarySearch.putIfAbsent(notationFromBinarySearch, new HashSet<>());
      mapFromMinimum.putIfAbsent(notationFromMinimum, new HashSet<>());
      mapFromBinarySearch.get(notationFromBinarySearch).add(value);
      mapFromMinimum.get(notationFromMinimum).add(value);
      value = value.add(pointZeroOne);
    }

    System.out.println(mapFromBinarySearch.equals(mapFromMinimum));
    System.out.println(mapFromBinarySearch);

  }

  private static String getNotation(final BigDecimal value, Boolean withBinarySearch) {
    final BigDecimal fractionPart = value.remainder(BigDecimal.ONE);
    final BigDecimal integerPart = value.subtract(fractionPart).setScale(0);
    return integerPart + "-" +
        notationValues[(withBinarySearch ?
            getIndexWithBinarySearch(notationValues, fractionPart, 0, notationValues.length - 1) :
            getIndexWithMinimum(notationValues, fractionPart))].getKey();
  }

  private static int getIndexWithBinarySearch(Pair<String, BigDecimal>[] values, BigDecimal value,
      int startIndex,
      int endIndex) {
    if (endIndex == startIndex) {
      return endIndex;
    } else if (endIndex - startIndex < 2) {
      return values[endIndex].getValue().subtract(value)
          .compareTo(value.subtract(values[startIndex].getValue())) <= 0 ?
          endIndex : startIndex;
    } else {
      int midIndex = (startIndex + endIndex) / 2;
      int comparision = values[midIndex].getValue().compareTo(value);
      return comparision == 0 ?
          midIndex :
          comparision < 0 ?
              getIndexWithBinarySearch(values, value, midIndex, endIndex) :
              getIndexWithBinarySearch(values, value, startIndex, midIndex);
    }
  }

  private static int getIndexWithMinimum(Pair<String, BigDecimal>[] values, BigDecimal value) {
    int index = -1;
    BigDecimal diff = null;
    for (int i = 0; i < values.length; i++) {
      BigDecimal valueFromArray = values[i].getValue();
      BigDecimal diffFromCurrentValue = valueFromArray.subtract(value).abs();
      if (diff == null || diff.compareTo(diffFromCurrentValue) > 0) {
        diff = diffFromCurrentValue;
        index = i;
      }
    }
    return index;
  }

}
