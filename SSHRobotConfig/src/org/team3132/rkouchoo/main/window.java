package org.team3132.rkouchoo.main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JList;
import java.awt.List;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class window {

	private JFrame frmRobotconfigEditor;
	private JTextField DataEntryTextBox;
	private JTextField statusReadoutTextBox;

	/**
	 * Launch the application.
	 */
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window window = new window();
					window.frmRobotconfigEditor.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public window() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("deprecation")
	private void initialize() {
		frmRobotconfigEditor = new JFrame();
		frmRobotconfigEditor.setTitle("RobotConfig Editor");
		frmRobotconfigEditor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmRobotconfigEditor.getContentPane().setLayout(null);
		
		statusReadoutTextBox = new JTextField();
		DataEntryTextBox = new JTextField();
		
		List keyList = new List();
		JButton btnUpdate = new JButton("Update locally");
		JButton btnPushToRobot = new JButton("Push to robot");
		JButton btnConnect = new JButton("Connect");
		JButton btnDisconnect = new JButton("Disconnect");
		JLabel lblDataEntry = new JLabel("Data Entry:");
		JLabel lblLocalStatus = new JLabel("Local status:");
		JLabel lblAuthor = new JLabel("Author: @RKouchoo for Team3132.");
		
		frmRobotconfigEditor.getContentPane().add(btnUpdate);
		statusReadoutTextBox.setEditable(false);
		statusReadoutTextBox.setColumns(10);

		keyList.setMultipleSelections(false);
		DataEntryTextBox.setColumns(10);
		
		keyList.setBounds(12, 10, 345, 680);
		DataEntryTextBox.setBounds(401, 50, 300, 76);
		frmRobotconfigEditor.setBounds(100, 100, 740, 747);	
		btnUpdate.setBounds(401, 133, 132, 25);
		btnPushToRobot.setBounds(401, 160, 132, 25);	
		btnConnect.setBounds(391, 609, 149, 62);
		btnDisconnect.setBounds(552, 609, 149, 62);
		lblDataEntry.setBounds(401, 27, 85, 15);
		lblLocalStatus.setBounds(391, 513, 98, 15);
		lblAuthor.setBounds(12, 696, 322, 15);
		statusReadoutTextBox.setBounds(391, 533, 310, 56);
		
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
	
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
	
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		btnPushToRobot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		frmRobotconfigEditor.getContentPane().add(btnConnect);
		frmRobotconfigEditor.getContentPane().add(DataEntryTextBox);
		frmRobotconfigEditor.getContentPane().add(btnPushToRobot);
		frmRobotconfigEditor.getContentPane().add(statusReadoutTextBox);
		frmRobotconfigEditor.getContentPane().add(btnDisconnect);
		frmRobotconfigEditor.getContentPane().add(lblDataEntry);
		frmRobotconfigEditor.getContentPane().add(lblLocalStatus);
		frmRobotconfigEditor.getContentPane().add(lblAuthor);
		frmRobotconfigEditor.getContentPane().add(keyList);
	}
}
