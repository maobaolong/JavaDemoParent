import java.util.Arrays;

public class SortedSquares {

  public static int[] sortedSquares(int[] A) {
    int[] result = new int[A.length];
    for (int i = 0; i< A.length; i++) {
      result[i] = A[i] * A[i];
    }
    Arrays.sort(result);
    return result;
  }

  public static void main(String[] args) {
    {
      int[] arrays = {-4, -1, 0, 3, 10};
      int[] result = sortedSquares(arrays);
      System.out.println(Arrays.toString(result));
    }
    
    {
      int[] arrays = {-7,-3,2,3,11};
      int[] result = sortedSquares(arrays);
      System.out.println(Arrays.toString(result));
    }
  }
}
