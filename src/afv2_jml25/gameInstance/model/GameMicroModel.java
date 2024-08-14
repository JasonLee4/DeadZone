package afv2_jml25.gameInstance.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;

import javax.swing.JComponent;
import javax.swing.Timer;



import afv2_jml25.api.IDeathData;
import afv2_jml25.api.IEliminationData;
import afv2_jml25.api.IGameBooleanData;
import common.dataPacket.ICmd2ModelAdapter;
import common.dataPacket.data.room.ITextData;
import common.serverObj.INamedRoomConnection;
import provided.logger.ILogger;
import provided.logger.ILoggerControl;
import provided.logger.LogLevel;
import provided.mixedData.IMixedDataDictionary;
import provided.mixedData.MixedDataKey;
import provided.owlMaps.control.IOwlMapControl;
import provided.owlMaps.general.ILatLng;
import provided.owlMaps.map.IMapOptions;
import provided.owlMaps.map.IOwlMap;
import provided.owlMaps.utils.IOwlMapUtils;

/**
 * Model of the game-level MVC system
 * 
 * @author Andres Villada (afv2)
 * @author Jason Lee (jml25)
 */
public class GameMicroModel {

	/**
	 * Milliseconds for each interval of timer
	 */
	private static final int TIMER_DELAY = 35000;

	/**
	 * List of countries recognized by the game
	 */
	String[] countries = new String[] { "Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola",
			"Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria",
			"Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin",
			"Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegovina", "Botswana", "Bouvet Island", "Brazil",
			"British Indian Ocean Territory", "Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon",
			"Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China",
			"Christmas Island", "Cocos Islands", "Colombia", "Comoros", "Congo", "The Democratic Republic of the Congo",
			"Cook Islands", "Costa Rica", "Cote d'Ivoire", "Croatia", "Cuba", "Cyprus", "Czech Republic", "Denmark",
			"Djibouti", "Dominica", "Dominican Republic", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea",
			"Eritrea", "Estonia", "Ethiopia", "Falkland Islands", "Faroe Islands", "Fiji", "Finland", "France",
			"French Guiana", "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany",
			"Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea",
			"Guinea-Bissau", "Guyana", "Haiti", "Heard and McDonald Islands", "Honduras", "Hong Kong", "Hungary",
			"Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan",
			"Kazakhstan", "Kenya", "Kiribati", "North Korea", "South Korea", "Kuwait", "Kyrgyzstan", "Laos", "Latvia",
			"Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania", "Luxembourg",
			"Macau", "Macedonia", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands",
			"Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia",
			"Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands",
			"Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue",
			"Norfolk Island", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Panama",
			"Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal", "Puerto Rico",
			"Qatar", "Reunion", "Romania", "Russia", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia",
			"Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia",
			"Senegal", "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Solomon Islands", "Somalia",
			"South Africa", "South Georgia and the South Sandwich Islands", "Spain", "Sri Lanka", "St. Helena",
			"St. Pierre and Miquelon", "Sudan", "Suriname", "Svalbard and Jan Mayen", "Eswatini", "Sweden",
			"Switzerland", "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Togo", "Tokelau", "Tonga",
			"Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda",
			"Ukraine", "United Arab Emirates", "United Kingdom", "United States",
			"United States Minor Outlying Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam",
			"Virgin Islands", "Wallis and Futuna Islands", "Western Sahara", "Yemen", "Zambia", "Zimbabwe",
			"Palestine" };

