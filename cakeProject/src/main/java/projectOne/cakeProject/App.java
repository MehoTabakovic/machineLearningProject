package projectOne.cakeProject;

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
public class App extends JFrame
{
	// Directory where we've stored the local data files, such as cakeOntology.owl
    public static final String SOURCE = "./src/main/resources/data/";
    
    // Cake ontology namespace
    public static final String Cakes_n = "http://www.semanticweb.org/mehotabakovic/ontologies/2019/9/untitled-ontology-5#";
    
	private JPanel contentPane;
	
	JLabel cakeLabel;

	private JScrollPane sp=new JScrollPane();
			
	public static App people_frame = new App();
	
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

	public static void main(String[] args) {
	
		EventQueue.invokeLater(new Runnable() {
			public void run() {
			
				try {
					people_frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
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
		            System.out.println(query_text);
		            
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
                
                System.out.println(qs.get("name"));
                i++;
            }
            
         /*************************Create Table and tableModel******************************/
            TableModel  tableModel = new DefaultTableModel(values.toArray(new Object[][] {}), columns.toArray());
           final JTable table = new JTable(tableModel);
            table.setForeground(Color.DARK_GRAY);
            table.setBackground(Color.WHITE);
            table.setRowHeight(35);		
            sp.setViewportView(table);		           
            sp.setBounds(50, 180, 800, 317);
            //---------------------
         
            table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
                public void valueChanged(ListSelectionEvent event) {
                 
                	 System.out.println(table.getValueAt(table.getSelectedRow(), 0).toString());
                	String  cakeNamee=table.getValueAt(table.getSelectedRow(), 0).toString();
                	new ingredientPage(cakeNamee).setVisible(true);
                	App.people_frame.setVisible(false);
                   
                }
            });
				
            
            //------------------
            
            contentPane.add(sp);
            contentPane.repaint();
            
            
          /*********************************************************************************/
        }
        finally {
            qexec.close();
        }
	
	}

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

                /****************************  Assign query data to array. That will populate JTable **************************/
                valuesCT.add(new String[] {qs.get("name").toString()});
               /**************************************************************************************************************/
                
                System.out.println(qs.get("name"));
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
		cakeTypeCombo.setBounds(50,105,140,50);
		contentPane.add(cakeTypeCombo);
		
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

                /****************************  Assign query data to array. That will populate JTable **************************/
                valuesCT.add(new String[] {qs.get("name").toString()});
               /**************************************************************************************************************/
                
                System.out.println(qs.get("name"));
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
		frostingCombo.setBounds(200,105,140,50);
		contentPane.add(frostingCombo);
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

                /****************************  Assign query data to array. That will populate JTable **************************/
                valuesCT.add(new String[] {qs.get("name").toString()});
               /**************************************************************************************************************/
                
                System.out.println(qs.get("name"));
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
        spongeFlavourCombo.setBounds(350,105,140,50);
		contentPane.add(spongeFlavourCombo);
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

                /****************************  Assign query data to array. That will populate JTable **************************/
                valuesCT.add(new String[] {qs.get("name").toString()});
               /**************************************************************************************************************/
                
                System.out.println(qs.get("name"));
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
        dietaryRequirementCombo.setBounds(495,105,140,50);
		contentPane.add(dietaryRequirementCombo);
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
		
