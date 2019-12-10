package projectOne.cakeProject;

import javax.swing.*;


import java.awt.*;

import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDFS;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class priceFilter extends JFrame {

	// Directory where we've stored the local data files, such as cakeOntology.owl
    public static final String SOURCE = "./src/main/resources/data/";
    
    // Cake ontology namespace
    public static final String Cakes_n = "http://www.semanticweb.org/mehotabakovic/ontologies/2019/9/untitled-ontology-5#";
    
	private JPanel mainPanel;
	
	
	private JPanel filterPanel;
	
	JLabel cakeLabel;

	private JScrollPane sp=new JScrollPane();

			
	public static priceFilter priceFilter=new priceFilter();
	
	
	public JComboBox cakeTypeCombo;
	
	public JComboBox cakeCategoryCombo;
	
	
	String selectedCakeType="Cake type";
	
	String selectedCakeCategory="Cake category";
	
public void LoadCakeType() {
		
		
		OntModel mm = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
	
		FileManager.get().readModel( mm, SOURCE + "cakeOntology.owl" );
		
		String prefixx = "prefix ck: <" + Cakes_n + ">\n" +
                		"prefix rdfs: <" + RDFS.getURI() + ">\n" +
                		"prefix owl: <" + OWL.getURI() + ">\n";

		String query_textt=  prefixx +
				"SELECT ?name\r\n" + 
				"WHERE {?cakeType a ck:CakeType.?cakeType ck:cakeTypeName ?name.}";
		
		Query query1 = QueryFactory.create( query_textt );
        QueryExecution qexecc = QueryExecutionFactory.create( query1, mm );
        

        List<String[]> valuesCT = new ArrayList<String[]>();

      
        try {
            ResultSet results = qexecc.execSelect();
            int i = 0;
            while ( results.hasNext() ) {
                QuerySolution qs = results.next();     
                valuesCT.add(new String[] {qs.get("name").toString()});
                i++;
            }
            
        
        }
        finally {
            qexecc.close();
        }
        ArrayList<String>  cakeType_values = new ArrayList<String>();
        cakeType_values.add("Cake type");
        for (String[] var : valuesCT) 
        { 
        	cakeType_values.add(var[0].toString());
        	
        }
		
        String[] array = cakeType_values.toArray(new String[cakeType_values.size()]);
		cakeTypeCombo = new JComboBox(array);
		cakeTypeCombo.setBounds(400,10,140,50);
		filterPanel.add(cakeTypeCombo);
		
	}
public void LoadCakeCategory() {


	
	OntModel mm = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );

	FileManager.get().readModel( mm, SOURCE + "cakeOntology.owl" );
	
	String prefixx = "prefix ck: <" + Cakes_n + ">\n" +
            		"prefix rdfs: <" + RDFS.getURI() + ">\n" +
            		"prefix owl: <" + OWL.getURI() + ">\n";

	String query_textt=  prefixx +
			"SELECT DISTINCT (strafter(?label,\"#\") as ?cakeCategoryy)" + 
			"WHERE {?Cake a ?cCake.?cCake rdfs:subClassOf ck:Cake. \r\n"+
			"BIND(str(?cCake) AS ?label). \r\n"+
			"BIND(strafter(?label,\"#\") as ?cakeCategory)}";
	
	Query query1 = QueryFactory.create( query_textt );
    QueryExecution qexecc = QueryExecutionFactory.create( query1, mm );
    

    List<String[]> valuesCT = new ArrayList<String[]>();

  
    try {
        ResultSet results = qexecc.execSelect();
        int i = 0;
        while ( results.hasNext() ) {
            QuerySolution qs = results.next();     
            valuesCT.add(new String[] {qs.get("cakeCategoryy").toString()});
            i++;
        }
        
    
    }
    finally {
        qexecc.close();
    }
    ArrayList<String>  cakeCategory_values = new ArrayList<String>();
    cakeCategory_values.add("Cake category");
    for (String[] var : valuesCT) 
    { 
    	cakeCategory_values.add(var[0].toString());
    	
    }
	
    String[] array = cakeCategory_values.toArray(new String[cakeCategory_values.size()]);
    cakeCategoryCombo = new JComboBox(array);
    cakeCategoryCombo.setBounds(250,10,140,50);
	filterPanel.add(cakeCategoryCombo);
	
}	
public void LoadDRTable() {
	
	//read ontology model
		//create instance of OntModel class
		OntModel m = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );

		FileManager.get().readModel( m, SOURCE + "cakeOntology.owl" );
