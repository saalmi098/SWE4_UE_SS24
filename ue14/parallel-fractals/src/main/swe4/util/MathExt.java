package swe4.util;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MathExt {
  public static final double EPSILON = 1e-10;

  public static double transform(double x, double x1, double x2, double y1, double y2) {
    if (Math.abs(x2 - x1) < EPSILON)
      throw new ArithmeticException("Interpolation error: x1 and x2 are too close");
    return y1 + (x - x1) * (y2 - y1) / (x2 - x1);
  }

  private static void shuffleArray(int[] array) {
    Random random = ThreadLocalRandom.current();
    for (int i = array.length - 1; i > 0; i--) {
      int index = random.nextInt(i + 1);
      // swap ar[i] and ar[index]
      int a = array[index];
      array[index] = array[i];
      array[i] = a;
    }
  }

  public static record Range(int from, int to) {}
  public static record Set(int[] elements) {
    public int size() { return elements.length; }
  }

  public static Range[] partition(int from, int to, int nPartitions) {
    Range[] ranges = new Range[nPartitions];
    double partitionSize = (to - from) / (double)nPartitions;
    for (int i = 0; i < nPartitions; i++) {
      ranges[i] = new Range((int) Math.round(i * partitionSize) + (i==0 ? 0 : 1),
                            (int) Math.round((i + 1) * partitionSize));
    }

    return ranges;
  }

  public static Set[] randomPartition(int from, int to, int nPartitions) {
    Random random = ThreadLocalRandom.current();
    int[] array = new int[to-from+1];
    for (int i=0; i<to-from+1; i++) array[i] = from+i;

    shuffleArray(array);

    Set[] subSets = new Set[nPartitions];
    double partitionSize = (to - from) / (double)nPartitions;
    for (int i = 0; i < nPartitions; i++) {
      int start = (int) Math.round(i * partitionSize) + (i==0 ? 0 : 1);
      int last  = (int) Math.round((i + 1) * partitionSize);

      int[] subset = new int[last-start+1];
      for (int j=0; j<last-start+1; j++)
           subset[j] = array[start+j];
      subSets[i] = new Set(subset);
    }

    return subSets;
  }
}
