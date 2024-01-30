package net.app.utils;

import java.awt.Dimension;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;


@Plugin(name="GuiLogger", category="Core", elementType="appender", printObject=true)
public class GuiLogger extends AbstractAppender  {
	
	private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
	private final Lock readLock = rwLock.readLock();
	
	
	private JDialog dialog;
	private JEditorPane pane;
	
	@SuppressWarnings("deprecation")
	protected GuiLogger(String name, Filter filter,
            Layout<? extends Serializable> layout, final boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
        
        dialog = new JDialog((JFrame)null, "Log", false);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        
		pane = new JEditorPane();
		pane.setEditable(false);
		
		dialog.add(new JScrollPane(pane));
		
		pane.setPreferredSize(new Dimension(300,300));
		dialog.setResizable(false);
		dialog.pack();
		dialog.setVisible(true);
    }

	@Override
	public void append(LogEvent event) {
		readLock.lock();
        try {
            String msg = new String(getLayout().toByteArray(event), StandardCharsets.UTF_8);
            pane.setText(pane.getText() + msg);
        } catch (Exception ex) {
            if (!ignoreExceptions()) {
                throw new AppenderLoggingException(ex);
            }
        } finally {
            readLock.unlock();
        }
	}
	
	@PluginFactory
    public static GuiLogger createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginElement("Filter") final Filter filter,
            @PluginAttribute("otherAttribute") String otherAttribute) {
        if (name == null) {
            LOGGER.error("No name provided for MyCustomAppenderImpl");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        return new GuiLogger(name, filter, layout, true);
    }
}
