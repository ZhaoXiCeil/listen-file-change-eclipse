package co.yujie.fileChangeListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.eclipse.core.internal.runtime.Activator;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * �������ʱ�߹�����·
 * @author yujie
 *
 */
public class FileChangeListener implements IStartup {
	
	private static Logger logger = null;
	
	@Override
	public void earlyStartup() {
		// TODO Auto-generated method stub
		System.out.println("-------- test start up.");
		
		JavaCore.addElementChangedListener(new IElementChangedListener() {
			
			@Override
			public void elementChanged(ElementChangedEvent event) {
				
				if(event != null) {
					System.out.println("--------- java-core: " + event.getDelta());
		        	System.out.println("-------- type: "+ event.getType());
				}
			}
		});
		
		
		
		createPlugLog();
		//log.info("�Ҵ�ӡ�˵�һ����־");
		
		
		/*
		final IWorkbench workbench = PlatformUI.getWorkbench();
		workbench.getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
				if(window != null) {
					
				}
			}
			
		});
		*/
	}
	
	private void createPlugLog() {
		String workPlacePath = Platform.getInstanceLocation().getURL().getPath();
		String plugPath = workPlacePath + ".metadata/.plugins/aLitterEdite.FileChangeListener/";
		System.out.println("======workplace==== " + workPlacePath);
		isExist(plugPath);
		//createFile(plugPath + new Date().getTime() + ".log");
	}
	
	private Logger getLogger(String path) {
		if(logger == null) {
			logger = Logger.getLogger("demain");
			File file = new File(path + "/logger.properties");
			if(file.exists()) {
				try {
					LogManager.getLogManager().readConfiguration(new FileInputStream(file));
				} catch (SecurityException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return logger;
	}
	
	private void createFile(String path) {
		File file = new File(path);
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void isExist(String path) {
		File file = new File(path);
		//�ж��ļ����Ƿ����,����������򴴽��ļ���
		if (!file.exists()) {
		file.mkdir();
		}
		}

}