	/**
	 * Mapping of countries to booleans indicating whether or not they're dead
	 * zones.
	 */
	HashMap<String, Boolean> countryMap = new HashMap<>();
	/**
	 * Mapping of countries to their coordinates
	 */
	HashMap<String, ILatLng> coordsMap = new HashMap<>(Map.ofEntries(
			Map.entry("afghanistan", ILatLng.make(33.93911, 67.709953)),
			Map.entry("albania", ILatLng.make(41.153332, 20.168331)),
			Map.entry("algeria", ILatLng.make(28.033886, 1.659626)),
			Map.entry("american samoa", ILatLng.make(-14.270972, -170.132217)),
			Map.entry("andorra", ILatLng.make(42.546245, 1.601554)),
			Map.entry("angola", ILatLng.make(-11.202692, 17.873887)),
			Map.entry("anguilla", ILatLng.make(18.220554, -63.068615)),
			Map.entry("antarctica", ILatLng.make(-75.250973, -0.071389)),
			Map.entry("antigua and barbuda", ILatLng.make(17.060816, -61.796428)),
			Map.entry("argentina", ILatLng.make(-38.416097, -63.616672)),
			Map.entry("armenia", ILatLng.make(40.069099, 45.038189)),
			Map.entry("aruba", ILatLng.make(12.52111, -69.968338)),
			Map.entry("australia", ILatLng.make(-25.274398, 133.775136)),
			Map.entry("austria", ILatLng.make(47.516231, 14.550072)),
			Map.entry("azerbaijan", ILatLng.make(40.143105, 47.576927)),
			Map.entry("bahamas", ILatLng.make(25.03428, -77.39628)),
			Map.entry("bahrain", ILatLng.make(25.930414, 50.637772)),
			Map.entry("bangladesh", ILatLng.make(23.684994, 90.356331)),
			Map.entry("barbados", ILatLng.make(13.193887, -59.543198)),
			Map.entry("belarus", ILatLng.make(53.709807, 27.953389)),
			Map.entry("belgium", ILatLng.make(50.503887, 4.469936)),
			Map.entry("belize", ILatLng.make(17.189877, -88.49765)),
			Map.entry("benin", ILatLng.make(9.30769, 2.315834)),
			Map.entry("bermuda", ILatLng.make(32.321384, -64.75737)),
			Map.entry("bhutan", ILatLng.make(27.514162, 90.433601)),
			Map.entry("bolivia", ILatLng.make(-16.290154, -63.588653)),
			Map.entry("bosnia and herzegovina", ILatLng.make(43.915886, 17.679076)),
			Map.entry("bouvet island", ILatLng.make(-54.423199, 3.413194)),
			Map.entry("brazil", ILatLng.make(-14.235004, -51.92528)),
			Map.entry("british indian ocean territory", ILatLng.make(-6.343194, 71.876519)),
			Map.entry("brunei", ILatLng.make(4.535277, 114.727669)),
			Map.entry("bulgaria", ILatLng.make(42.733883, 25.48583)),
			Map.entry("burkina faso", ILatLng.make(12.238333, -1.561593)),
			Map.entry("burundi", ILatLng.make(-3.373056, 29.918886)),
			Map.entry("cambodia", ILatLng.make(12.565679, 104.990963)),
			Map.entry("cameroon", ILatLng.make(7.369722, 12.354722)),
			Map.entry("canada", ILatLng.make(56.130366, -106.346771)),
			Map.entry("cape verde", ILatLng.make(16.002082, -24.013197)),
			Map.entry("cayman islands", ILatLng.make(19.513469, -80.566956)),
			Map.entry("central african republic", ILatLng.make(6.611111, 20.939444)),
			Map.entry("chad", ILatLng.make(15.454166, 18.732207)),
			Map.entry("chile", ILatLng.make(-35.675147, -71.542969)),
			Map.entry("china", ILatLng.make(35.86166, 104.195397)),
			Map.entry("christmas island", ILatLng.make(-10.447525, 105.690449)),
			Map.entry("cocos islands", ILatLng.make(-12.164165, 96.870956)),
			Map.entry("colombia", ILatLng.make(4.570868, -74.297333)),
			Map.entry("comoros", ILatLng.make(-11.875001, 43.872219)),
			Map.entry("congo", ILatLng.make(-0.228021, 15.827659)),
			Map.entry("the democratic republic of the congo", ILatLng.make(-4.038333, 21.758664)),
			Map.entry("cook islands", ILatLng.make(-21.236736, -159.777671)),
			Map.entry("costa rica", ILatLng.make(9.748917, -83.753428)),
			Map.entry("cote d'ivoire", ILatLng.make(7.539989, -5.547083)),
			Map.entry("croatia", ILatLng.make(45.1, 15.2)), Map.entry("cuba", ILatLng.make(21.521757, -77.781167)),
			Map.entry("cyprus", ILatLng.make(35.126413, 33.429859)),
			Map.entry("czech republic", ILatLng.make(49.817492, 15.472962)),
			Map.entry("denmark", ILatLng.make(56.26392, 9.501785)),
			Map.entry("djibouti", ILatLng.make(11.825138, 42.590275)),
			Map.entry("dominica", ILatLng.make(15.414999, -61.370976)),
			Map.entry("dominican republic", ILatLng.make(18.735693, -70.162651)),
			Map.entry("ecuador", ILatLng.make(-1.831239, -78.183406)),
			Map.entry("egypt", ILatLng.make(26.820553, 30.802498)),
			Map.entry("el salvador", ILatLng.make(13.794185, -88.89653)),
			Map.entry("equatorial guinea", ILatLng.make(1.650801, 10.267895)),
			Map.entry("eritrea", ILatLng.make(15.179384, 39.782334)),
			Map.entry("estonia", ILatLng.make(58.595272, 25.013607)),
			Map.entry("ethiopia", ILatLng.make(9.145, 40.489673)),
			Map.entry("falkland islands", ILatLng.make(-51.796253, -59.523613)),
			Map.entry("faroe islands", ILatLng.make(61.892635, -6.911806)),
			Map.entry("fiji", ILatLng.make(-16.578193, 179.414413)),
			Map.entry("finland", ILatLng.make(61.92411, 25.748151)),
			Map.entry("france", ILatLng.make(46.227638, 2.213749)),
			Map.entry("french guiana", ILatLng.make(3.933889, -53.125782)),
			Map.entry("french polynesia", ILatLng.make(-17.679742, -149.406843)),
			Map.entry("french southern territories", ILatLng.make(-49.280366, 69.348557)),
			Map.entry("gabon", ILatLng.make(-0.803689, 11.609444)),
			Map.entry("gambia", ILatLng.make(13.443182, -15.310139)),
			Map.entry("georgia", ILatLng.make(42.315407, 43.356892)),
			Map.entry("germany", ILatLng.make(51.165691, 10.451526)),
			Map.entry("ghana", ILatLng.make(7.946527, -1.023194)),
			Map.entry("gibraltar", ILatLng.make(36.137741, -5.345374)),
			Map.entry("greece", ILatLng.make(39.074208, 21.824312)),
			Map.entry("greenland", ILatLng.make(71.706936, -42.604303)),
			Map.entry("grenada", ILatLng.make(12.262776, -61.604171)),
			Map.entry("guadeloupe", ILatLng.make(16.995971, -62.067641)),
			Map.entry("guam", ILatLng.make(13.444304, 144.793731)),
			Map.entry("guatemala", ILatLng.make(15.783471, -90.230759)),
			Map.entry("guinea", ILatLng.make(9.945587, -9.696645)),
			Map.entry("guinea-bissau", ILatLng.make(11.803749, -15.180413)),
			Map.entry("guyana", ILatLng.make(4.860416, -58.93018)),
			Map.entry("haiti", ILatLng.make(18.971187, -72.285215)),
			Map.entry("heard and mcdonald islands", ILatLng.make(-53.08181, 73.504158)),
			Map.entry("honduras", ILatLng.make(15.199999, -86.241905)),
			Map.entry("hong kong", ILatLng.make(22.396428, 114.109497)),
			Map.entry("hungary", ILatLng.make(47.162494, 19.503304)),
			Map.entry("iceland", ILatLng.make(64.963051, -19.020835)),
			Map.entry("india", ILatLng.make(20.593684, 78.96288)),
			Map.entry("indonesia", ILatLng.make(-0.789275, 113.921327)),
			Map.entry("iran", ILatLng.make(32.427908, 53.688046)),
			Map.entry("iraq", ILatLng.make(33.223191, 43.679291)),
			Map.entry("ireland", ILatLng.make(53.41291, -8.24389)),
			Map.entry("israel", ILatLng.make(31.046051, 34.851612)),
			Map.entry("italy", ILatLng.make(41.87194, 12.56738)),
			Map.entry("jamaica", ILatLng.make(18.109581, -77.297508)),
			Map.entry("japan", ILatLng.make(36.204824, 138.252924)),
			Map.entry("jordan", ILatLng.make(30.585164, 36.238414)),
			Map.entry("kazakhstan", ILatLng.make(48.019573, 66.923684)),
			Map.entry("kenya", ILatLng.make(-0.023559, 37.906193)),
			Map.entry("kiribati", ILatLng.make(-3.370417, -168.734039)),
			Map.entry("north korea", ILatLng.make(40.339852, 127.510093)),
			Map.entry("south korea", ILatLng.make(35.907757, 127.766922)),
			Map.entry("kuwait", ILatLng.make(29.31166, 47.481766)),
			Map.entry("kyrgyzstan", ILatLng.make(41.20438, 74.766098)),
			Map.entry("laos", ILatLng.make(19.85627, 102.495496)),
			Map.entry("luxembourg", ILatLng.make(49.815273, 6.129583)),
			Map.entry("macau", ILatLng.make(22.198745, 113.543873)),
			Map.entry("macedonia", ILatLng.make(41.608635, 21.745275)),
			Map.entry("madagascar", ILatLng.make(-18.766947, 46.869107)),
			Map.entry("malawi", ILatLng.make(-13.254308, 34.301525)),
			Map.entry("malaysia", ILatLng.make(4.210484, 101.975766)),
			Map.entry("maldives", ILatLng.make(3.202778, 73.22068)),
			Map.entry("mali", ILatLng.make(17.570692, -3.996166)),
			Map.entry("malta", ILatLng.make(35.937496, 14.375416)),
			Map.entry("marshall islands", ILatLng.make(7.131474, 171.184478)),
			Map.entry("martinique", ILatLng.make(14.641528, -61.024174)),
			Map.entry("mauritania", ILatLng.make(21.00789, -10.940835)),
			Map.entry("mauritius", ILatLng.make(-20.348404, 57.552152)),
			Map.entry("mayotte", ILatLng.make(-12.8275, 45.166244)),
			Map.entry("mexico", ILatLng.make(23.634501, -102.552784)),
			Map.entry("micronesia", ILatLng.make(7.425554, 150.550812)),
			Map.entry("moldova", ILatLng.make(47.411631, 28.369885)),
			Map.entry("monaco", ILatLng.make(43.750298, 7.412841)),
			Map.entry("mongolia", ILatLng.make(46.862496, 103.846656)),
			Map.entry("montserrat", ILatLng.make(16.742498, -62.187366)),
			Map.entry("morocco", ILatLng.make(31.791702, -7.09262)),
			Map.entry("mozambique", ILatLng.make(-18.665695, 35.529562)),
			Map.entry("myanmar", ILatLng.make(21.913965, 95.956223)),
			Map.entry("namibia", ILatLng.make(-22.95764, 18.49041)),
			Map.entry("nauru", ILatLng.make(-0.522778, 166.931503)),
			Map.entry("nepal", ILatLng.make(28.394857, 84.124008)),
			Map.entry("netherlands", ILatLng.make(52.132633, 5.291266)),
			Map.entry("netherlands antilles", ILatLng.make(12.226079, -69.060087)),
			Map.entry("new caledonia", ILatLng.make(-20.904305, 165.618042)),
			Map.entry("new zealand", ILatLng.make(-40.900557, 174.885971)),
			Map.entry("nicaragua", ILatLng.make(12.865416, -85.207229)),
			Map.entry("niger", ILatLng.make(17.607789, 8.081666)),
			Map.entry("nigeria", ILatLng.make(9.081999, 8.675277)),
			Map.entry("niue", ILatLng.make(-19.054445, -169.867233)),
			Map.entry("norfolk island", ILatLng.make(-29.040835, 167.954712)),
			Map.entry("northern mariana islands", ILatLng.make(17.33083, 145.38469)),
			Map.entry("norway", ILatLng.make(60.472024, 8.468946)),
			Map.entry("oman", ILatLng.make(21.512583, 55.923255)),
			Map.entry("pakistan", ILatLng.make(30.375321, 69.345116)),
			Map.entry("palau", ILatLng.make(7.51498, 134.58252)),
			Map.entry("panama", ILatLng.make(8.537981, -80.782127)),
			Map.entry("papua new guinea", ILatLng.make(-6.314993, 143.95555)),
			Map.entry("paraguay", ILatLng.make(-23.442503, -58.443832)),
			Map.entry("peru", ILatLng.make(-9.189967, -75.015152)),
			Map.entry("phillipines", ILatLng.make(12.879721, 121.774017)),
			Map.entry("pitcairn", ILatLng.make(-24.703615, -127.439308)),
			Map.entry("poland", ILatLng.make(51.919438, 19.145136)),
			Map.entry("portugal", ILatLng.make(39.399872, -8.224454)),
			Map.entry("puerto rico", ILatLng.make(18.220833, -66.590149)),
			Map.entry("qatar", ILatLng.make(25.354826, 51.183884)),
			Map.entry("reunion", ILatLng.make(-21.115141, 55.536384)),
			Map.entry("romania", ILatLng.make(45.943161, 24.96676)),
			Map.entry("russia", ILatLng.make(61.52401, 105.318756)),
			Map.entry("rwanda", ILatLng.make(-1.940278, 29.873888)),
			Map.entry("saint kitts and nevis", ILatLng.make(17.357822, -62.782998)),
			Map.entry("saint lucia", ILatLng.make(13.909444, -60.978893)),
			Map.entry("saint vincent and the grenadines", ILatLng.make(12.984305, -61.287228)),
			Map.entry("samoa", ILatLng.make(-13.759029, -172.104629)),
			Map.entry("sao tome and principe", ILatLng.make(0.18636, 6.613081)),
			Map.entry("san marino", ILatLng.make(43.94236, 12.457777)),
			Map.entry("saudi arabia", ILatLng.make(23.885942, 45.079162)),
			Map.entry("senegal", ILatLng.make(14.497401, -14.452362)),
			Map.entry("seychelles", ILatLng.make(-4.679574, 55.491977)),
			Map.entry("sierra leone", ILatLng.make(8.460555, -11.779889)),
			Map.entry("singapore", ILatLng.make(1.352083, 103.819836)),
			Map.entry("slovakia", ILatLng.make(48.669026, 19.699024)),
			Map.entry("slovenia", ILatLng.make(46.151241, 14.995463)),
			Map.entry("solomon islands", ILatLng.make(-9.64571, 160.156194)),
			Map.entry("somalia", ILatLng.make(5.152149, 46.199616)),
			Map.entry("south africa", ILatLng.make(-30.559482, 22.937506)),
			Map.entry("south georgia and the south sandwich islands", ILatLng.make(-54.429579, -36.587909)),
			Map.entry("spain", ILatLng.make(40.463667, -3.74922)),
			Map.entry("sri lanka", ILatLng.make(7.873054, 80.771797)),
			Map.entry("st. helena", ILatLng.make(-24.143474, -10.030696)),
			Map.entry("st. pierre and miquelon", ILatLng.make(46.941936, -56.27111)),
			Map.entry("sudan", ILatLng.make(12.862807, 30.217636)),
			Map.entry("suriname", ILatLng.make(3.919305, -56.027783)),
			Map.entry("svalbard and jan mayen", ILatLng.make(77.553604, 23.670272)),
			Map.entry("eswatini", ILatLng.make(-26.522503, 31.465866)),
			Map.entry("sweden", ILatLng.make(60.128161, 18.643501)),
			Map.entry("switzerland", ILatLng.make(46.818188, 8.227512)),
			Map.entry("syria", ILatLng.make(34.802075, 38.996815)),
			Map.entry("taiwan", ILatLng.make(23.69781, 120.960515)),
			Map.entry("tajikistan", ILatLng.make(38.861034, 71.276093)),
			Map.entry("tanzania", ILatLng.make(-6.369028, 34.888822)),
			Map.entry("thailand", ILatLng.make(15.870032, 100.992541)),
			Map.entry("togo", ILatLng.make(8.619543, 0.824782)),
			Map.entry("tokelau", ILatLng.make(-8.967363, -171.855881)),
			Map.entry("tonga", ILatLng.make(-21.178986, -175.198242)),
			Map.entry("trinidad and tobago", ILatLng.make(10.691803, -61.222503)),
			Map.entry("tunisia", ILatLng.make(33.886917, 9.537499)),
			Map.entry("turkey", ILatLng.make(38.963745, 35.243322)),
			Map.entry("turkmenistan", ILatLng.make(38.969719, 59.556278)),
			Map.entry("turks and caicos islands", ILatLng.make(21.694025, -71.797928)),
			Map.entry("uganda", ILatLng.make(1.373333, 32.290275)),
			Map.entry("tuvalu", ILatLng.make(-7.109535, 177.64933)),
			Map.entry("united states", ILatLng.make(37.09024, -95.712891)),
			Map.entry("united states minor outlying islands", ILatLng.make(19.2823, 166.6470)),
			Map.entry("uruguay", ILatLng.make(-32.522779, -55.765835)),
			Map.entry("uzbekistan", ILatLng.make(41.377491, 64.585262)),
			Map.entry("vanuatu", ILatLng.make(-15.376706, 166.959158)),
			Map.entry("venezuela", ILatLng.make(6.42375, -66.58973)),
			Map.entry("united arab emirates", ILatLng.make(23.424076, 53.847818)),
			Map.entry("united kingdom", ILatLng.make(55.378051, -3.435973)),
			Map.entry("ukraine", ILatLng.make(48.379433, 31.16558)),
			Map.entry("vietnam", ILatLng.make(14.058324, 108.277199)),
			Map.entry("virgin islands", ILatLng.make(18.335765, -64.896335)),
			Map.entry("wallis and futuna islands", ILatLng.make(-13.768752,	-177.156097)),
			Map.entry("western sahara", ILatLng.make(24.215527, -12.885834)),
			Map.entry("yemen", ILatLng.make(15.552727, 48.516388)),
			Map.entry("zambia", ILatLng.make(-13.133897, 27.849332)),
			Map.entry("zimbabwe", ILatLng.make(19.015438, 29.154857)),
			Map.entry("palestine", ILatLng.make(31.952162, 35.233154))));

