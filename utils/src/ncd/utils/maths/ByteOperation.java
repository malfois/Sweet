/*
 * Copyright (c) 2012 Diamond Light Source Ltd.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package ncd.utils.maths;

import java.nio.ByteBuffer;

/** Utilities class */
public class ByteOperation {

	/**
	 * @param b1
	 * @param b2
	 * @return an integer from bytes
	 */
	public static int toInt(boolean littleEndian, boolean signed, int b1, int b2) {
		if (signed) {
			if (littleEndian)
				return littleEndianIntSigned(b1, b2);
			return bigEndianIntSigned(b1, b2);
		}
		if (littleEndian)
			return littleEndianInt(b1, b2);
		return bigEndianInt(b1, b2);
	}

	/**
	 * @param b1
	 * @param b2
	 * @return an integer from bytes specified in little endian order
	 */
	private static int littleEndianInt(int b1, int b2) {
		return ((b2 & 0xff) << 8) | (b1 & 0xff);
	}

	/**
	 * @param b1
	 * @param b2
	 * @return an integer from bytes specified in big endian order
	 */
	private static int bigEndianInt(int b1, int b2) {
		return ((b1 & 0xff) << 8) | (b2 & 0xff);
	}

	/**
	 * Sign extended conversion
	 * 
	 * @param b1
	 * @param b2
	 * @return an integer from bytes specified in little endian order
	 */
	private static int littleEndianIntSigned(int b1, int b2) {
		return (b2 << 8) | (b1 & 0xff);
	}

	/**
	 * Sign extended conversion
	 * 
	 * @param b1
	 * @param b2
	 * @return an integer from bytes specified in big endian order
	 */
	private static int bigEndianIntSigned(int b1, int b2) {
		return (b1 << 8) | (b2 & 0xff);
	}

	/**
	 * @param b1
	 * @param b2
	 * @return an integer from bytes
	 */
	public static int toInt(boolean littleEndian, int b1, int b2, int b3, int b4) {
		if (littleEndian)
			return littleEndianInt(b1, b2, b3, b4);
		return bigEndianInt(b1, b2, b3, b4);
	}

	/**
	 * @param b1
	 * @param b2
	 * @param b3
	 * @param b4
	 * @return an integer from bytes specified in little endian order
	 */
	private static int littleEndianInt(int b1, int b2, int b3, int b4) {
		return ((b4 & 0xff) << 24) | ((b3 & 0xff) << 16) | ((b2 & 0xff) << 8) | (b1 & 0xff);
	}

	/**
	 * @param b1
	 * @param b2
	 * @param b3
	 * @param b4
	 * @return an integer from bytes specified in big endian order
	 */
	private static int bigEndianInt(int b1, int b2, int b3, int b4) {
		return ((b1 & 0xff) << 24) | ((b2 & 0xff) << 16) | ((b3 & 0xff) << 8) | (b4 & 0xff);
	}

	public static byte[] toBytes(boolean littleEndian, int val) {
		if (littleEndian)
			return littleEndianBytes(val);
		return bigEndianBytes(val);
	}

	private static byte[] littleEndianBytes(int val) {
		return new byte[] { (byte) (val & 0xff), (byte) ((val >> 8) & 0xff), (byte) ((val >> 16) & 0xff), (byte) ((val >> 24) & 0xff) };
	}

	private static byte[] bigEndianBytes(int val) {
		return new byte[] { (byte) ((val >> 24) & 0xff), (byte) ((val >> 16) & 0xff), (byte) ((val >> 8) & 0xff), (byte) (val & 0xff) };
	}

	public static float toFloat(byte[] bdata) {
		return ByteBuffer.wrap(bdata).getFloat();
	}

	public static byte[] toBytes(float value) {
		return ByteBuffer.allocate(4).putFloat(value).array();
	}
}
