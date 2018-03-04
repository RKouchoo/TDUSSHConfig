package org.team3132.rkouchoo.main;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class Main {

	static JSch javaSSH;
	static Session session;
	
	static JSONObject json;
	
	private JFrame frame;

	private JFrame frmRobotconfigEditor;
	private JTextField DataEntryTextBox;
	private JTextField statusReadoutTextBox;

	
	public static void main(String[] args) {
		try {
		
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						Main main = new Main();
						main.frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			/**
			 * Set up classes and connection properties.
			 */
			javaSSH = new JSch();
			session = javaSSH.getSession(Constants.SSH_USER, Constants.HOST_ADDRESS, Constants.SSH_PORT);
			session.setPassword(Constants.SSH_PASSWORD); // Not needed but good to have.
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			
			ChannelSftp sftpConnection = (ChannelSftp) session.openChannel("sftp");
			
			sftpConnection.connect();
			
			
			/**
			 * Finally connect when everything is configured.
			 */
			InputStream out = sftpConnection.get(Constants.SSH_REMOTE_FILE);
			System.out.println(String.format("Got file: %s", Constants.SSH_REMOTE_FILE));
					
			/**
			 * Start to use the JSON file.
			 */
			BufferedReader br = new BufferedReader(new InputStreamReader(out));
			StringBuilder s = new StringBuilder();
			
			String line;
			
			while ((line = br.readLine()) != null) {
				s.append(line);
			}
			
			String JSONDump = s.toString();
			JSONParser parser = new JSONParser();
			json = (JSONObject) parser.parse(JSONDump);
			Set<String> keys = json.keySet();
			java.util.Iterator<String> keyIterator = keys.iterator();
			List<String> keysList = new ArrayList<String>();
			
			while (keyIterator.hasNext()) {
				keysList.add(keyIterator.next());
			}
			
			/**
			 * Close all connections to the robot.
			 */
			br.close();
			sftpConnection.disconnect();		
			session.disconnect();			
		
		} catch (Exception e) {			
			System.err.println("failed to create an SSH or SFTP connection.");
			e.printStackTrace();
		}
		
	}
	
	public Main() {
	}

}
