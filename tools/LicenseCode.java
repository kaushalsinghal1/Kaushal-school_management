
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

public  class LicenseCode {
    String    itsMDStr,itsSeedCode;
    String    itsParam,itsEncrypt,itsDecrypt;
    SecretKey itsSecretKey=null;
    String    itsKey,itsHeader,itsSerial,itsHash;
    boolean   itsDESMode    = false;
    boolean   itsDESedeMode = false;

    public LicenseCode(String a){
        if( "DESede".equals("DESede") ){
            itsDESMode    = true;
        }
    }
    /**
    * @param aSecretKey : SecretKey
    **/
    void setSecretKey(SecretKey aSecretKey){
        itsSecretKey = aSecretKey;
    }
    /**
    * @param aSecretKey : SecretKey
    **/
    void setSecretKey(String aHeader,String aHash,String aSerial){
        itsHeader = aHeader;
        itsHash   = aHash;
        itsSerial = aSerial;
        itsKey    = aHeader + aSerial + aHash;
    }
    /**
    * @return SecretKey : Secret Key 
    **/
    SecretKey getSecretKey(){
        return(itsSecretKey);
    }
    /**
    * @return String : Secret Key 
    **/
    String getSecretKeyString(){
        return(itsKey);
    }
    /**
    * @param aSecretKey : SecretKey
    * @return SecretKey : SecretKey 
    **/
    void setMDCode(String aSeedCode,String aMDStr){
        itsSeedCode = aSeedCode;
        itsMDStr    = aMDStr;
    }
    /**
    * @return MD5 data
    **
    String getMDStr(){
        return(itsMDStr);
    }
    /**
    * @return Seed String 
    **
    String getSeedCode(){
        return(itsSeedCode);
    }
    /**
    * @return Encrypted Param 
    **
    String getEncryptedParam(){
        return(itsEncrypt);
    }
    /**
    * @param aVersion Version Number of the product
    * @param aFlag    Flags, You may use this for any pourpose.
    **
    public void setParam(String aVersion,String aFlag){
        itsParam = new String(aVersion) + "-" + new String(aFlag);
    }
    /**
    * @param aParam Version Number of the product and flags
    **/
    public void setParam(String aParam){
        itsParam = new String(aParam);
    }
    /**
    * @return Returns the Version Number of the product and flags
    **/
    public String getParam(){
        return(itsParam);
    }
    /**
    * @return Returns the License String.
    **/
    public String getLicenseStr(){
        return(itsHeader+"-"+itsHash+"-"+itsEncrypt+"-"+itsSerial);
    }
    /**
    * encoding strings 
    * @param key String
    * @return Return the encorded string
    **/
    public  String encodeMD5(String key){

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(key.getBytes());
            return LicenseCode.convByte2HexStr(md.digest());
        }catch(NoSuchAlgorithmException ne){
            System.err.println(ne);
        }
        return null;
    }
    /**
    * @Param String : NV1107PR077F41-A4B0532D040BE8E5 (OLD)
    * @Param String : NV11-077F41-A4B0532D040BE8E5-07PR (NEW)
    **/
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
                    System.out.println("Set Key     :" + itsKey);
                    System.out.println("Set Encrypt :" + itsEncrypt);

                    itsHeader = myHeader;
                    itsHash   = myHash;
                    itsSerial = mySerial;
                }
            }
        }
    }
    /**
    * @return boolean :  Valid=true , Invalid=false
    **/
    public boolean isValidLicense(){ 
        String myMDCodeString,myMDHash,myMD5Hash;

        myMDCodeString  = itsHeader + itsSerial;
        //System.out.println("isValidLicense(): MDCodeString:" + myMDCodeString);
        myMD5Hash       = encodeMD5(myMDCodeString);
        myMD5Hash       = myMD5Hash.substring(0,6);
        //System.out.println("isValidLicense(): MD5(6bytes):" + myMD5Hash);
        //System.out.println("isValidLicense(): [" + myMD5Hash +"][" + itsHash +"]");
        if( myMD5Hash.equals(itsHash) ){
            return(true);
        }
        return(false);
    }
    /**
    * @return boolean :  NoError=true , Error=false
    **/
    public boolean crypt(){
        String myKey;
        boolean myResult = true;

        if( itsKey.length() > 8 ){
            myKey     = itsKey.substring(itsKey.length()-8);
            System.out.println("To encipher, 8 Bytes [" + myKey + "] are used from[" + itsKey +"]");
        }else{
            myKey     = itsKey;
        }
        try{
            itsEncrypt = doEncryptDES(myKey , itsParam );
            System.out.println("Encrypted:" + itsEncrypt);
        }catch(Exception ne){
            myResult = false;
            System.out.println("Error handling");
            System.out.println("Exception at crypt():" + ne);
        }
        return(myResult);
    }
    /**
    * @return String : If the license code is invalid, result is null
    **/
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
            //System.out.println("Key :" + myKey);
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
    * convert byte data to Hex Strings
    *
    * @param b an array of encrypted byte data
    * @return String : hex strings
    **/
    public  static final String convByte2HexStr( byte[] b ) {
      StringBuffer buf = new StringBuffer();
  
      for(int i=0; i < b.length; i++ ) {
        buf.append(convHex2Ascii((b[i] >>> 4) & 0x0F));
        buf.append(convHex2Ascii(b[i] & 0x0F));
      }
      return buf.toString();
    }
    /**
    * convert to hex string to ascii
    *
    * @param h
    * @return char : character [0-9,A-F]
    **/
    public  static char convHex2Ascii(int h){
      if ((h >= 10) && (h <= 15)) return (char)('A' + (h - 10));
      if ((h >= 0) && (h <= 9)) return (char)('0' + h);
      throw new Error("hex to ascii failed");
    }
    /**
    * convert to hex string to Byte
    **/
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
    /** 
    * @Return Instance of DESede
    **/
    public  SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keygen = KeyGenerator.getInstance("DESede");
        return keygen.generateKey();
    }

    /** 
    * readKey
    * read TripleDES SecretKey from the specified file 
    * @param           : File f
    * @return SecetKey : Scret key
    **/
    public  SecretKey readKey(File f)
            throws InvalidKeyException, InvalidKeySpecException, IOException,
            NoSuchAlgorithmException {
        DataInputStream in = new DataInputStream(new FileInputStream(f));
        byte[] rawkey = new byte[(int)f.length()];
        in.readFully(rawkey);
        in.close();

        DESedeKeySpec keyspec = new DESedeKeySpec(rawkey);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
        SecretKey key = keyfactory.generateSecret(keyspec);
        return key;
    }
    public  byte[] copy(byte[] aByte,int aPosition){
        byte[] myNew = new byte[aByte.length - aPosition];
        try{
            for(int i=0; i < aByte.length-aPosition; i++){
                myNew[i] = aByte[i+aPosition];
            }
        }catch(Exception ne){
            System.out.println("Exception:" + ne);
            ne.printStackTrace();
        }
        return myNew;
    }
    public  byte[] copy(byte[] aByte,int aPosition,int aLength ){
        byte[] myNew = new byte[aLength];
        try{
            for(int i=0; i < aLength; i++){
                myNew[i] = aByte[i+aPosition];
            }
        }catch(Exception ne){
            System.out.println("Exception:" + ne);
            ne.printStackTrace();
        }
        return myNew;
    }
    /** 
    * write TripleDES SecretKey to the specified file 
    **/
    public  void writeKey(SecretKey key, File f)
            throws InvalidKeySpecException, IOException,
            NoSuchAlgorithmException {
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
        DESedeKeySpec keyspec =
            (DESedeKeySpec)keyfactory.getKeySpec(key, DESedeKeySpec.class);
        byte[] rawkey = keyspec.getKey();

        // write the raw key to the file
        FileOutputStream out = new FileOutputStream(f);
        out.write(rawkey);
        out.close();
    }
    /** 
    * Encrypt the text using DESede.
    **/
    public  String doEncryptDESede(SecretKey seckey,String plainText) throws Exception{

        Cipher      cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE,seckey);

        byte[]          iv = cipher.getIV(); // get the initialization vector
        String myStringIV  = convByte2HexStr( iv );
        byte[]           b = cipher.doFinal(plainText.getBytes());
        byte[]    myIVLen  = new byte[4];
        myIVLen[0]    = (byte)(iv.length >> 24);
        myIVLen[1]    = (byte)((iv.length >> 16) & 0xff);
        myIVLen[2]    = (byte)((iv.length >>  8) & 0xff);
        myIVLen[3]    = (byte)((iv.length      ) & 0xff);

        String    myLength = convByte2HexStr(myIVLen);
        String myEncrypted = convByte2HexStr( b );

        System.out.println("Encrypted start with:" + b[0]);

        return( myLength + myStringIV + myEncrypted);
    }
    /** 
    * Decrypt the text
    **/
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

    /** 
    * Encrypt the text using DES.
    **/
    public  String doEncryptDES(String key,String plainText) throws Exception{
        Provider sunJce = new com.sun.crypto.provider.SunJCE();
        Security.addProvider(sunJce);

        SecretKeyFactory keyFac = SecretKeyFactory.getInstance("DES");
        DESKeySpec      keySpec = new DESKeySpec(key.getBytes());
        SecretKey        secKey = keyFac.generateSecret(keySpec);

        Cipher                c = Cipher.getInstance("DES");
        c.init(Cipher.ENCRYPT_MODE,secKey);
        byte[]                b = c.doFinal(plainText.getBytes());

        return( convByte2HexStr(b) );
    }

    /** 
    * Decrypt the text using DES.
    **/
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