	/**
	 * The game-level MVC model to view adapter
	 */
	private IGameMicroModel2ViewAdapter gameModel2ViewAdpt;

	/**
	 * Unique Google API key for Google Maps use
	 */
	private String googleAPIKey = "AIzaSyApkLYsPwlSJfEKhE1PJrSjm200Of1go4A";

	/**
	 * Configuration specifications for instance of Google Maps
	 */
	private IMixedDataDictionary mapOptionsDict = IMapOptions.makeDefault();

	/**
	 * Logger for logging program progression and updates
	 */
	private ILogger logger = ILoggerControl.getSharedLogger();

	/**
	 * Controller for starting, stopping, and generating Owl Maps system
	 */
	private IOwlMapControl owlMapControl;

	/**
	 * Representation for a displayed map
	 */
	private IOwlMap owlMap;

	/**
	 * Utilities for working with map
	 */
	private IOwlMapUtils owlMapUtils;

	/**
	 * Game round timer for in-round game behavior
	 */
	private Timer timer;
	
	/**
	 * Flag indicating whether or not the player is actively in the game
	 */
	private boolean active = false;

	/**
	 * Command to model adapter.
	 */
	ICmd2ModelAdapter cmd2ModelAdpt;

	/**
	 * Game server
	 */
	INamedRoomConnection gameServerRoomConnection;

