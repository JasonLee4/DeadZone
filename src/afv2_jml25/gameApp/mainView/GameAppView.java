package afv2_jml25.gameApp.mainView;

import java.util.HashSet;
import java.util.function.Supplier;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import afv2_jml25.api.GameRoom;
import common.serverObj.INamedAppConnection;
import common.serverObj.INamedRoomID;
import provided.logger.ILoggerControl;
import provided.utils.view.TabbedPanel;

import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;

/**
 * Class for view for app
 * 
 * @author Jason Lee
 * @author Andres Villada
 *
 */
public class GameAppView extends JFrame {

	/**
	 * For serialization
	 */
	private static final long serialVersionUID = -1791435552414751784L;

	/**
	 * adapter from app view to app model
	 */
	IMainView2ModelAdapter mainView2ModelAdpt;

	/**
	 * content panel
	 */
	private JPanel contentPane;
	/**
	 * panel for controls
	 */
	private final JPanel controlPanel = new JPanel();

	/**
	 * chatroom list panel
	 */
	private final TabbedPanel teamRoom = new TabbedPanel("Chat Rooms", ILoggerControl.getSharedLogger(), true);
	/**
	 * panel for connection controls
	 */
	private final JPanel connectPanel = new JPanel();
	/**
	 * label for panel for connection controls
	 */
	private final JLabel lblConnect = new JLabel("Connect to a FinalProjectAPP!");
	/**
	 * text field chat room name
	 */
	private final JTextField chatRoomTxt = new JTextField();
	/**
	 * connection button
	 */
	private final JButton btnConnect = new JButton("Connect");
	/**
	 * Controls related to chat rooms
	 */
	private final JPanel roomManagerPanel = new JPanel();
	/**
	 * label for chatrooms controls panel
	 */
	private final JLabel lblManage = new JLabel("Manage Team Rooms");
	/**
	 * button to create rooms
	 */
	private final JButton btnCreateRoom = new JButton("Create Team room");
	/**
	 * quit app button
	 */
	private final JButton btnQuit = new JButton("Quit");
	/**
	 * button to join a room
	 */
	private final JButton btnJoinRoom = new JButton("Join selected room");
	/**
	 * room list
	 */
	private final JComboBox<GameRoom> currRoomList = new JComboBox<GameRoom>();
	/**
	 * panel for room and users display
	 */
	private final JPanel roomUserDisplayPanel = new JPanel();
	/**
	 * label for chat rooms info panel
	 */
	private final JLabel lblChatInfo = new JLabel("Connected Hosts");
	/**
	 * text field for creating chat room with name
	 */
	private final JTextField createRoomTextField = new JTextField();
	/**
	 * Text field for remote connection bound names.
	 */
	private final JTextField boundNametxtField = new JTextField();
	/**
	 * Drop list of connected hosts
	 */
	private final JComboBox<INamedAppConnection> connectedHostsComboBox = new JComboBox<INamedAppConnection>();
	/**
	 * Button used to send a forced invite to selected connection.
	 */
	private final JButton btnForcedInvite = new JButton("Forced Invite");
	/**
	 * Button to start the game
	 */
	private final JButton btnStartGame = new JButton("Game Time!");
	/**
	 * Panel for the "Connect to a FinalProjectAPP!" label
	 */
	private final JPanel pnlConnLbl = new JPanel();
	/**
	 * Panel for the "Manage Team Rooms" label
	 */
	private final JPanel pnlManageLbl = new JPanel();
	/**
	 * Panel for the "Connected Hosts" label
	 */
	private final JPanel pnlHostsLbl = new JPanel();
	/**
	 * Panel for room requests and joining functionality
	 */
	private final JPanel pnlRoomRequest = new JPanel();
	/**
	 * Title for panel for room requests and joining
	 */
	private final JLabel lblRequestPickRoom = new JLabel("Request/Pick Room");
	/**
	 * Button that, when clicked, requests the chat rooms from the server
	 */
	private final JButton btnRequestRooms = new JButton("Request Server Rooms");
	/**
	 * Combo box that displays the team rooms the server is connected to
	 */
	private final JComboBox<INamedRoomID> comboServerRooms = new JComboBox<INamedRoomID>();
	/**
	 * Button to join the selected room from the server list
	 */
	private final JButton btnJoinServerRoom = new JButton("Join Room");

	/**
	 * Create the frame.
	 * 
	 * @param viewAdpt app view to model adapter
	 */
	public GameAppView(IMainView2ModelAdapter viewAdpt) {
		boundNametxtField.setToolTipText("Enter a bound name");
		boundNametxtField.setText("Bound name");
		boundNametxtField.setColumns(10);
		createRoomTextField.setText("Team 1");
		createRoomTextField.setColumns(10);
		this.mainView2ModelAdpt = viewAdpt;
		chatRoomTxt.setToolTipText("Enter an IP address");
		chatRoomTxt.setText("IP Address");
		chatRoomTxt.setColumns(10);
		initGUI();
	}

	/**
	 * Start the view
	 */
	public void start() {
		setVisible(true);
	}

