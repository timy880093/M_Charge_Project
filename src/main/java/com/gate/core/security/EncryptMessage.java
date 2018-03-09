package com.gate.core.security;

/**
 * Created by simon on 2014/7/15.
 */

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;

/**
 * @author Giulio
 */
public class EncryptMessage {


    private static final String SECRET_KEY_SPEC = "DES";

    private Key autoGenOrLoadKey(String keyPath) throws NoSuchAlgorithmException, IOException, org.apache.commons.codec.DecoderException {
        boolean isExist = false;
        Key key = null;
        File checkFile = new File(keyPath);
        if (!checkFile.exists()) {//如果檔案不存在
            KeyGenerator generator;
            generator = KeyGenerator.getInstance(SECRET_KEY_SPEC);
            generator.init(new SecureRandom());
            key = generator.generateKey();
            saveKey(key, new File(keyPath));
        } else {//檔案存在時
            String data = new String(FileUtils.readFileToString(checkFile));
            byte[] encoded = null;
            encoded = Hex.decodeHex(data.toCharArray());
            key = new SecretKeySpec(encoded, SECRET_KEY_SPEC);
        }
        return key;
    }

    public void saveKey(Key key, File file) throws IOException {
        char[] hex = Hex.encodeHex(key.getEncoded());
        FileUtils.writeStringToFile(file, String.valueOf(hex));
    }

    public Key loadKey(File file) throws IOException, org.apache.commons.codec.DecoderException {
        String data = new String(FileUtils.readFileToString(file));
        byte[] encoded = null;
        encoded = Hex.decodeHex(data.toCharArray());
        return new SecretKeySpec(encoded, SECRET_KEY_SPEC);
    }


    public String encrypt(String message, Key key) throws IllegalBlockSizeException,
            BadPaddingException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException,
            UnsupportedEncodingException {
        // Get a cipher object.
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        // Gets the raw bytes to encrypt, UTF8 is needed for
        // having a standard character set
        byte[] stringBytes = message.getBytes("UTF8");

        // encrypt using the cypher
        byte[] raw = cipher.doFinal(stringBytes);

        // converts to base64 for easier display.
        String base64 = Base64.getEncoder().encodeToString(raw);

        return base64;
    }

    public String decrypt(String encrypted, Key key) throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException, IOException {

        // Get a cipher object.
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);

        //decode the BASE64 coded message
        byte[] raw = Base64.getDecoder().decode(encrypted);

        //decode the message
        byte[] stringBytes = cipher.doFinal(raw);

        //converts the decoded message to a String
        String clear = new String(stringBytes, "UTF8");
        return clear;
    }


    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            //產生
            EncryptMessage encryptMessage = new EncryptMessage();
            Key key= encryptMessage.autoGenOrLoadKey("d:/Temp/001.key");
            String encrypt = encryptMessage.encrypt("我要測試連線abcde13323",key);
            System.out.println("encrypt = " + encrypt );
            String decrypt= encryptMessage.decrypt(encrypt, key);
            System.out.println("decrypt = "+ decrypt);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
