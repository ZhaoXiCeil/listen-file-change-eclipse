package co.yujie.fileChangeListener.iter;

import java.io.File;

import org.apache.log4j.Logger;

import co.yujie.fileChangeListener.model.FileChangeInfo;
import co.yujie.fileChangeListener.util.ConfigUtil;
import co.yujie.fileChangeListener.util.LogUtil;

/**
 * �����ĵ���֯�ṹ��Ѱ�Һ��ʵı���Ŀ���ļ�
 * @author yujie
 *
 */
public class DocumentOrganization {
	
	private final static Logger log = LogUtil.getLog(DocumentOrganization.class);
	
	/**
	 * ��Ŀ·��
	 */
	private String projectPath = null;

	/**
	 * �����ļ�·��
	 */
	private String entryPath = null;

	/**
	 * ����ļ�·��
	 */
	private String outPath = null;
	
	/**
	 * ����ļ���tomcat�е�λ�ã�������ʵ�Ĵ��룬��Ϊ�˽��tomcat�еĻ�������
	 */
	private String webOutPath = null;
	
	/**
	 * ����ļ���
	 */
	private String outName = null;

	/**
	 * �����ļ��仯���������ļ���������
	 * @param fileChangeInfo
	 */
	public DocumentOrganization(FileChangeInfo fileChangeInfo) {
		projectPath = fileChangeInfo.getProjectLocation();
		String context = projectPath + ConfigUtil.getContext();
		log.debug("context:" + context);
		String location = fileChangeInfo.getLocation();
		log.debug("location:" + location);
		String fileName = ConfigUtil.getFileName();
		log.debug("fileName:" + fileName);
		if(location.endsWith(fileName)) {
			setValues(location);
		}else {
			String temp = location.replace(context, "");
			String fix = temp.substring(0, temp.lastIndexOf("/"));
			String paths = findPath(context, fix);
			setValues(paths);
		}
	}
	
	/**
	 * ����webpack ����
	 * @return
	 */
	public String createWebpackCommand() {
		if(null != entryPath 
				&& null != outName 
				&& null != projectPath
				&& null != outPath) {
			return "node " + ConfigUtil.getWebpackPrefix() + "node_modules/webpack/bin/webpack.js --context " + projectPath + ConfigUtil.getContext() 
					+ " " + entryPath + ConfigUtil.getFileName() + " -o " + projectPath + outPath + outName + " --mode " + ConfigUtil.getModel();
		}
		return null;
	}
	
	/**
	 * ��ȡ����ļ�
	 * @return
	 */
	public String getOutFile() {
		if(null != outPath 
				&& null != outName) {
			return ConfigUtil.getProjectName() + outPath + outName;
		}
		return null;
	}
	
	/**
	 * ��ȡ����ļ��ľ���·��
	 * @return
	 */
	public String getAbsoluteOutFile() {
		if(null != projectPath 
				&& null != outPath 
				&& null != outName) {
			return projectPath + outPath + outName;
		}
		return null;
	}
	
	/**
	 * ����ļ���web�еľ���λ��
	 * @return
	 */
	public String getWebAbsoluteOutFile() {
		if(null != webOutPath 
				&& null != outName) {
			return ConfigUtil.getWorkpath() + ".metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/" + 
					ConfigUtil.getRecentProjectName() + webOutPath + outName;
		}
		return null;
	}
	
	private String findPath(String context, String sep) {
		log.debug("context:" + context);
		log.debug("sep:" + sep);
		String path = context + sep + "/" + ConfigUtil.getFileName();
		File file = new File(path);
		if(file.exists()) {
			return path;
		}
		log.debug(path + "������");
		if("".equals(sep)) {
			return null;
		}else {
			return findPath(context, sep.substring(0, sep.lastIndexOf("/")));
		}
	}
	
	private void setValues(String path) {
		log.debug("path:" + path);
		if(null != path) {
			String temp = path.replace(projectPath + ConfigUtil.getContext(), "");
			String fix = temp.replace(ConfigUtil.getFileName(), "");
			entryPath = "." + fix;
			if("/".equals(fix)) {
				outPath = ConfigUtil.getOutPath() + "/";
				webOutPath = ConfigUtil.getWebFilePath() + "/";
				outName = ConfigUtil.getFileName();
			}else {
				fix = fix.substring(0, fix.lastIndexOf("/"));
				String ftemp = fix.substring(0, fix.lastIndexOf("/") + 1);
				outPath = ConfigUtil.getOutPath() + ftemp;
				webOutPath = ConfigUtil.getWebFilePath() + ftemp;
				outName = fix.substring(fix.lastIndexOf("/") + 1, fix.length()) + ".js";
			}
			log.debug("entryPath:" + entryPath);
			log.debug("outPath:" + outPath);
			log.debug("outName:" + outName);
			log.debug("projectPath:" + projectPath);
		}
	}
	
}