//		String query_text;
		String prefix = "prefix ck: <" + Cakes_n + ">\n" +
	            		"prefix rdfs: <" + RDFS.getURI() + ">\n" +
	            		"prefix owl: <" + OWL.getURI() + ">\n";
		String query_text;
		 query_text=  prefix  +
					"SELECT (strafter(?label,\"#\") as ?cakeCategoryy) ?cakeTypeName ?city (AVG(?price) as ?averagePrice)"+
					"WHERE{ ?Cake a ?cCake. ?cCake rdfs:subClassOf ck:Cake. ?Cake ck:cakeName ?name. ?Cake ck:cakePrice ?price."+
					" ?CakeBranch ck:sells ?Cake.?CakeBranch ck:isLocatedOnAddress ?Address.?Address ck:addressLine1 ?firstAddress."+
					"?City ck:hasAddress ?Address.?City ck:cityName ?city.?CakeShop a ?cCakeShop.?cCakeShop rdfs:subClassOf ck:CakeShop."+
					" ?CakeShop ck:hasCakeBranch ?CakeBranch.?CakeShop ck:cakeShopName ?shopName.?Cake ck:hasCakeType ?cakeType.?cakeType ck:cakeTypeName ?cakeTypeName. \r\n"+
					"BIND(str(?cCake) AS ?label). \r\n"+
					"BIND(strafter(?label,\"#\") as ?cakeCategory). \r\n";
		
			if(selectedCakeCategory.contentEquals("Cake category") && selectedCakeType.contentEquals("Cake type"))	{
				
		
			}
			else if(!selectedCakeCategory.contentEquals("Cake category") && selectedCakeType.contentEquals("Cake type"))	{
				    query_text+="FILTER(regex(str(?cakeCategory),"+"\""+selectedCakeCategory+"\""+"))";
						
					}
		    else if(selectedCakeCategory.contentEquals("Cake category") && !selectedCakeType.contentEquals("Cake type"))	{
						    query_text+="FILTER(regex(str(?cakeTypeName),"+"\""+selectedCakeType+"\""+"))";
								
				}
		    else {
		    	 query_text+="FILTER(regex(str(?cakeTypeName),"+"\""+selectedCakeType+"\""+")"+"&& regex(str(?cakeCategory),"+"\""+selectedCakeCategory+"\""+")"+ ")";
		    }
				 
				 query_text +="} GROUP BY ?cakeTypeName ?label ?city ";
				  System.out.println(query_text);
				Query query = QueryFactory.create( query_text );
		        QueryExecution qexec = QueryExecutionFactory.create( query, m );
		       
		        /*************************************** Create Arrays for Table Headers and Table Values **********************************/ 
		        List<String> columns = new ArrayList<String>();
		        List<String[]> values = new ArrayList<String[]>();

		        columns.add("Cake category");
		        columns.add("Cake type");
		        columns.add("City");
		        columns.add("Price");
		        
		   
		        /*******************************************************************************************************************************/

		        try {
		            ResultSet results = qexec.execSelect();
		            int i = 0;
		            while ( results.hasNext() ) {
		                QuerySolution qs = results.next();

		                /****************************  Assign query data to array. That will populate JTable **************************/
		                values.add(new String[] {qs.get("cakeCategoryy").toString(),qs.get("cakeTypeName").toString(),qs.get("city").toString(),"£"+ qs.get("averagePrice").toString().substring(0, qs.get("averagePrice").toString().indexOf("^"))});
		               /**************************************************************************************************************/
		                

		                i++;
		            }
		            
		         /*************************Create Table and tableModel******************************/
		           TableModel tableModel = new DefaultTableModel(values.toArray(new Object[][] {}), columns.toArray());
		           final JTable table = new JTable(tableModel);
		            table.setForeground(Color.DARK_GRAY);
		            table.setBackground(Color.WHITE);
		            table.setRowHeight(35);		
		            sp.setViewportView(table);		           
		            sp.setBounds(10, 10, 780, 390);
		            
		            
		            
		   
		           
		            mainPanel.add(sp);
		            mainPanel.validate();
		            mainPanel.setBackground(new Color(255,255,255,30));
		          mainPanel.repaint();
		          
		          /*********************************************************************************/
		        }
		        finally {
		            qexec.close();
		        }
			
				

}

