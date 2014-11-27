package p1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import utilidades.Utilidades;



public class Bd1 extends JFrame implements ActionListener{

	// DB connection, query and processing
    Connection connection;
    //static final String connURL = "jdbc:mysql://localhost/concesionario?user=root";
    static final String connURL = "jdbc:mysql://localhost/concesionario?user=alumno&password=lsi";
    Statement statement;
    ResultSet resultSet;
    ResultSetMetaData rsMetaData;
    
    String currentQuery = "SELECT dni, nombre FROM  clientes WHERE ciudad IN ('Madrid')";
    int currentTable = 4;
    
    String [] tableNames = {"clientes", "coches", "concesionarios", "distribucion", "marcas", "marco", "ventas"};
    String [] procedureNames = {"cliente por ciudad", "coche por concesionario"};
    
    
    // GUI components.
    private JTable      table;
    private JScrollPane tableScroller; 
    
    private JTextArea TA_inputQuery;
    private JTextArea TA_queryResult;
    
    private JButton BT_submitQuery;
    private JButton BT_InsertRecord;
    private JButton BT_DeleteRecord;
    private JButton BT_ModifyRecord;
    
    private JMenuBar     barraMenu;
    private JMenu        menuTablas;
    private JMenuItem [] itTabla = new JMenuItem[tableNames.length];
    private JMenu        menuProcedimientos;
    private JMenuItem [] itProcedure = new JMenuItem[procedureNames.length];
	
	public Bd1() {
		
		super("Test BD");
				
        // Creating gui components.
		TA_inputQuery = new JTextArea(currentQuery, 4, 30);
		TA_queryResult = new JTextArea("Waiting query...", 4, 30);
		
		BT_submitQuery = new JButton("Submit Query");
		BT_submitQuery.addActionListener(this);
		
		BT_InsertRecord = new JButton("Insert Record");
		BT_InsertRecord.addActionListener(this);
		BT_DeleteRecord = new JButton("Delete Record");
		BT_DeleteRecord.addActionListener(this);
		BT_ModifyRecord = new JButton("Modify Record");
		BT_ModifyRecord.addActionListener(this);
		
		// Menus
		buildMenu();
		
		// Query Panel
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		topPanel.add(new JScrollPane(TA_inputQuery), BorderLayout.CENTER);
		topPanel.add(BT_submitQuery, BorderLayout.SOUTH);
		
		// Debugging panel
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.setBorder(BorderFactory.createLineBorder(Color.blue));
		bottomPanel.add(new JLabel("Debugging panel "), BorderLayout.NORTH);
		bottomPanel.add(new JScrollPane(TA_queryResult), BorderLayout.CENTER);
		
		// Putting all together in frame. Center is reserved for tables.
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		c.add(topPanel, BorderLayout.NORTH);
		c.add(bottomPanel, BorderLayout.SOUTH);
		
		// Window arrangements.
		setSize (720,500);
		setVisible(true);  
		validate();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		
		// connect to data base.
		connection = Utilidades.connect(connURL);
	}
	
