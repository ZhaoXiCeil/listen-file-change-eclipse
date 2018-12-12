package co.yujie.fileChangeListener;

import org.eclipse.core.internal.events.ResourceDelta;
import org.eclipse.core.internal.events.ResourceDeltaFactory;
import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.IStartup;

import java.util.Date;

import org.apache.log4j.Logger;

import co.yujie.fileChangeListener.model.ExtendResourceDelta;
import co.yujie.fileChangeListener.model.FileChangeInfo;
import co.yujie.fileChangeListener.model.FileOperate;
import co.yujie.fileChangeListener.util.LogUtil;

/**
 *    ��Ŀ����ļ�
 * @author yujie
 *
 */
public class ListenFileChange implements IStartup {
	
	private static Logger log = LogUtil.getLog(ListenFileChange.class);

	/**
	 * ��eclipse����ʱ��������������ִ�д˷�����
	 * ����ĵ��޸ĵļ���
	 */
	@Override
	public void earlyStartup() {

		log.debug("��ʼ���ز��");
		
		JavaCore.addElementChangedListener(new IElementChangedListener() {
			
			@Override
			public void elementChanged(ElementChangedEvent event) {
				
				System.out.println("----event: " + event);
				IJavaElementDelta d = event.getDelta();
				System.out.println("-----source: " + d);
				if(event != null && event.getType() == 1) {  //type Ϊ1ʱ��ʾ�����ļ�
					FileChangeInfo info = convert(event);
					if(null != info) {
						log.info(info);
					}
				}
			}
		});
		
		log.debug("����������");
	}
	
	/**
	 * ת��Ϊ�ļ��޸�������Ϣ��
	 * @param event
	 * @return
	 */
	private FileChangeInfo convert(ElementChangedEvent event) {
		IJavaElementDelta[] deltas = event.getDelta().getAffectedChildren();
		IResourceDelta resource = null;
		if(deltas.length > 0) {
			resource = convertSourece(deltas);
		}else {
			resource = event.getDelta().getResourceDeltas()[0];
		}
		System.out.println("--------e: " + resource);
		if(resource != null) {
			IPath path = resource.getFullPath();
			FileChangeInfo info = new FileChangeInfo(path.toString(), getKind(resource.getKind()), path.getFileExtension());
			return info;
		}
		return null;
	}
	
	private IResourceDelta convertSourece(IJavaElementDelta[] deltas) {
		for (int i = 0; i < deltas.length; i++) {
			IJavaElementDelta item = deltas[i];
			IJavaElementDelta[] list = item.getAffectedChildren();
			if(list.length > 0) {
				return convertSourece(list);
			}else if(item != null) {
				if(item.getResourceDeltas() != null) {
					return item.getResourceDeltas()[0];
				}else {
					return new ExtendResourceDelta(item.getElement().getPath(), item.getKind());
				}
			}
		}
		return null;
	}
	
	@SuppressWarnings("restriction")
	private FileOperate getKind(int code) {
		switch(code) {
			case ResourceDelta.ADDED:
				return FileOperate.ADD;
			case ResourceDelta.CHANGED:
				return FileOperate.CHANGE;
			case ResourceDelta.REMOVED:
				return FileOperate.REMOVE;
		}
		return null;
	}

}