		if(selectedDietaryRequirement.contentEquals("Dietary requirement") && selectedCakeType.contentEquals("Cake type") && selectedFrostingType.contentEquals("Frosting type")&& selectedSpongeFlavour.contentEquals("Sponge flavour")) {
			query_text=  prefix +
					"SELECT ?name ?label  (strafter(?label,\"#\") as ?cakeCategory) ?price  ?shopName ?firstAddress ?city ?phoneNumber (GROUP_CONCAT(DISTINCT ?cakeTypeName;SEPARATOR=\",\") AS ?cakeTypename) (GROUP_CONCAT(DISTINCT ?frostingName;SEPARATOR=\",\") AS ?frostingname)(GROUP_CONCAT(DISTINCT ?spongeName;SEPARATOR=\",\") AS ?spongeFlavourname)(GROUP_CONCAT( DISTINCT ?dietaryName;SEPARATOR=\",\") AS ?dietaryname)\r\n" + 
					"WHERE{{?Cake a ?cCake.?cCake rdfs:subClassOf ck:Cake.?Cake ck:cakeName ?name.?Cake ck:cakePrice ?price.?Cake ck:cakePicture ?pictureLink.?CakeBranch ck:sells ?Cake.?CakeBranch ck:branchphoneNumber ?phoneNumber.?CakeBranch ck:isLocatedOnAddress ?Address.?Address ck:addressLine1 ?firstAddress.?City ck:hasAddress ?Address.?City ck:cityName ?city.?CakeShop a ?cCakeShop.?cCakeShop rdfs:subClassOf ck:CakeShop.?CakeShop ck:hasCakeBranch ?CakeBranch.?CakeShop ck:cakeShopName ?shopName.OPTIONAL{?Cake ck:hasCakeType ?cakeType.?cakeType ck:cakeTypeName ?cakeTypeName}.OPTIONAL{?Cake ck:hasFrosting ?frosting.?frosting ck:frostingName ?frostingName}.OPTIONAL{?Cake ck:hasSpongeFlavour ?spongeFlavour.?spongeFlavour ck:spongeFlavourName ?spongeName}. OPTIONAL{?Cake ck:hasDietaryRequirement ?dietaryRequirement. ?dietaryRequirement ck:dietaryName ?dietaryName}.BIND(str(?cCake) AS ?label)}}";	           
		            query_text +="GROUP BY ?name ?label ?cakeCategory ?price  ?shopName ?firstAddress ?city ?phoneNumber " ;  	
		            query_text +="ORDER BY ASC(?name)" ;  
		            System.out.println(query_text);
		}
		else if(!selectedDietaryRequirement.contentEquals("Dietary requirement") && selectedCakeType.contentEquals("Cake type")&& selectedFrostingType.contentEquals("Frosting type")&& selectedSpongeFlavour.contentEquals("Sponge flavour")) {
			query_text=  prefix +
					"SELECT ?name ?label  (strafter(?label,\"#\") as ?cakeCategory) ?price  ?shopName ?firstAddress ?city ?phoneNumber (GROUP_CONCAT(DISTINCT ?cakeTypeName;SEPARATOR=\",\") AS ?cakeTypename) (GROUP_CONCAT(DISTINCT ?frostingName;SEPARATOR=\",\") AS ?frostingname)(GROUP_CONCAT(DISTINCT ?spongeName;SEPARATOR=\",\") AS ?spongeFlavourname)(GROUP_CONCAT( DISTINCT ?dietaryName;SEPARATOR=\",\") AS ?dietaryname)\r\n" + 
					"WHERE{{?Cake a ?cCake.?cCake rdfs:subClassOf ck:Cake.?Cake ck:cakeName ?name.?Cake ck:cakePrice ?price.?Cake ck:cakePicture ?pictureLink.?CakeBranch ck:sells ?Cake.?CakeBranch ck:branchphoneNumber ?phoneNumber.?CakeBranch ck:isLocatedOnAddress ?Address.?Address ck:addressLine1 ?firstAddress.?City ck:hasAddress ?Address.?City ck:cityName ?city.?CakeShop a ?cCakeShop.?cCakeShop rdfs:subClassOf ck:CakeShop.?CakeShop ck:hasCakeBranch ?CakeBranch.?CakeShop ck:cakeShopName ?shopName.OPTIONAL{?Cake ck:hasCakeType ?cakeType.?cakeType ck:cakeTypeName ?cakeTypeName}.OPTIONAL{?Cake ck:hasFrosting ?frosting.?frosting ck:frostingName ?frostingName}.OPTIONAL{?Cake ck:hasSpongeFlavour ?spongeFlavour.?spongeFlavour ck:spongeFlavourName ?spongeName}. OPTIONAL{?Cake ck:hasDietaryRequirement ?dietaryRequirement. ?dietaryRequirement ck:dietaryName ?dietaryName}.BIND(str(?cCake) AS ?label)}";	           
			query_text+="FILTER(regex(str(?dietaryName),"+"\""+ selectedDietaryRequirement+"\""+
					"))} "+"GROUP BY ?name ?label ?cakeCategory ?price  ?shopName ?firstAddress ?city ?phoneNumber";
			
		}
		else if(selectedDietaryRequirement.contentEquals("Dietary requirement") && !selectedCakeType.contentEquals("Cake type")&& selectedFrostingType.contentEquals("Frosting type")&& selectedSpongeFlavour.contentEquals("Sponge flavour")) {
		
			query_text=  prefix +
					"SELECT ?name ?label  (strafter(?label,\"#\") as ?cakeCategory) ?price  ?shopName ?firstAddress ?city ?phoneNumber (GROUP_CONCAT(DISTINCT ?cakeTypeName;SEPARATOR=\",\") AS ?cakeTypename) (GROUP_CONCAT(DISTINCT ?frostingName;SEPARATOR=\",\") AS ?frostingname)(GROUP_CONCAT(DISTINCT ?spongeName;SEPARATOR=\",\") AS ?spongeFlavourname)(GROUP_CONCAT( DISTINCT ?dietaryName;SEPARATOR=\",\") AS ?dietaryname)\r\n" + 
					"WHERE{{?Cake a ?cCake.?cCake rdfs:subClassOf ck:Cake.?Cake ck:cakeName ?name.?Cake ck:cakePrice ?price.?Cake ck:cakePicture ?pictureLink.?CakeBranch ck:sells ?Cake.?CakeBranch ck:branchphoneNumber ?phoneNumber.?CakeBranch ck:isLocatedOnAddress ?Address.?Address ck:addressLine1 ?firstAddress.?City ck:hasAddress ?Address.?City ck:cityName ?city.?CakeShop a ?cCakeShop.?cCakeShop rdfs:subClassOf ck:CakeShop.?CakeShop ck:hasCakeBranch ?CakeBranch.?CakeShop ck:cakeShopName ?shopName.OPTIONAL{?Cake ck:hasCakeType ?cakeType.?cakeType ck:cakeTypeName ?cakeTypeName}.OPTIONAL{?Cake ck:hasFrosting ?frosting.?frosting ck:frostingName ?frostingName}.OPTIONAL{?Cake ck:hasSpongeFlavour ?spongeFlavour.?spongeFlavour ck:spongeFlavourName ?spongeName}. OPTIONAL{?Cake ck:hasDietaryRequirement ?dietaryRequirement. ?dietaryRequirement ck:dietaryName ?dietaryName}.BIND(str(?cCake) AS ?label)}";	           
			query_text+="FILTER(regex(str(?cakeTypeName),"+"\""+ selectedCakeType+"\""+
					"))} "+"GROUP BY ?name ?label ?cakeCategory ?price  ?shopName ?firstAddress ?city ?phoneNumber";
		}
		else if(!selectedDietaryRequirement.contentEquals("Dietary requirement") && !selectedCakeType.contentEquals("Cake type")&& selectedFrostingType.contentEquals("Frosting type")&& selectedSpongeFlavour.contentEquals("Sponge flavour")) {
			query_text=  prefix + 	
					"SELECT ?name ?label  (strafter(?label,\"#\") as ?cakeCategory) ?price  ?shopName ?firstAddress ?city ?phoneNumber (GROUP_CONCAT(DISTINCT ?cakeTypeName;SEPARATOR=\",\") AS ?cakeTypename) (GROUP_CONCAT(DISTINCT ?frostingName;SEPARATOR=\",\") AS ?frostingname)(GROUP_CONCAT(DISTINCT ?spongeName;SEPARATOR=\",\") AS ?spongeFlavourname)(GROUP_CONCAT( DISTINCT ?dietaryName;SEPARATOR=\",\") AS ?dietaryname)\r\n" + 
					"WHERE{{?Cake a ?cCake.?cCake rdfs:subClassOf ck:Cake.?Cake ck:cakeName ?name.?Cake ck:cakePrice ?price.?Cake ck:cakePicture ?pictureLink.?CakeBranch ck:sells ?Cake.?CakeBranch ck:branchphoneNumber ?phoneNumber.?CakeBranch ck:isLocatedOnAddress ?Address.?Address ck:addressLine1 ?firstAddress.?City ck:hasAddress ?Address.?City ck:cityName ?city.?CakeShop a ?cCakeShop.?cCakeShop rdfs:subClassOf ck:CakeShop.?CakeShop ck:hasCakeBranch ?CakeBranch.?CakeShop ck:cakeShopName ?shopName.OPTIONAL{?Cake ck:hasCakeType ?cakeType.?cakeType ck:cakeTypeName ?cakeTypeName}.OPTIONAL{?Cake ck:hasFrosting ?frosting.?frosting ck:frostingName ?frostingName}.OPTIONAL{?Cake ck:hasSpongeFlavour ?spongeFlavour.?spongeFlavour ck:spongeFlavourName ?spongeName}. OPTIONAL{?Cake ck:hasDietaryRequirement ?dietaryRequirement. ?dietaryRequirement ck:dietaryName ?dietaryName}.BIND(str(?cCake) AS ?label)}";	           
			query_text+="FILTER(regex(str(?cakeTypeName),"+"\""+ selectedCakeType+"\""+")"+
			          "&& regex(str(?dietaryName),"+"\""+selectedDietaryRequirement+"\""+")"+
					")} "+"GROUP BY ?name ?label ?cakeCategory ?price  ?shopName ?firstAddress ?city ?phoneNumber";
			
		}
		else if(selectedDietaryRequirement.contentEquals("Dietary requirement") && selectedCakeType.contentEquals("Cake type")&& !selectedFrostingType.contentEquals("Frosting type")&& selectedSpongeFlavour.contentEquals("Sponge flavour")) {
			
			query_text=  prefix +
					"SELECT ?name ?label  (strafter(?label,\"#\") as ?cakeCategory) ?price  ?shopName ?firstAddress ?city ?phoneNumber (GROUP_CONCAT(DISTINCT ?cakeTypeName;SEPARATOR=\",\") AS ?cakeTypename) (GROUP_CONCAT(DISTINCT ?frostingName;SEPARATOR=\",\") AS ?frostingname)(GROUP_CONCAT(DISTINCT ?spongeName;SEPARATOR=\",\") AS ?spongeFlavourname)(GROUP_CONCAT( DISTINCT ?dietaryName;SEPARATOR=\",\") AS ?dietaryname)\r\n" + 
					"WHERE{{?Cake a ?cCake.?cCake rdfs:subClassOf ck:Cake.?Cake ck:cakeName ?name.?Cake ck:cakePrice ?price.?Cake ck:cakePicture ?pictureLink.?CakeBranch ck:sells ?Cake.?CakeBranch ck:branchphoneNumber ?phoneNumber.?CakeBranch ck:isLocatedOnAddress ?Address.?Address ck:addressLine1 ?firstAddress.?City ck:hasAddress ?Address.?City ck:cityName ?city.?CakeShop a ?cCakeShop.?cCakeShop rdfs:subClassOf ck:CakeShop.?CakeShop ck:hasCakeBranch ?CakeBranch.?CakeShop ck:cakeShopName ?shopName.OPTIONAL{?Cake ck:hasCakeType ?cakeType.?cakeType ck:cakeTypeName ?cakeTypeName}.OPTIONAL{?Cake ck:hasFrosting ?frosting.?frosting ck:frostingName ?frostingName}.OPTIONAL{?Cake ck:hasSpongeFlavour ?spongeFlavour.?spongeFlavour ck:spongeFlavourName ?spongeName}. OPTIONAL{?Cake ck:hasDietaryRequirement ?dietaryRequirement. ?dietaryRequirement ck:dietaryName ?dietaryName}.BIND(str(?cCake) AS ?label)}";	           
			query_text+="FILTER(regex(str(?frostingName),"+"\""+ selectedFrostingType+"\""+
					"))} "+"GROUP BY ?name ?label ?cakeCategory ?price  ?shopName ?firstAddress ?city ?phoneNumber";
		}
		else if(selectedDietaryRequirement.contentEquals("Dietary requirement") && !selectedCakeType.contentEquals("Cake type")&& !selectedFrostingType.contentEquals("Frosting type")&& selectedSpongeFlavour.contentEquals("Sponge flavour")) {
			query_text=  prefix + 	
					"SELECT ?name ?label  (strafter(?label,\"#\") as ?cakeCategory) ?price  ?shopName ?firstAddress ?city ?phoneNumber (GROUP_CONCAT(DISTINCT ?cakeTypeName;SEPARATOR=\",\") AS ?cakeTypename) (GROUP_CONCAT(DISTINCT ?frostingName;SEPARATOR=\",\") AS ?frostingname)(GROUP_CONCAT(DISTINCT ?spongeName;SEPARATOR=\",\") AS ?spongeFlavourname)(GROUP_CONCAT( DISTINCT ?dietaryName;SEPARATOR=\",\") AS ?dietaryname)\r\n" + 
					"WHERE{{?Cake a ?cCake.?cCake rdfs:subClassOf ck:Cake.?Cake ck:cakeName ?name.?Cake ck:cakePrice ?price.?Cake ck:cakePicture ?pictureLink.?CakeBranch ck:sells ?Cake.?CakeBranch ck:branchphoneNumber ?phoneNumber.?CakeBranch ck:isLocatedOnAddress ?Address.?Address ck:addressLine1 ?firstAddress.?City ck:hasAddress ?Address.?City ck:cityName ?city.?CakeShop a ?cCakeShop.?cCakeShop rdfs:subClassOf ck:CakeShop.?CakeShop ck:hasCakeBranch ?CakeBranch.?CakeShop ck:cakeShopName ?shopName.OPTIONAL{?Cake ck:hasCakeType ?cakeType.?cakeType ck:cakeTypeName ?cakeTypeName}.OPTIONAL{?Cake ck:hasFrosting ?frosting.?frosting ck:frostingName ?frostingName}.OPTIONAL{?Cake ck:hasSpongeFlavour ?spongeFlavour.?spongeFlavour ck:spongeFlavourName ?spongeName}. OPTIONAL{?Cake ck:hasDietaryRequirement ?dietaryRequirement. ?dietaryRequirement ck:dietaryName ?dietaryName}.BIND(str(?cCake) AS ?label)}";	           
			query_text+="FILTER(regex(str(?cakeTypeName),"+"\""+ selectedCakeType+"\""+")"+
			          "&& regex(str(?frostingName),"+"\""+selectedFrostingType+"\""+")"+
					")} "+"GROUP BY ?name ?label  ?cakeCategory ?price  ?shopName ?firstAddress ?city ?phoneNumber";
		}
		else if (!selectedDietaryRequirement.contentEquals("Dietary requirement") && selectedCakeType.contentEquals("Cake type")&& !selectedFrostingType.contentEquals("Frosting type")&& selectedSpongeFlavour.contentEquals("Sponge flavour")){
			query_text=  prefix +  
					"SELECT ?name ?label  (strafter(?label,\"#\") as ?cakeCategory) ?price  ?shopName ?firstAddress ?city ?phoneNumber (GROUP_CONCAT(DISTINCT ?cakeTypeName;SEPARATOR=\",\") AS ?cakeTypename) (GROUP_CONCAT(DISTINCT ?frostingName;SEPARATOR=\",\") AS ?frostingname)(GROUP_CONCAT(DISTINCT ?spongeName;SEPARATOR=\",\") AS ?spongeFlavourname)(GROUP_CONCAT( DISTINCT ?dietaryName;SEPARATOR=\",\") AS ?dietaryname)\r\n" + 
					"WHERE{{?Cake a ?cCake.?cCake rdfs:subClassOf ck:Cake.?Cake ck:cakeName ?name.?Cake ck:cakePrice ?price.?Cake ck:cakePicture ?pictureLink.?CakeBranch ck:sells ?Cake.?CakeBranch ck:branchphoneNumber ?phoneNumber.?CakeBranch ck:isLocatedOnAddress ?Address.?Address ck:addressLine1 ?firstAddress.?City ck:hasAddress ?Address.?City ck:cityName ?city.?CakeShop a ?cCakeShop.?cCakeShop rdfs:subClassOf ck:CakeShop.?CakeShop ck:hasCakeBranch ?CakeBranch.?CakeShop ck:cakeShopName ?shopName.OPTIONAL{?Cake ck:hasCakeType ?cakeType.?cakeType ck:cakeTypeName ?cakeTypeName}.OPTIONAL{?Cake ck:hasFrosting ?frosting.?frosting ck:frostingName ?frostingName}.OPTIONAL{?Cake ck:hasSpongeFlavour ?spongeFlavour.?spongeFlavour ck:spongeFlavourName ?spongeName}. OPTIONAL{?Cake ck:hasDietaryRequirement ?dietaryRequirement. ?dietaryRequirement ck:dietaryName ?dietaryName}.BIND(str(?cCake) AS ?label)}";	           
			query_text+="FILTER(regex(str(?dietaryName),"+"\""+ selectedDietaryRequirement+"\""+")"+
			          "&& regex(str(?frostingName),"+"\""+selectedFrostingType+"\""+")"+
					")} "+"GROUP BY ?name ?label ?cakeCategory ?price  ?shopName ?firstAddress ?city ?phoneNumber";
		}
		else if(selectedDietaryRequirement.contentEquals("Dietary requirement") && selectedCakeType.contentEquals("Cake type") && selectedFrostingType.contentEquals("Frosting type")&& !selectedSpongeFlavour.contentEquals("Sponge flavour")) {
			query_text=  prefix + 
					"SELECT ?name ?label  (strafter(?label,\"#\") as ?cakeCategory) ?price  ?shopName ?firstAddress ?city ?phoneNumber (GROUP_CONCAT(DISTINCT ?cakeTypeName;SEPARATOR=\",\") AS ?cakeTypename) (GROUP_CONCAT(DISTINCT ?frostingName;SEPARATOR=\",\") AS ?frostingname)(GROUP_CONCAT(DISTINCT ?spongeName;SEPARATOR=\",\") AS ?spongeFlavourname)(GROUP_CONCAT( DISTINCT ?dietaryName;SEPARATOR=\",\") AS ?dietaryname)\r\n" + 
					"WHERE{{?Cake a ?cCake.?cCake rdfs:subClassOf ck:Cake.?Cake ck:cakeName ?name.?Cake ck:cakePrice ?price.?Cake ck:cakePicture ?pictureLink.?CakeBranch ck:sells ?Cake.?CakeBranch ck:branchphoneNumber ?phoneNumber.?CakeBranch ck:isLocatedOnAddress ?Address.?Address ck:addressLine1 ?firstAddress.?City ck:hasAddress ?Address.?City ck:cityName ?city.?CakeShop a ?cCakeShop.?cCakeShop rdfs:subClassOf ck:CakeShop.?CakeShop ck:hasCakeBranch ?CakeBranch.?CakeShop ck:cakeShopName ?shopName.OPTIONAL{?Cake ck:hasCakeType ?cakeType.?cakeType ck:cakeTypeName ?cakeTypeName}.OPTIONAL{?Cake ck:hasFrosting ?frosting.?frosting ck:frostingName ?frostingName}.OPTIONAL{?Cake ck:hasSpongeFlavour ?spongeFlavour.?spongeFlavour ck:spongeFlavourName ?spongeName}. OPTIONAL{?Cake ck:hasDietaryRequirement ?dietaryRequirement. ?dietaryRequirement ck:dietaryName ?dietaryName}.BIND(str(?cCake) AS ?label)}";	           
			query_text+="FILTER(regex(str(?spongeName),"+"\""+ selectedSpongeFlavour+"\""+
					"))} "+"GROUP BY ?name ?label ?cakeCategory ?price  ?shopName ?firstAddress ?city ?phoneNumber";
		}
		else if(selectedDietaryRequirement.contentEquals("Dietary requirement") && selectedCakeType.contentEquals("Cake type") && !selectedFrostingType.contentEquals("Frosting type")&& !selectedSpongeFlavour.contentEquals("Sponge flavour")) {
			query_text=  prefix +  
					"SELECT ?name ?label  (strafter(?label,\"#\") as ?cakeCategory) ?price  ?shopName ?firstAddress ?city ?phoneNumber (GROUP_CONCAT(DISTINCT ?cakeTypeName;SEPARATOR=\",\") AS ?cakeTypename) (GROUP_CONCAT(DISTINCT ?frostingName;SEPARATOR=\",\") AS ?frostingname)(GROUP_CONCAT(DISTINCT ?spongeName;SEPARATOR=\",\") AS ?spongeFlavourname)(GROUP_CONCAT( DISTINCT ?dietaryName;SEPARATOR=\",\") AS ?dietaryname)\r\n" + 
					"WHERE{{?Cake a ?cCake.?cCake rdfs:subClassOf ck:Cake.?Cake ck:cakeName ?name.?Cake ck:cakePrice ?price.?Cake ck:cakePicture ?pictureLink.?CakeBranch ck:sells ?Cake.?CakeBranch ck:branchphoneNumber ?phoneNumber.?CakeBranch ck:isLocatedOnAddress ?Address.?Address ck:addressLine1 ?firstAddress.?City ck:hasAddress ?Address.?City ck:cityName ?city.?CakeShop a ?cCakeShop.?cCakeShop rdfs:subClassOf ck:CakeShop.?CakeShop ck:hasCakeBranch ?CakeBranch.?CakeShop ck:cakeShopName ?shopName.OPTIONAL{?Cake ck:hasCakeType ?cakeType.?cakeType ck:cakeTypeName ?cakeTypeName}.OPTIONAL{?Cake ck:hasFrosting ?frosting.?frosting ck:frostingName ?frostingName}.OPTIONAL{?Cake ck:hasSpongeFlavour ?spongeFlavour.?spongeFlavour ck:spongeFlavourName ?spongeName}. OPTIONAL{?Cake ck:hasDietaryRequirement ?dietaryRequirement. ?dietaryRequirement ck:dietaryName ?dietaryName}.BIND(str(?cCake) AS ?label)}";	           
			query_text+="FILTER(regex(str(?spongeName),"+"\""+ selectedSpongeFlavour+"\""+")"+
			          "&& regex(str(?frostingName),"+"\""+selectedFrostingType+"\""+")"+
					")} "+"GROUP BY ?name ?label ?cakeCategory ?price  ?shopName ?firstAddress ?city ?phoneNumber";
		}
		else if(selectedDietaryRequirement.contentEquals("Dietary requirement") && !selectedCakeType.contentEquals("Cake type") && selectedFrostingType.contentEquals("Frosting type")&& !selectedSpongeFlavour.contentEquals("Sponge flavour")) {
			query_text=  prefix + 
					"SELECT ?name ?label  (strafter(?label,\"#\") as ?cakeCategory) ?price  ?shopName ?firstAddress ?city ?phoneNumber (GROUP_CONCAT(DISTINCT ?cakeTypeName;SEPARATOR=\",\") AS ?cakeTypename) (GROUP_CONCAT(DISTINCT ?frostingName;SEPARATOR=\",\") AS ?frostingname)(GROUP_CONCAT(DISTINCT ?spongeName;SEPARATOR=\",\") AS ?spongeFlavourname)(GROUP_CONCAT( DISTINCT ?dietaryName;SEPARATOR=\",\") AS ?dietaryname)\r\n" + 
					"WHERE{{?Cake a ?cCake.?cCake rdfs:subClassOf ck:Cake.?Cake ck:cakeName ?name.?Cake ck:cakePrice ?price.?Cake ck:cakePicture ?pictureLink.?CakeBranch ck:sells ?Cake.?CakeBranch ck:branchphoneNumber ?phoneNumber.?CakeBranch ck:isLocatedOnAddress ?Address.?Address ck:addressLine1 ?firstAddress.?City ck:hasAddress ?Address.?City ck:cityName ?city.?CakeShop a ?cCakeShop.?cCakeShop rdfs:subClassOf ck:CakeShop.?CakeShop ck:hasCakeBranch ?CakeBranch.?CakeShop ck:cakeShopName ?shopName.OPTIONAL{?Cake ck:hasCakeType ?cakeType.?cakeType ck:cakeTypeName ?cakeTypeName}.OPTIONAL{?Cake ck:hasFrosting ?frosting.?frosting ck:frostingName ?frostingName}.OPTIONAL{?Cake ck:hasSpongeFlavour ?spongeFlavour.?spongeFlavour ck:spongeFlavourName ?spongeName}. OPTIONAL{?Cake ck:hasDietaryRequirement ?dietaryRequirement. ?dietaryRequirement ck:dietaryName ?dietaryName}.BIND(str(?cCake) AS ?label)}";	           
			query_text+="FILTER(regex(str(?spongeName),"+"\""+ selectedSpongeFlavour+"\""+")"+
			          "&& regex(str(?cakeTypeName),"+"\""+selectedCakeType+"\""+")"+
					")} "+"GROUP BY ?name ?label ?cakeCategory ?price  ?shopName ?firstAddress ?city ?phoneNumber";
		}
		else if(!selectedDietaryRequirement.contentEquals("Dietary requirement") && selectedCakeType.contentEquals("Cake type") && selectedFrostingType.contentEquals("Frosting type")&& !selectedSpongeFlavour.contentEquals("Sponge flavour")) {
			query_text=  prefix + 
					"SELECT ?name ?label  (strafter(?label,\"#\") as ?cakeCategory) ?price  ?shopName ?firstAddress ?city ?phoneNumber (GROUP_CONCAT(DISTINCT ?cakeTypeName;SEPARATOR=\",\") AS ?cakeTypename) (GROUP_CONCAT(DISTINCT ?frostingName;SEPARATOR=\",\") AS ?frostingname)(GROUP_CONCAT(DISTINCT ?spongeName;SEPARATOR=\",\") AS ?spongeFlavourname)(GROUP_CONCAT( DISTINCT ?dietaryName;SEPARATOR=\",\") AS ?dietaryname)\r\n" + 
					"WHERE{{?Cake a ?cCake.?cCake rdfs:subClassOf ck:Cake.?Cake ck:cakeName ?name.?Cake ck:cakePrice ?price.?Cake ck:cakePicture ?pictureLink.?CakeBranch ck:sells ?Cake.?CakeBranch ck:branchphoneNumber ?phoneNumber.?CakeBranch ck:isLocatedOnAddress ?Address.?Address ck:addressLine1 ?firstAddress.?City ck:hasAddress ?Address.?City ck:cityName ?city.?CakeShop a ?cCakeShop.?cCakeShop rdfs:subClassOf ck:CakeShop.?CakeShop ck:hasCakeBranch ?CakeBranch.?CakeShop ck:cakeShopName ?shopName.OPTIONAL{?Cake ck:hasCakeType ?cakeType.?cakeType ck:cakeTypeName ?cakeTypeName}.OPTIONAL{?Cake ck:hasFrosting ?frosting.?frosting ck:frostingName ?frostingName}.OPTIONAL{?Cake ck:hasSpongeFlavour ?spongeFlavour.?spongeFlavour ck:spongeFlavourName ?spongeName}. OPTIONAL{?Cake ck:hasDietaryRequirement ?dietaryRequirement. ?dietaryRequirement ck:dietaryName ?dietaryName}.BIND(str(?cCake) AS ?label)}";	           
			query_text+="FILTER(regex(str(?spongeName),"+"\""+ selectedSpongeFlavour+"\""+")"+
			          "&& regex(str(?dietaryName),"+"\""+selectedDietaryRequirement+"\""+")"+
					")} "+"GROUP BY ?name ?label ?cakeCategory ?price  ?shopName ?firstAddress ?city ?phoneNumber";
		}
		else if(!selectedDietaryRequirement.contentEquals("Dietary requirement") && !selectedCakeType.contentEquals("Cake type") && !selectedFrostingType.contentEquals("Frosting type")&& selectedSpongeFlavour.contentEquals("Sponge flavour")) {
			query_text=  prefix + 
					"SELECT ?name ?label  (strafter(?label,\"#\") as ?cakeCategory) ?price  ?shopName ?firstAddress ?city ?phoneNumber (GROUP_CONCAT(DISTINCT ?cakeTypeName;SEPARATOR=\",\") AS ?cakeTypename) (GROUP_CONCAT(DISTINCT ?frostingName;SEPARATOR=\",\") AS ?frostingname)(GROUP_CONCAT(DISTINCT ?spongeName;SEPARATOR=\",\") AS ?spongeFlavourname)(GROUP_CONCAT( DISTINCT ?dietaryName;SEPARATOR=\",\") AS ?dietaryname)\r\n" + 
					"WHERE{{?Cake a ?cCake.?cCake rdfs:subClassOf ck:Cake.?Cake ck:cakeName ?name.?Cake ck:cakePrice ?price.?Cake ck:cakePicture ?pictureLink.?CakeBranch ck:sells ?Cake.?CakeBranch ck:branchphoneNumber ?phoneNumber.?CakeBranch ck:isLocatedOnAddress ?Address.?Address ck:addressLine1 ?firstAddress.?City ck:hasAddress ?Address.?City ck:cityName ?city.?CakeShop a ?cCakeShop.?cCakeShop rdfs:subClassOf ck:CakeShop.?CakeShop ck:hasCakeBranch ?CakeBranch.?CakeShop ck:cakeShopName ?shopName.OPTIONAL{?Cake ck:hasCakeType ?cakeType.?cakeType ck:cakeTypeName ?cakeTypeName}.OPTIONAL{?Cake ck:hasFrosting ?frosting.?frosting ck:frostingName ?frostingName}.OPTIONAL{?Cake ck:hasSpongeFlavour ?spongeFlavour.?spongeFlavour ck:spongeFlavourName ?spongeName}. OPTIONAL{?Cake ck:hasDietaryRequirement ?dietaryRequirement. ?dietaryRequirement ck:dietaryName ?dietaryName}.BIND(str(?cCake) AS ?label)}";	           
			query_text+="FILTER(regex(str(?dietaryName),"+"\""+ selectedDietaryRequirement+"\""+")"+
			          "&& regex(str(?frostingName),"+"\""+selectedFrostingType+"\""+")"+
			          "&& regex(str(?cakeTypeName),"+"\""+selectedCakeType+"\""+")"+
					")} "+"GROUP BY ?name ?label ?cakeCategory ?price  ?shopName ?firstAddress ?city ?phoneNumber";
		}
		else if(selectedDietaryRequirement.contentEquals("Dietary requirement") && !selectedCakeType.contentEquals("Cake type") && !selectedFrostingType.contentEquals("Frosting type")&& !selectedSpongeFlavour.contentEquals("Sponge flavour")) {
			query_text=  prefix + 
					"SELECT ?name ?label  (strafter(?label,\"#\") as ?cakeCategory) ?price  ?shopName ?firstAddress ?city ?phoneNumber (GROUP_CONCAT(DISTINCT ?cakeTypeName;SEPARATOR=\",\") AS ?cakeTypename) (GROUP_CONCAT(DISTINCT ?frostingName;SEPARATOR=\",\") AS ?frostingname)(GROUP_CONCAT(DISTINCT ?spongeName;SEPARATOR=\",\") AS ?spongeFlavourname)(GROUP_CONCAT( DISTINCT ?dietaryName;SEPARATOR=\",\") AS ?dietaryname)\r\n" + 
					"WHERE{{?Cake a ?cCake.?cCake rdfs:subClassOf ck:Cake.?Cake ck:cakeName ?name.?Cake ck:cakePrice ?price.?Cake ck:cakePicture ?pictureLink.?CakeBranch ck:sells ?Cake.?CakeBranch ck:branchphoneNumber ?phoneNumber.?CakeBranch ck:isLocatedOnAddress ?Address.?Address ck:addressLine1 ?firstAddress.?City ck:hasAddress ?Address.?City ck:cityName ?city.?CakeShop a ?cCakeShop.?cCakeShop rdfs:subClassOf ck:CakeShop.?CakeShop ck:hasCakeBranch ?CakeBranch.?CakeShop ck:cakeShopName ?shopName.OPTIONAL{?Cake ck:hasCakeType ?cakeType.?cakeType ck:cakeTypeName ?cakeTypeName}.OPTIONAL{?Cake ck:hasFrosting ?frosting.?frosting ck:frostingName ?frostingName}.OPTIONAL{?Cake ck:hasSpongeFlavour ?spongeFlavour.?spongeFlavour ck:spongeFlavourName ?spongeName}. OPTIONAL{?Cake ck:hasDietaryRequirement ?dietaryRequirement. ?dietaryRequirement ck:dietaryName ?dietaryName}.BIND(str(?cCake) AS ?label)}";	           
			query_text+="FILTER(regex(str(?spongeName),"+"\""+ selectedSpongeFlavour+"\""+")"+
			          "&& regex(str(?frostingName),"+"\""+selectedFrostingType+"\""+")"+
			          "&& regex(str(?cakeTypeName),"+"\""+selectedCakeType+"\""+")"+
					")} "+"GROUP BY ?name ?label ?cakeCategory ?price  ?shopName ?firstAddress ?city ?phoneNumber";
		}
		else if(!selectedDietaryRequirement.contentEquals("Dietary requirement") && selectedCakeType.contentEquals("Cake type") && !selectedFrostingType.contentEquals("Frosting type")&& !selectedSpongeFlavour.contentEquals("Sponge flavour")) {
			query_text=  prefix + 
					"SELECT ?name ?label  (strafter(?label,\"#\") as ?cakeCategory) ?price  ?shopName ?firstAddress ?city ?phoneNumber (GROUP_CONCAT(DISTINCT ?cakeTypeName;SEPARATOR=\",\") AS ?cakeTypename) (GROUP_CONCAT(DISTINCT ?frostingName;SEPARATOR=\",\") AS ?frostingname)(GROUP_CONCAT(DISTINCT ?spongeName;SEPARATOR=\",\") AS ?spongeFlavourname)(GROUP_CONCAT( DISTINCT ?dietaryName;SEPARATOR=\",\") AS ?dietaryname)\r\n" + 
					"WHERE{{?Cake a ?cCake.?cCake rdfs:subClassOf ck:Cake.?Cake ck:cakeName ?name.?Cake ck:cakePrice ?price.?Cake ck:cakePicture ?pictureLink.?CakeBranch ck:sells ?Cake.?CakeBranch ck:branchphoneNumber ?phoneNumber.?CakeBranch ck:isLocatedOnAddress ?Address.?Address ck:addressLine1 ?firstAddress.?City ck:hasAddress ?Address.?City ck:cityName ?city.?CakeShop a ?cCakeShop.?cCakeShop rdfs:subClassOf ck:CakeShop.?CakeShop ck:hasCakeBranch ?CakeBranch.?CakeShop ck:cakeShopName ?shopName.OPTIONAL{?Cake ck:hasCakeType ?cakeType.?cakeType ck:cakeTypeName ?cakeTypeName}.OPTIONAL{?Cake ck:hasFrosting ?frosting.?frosting ck:frostingName ?frostingName}.OPTIONAL{?Cake ck:hasSpongeFlavour ?spongeFlavour.?spongeFlavour ck:spongeFlavourName ?spongeName}. OPTIONAL{?Cake ck:hasDietaryRequirement ?dietaryRequirement. ?dietaryRequirement ck:dietaryName ?dietaryName}.BIND(str(?cCake) AS ?label)}";	           
			query_text+="FILTER(regex(str(?spongeName),"+"\""+ selectedSpongeFlavour+"\""+")"+
			          "&& regex(str(?frostingName),"+"\""+selectedFrostingType+"\""+")"+
			          "&& regex(str(?dietaryName),"+"\""+selectedDietaryRequirement+"\""+")"+
					")} "+"GROUP BY ?name ?label ?cakeCategory ?price  ?shopName ?firstAddress ?city ?phoneNumber";
		}
		else if(!selectedDietaryRequirement.contentEquals("Dietary requirement") && !selectedCakeType.contentEquals("Cake type") && selectedFrostingType.contentEquals("Frosting type")&& !selectedSpongeFlavour.contentEquals("Sponge flavour")) {
			query_text=  prefix + 
					"SELECT ?name ?label  (strafter(?label,\"#\") as ?cakeCategory) ?price  ?shopName ?firstAddress ?city ?phoneNumber (GROUP_CONCAT(DISTINCT ?cakeTypeName;SEPARATOR=\",\") AS ?cakeTypename) (GROUP_CONCAT(DISTINCT ?frostingName;SEPARATOR=\",\") AS ?frostingname)(GROUP_CONCAT(DISTINCT ?spongeName;SEPARATOR=\",\") AS ?spongeFlavourname)(GROUP_CONCAT( DISTINCT ?dietaryName;SEPARATOR=\",\") AS ?dietaryname)\r\n" + 
					"WHERE{{?Cake a ?cCake.?cCake rdfs:subClassOf ck:Cake.?Cake ck:cakeName ?name.?Cake ck:cakePrice ?price.?Cake ck:cakePicture ?pictureLink.?CakeBranch ck:sells ?Cake.?CakeBranch ck:branchphoneNumber ?phoneNumber.?CakeBranch ck:isLocatedOnAddress ?Address.?Address ck:addressLine1 ?firstAddress.?City ck:hasAddress ?Address.?City ck:cityName ?city.?CakeShop a ?cCakeShop.?cCakeShop rdfs:subClassOf ck:CakeShop.?CakeShop ck:hasCakeBranch ?CakeBranch.?CakeShop ck:cakeShopName ?shopName.OPTIONAL{?Cake ck:hasCakeType ?cakeType.?cakeType ck:cakeTypeName ?cakeTypeName}.OPTIONAL{?Cake ck:hasFrosting ?frosting.?frosting ck:frostingName ?frostingName}.OPTIONAL{?Cake ck:hasSpongeFlavour ?spongeFlavour.?spongeFlavour ck:spongeFlavourName ?spongeName}. OPTIONAL{?Cake ck:hasDietaryRequirement ?dietaryRequirement. ?dietaryRequirement ck:dietaryName ?dietaryName}.BIND(str(?cCake) AS ?label)}";	           
			query_text+="FILTER(regex(str(?spongeName),"+"\""+ selectedSpongeFlavour+"\""+")"+
			          "&& regex(str(?cakeTypeName),"+"\""+selectedCakeType+"\""+")"+
			          "&& regex(str(?dietaryName),"+"\""+selectedDietaryRequirement+"\""+")"+
					")} "+"GROUP BY ?name ?label ?cakeCategory ?price  ?shopName ?firstAddress ?city ?phoneNumber";
		}
		else {
			query_text=  prefix +
					"SELECT ?name ?label  (strafter(?label,\"#\") as ?cakeCategory) ?price  ?shopName ?firstAddress ?city ?phoneNumber (GROUP_CONCAT(DISTINCT ?cakeTypeName;SEPARATOR=\",\") AS ?cakeTypename) (GROUP_CONCAT(DISTINCT ?frostingName;SEPARATOR=\",\") AS ?frostingname)(GROUP_CONCAT(DISTINCT ?spongeName;SEPARATOR=\",\") AS ?spongeFlavourname)(GROUP_CONCAT( DISTINCT ?dietaryName;SEPARATOR=\",\") AS ?dietaryname)\r\n" + 
					"WHERE{{?Cake a ?cCake.?cCake rdfs:subClassOf ck:Cake.?Cake ck:cakeName ?name.?Cake ck:cakePrice ?price.?Cake ck:cakePicture ?pictureLink.?CakeBranch ck:sells ?Cake.?CakeBranch ck:branchphoneNumber ?phoneNumber.?CakeBranch ck:isLocatedOnAddress ?Address.?Address ck:addressLine1 ?firstAddress.?City ck:hasAddress ?Address.?City ck:cityName ?city.?CakeShop a ?cCakeShop.?cCakeShop rdfs:subClassOf ck:CakeShop.?CakeShop ck:hasCakeBranch ?CakeBranch.?CakeShop ck:cakeShopName ?shopName.OPTIONAL{?Cake ck:hasCakeType ?cakeType.?cakeType ck:cakeTypeName ?cakeTypeName}.OPTIONAL{?Cake ck:hasFrosting ?frosting.?frosting ck:frostingName ?frostingName}.OPTIONAL{?Cake ck:hasSpongeFlavour ?spongeFlavour.?spongeFlavour ck:spongeFlavourName ?spongeName}. OPTIONAL{?Cake ck:hasDietaryRequirement ?dietaryRequirement. ?dietaryRequirement ck:dietaryName ?dietaryName}.BIND(str(?cCake) AS ?label)}";	           
			query_text+="FILTER(regex(str(?spongeName),"+"\""+ selectedSpongeFlavour+"\""+")"+
			          "&& regex(str(?cakeTypeName),"+"\""+selectedCakeType+"\""+")"+
			          "&& regex(str(?frostingName),"+"\""+selectedFrostingType+"\""+")"+
			          "&& regex(str(?dietaryName),"+"\""+selectedDietaryRequirement+"\""+")"+
					")} "+"GROUP BY ?name ?label ?cakeCategory ?price  ?shopName ?firstAddress ?city ?phoneNumber";
			
		}
	
