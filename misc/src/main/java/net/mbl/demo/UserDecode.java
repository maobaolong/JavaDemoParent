package net.mbl.demo;


import org.apache.commons.codec.binary.Base64;

/**
 * Created by mbl on 11/04/2018.
 */
public class UserDecode {
  public static void main(String[] args) {
    String desDecrypt = "t4Zqyog8xwML/AMfUzeLRVmcyNEwNa72XPUjiFojTnJ8B9WPKuxZe+Y7HDis5MNIdf8OQe9aNYh38keRcNZHDjxkM1G7ws0NFb4VBZ039e+7iEDZafgDnbNcgpR/yHx4m3hlxMkZh7r+4+KCQKGN24utewm/FWf733u8NiuG0rqmpEUsOXCdnUG6KBPYHglwZhDqkQFRRcXoe99KM9dpyS4IDfR9yQOBCOiFUFx19yvcMqK7jiF17RUS44n02EKImF3yDO1/3i1q5/PSOrkfpHfEbGuW+6t+WQNTUSfvenIqXTBAPj0HzoldSUmkkMj3t4nKsLy1pQojgjf1lY9D9A==IyMjIw==bWFydF9tb2JpbGU=";
    String splitStr = Base64.encodeBase64String("####".getBytes());
    String[] certificateTonken = desDecrypt.split(splitStr.trim());
    System.out.println(certificateTonken.length);
    System.out.println("[extends log] 使用令牌, certificateTonken 解密用户 = " + new String(Base64
        .decodeBase64(certificateTonken[1])));
    // 1.7.2 获取envUser
    String envUser = new String(Base64.decodeBase64(certificateTonken[1]));
    // 1.7.3 获取userKey
    String userKey = certificateTonken[0];
    System.out.println(envUser);
    System.out.println(userKey);
  }
}
