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
public class searchFiltersPage extends JFrame {
	
	// Directory where we've stored the local data files, such as cakeOntology.owl
    public static final String SOURCE = "./src/main/resources/data/";
    
    // Cake ontology namespace
    public static final String Cakes_n = "http://www.semanticweb.org/mehotabakovic/ontologies/2019/9/untitled-ontology-5#";
    
	private JPanel mainPanel;
	
	
	private JPanel filterPanel;
	
	JLabel cakeLabel;

	private JScrollPane sp=new JScrollPane();

			
	public static searchFiltersPage searchFilterPage=new searchFiltersPage();
	
	private JTextField txtByName;
	
	public JComboBox cakeTypeCombo;
	
	public JComboBox frostingCombo;
	
	public JComboBox spongeFlavourCombo;
	
	public JComboBox dietaryRequirementCombo;
	
	public String query_text;
	

	String selectedDietaryRequirement="Dietary requirement";
	
	String selectedCakeType="Cake type";
	
	String selectedFrostingType="Frosting type";
	
	String selectedSpongeFlavour="Sponge flavour";
	
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
		cakeTypeCombo.setBounds(50,70,140,50);
		filterPanel.add(cakeTypeCombo);
		
	}
	
public void LoadFrosting() {
	
	OntModel mm = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
	
	FileManager.get().readModel( mm, SOURCE + "cakeOntology.owl" );
	
	String prefixx = "prefix ck: <" + Cakes_n + ">\n" +
            		"prefix rdfs: <" + RDFS.getURI() + ">\n" +
            		"prefix owl: <" + OWL.getURI() + ">\n";

	String query_textt=  prefixx +
			"SELECT ?name\r\n" + 
			"WHERE {?frosting a ck:Frosting.?frosting ck:frostingName ?name.}";
	
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
    ArrayList<String>  frosting_values = new ArrayList<String>();
    frosting_values.add("Frosting type");
    for (String[] var : valuesCT) 
    { 
    	frosting_values.add(var[0].toString());
    	
    }

	
    String[] array = frosting_values.toArray(new String[frosting_values.size()]);
	frostingCombo = new JComboBox(array);
	frostingCombo.setBounds(200,70,140,50);
	filterPanel.add(frostingCombo);
}

public void LoadSpongeFlavour() {
	
OntModel mm = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
	
	FileManager.get().readModel( mm, SOURCE + "cakeOntology.owl" );
	
	String prefixx = "prefix ck: <" + Cakes_n + ">\n" +
            		"prefix rdfs: <" + RDFS.getURI() + ">\n" +
            		"prefix owl: <" + OWL.getURI() + ">\n";

	String query_textt=  prefixx +
			"SELECT ?name\r\n" + 
			"WHERE {?sf a ck:SpongeFlavour.?sf ck:spongeFlavourName ?name.}";
	
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
    ArrayList<String>  spongeFlavour_values = new ArrayList<String>();
    spongeFlavour_values.add("Sponge flavour");
    for (String[] var : valuesCT) 
    { 
    	spongeFlavour_values.add(var[0].toString());
    	
    }

	
    String[] array = spongeFlavour_values.toArray(new String[spongeFlavour_values.size()]);
    spongeFlavourCombo = new JComboBox(array);
    spongeFlavourCombo.setBounds(350,70,140,50);
    filterPanel.add(spongeFlavourCombo);
}

public void LoadDietaryRequirement() {
OntModel mm = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
		
		FileManager.get().readModel( mm, SOURCE + "cakeOntology.owl" );
		
		String prefixx = "prefix ck: <" + Cakes_n + ">\n" +
                		"prefix rdfs: <" + RDFS.getURI() + ">\n" +
                		"prefix owl: <" + OWL.getURI() + ">\n";

		String query_textt=  prefixx +
				"SELECT ?name\r\n" + 
				"WHERE {?sf a ck:DietaryRequirements.?sf ck:dietaryName ?name.}";
		
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
        ArrayList<String>  dietaryRequirements_values = new ArrayList<String>();
        dietaryRequirements_values.add("Dietary requirement");
        for (String[] var : valuesCT) 
        { 
        	dietaryRequirements_values.add(var[0].toString());
        	
        }
	
		
        String[] array = dietaryRequirements_values.toArray(new String[dietaryRequirements_values.size()]);
        dietaryRequirementCombo = new JComboBox(array);
        dietaryRequirementCombo.setBounds(500,70,140,50);
        filterPanel.add(dietaryRequirementCombo);
	}
	