	/**
	 * UUID of team room shared by game.
	 */
	UUID teamUUID;

	/**
	 * Player of the game instance.
	 */
	INamedRoomConnection player;

	/**
	 * option id unique for default owl maps options
	 */
	public static UUID OPTIONS_ID = UUID.fromString("5607567c-21d2-44db-be13-a83a1aa113c1");

	/**
	 * Constructor for this GameMicroModel object
	 * 
	 * @param player                      INamedRoomConnection representation of
	 *                                    player of the game instance
	 * @param gameUUID                    id shared by game and room/team
	 * @param serverRoomConnection        INamedRoomConnection dyad of server (us)
	 * 
	 * @param iGameMicroModel2ViewAdapter game-level model to view adapter
	 * @param cmdAdpt                     command-to-model adapter
	 */
	public GameMicroModel(INamedRoomConnection player, UUID gameUUID, INamedRoomConnection serverRoomConnection,
			IGameMicroModel2ViewAdapter iGameMicroModel2ViewAdapter, ICmd2ModelAdapter cmdAdpt) {
		this.teamUUID = gameUUID;
		this.gameModel2ViewAdpt = iGameMicroModel2ViewAdapter;
		this.cmd2ModelAdpt = cmdAdpt;
		this.gameServerRoomConnection = serverRoomConnection;
		this.player = player;

		mapOptionsDict.put(new MixedDataKey<Integer>(OPTIONS_ID, "zoom", Integer.class), 4);
	}

