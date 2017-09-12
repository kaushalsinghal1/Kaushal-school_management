
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

public  class LicenseList {
    private List itsLicenses = new ArrayList();
    public void addLicense(LicenseCode aLicense){
        itsLicenses.add(aLicense);
    }
    public void dumpLicense(){
       int aSize;
       try{
           for(aSize=0 ; aSize < itsLicenses.size(); aSize++){
               String      myDecrypt,myCipher,myLicense,myKey;
               LicenseCode myLicenseCode = (LicenseCode)itsLicenses.get(aSize);
               myLicense   = myLicenseCode.getLicenseStr();
               System.out.println("License Code:" + myLicense);
               myDecrypt   = myLicenseCode.decrypt();
               System.out.println("Decrypted Cipher:" + myDecrypt);
               //myKey       = myLicenseCode.getSecretKeyString();
               //System.out.println("(Secret Key:" + myKey +")");

               //System.out.println("Encrypt Key:" + mySeedStr + myMDStr +",(" + mySeedStr +"," + myMDStr +")");
               //System.out.println("Plain Data :" + myParam );
               //System.out.println("Encrypted  :" + myEncrypt );
           }
        }catch(Exception ne){
           System.out.println("Exception at dump():" + ne);
        }
    }
}