public  void LoadData() {
	
    OntModel m = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
		
		//read ontology model
		FileManager.get().readModel( m, SOURCE + "cakeOntology.owl" );
		
		String prefix = "prefix ck: <" + Cakes_n + ">\n" +
             		"prefix rdfs: <" + RDFS.getURI() + ">\n" +
             		"prefix owl: <" + OWL.getURI() + ">\n";

		 query_text=  prefix +
					"SELECT ?name ?label  (strafter(?label,\"#\") as ?cakeCategory) ?price  ?shopName ?firstAddress ?city ?phoneNumber (GROUP_CONCAT(DISTINCT ?cakeTypeName;SEPARATOR=\",\") AS ?cakeTypename) (GROUP_CONCAT(DISTINCT ?frostingName;SEPARATOR=\",\") AS ?frostingname)(GROUP_CONCAT(DISTINCT ?spongeName;SEPARATOR=\",\") AS ?spongeFlavourname)(GROUP_CONCAT( DISTINCT ?dietaryName;SEPARATOR=\",\") AS ?dietaryname)\r\n" + 
					"WHERE{{?Cake a ?cCake.?cCake rdfs:subClassOf ck:Cake.?Cake ck:cakeName ?name.?Cake ck:cakePrice ?price.?Cake ck:cakePicture ?pictureLink.?CakeBranch ck:sells ?Cake.?CakeBranch ck:branchphoneNumber ?phoneNumber.?CakeBranch ck:isLocatedOnAddress ?Address.?Address ck:addressLine1 ?firstAddress.?City ck:hasAddress ?Address.?City ck:cityName ?city.?CakeShop a ?cCakeShop.?cCakeShop rdfs:subClassOf ck:CakeShop.?CakeShop ck:hasCakeBranch ?CakeBranch.?CakeShop ck:cakeShopName ?shopName.OPTIONAL{?Cake ck:hasCakeType ?cakeType.?cakeType ck:cakeTypeName ?cakeTypeName}.OPTIONAL{?Cake ck:hasFrosting ?frosting.?frosting ck:frostingName ?frostingName}.OPTIONAL{?Cake ck:hasSpongeFlavour ?spongeFlavour.?spongeFlavour ck:spongeFlavourName ?spongeName}. OPTIONAL{?Cake ck:hasDietaryRequirement ?dietaryRequirement. ?dietaryRequirement ck:dietaryName ?dietaryName}.BIND(str(?cCake) AS ?label)}}";	           
		            query_text +="GROUP BY ?name ?label ?cakeCategory ?price  ?shopName ?firstAddress ?city ?phoneNumber " ;  	
		            query_text +="ORDER BY ASC(?name)" ;  	
		          
		            
		Query query = QueryFactory.create( query_text );
        QueryExecution qexec = QueryExecutionFactory.create( query, m );
    
     // Create Arrays for Table Headers and Table Values 
     List<String> columns = new ArrayList<String>();
     List<String[]> values = new ArrayList<String[]>();

     columns.add("Cake");
     columns.add("Category");
     columns.add("Price");
     columns.add("Cake shop");
     columns.add("Address");
     columns.add("City");
     columns.add("Phone number");
     columns.add("Cake type");

     try {
         ResultSet results = qexec.execSelect();
         int i = 0;
         while ( results.hasNext() ) {
             QuerySolution qs = results.next();

             //Assign query data to array. That will populate JTable 
             values.add(new String[] {qs.get("name").toString(),qs.get("cakeCategory").toString(),"£"+ qs.get("price").toString().substring(0, qs.get("price").toString().indexOf("^")),qs.get("shopName").toString(), qs.get("firstAddress").toString(), qs.get("city").toString(), qs.get("phoneNumber").toString(),qs.get("cakeTypename").toString()});
             
     
             i++;
         }
         
      //Create Table and tableModel
         TableModel  tableModel = new DefaultTableModel(values.toArray(new Object[][] {}), columns.toArray());
        final JTable table = new JTable(tableModel);
         table.setForeground(Color.DARK_GRAY);
         table.setBackground(Color.WHITE);
         table.setRowHeight(35);		
         sp.setViewportView(table);		           
         sp.setBounds(10, 10, 780, 317);
         
         //---------------------
         
         // table Listener that is used for passing clicked value from table to another class- ingredientPage
         //this will be explained later
         table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
             public void valueChanged(ListSelectionEvent event) {
              
             	
             	String  cakeNamee=table.getValueAt(table.getSelectedRow(), 0).toString();
             	new ingredientPage(cakeNamee).setVisible(true);
             	searchFiltersPage.searchFilterPage.setVisible(false);
                
             }
         });
				
         
        mainPanel.add(sp);
        mainPanel.validate();
        mainPanel.repaint();
         

     }
     finally {
         qexec.close();
     }
	
	}
	
