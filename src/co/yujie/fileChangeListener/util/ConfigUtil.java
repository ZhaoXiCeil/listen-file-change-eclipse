package co.yujie.fileChangeListener.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Platform;

/**
 * ������Ŀ����
 * @author yujie
 *
 */
public class ConfigUtil {

	/**
	 * webpack.js ��װλ��
	 */
	private static String webpackPrefix = null;
	
	/**
	 * ��Ҫ������Ŀ��Ŀ¼
	 */
	private static String context = null;
	
	/**
	 * �����Ŀ¼
	 */
	private static String outPath = null;
	
	/**
	 * ��Ҫ�������Ŀ��
	 */
	private static String projectName = null;
	
	/**
	 * ����ģʽ  ��������/��������,Ĭ�Ͽ�������
	 */
	private static String model = "development";
	
	/**
	 * ��Ч���ļ�����
	 */
	private static String fileType = "js";
	
	/**
	 * ��Ч���ļ���
	 */
	private static String fileName = "index.js";
	
	/**
	 * ��������ռ�λ��
	 */
	private final static String workPlugPath;
	
	/**
	 * �����װλ��
	 */
	private final static String plugPath;
	
	private final static Logger log = LogUtil.getLog(ConfigUtil.class);
	
	static {
		workPlugPath = Platform.getInstanceLocation().getURL().getPath() + 
				".metadata/.plugins/co.yujie.fileChangeListener/";
		plugPath = Platform.getInstallLocation().getURL().getPath() + 
				"plugins/co.yujie.fileChangeListener/";
		File file = findConfigFile();
		if(null != file) {
			Properties properties = new Properties();
			try {
				FileInputStream fis = new FileInputStream(file);
				properties.load(fis);
			} catch (IOException e) {
				e.printStackTrace();
			}
			webpackPrefix = properties.getProperty("webpackPrefix");
			context = properties.getProperty("context");
			outPath = properties.getProperty("outPath");
			projectName = properties.getProperty("projectName");
			String outP = properties.getProperty("model");
			if(null != outP) {
				model = outP;
			}
			String fileN = properties.getProperty("fileName");
			if(null != fileN) {
				fileName = fileN;
			}
			String fileT = properties.getProperty("fileType");
			if(null != fileT) {
				//fileType = fileT;
			}
		}
	}
	
	/**
	 * ��������
	 */
	public static void loadConfigs() {
		File plugPathFile = new File(plugPath);
		log.debug("�����װλ�ã�" + plugPath);
		if(!plugPathFile.exists()) {
			plugPathFile.mkdirs();
		}
		File workPathFile = new File(workPlugPath);
		log.debug("����ڹ����ռ�λ�ã�" + workPlugPath);
		if(!workPathFile.exists()) {
			workPathFile.mkdirs();
		}
	}
	
	/**
	 * Ѱ�������ļ�λ��
	 * @return
	 */
	private static File findConfigFile() {
		File plugPathFile = new File(plugPath + "config.properties");
		if(plugPathFile.exists()) {
			return plugPathFile;
		}
		File workPathFile = new File(workPlugPath + "config.properties");
		if(workPathFile.exists()) {
			return workPathFile;
		}
		return null;
	}

	public static String getWebpackPrefix() {
		return webpackPrefix;
	}

	public static String getWorkPlugPath() {
		return workPlugPath;
	}

	public static String getContext() {
		return context;
	}

	public static String getPlugpath() {
		return plugPath;
	}

	public static String getModel() {
		return model;
	}

	public static String getOutPath() {
		return outPath;
	}

	public static String getFileType() {
		return fileType;
	}

	public static String getFileName() {
		return fileName;
	}

	public static String getProjectName() {
		return projectName;
	}
	
}
