public class MaxLengthOfPublicPrefix {
    public String longestCommonPrefix(String[] strs) {
        int j = 0;
        while(true) {
            int i;
            for (i = 1; i < strs.length; i++) {
                if (strs[i].charAt(j) != strs[0].charAt(j)) {
                  break;
                }
            }
            if (i != strs.length) {
              break;
            }
            
            j++;
        }
        return strs[0].substring(0, j);
    }
}
