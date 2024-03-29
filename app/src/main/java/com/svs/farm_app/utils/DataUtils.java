package com.svs.farm_app.utils;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


public class DataUtils {

    private char[] getChar(int position) {
        String str = String.valueOf(position);
        if (str.length() == 1) {
            str = "0" + str;
        }
        char[] c = {str.charAt(0), str.charAt(1)};
        return c;
    }

    /**
     * 16�����ַ�ת��������
     *
     * @param hex
     * @return
     */
    public static byte[] hexStringTobyte(String hex) {
        if (TextUtils.isEmpty(hex)) {
            return null;
        }
        int len = hex.length() / 2;
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        String temp = "";
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
            temp += result[i] + ",";
        }
        // uiHandler.obtainMessage(206, hex + "=read=" + new String(result))
        // .sendToTarget();
        return result;
    }

    public static int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    /**
     * ����ת��16�����ַ�
     *
     * @param b
     * @return
     */
    public static String toHexString(byte[] b) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            buffer.append(toHexString1(b[i]));
        }
        return buffer.toString();
    }

    public static String toHexString1(byte b) {
        String s = Integer.toHexString(b & 0xFF);
        if (s.length() == 1) {
            return "0" + s;
        } else {
            return s;
        }
    }

    /**
     * ʮ������ַ�ת�����ַ�
     */
    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

    /**
     * �ַ�ת����ʮ������ַ�
     */
    public static String str2Hexstr(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString();
    }

    public static String byte2Hexstr(byte b) {
        String temp = Integer.toHexString(0xFF & b);
        if (temp.length() < 2) {
            temp = "0" + temp;
        }
        temp = temp.toUpperCase();
        return temp;
    }

    public static String str2Hexstr(String str, int size) {
        byte[] byteStr = str.getBytes();
        byte[] temp = new byte[size];
        System.arraycopy(byteStr, 0, temp, 0, byteStr.length);
        temp[size - 1] = (byte) byteStr.length;
        String hexStr = toHexString(temp);
        return hexStr;
    }

    /**
     * 16�����ַ�ָ�����ɿ飬ÿ��32��16�����ַ�16�ֽ�
     *
     * @param str
     * @return
     */
    public static String[] hexStr2StrArray(String str) {
        // 32��ʮ������ַ��ʾ16�ֽ�
        int len = 32;
        int size = str.length() % len == 0 ? str.length() / len : str.length()
                / len + 1;
        String[] strs = new String[size];
        for (int i = 0; i < size; i++) {
            if (i == size - 1) {
                String temp = str.substring(i * len);
                for (int j = 0; j < len - temp.length(); j++) {
                    temp = temp + "0";
                }
                strs[i] = temp;
            } else {
                strs[i] = str.substring(i * len, (i + 1) * len);
            }
        }
        return strs;
    }

    /**
     * ��16�����ַ�ѹ�����ֽ����飬�ڰ��ֽ�����ת����16�����ַ�
     *
     * @param hexstr
     * @return
     * @throws IOException
     */
    public static byte[] compress(byte[] data) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(data);
        gzip.close();
        return out.toByteArray();
    }

    /**
     * ��16�����ַ��ѹ��ѹ�����ֽ����飬�ڰ��ֽ�����ת����16�����ַ�
     *
     * @param hexstr
     * @return
     * @throws IOException
     */
    public static byte[] uncompress(byte[] data) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        GZIPInputStream gunzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n;
        while ((n = gunzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

    public static byte[] short2byte(short s) {
        byte[] size = new byte[2];
        size[0] = (byte) (s >>> 8);
        short temp = (short) (s << 8);
        size[1] = (byte) (temp >>> 8);

        // size[0] = (byte) ((s >> 8) & 0xff);
        // size[1] = (byte) (s & 0x00ff);
        return size;
    }

    public static short[] hexStr2short(String hexStr) {
        byte[] data = hexStringTobyte(hexStr);
        short[] size = new short[4];
        for (int i = 0; i < size.length; i++) {
            size[i] = getShort(data[i * 2], data[i * 2 + 1]);
        }
        return size;
    }

    public static short getShort(byte b1, byte b2) {
        short temp = 0;
        temp |= (b1 & 0xff);
        temp <<= 8;
        temp |= (b2 & 0xff);
        return temp;
    }

    /**
     * Make a list with one object
     *
     * @param object
     * @return
     */

    public static <T> List<T> makeSingleList(T object) {
        List<T> tempList = new ArrayList<>();
        tempList.add(object);
        return tempList;
    }

    /**
     * Copies text to clipboard
     *
     * @param name
     * @param value
     * @param ctx
     */
    public static void copyToClipBoard(String name, String value, Context ctx) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText(name, value);
        clipboard.setPrimaryClip(clip);
    }

    /**
     * Converts an input streams' data to string
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static String convertInputStreamToString(InputStream in) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        String line = "";
        StringBuilder result = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            result.append(line);
        }
        in.close();
        return result.toString();
    }

    /**
     * Returns the consumer friendly device name
     */
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER.toUpperCase();
        String model = Build.MODEL.toUpperCase();
        if (model.startsWith(manufacturer)) {
            return model.toUpperCase();
        }
        return manufacturer + model;
    }

    public static void writeBytesToFile( String fileDest,byte[] bFile) {

        try {
            FileOutputStream fos = new FileOutputStream(fileDest);
            fos.write(bFile);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void SaveDataToFile(String filename, byte[] data) {
        File f = new File(filename);
        if (f.exists()) {
            f.delete();
        }
        new File(filename);
        try {
            RandomAccessFile randomFile = new RandomAccessFile(filename, "rw");
            long fileLength = randomFile.length();
            randomFile.seek(fileLength);
            randomFile.write(data);
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] fileToByteArray(File file) {
        FileInputStream fileInputStream = null;
        byte[] bFile = new byte[(int) file.length()];
        try {
            //convert file into array of bytes
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
            for (int i = 0; i < bFile.length; i++) {
                System.out.print((char) bFile[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bFile;
    }


}
