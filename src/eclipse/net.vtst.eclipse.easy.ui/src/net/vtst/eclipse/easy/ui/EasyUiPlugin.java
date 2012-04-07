package net.vtst.eclipse.easy.ui;

import net.vtst.eclipse.easy.ui.messages.EasyBundleMessages;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class EasyUiPlugin extends AbstractUIPlugin implements BundleActivator {

	private static BundleContext context;
	private static EasyUiPlugin instance;
	private EasyUiMessages messages = new EasyUiMessages();

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		EasyUiPlugin.context = bundleContext;
		instance = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		EasyUiPlugin.context = null;
	}
	
	public EasyBundleMessages getMessages() {
	  return messages;
	}
	
	public static EasyUiPlugin getDefault() {
	  return instance;
	}

  public Shell getShell() {
    return this.getWorkbench().getActiveWorkbenchWindow().getShell();
  }

}
