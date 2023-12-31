package vn.edu.hcmuaf.fit.SymmetricEncryption;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Base64;

public class AES implements Sym{
    private static String name = "AES";
    private static int bits = 128;
    private static SymmetricConverter converter = SymmetricConverter.getInstance();
    private SecretKey secretKey ;


    public AES(){

    }
    @Override
    public String getEncryptionName() {
        return name;
    }

    @Override
    public int getEncryptionBits() {
        return bits;
    }

    @Override
    public boolean checkStringKeyValid(String keyString) {
        SecretKey k = converter.generateSecretKey(keyString,name,bits);
        if (k == null){
            return false;
        }else{
            this.secretKey = k;
            return true;
        }
    }

    @Override
    public void createKey(String keyString){
        this.secretKey = converter.generateSecretKey(keyString,name,bits);
    }


    @Override
    public void createKey(String keyString, String ivString) {

    }
    @Override
    public String exportStringKey() {
        return converter.secretKeyToString(this.secretKey);
    }

    public SecretKey exportSecretKey(){
        return this.secretKey;
    }

    @Override
    public String encrypt(String inputString ){
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
                return null;
            }
        }
        return null;
    }



    @Override
    public String decrypt(String inputString ){
            SecretKey secretKey = exportSecretKey();
        if (secretKey != null) {
            try {
                Cipher cipher = Cipher.getInstance(name);
                // Dữ liệu cần giải mã
                byte[] receivedData = Base64.getDecoder().decode(inputString);
                // Giải mã dữ liệu
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
                byte[] decryptedData = cipher.doFinal(receivedData);
                String decryptedStringData = new String(decryptedData);
                // In ra dữ liệu
                System.out.println("Encrypted data: " + inputString);
                System.out.println("Decrypted data: " + decryptedStringData);
                return decryptedStringData;
            } catch (Exception e) {
                System.out.println("Key giải mã bạn nhập vào khác với key đã mã hóa dữ liệu !! Vui lòng nhập đúng key.");
                e.printStackTrace();
                return null;
            }
        }

        return null;
    }

    @Override
    public boolean encryptFile(String sourceFile, String destFile) throws Exception{
        SecretKey secretKey = exportSecretKey();
        if(secretKey==null) throw new FileNotFoundException("Key not found");
        File file=new File(sourceFile);
        if(file.isFile()){
            Cipher cipher=Cipher.getInstance(name);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            FileInputStream fis= new FileInputStream(file);
            FileOutputStream fos=new FileOutputStream(destFile);

            byte[] input=new byte[1024];
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
            fos.close();
            System.out.println("Encrypted");
            return true;

        }else{
            System.out.println("This is not a file");
            return false;
        }

    }
    @Override
    public boolean decryptFile(String sourceFile, String destFile) throws  Exception{
        SecretKey secretKey = exportSecretKey();
        if(secretKey==null) throw new FileNotFoundException("Key not found");
        File file=new File(sourceFile);
        if(file.isFile()){
            Cipher cipher=Cipher.getInstance(name);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            FileInputStream fis= new FileInputStream(file);
            FileOutputStream fos=new FileOutputStream(destFile);

            byte[] input=new byte[1024];
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
            fos.close();
            System.out.println("Decrypted");

            return true;

        }else{
            System.out.println("This is not a file");
            return false;
        }
    }
    public static void main(String[] args) throws Exception {


    }
}