	/**
	 * Starts Owl Maps
	 */
	public void start() {
		for (String s : countries) {
			String key = s.toLowerCase();
			countryMap.put(key, true);
		}

		startOwlMaps();

		this.gameModel2ViewAdpt.roundTimer();
		timer = new Timer(TIMER_DELAY, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Beginning of round:
				logger.log(LogLevel.INFO, "New round. 35 seconds have passed. Pick a country, good luck!");
				eliminateCountries();

				// End of round:
				gameEvaluate(active);
				// If player is still active, reset timer for next round
				if (active) {
					gameModel2ViewAdpt.roundTimer();
				}
				active = false;
			}
		});
		timer.start();
	}

	/**
	 * Starts owl maps system
	 */
	private void startOwlMaps() { 
		Supplier<JComponent> mapFactory = IOwlMapControl.makeMapFactory(googleAPIKey, mapOptionsDict,
				(owlMapControl) -> {
					// The mapControl and its map are NOT available until this function runs!
					// Any initial processing of the map must be done here.

					this.owlMapControl = owlMapControl; // The IOwlMapControl instance has not been saved anywhere yet,
														// so
														// assign it to a field in the model!
					this.owlMap = owlMapControl.getMap(); // Get the map -- map is not available until now!
					this.owlMapUtils = owlMapControl.getMapUtils(); // Save the map utils

					// The IMapComponentFactory can be obtained from the IOwlMapControl and used now
					// if desired.

					// Add a right-click mouse handler to the map, for example
					// this.owlMap.setMapMouseEvent(MapMouseEventType.RIGHT_CLICK, (mouseEvent)->{
					// viewLogger.log(LogLevel.DEBUG, "[MapMouseEvent:
					// "+MapMouseEventType.RIGHT_CLICK.getName()+"] mouseEvent = "+mouseEvent);
//					 model2ViewAdpt.setLatLng(mouseEvent.getLatLng());
					// });
					// viewLogger.log(LogLevel.INFO, "[onMapLoad] completed, releasing countdown
					// latch!");
					// mapReadyCountDownLatch.countDown(); // release threads waiting for map to be
					// ready
				}, logger);

		gameModel2ViewAdpt.addMapComp(mapFactory, "Game Map");
	}

	/**
	 * Eliminates randomly-selected countries and determines them as dead-zones.
	 */
	public void eliminateCountries() {

		Random rand = new Random();
		for (int i = 0; i < 20; i++) {

			int index = rand.nextInt(countries.length);

			countryMap.put(countries[index].toLowerCase(), false);
			countryMap.put("test", false);
			ITextData elimCountry = ITextData.make(countries[index]);
			cmd2ModelAdpt.sendMessageToDyad(elimCountry, gameServerRoomConnection);
			this.logger.log(LogLevel.INFO, countries[index] + " has become a dead zone.");
		}

	}

	/**
	 * Stops any input-control or round progression for the player and informs the
	 * game server that this player has been eliminated.
	 */
	public void eliminatePlayer() {
		// Report player death
		IDeathData playerDeath = IDeathData.make(this.player);
		this.cmd2ModelAdpt.sendMessageToDyad(playerDeath, gameServerRoomConnection);
		// Stop the timer for the player's game
		timer.stop(); 
		// Player is no longer active
		active = false; 
		// Disable map controls for the player
		owlMapControl.stop();
		// Demonstrate to the player that they have lost on their game GUI
		gameModel2ViewAdpt.displayLossPanel();
		gameModel2ViewAdpt.updateGameStatus("Eliminated");
		// Disable GUI input controls for the player
		this.gameModel2ViewAdpt.enableInputComponents(false);
		// Log to the player that they have lost
		this.logger.log(LogLevel.INFO, this.player.getName() + " took this L.");
	}

	/**
	 * Checks if the given country is a recognized country of the game. If not, then
	 * inform the player. Otherwise, perform game processing of whether or not the
	 * player is still in the game
	 * 
	 * @param country the country the player chooses to be set on
	 */
	public void processCountry(String country) {
		// Check that input from player is valid
		if (countryMap.containsKey(country.toLowerCase())) {
			owlMap.panTo(coordsMap.get(country.toLowerCase()));
			
			if (countryMap.get(country.toLowerCase())) {
				this.logger.log(LogLevel.INFO, "\'" + country + "\' was entered. This is a safe zone.");
				this.active = true;
			} else {
				this.logger.log(LogLevel.INFO, country + " is a Dead Zone, perish.");
				this.active = false;

			}
			// Disables any input until the next round
			this.gameModel2ViewAdpt.enableInputComponents(false);
			this.logger.log(LogLevel.INFO, "Player (" + this.player.getName() + ") input disabled.");

			countryMap.put(country.toLowerCase(), false);
			this.logger.log(LogLevel.INFO, country + " has become a dead zone.");
		} else {
			this.logger.log(LogLevel.INFO, country + " is a non-valid input country. Please put a new country");
			this.active = false;
		}
	}

	/**
	 * Looping round logic of game.
	 * 
	 * @param active boolean value which determines if player should be eliminated
	 *               or not.
	 */
	public void gameEvaluate(boolean active) {

		// If the player is dead...
		if (!active) {
			eliminatePlayer();
		}
		// If alive, continue playing
		else {
			this.gameModel2ViewAdpt.enableInputComponents(true);
		}

		// Check if team that player belongs to is still active
		IEliminationData teamEliminationData = IEliminationData.make(this.teamUUID);
		cmd2ModelAdpt.sendMessageToDyad(teamEliminationData, gameServerRoomConnection);

		// Check if game has been won or a draw has occurred
		IGameBooleanData gameBoolData = IGameBooleanData.make();
		cmd2ModelAdpt.sendMessageToDyad(gameBoolData, this.gameServerRoomConnection);
	}

}
