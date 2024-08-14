package afv2_jml25.gameInstance.view;

import java.awt.BorderLayout;
import java.awt.TextField;
import java.awt.Window;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import javax.swing.SwingConstants;

/**
 * View of the game-level MVC system
 * 
 * @author Andres Villada (afv2)
 * @author Jason Lee (jml25)
 */
public class GameMicroView extends JPanel {

	/**
	 * For serialization
	 */
	private static final long serialVersionUID = 46105026135368755L;
	/**
	 * View to model adapter for the game-level MVC system
	 */
	private IGameMicroView2ModelAdapter gameView2ModelAdpt;
	/**
	 * Panel for displaying map
	 */
	private final JPanel pnlMap = new JPanel();
	/**
	 * Panel for map controls
	 */
	private final JPanel pnlControls = new JPanel();
	/**
	 * Button to quit the game
	 */
	private final JButton btnQuit = new JButton("Quit");
	/**
	 * Panel for player game controls
	 */
	private final JPanel pnlGameControls = new JPanel();
	/**
	 * Text field for a user to specify their country guess
	 */
	private final JTextField countryTextField = new JTextField();
	/**
	 * Button to submit country guess
	 */
	private final JButton countrySubmitButton = new JButton("Enter Country Name");
	/**
	 * Panel for the dynamic game status
	 */
	private final JPanel pnlGameStatus = new JPanel();
	/**
	 * Label indicating the time left for the round
	 */
	private final JLabel lblTimer = new JLabel("Timer");
	/**
	 * Panel for holding the quit button to leave the game
	 */
	private final JPanel pnlQuit = new JPanel();
	/**
	 * Label indicating the player's status in the game
	 */
	private final JLabel lblGameStatus = new JLabel("Status");

	/**
	 * Create the frame.
	 * 
	 * @param iGameMicroView2ModelAdapter view to model adapter for the game-level
	 *                                    MVC system
	 */
	public GameMicroView(IGameMicroView2ModelAdapter iGameMicroView2ModelAdapter) {
		countryTextField.setToolTipText("Text field to enter country name into.");
		countryTextField.setText("Country");
		countryTextField.setColumns(10);
		countryTextField.setEditable(true);
		lblGameStatus.setText("Alive");
		this.gameView2ModelAdpt = iGameMicroView2ModelAdapter;
		setBounds(100, 100, 450, 300);

		initGUI();
	}

	/**
	 * Initializes the GUI
	 */
	private void initGUI() {
		setLayout(new BorderLayout(0, 0));
		pnlMap.setBorder(new TitledBorder(null, "Map", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		this.add(pnlMap, BorderLayout.CENTER);
		pnlMap.setLayout(new BorderLayout(0, 0));
		pnlControls.setBorder(new TitledBorder(null, "Controls", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		this.add(pnlControls, BorderLayout.SOUTH);
		pnlControls.setLayout(new GridLayout(1, 3, 0, 0));
		pnlGameStatus.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Game Status",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

		pnlControls.add(pnlGameStatus);
		pnlGameStatus.setLayout(new GridLayout(2, 1, 0, 0));
		lblTimer.setHorizontalAlignment(SwingConstants.CENTER);

		pnlGameStatus.add(lblTimer);
		lblGameStatus.setHorizontalAlignment(SwingConstants.CENTER);

		pnlGameStatus.add(lblGameStatus);
		pnlGameControls
				.setBorder(new TitledBorder(null, "Controls", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlGameControls.setToolTipText("Panel for player input controls.");

		pnlControls.add(pnlGameControls);
		pnlGameControls.setLayout(new GridLayout(2, 1, 0, 0));

		pnlGameControls.add(countryTextField);
		countrySubmitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!countryTextField.getText().equals("") || !countryTextField.getText().equals(null)) {
					// send input country name to server or the game logic
					// disable button after one correct submission
					gameView2ModelAdpt.sendCountry(countryTextField.getText());

				}
			}
		});
		countrySubmitButton.setToolTipText("Button to submit name of country.");

		pnlGameControls.add(countrySubmitButton);
		pnlQuit.setBorder(
				new TitledBorder(null, "Quit Game Button", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		pnlControls.add(pnlQuit);
		pnlQuit.setLayout(new GridLayout(0, 1, 0, 0));
		pnlQuit.add(btnQuit);
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				gameView2ModelAdpt.quit();
				closeGameWindow(e);
			}
		});
		btnQuit.setToolTipText("Click here to quit the game");
		pnlControls.setVisible(getFocusTraversalKeysEnabled());
	}

	/**
	 * Starts the view by making the GUI visible
	 */
	public void start() {
		setVisible(true);
	}

	/**
	 * Adds the map factory component to the view
	 * 
	 * @param mapFactory map factory component
	 * @param string     label for this map factory component
	 */
	public void addComponent(Supplier<JComponent> mapFactory, String string) {
		SwingUtilities.invokeLater(() -> {
			pnlMap.add(mapFactory.get(), BorderLayout.CENTER); // The component factory is run on
																// the GUI
																// thread!
																// frameGame.setTitle(string); // Probably wouldn't need
																// a title if this view was a panel being added to a
																// client GUI.
			pnlMap.revalidate(); // force the parent component to re-layout its child components since a new one
									// was added.
			pnlMap.repaint(); // request that the parent repaint itself.
		});
	}

	/**
	 * Enables or disables game controls depending on the boolean passed in.
	 * 
	 * @param val boolean if true, enables controls. Otherwise, disables controls
	 */
	public void enableInputAndButton(boolean val) {
		this.countrySubmitButton.setEnabled(val);
		this.countryTextField.setEnabled(val);
		this.countryTextField.setEditable(val);
	}

	/**
	 * Closes the game window.
	 * 
	 * @param e action performed on a component
	 */
	public void closeGameWindow(ActionEvent e) {
		JComponent comp = (JComponent) e.getSource();
		Window win = SwingUtilities.getWindowAncestor(comp);
		win.dispose();
	}

	/**
	 * Generates a panel that indicates a player has been eliminated from the game
	 */
	public void genLossPanel() {
		pnlMap.removeAll();
		pnlMap.revalidate();
		pnlMap.repaint();
		TextField lText = new TextField();
		lText.setText("L");
		pnlMap.add(lText);
	}

	/**
	 * Updates the game status
	 */
	public void roundTimer() {
		AtomicInteger secs = new AtomicInteger(35);
		final int TIMER_DELAY = 1000;
		Timer timer = new Timer(TIMER_DELAY, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (secs.get() <= 0) {
					((Timer) e.getSource()).stop();
					secs.set(35);
					// secs = 35;
				}
				lblTimer.setText(Integer.toString(secs.addAndGet(-1)));
			}
		});
		timer.start();
	}

	/**
	 * Updates the game status of the player
	 * 
	 * @param status new status to be displayed
	 */
	public void updateGameStatus(String status) {
		lblGameStatus.setText(status);
	}
}
