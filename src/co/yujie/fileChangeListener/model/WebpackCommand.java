package co.yujie.fileChangeListener.model;

import co.yujie.fileChangeListener.util.ConfigUtil;

public class WebpackCommand {
	
	/**
	 * webpack ��װ·��ǰ׺
	 */
	private String webpackPrefix = ConfigUtil.getWebpackPrefix();
	
	/**
	 * �����ļ�·��
	 */
	private String entryPath = ConfigUtil.getContext();
	
	/**
	 * �����ļ���
	 */
	private String entryName;
	
	/**
	 * ����ļ�·��
	 */
	private String outPath = ConfigUtil.getOutPath();
	
	/**
	 * ����ļ�·��
	 */
	private String outName;
	
	/**
	 * ����ģʽ ����ģʽ/����ģʽ
	 */
	private String model = ConfigUtil.getModel();
	
	/**
	 * ��������
	 * @return
	 */
	public String getCommand() {
		if(null != webpackPrefix 
				&& null != entryPath 
				&& null != entryName 
				&& null != outName 
				&& null != outPath 
				&& null != model) {
			return "node " + webpackPrefix + "node_modules/webpack/bin/webpack.js --context " + entryPath 
					+ " ./" + entryName + " -o " + outPath + outName + " --mode " + model;
		}
		return null;
	}

}
