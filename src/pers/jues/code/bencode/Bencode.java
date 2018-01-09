package pers.jues.code.bencode;

import java.util.ArrayList;
import java.util.List;

public class Bencode {
	// data type
	public static enum TYPE {
		INVALID, STRING, INT, LIST, DICTIONARY,
	}

	// var
	TYPE m_type = TYPE.INVALID;
	Object m_value;

	// type
	public TYPE type() {
		return this.m_type;
	}

	// value
	public Object value() {
		return this.m_value;
	}

	//
	public static List<Bencode> fromString(String text) {
		List<Bencode> list = new ArrayList<Bencode>();
		String content = new String(text);
		//
		while (true) {
			//
			Bencode code = new Bencode();
			int i;
			//
			i = Bencode.fromString(content, code);
			if (0 >= i || TYPE.INVALID == code.type()) {
				break;
			}
			//
			list.add(code);
			//
			i++;
			//
			if ( content.length() <= i ) {
				break;
			}
			//
			content = content.substring(i);
		}
		//
		return list;
	}

	//
	public static int fromString(String text, Bencode code) {
		if (0 >= text.length() || null == code) {
			return 0;
		}

		//
		int begin, end;
		char flag = text.charAt(0);
		String value;

		// init
		begin = 0;
		end = 0;

		// check is int
		if ('i' == flag) {
			code.m_type = TYPE.INT;
			begin++;
			end = text.indexOf('e', begin);
			if (0 >= end) {
				return (-1);
			}
			// get number
			value = text.substring(begin, end);
			int i = Integer.parseInt(value);
			code.m_value = i;
		}
		// check is list
		else if ('l' == flag) {
			code.m_type = TYPE.LIST;
		}
		// check is dictionary
		else if ('d' == flag) {
			code.m_type = TYPE.DICTIONARY;
		}
		// is string
		else {
			code.m_type = TYPE.STRING;
			end = text.indexOf(':', begin);
			if (0 >= end) {
				return (-1);
			}
			// get string len
			value = text.substring(begin, end);
			int len = Integer.parseInt(value);
			
			// get string value
			end++;
			begin = end;
			end = begin+len;
			value = text.substring(begin, end);
			code.m_value = value;
		}

		//
		return end;
	}

}
