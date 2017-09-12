
package com.banti.framework.platform.licence;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.prefs.Preferences;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class PrefsLicenseDelegator {

    private boolean userMode;

    public PrefsLicenseDelegator() {
        this(false);
    }

    public PrefsLicenseDelegator(boolean mode) {
        super();
        userMode = mode;
    }
    
    public void register(Class c, String key, String code) throws SecurityException {
        Preferences prefs = null;
        if (userMode) {
            prefs = Preferences.userNodeForPackage(c);
        } else {
            prefs = Preferences.systemNodeForPackage(c);
        }
        
        prefs.put(key, code);
    }
    
    public void delete(Class c, String key) throws SecurityException {
        Preferences prefs = null;
        if (userMode) {
            prefs = Preferences.userNodeForPackage(c);
        } else {
            prefs = Preferences.systemNodeForPackage(c);
        }
        
        prefs.remove(key);
    }
    
    public String read(Class c, String key) {
        Preferences prefs = null;
        if (userMode) {
            prefs = Preferences.userNodeForPackage(c);
        } else {
            prefs = Preferences.systemNodeForPackage(c);
        }
        
        return prefs.get(key, null);
    }
    
    public boolean decryptLicense(String myLicense) 
        throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, 
                    NoSuchPaddingException, IllegalStateException,
                    IllegalBlockSizeException, BadPaddingException {

        String myDecrypt;
        LicenseCodeDecoder myLicenseCode = new LicenseCodeDecoder("DES");
        myLicenseCode.setLicenseStr(myLicense);

        myDecrypt = myLicenseCode.decrypt();

        if (myDecrypt == null) {
            return false;
        }

        return true;
    }
    
    // ------------------------private Inner LicenseCodeDecoder class -------------------------//
    private final class LicenseCodeDecoder {
        String itsMDStr, itsSeedCode;

        String itsParam, itsEncrypt, itsDecrypt;

        SecretKey itsSecretKey = null;

        String itsKey, itsHeader, itsSerial, itsHash;

        boolean itsDESMode = false;

        boolean itsDESedeMode = false;

        private LicenseCodeDecoder(String a) {
            if ("DESede".equals("DESede")) {
                itsDESMode = true;
            }
        }

        /**
         * @return String    : encorded string
         **/
        private String encodeMD5(String key) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(key.getBytes());
                return convByte2HexStr(md.digest());
            } catch (NoSuchAlgorithmException ne) {
                System.err.println(ne);
            }
            return null;
        }

        /*
         * convert byte data to Hex Strings
         *
         *  @param b an array of encrypted byte data
         *  @return String : hex strings
        **/
       private final String convByte2HexStr(byte[] b) {
           StringBuffer buf = new StringBuffer();

           for (int i = 0; i < b.length; i++) {
               buf.append(convHex2Ascii((b[i] >>> 4) & 0x0F));
               buf.append(convHex2Ascii(b[i] & 0x0F));
           }
           return buf.toString();
       }

        private final byte[] convHexStr2Byte(String hex) {
            int myLength = (hex.length() + 1) >> 1;
            byte[] myByte = new byte[myLength];

            for (int i = 0; i < myLength; i++) {
                String myParse;
                myParse = hex.substring(i * 2, i * 2 + 2);
                myByte[i] = (byte) Integer.parseInt(
                        hex.substring(i * 2, i * 2 + 2), 16);
            }
            return (myByte);
        }

        /**
         * convert to hex string to ascii
         *
         * @param h
         * @return char : character [0-9,A-F]
         **/
        private final char convHex2Ascii(int h) {
            if ((h >= 10) && (h <= 15))
                return (char) ('A' + (h - 10));
            if ((h >= 0) && (h <= 9))
                return (char) ('0' + h);
            throw new Error("hex to ascii failed");
        }
        
        private void setLicenseStr(String aLicenseStr) throws InvalidKeyException {
            int myPtr = aLicenseStr.indexOf("-");
            if (myPtr >= 0) {
                String myHeader, myHash, mySerial, myCipher, myTmp;
                myHeader = aLicenseStr.substring(0, myPtr);
                myTmp = aLicenseStr.substring(myPtr + 1);
                myPtr = myTmp.indexOf("-");
                if (myPtr >= 0) {
                    myHash = myTmp.substring(0, myPtr);
                    myTmp = myTmp.substring(myPtr + 1);
                    myPtr = myTmp.indexOf("-");
                    if (myPtr >= 0) {
                        myCipher = myTmp.substring(0, myPtr);
                        mySerial = myTmp.substring(myPtr + 1);
                        itsKey = myHeader + mySerial + myHash;
                        
                        if (myCipher.length() != 16) {
                            throw new InvalidKeyException("Hash is invalid.");
                        }
                        itsEncrypt = myCipher;
                        //System.out.println("Set Key     :" + itsKey);
                        //System.out.println("Set Encrypt :" + itsEncrypt);

                        itsHeader = myHeader;
                        itsHash = myHash;
                        itsSerial = mySerial;
                    }
                }
            }
        }

        private String decrypt() throws InvalidKeyException, InvalidKeySpecException, 
                                                        NoSuchAlgorithmException, NoSuchPaddingException, 
                                                        IllegalStateException, IllegalBlockSizeException, BadPaddingException {

            String myKey;

            if (isValidLicense() == false) {
                return null;
            }
            if (itsKey == null) {
                return null;
            }
            if (itsKey.length() > 8) {
                myKey = itsKey.substring(itsKey.length() - 8);
                //System.out.println("Key :" + itsKey);
                //System.out.println("Sub Key :" + myKey);
            } else {
                myKey = itsKey;
            }

            itsDecrypt = doDecryptDES(myKey, itsEncrypt);

            return (itsDecrypt);
        }

        /**
         * @return boolean . The license code is then return 'true' , invalid is 'false'.
         **/
        private boolean isValidLicense() {
            String myMDCodeString, myMDHash, myMD5Hash;

            myMDCodeString = itsHeader + itsSerial;
            myMD5Hash = encodeMD5(myMDCodeString);
            myMD5Hash = myMD5Hash.substring(0, 6);
            //System.out.println("isValidLicense(): MDCodeString:" + myMDCodeString);
            //System.out.println("isValidLicense(): MD5(6bytes):" + myMD5Hash);
            //System.out.println("isValidLicense(): [" + myMD5Hash +"][" + itsHash +"]");
            if (myMD5Hash.equals(itsHash)) {
                return (true);
            }
            return (false);
        }

        private String doDecryptDES(String key, String cipherText) 
                throws InvalidKeyException, InvalidKeySpecException, 
                                NoSuchAlgorithmException, NoSuchPaddingException, 
                                IllegalStateException, IllegalBlockSizeException, BadPaddingException {

            Provider sunJce = new com.sun.crypto.provider.SunJCE();
            Security.addProvider(sunJce);
            SecretKeyFactory keyFac = SecretKeyFactory.getInstance("DES");
            DESKeySpec keySpec = new DESKeySpec(key.getBytes());
            SecretKey secKey = keyFac.generateSecret(keySpec);

            Cipher c = Cipher.getInstance("DES");
            c.init(Cipher.DECRYPT_MODE, secKey);
            byte[] b = convHexStr2Byte(cipherText);
            return new String(c.doFinal(b));
        }
        
        /*
        public  String doDecryptDESede(SecretKey seckey,String cipherText) throws Exception{
        Provider sunJce = new com.sun.crypto.provider.SunJCE();
        Security.addProvider(sunJce);
        Cipher c = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        byte[] myCipher  = null;
        byte[] b=null;
        try {
        b = convHexStr2Byte(cipherText);
        int myIVLen = b[0] << 24 | b[1] << 16 | b[2] << 8 | b[3];
        byte[] myIV    = copy(b, 4,  myIVLen );
        myCipher       = copy(b, 4+myIVLen ); 
        IvParameterSpec ivp = new IvParameterSpec(myIV);

        //String myString = convByte2HexStr( b );
        //System.out.println("Encrypted->Decrypted:" + myString);
        c.init(Cipher.DECRYPT_MODE,seckey, ivp);
        }catch(Exception ne){
        System.out.println("Exception at doDecryptDESede:" + ne);
        ne.printStackTrace();
        }
        System.out.println("Encrypted start with:" + myCipher[0]);
        return new String(c.doFinal(myCipher));
        }
        */
       

    } // End of LicenseCodeDecoder class

}
