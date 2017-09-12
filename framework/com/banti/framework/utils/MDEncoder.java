package com.banti.framework.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MDEncoder {

    /**
     * encoding strings
     *
     * @return String : encorded string
     */
    public static String encode(String target) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(target.getBytes());
        return convByte2HexStr(md.digest());
    }

    public static String encode(byte[] target) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(target);
        return convByte2HexStr(md.digest());
    }
    
    /**
     * convert byte data to Hex Strings
     *
     * @param b an array of encrypted byte data
     * @return String : hex strings
     */
    public static final String convByte2HexStr(byte[] b) {
        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < b.length; i++) {
            buf.append(convHex2Ascii((b[i] >>> 4) & 0x0F));
            buf.append(convHex2Ascii(b[i] & 0x0F));
        }
        return buf.toString();
    }

    /**
     * convert to hex string to ascii
     *
     * @param h - 
     * @return char : character [0-9,A-F]
     */
    public static char convHex2Ascii(int h) {
        if ((h >= 10) && (h <= 15))
            return (char) ('A' + (h - 10));
        if ((h >= 0) && (h <= 9))
            return (char) ('0' + h);
        throw new Error("hex to ascii failed");
    }

    public static final byte[] convHexStr2Byte(String hex) {
        int myLength = (hex.length() + 1) >> 1;
        byte[] myByte = new byte[myLength];

        for (int i = 0; i < myLength; i++) {
            myByte[i] = (byte) Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
        }
        return (myByte);
    }

    /**
     * test
     */
    /*
     public static void main(String[] args){
     String target1 = "Dec 20 11:55:04 aso sendmail[25176]: [ID 801593 mail.info] gBK2t48h025176: from=<zero@cysols.com>, size=0, class=0, nrcpts=2, proto=ESMTP, daemon=MTA-v4, relay=dhcp239.priv.cysol.co.jp [192.168.0.239]";
     
     String target2 = "Dec 20 11:55:05 aso sendmail[25176]: [ID 801593 mail.info] gBK2t48j025176: from=<zero@cysols.com>, size=377, class=0, nrcpts=2, msgid=<20021220115442.1E34.ZERO@cysol.co.jp>, proto=ESMTP, daemon=MTA-v4, relay=dhcp239.priv.cysol.co.jp [192.168.0.239]";
     try {
     System.out.println("Log Record:\n" + target1);
     System.out.println("Digest: " + MDEncoder.encode(target1));

     System.out.println("\n");
     System.out.println("Log Record:\n" + target2);
     System.out.println("Digest: " + MDEncoder.encode(target2));
     } catch(NoSuchAlgorithmException ne){
     System.out.println("No Such Algorithm exception "+ne);
     }
     }
     */
}
