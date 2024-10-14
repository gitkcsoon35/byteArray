package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello world!");
        String gubun = "1";

        if (gubun == "1") {
//            byteArray("C:\\Users\\P21911001\\Downloads\\licenseInsert_111.eci");
//            byteArray("C:\\Users\\P21911001\\Downloads\\licenseUpdate_111.eci");
            byteArray("C:\\Users\\P21911001\\Downloads\\licenseInsert_55.eci");
            byteArray("C:\\Users\\P21911001\\Downloads\\licenseUpdate_55.eci");
//            byteArray("C:\\Users\\P21911001\\Downloads\\licenseUpdate2_55.eci");
        } else if (gubun == "2") {
            contractNumber();
        } else if (gubun == "3") {
//            byteArray();
            contractNumber();
        }

    }

    public static void byteArray(String eciFile) {
        try {
            String requestBody;
            byte[] readContent = Files.readAllBytes(Paths.get(eciFile));
            requestBody = Arrays.toString(readContent);
            System.out.println(requestBody);

        } catch (IOException e) {
            System.out.println("SaveFile =========== none");
            throw new RuntimeException(e);
        }
    }
    public static void contractNumber() throws Exception {
        String CONTRACT_NUMBER_PREFIX = "PENTASECURITY";
        String testDate = "20241009"; // 만료일
        String contractNumber = "T202312007001"; // 라이선스번호

        String hashKey = computeSha256Hash(CONTRACT_NUMBER_PREFIX + contractNumber);
        System.out.println("hashKey : " + hashKey);
        byte checkSum = (byte)createChecksum(testDate);
        StringBuilder sb = new StringBuilder();
        sb.append((char)checkSum);
        for(int i = 0; i < 8; i += 2) {
            BigInteger tmpInt = new BigInteger(testDate.substring(i, i + 2));
            sb.append((char)tmpInt.intValue());
        }
        String mCodeStr = sb.toString();
        String maintenanceCode = exclusiveValue(mCodeStr, hashKey);
        System.out.println("maintenanceCode : " + maintenanceCode);
    }

    static String computeSha256Hash(String rawData) throws Exception{
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new Exception(e);
        }
        byte[] encodeHash = digest.digest(rawData.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for(byte encodeByte : encodeHash) {
            sb.append(String.format("%02X", encodeByte & 0xff));
        }
        return sb.toString().toUpperCase();
    }


    static String exclusiveValue(String binaryCode, String hashKey) throws Exception {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 5; i++) {
            try {
                sb.append(String.format("%02X", hashKey.charAt(i) ^ binaryCode.charAt(i)));
            } catch (IndexOutOfBoundsException e) {
                throw new Exception(e);
            }
        }
        return sb.toString();
    }


    static int createChecksum(String date) {
        int checksum = 0;
        for(int i = 0; i < date.length(); i++) {
            char checkVal = date.charAt(i);
            if(!Character.isDigit(checkVal)){
                return 0;
            }
            checksum += Character.getNumericValue(checkVal);
        }
        return checksum;
    }

}