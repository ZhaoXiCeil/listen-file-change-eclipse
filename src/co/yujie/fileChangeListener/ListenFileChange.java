package co.yujie.fileChangeListener;

import org.eclipse.core.internal.events.ResourceDelta;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.apache.log4j.Logger;

import co.yujie.fileChangeListener.iter.Compiler;
import co.yujie.fileChangeListener.iter.Judger;
import co.yujie.fileChangeListener.model.ExtendResourceDelta;
import co.yujie.fileChangeListener.model.FileChangeInfo;
import co.yujie.fileChangeListener.model.FileOperate;
import co.yujie.fileChangeListener.util.ConfigUtil;
import co.yujie.fileChangeListener.util.LogUtil;

/**
 * ��Ŀ����ļ�
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
		
		beforeEarlyStartup();
		
		JavaCore.addElementChangedListener(new IElementChangedListener() {
			
			@Override
			public void elementChanged(ElementChangedEvent event) {
				
				if(event != null && null != PlatformUI.getWorkbench().getActiveWorkbenchWindow()) {
					//FileChangeInfo info = convert(event);
					FileChangeInfo info = convertByWindow(event);
					if(null != info) {
						log.info(info);
						
						/**
						 * //����������ļ����
						 * dothing...
						 */
						
						/**
						 * ִ��һ��js������
						 */
						if(Judger.haveConfig() 
								&& Judger.mateContext(info)) {
							Compiler.compile(info);   //��ʼ����js�ļ�
						}
						
					}
				}
			}
		}, 1);
		
		afterEarlyStartup();
		
		log.debug("����������");
	}
	
	/**
	 * earlyStartup ִ��ǰִ��
	 */
	private void beforeEarlyStartup() {
		
		log.debug("��ʼ  earlyStartup ִ��ǰִ��");
		
		ConfigUtil.loadConfigs();
		
		log.debug("����  earlyStartup ִ��ǰִ��");
		
	}
	
	/**
	 * earlyStartup ִ�к�ִ��
	 */
	private void afterEarlyStartup() {
		
		log.debug("��ʼ  earlyStartup ִ�к�ִ��");
		
		log.debug("����  earlyStartup ִ�к�ִ��");
		
	}
	
	/**
	 * ת��Ϊ�ļ��޸�������Ϣ��
	 * @param event
	 * @return
	 */
	private FileChangeInfo convertByWindow(ElementChangedEvent event) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IEditorPart editor = page.getActiveEditor();
        if(null != editor) {
        	FileEditorInput input = (FileEditorInput) editor.getEditorInput();
        	IFile file = input.getFile();
        	return new FileChangeInfo(file.getFullPath().toString(), getKind(event.getDelta().getKind()), file.getFileExtension(), file.getName(), input.getStorage().toString(), file.getLocation().toString());
        }
        return null;
	}
	
	/**
	 * ת��Ϊ�ļ��޸�������Ϣ��
	 * @param event
	 * @return
	 */
	@SuppressWarnings("unused")
	private FileChangeInfo convert(ElementChangedEvent event) {
		IJavaElementDelta[] deltas = event.getDelta().getAffectedChildren();
		IResourceDelta resource = null;
		if(deltas.length > 0) {
			resource = convertSourece(deltas);
		}else {
			resource = event.getDelta().getResourceDeltas()[0];
		}
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
