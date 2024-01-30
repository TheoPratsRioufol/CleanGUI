package net.app;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class WorkspaceDialog extends JDialog {
	
	private JComboBox<String> combo;
	private JButton launch;

	public WorkspaceDialog(App app) {
		super((JFrame)null, "Select workspace", true);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		
		JPanel panel = new JPanel();
		
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = gbc.gridy = 0;
		gbc.gridwidth = gbc.gridheight = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);

		gbc.gridwidth = 2;		
		panel.add(new JLabel("Select workspace"), gbc);
		
		combo = new JComboBox<String>(new Vector<>(app.getConfig().getPreviousWorkSpaces()));
		combo.setEditable(true);
		
		gbc.gridy++;
		panel.add(combo, gbc);
		
		JButton browse = new JButton("Browse");
		browse.setIcon(app.getIconAtlas().getIcon("browse", 16));
		browse.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int choice = chooser.showOpenDialog(this);
			if (choice == JFileChooser.APPROVE_OPTION) {
				combo.setSelectedItem(chooser.getSelectedFile().getAbsolutePath());
				combo.revalidate();
				combo.repaint();
			}
		});
		
		gbc.gridy++;
		panel.add(browse, gbc);
		
		launch = new JButton("Launch");
		launch.addActionListener(e -> {
			File target = new File(combo.getSelectedItem().toString());
			if (target.exists()) {
				setVisible(false);
				dispose();	
			} else {
				combo.setBorder(BorderFactory.createLineBorder(Color.RED));
				Toolkit.getDefaultToolkit().beep();
			}
		});
		
		gbc.gridy++;
		gbc.gridwidth = 1;
		panel.add(launch, gbc);
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(e -> {
			setVisible(false);
			dispose();
			System.exit(0);
		});
		
		gbc.gridx++;
		panel.add(cancel, gbc);
		
		refresh();
		
		combo.addActionListener(e -> refresh());
		
		combo.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				refresh();
				panel.repaint();
			}
		});
		
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
	}
	
	public String getSelectedWorkingDir() {
		return (String) combo.getSelectedItem();
	}
	
	private void refresh() {
		String comboTxt = (String)combo.getEditor().getItem();
		launch.setEnabled((comboTxt != null) && (comboTxt.length() > 0));
		launch.repaint();
	}
	
}
