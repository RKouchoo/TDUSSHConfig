package org.team3132.rkouchoo.main;

import javax.swing.JFrame;
import java.awt.List;
import javax.swing.JTextField;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Set;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class Main {

	private JFrame frmRobotconfigEditor;
	private JTextField dataEntryTextBox;
	private JTextField statusReadoutTextBox;
	private JButton btnUpdate;
	private List jKeyList;
	private JButton btnPushToRobot;
	private JButton btnConnect;
	private JButton btnDisconnect;
	private JLabel lblDataEntry;
	private JLabel lblLocalStatus;
	private JLabel lblAuthor;

	static JSch javaSSH;
	static Session session;

	static InputStream blobIn;
	static JSONObject json;

	/**
	 * Launch the application.
	 */
	public static void main(String[] argss) {
		Main window = new Main();
		window.frmRobotconfigEditor.setVisible(true);
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialiseWindow(); // create the window
		javaSSH = new JSch(); // create and configure ssh library
		try {
			session = javaSSH.getSession(Constants.SSH_USER, Constants.HOST_ADDRESS, Constants.SSH_PORT);
			session.setPassword(Constants.SSH_PASSWORD);
			session.setConfig("StrictHostKeyChecking", "no");
		} catch (JSchException e) {
			statusReadoutTextBox.setText("[ERR]: Failed to initiate SSH library");
			e.printStackTrace(); // send exception to the console, will be a st													range error if this fails
		}
		handleButtons();
	}

	/**
	 * Initialize the contents of the windo.
	 */
	@SuppressWarnings("deprecation")
	private void initialiseWindow() {
		frmRobotconfigEditor = new JFrame();
		frmRobotconfigEditor.setTitle("RobotConfig Editor");
		frmRobotconfigEditor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmRobotconfigEditor.getContentPane().setLayout(null);

		statusReadoutTextBox = new JTextField();
		dataEntryTextBox = new JTextField();

		jKeyList = new List();
		btnUpdate = new JButton("Update locally");
		btnPushToRobot = new JButton("Push to robot");
		btnConnect = new JButton("Connect");
		btnDisconnect = new JButton("Disconnect");

		lblDataEntry = new JLabel("Data Entry:");
		lblLocalStatus = new JLabel("Connection Status: DISCONNECTED");
		lblAuthor = new JLabel("Author: @RKouchoo for Team3132. 2018");

		statusReadoutTextBox.setEditable(false);
		statusReadoutTextBox.setColumns(100);

		jKeyList.setMultipleSelections(false);
		dataEntryTextBox.setColumns(10);

		jKeyList.setBounds(12, 10, 345, 680);
		dataEntryTextBox.setBounds(401, 50, 300, 76);
		frmRobotconfigEditor.setBounds(100, 100, 740, 747);
		btnUpdate.setBounds(401, 133, 132, 25);
		btnPushToRobot.setBounds(401, 160, 132, 25);
		btnConnect.setBounds(391, 609, 149, 62);
		btnDisconnect.setBounds(552, 609, 149, 62);
		lblDataEntry.setBounds(401, 27, 85, 15);
		lblLocalStatus.setBounds(391, 513, 200, 15);
		lblAuthor.setBounds(12, 696, 322, 15);
		statusReadoutTextBox.setBounds(391, 533, 310, 56);

		frmRobotconfigEditor.getContentPane().add(btnUpdate);
		frmRobotconfigEditor.getContentPane().add(btnConnect);
		frmRobotconfigEditor.getContentPane().add(dataEntryTextBox);
		frmRobotconfigEditor.getContentPane().add(btnPushToRobot);
		frmRobotconfigEditor.getContentPane().add(statusReadoutTextBox);
		frmRobotconfigEditor.getContentPane().add(btnDisconnect);
		frmRobotconfigEditor.getContentPane().add(lblDataEntry);
		frmRobotconfigEditor.getContentPane().add(lblLocalStatus);
		frmRobotconfigEditor.getContentPane().add(lblAuthor);
		frmRobotconfigEditor.getContentPane().add(jKeyList);

	}

	/**
	 * Handles all of the logic behind connecting and sending the data to the robot.
	 */
	private void handleButtons() {
		btnUpdate.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent e) {
				// Check if the json file actually contains data and keys are present.
				if (json == null) {
					statusReadoutTextBox.setText("[ERR]: Invalid JSON file! Connect to retrieve data!");
					return;
				}

				statusReadoutTextBox.setText(String.format("[WARN]: Put: %s in key: %s", dataEntryTextBox.getText(),
						jKeyList.getSelectedItem()));
				json.put(jKeyList.getSelectedItem(), dataEntryTextBox.getText());
				dataEntryTextBox.setText(null);
			}
		});

		btnConnect.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent e) {
				try {

					statusReadoutTextBox.setText("[WARN]: Trying to connect to the robot.");

					if (!session.isConnected()) {
						session.connect();
					} else {
						statusReadoutTextBox.setText("[WARN]: Already connected to robot, re-fetching data.");
					}

					// Create a local SFTP connection.
					ChannelSftp sftpConnection = (ChannelSftp) session.openChannel("sftp");
					sftpConnection.connect();

					// Grab the remote file.
					blobIn = sftpConnection.get(Constants.SSH_REMOTE_FILE);
					statusReadoutTextBox.setText(String.format("[WARN]: Got file: %s", Constants.SSH_REMOTE_FILE));

					/**
					 * Begin to convert the json blob into a json object.
					 */

					BufferedReader br = new BufferedReader(new InputStreamReader(blobIn));
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
					java.util.List<String> keysList = new ArrayList<String>();

					while (keyIterator.hasNext()) {
						keysList.add(keyIterator.next());
					}

					// Add the keys to the jlist
					for (int i = 0; i < keysList.size(); i++) {
						jKeyList.add(keysList.get(i));
					}

					br.close();
					sftpConnection.disconnect();

					lblLocalStatus.setText("Connection Status: CONNECTED");

				} catch (Exception ex) {
					statusReadoutTextBox.setText("[ERR]: Failed to get a conneciton to the robot.");
					ex.printStackTrace(); // Sent to the console not the text box
				}
			}
		});

		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (session.isConnected()) {
					session.disconnect();
					statusReadoutTextBox.setText("[WARN]: Disconnected from robot.");
					lblLocalStatus.setText("Connection Status: DISCONNECTED");
				} else {
					statusReadoutTextBox.setText("[WARN]: Not connected to robot, ignoring disconnect.");
				}
			}
		});

		btnPushToRobot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// Check if a SSH session exists, exit if it doesn't.
				if (session == null) {
					statusReadoutTextBox.setText("[ERR]: Not connected to robot!");
					return;
				}

				// Check if the json has been initialized previously and contains data.
				if (json == null) {
					statusReadoutTextBox.setText("[ERR]: No data in JSON file!");
					return;
				}

				// Parse the json blob to make it pretty in-case of manual editing in vi.
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				JsonParser jp = new JsonParser();
				JsonElement je = jp.parse(json.toJSONString());
				String prettyJsonString = gson.toJson(je);

				// Convert the string to a stream to send over ssh.
				InputStream jsonStream = new ByteArrayInputStream(prettyJsonString.getBytes(StandardCharsets.UTF_8));

				// Only try an SFTP connection if SSH is connected.
				if (session.isConnected()) {
					try {
						ChannelSftp sftpConnection = (ChannelSftp) session.openChannel("sftp");
						sftpConnection.connect();
						sftpConnection.put(blobIn, Constants.SSH_REMOTE_FILE + ".save"); // Make a backup copy of the
																							// previous config.
						sftpConnection.put(jsonStream, Constants.SSH_REMOTE_FILE + ".test"); // TODO: REMOVE .test WHEN
																								// TESTED!
						statusReadoutTextBox.setText("[WARN]: Sent file to robot. restart code for changes to apply.");
					} catch (Exception ex) {
						statusReadoutTextBox.setText("[ERR]: Failed to create SFTP connection!.");
						ex.printStackTrace(); // send to console
					}
				} else {
					statusReadoutTextBox.setText("[ERR]: Disconnected from robot, CANNOT PUSH.");
					return;
				}

			}
		});

		jKeyList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// Check if the json has been initialized previously and contains data.
				if (json == null) {
					statusReadoutTextBox.setText("[ERR]: No data in JSON file!");
					return;
				}
				
				Object object = json.get(jKeyList.getSelectedItem());
				String data = (String) object;
				
				dataEntryTextBox.setText(data);
			}
		});
	}
}
