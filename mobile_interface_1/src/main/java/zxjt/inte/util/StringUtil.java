package zxjt.inte.util;

/**
 * 字符串工具类
 * 
 * @since 1.0.0
 */
public abstract class StringUtil {
	private static final char FOLDER_SEPARATOR = '/';
	private static final char EXTENSION_SEPARATOR = '.';

	/**
	 * 检查指定字符串是否为null或长度是否为0
	 * 
	 * @param str 被检查字符串
	 * @return 不为null并且有长度返回true
	 */
	public static boolean hasLength(CharSequence str) {
		return (str != null && str.length() > 0);
	}

	/**
	 * 检查指定字符串是否为null或长度是否为0
	 * 
	 * @param str 被检查字符串
	 * @return 不为null并且有长度返回true
	 */
	public static boolean hasLength(String str) {
		return hasLength((CharSequence) str);
	}

	/**
	 * 检查指定字符串是否有内容
	 * 
	 * @param str 被检查字符串
	 * @return 当字符串包含至少一个非空白字符时返回true
	 */
	public static boolean hasText(CharSequence str) {
		if (!hasLength(str)) {
			return false;
		}
		int vStrLen = str.length();
		for (int i = 0; i < vStrLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检查指定字符串是否有内容
	 * 
	 * @param str 被检查字符串
	 * @return 当字符串包含至少一个非空白字符时返回true
	 */
	public static boolean hasText(String str) {
		return hasText((CharSequence) str);
	}

	/**
	 * 替换字符串中所有的指定文本为新文本
	 * 
	 * @param inString 要查找的字符串
	 * @param oldPattern 被替换的文本
	 * @param newPattern 要替换成的文本
	 * @return 替换后的字符串
	 */
	public static String replace(String inString, String oldPattern, String newPattern) {
		if (!hasLength(inString) || !hasLength(oldPattern) || newPattern == null) {
			return inString;
		}
		StringBuilder vSb = new StringBuilder();
		int vPos = 0;
		int vIndex = inString.indexOf(oldPattern);
		int vPatLen = oldPattern.length();
		while (vIndex >= 0) {
			vSb.append(inString.substring(vPos, vIndex));
			vSb.append(newPattern);
			vPos = vIndex + vPatLen;
			vIndex = inString.indexOf(oldPattern, vPos);
		}
		vSb.append(inString.substring(vPos));
		return vSb.toString();
	}

	/**
	 * 从给定的路径中获取文件名（例如"folder/file.xml" -> "file.xml"），分隔符需使用"/"
	 * 
	 * @param path 文件路径
	 * @return 文件名，若没有返回null
	 */
	public static String getFilename(String path) {
		if (path == null) {
			return null;
		}
		int vSeparatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		return (vSeparatorIndex != -1 ? path.substring(vSeparatorIndex + 1) : path);
	}

	/**
	 * 从给定的路径中获取扩展名（例如"folder/file.xml" -> "xml"），分隔符需使用"/"
	 * 
	 * @param path 文件路径
	 * @return 扩展名，若没有返回null
	 */
	public static String getFilenameExtension(String path) {
		if (path == null) {
			return null;
		}
		int vExtIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
		if (vExtIndex == -1) {
			return null;
		}
		int vFolderIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		if (vFolderIndex > vExtIndex) {
			return null;
		}
		return path.substring(vExtIndex + 1);
	}

	/**
	 * 去掉首尾空白字符
	 * 
	 * @param str 待处理字符串
	 * @return 去掉首尾空白字符的字符串
	 * @see Character#isWhitespace(char)
	 */
	public static String trimWhitespace(String str) {
		return trimWhitespace(str, null);
	}

	/**
	 * 去掉首尾指定字符
	 * 
	 * @param str 待处理字符串
	 * @param stripChars 要去除的字符，为空或null时则去除空白字符
	 * @return 去掉首尾空白字符的字符串
	 */
	public static String trimWhitespace(String str, String stripChars) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}

		int start = 0, end = strLen;
		if (stripChars == null || stripChars.isEmpty()) {
			while (start != strLen && Character.isWhitespace(str.charAt(start))) {
				start++;
			}
			if (start == strLen) {
				return "";
			}
			while (end != 0 && Character.isWhitespace(str.charAt(end - 1))) {
				end--;
			}
		} else {
			while (start != strLen && stripChars.indexOf(str.charAt(start)) != -1) {
				start++;
			}
			if (start == strLen) {
				return "";
			}
			while (end != 0 && stripChars.indexOf(str.charAt(end - 1)) != -1) {
				end--;
			}
		}

		return str.substring(start, end);
	}

}
