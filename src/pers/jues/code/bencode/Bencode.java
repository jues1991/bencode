package pers.jues.code.bencode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import pers.jues.data.byteBuffer;

/*
 * @author jues
 * @version 0.0.1
 * @time 2018-01-09
 * @see Bencode convert.
 * */
public class Bencode {
	public static class ObjectRef {
		public Object obj = null;
	}

	/*
	 * @see string to object list.
	 * 
	 * @param text: need parse string.
	 * 
	 * @return List<Object>.
	 */
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
			if (null == data.obj) {
				break;
			}
			//
			list.add(data.obj);
			//
			if (content.length() <= i) {
				break;
			}
			//
			content = content.substring(i);
		}
		//
		return list;
	}

	/*
	 * @see string to object.
	 * 
	 * @param text: need parse string; data is save object.
	 * 
	 * @return int.
	 */
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
				return 0;
			}
			// get number
			value = text.substring(begin, end);
			int i = Integer.parseInt(value);
			data.obj = i;
			end++;
		}
		// check is list
		else if ('l' == flag) {
			begin++;
			int i;
			String content = text.substring(begin);
			List<Object> list = new ArrayList<Object>();
			end = begin;
			//
			while (true) {
				ObjectRef l = new ObjectRef();
				//
				i = Bencode.fromString(content, l);
				end += i;
				if (null == l.obj) {
					break;
				}
				//
				list.add(l.obj);
				//
				if ((content.length() - 1) <= i) {
					break;
				}
				//
				content = content.substring(i);
			}
			//
			data.obj = list;
		}
		// check is dictionary
		else if ('d' == flag) {
			begin++;
			int i;
			String content = text.substring(begin);
			HashMap<Object, Object> list = new HashMap<Object, Object>();
			end = begin;
			//
			while (true) {
				ObjectRef k = new ObjectRef();
				ObjectRef v = new ObjectRef();

				// key
				i = Bencode.fromString(content, k);
				end += i;
				if (null == k.obj) {
					break;
				}

				if ((content.length() - 1) <= i) {
					break;
				}
				//
				content = content.substring(i);

				// value

				i = Bencode.fromString(content, v);
				end += i;
				if (null == v.obj) {
					break;
				}
				//
				list.put(k.obj, v.obj);
				//
				if ((content.length() - 1) <= i) {
					break;
				}
				//
				content = content.substring(i);
			}
			//
			data.obj = list;
		}
		// check is end flag
		else if ('e' == flag) {
			return 1;
		}
		// is string
		else {
			end = text.indexOf(':', begin);
			if (0 >= end) {
				return 0;
			}
			// get string len
			value = text.substring(begin, end);
			int len = Integer.parseInt(value);

			// get string value
			end++;
			begin = end;
			end = begin + len;
			value = text.substring(begin, end);
			data.obj = value;
		}

		//
		return end;
	}

	/*
	 * @see list to string.
	 * 
	 * @param list: need convert in the object list.
	 * 
	 * @return String.
	 */
	public static String toString(List<Object> list) {
		String text = new String();
		//
		for (Object obj : list) {
			String value = Bencode.toString(obj);
			//
			text += value;
		}
		//
		return text;
	}

	/*
	 * @see object to string.
	 * 
	 * @param list: need convert in the object.
	 * 
	 * @return String.
	 */
	public static String toString(Object obj) {
		String text = new String();
		//
		if (obj instanceof Integer) {
			// is number
			int number = (int) obj;

			text = String.format("i%de", number);

		} else if (obj instanceof String) {
			// is number
			String str = (String) obj;

			text = String.format("%d:%s", str.length(), str);

		} else if (obj instanceof List) {
			// is list
			@SuppressWarnings("unchecked")
			List<Object> l = (List<Object>) obj;
			String l_str = Bencode.toString(l);

			text = "l" + l_str + "e";

		} else if (obj instanceof HashMap) {
			// is dictionary
			@SuppressWarnings("unchecked")
			HashMap<Object, Object> d = (HashMap<Object, Object>) obj;
			String d_str = new String();
			//
			Iterator<Entry<Object, Object>> it = d.entrySet().iterator();
			while (it.hasNext()) {
				@SuppressWarnings("rawtypes")
				Entry entry = (Entry) it.next();
				//
				d_str += Bencode.toString(entry.getKey());
				d_str += Bencode.toString(entry.getValue());
			}

			//
			text = "d" + d_str + "e";

		} else {
			text = "";
		}
		//
		return text;
	}

	/*
	 * @see byte[] to object list.
	 * 
	 * @param buf: need parse byte[].
	 * 
	 * @return List<Object>.
	 */
	public static List<Object> fromByte(byte[] buf) {
		List<Object> list = new ArrayList<Object>();
		byteBuffer buff = new byteBuffer(buf);
		//
		while (true) {
			//
			ObjectRef data = new ObjectRef();
			int i;
			//
			i = Bencode.fromByte(buff, data);
			if (null == data.obj) {
				break;
			}
			//
			list.add(data.obj);
			//
			if (buff.length() <= i) {
				break;
			}
			//
			buff = buff.subBuff(i);
		}
		//
		return list;
	}



	/*
	 * @see string to object.
	 * 
	 * @param buff: need parse byte[]; data is save object.
	 * 
	 * @return int.
	 */
	public static int fromByte(byteBuffer buff, ObjectRef data) {
		if (0 >= buff.length() || null == data) {
			return 0;
		}

		//
		int begin, end;
		byte flag = buff.at(0);
		byteBuffer value;

		// init
		begin = 0;
		end = 0;

		// check is int
		if ((byte)'i' == flag) {
			begin++;
			end = buff.indexOf((byte)'e', begin);
			if (0 >= end) {
				return 0;
			}
			//
			value = buff.subBuff(begin, end);
			int i = value.toInt();
			data.obj = i;
			end++;
		}
		// check is list
		else if ('l' == flag) {
			begin++;
			int i;
			byteBuffer content = buff.subBuff(begin);
			List<Object> list = new ArrayList<Object>();
			end = begin;
			//
			while (true) {
				ObjectRef l = new ObjectRef();
				//
				i = Bencode.fromByte(content, l);
				end += i;
				if (null == l.obj) {
					break;
				}
				//
				list.add(l.obj);
				//
				if ((content.length() - 1) <= i) {
					break;
				}
				//
				content = content.subBuff(i);
			}
			//
			data.obj = list;
		}
		// check is dictionary
		else if ('d' == flag) {
			begin++;
			int i;
			byteBuffer content = buff.subBuff(begin);
			HashMap<Object, Object> list = new HashMap<Object, Object>();
			end = begin;
			//
			while (true) {
				ObjectRef k = new ObjectRef();
				ObjectRef v = new ObjectRef();

				// key
				i = Bencode.fromByte(content, k);
				end += i;
				if (null == k.obj) {
					break;
				}

				if ((content.length() - 1) <= i) {
					break;
				}
				//
				content = content.subBuff(i);

				// value

				i = Bencode.fromByte(content, v);
				end += i;
				if (null == v.obj) {
					break;
				}
				//
				list.put(k.obj, v.obj);
				//
				if ((content.length() - 1) <= i) {
					break;
				}
				//
				content = content.subBuff(i);
			}
			//
			data.obj = list;
		}
		// check is end flag
		else if ('e' == flag) {
			return 1;
		}
		// is string
		else {
			end = buff.indexOf((byte)':', begin);
			if (0 >= end) {
				return 0;
			}
			// get string len
			value = buff.subBuff(begin, end);
			int len = value.toInt();

			// get string value
			end++;
			begin = end;
			end = begin + len;
			value = buff.subBuff(begin, end);
			data.obj = value.value();
		}

		//
		return end;
	}
	
	
	
	
	
	
	
	
	
	/*
	 * @see list to byte[].
	 * 
	 * @param list: need convert in the object list.
	 * 
	 * @return byte[].
	 */
	public static byte[] toByte(List<Object> list) {
		byteBuffer buff = new byteBuffer();
		//
		for (Object obj : list) {
			byteBuffer value = Bencode.toByte(obj);
			//
			buff.add(value);
		}
		//
		return buff.value();
	}

	/*
	 * @see object to string.
	 * 
	 * @param list: need convert in the object.
	 * 
	 * @return byteBuffer.
	 */
	public static byteBuffer toByte(Object obj) {
		byteBuffer buff = new byteBuffer();
		//
		if (obj instanceof Integer) {
			// is number
			int number = (int) obj;

			buff.add((byte)'i');	
			buff.add(number);
			buff.add((byte)'e');	

		} else if (obj instanceof byte[]) {
			// is number
			byte[] buf = (byte[]) obj;

			buff.add(buf.length);
			buff.add((byte)':');
			buff.add(buf);
			

		} else if (obj instanceof List) {
			// is list
			@SuppressWarnings("unchecked")
			List<Object> l = (List<Object>) obj;
			byte[] buf = Bencode.toByte(l);

			buff.add((byte)'l');	
			buff.add(buf);
			buff.add((byte)'e');	

		} else if (obj instanceof HashMap) {
			// is dictionary
			@SuppressWarnings("unchecked")
			HashMap<Object, Object> d = (HashMap<Object, Object>) obj;
			byteBuffer value = new byteBuffer();
			//
			Iterator<Entry<Object, Object>> it = d.entrySet().iterator();
			while (it.hasNext()) {
				@SuppressWarnings("rawtypes")
				Entry entry = (Entry) it.next();
				//
				value.add(Bencode.toByte(entry.getKey()));
				value.add(Bencode.toByte(entry.getValue()));
			}

			//
			buff.add((byte)'d');	
			buff.add(value);
			buff.add((byte)'e');	

		} else {
			buff = null;
		}
		//
		return buff;
	}
	

}
