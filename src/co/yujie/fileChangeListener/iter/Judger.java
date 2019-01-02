package co.yujie.fileChangeListener.iter;

import org.apache.log4j.Logger;

import co.yujie.fileChangeListener.model.FileChangeInfo;
import co.yujie.fileChangeListener.model.FileOperate;
import co.yujie.fileChangeListener.util.ConfigUtil;
import co.yujie.fileChangeListener.util.LogUtil;

/**
 * ��һЩҵ�����ʵ��ж�
 * @author yujie
 *
 */
public class Judger {

	private final static Logger log = LogUtil.getLog(Judger.class);

	/**
	 * �ж������ļ��Ƿ�ɹ�����
	 * @return
	 */
	public static boolean haveConfig() {
		if(null != ConfigUtil.getWebpackPrefix() 
				&& null != ConfigUtil.getContext() 
				&& null != ConfigUtil.getOutPath() 
				&& null != ConfigUtil.getModel() 
				&& null != ConfigUtil.getProjectName()
				&& "js".equals(ConfigUtil.getFileType())) {
			log.debug("�����ļ��ɹ����أ���Ч���ļ����ͣ�" + ConfigUtil.getFileType());
			return true;
		}
		log.debug("�����ļ�δ�ɹ�����");
		return false;
	}
	
	/**
	 * �˶���Ŀ�������õ���Ŀ���Ƿ�һ��
	 * @param info
	 * @return
	 */
	public static boolean checkProject(FileChangeInfo info) {
		String projectName = ConfigUtil.getProjectName();
		if(null != projectName) {
			String[] PNames = projectName.split("/");
			for(int i=0; i<PNames.length; i++) {
				if(info.getProjectName().equals(PNames[i])) {
					return true;
				}
			}
		}
		log.debug("��Ŀ������");
		return false;
	}
	
	/**
	 * �ж��ļ��仯��Ϣ�Ƿ�ƥ������·��
	 * @return
	 */
	public static boolean mateContext(FileChangeInfo info) {
		if(!"js".equals(info.getType())) {
			log.debug("ֻ�ܱ���js�ļ��������ļ����ͣ�" + info.getType());
			return false;
		}
		if(null != info 
				&& FileOperate.CHANGE == info.getOpear() 
				&& null != info.getLocation()) {
			if(info.getLocation().startsWith(info.getProjectLocation() + ConfigUtil.getContext())) {
				return true;
			}
			log.debug("�仯�ļ�λ�ã�" + info.getLocation());
			log.debug("���������ļ�λ�ã�" + info.getProjectLocation() + ConfigUtil.getContext());
			return false;
		}
		log.debug("û�б仯���ļ�����Ϣ������");
		return false;
	}
	
}