		 System.out.println(query_text);
	
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
	                
	                System.out.println(qs.get("name"));
	                i++;
	            }
	            
	         /*************************Create Table and tableModel******************************/
	           TableModel tableModel = new DefaultTableModel(values.toArray(new Object[][] {}), columns.toArray());
	           final JTable table = new JTable(tableModel);
	            table.setForeground(Color.DARK_GRAY);
	            table.setBackground(Color.WHITE);
	            table.setRowHeight(35);		
	            sp.setViewportView(table);		           
	            sp.setBounds(50, 180, 800, 317);
	            contentPane.add(sp);
	            contentPane.repaint();
	            
	            table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
	                public void valueChanged(ListSelectionEvent event) {
	                 
	                	 System.out.println(table.getValueAt(table.getSelectedRow(), 0).toString());
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
	
	public App() {
		
	
		setFont(new Font("Arial", Font.PLAIN, 14));
		setIconImage(Toolkit.getDefaultToolkit().getImage("./src/main/resources/images/semantic.png"));
		setTitle("Semantic Search");
		setResizable(false);
		//setPreferredSize(new Dimension(1100, 800));
		//setMaximumSize(new Dimension(1100, 800));
		//setBounds(100, 100, 1100, 700);
		setSize(900,600);
		setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setPreferredSize(new Dimension(900, 600));
		contentPane.setMaximumSize(new Dimension(900, 600));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));



	
		
		//Populate combo boxes with ontology data that will be used as filters
		//-------------------------------------
	
		  LoadCakeType();
		  LoadFrosting();
		  LoadSpongeFlavour();
		  LoadDietaryRequirement();
		//----------------------------------	 
		  
		  
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
		  
		  
		///----------------------------------------------  
		  
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
		  		  
		///----------------------------------------------  
		  
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
		  		  
		///----------------------------------------------  	  
		  
			///------Create an ActionListener for Sponge Flavour JComboBox component-----------------------------------
			
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
		  		  
		///----------------------------------------------  	 
		  
		//Search input 
		txtByName = new JTextField();
		txtByName.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtByName.setToolTipText("Enter Name");
		txtByName.setBounds(50, 50, 355, 51);
		txtByName.setColumns(10);
		contentPane.add(txtByName);
		
		
		setContentPane(contentPane);
	
	     //Search Button
		JButton btnSearchPeople = new JButton("Search");
		btnSearchPeople.setBackground(SystemColor.controlHighlight);
		btnSearchPeople.setFocusable(false);
		btnSearchPeople.setFocusTraversalKeysEnabled(false);
		btnSearchPeople.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnSearchPeople.setBounds(410, 50, 140, 51);
		btnSearchPeople.setFocusPainted(false);
		
		// populates table
				LoadData();
				
		//Search Button listener		
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
				
				System.out.println(query_text);
				
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
		                
		                System.out.println(qs.get("name"));
		                i++;
		            }
		            
		         /*************************Create Table and tableModel******************************/
		           TableModel tableModel = new DefaultTableModel(values.toArray(new Object[][] {}), columns.toArray());
		            final JTable table = new JTable(tableModel);
		            table.setForeground(Color.DARK_GRAY);
		            table.setBackground(Color.WHITE);
		            table.setRowHeight(35);		
		            sp.setViewportView(table);		           
		            sp.setBounds(50, 180, 800, 317);
		            contentPane.add(sp);
		            contentPane.repaint();
		            
		            table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
		                public void valueChanged(ListSelectionEvent event) {
		                 
		                	 System.out.println(table.getValueAt(table.getSelectedRow(), 0).toString());
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
		
		    
		contentPane.setLayout(null);
		
		btnSearchPeople.setPreferredSize(new Dimension(350, 45));
		contentPane.add(btnSearchPeople);
		
		
		
	}
}
