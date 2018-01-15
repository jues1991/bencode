package pers.jues.data;

public class byteBuffer {
	byte[] m_buf = null;

	/*
	 * @see byteBuffer.
	 * 
	 * @param .
	 * 
	 * @return .
	 */
	public byteBuffer() {
		this.m_buf = null;
	}

	/*
	 * @see byteBuffer.
	 * 
	 * @param buf: init byte[].
	 * 
	 * @return .
	 */
	public byteBuffer(byte[] buf) {
		this.fromByte(buf);
	}

	/*
	 * @see from byte[].
	 * 
	 * @param buf: reset byte[].
	 * 
	 * @return int: byte[] length.
	 */
	public int fromByte(byte[] buf) {
		this.m_buf = buf;

		//
		return this.length();
	}

	/*
	 * @see resize length.
	 * 
	 * @param size: new size;
	 * 
	 * @param copy: is copy old data;
	 * 
	 * @return int: byte[] length.
	 */
	public int resize(int size, boolean copy) {
		if (0 > size) {
			return (-1);
		} else if (0 == size) {
			this.m_buf = null;
			//
			return this.length();
		}
		//
		byte[] buf = new byte[size];
		//
		if (true == copy && null != this.m_buf) {
			for (int i = 0; this.m_buf.length > i && buf.length > i; i++) {
				buf[i] = this.m_buf[i];
			}
		}
		//
		this.m_buf = buf;
		//
		return this.length();
	}

	/*
	 * @see resize length.
	 * 
	 * @param size: new size;
	 * 
	 * @return int: byte[] length.
	 */
	public int resize(int size) {
		return this.resize(size, true);
	}

	/*
	 * @see add byte.
	 * 
	 * @param buff: buff;
	 * 
	 * @return int: byte[] length.
	 */
	public int add(byteBuffer buff) {
		int length_new = buff.length();
		if (0 >= length_new) {
			return this.length();
		}
		//
		int length_old = this.length();
		this.resize(length_old + length_new);
		for (int i = 0; length_new > i; i++) {
			this.m_buf[i + length_old] = buff.m_buf[i];
		}

		//
		return this.length();
	}

	/*
	 * @see add byte.
	 * 
	 * @param buf: byte[];
	 * 
	 * @return int: byte[] length.
	 */
	public int add(byte[] buf) {
		return this.add(new byteBuffer(buf));
	}

	/*
	 * @see add byte.
	 * 
	 * @param b: byte;
	 * 
	 * @return int: byte[] length.
	 */
	public int add(byte b) {
		byte[] buf = new byte[1];
		//
		buf[0] = b;
		return this.add(new byteBuffer(buf));
	}

	
	/*
	 * @see add number.
	 * 
	 * @param number: int;
	 * 
	 * @return int: byte[] length.
	 */
	public int add(int number) {
		String text = String.format("%d", number);
		//
		byte[] buf = new byte[text.length()];
		for ( int i = 0;buf.length>i;i++ ) {
			buf[i] = (byte)text.charAt(i);
		}
		//
		return this.add(buf);
	}

	/*
	 * @see value to byte[].
	 * 
	 * @param .
	 * 
	 * @return byte[]: byte[] value.
	 */
	public byte[] value() {
		return this.m_buf;
	}

	/*
	 * @see get length.
	 * 
	 * @param i: pos.
	 * 
	 * @return byte: byte value.
	 */
	public byte at(int i) {
		return this.m_buf[i];
	}

	/*
	 * @see get length.
	 * 
	 * @param .
	 * 
	 * @return int: byte[] length.
	 */
	public int length() {
		if (null == this.m_buf) {
			return 0;
		}
		//
		return this.m_buf.length;
	}

	/*
	 * @see convert to int.
	 * 
	 * @param .
	 * 
	 * @return int: int number.
	 */
	public int toInt() {
		int length = this.length();
		if (0 >= length) {
			return 0;
		}
		//
		String text = new String();
		for (int i = 0; length > i; i++) {
			byte b = this.m_buf[i];
			if (0x30 <= b && 0x39 >= b) {
				text += String.valueOf((char) b);
			}

		}
		//
		return Integer.parseInt(text);
	}

	/*
	 * @see convert to Byte[].
	 * 
	 * @param .
	 * 
	 * @return Byte[]:.
	 */
	public Byte[] toByte() {
		int length = this.length();
		//
		if (0 >= length) {
			return null;
		}
		//
		Byte[] arry = new Byte[length];
		for (int i = 0; length > i; i++) {
			arry[i] = this.m_buf[i];
		}
		//
		return arry;
	}

	/*
	 * @see get length.
	 * 
	 * @param beginIndex: index for begin;
	 * 
	 * @param endIndex: index for end;
	 * 
	 * @return byteBuffer: byteBuffer data.
	 */
	public byteBuffer subBuff(int beginIndex, int endIndex) {
		int size = endIndex - beginIndex;
		byteBuffer buff = new byteBuffer();
		if (0 >= size) {
			return new byteBuffer();
		}
		//
		byte[] buf = new byte[size];
		//
		for (int i = 0; size > i; i++) {
			buf[i] = this.m_buf[beginIndex + i];
		}
		//
		buff.fromByte(buf);
		//
		return buff;
	}

	/*
	 * @see get length.
	 * 
	 * @param beginIndex: index for begin;
	 * 
	 * @param endIndex: index for end;
	 * 
	 * @return byteBuffer: byteBuffer data.
	 */
	public byteBuffer subBuff(int beginIndex) {
		return this.subBuff(beginIndex, this.length());
	}

	/*
	 * @see search byte for byte[].
	 * 
	 * @param b: search byte value;
	 * 
	 * @param i: search for begin pos;
	 * 
	 * @return int: found pos, not found is (-1);
	 */
	public int indexOf(byte b, int i) {
		int length = this.length();

		if (length <= i) {
			return (-1);
		}
		//
		while (length > i) {
			if (this.m_buf[i] == b) {
				return i;
			}
			//
			i++;
		}
		//
		return 0;
	}

}
