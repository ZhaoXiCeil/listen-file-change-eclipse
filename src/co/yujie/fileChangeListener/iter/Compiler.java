package co.yujie.fileChangeListener.iter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import co.yujie.fileChangeListener.model.FileChangeInfo;

/**
 * ������
 * @author yujie
 *
 */
public final class Compiler {
	
	private final static Compiler compiler = new Compiler();
	
	private final static ExecutorService executor = Executors.newFixedThreadPool(1);
	
	private final static Object statusLock = new Object();
	
	private final static Object remainLock = new Object();
	
	private Compiler() {
		
	}
	
	/**
	 * ������״̬��true -> ���ڱ���
	 */
	private static boolean status = Boolean.FALSE;
	
	/**
	 * �Ƿ���ʣ��δ����
	 */
	private static boolean remain = Boolean.FALSE;
	
	/**
	 * ����
	 * @param info �ļ��仯��Ϣ����
	 */
	public static void compile(FileChangeInfo info) {
		if(shouldCompile()) {
			executor.submit(() -> {
				setStatus(Boolean.TRUE);
				IAction action = new JsAction();
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("compiler", compiler);
				data.put("fileChangeInfo", info);
				action.execute(data);
			});
		}
	}
	
	/**
	 * �ж��Ƿ�Ӧ�ý��б���
	 * @return
	 */
	private static boolean shouldCompile() {
		if(!isStatus()) {
			return true;
		}else {
			if(!isRemain()) {
				setRemain(Boolean.TRUE);
			}
		}
		return false;
	}

	private static boolean isStatus() {
		synchronized (statusLock) {
			return status;
		}
	}

	private static void setStatus(boolean status) {
		synchronized (statusLock) {
			Compiler.status = status;
		}
	}

	private static boolean isRemain() {
		synchronized (remainLock) {
			return remain;
		}
	}

	private static void setRemain(boolean remain) {
		synchronized (remainLock) {
			Compiler.remain = remain;
		}
	}

}
