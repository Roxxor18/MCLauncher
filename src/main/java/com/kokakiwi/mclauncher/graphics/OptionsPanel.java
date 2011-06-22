package com.kokakiwi.mclauncher.graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.kokakiwi.mclauncher.LauncherFrame;
import com.kokakiwi.mclauncher.graphics.utils.TransparentLabel;
import com.kokakiwi.mclauncher.utils.Utils;

public class OptionsPanel extends JDialog {
	private static final long serialVersionUID = -8525835533380625378L;
	private LauncherFrame launcherFrame;

	public class AL implements ActionListener {
		public JButton forceButton;

		public AL(JButton forceButton) {
			this.forceButton = forceButton;
		}

		public void actionPerformed(ActionEvent arg0) {
			launcherFrame.config.set("force-update", "true");
			this.forceButton.setText("Will force!");
			this.forceButton.setEnabled(false);
		}

	}

	public OptionsPanel(LauncherFrame parent) {
		super(parent);
		this.launcherFrame = parent;

		setModal(true);

		JPanel panel = new JPanel(new BorderLayout());
		JLabel label = new JLabel("Launcher options", 0);
		label.setBorder(new EmptyBorder(0, 0, 16, 0));
		label.setFont(new Font("Default", 1, 16));
		panel.add(label, "North");

		JPanel optionsPanel = new JPanel(new BorderLayout());
		JPanel labelPanel = new JPanel(new GridLayout(0, 1));
		JPanel fieldPanel = new JPanel(new GridLayout(0, 1));
		optionsPanel.add(labelPanel, "West");
		optionsPanel.add(fieldPanel, "Center");

		JButton forceButton = new JButton("Force update!");
		if(launcherFrame.config.get("force-update") != null)
		{
			forceButton.setEnabled(false);
			forceButton.setText("Will force!");
		}
		forceButton.addActionListener(new AL(forceButton));
		labelPanel.add(new JLabel("Force game update: ", 4));
		fieldPanel.add(forceButton);

		labelPanel.add(new JLabel("Game location on disk: ", 4));
		TransparentLabel dirLink = new TransparentLabel(Utils.getWorkingDirectory(launcherFrame).toString()) {
			private static final long serialVersionUID = 0L;

			public void paint(Graphics g) {
				super.paint(g);

				int x = 0;
				int y = 0;

				FontMetrics fm = g.getFontMetrics();
				int width = fm.stringWidth(getText());
				int height = fm.getHeight();

				if (getAlignmentX() == 2.0F)
					x = 0;
				else if (getAlignmentX() == 0.0F)
					x = getBounds().width / 2 - width / 2;
				else if (getAlignmentX() == 4.0F)
					x = getBounds().width - width;
				y = getBounds().height / 2 + height / 2 - 1;

				g.drawLine(x + 2, y, x + width - 2, y);
			}

			public void update(Graphics g) {
				paint(g);
			}
		};
		dirLink.setCursor(Cursor.getPredefinedCursor(12));
		dirLink.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent arg0) {
				try {
					Utils.openLink(new URL("file://"
							+ Utils.getWorkingDirectory(launcherFrame).getAbsolutePath().replaceAll(" ", "%20")).toURI());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		dirLink.setForeground(new Color(2105599));

		fieldPanel.add(dirLink);

		panel.add(optionsPanel, "Center");

		JPanel buttonsPanel = new JPanel(new BorderLayout());
		buttonsPanel.add(new JPanel(), "Center");
		JButton doneButton = new JButton("Done");
		doneButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				OptionsPanel.this.setVisible(false);
			}
		});
		buttonsPanel.add(doneButton, "East");
		buttonsPanel.setBorder(new EmptyBorder(16, 0, 0, 0));

		panel.add(buttonsPanel, "South");

		add(panel);
		panel.setBorder(new EmptyBorder(16, 24, 24, 24));
		pack();
		setLocationRelativeTo(parent);
	}
}