package afv2_jml25.gameRoom.view;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComponent;

import java.awt.event.ActionListener;
import java.util.function.Supplier;
import java.awt.event.ActionEvent;

import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import provided.utils.view.TabbedFrame;

/**
 * Class for the View of the mini-MVC
 * 
 * @author Jason Lee
 * @author Andres Villada
 *
 */
public class RoomView extends JPanel {

	/**
	 * For serialization
	 */
	private static final long serialVersionUID = 626309296025271250L;
	/**
	 * View to model adapter for the room-level MVC system
	 */
	IMiniView2ModelAdapter miniView2ModelAdpt;
	/**
	 * panel for title of room
	 */
	private final JPanel titlePanel = new JPanel();
	/**
	 * label for chat room
	 */
	private final JLabel lblChatRoomTitle = new JLabel("New Chat Room");
	/**
	 * quit button
	 */
	private final JButton btnQuit = new JButton("Quit");
	/**
	 * scroll pane
	 */
	private final JScrollPane messageScrollPane = new JScrollPane();
	/**
	 * text area for messages
	 */
	private final JTextArea messageTextArea = new JTextArea();
	/**
	 * panel for message sending controls
	 */
	private final JPanel messageSendPanel = new JPanel();
	/**
	 * text field to send a message
	 */
	private final JTextField messageTextField = new JTextField();
	/**
	 * button to send text message
	 */
	private final JButton btnSendText = new JButton("Send Text");
	/**
	 * Button used to send an image
	 */
	/**
	 * Panel for broadcasting message components
	 */
	private final JPanel pnlBroadcastMsg = new JPanel();
	/**
	 * Text field for typing a message to be broadcasted across all game rooms
	 */
	private final JTextField txtMsgBroadcast = new JTextField();
	/**
	 * Button, when clicked, sends typed message to all game rooms
	 */
	private final JButton btnBroadcast = new JButton("Broadcast");
	/**
	 * Button to send map to players
	 */
	private final JButton btnSendMap = new JButton("Send A Map");

	/**
	 * Create the panel.
	 * 
	 * @param miniView2ModelAdpt room view to room model adapter
	 */
	public RoomView(IMiniView2ModelAdapter miniView2ModelAdpt) {
		txtMsgBroadcast.setToolTipText("Type message to be broadcasted to all rooms");
		txtMsgBroadcast.setColumns(10);
		messageTextField.setText("Enter a text message");
		messageTextField.setColumns(10);
		this.miniView2ModelAdpt = miniView2ModelAdpt;
		initGUI();
	}

	/**
	 * Initializes GUI
	 */
	private void initGUI() {
		setLayout(new BorderLayout(0, 0));

		add(titlePanel, BorderLayout.NORTH);

		titlePanel.add(lblChatRoomTitle);
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				miniView2ModelAdpt.exit();
			}
		});

		titlePanel.add(btnQuit);

		add(messageScrollPane, BorderLayout.CENTER);

		messageScrollPane.setViewportView(messageTextArea);

		add(messageSendPanel, BorderLayout.SOUTH);

		messageSendPanel.add(messageTextField);
		btnSendText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// displayTextMessage(messageTextField.getText());
				miniView2ModelAdpt.sendMessage(messageTextField.getText());
			}
		});

		messageSendPanel.add(btnSendText);
//		btnSendImage.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//
//				miniView2ModelAdpt.sendImage();
//			}
//		});
//		btnSendImage.setToolTipText("Press to send an image to the room");
//
//		messageSendPanel.add(btnSendImage);
		pnlBroadcastMsg.setBorder(
				new TitledBorder(null, "Broadcast Message", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		messageSendPanel.add(pnlBroadcastMsg);

		pnlBroadcastMsg.add(txtMsgBroadcast);
		btnBroadcast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				miniView2ModelAdpt.broadcastMessage(txtMsgBroadcast.getText());
			}
		});
		btnBroadcast.setToolTipText("Click to broadcast the typed message");

		pnlBroadcastMsg.add(btnBroadcast);
		btnSendMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				miniView2ModelAdpt.sendMap();
			}
		});
		btnSendMap.setToolTipText("Button used to send a map in this team/chatroom.");

		messageSendPanel.add(btnSendMap);
	}

	/**
	 * Starts the view
	 */
	public void start() {

	}

	/**
	 * Display text on the message text area.
	 * 
	 * @param text message to display
	 */
	public void displayTextMessage(String text) {
		// TODO Auto-generated method stub
		this.messageTextArea.append(text + "\n");
	}

	/**
	 * Displays component onto area for messages in chat rooms.
	 * 
	 * @param label     Label for the component
	 * @param component GUI component to add
	 * @return executable code invoked on the new component
	 */
	public Runnable displayJComponent(String label, Supplier<JComponent> component) {

		Runnable newRunnable = new Runnable() {
			@Override
			public void run() {
				TabbedFrame gameFrame = new TabbedFrame(label);
				gameFrame.setBounds(12, 12, 500, 500);
				gameFrame.setSize(500, 500);
				gameFrame.setMinimumSize(gameFrame.getSize());
				gameFrame.addComponentFac(label, component);
				gameFrame.start();
				gameFrame.setDefaultCloseOperation(TabbedFrame.DISPOSE_ON_CLOSE);
				validate();
			}
		};
		SwingUtilities.invokeLater(newRunnable); // run on the GUI thread
		return newRunnable;

	}

}
