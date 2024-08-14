package provided.owlMaps.demo.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComponent;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.function.Supplier;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import javax.swing.JComboBox;

/**
 * 
 * The OwlMaps demo's view
 * @author swong
 * @param <TPlacesDropListItem> The type of item held by the combobox of places
 *
 */
public class DemoFrame<TPlacesDropListItem> extends JFrame {
	
	/**
	 * For Serializable
	 */
	private static final long serialVersionUID = -607090219537077804L;

	/**
	 * The panel holding the controls to manipulate the map
	 */
	private final JPanel pnlControl = new JPanel();
	
	/**
	 * The subpanel holding the map navigation controls.
	 */
	private final JPanel pnlNav = new JPanel();
	
	/**
	 * The text field for the latitude value
	 */
	private final JTextField tfLat = new JTextField();
	
	/**
	 * The text field for the longitude value
	 */
	private final JTextField tfLng = new JTextField();
	
	/**
	 * Label for the navigation subpanel
	 */
	private final JLabel lblNav = new JLabel("<html><h2>Navigation</h2></html>");

	
	/**
	 * The button to make the map go to the given lat/lng
	 */
	private final JButton btnNav = new JButton("Go to LatLng");
	
	/**
	 * The adapter to the model
	 */
	private IView2ModelAdapter<TPlacesDropListItem> view2ModelAdpt;
	
	/**
	 * The button to make an info window
	 */
	private final JButton btnMakeInfoWin = new JButton("Make Info Win");
	
	/**
	 * The button to make a polygon
	 */
	private final JButton btnMakePolygon = new JButton("Make Polygon");
	
	/**
	 * The button to make a polyline
	 */
	private final JButton btnMakePolyline = new JButton("Make Polyline");
	
	/**
	 * The button to make a rectangle
	 */
	private final JButton btnMakeRectangle = new JButton("Make Rectangle");
	
	/**
	 * The button to make a circle
	 */
	private final JButton btnCircle = new JButton("Make Circle");
	/**
	 * Scrollpane for holding the scrolling text area for console messages
	 */
	private final JScrollPane spDisplayText = new JScrollPane();
	
	/**
	 * A text area to display console messages
	 */
	private final JTextArea taDisplayText = new JTextArea();
	/**
	 * Button to reset the map
	 */
	private final JButton btnResetMap = new JButton("Reset Map");
	/**
	 * Button to make a ground image overlay
	 */
	private final JButton btnMakeOverlay = new JButton("Make Overlay");
	/**
	 * Directions on capturing a lat/lng
	 */
	private final JLabel lblLatLngDirections = new JLabel("<html>Right-click the map to fill<br/>latitude & longitude below:</html>");
	/**
	 * Label for info about tool tips
	 */
	private final JLabel lblToolTipInfo = new JLabel("<html><em>See tooltips for more info</em></html>");
	/**
	 * Button to add GeoJSON from a string
	 */
	private final JButton btnAddGeoJson = new JButton("Add GeoJSON from String");
	/**
	 * Button to add GeoJSON from a URL
	 */
	private final JButton btnLoadGeoJson = new JButton("Load GeoJSON from URL");
	/**
	 * The panel holding the components to enable moving the map to a new place
	 */
	private final JPanel pnlPlaces = new JPanel();
	/**
	 * The button that will cause the map move to the selected place 
	 */
	private final JButton btnGoToPlaces = new JButton("Go!");
	
	/**
	 * A combobox of all the choices of places to move the map to.
	 */
	private final JComboBox<TPlacesDropListItem> cbxPlaces = new JComboBox<TPlacesDropListItem>();

