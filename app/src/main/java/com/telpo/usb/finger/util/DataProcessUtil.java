package com.telpo.usb.finger.util;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class DataProcessUtil {

    private static String hexStr =  "0123456789ABCDEF";
    private static String[] binaryArray =
            {"0000","0001","0010","0011",
                    "0100","0101","0110","0111",
                    "1000","1001","1010","1011",
                    "1100","1101","1110","1111"};

    public static byte[] intToByteArray(int i){
        byte[] result = new byte[4];
        result[0] = (byte)((i >> 24) & 0xFF);
        result[1] = (byte)((i >> 16) & 0xFF);
        result[2] = (byte)((i >> 8) & 0xFF);
        result[3] = (byte)(i & 0xFF);
        return result;
    }




    public static int byte2Int(byte[] b) {
        int intValue = 0;
//        int tempValue = 0xFF;
        for (int i = 0; i < b.length; i++) {
            intValue += (b[i] & 0xFF) << (8 * (3 - i));
        }
        return intValue;
    }

    /**
	 * 整型转字节数�?
	 * 
	 * @param intValue
	 * @return
	 */
    public static byte[] int2Byte(int intValue,int arrayLen) {
        byte[] b = new byte[arrayLen];
        for (int i = 0; i < arrayLen; i++) {
            b[i] = (byte) (intValue >> 8 * (arrayLen-1 - i) & 0xFF);
        }
        return b;
    }



    public static String getHexString(byte[] b, int len) throws Exception {
        String result = "";
        for (int i = 0; i < len; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    public static byte[] getByteArray(String src){
        byte[] res = new byte[src.length()/2];
        char[] chs = src.toCharArray();
        int[] b = new int[2];

        for(int i=0,c=0; i<chs.length; i+=2,c++){
            for(int j=0; j<2; j++){
                if(chs[i+j]>='0' && chs[i+j]<='9'){
                    b[j] = (chs[i+j]-'0');
                }else if(chs[i+j]>='A' && chs[i+j]<='F'){
                    b[j] = (chs[i+j]-'A'+10);
                }else if(chs[i+j]>='a' && chs[i+j]<='f'){
                    b[j] = (chs[i+j]-'a'+10);
                }
            }

            b[0] = (b[0]&0x0f)<<4;
            b[1] = (b[1]&0x0f);
            res[c] = (byte) (b[0] | b[1]);
        }

        return res;
    }

//    /**
	// * 字符串转16进制字节数组
//     *
//     * @param hexString
//     * @return
//     */
//    public static byte[] getByteArray(String hexString) {
//        if(hexString==null)
//            return null;
//        int length=hexString.length()/2;
//        if(hexString.length()%2!=0)
//        {
//            length+=1;
//        }
//        byte[] result=new byte[length];
//        byte[] temp= new BigInteger(hexString, 16).toByteArray();
//        System.arraycopy(temp,0,result,length-temp.length,temp.length);
//        return result;
//    }


    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }


    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    public static final Object bytesToObject(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ObjectInputStream oi = new ObjectInputStream(in);
        Object o = oi.readObject();
        oi.close();
        return o;
    }

    public static final byte[] objectToBytes(Object s) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream ot = new ObjectOutputStream(out);
        ot.writeObject(s);
        ot.flush();
        ot.close();
        return out.toByteArray();
    }

    public static final String objectToHexString(Serializable s) throws IOException {
        return bytesToHexString(objectToBytes(s));
    }

    public static final Object hexStringToObject(String hex) throws IOException, ClassNotFoundException {
        return bytesToObject(hexStringToByte(hex));
    }

/** */

    public static String bcd2Str(byte[] bytes) {
        StringBuffer temp = new StringBuffer(bytes.length * 2);

        for (int i = 0; i < bytes.length; i++) {
            temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
            temp.append((byte) (bytes[i] & 0x0f));
        }
        return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp.toString().substring(1) : temp.toString();
    }

    public static byte[] str2Bcd(String asc) {
        int len = asc.length();
        int mod = len % 2;

        if (mod != 0) {
            asc = "0" + asc;
            len = asc.length();
        }

        byte abt[] = new byte[len];
        if (len >= 2) {
            len = len / 2;
        }

        byte bbt[] = new byte[len];
        abt = asc.getBytes();
        int j, k;

        for (int p = 0; p < asc.length() / 2; p++) {
            if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
                j = abt[2 * p] - '0';
            } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                j = abt[2 * p] - 'a' + 0x0a;
            } else {
                j = abt[2 * p] - 'A' + 0x0a;
            }

            if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
                k = abt[2 * p + 1] - '0';
            } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                k = abt[2 * p + 1] - 'a' + 0x0a;
            } else {
                k = abt[2 * p + 1] - 'A' + 0x0a;
            }

            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return bbt;
    }

//    public static String BCD2ASC(byte[] bytes) {
//        StringBuffer temp = new StringBuffer(bytes.length * 2);
//
//        for (int i = 0; i < bytes.length; i++) {
//            int h = ((bytes[i] & 0xf0) >>> 4);
//            int l = (bytes[i] & 0x0f);
//            temp.append(BToA[h]).append( BToA[l]);
//        }
//        return temp.toString() ;
//    }

    public static String MD5EncodeToHex(String origin) {
        return bytesToHexString(MD5Encode(origin));
    }

    public static byte[] MD5Encode(String origin) {
        return MD5Encode(origin.getBytes());
    }


    public static byte[] MD5Encode(byte[] bytes) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            return md.digest(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }


    public static String bytes2BinaryStr(byte[] bArray){

        String outStr = "";
        int pos = 0;
        for(byte b:bArray){
            pos = (b&0xF0)>>4;
            outStr+=binaryArray[pos];
            pos=b&0x0F;
            outStr+=binaryArray[pos];
        }
        return outStr;

    }

    public static String BinaryToHexString(byte[] bytes){

        String result = "";
        String hex = "";
        for(int i=0;i<bytes.length;i++){

            hex = String.valueOf(hexStr.charAt((bytes[i]&0xF0)>>4));

            hex += String.valueOf(hexStr.charAt(bytes[i]&0x0F));
            result +=hex+" ";
        }
        return result;
    }
    

    public static byte[] HexStringToBinary(String hexString){
		// hexString
        int len = hexString.length()/2;
        byte[] bytes = new byte[len];
		byte high = 0;
		byte low = 0;

        for(int i=0;i<len;i++){
            high = (byte)((hexStr.indexOf(hexString.charAt(2*i)))<<4);
            low = (byte)hexStr.indexOf(hexString.charAt(2*i+1));
			bytes[i] = (byte) (high | low);
        }
        return bytes;
    }

    public static boolean isNumeric(String str){
        for (int i = 0; i < str.length(); i++){
            System.out.println(str.charAt(i));
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }
}
