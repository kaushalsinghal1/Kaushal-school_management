


// MD5
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// DES
import javax.crypto.*;
import javax.crypto.spec.*;
import sun.misc.*;
import java.security.*;
import com.sun.crypto.provider.*;
import java.lang.Exception;

// Array
import java.util.List;
import java.util.ArrayList;

// File I/O
import java.security.spec.*;
import java.io.*;


public class LicenseChecker{

    LicenseChecker(){       
    }
    /**
    *
    **/
    public void decryptLicense(String myLicense){
        String      myDecrypt;
        LicenseCodeDecoder myLicenseCode = new LicenseCodeDecoder("DES");
        myLicenseCode.setLicenseStr(myLicense);
        myDecrypt   = myLicenseCode.decrypt();

        if( myDecrypt == null ){
               System.out.println("The license code is invalid");
        }else{
           System.out.println("The license code is valid");
           System.out.println("Decrypted Cipher:" + myDecrypt);
        }
    }
    /**
    * java LicenseCode NSV11-57A3A8-43B4A99402C292C9-05
    **/
    public static void main(String args[]){
        if( args.length >= 1 ){
            String      myDecrypt,myCipher,myLicense,myKey;
            myLicense = args[0];

            System.out.println("License Code:" + myLicense);
            LicenseChecker myLicenseChecker = new LicenseChecker();
            myLicenseChecker.decryptLicense(myLicense);

            //myDecrypt   = myLicenseCode.decrypt();
        }else{
            System.out.println("Usage : ");
            System.out.println("LicenseChecker <LicenseCode>");
        }
    }
    public  class LicenseCodeDecoder {
        String itsMDStr,itsSeedCode;
        String itsParam,itsEncrypt,itsDecrypt;
        SecretKey itsSecretKey=null;
        String    itsKey,itsHeader,itsSerial,itsHash;
        boolean   itsDESMode    = false;
        boolean   itsDESedeMode = false;

        public LicenseCodeDecoder(String a){
            if( "DESede".equals("DESede") ){
                itsDESMode    = true;
            }
        }
        public void setLicenseStr(String aLicenseStr){
            int myPtr = aLicenseStr.indexOf("-");
            if( myPtr >= 0 ){
                String myHeader,myHash,mySerial,myCipher,myTmp; 
                myHeader   = aLicenseStr.substring(0,myPtr);
                myTmp      = aLicenseStr.substring(myPtr+1);
                myPtr = myTmp.indexOf("-");
                if( myPtr >= 0 ){
                    myHash     = myTmp.substring(0,myPtr);
                    myTmp      = myTmp.substring(myPtr+1);
                    myPtr = myTmp.indexOf("-");
                    if( myPtr >= 0 ){
                        myCipher  = myTmp.substring(0,myPtr);
                        mySerial  = myTmp.substring(myPtr+1);
                        itsKey     = myHeader+mySerial+myHash;
                        itsEncrypt = myCipher;
                        //System.out.println("Set Key     :" + itsKey);
                        //System.out.println("Set Encrypt :" + itsEncrypt);

                        itsHeader = myHeader;
                        itsHash   = myHash;
                        itsSerial = mySerial;
                    }
                }
            }
        }
        public String decrypt(){
            String myKey;

            if( isValidLicense() == false ){
                return null;
            }
            if( itsKey == null ){
                return null;
            }       
            if( itsKey.length() > 8 ){
                myKey     = itsKey.substring(itsKey.length()-8);
                //System.out.println("Key :" + itsKey);
                //System.out.println("Sub Key :" + myKey);
            }else{
                myKey     = itsKey;
            }
            try{
                itsDecrypt = doDecryptDES(myKey , itsEncrypt );
            }catch(Exception ne){
                itsDecrypt = null;
                System.out.println("Error handling");
                System.out.println("Exception at decrypt():" + ne);
            }
            return(itsDecrypt);
        }
        /**
        * @return boolean . The license code is then return 'true' , invalid is 'false'.
        **/
        public boolean isValidLicense(){ 
            String myMDCodeString,myMDHash,myMD5Hash;

            myMDCodeString  = itsHeader + itsSerial;
            myMD5Hash       = encodeMD5(myMDCodeString);
            myMD5Hash       = myMD5Hash.substring(0,6);
            //System.out.println("isValidLicense(): MDCodeString:" + myMDCodeString);
            //System.out.println("isValidLicense(): MD5(6bytes):" + myMD5Hash);
            //System.out.println("isValidLicense(): [" + myMD5Hash +"][" + itsHash +"]");
            if( myMD5Hash.equals(itsHash) ){
                return(true);
            }
            return(false);
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
        public  String doDecryptDES(String key,String cipherText) throws Exception{

            Provider sunJce = new com.sun.crypto.provider.SunJCE();
            Security.addProvider(sunJce);
            SecretKeyFactory keyFac = SecretKeyFactory.getInstance("DES");
            DESKeySpec      keySpec = new DESKeySpec(key.getBytes());
            SecretKey         secKey = keyFac.generateSecret(keySpec);

            Cipher                c = Cipher.getInstance("DES");
            c.init(Cipher.DECRYPT_MODE,secKey);
            byte[] b = convHexStr2Byte(cipherText);
            return new String(c.doFinal(b));
        }
    }
    /**
    * convert to hex string to ascii
    *
    * @param h
    * @return char : character [0-9,A-F]
    **/
    public final char convHex2Ascii(int h){
      if ((h >= 10) && (h <= 15)) return (char)('A' + (h - 10));
      if ((h >= 0) && (h <= 9)) return (char)('0' + h);
      throw new Error("hex to ascii failed");
    }
    /*

    /**
    * convert byte data to Hex Strings
    *
    * @param b an array of encrypted byte data
    * @return String : hex strings
    **/
    public final String convByte2HexStr( byte[] b ) {
      StringBuffer buf = new StringBuffer();

      for(int i=0; i < b.length; i++ ) {
        buf.append(convHex2Ascii((b[i] >>> 4) & 0x0F));
        buf.append(convHex2Ascii(b[i] & 0x0F));
      }
      return buf.toString();
    }

    /**
    * @return String    : encorded string
    **/
    public  String encodeMD5(String key){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(key.getBytes());
            return convByte2HexStr(md.digest());
        }catch(NoSuchAlgorithmException ne){
            System.err.println(ne);
        }
        return null;
    }
    public  final byte[] convHexStr2Byte( String hex ) {
        int myLength = (hex.length()+1)>>1;
        byte[] myByte = new byte[myLength];

        for(int i=0; i < myLength; i++ ) {
            String myParse;
            myParse = hex.substring(i*2,i*2+2);
            myByte[i] = (byte)Integer.parseInt(hex.substring(i*2,i*2+2),16);
        }
        return(myByte);
    }
}
