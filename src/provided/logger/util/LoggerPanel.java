package provided.logger.util;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import provided.logger.ILogEntry;
import provided.logger.ILogEntryFormatter;

import java.awt.BorderLayout;
import java.util.Objects;

import javax.swing.JTextArea;
import javax.swing.JScrollPane;

/**
 * Simple display panel for logger output.  This panel implements the IStringLogEntryProcessor
 * and hence also ILogEntryProcessor, to facilitate its use by ILoggers.
 * Note that this does not extend AStringLogEntryrocessor because of Java's multiple inheritance restrictions.
 * @author swong
 *
 */
public class LoggerPanel extends JPanel implements IStringLogEntryProcessor {

	/**
	 * The title of the panel shown on its titled border
	 */
	private String title;

	/**
	 * For serialization
	 */
	private static final long serialVersionUID = -6686909804641745363L;

	/**
	 * The log entry formatter in use.  This has to be explicitly implemented here because
	 * this JPanel cannot also extend AStringLogEntryProcessor.   
	 * This could be done with as a decorated AStringLogEntryProcessor but was not deemed 
	 * worth the trouble of its benefits. 
	 */
	private ILogEntryFormatter logEntryFormatter = ILogEntryFormatter.DEFAULT;

	/**
	 * Teh scrollbars around the text area
	 */
	private final JScrollPane spnDisplay = new JScrollPane();

	/**
	 * The text display area
	 */
	private final JTextArea taDisplay = new JTextArea();

	/**
	 * Create the panel with a given title
	 * @param title The border title used to identify this component on the GUI
	 */
	public LoggerPanel(String title) {
		Objects.requireNonNull(title, "[LoggerPanel constructior] The panel's title must be non-null.");
		this.title = title;
		initGUI();
	}

	/**
	 * Uses a default title for the panel's border.
	 */
	public LoggerPanel() {
		this("Logger Output");
	}

	/**
	 * Initialize the panel's GUI elements
	 */
	private void initGUI() {
		setBorder(new TitledBorder(null, title, TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setLayout(new BorderLayout(0, 0));

		add(spnDisplay, BorderLayout.CENTER);

		spnDisplay.setViewportView(taDisplay);
	}

	/**
	 * Add a log entry to the display using the current log entry formatting function
	 * @param logEntry The log entry to append to the display 
	 */
	public void accept(ILogEntry logEntry) {
		addMsg(logEntryFormatter.apply(logEntry) + "\n");
	}

	/**
	 * Add an already formatted message (linefeed NOT automatically added)
	 * @param fullMsg  The message to display, already formatted as desired, including a trailing linefeed if needed.
	 */
	public void addMsg(String fullMsg) {
		taDisplay.append(fullMsg);
	}

	@Override
	public void setLogEntryFormatter(ILogEntryFormatter leFormatter) {
		Objects.requireNonNull(leFormatter,
				"[LoggerPanel.setLogEntryFormatter()] The log entry formatter must be non-null.");
		this.logEntryFormatter = leFormatter;
	}

	@Override
	public ILogEntryFormatter getLogEntryFormatter() {
		return this.logEntryFormatter;
	}

}
