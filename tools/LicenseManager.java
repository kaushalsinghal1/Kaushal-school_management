
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


public class LicenseManager{

    LicenseManager(){    
    }
    /**
    * encoding strings  : key
    * @param key String :
    * @return String    : encorded string
    **/
    public  String encodeMD5(String key){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(key.getBytes());
            //md.update(key);
            return LicenseCode.convByte2HexStr(md.digest());
        }catch(NoSuchAlgorithmException ne){
            System.err.println(ne);
        }
        return null;
    } 
    /**
    * getHashWithSeed
    * @param aSeed : Seed for the MD5. To generate the hash, aSeed + Digit is used.
    * @param aBegin: MD5 Hashed string, Uses from aBegin to aEnd. This is usually 0.
    * @param aEnd  : As default, 6 bytes are used.
    * @param aNum  : Number of the license codes. 'aNum' of hashes are generated.
    **/
    public  final  LicenseList generate(String aSeed,int aBegin,int aEnd,int aNum,String aParam){
        LicenseList   myLicenseList;
        String        myStringCode = "00";
        String        myMDCodeString;
        String[]      myMDResult;
        byte[]        myMDCodeBytes;
        String        myStringHexCode,myCounterString;
        byte[]        myCode;
        byte[]        myCounter;
        int           mySeedLength,myDigitLength,myLength,myPointer;
        int           myMatchCounter;
        int           myGenCounter,myGenNumber;

        myLicenseList  = new LicenseList();
        myMDResult     = new String[aNum];
        myMatchCounter = 0;
        myGenNumber    = aNum;
        myGenCounter   = 0;
        mySeedLength   = aSeed.length();
        myDigitLength  = (new String()).valueOf(aNum-1).length() ; // 100 -> 3
        myLength       = mySeedLength + myDigitLength;
        myPointer      = myDigitLength-1;
        myCounter      = new byte[myDigitLength];

        System.out.println("Seed     is " + aSeed );
        System.out.println("Num      is " + aNum );
        System.out.println("Length   is " + myLength );
        System.out.println("Begin    is " + aBegin );
        System.out.println("End      is " + aEnd );
        //System.out.println("DigitLen is " + myDigitLength );

        for(int i=0; i < myDigitLength ; i++ ) {
            myCounter[i] = '0';
        }

        /**
        * aSeed + Digit is used for the seed.
        * aSeed + Digit     -> HASH
        * NSVI0( =HEX STR: 4E53564930) -> 58B9B535BB545B0BAC2242EA9E5EB0B5
        **/
        //System.out.println("Pointer is " + myPointer );
        for(int loopCount=0; loopCount < myLength ; loopCount++ ) {
            int           position,countup;
            for(position=myPointer; position >= 0 ; ) {
                for(int i=0; i <= 35 ; i++ ) {
                    String myKey,myMD5Hash;
                    LicenseCode myLicenseCode;
                    if ((i >= 10) && (i <= 35)) 
                       myCounter[myPointer] =  (byte)('A' + (i - 10));
                    if ((i >= 0) && (i <= 9)) 
                       myCounter[myPointer] =  (byte)('0' + i);

                    myLicenseCode   = new LicenseCode("DES");
                    myCounterString = new String(myCounter);
                    myMDCodeString    = aSeed + myCounterString;
                    //System.out.println("Encode data is :" + myMDCodeString);
                    myMD5Hash       = encodeMD5(myMDCodeString);
                    myMD5Hash       = myMD5Hash.substring(aBegin,aEnd);
                    //System.out.println("MD5 :" + myMD5Hash);
                    myKey         = new String(myCounter) + myMD5Hash;
                    //System.out.println("Key :" + myKey);
                    /**
                    * myKey is "NSVI" + <Number> + <MD5Hash(6bytes)>
                    **/
                    myLicenseCode.setSecretKey(aSeed,myMD5Hash,myCounterString);
                    //System.out.println("Encrypt key is :" + myKey);

                    /**
                    * myLicenseCode.setParam("11-F-02A1F");
                    **/
                    myLicenseCode.setParam(aParam);
                    myLicenseCode.crypt();
                    myLicenseList.addLicense(myLicenseCode);

                    myGenCounter++;

                    if( myGenCounter >= myGenNumber ){
                        //return(myMDResult);
                        return myLicenseList;
                    }
                }
                //System.out.println("Position is " + position );
                for(countup = position-1; countup >= 0 ; countup-- ) {
                    int myData;
                    if ( myCounter[countup] == 'Z' ){
                        myCounter[countup] = '0';
                        continue;
                    }else{
                        myData = myCounter[countup];
                        if ( myData < '9' ) 
                           myCounter[countup] += 1;
                        if ( myData >= 'A' ) 
                           myCounter[countup] += 1;
                        if ( myData == '9' ) 
                           myCounter[countup] =  'A';
                        break;
                    }
                }
                if(countup <= -1 ){
                    System.out.println("Match Count : " + myMatchCounter);
                    //return(myMDResult);
                    return myLicenseList;
                }
            }
        }
        return myLicenseList;
    }
    /**
    *
    **/
    public static void main(String args[]){
        String[] myMDList;
        LicenseList myLicenseList;

        if( args.length >= 1 ){
            int myGenerateNumber;
            LicenseManager myLicenseManager = new LicenseManager();
            myGenerateNumber = Integer.parseInt(args[0]);
            myLicenseList = myLicenseManager.generate(args[1],0,6,myGenerateNumber,args[2]);
            myLicenseList.dumpLicense();
        }else{
            System.out.println("Usage : ");
            System.out.println("LicenseManager <NUMBER OF CODES> <HEADER> <DATA>");
            System.out.println("<NUMBER OF CODES>: INTEGER");
            System.out.println("<HEADER>:          Variable String");
            System.out.println("<DATA>:            Variable String");
            System.out.println("% java LicenseManager 10 NSV11 000F");
         }
    }
}

