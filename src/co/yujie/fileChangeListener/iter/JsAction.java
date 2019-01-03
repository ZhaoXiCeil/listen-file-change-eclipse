package co.yujie.fileChangeListener.iter;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

import co.yujie.fileChangeListener.model.FileChangeInfo;
import co.yujie.fileChangeListener.util.ConfigUtil;
import co.yujie.fileChangeListener.util.FileUtil;
import co.yujie.fileChangeListener.util.LogUtil;

/**
 * ��js�仯ʱִ��
 * @author yujie
 *
 */
public class JsAction implements IAction {
	
	private final static Logger log = LogUtil.getLog(JsAction.class);

	@Override
	public void execute(Object obj) {

		@SuppressWarnings("unchecked")
		Map<String, Object> data = (Map<String, Object>) obj;
		FileChangeInfo fileChangeInfo = (FileChangeInfo) data.get("fileChangeInfo");
		
		DocumentOrganization doc = new DocumentOrganization(fileChangeInfo);
		String command = doc.createWebpackCommand();
		log.debug("webpack���" + command);
		
		String result = "����ʧ�ܣ�";

		if(null != command) {
			Process process = null;
			try {
				process = Runtime.getRuntime().exec(command);
				process.waitFor();
			} catch (IOException | InterruptedException e) {
				stopExecute();  //�������(�˳��˴α�������)
				log.error("����ʱ�׳��쳣,�Զ��������α�������");
			} finally {
				if(process.exitValue() == 0) {
					result = "����ɹ���";
				}
				process.destroy();
				process = null;
				log.debug(result);
			}
			
			log.debug("��ʼִ����β����");
			//������β����  ���tomcat��������
			afterExecute(doc);
		}else {
			stopExecute();  //�������(�˳��˴α�������)
			log.debug("��������Ϊnull, " + result);
		}
		
		
		/**
		 * ˢ���ļ�
		 * ע�⣺eclipse������ⲿ�༭���޸��ļ��󲻻�ʹ�ļ���eclipse��ˢ��
		 */
//		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(doc.getOutFile()));
//		try {
//			file.refreshLocal(IResource.DEPTH_ONE, null);
//		} catch (CoreException e) {
//			log.error("ˢ���ļ�ʧ��");
//		}
		
	}
	
	/**
	 * ִ�к�����β����
	 */
	private void afterExecute(DocumentOrganization doc) {
		String srcPath = doc.getWebAbsoluteOutFile();
		String sourcePath = doc.getAbsoluteOutFile();
		if(null != srcPath && null != sourcePath) {
			File source = new File(sourcePath);
			File src = new File(srcPath);
			if(source.exists()) {
				//�����ļ���tomcat�� ���tomcat��������
				try {
					FileUtil.copyFileUsingFileStreams(source, src);
				} catch (IOException e) {
					stopExecute();  //�������(�˳��˴α�������)
					log.info("sourcePath:" + doc.getAbsoluteOutFile());
					log.info("srcPath:"+ srcPath);
					log.error("�����ļ���tomcat��ʧ����,�Զ������˴�����");
				}
			}
		}else {
			log.debug("srcPath:" + srcPath);
			log.debug("sourcePath:" + sourcePath);
		}
	}
	
	/**
	 * �����˴α���
	 */
	private void stopExecute() {
		Compiler.setStatus(Boolean.FALSE);
	}

}
