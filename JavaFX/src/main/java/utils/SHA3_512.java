package utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SHA3_512 {
    private final String dataToHash;
    private final String salt;

    public SHA3_512(String dataToHash, String salt) {
        this.dataToHash = dataToHash;
        this.salt = salt;
    }

    public String encoder() throws NoSuchAlgorithmException {
        String b64E = Base64.getEncoder().encodeToString(dataToHash.getBytes());
        MessageDigest digest = MessageDigest.getInstance("SHA3-512");
        byte[] encodedHash = digest.digest((b64E + salt).getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for(byte b: encodedHash) {
            sb.append(String.format("%02x", b));
            // %02x : print at least 2 digits, prepend it with 0's if there's less
        }
        return sb.toString();
    }
}
