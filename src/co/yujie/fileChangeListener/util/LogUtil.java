package co.yujie.fileChangeListener.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;

/**
 * ��־������
 * @author yujie
 *
 */
public class LogUtil {
	
	public static Logger log = LogManager.getLogger(LogUtil.class);
	
	/**
	 * ���������ļ���Ĭ���ڹ����ռ��в���ļ�����
	 */
	static {
		String workPlugPath = Platform.getInstanceLocation().getURL().getPath() + 
				".metadata/.plugins/co.yujie.fileChangeListener/";
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
		}else {
			dir.mkdirs();
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
