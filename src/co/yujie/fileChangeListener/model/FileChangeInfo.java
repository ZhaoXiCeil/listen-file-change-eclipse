package co.yujie.fileChangeListener.model;

/**
 * �ļ��޸�������
 * @author yujie
 *
 */
public class FileChangeInfo {

	private String path;  //�ļ�·��
	
	/**
	 * �ļ���������
	 */
	private FileOperate opear;  
	
	private String type;  //�ļ�����

	public FileChangeInfo(String path, FileOperate opear, String type) {
		super();
		this.path = path;
		this.opear = opear;
		this.type = type;
	}

	@Override
	public String toString() {
		return " [path=" + path + ", opear=" + opear + ", type=" + type + "] ";
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public FileOperate getOpear() {
		return opear;
	}

	public void setOpear(FileOperate opear) {
		this.opear = opear;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