	/**
	 * Build application menus.
	 */
	private void buildMenu(){
		barraMenu = new JMenuBar();
		menuTablas = new JMenu("Seleccione tabla");
		for (int i = 0; i < itTabla.length; i++){
			itTabla[i] = new JMenuItem(tableNames[i]);
			itTabla[i].addActionListener(this);
			menuTablas.add(itTabla[i]);
		}
		menuProcedimientos = new JMenu("Seleccione procedimiento");
		for (int i = 0; i < itProcedure.length; i++){
			itProcedure[i] = new JMenuItem(procedureNames[i]);
			itProcedure[i].addActionListener(this);
			menuProcedimientos.add(itProcedure[i]);
		}		
		
        barraMenu.add(menuTablas);
        barraMenu.add(menuProcedimientos);
        barraMenu.add(BT_InsertRecord); 
        barraMenu.add(BT_ModifyRecord); 
        barraMenu.add(BT_DeleteRecord); 

        barraMenu.setBorder(BorderFactory.createLineBorder(Color.blue));
        setJMenuBar(barraMenu);    		
	}
	

	

	
	/**
	 * 
	 * @param rs
	 * @throws SQLException
	 */
	private void displayResultSet(ResultSet rs) throws SQLException {
		
		boolean masFilas = rs.next();
		
		if (!masFilas){
			JOptionPane.showMessageDialog(this, "displayResultSet: Tabla vacía");
			System.out.println("displayResultSet: Tabla vacía");
			return;
		}
		
		Vector<String> cabeceras = new Vector<String>();
		Vector<Object> filas     = new Vector<>();
		
		try {
			// get headers
			ResultSetMetaData rsmd = rs.getMetaData();
			for(int i = 1; i <= rsmd.getColumnCount(); ++i){
				cabeceras.addElement(rsmd.getColumnName(i));
			}
			// get data, row by row
			do {
				filas.addElement(getNextRow(rs, rsmd));
			} while (rs.next());
			
			// DisplayTable
			table = new JTable(filas, cabeceras);
			if (tableScroller != null) getContentPane().remove(tableScroller);
			tableScroller = new JScrollPane(table);
			getContentPane().add(tableScroller, BorderLayout.CENTER);
			validate();	
		}
		catch(SQLException e){
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	 * @param rs
	 * @param rsmd
	 * @return
	 * @throws SQLException
	 */
	private Vector<Object> getNextRow(ResultSet rs, ResultSetMetaData rsmd) throws SQLException {
		
		Vector<Object> fila = new Vector<>();
		
		for(int i = 1; i <= rsmd.getColumnCount(); ++i){
			switch(rsmd.getColumnType(i)){
			case Types.CHAR:
				fila.addElement(rs.getString(i));
				break;
			case Types.VARCHAR:
				fila.addElement(rs.getString(i));
				break;
			case Types.TIME:
				fila.addElement(rs.getString(i));
				break;			
			case Types.INTEGER:
				fila.addElement(new Long(rs.getLong(i)));
				break;	
			case Types.DOUBLE:
				fila.addElement(new Double(rs.getDouble(i)));
				break;
			default:
				fila.addElement(rs.getObject(i));
                System.out.println("El tipo de la columna " + i + " es " + rsmd.getColumnTypeName(i) + ".");
			}
		}		
		return fila;
	}
	
	/**
	 * Event handler for JFrame Controls (menu entries and frame buttons).
	 */
	public void actionPerformed(ActionEvent ae){
		
		// Custom query submission
		if (ae.getSource() == BT_submitQuery){
			try {
				ResultSet rs = Utilidades.getTableFromQuery(connection, TA_inputQuery.getText());
			    displayResultSet(rs);
			}
			catch (Exception e){
				TA_queryResult.append("Query " + TA_inputQuery.getText() + " ->  " + e.getMessage());
				e.printStackTrace();
			}					
		}	
		
		// Insert query.
		if (ae.getSource() == BT_InsertRecord){	
			try {
				new InsertRecordDialog(this, "Insert Record. Table -> " + tableNames[currentTable]);
			}
			catch (Exception e){
				TA_queryResult.append("Query " + TA_inputQuery.getText() + " ->  " + e.getMessage());
				e.printStackTrace();
			}					
		}	
		
		// Modify query.
		if (ae.getSource() == BT_ModifyRecord){	
			try {
				new ModifyRecordDialog(this, "Modify Record. Table -> " + tableNames[currentTable]);
			}
			catch (Exception e){
				TA_queryResult.append("Query " + TA_inputQuery.getText() + " ->  " + e.getMessage());
				e.printStackTrace();
			}					
		}	
		
		// Delete query.
		if (ae.getSource() == BT_DeleteRecord){	
			try {
				new ModifyRecordDialog(this, "Delete Record. Table -> " + tableNames[currentTable]);
			}
			catch (Exception e){
				TA_queryResult.append("Query " + TA_inputQuery.getText() + " ->  " + e.getMessage());
				e.printStackTrace();
			}					
		}	
		
		
		// Table selection from menu
		for (int i = 0; i < itTabla.length; i++){
			if (ae.getSource() == itTabla[i]){
				System.out.println(tableNames[i]);
				try {
					ResultSet rs = Utilidades.getTable(connection, tableNames[i]);
				    displayResultSet(rs);
				    currentTable = i;
				}
				catch (Exception e){
					TA_queryResult.append("Query " + TA_inputQuery.getText() + " -> \n" + e.getMessage() + "\n");
					e.printStackTrace();
				}
				return;
			}
		}
		
		// Procedure selection from menu
		for (int i = 0; i < itProcedure.length; i++){
			if (ae.getSource() == itProcedure[i]){
				System.out.println(procedureNames[i]);
				try {
					new MiDialogo(this, procedureNames[i]);
				}
				catch (Exception e){
					TA_queryResult.append("Query " + TA_inputQuery.getText() + " -> \n" + e.getMessage() + "\n");
					e.printStackTrace();
				}
				return;
			}
		}		
	}
	

	
	/**
	 * Dialog for inserting a new record in a given table.
	 * @author Juan Ángel
	 *
	 */
	class InsertRecordDialog extends JDialog implements ActionListener {
		
		Bd1 owner;
		
		ResultSet rs;
		ResultSetMetaData rsmd;
		Vector<String> columnNames = new Vector<String>();
		Vector<Integer> columnTypes =  new Vector<Integer>();
		
		JLabel LB_fieldsNames [];
		JTextField  TX_fieldValues [];
		JPanel PN_fields;
		JPanel PN_buttons;
		
		JButton BT_ClearFields;
		JButton BT_Accept;
		JButton BT_Cancel;
		
		
		public InsertRecordDialog(Bd1 owner, String title) throws Exception {
			super(owner, title, true);
			this.owner = owner;
			PN_fields = new JPanel();
			PN_buttons = new JPanel();
			BT_ClearFields = new JButton("Clear Fields");
			BT_ClearFields.addActionListener(this);
			BT_Accept = new JButton("Accept");
			BT_Accept.addActionListener(this);	
			BT_Cancel = new JButton("Accept");
			BT_Cancel.addActionListener(this);	
			
			PN_buttons.add(BT_ClearFields);
			PN_buttons.add(BT_Accept);
			PN_buttons.add(BT_Cancel);
			
			// Get current table to get metadata.
			// Dialog fields and query depend on current table attributes and types.
			try {
				rs = Utilidades.getTable(connection, owner.tableNames[owner.currentTable]);
				rsmd = rs.getMetaData();
				LB_fieldsNames = new JLabel[rsmd.getColumnCount()];
				TX_fieldValues = new JTextField[rsmd.getColumnCount()];
				
				//Container c = getContentPane();
				//c.setLayout(new GridLayout(rsmd.getColumnCount(),2));
				PN_fields.setLayout(new GridLayout(rsmd.getColumnCount(),2));
				
				for(int i = 0; i < rsmd.getColumnCount(); ++i){
					columnNames.addElement(rsmd.getColumnName(i+1));
					columnTypes.addElement(rsmd.getColumnType(i+1));
					LB_fieldsNames[i] = new JLabel(rsmd.getColumnName(i+1));
					TX_fieldValues[i] = new JTextField(10);
					PN_fields.add(LB_fieldsNames[i]);
					PN_fields.add(TX_fieldValues[i]);
				}
			}
			catch(Exception e){
				e.printStackTrace();
				throw e;
			}
		
			getContentPane().setLayout(new BorderLayout());
			getContentPane().add(PN_fields);
			getContentPane().add(PN_buttons, BorderLayout.SOUTH);			
			setSize (320,200);
			setVisible(true);  
			validate();		
		}
		
		public void actionPerformed(ActionEvent ae){
			if (ae.getSource() == BT_Accept){
				try{

					// Building query.
					String query = "INSERT INTO " + owner.tableNames[owner.currentTable] + " (";
					for (int i = 0; i < LB_fieldsNames.length; i++){
						query += LB_fieldsNames[i].getText();
						if (i < LB_fieldsNames.length - 1){
							query += ", ";
						}
						else{
							query += ") VALUES (";
						}
					}
					// Debug.
					System.out.println("Q1 -> " + query);

					for (int i = 0; i < TX_fieldValues.length; i++){
						query = query + "'" + TX_fieldValues[i].getText() + "'";
						if (i < TX_fieldValues.length - 1){
							query += ", ";
						}
						else{
							query += ")";
						}
					}
					// Debug.
					System.out.println("Q2 -> " + query);

					// Issue query to data base
					Statement statement = owner.connection.createStatement();
					System.out.println("Q3 -> " + owner.connection.nativeSQL(query));
					int result = statement.executeUpdate(query);
					System.out.println("Result -> " + result);	
					statement.close();
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			if (ae.getSource() == BT_Cancel || ae.getSource() == BT_ClearFields ){
				for (int i = 0; i < TX_fieldValues.length; i++){
					TX_fieldValues[i].setText("");
				}				
			}
		}
	}
	
	/**
	 * Dialog for deleting a given record in a given table.
	 * @author Juan Ángel
	 *
	 */
	class DeleteRecordDialog extends JDialog {
		public DeleteRecordDialog(Bd1 owner, String title) throws Exception{}
	}

	/**
	 * Dialog for modifying a record in a given table.
	 * @author Juan Ángel
	 *
	 */
	class ModifyRecordDialog extends JDialog {
		public ModifyRecordDialog(Bd1 owner, String title) throws Exception{}		
	}
	
	
	class MiDialogo extends JDialog {
		public MiDialogo(JFrame owner, String title){
			super(owner, title, true);
			setSize (100,100);
			setVisible(true);  
			validate();
		}
	}

	public static void main(String [] args) throws Exception{
	    Bd1 test = new Bd1();
	}
}