priceFilter(){
		
		
		//font
		Font f=new Font("Serif",Font.BOLD,30);
		
		
		//header
		JPanel heading;
		heading=new JPanel();

		heading.setBackground(new Color(255,255,255,30));
		heading.setBounds(0,0,900,60);
		JLabel name=new JLabel("Average cake price");
		name.setForeground(Color.WHITE);
		name.setBounds(200,50,400,50);
		name.setFont(f);
		heading.add(name);
		
		 filterPanel=new JPanel();
		 filterPanel.setLayout(null);
		setSize(850,70);
		filterPanel.setBackground(new Color(255,255,255,30));
		filterPanel.setBounds(20,70,850,70);
		
		   mainPanel=new JPanel();
			mainPanel.setLayout(null);
			setSize(800,400);
			mainPanel.setBackground(new Color(255,255,255,30));
			mainPanel.setBounds(50,150,800,400);
	 
			LoadCakeType();
			LoadCakeCategory();
			
			  cakeTypeCombo.addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent event) {
		                // Get the source of the component, which is our combo
		                // box.
		                JComboBox comboBox = (JComboBox) event.getSource();

		                // Print the selected items and the action command.
		                Object selected = comboBox.getSelectedItem();	                
		                String command = event.getActionCommand();
		                selectedCakeType=selected.toString();
		                
		                // Detect whether the action command is "comboBoxEdited"
		                // or "comboBoxChanged"
		                 if ("comboBoxChanged".equals(command)) {
		                	 LoadDRTable();
      	 
		                }
		            }
		        });
			  
			  cakeCategoryCombo.addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent event) {
		                // Get the source of the component, which is our combo
		                // box.
		                JComboBox comboBox = (JComboBox) event.getSource();

		                // Print the selected items and the action command.
		                Object selected = comboBox.getSelectedItem();	                
		                String command = event.getActionCommand();
		                selectedCakeCategory=selected.toString();
		                
		                // Detect whether the action command is "comboBoxEdited"
		                // or "comboBoxChanged"
		                 if ("comboBoxChanged".equals(command)) {
		                	 LoadDRTable();
    	 
		                }
		            }
		        });
			
			//frame
			setSize(900,600);
			setLayout(null);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			
			//background
			ImageIcon background_image=new ImageIcon(Toolkit.getDefaultToolkit().getImage("./src/main/resources/images/newPozadina3.jpg"));
			Image img=background_image.getImage();
			Image temp_image=img.getScaledInstance(900,600, Image.SCALE_SMOOTH);
			background_image=new ImageIcon(temp_image);
			JLabel background=new JLabel("",background_image,JLabel.CENTER);
			background.setBounds(0,0,900,600);
			background.add(filterPanel);
			background.add(mainPanel);
			background.add(heading);
			add(background);
			
			setVisible(true);
	}
	
public static void main(String args[]) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					priceFilter.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
