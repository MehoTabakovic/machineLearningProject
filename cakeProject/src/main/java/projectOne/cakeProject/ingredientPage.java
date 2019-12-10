package projectOne.cakeProject;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

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



public class ingredientPage extends JFrame {
	
	
   public static String cakeName="";

   public static final String SOURCE = "./src/main/resources/data/";
   
   public static final String Cakes_n = "http://www.semanticweb.org/mehotabakovic/ontologies/2019/9/untitled-ontology-5#";
  
   public JLabel baseIngredientData;
   
   public JLabel frostingIngredientData;
   
   public JLabel toppingIngredientData;
   
   public void LoadBaseIngredients(String nameOfCake) {
	   
	    OntModel m = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
		
	
		FileManager.get().readModel( m, SOURCE + "cakeOntology.owl" );
			
		String prefix = "prefix ck: <" + Cakes_n + ">\n" +
	                	"prefix rdfs: <" + RDFS.getURI() + ">\n" +
	                	"prefix owl: <" + OWL.getURI() + ">\n";

			String query_text=  prefix +
						"SELECT  ?Ingredient \r\n" + 
						"WHERE {?Cake a ?cCake.?cCake rdfs:subClassOf ck:Cake.?Cake ck:cakeName ?cakeName. ?Cake ck:hasBaseIngredient ?ingredient.?ingredient ck:ingredientName ?Ingredient.BIND(\"Base Ingredient\" AS ?ingredientType).";	           
			            query_text +=" FILTER(?cakeName=\""+ nameOfCake+"\""+")}";  	
			            query_text +="ORDER BY (?Ingredient)" ;  	
			            
			            System.out.println(query_text);
			            
			Query query = QueryFactory.create( query_text );
	        QueryExecution qexec = QueryExecutionFactory.create( query, m );
	        
	        List<String[]> values = new ArrayList<String[]>();
	        StringBuilder sb = new StringBuilder();
	        try {
	            ResultSet results = qexec.execSelect();
	            int i = 0;
	            while ( results.hasNext() ) {
	                QuerySolution qs = results.next();

	               
	                values.add(new String[] {qs.get("Ingredient").toString()});	
	                sb.append("- "+qs.get("Ingredient").toString()+"\n");
	                i++;
	            }
	            baseIngredientData.setText("<html>" +sb.toString().replaceAll("\n", "<br/>") + "</html>");
	        }
	        finally {
	            qexec.close();
	        }
   }
   
   public void LoadFrostingIngredients(String nameOfCake) {
	   
	    OntModel m = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
		
	
		FileManager.get().readModel( m, SOURCE + "cakeOntology.owl" );
			
		String prefix = "prefix ck: <" + Cakes_n + ">\n" +
	                	"prefix rdfs: <" + RDFS.getURI() + ">\n" +
	                	"prefix owl: <" + OWL.getURI() + ">\n";

			String query_text=  prefix +
						"SELECT  ?Ingredient \r\n" + 
						"WHERE {?Cake a ?cCake.?cCake rdfs:subClassOf ck:Cake.?Cake ck:cakeName ?cakeName. ?Cake ck:hasFrostingIngredient ?ingredient.?ingredient ck:ingredientName ?Ingredient.BIND(\"Base Ingredient\" AS ?ingredientType).";	           
			            query_text +=" FILTER(?cakeName=\""+ nameOfCake+"\""+")}";  	
			            query_text +="ORDER BY (?Ingredient)" ;  	
			            
			Query query = QueryFactory.create( query_text );
	        QueryExecution qexec = QueryExecutionFactory.create( query, m );
	        
	        List<String[]> values = new ArrayList<String[]>();
	        StringBuilder sb = new StringBuilder();
	        try {
	            ResultSet results = qexec.execSelect();
	            int i = 0;
	            while ( results.hasNext() ) {
	                QuerySolution qs = results.next();

	               
	                values.add(new String[] {qs.get("Ingredient").toString()});	
	                sb.append("- "+qs.get("Ingredient").toString()+"\n");
	                i++;
	            }
	            frostingIngredientData.setText("<html>" + sb.toString().replaceAll("\n", "<br/>") + "</html>");
	        }
	        finally {
	            qexec.close();
	        }
  }

