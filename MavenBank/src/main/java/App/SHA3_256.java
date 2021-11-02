package App;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA3_256 {
    private final String dataToHash;
    private MessageDigest digest;

    public SHA3_256(String dataToHash) {
        this.dataToHash = dataToHash;
    }
    
    public String encoder() throws NoSuchAlgorithmException {
        digest = MessageDigest.getInstance("SHA3-256");
        byte[] encodedHash = digest.digest(dataToHash.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for(byte b: encodedHash) {
            sb.append(String.format("%02x", b));
            // %02x : print at least 2 digits, prepend it with 0's if there's less
        }
        return sb.toString();
    }

}
