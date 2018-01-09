package pers.jues.code.bencode;

import java.util.ArrayList;
import java.util.List;

public class Bencode {
	 public static class ObjectRef{
		public Object obj = new Object();
	}


	//
	public static List<Object> fromString(String text) {
		List<Object> list = new ArrayList<Object>();
		String content = new String(text);
		//
		while (true) {
			//
			 ObjectRef data = new ObjectRef();
			int i;
			//
			i = Bencode.fromString(content, data);
			if (0 >= i || data.obj.getClass().equals(Object.class)  ) {
				break;
			}
			//
			list.add(data.obj);
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
	public static int fromString(String text, ObjectRef data) {
		if (0 >= text.length() || null == data) {
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
			begin++;
			end = text.indexOf('e', begin);
			if (0 >= end) {
				return (-1);
			}
			// get number
			value = text.substring(begin, end);
			int i = Integer.parseInt(value);
			data.obj = i;
			end++;
		}
		// check is list
		else if ('l' == flag) {
			 
		}
		// check is dictionary
		else if ('d' == flag) {
			 
		}
		// is string
		else {
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
			data.obj = value;
		}

		//
		return end;
	}

}
