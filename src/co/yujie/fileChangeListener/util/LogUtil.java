package co.yujie.fileChangeListener.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * ��־������
 * @author yujie
 *
 */
public class LogUtil {
	
	private static Logger log = LogManager.getLogger(LogUtil.class);
	
	/**
	 * ���������ļ���Ĭ���ڹ����ռ��в���ļ�����
	 */
	static {
		String workPlugPath = ConfigUtil.getWorkPlugPath();
		File dir = new File(workPlugPath);
		if(dir.exists()) {
			File file = new File(workPlugPath + "log4j.properties");
			if(file.exists()) {
				Properties properties = new Properties();
				try {
					FileInputStream fis = new FileInputStream(file);
					properties.load(fis);
				} catch (IOException e) {
					e.printStackTrace();
				}
				PropertyConfigurator.configure(properties);
			}
		}
	}
	
	/**
	 * ��ȡ��־����
	 * @param clazz
	 * @return
	 */
	public static Logger getLog(Class clazz) {
		return Logger.getLogger(clazz);
	}

}