	/**
	 * Initialize the GUI
	 */
	private void initGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 729, 438);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		controlPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "App Controls",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

		contentPane.add(controlPanel, BorderLayout.NORTH);
		btnStartGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				mainView2ModelAdpt.genGame((GameRoom) currRoomList.getSelectedItem());
				mainView2ModelAdpt.genGame();
			}
		});
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
		btnStartGame.setToolTipText("Click to start game");

		controlPanel.add(btnStartGame);

		controlPanel.add(connectPanel);
		connectPanel.setLayout(new GridLayout(0, 1, 0, 0));

		connectPanel.add(pnlConnLbl);
		pnlConnLbl.add(lblConnect);

		connectPanel.add(chatRoomTxt);
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainView2ModelAdpt.connectToRoom(chatRoomTxt.getText());
			}
		});

		connectPanel.add(boundNametxtField);
		btnConnect.setToolTipText("Connect to a chat room");

		connectPanel.add(btnConnect);
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				mainView2ModelAdpt.quit();
			}
		});
		btnQuit.setToolTipText("Press this button to quit the application");

		connectPanel.add(btnQuit);

		controlPanel.add(roomManagerPanel);
		roomManagerPanel.setLayout(new GridLayout(0, 1, 0, 0));
		pnlManageLbl.setForeground(Color.BLACK);

		roomManagerPanel.add(pnlManageLbl);
		pnlManageLbl.add(lblManage);
		lblManage.setAlignmentY(Component.TOP_ALIGNMENT);
		btnCreateRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				mainView2ModelAdpt.createChatRoom(createRoomTextField.getText());

			}
		});
		btnCreateRoom.setToolTipText("Press this button to create a new chat room");

		roomManagerPanel.add(btnCreateRoom);

		roomManagerPanel.add(createRoomTextField);

		roomManagerPanel.add(currRoomList);
		btnJoinRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainView2ModelAdpt.joinRoom((GameRoom) currRoomList.getSelectedItem());
			}
		});

		roomManagerPanel.add(btnJoinRoom);

		controlPanel.add(pnlRoomRequest);
		pnlRoomRequest.setLayout(new GridLayout(0, 1, 0, 0));

		pnlRoomRequest.add(lblRequestPickRoom);
		btnRequestRooms.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainView2ModelAdpt.requestRooms((INamedAppConnection) connectedHostsComboBox.getSelectedItem());
			}
		});
		btnRequestRooms.setToolTipText("Click to get rooms of server");

		pnlRoomRequest.add(btnRequestRooms);
		comboServerRooms.setToolTipText("Select a room from the server to join");

		pnlRoomRequest.add(comboServerRooms);
		btnJoinServerRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainView2ModelAdpt.joinServerRoom((INamedRoomID) comboServerRooms.getSelectedItem());
			}
		});
		btnJoinServerRoom.setToolTipText("Click here to join this server's room");

		pnlRoomRequest.add(btnJoinServerRoom);

		controlPanel.add(roomUserDisplayPanel);
		roomUserDisplayPanel.setLayout(new GridLayout(0, 1, 0, 0));

		roomUserDisplayPanel.add(pnlHostsLbl);
		pnlHostsLbl.add(lblChatInfo);

		roomUserDisplayPanel.add(connectedHostsComboBox);
		btnForcedInvite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				mainView2ModelAdpt.inviteToRoom((GameRoom) currRoomList.getSelectedItem(),
						(INamedAppConnection) connectedHostsComboBox.getSelectedItem());

			}
		});
		btnForcedInvite.setToolTipText("Invite selected host to a chat room");

		roomUserDisplayPanel.add(btnForcedInvite);
		teamRoom.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Team Room",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

		contentPane.add(teamRoom, BorderLayout.CENTER);
	}

	/**
	 * Adds view of chat room to app GUI
	 * 
	 * @param name     name of chat room
	 * @param roomView supplier for GUI component
	 * @return runnable from adding component
	 */
	public Runnable installRoomView(String name, Supplier<JComponent> roomView) {
		return teamRoom.addComponentFac(name, roomView, true);
	}

	/**
	 * Add discovery server components to app view.
	 * 
	 * @param comp discovery component
	 */
	public void addDiscoveryComponent(JComponent comp) {
		controlPanel.add(comp);
		validate(); // re-runs the frame's layout manager to account for the newly added component
		pack();
	}

	/**
	 * Add room to drop list of rooms
	 * 
	 * @param room a chat room
	 */
	public void addRoomToList(GameRoom room) {
		this.currRoomList.insertItemAt(room, 0);
	}

	/**
	 * add connected user/host to drop list
	 * 
	 * @param remoteDyad remote connection/ user to add to list
	 */
	public void addRemoteHost(INamedAppConnection remoteDyad) {
		this.connectedHostsComboBox.insertItemAt(remoteDyad, 0);
	}

	/**
	 * Update combo box the new list of rooms connected to the server
	 * 
	 * @param roomsFromServer list of server rooms
	 */
	public void updateServerRooms(HashSet<INamedRoomID> roomsFromServer) {
		for (INamedRoomID room : roomsFromServer) {
			comboServerRooms.insertItemAt(room, 0);
		}
	}

}
