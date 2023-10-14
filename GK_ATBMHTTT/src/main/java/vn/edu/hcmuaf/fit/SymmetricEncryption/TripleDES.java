package vn.edu.hcmuaf.fit.SymmetricEncryption;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.io.*;
import java.util.Base64;

public class TripleDES {

    private static TripleDES instance;
    private static String name = "DESede/ECB/PKCS5Padding";

    private static String keyName = "DESede";
    private static int bits = 168;
    private SecretKey secretKey ;
    private static SymmetricConverter converter = SymmetricConverter.getInstance();


    public TripleDES(){
        this.secretKey = converter.generateSecretKey(null,keyName,168);
    }

    public String getKeyAlgoName(){
        return keyName;
    }
    public SecretKey exportSecretKey(){
        return this.secretKey;
    }
    public String encrypt(String inputString){
        SecretKey secretKey = exportSecretKey();
        if (secretKey != null){
            try {
                Cipher cipher = Cipher.getInstance(name);

                // Dữ liệu cần mã hóa
                byte[] dataToEncrypt = inputString.getBytes();

                // Mã hóa dữ liệu
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                byte[] encryptedData = cipher.doFinal(dataToEncrypt);
                String encryptedStringData = Base64.getEncoder().encodeToString(encryptedData);
                // In ra dữ liệu đã mã hóa
                System.out.println("Regular data: "+inputString);
                System.out.println("Encrypted data: " + encryptedStringData);
                return encryptedStringData;
            }catch (Exception e){
                System.out.println("Lỗi khởi tạo key!!");
                e.printStackTrace();
            }

        }
        return null;
    }

    public String decrypt(String inputString){
//        if ((keyString != null || keyString != "")){
            SecretKey secretKey = exportSecretKey();
            try {
                Cipher cipher = Cipher.getInstance(name);
                // Dữ liệu cần giải mã
                byte[] receivedData = Base64.getDecoder().decode(inputString);

                // Giải mã dữ liệu

                cipher.init(Cipher.DECRYPT_MODE, secretKey);
                byte[] decryptedData = cipher.doFinal(receivedData);

                String decryptedStringData = new String(decryptedData);
                // In ra dữ liệu
                System.out.println("Encrypted data: "+inputString);
                System.out.println("Decrypted data: " + decryptedStringData);
                return decryptedStringData;
            }catch (Exception e){
                System.out.println("Key giải mã bạn nhập vào khác với key đã mã hóa dữ liệu !! Vui lòng nhập đúng key.");
                e.printStackTrace();
            }

//        }else {
//            System.out.println("Bạn phải có key để giải mã!!");
//        }
        return null;
    }

    public void encryptFile(String sourceFile,String destFile) throws Exception{
        SecretKey secretKey = exportSecretKey();
        if(secretKey==null) throw new FileNotFoundException("Key not found");
        File file=new File(sourceFile);
        if(file.isFile()){
            Cipher cipher=Cipher.getInstance(name);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            FileInputStream fis= new FileInputStream(file);
            FileOutputStream fos=new FileOutputStream(destFile);

            byte[] input=new byte[64];
            int byteRead = 0;
            while((byteRead= fis.read(input))!=-1){
                byte[] output=cipher.update(input,0,byteRead);
                if(output!=null){
                    fos.write(output);
                }
            }
            byte[] output=cipher.doFinal();
            if(output!=null){
                fos.write(output);
            }
            fis.close();
            fos.flush();
            fis.close();
            System.out.println("Encrypted");

        }else{
            System.out.println("This is not a file");
        }
    }
    public void decryptFile(String sourceFile,String destFile) throws  Exception{
        SecretKey secretKey = exportSecretKey();
        if(secretKey==null) throw new FileNotFoundException("Key not found");
        File file=new File(sourceFile);
        if(file.isFile()){
            Cipher cipher=Cipher.getInstance(name);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            FileInputStream fis= new FileInputStream(file);
            FileOutputStream fos=new FileOutputStream(destFile);

            byte[] input=new byte[64];
            int readByte = 0;
            while((readByte= fis.read(input))!=-1){
                byte[] output=cipher.update(input,0,readByte);
                if(output!=null){
                    fos.write(output);
                }
            }
            byte[] output=cipher.doFinal();
            if(output!=null){
                fos.write(output);
            }
            fis.close();
            fos.flush();
            fis.close();
            System.out.println("Decrypted");

        }else{
            System.out.println("This is not a file");        }
    }

    public static void main(String[] args) throws Exception {

          TripleDES tdes = new TripleDES();
//          String ens = tdes.encrypt("Hello everybody!!");
//          tdes.decrypt(ens);

        tdes.encryptFile("en.zip","en2.zip");
        tdes.decryptFile("en2.zip","en3.zip");
    }

}
