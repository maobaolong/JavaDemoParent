package net.mbl.demo;

import java.io.File;

/**
 * Created by mbl on 18/04/2018.
 */
public class BlockPath {
  static {
    System.out.println("ss");
  }
  private static  String SEP = System.getProperty("file.separator");

  static {
    System.out.println("bb");
  }
  public static final String BLOCK_SUBDIR_PREFIX = "subdir";
  public static File idToBlockDir(File root, long blockId) {
    int d1 = (int)((blockId >> 16) & 0xff);
    int d2 = (int)((blockId >> 8) & 0xff);
    String path = BLOCK_SUBDIR_PREFIX + d1 + SEP +
        BLOCK_SUBDIR_PREFIX + d2;
    return new File(root, path);
  }
  public static void main(String[] args) {
    String blkIds = "blk_8407642885";
    String bpId = "BP-1869567922-172.16.172.37-1470393241388";
    String prefix = "/data*/dfs/current/" + bpId + "/current/finalized/";
    System.out.println("prefix = " + prefix);
    for (String blkId : blkIds.split(",")) {
      File f = idToBlockDir(new File(prefix), Long.parseLong(blkId.substring(4)));
      System.out.println(f + "/" + blkId);
    }
  }


//  public static void main(String[] args) {
//    String blkId = "blk_1149330537";
//    String bpId = "BP-777948004-10.198.24.235-1518587510557";
//    if (args.length > 3) {
//      bpId = args[1];
//      blkId = args[2];
//    }
//    String prefix = "/data*/dfs/current/" + bpId + "/current/finalized/";
//    System.out.println("prefix = " + prefix);
//    System.out.println("blkId = " + blkId);
//    File f = idToBlockDir(new File(prefix), Long.parseLong(blkId.substring(4)));
//    System.out.println(f + "/" + blkId);
//  }
}
