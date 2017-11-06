package zxjt.inte.util;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;



/**
 * 核心配置类
 * 
 * @since 1.0.0
 */
public abstract class GetFolderPath {
	private static Map<String, String> folderPaths = new HashMap<>();
	private static Map<String, Object> attributes = new HashMap<>();
	private static String rootPath;
	
	static {
		for (FolderTypes vType : FolderTypes.values()) {
			folderPaths.put(vType.getFolderType(), vType.getDefault());
			
		}
		
	}

	/**
	 * 获取根目录路径，默认为"当前目录/平台名/"，例如 /D:/Workspaces/ZXJT/target/classes/android/
	 * 
	 * @return 根目录路径
	 */
	public static String getRootPath() {
		if (rootPath == null) {
			rootPath = GetFolderPath.class.getResource("/").getPath()  + "/";
			
		}
		return rootPath;
	}

	/**
	 * 获取文件夹路径
	 * 
	 * @param folderType 文件夹类型
	 * @return 文件夹路径，以"/"结束
	 */
	public static String getFolderPath(String folderType) {
		Assert.assertNotNull(folderType, "FolderType must not be null");
		String vPath = folderPaths.get(folderType);
		Assert.assertNotNull(vPath, "Can't get folder's path with type [" + folderType + "]");
		return getResourcePath() + vPath + "/";
	}
	public static String getResourcePath(){
		
		return System.getProperty("user.dir")+ "/";
		
	}
	
	
	/**
	 * 获取文件夹路径
	 * 
	 * @param folderType 文件夹类型
	 * @return 文件夹路径，以"/"结束
	 */
	public static String getFolderPath(FolderTypes folderType) {
		Assert.assertNotNull(folderType, "FolderType must not be null");
		return getFolderPath(folderType.getFolderType());
	}

	/**
	 * 设置文件夹路径
	 * 
	 * @param type 文件夹类型
	 * @param path 文件夹路径
	 */
	public static void setFolderPath(String type, String path) {
		folderPaths.put(type, path);
	}

	/**
	 * 设置自定义属性
	 * 
	 * @param name 属性名
	 * @param value 属性值
	 */
	public static void setAttribute(String name, Object value) {
		attributes.put(name, value);
	}

	/**
	 * 获取自定义属性
	 * 
	 * @param name 属性名
	 * @return 属性值，未找到对应属性返回null
	 */
	public static Object getAttribute(String name) {
		return attributes.get(name);
	}

}
