package com.telpo.usb.finger.util;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Created by yangw160602 on 2016/11/15.
 */

public class RawToBitMap {
	public static byte[] readByteArrayFormStream(InputStream stream) {
		try {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			int len = 0;
			byte[] tmp = new byte[1024];
			while ((len = stream.read(tmp)) != -1) {
				outStream.write(tmp, 0, len);
			}
			byte[] data = outStream.toByteArray();
			return data;
		} catch (IOException e) {
			e.printStackTrace();
			return new byte[0];
		}
	}

	public static Bitmap convert8bit(byte[] data, int width, int height) {
		byte[] Bits = new byte[data.length * 4];
		int i;
		for (i = 0; i < data.length; i++) {
			Bits[i * 4] = Bits[i * 4 + 1] = Bits[i * 4 + 2] = data[i];
			Bits[i * 4 + 3] = -1;
		}

		Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		bmp.copyPixelsFromBuffer(ByteBuffer.wrap(Bits));
		return bmp;
	}

	public static Bitmap convert24bit(byte[] data, int width, int height) {
		byte[] Bits = new byte[data.length * 4];
		int i;
		for (i = 0; i < data.length / 3; i++) {
			// = 0xff;104
			Bits[i * 4] = data[i * 3];
			Bits[i * 4 + 1] = data[i * 3 + 1];
			Bits[i * 4 + 2] = data[i * 3 + 2];
			Bits[i * 4 + 3] = -1;
		}

		Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		bmp.copyPixelsFromBuffer(ByteBuffer.wrap(Bits));
		return bmp;
	}

	public static Bitmap convert8bit(InputStream stream, int width, int height) {
		return convert8bit(readByteArrayFormStream(stream), width, height);
	}

	public static Bitmap convert24bit(InputStream stream, int width, int height) {
		return convert24bit(readByteArrayFormStream(stream), width, height);
	}
}