public void LoadDRTable() {
	
	
	//read ontology model
	//create instance of OntModel class
	OntModel m = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );

	FileManager.get().readModel( m, SOURCE + "cakeOntology.owl" );
//	String query_text;
	String prefix = "prefix ck: <" + Cakes_n + ">\n" +
            		"prefix rdfs: <" + RDFS.getURI() + ">\n" +
            		"prefix owl: <" + OWL.getURI() + ">\n";
	
	query_text=  prefix +
			"SELECT ?name ?label  (strafter(?label,\"#\") as ?cakeCategory) ?price  ?shopName ?firstAddress ?city ?phoneNumber (GROUP_CONCAT(DISTINCT ?cakeTypeName;SEPARATOR=\",\") AS ?cakeTypename) (GROUP_CONCAT(DISTINCT ?frostingName;SEPARATOR=\",\") AS ?frostingname)(GROUP_CONCAT(DISTINCT ?spongeName;SEPARATOR=\",\") AS ?spongeFlavourname)(GROUP_CONCAT( DISTINCT ?dietaryName;SEPARATOR=\",\") AS ?dietaryname)\r\n" + 
			"WHERE{{?Cake a ?cCake.?cCake rdfs:subClassOf ck:Cake.?Cake ck:cakeName ?name.?Cake ck:cakePrice ?price.?Cake ck:cakePicture ?pictureLink.?CakeBranch ck:sells ?Cake.?CakeBranch ck:branchphoneNumber ?phoneNumber.?CakeBranch ck:isLocatedOnAddress ?Address.?Address ck:addressLine1 ?firstAddress.?City ck:hasAddress ?Address.?City ck:cityName ?city.?CakeShop a ?cCakeShop.?cCakeShop rdfs:subClassOf ck:CakeShop.?CakeShop ck:hasCakeBranch ?CakeBranch.?CakeShop ck:cakeShopName ?shopName.OPTIONAL{?Cake ck:hasCakeType ?cakeType.?cakeType ck:cakeTypeName ?cakeTypeName}.OPTIONAL{?Cake ck:hasFrosting ?frosting.?frosting ck:frostingName ?frostingName}.OPTIONAL{?Cake ck:hasSpongeFlavour ?spongeFlavour.?spongeFlavour ck:spongeFlavourName ?spongeName}. OPTIONAL{?Cake ck:hasDietaryRequirement ?dietaryRequirement. ?dietaryRequirement ck:dietaryName ?dietaryName}.BIND(str(?cCake) AS ?label)}";	           
	
	if(selectedDietaryRequirement.contentEquals("Dietary requirement") && selectedCakeType.contentEquals("Cake type") && selectedFrostingType.contentEquals("Frosting type")&& selectedSpongeFlavour.contentEquals("Sponge flavour")) {
	                 
	}
	else if(!selectedDietaryRequirement.contentEquals("Dietary requirement") && selectedCakeType.contentEquals("Cake type")&& selectedFrostingType.contentEquals("Frosting type")&& selectedSpongeFlavour.contentEquals("Sponge flavour")) {
		query_text+="FILTER(regex(str(?dietaryName),"+"\""+ selectedDietaryRequirement+"\""+"))} ";
		
	}
	else if(selectedDietaryRequirement.contentEquals("Dietary requirement") && !selectedCakeType.contentEquals("Cake type")&& selectedFrostingType.contentEquals("Frosting type")&& selectedSpongeFlavour.contentEquals("Sponge flavour")) {
	
		query_text+="FILTER(regex(str(?cakeTypeName),"+"\""+ selectedCakeType+"\""+"))} ";
	}
	else if(!selectedDietaryRequirement.contentEquals("Dietary requirement") && !selectedCakeType.contentEquals("Cake type")&& selectedFrostingType.contentEquals("Frosting type")&& selectedSpongeFlavour.contentEquals("Sponge flavour")) {
		
		query_text+="FILTER(regex(str(?cakeTypeName),"+"\""+ selectedCakeType+"\""+")"+
		          "&& regex(str(?dietaryName),"+"\""+selectedDietaryRequirement+"\""+")"+")} ";
		
	}
	else if(selectedDietaryRequirement.contentEquals("Dietary requirement") && selectedCakeType.contentEquals("Cake type")&& !selectedFrostingType.contentEquals("Frosting type")&& selectedSpongeFlavour.contentEquals("Sponge flavour")) {
		
		query_text+="FILTER(regex(str(?frostingName),"+"\""+ selectedFrostingType+"\""+"))} ";
	}
	else if(selectedDietaryRequirement.contentEquals("Dietary requirement") && !selectedCakeType.contentEquals("Cake type")&& !selectedFrostingType.contentEquals("Frosting type")&& selectedSpongeFlavour.contentEquals("Sponge flavour")) {
	
		query_text+="FILTER(regex(str(?cakeTypeName),"+"\""+ selectedCakeType+"\""+")"+
		          "&& regex(str(?frostingName),"+"\""+selectedFrostingType+"\""+")"+")} ";
	}
	else if (!selectedDietaryRequirement.contentEquals("Dietary requirement") && selectedCakeType.contentEquals("Cake type")&& !selectedFrostingType.contentEquals("Frosting type")&& selectedSpongeFlavour.contentEquals("Sponge flavour")){
		
		query_text+="FILTER(regex(str(?dietaryName),"+"\""+ selectedDietaryRequirement+"\""+")"+
		          "&& regex(str(?frostingName),"+"\""+selectedFrostingType+"\""+")"+")} ";
	}
	else if(selectedDietaryRequirement.contentEquals("Dietary requirement") && selectedCakeType.contentEquals("Cake type") && selectedFrostingType.contentEquals("Frosting type")&& !selectedSpongeFlavour.contentEquals("Sponge flavour")) {
		
		query_text+="FILTER(regex(str(?spongeName),"+"\""+ selectedSpongeFlavour+"\""+"))} ";
	}
	else if(selectedDietaryRequirement.contentEquals("Dietary requirement") && selectedCakeType.contentEquals("Cake type") && !selectedFrostingType.contentEquals("Frosting type")&& !selectedSpongeFlavour.contentEquals("Sponge flavour")) {
		
		query_text+="FILTER(regex(str(?spongeName),"+"\""+ selectedSpongeFlavour+"\""+")"+
		          "&& regex(str(?frostingName),"+"\""+selectedFrostingType+"\""+")"+")} ";
	}
	else if(selectedDietaryRequirement.contentEquals("Dietary requirement") && !selectedCakeType.contentEquals("Cake type") && selectedFrostingType.contentEquals("Frosting type")&& !selectedSpongeFlavour.contentEquals("Sponge flavour")) {
		
		query_text+="FILTER(regex(str(?spongeName),"+"\""+ selectedSpongeFlavour+"\""+")"+
		          "&& regex(str(?cakeTypeName),"+"\""+selectedCakeType+"\""+")"+")} ";
	}
	else if(!selectedDietaryRequirement.contentEquals("Dietary requirement") && selectedCakeType.contentEquals("Cake type") && selectedFrostingType.contentEquals("Frosting type")&& !selectedSpongeFlavour.contentEquals("Sponge flavour")) {
		
		query_text+="FILTER(regex(str(?spongeName),"+"\""+ selectedSpongeFlavour+"\""+")"+
		          "&& regex(str(?dietaryName),"+"\""+selectedDietaryRequirement+"\""+")"+")} ";
	}
	else if(!selectedDietaryRequirement.contentEquals("Dietary requirement") && !selectedCakeType.contentEquals("Cake type") && !selectedFrostingType.contentEquals("Frosting type")&& selectedSpongeFlavour.contentEquals("Sponge flavour")) {
	
		query_text+="FILTER(regex(str(?dietaryName),"+"\""+ selectedDietaryRequirement+"\""+")"+
		          "&& regex(str(?frostingName),"+"\""+selectedFrostingType+"\""+")"+
		          "&& regex(str(?cakeTypeName),"+"\""+selectedCakeType+"\""+")"+")} ";
	}
	else if(selectedDietaryRequirement.contentEquals("Dietary requirement") && !selectedCakeType.contentEquals("Cake type") && !selectedFrostingType.contentEquals("Frosting type")&& !selectedSpongeFlavour.contentEquals("Sponge flavour")) {
		
		query_text+="FILTER(regex(str(?spongeName),"+"\""+ selectedSpongeFlavour+"\""+")"+
		          "&& regex(str(?frostingName),"+"\""+selectedFrostingType+"\""+")"+
		          "&& regex(str(?cakeTypeName),"+"\""+selectedCakeType+"\""+")"+	")} ";
	}
	else if(!selectedDietaryRequirement.contentEquals("Dietary requirement") && selectedCakeType.contentEquals("Cake type") && !selectedFrostingType.contentEquals("Frosting type")&& !selectedSpongeFlavour.contentEquals("Sponge flavour")) {
		
		query_text+="FILTER(regex(str(?spongeName),"+"\""+ selectedSpongeFlavour+"\""+")"+
		          "&& regex(str(?frostingName),"+"\""+selectedFrostingType+"\""+")"+
		          "&& regex(str(?dietaryName),"+"\""+selectedDietaryRequirement+"\""+")"+")} ";
	}
	else if(!selectedDietaryRequirement.contentEquals("Dietary requirement") && !selectedCakeType.contentEquals("Cake type") && selectedFrostingType.contentEquals("Frosting type")&& !selectedSpongeFlavour.contentEquals("Sponge flavour")) {
		
		query_text+="FILTER(regex(str(?spongeName),"+"\""+ selectedSpongeFlavour+"\""+")"+
		          "&& regex(str(?cakeTypeName),"+"\""+selectedCakeType+"\""+")"+
		          "&& regex(str(?dietaryName),"+"\""+selectedDietaryRequirement+"\""+")"+")} ";
	}
	else {
		
		query_text+="FILTER(regex(str(?spongeName),"+"\""+ selectedSpongeFlavour+"\""+")"+
		          "&& regex(str(?cakeTypeName),"+"\""+selectedCakeType+"\""+")"+
		          "&& regex(str(?frostingName),"+"\""+selectedFrostingType+"\""+")"+
		          "&& regex(str(?dietaryName),"+"\""+selectedDietaryRequirement+"\""+")"+")} ";
		
	}
	  query_text +="GROUP BY ?name ?label ?cakeCategory ?price  ?shopName ?firstAddress ?city ?phoneNumber " ; 
	
	 Query query = QueryFactory.create( query_text );
        QueryExecution qexec = QueryExecutionFactory.create( query, m );
       
        /*************************************** Create Arrays for Table Headers and Table Values **********************************/ 
        List<String> columns = new ArrayList<String>();
        List<String[]> values = new ArrayList<String[]>();

        columns.add("Cake");
        columns.add("Category");
        columns.add("Price");
        columns.add("Cake shop");
        columns.add("Address");
        columns.add("City");
        columns.add("Phone number");
        columns.add("Cake type");
   
        /*******************************************************************************************************************************/

        try {
            ResultSet results = qexec.execSelect();
            int i = 0;
            while ( results.hasNext() ) {
                QuerySolution qs = results.next();

                /****************************  Assign query data to array. That will populate JTable **************************/
                values.add(new String[] {qs.get("name").toString(),qs.get("cakeCategory").toString(),"£"+ qs.get("price").toString().substring(0, qs.get("price").toString().indexOf("^")),qs.get("shopName").toString(), qs.get("firstAddress").toString(), qs.get("city").toString(), qs.get("phoneNumber").toString(),qs.get("cakeTypename").toString()});
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
            sp.setBounds(10, 10, 780, 317);
            
            
            
            table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
                public void valueChanged(ListSelectionEvent event) {
                 
                	
                	String  cakeNamee=table.getValueAt(table.getSelectedRow(), 0).toString();
                	new ingredientPage(cakeNamee).setVisible(true);
                	App.people_frame.setVisible(false);

                }
            });
           
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

	searchFiltersPage(){
		
		//font
				Font f=new Font("Serif",Font.BOLD,30);
				
				
				//header
				JPanel heading;
				heading=new JPanel();

				heading.setBackground(new Color(255,255,255,30));
				heading.setBounds(0,0,900,60);
				JLabel name=new JLabel("Search and Filter Shop Analyzer");
				name.setForeground(Color.WHITE);
				name.setBounds(200,50,400,50);
				name.setFont(f);
				heading.add(name);
				
				 filterPanel=new JPanel();
				 filterPanel.setLayout(null);
				setSize(850,130);
				filterPanel.setBackground(new Color(255,255,255,30));
				filterPanel.setBounds(20,70,850,130);
				
				   mainPanel=new JPanel();
					mainPanel.setLayout(null);
					setSize(800,300);
					mainPanel.setBackground(new Color(255,255,255,30));
					mainPanel.setBounds(50,225,800,300);
			 
					// panel- Search input 
					txtByName = new JTextField();
					txtByName.setFont(new Font("Tahoma", Font.PLAIN, 18));
					txtByName.setToolTipText("Enter Name");
					txtByName.setBounds(50, 10, 355, 51);
					txtByName.setColumns(10);
					filterPanel.add(txtByName);
					
					
				
				
				     //Search Button
					JButton btnSearchPeople = new JButton("Search");
					btnSearchPeople.setBackground(SystemColor.controlHighlight);
					btnSearchPeople.setFocusable(false);
					btnSearchPeople.setFocusTraversalKeysEnabled(false);
					btnSearchPeople.setFont(new Font("Tahoma", Font.PLAIN, 20));
					btnSearchPeople.setBounds(410, 10, 140, 51);
					btnSearchPeople.setFocusPainted(false);
					filterPanel.add(btnSearchPeople);
					
				
					// Load  combo box
					
					LoadCakeType();
					LoadFrosting();
					LoadSpongeFlavour();
					LoadDietaryRequirement();
					LoadData();
					
					///------Create an ActionListener for DietaryRequirement JComboBox component-----------------------------------
					
					  dietaryRequirementCombo.addActionListener(new ActionListener() {
				            public void actionPerformed(ActionEvent event) {
				                // Get the source of the component, which is our combo
				                // box.
				                JComboBox comboBox = (JComboBox) event.getSource();

				                // Print the selected items and the action command.
				                Object selected = comboBox.getSelectedItem();           
				                String command = event.getActionCommand();
				                selectedDietaryRequirement=selected.toString();
			                    
				                
				                // Detect whether the action command is "comboBoxEdited"
				                // or "comboBoxChanged"
				                 if ("comboBoxChanged".equals(command)) {
				                
				                	LoadDRTable();
				                	               	 
				                }
				            }
				        });
					///------Create an ActionListener for CakeType JComboBox component-----------------------------------
						
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
					///------Create an ActionListener for Frosting JComboBox component-----------------------------------
						
					  frostingCombo.addActionListener(new ActionListener() {
				            public void actionPerformed(ActionEvent event) {
				                // Get the source of the component, which is our combo
				                // box.
				                JComboBox comboBox = (JComboBox) event.getSource();

				                // Print the selected items and the action command.
				                Object selected = comboBox.getSelectedItem();	                
				                String command = event.getActionCommand();
				                selectedFrostingType=selected.toString();
				                
				                // Detect whether the action command is "comboBoxEdited"
				                // or "comboBoxChanged"
				                 if ("comboBoxChanged".equals(command)) {
				                	 LoadDRTable();
				                	 
				                }
				            }
				        });
					  spongeFlavourCombo.addActionListener(new ActionListener() {
				            public void actionPerformed(ActionEvent event) {
				                // Get the source of the component, which is our combo
				                // box.
				                JComboBox comboBox = (JComboBox) event.getSource();

				                // Print the selected items and the action command.
				                Object selected = comboBox.getSelectedItem();	                
				                String command = event.getActionCommand();
				                selectedSpongeFlavour=selected.toString();
				                
				                // Detect whether the action command is "comboBoxEdited"
				                // or "comboBoxChanged"
				                 if ("comboBoxChanged".equals(command)) {
				                	 LoadDRTable();
				                	 
				                }
				            }
				        });		
					  
					  
					  btnSearchPeople.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								String country = txtByName.getText().toString().toLowerCase();
								
								//read ontology model
								//create instance of OntModel class
								OntModel m = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
							
								FileManager.get().readModel( m, SOURCE + "cakeOntology.owl" );
								
								String prefix = "prefix ck: <" + Cakes_n + ">\n" +
						                		"prefix rdfs: <" + RDFS.getURI() + ">\n" +
						                		"prefix owl: <" + OWL.getURI() + ">\n";
						
								  
							
						// ontology for search
				//String query_text
								 query_text=  prefix +
										"SELECT ?name ?label  (strafter(?label,\"#\") as ?cakeCategory) ?price  ?shopName ?firstAddress ?city ?phoneNumber (GROUP_CONCAT(DISTINCT ?cakeTypeName;SEPARATOR=\",\") AS ?cakeTypename) (GROUP_CONCAT(DISTINCT ?frostingName;SEPARATOR=\",\") AS ?frostingname)(GROUP_CONCAT(DISTINCT ?spongeName;SEPARATOR=\",\") AS ?spongeFlavourname)(GROUP_CONCAT( DISTINCT ?dietaryName;SEPARATOR=\",\") AS ?dietaryname)\r\n" + 
										"WHERE{{?Cake a ?cCake.?cCake rdfs:subClassOf ck:Cake.?Cake ck:cakeName ?name.?Cake ck:cakePrice ?price.?Cake ck:cakePicture ?pictureLink.?CakeBranch ck:sells ?Cake.?CakeBranch ck:branchphoneNumber ?phoneNumber.?CakeBranch ck:isLocatedOnAddress ?Address.?Address ck:addressLine1 ?firstAddress.?City ck:hasAddress ?Address.?City ck:cityName ?city.?CakeShop a ?cCakeShop.?cCakeShop rdfs:subClassOf ck:CakeShop.?CakeShop ck:hasCakeBranch ?CakeBranch.?CakeShop ck:cakeShopName ?shopName.OPTIONAL{?Cake ck:hasCakeType ?cakeType.?cakeType ck:cakeTypeName ?cakeTypeName}.OPTIONAL{?Cake ck:hasFrosting ?frosting.?frosting ck:frostingName ?frostingName}.OPTIONAL{?Cake ck:hasSpongeFlavour ?spongeFlavour.?spongeFlavour ck:spongeFlavourName ?spongeName}. OPTIONAL{?Cake ck:hasDietaryRequirement ?dietaryRequirement. ?dietaryRequirement ck:dietaryName ?dietaryName}.BIND(str(?cCake) AS ?label)}";	           
							           							
								if(country != null && !country.isEmpty()) {
										query_text += "FILTER(regex(str(?name),\""+country+"\",\"i\")) ";
								}
								query_text +="}GROUP BY ?name ?label ?cakeCategory ?price  ?shopName ?firstAddress ?city ?phoneNumber " ;  	
								query_text +=	" ORDER BY ASC(?name)" ;  
								
								
								
								Query query = QueryFactory.create( query_text );
						        QueryExecution qexec = QueryExecutionFactory.create( query, m );
						       
						        /*************************************** Create Arrays for Table Headers and Table Values **********************************/ 
						        List<String> columns = new ArrayList<String>();
						        List<String[]> values = new ArrayList<String[]>();

						        columns.add("Cake");
						        columns.add("Category");
						        columns.add("Price");
						        columns.add("Cake shop");
						        columns.add("Address");
						        columns.add("City");
						        columns.add("Phone number");
						        columns.add("Cake type");
						        /*******************************************************************************************************************************/

						        try {
						            ResultSet results = qexec.execSelect();
						            int i = 0;
						            while ( results.hasNext() ) {
						                QuerySolution qs = results.next();

						                /****************************  Assign query data to array. That will populate JTable **************************/
						                values.add(new String[] {qs.get("name").toString(),qs.get("cakeCategory").toString(),"£"+ qs.get("price").toString().substring(0, qs.get("price").toString().indexOf("^")),qs.get("shopName").toString(), qs.get("firstAddress").toString(), qs.get("city").toString(), qs.get("phoneNumber").toString(),qs.get("cakeTypename").toString()});
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
						            sp.setBounds(10, 10, 780, 317);
						           
						            
						            table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
						                public void valueChanged(ListSelectionEvent event) {
						                 
						                	
						                	String  cakeNamee=table.getValueAt(table.getSelectedRow(), 0).toString();
						                	new ingredientPage(cakeNamee).setVisible(true);
						                	App.people_frame.setVisible(false);
						                   
						                }
						            });
						          /*********************************************************************************/
						        }
						        finally {
						            qexec.close();
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
					searchFilterPage.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
