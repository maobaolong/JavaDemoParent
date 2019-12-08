import java.util.Arrays;

public class MergeSortedArray {

  public static void merge(int[] nums1, int m, int[] nums2, int n) {
    for (int i = 0; i < nums2.length; i++) {
      nums1[m + i] = nums2[i];
    }
  }

  public static void main(String[] args) {
  }
}
