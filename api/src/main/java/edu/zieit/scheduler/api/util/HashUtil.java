package edu.zieit.scheduler.api.util;

import org.apache.poi.util.IOUtils;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Util to calculate hash sum for various data
 */
public final class HashUtil {

    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    private HashUtil() { }

    /**
     * Get Sha-1 hash of the string
     * @return Sha-1 hash of the string
     */
    public static String getHash(final String str){
        try{
            return getSHA1(str.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e){
            return null;
        }
    }

    /**
     * Get Sha-1 hash of file by URL
     * @return Sha-1 hash of the file
     */
    public static String getHash(final URL url){
        try{
            byte[] bytes = IOUtils.toByteArray(url.openStream());
            return getSHA1(bytes);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get Sha-1 hash of byte array
     * @return Sha-1 hash of byte array
     */
    public static String getSHA1(final byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(bytes);
        return bytesToHex(crypt.digest());
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

}