	/**
	 * Construct the demo's view
	 * @param view2ModelAdpt The adapter to the demo view's model
	 */
	public DemoFrame(IView2ModelAdapter<TPlacesDropListItem> view2ModelAdpt) {
		this.view2ModelAdpt = view2ModelAdpt;
		tfLng.setToolTipText("The longitude value to use in the operations below");
		tfLng.setColumns(10);
		tfLat.setToolTipText("The latitude value to use in the operations below");
		tfLat.setColumns(10);
		initGUI();
		
		addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
            	view2ModelAdpt.exit();
            }
        });
	}
	
	/**
	 * Initialize the GUI
	 */
	private void initGUI() {
		setSize(new Dimension(800, 800));
		setTitle("DemoApp");
		pnlControl.setBackground(Color.CYAN);
		
		getContentPane().add(pnlControl, BorderLayout.NORTH);
		pnlNav.setToolTipText("Note: This panel is on the East side so that the tool tips aren't obscured by the map.");
		pnlNav.setBackground(Color.GREEN);
		
		getContentPane().add(pnlNav, BorderLayout.EAST);
		GridBagLayout gbl_pnlNav = new GridBagLayout();
		gbl_pnlNav.columnWidths = new int[]{0, 0};
		gbl_pnlNav.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_pnlNav.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_pnlNav.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		pnlNav.setLayout(gbl_pnlNav);
		
		GridBagConstraints gbc_lblNav = new GridBagConstraints();
		gbc_lblNav.insets = new Insets(0, 0, 5, 0);
		gbc_lblNav.gridx = 0;
		gbc_lblNav.gridy = 0;
		pnlNav.add(lblNav, gbc_lblNav);
		
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 1;
		pnlNav.add(lblToolTipInfo, gbc_lblNewLabel_1);
		
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 2;
		pnlNav.add(lblLatLngDirections, gbc_lblNewLabel);
		
		GridBagConstraints gbc_tfLat = new GridBagConstraints();
		gbc_tfLat.insets = new Insets(0, 0, 5, 0);
		gbc_tfLat.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfLat.gridx = 0;
		gbc_tfLat.gridy = 3;
		pnlNav.add(tfLat, gbc_tfLat);
		
		GridBagConstraints gbc_tfLng = new GridBagConstraints();
		gbc_tfLng.insets = new Insets(0, 0, 5, 0);
		gbc_tfLng.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfLng.gridx = 0;
		gbc_tfLng.gridy = 4;
		pnlNav.add(tfLng, gbc_tfLng);
		
		GridBagConstraints gbc_btnNav = new GridBagConstraints();
		gbc_btnNav.insets = new Insets(0, 0, 5, 0);
		gbc_btnNav.gridx = 0;
		gbc_btnNav.gridy = 5;
		btnNav.setToolTipText("Center the map on the above LatLng and create a marker with an attached info window.");
		btnNav.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view2ModelAdpt.goToLatLng(tfLat.getText(), tfLng.getText());
			}
		});
		pnlNav.add(btnNav, gbc_btnNav);
		
		GridBagConstraints gbc_btnMakeInfoWin = new GridBagConstraints();
		gbc_btnMakeInfoWin.insets = new Insets(0, 0, 5, 0);
		gbc_btnMakeInfoWin.gridx = 0;
		gbc_btnMakeInfoWin.gridy = 6;
		btnMakeInfoWin.setToolTipText("Make an info window at the above location");
		btnMakeInfoWin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view2ModelAdpt.makeInfoWin(tfLat.getText(), tfLng.getText());
			}
		});
		pnlNav.add(btnMakeInfoWin, gbc_btnMakeInfoWin);
		
		GridBagConstraints gbc_btnMakePolygon = new GridBagConstraints();
		gbc_btnMakePolygon.insets = new Insets(0, 0, 5, 0);
		gbc_btnMakePolygon.gridx = 0;
		gbc_btnMakePolygon.gridy = 7;
		btnMakePolygon.setToolTipText("Make a polygon whose origin is at the above location");
		btnMakePolygon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view2ModelAdpt.makePolygon(tfLat.getText(), tfLng.getText());
			}
		});
		pnlNav.add(btnMakePolygon, gbc_btnMakePolygon);
		
		GridBagConstraints gbc_btnMakePolyline = new GridBagConstraints();
		gbc_btnMakePolyline.insets = new Insets(0, 0, 5, 0);
		gbc_btnMakePolyline.gridx = 0;
		gbc_btnMakePolyline.gridy = 8;
		btnMakePolyline.setToolTipText("Make a polyline whose origin is at the above location");
		btnMakePolyline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view2ModelAdpt.makePolyline(tfLat.getText(), tfLng.getText());
			}
		});
		pnlNav.add(btnMakePolyline, gbc_btnMakePolyline);
		
		GridBagConstraints gbc_btnMakeRectangle = new GridBagConstraints();
		gbc_btnMakeRectangle.insets = new Insets(0, 0, 5, 0);
		gbc_btnMakeRectangle.gridx = 0;
		gbc_btnMakeRectangle.gridy = 9;
		btnMakeRectangle.setToolTipText("Make a rectangle centered at the above location");
		btnMakeRectangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view2ModelAdpt.makeRectangle(tfLat.getText(), tfLng.getText());
			}
		});
		pnlNav.add(btnMakeRectangle, gbc_btnMakeRectangle);
		
		GridBagConstraints gbc_btnCircle = new GridBagConstraints();
		gbc_btnCircle.insets = new Insets(0, 0, 5, 0);
		gbc_btnCircle.gridx = 0;
		gbc_btnCircle.gridy = 10;
		btnCircle.setToolTipText("Make a circle centered at the above location");
		btnCircle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view2ModelAdpt.makeCircle(tfLat.getText(), tfLng.getText());
			}
		});
		pnlNav.add(btnCircle, gbc_btnCircle);
		
		GridBagConstraints gbc_btnMakeOverlay = new GridBagConstraints();
		gbc_btnMakeOverlay.insets = new Insets(0, 0, 5, 0);
		gbc_btnMakeOverlay.gridx = 0;
		gbc_btnMakeOverlay.gridy = 11;
		btnMakeOverlay.setToolTipText("Make a ground overlay centered at the above location. Click the overlay to make it disappear.");
		btnMakeOverlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view2ModelAdpt.makeOverlay(tfLat.getText(), tfLng.getText());
			}
		});
		pnlNav.add(btnMakeOverlay, gbc_btnMakeOverlay);
		
		GridBagConstraints gbc_btnResetMap = new GridBagConstraints();
		gbc_btnResetMap.insets = new Insets(0, 0, 5, 0);
		gbc_btnResetMap.gridx = 0;
		gbc_btnResetMap.gridy = 14;
		btnResetMap.setToolTipText("Sets original map options.");
		btnResetMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view2ModelAdpt.resetMap();
			}
		});
		
		GridBagConstraints gbc_btnAddGeoJson = new GridBagConstraints();
		gbc_btnAddGeoJson.insets = new Insets(0, 0, 5, 0);
		gbc_btnAddGeoJson.gridx = 0;
		gbc_btnAddGeoJson.gridy = 12;
		btnAddGeoJson.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view2ModelAdpt.addGeoJson();
			}
		});
		pnlNav.add(btnAddGeoJson, gbc_btnAddGeoJson);
		
		GridBagConstraints gbc_btnLoadGeoJson = new GridBagConstraints();
		gbc_btnLoadGeoJson.anchor = GridBagConstraints.BASELINE;
		gbc_btnLoadGeoJson.insets = new Insets(0, 0, 5, 0);
		gbc_btnLoadGeoJson.gridx = 0;
		gbc_btnLoadGeoJson.gridy = 13;
		btnLoadGeoJson.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view2ModelAdpt.loadGeoJson();
			}
		});
		pnlNav.add(btnLoadGeoJson, gbc_btnLoadGeoJson);
		pnlNav.add(btnResetMap, gbc_btnResetMap);
		
		GridBagConstraints gbc_pnlPlaces = new GridBagConstraints();
		gbc_pnlPlaces.insets = new Insets(0, 0, 5, 0);
		gbc_pnlPlaces.gridx = 0;
		gbc_pnlPlaces.gridy = 15;
		pnlPlaces.setBorder(new TitledBorder(null, "Go to Places", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlNav.add(pnlPlaces, gbc_pnlPlaces);
		GridBagLayout gbl_pnlPlaces = new GridBagLayout();
		gbl_pnlPlaces.columnWidths = new int[]{0, 0};
		gbl_pnlPlaces.rowHeights = new int[]{0, 0};
		gbl_pnlPlaces.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_pnlPlaces.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		pnlPlaces.setLayout(gbl_pnlPlaces);
		
		GridBagConstraints gbc_cbxPlaces = new GridBagConstraints();
		gbc_cbxPlaces.insets = new Insets(0, 0, 5, 0);
		gbc_cbxPlaces.gridx = 0;
		gbc_cbxPlaces.gridy = 0;
		pnlPlaces.add(cbxPlaces, gbc_cbxPlaces);
		
		GridBagConstraints gbc_btnGoToPlaces = new GridBagConstraints();
		gbc_btnGoToPlaces.insets = new Insets(0, 0, 5, 0);
		gbc_btnGoToPlaces.gridwidth = 0;
		gbc_btnGoToPlaces.gridx = 0;
		gbc_btnGoToPlaces.gridy = 1;
		btnGoToPlaces.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view2ModelAdpt.goToPlace(cbxPlaces.getItemAt(cbxPlaces.getSelectedIndex()));
			}
		});
		pnlPlaces.add(btnGoToPlaces, gbc_btnGoToPlaces);
		
		getContentPane().add(spDisplayText, BorderLayout.SOUTH);
		taDisplayText.setRows(5);
		
		spDisplayText.setViewportView(taDisplayText);
	}

	/**
	 * Add the given component to the main center display area of the frame
	 * and sets the title of the frame to the given label.
	 * The given factory is run as part of its own event on the GUI thread to decouple it from anything else that is going on.
	 * @param compFac The factory for the component to add
	 * @param label The label associated with the component.
	 */
	public void addComponentToCenter(Supplier<JComponent> compFac, String label) {
		// Dispatch over to the GUI thread.   Don't make assumptions on what thread the method is being called on!

		SwingUtilities.invokeLater(()->{
			getContentPane().add(compFac.get(), BorderLayout.CENTER);	// The component factory is run on the GUI thread!
			setTitle(label);   // Probably wouldn't need a title if this view was a panel being added to a client GUI.
			this.revalidate();   // force the parent component to re-layout its child components since a new one was added.
			this.repaint();   // request that the parent repaint itself.
		});
	}
	
	/**
	 * Start the demo view
	 */
	public void start() {
		setVisible(true);
	}
	
	/**
	 * Display the latitude and longitude values on their respective text fields.
	 * @param lat The latitude value
	 * @param lng The longitude value
	 */
	public void setLatLng(double lat, double lng) {
		tfLat.setText(""+lat);
		tfLng.setText(""+lng);
	}

	/**
	 * Display the given text in the text display area
	 * @param text the text to display
	 */
	public void displayText(String text) {
		taDisplayText.append(text+"\n");
	}
	
	/**
	 * Add an item to the places combobox (droplist)
	 * @param newItem The new place to add to the combobox
	 */
	public void addPlace(TPlacesDropListItem newItem) {
		cbxPlaces.addItem(newItem);
	}
}