   public void LoadToppingIngredients(String nameOfCake) {
	   
	    OntModel m = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
		
	
		FileManager.get().readModel( m, SOURCE + "cakeOntology.owl" );
			
		String prefix = "prefix ck: <" + Cakes_n + ">\n" +
	                	"prefix rdfs: <" + RDFS.getURI() + ">\n" +
	                	"prefix owl: <" + OWL.getURI() + ">\n";

			String query_text=  prefix +
						"SELECT  ?Ingredient \r\n" + 
						"WHERE {?Cake a ?cCake.?cCake rdfs:subClassOf ck:Cake.?Cake ck:cakeName ?cakeName. ?Cake ck:hasToppingIngredient ?ingredient.?ingredient ck:ingredientName ?Ingredient.BIND(\"Base Ingredient\" AS ?ingredientType).";	           
			            query_text +=" FILTER(?cakeName=\""+ nameOfCake+"\""+")}";  	
			            query_text +="ORDER BY (?Ingredient)" ;  	
			            
			Query query = QueryFactory.create( query_text );
	        QueryExecution qexec = QueryExecutionFactory.create( query, m );
	        
	        List<String[]> values = new ArrayList<String[]>();
	        StringBuilder sb = new StringBuilder();
	        try {
	            ResultSet results = qexec.execSelect();
	            int i = 0;
	            while ( results.hasNext() ) {
	                QuerySolution qs = results.next();

	               
	                values.add(new String[] {qs.get("Ingredient").toString()});	
	                sb.append("- "+qs.get("Ingredient").toString()+"\n");
	                i++;
	            }
	            toppingIngredientData.setText("<html>" + sb.toString().replaceAll("\n", "<br/>") + "</html>");
	        }
	        finally {
	            qexec.close();
	        }
 }
	
   ingredientPage(String cakename){
		
		cakeName=cakename;
	Font f=new Font("Serif",Font.BOLD,30);
	Font ff=new Font("Serif",Font.BOLD,15);
		
		//header
		JPanel heading;
		heading=new JPanel();

		heading.setBackground(new Color(255,255,255,30));
		heading.setBounds(0,0,900,60);
		
		JLabel name=new JLabel("Ingredients for "+cakeName);
		name.setForeground(Color.WHITE);
		name.setBounds(200,50,400,50);
		name.setFont(f);
		heading.add(name);
				
		
		
		//panel
		
		JPanel mainPanel=new JPanel();
		mainPanel.setLayout(null);
		setSize(520,450);
		mainPanel.setBackground(new Color(255,255,255,30));
		mainPanel.setBounds(190,90,520,450);
		
		//panel-label base Ingredients
				JLabel baseIngredient=new JLabel("Base ingredients");
				baseIngredient.setBounds(30,10,200,50);
				baseIngredient.setForeground(Color.WHITE);
				baseIngredient.setBackground(new Color(210,180,140));
				baseIngredient.setFont(ff);
				mainPanel.add(baseIngredient);
		
			    baseIngredientData=new JLabel();
				baseIngredientData.setBounds(30,70,200,200);
				baseIngredientData.setForeground(Color.WHITE);
				baseIngredientData.setBackground(new Color(210,180,140));
				baseIngredientData.setFont(ff);
				baseIngredientData.setVerticalAlignment(JLabel.TOP);
				mainPanel.add(baseIngredientData);
				
				 LoadBaseIngredients(cakeName);
				 
				//panel-label Frosting Ingredients
					JLabel frostingIngredient=new JLabel("Frosting ingredients");
					frostingIngredient.setBounds(180,10,200,50);
					frostingIngredient.setForeground(Color.WHITE);
					frostingIngredient.setBackground(new Color(210,180,140));
					frostingIngredient.setFont(ff);
					mainPanel.add(frostingIngredient);
			
					frostingIngredientData=new JLabel();
					frostingIngredientData.setBounds(180,70,200,200);
					frostingIngredientData.setForeground(Color.WHITE);
					frostingIngredientData.setBackground(new Color(210,180,140));
					frostingIngredientData.setFont(ff);
					frostingIngredientData.setVerticalAlignment(JLabel.TOP);
					mainPanel.add(frostingIngredientData);
					
					LoadFrostingIngredients(cakeName);
					
					//panel-label Topping Ingredients
					JLabel toppingIngredient=new JLabel("Topping ingredients");
					toppingIngredient.setBounds(340,10,200,50);
					toppingIngredient.setForeground(Color.WHITE);
					toppingIngredient.setBackground(new Color(210,180,140));
					toppingIngredient.setFont(ff);
					mainPanel.add(toppingIngredient);
			
					toppingIngredientData=new JLabel();
					toppingIngredientData.setBounds(340,70,200,200);
					toppingIngredientData.setForeground(Color.WHITE);
					toppingIngredientData.setBackground(new Color(210,180,140));
					toppingIngredientData.setFont(ff);
					toppingIngredientData.setVerticalAlignment(JLabel.TOP);
					mainPanel.add(toppingIngredientData);
					
					LoadToppingIngredients(cakeName);

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
		background.add(mainPanel);
		background.add(heading);
		add(background);
		
		setVisible(true);
	}
	
	public static ingredientPage ingredientPage=new ingredientPage(cakeName);
	
	public static void main(String args[]) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ingredientPage.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
