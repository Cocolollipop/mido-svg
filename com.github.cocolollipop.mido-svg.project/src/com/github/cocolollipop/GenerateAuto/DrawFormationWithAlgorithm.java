package com.github.cocolollipop.GenerateAuto;

import java.awt.Color;
import java.awt.Dimension;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import com.github.cocolollipop.univ.Formation;
import com.github.cocolollipop.univ.Licence;
import com.github.cocolollipop.univ.Master;

public class DrawFormationWithAlgorithm {
	int dimXCanvas = 1920;
	int dimYCanvas = 1080;
	int shiftX;
	int shiftY;
	
	
	
	
	/**
	 * Paints the drawing
	 * 
	 * @param listOfAllLicences
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public void paint(ArrayList<Licence> listOfAllLicences) throws IOException, ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();

		// Get a DOMImplementation.
		DOMImplementation domImpl = db.getDOMImplementation();
		// Create an instance of org.w3c.dom.Document.
		String svgNS = "http://www.w3.org/2000/svg";
		Document document = domImpl.createDocument(svgNS, "svg", null);
		// Create an instance of the SVG Generator.
		SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(document);
		ctx.setEmbeddedFontsOn(true);
		SVGGraphics2D g = new SVGGraphics2D(ctx, false);
		// set the size of SVG
		g.setSVGCanvasSize(new Dimension(this.dimXCanvas, this.dimYCanvas));
		g.setPaint(Color.black); // paint in black
		/**
		 * We starts painting
		 */
		g.drawString("MIDO", 300, 20);
		g.drawString("Masters", 300, 50);
		/**
		 * We creates the licences
		 */
		for (int i = 0; i < listOfAllLicences.size(); i++) {
			g.drawString(listOfAllLicences.get(i).getFullName(), listOfAllLicences.get(i).getPosX(),
					listOfAllLicences.get(i).getPosY());
		}

		/**
		 * Exports the SVG
		 */
		try (Writer out = new OutputStreamWriter(new FileOutputStream("DessinLicenceTest.svg"), "UTF-8")) {
			g.stream(out, true);
		}
	}
	
	/*
	 * defineMasters definit la liste des formations et retourne un objet mesFormations
	 */
	public LinkedList definemesFormations(){
		LinkedList<Formation> mesFormations = new LinkedList<Formation>();
		Licence L3MIAGE = new Licence("L3 MIAGE", 3, 250, 70);
		L3MIAGE.setFullNameWithLink(
				"http://formations.dauphine.fr/offre/fr-FR/fiche/A3INF/programme//#FRUAI0750736TPRPRA3INF");
		Licence L3MIAGEApp = new Licence("L3 MIAGE App", 3, 750, 70);
		L3MIAGEApp.setFullNameWithLink(
				"http://formations.dauphine.fr/offre/fr-FR/fiche/A3INF/programme//#FRUAI0750736TPRPRA3INFAPP");
		Master M1MIAGE = new Master("M1 MIAGE", 4, 250, 150);
		M1MIAGE.setFullNameWithLink(
				"http://formations.dauphine.fr/offre/fr-FR/fiche/A5STI/programme//#FRUAI0750736TPRPRA4MIAI");
		Master M1MIAGEApp = new Master("M1 MIAGE App", 4, 750, 150);
		Master M2MIAGEIF = new Master("M2 MIAGE IF", 5, 100, 300);
		Master M2MIAGEID = new Master("M2 MIAGE ID", 5, 250, 300);
		Master M2MIAGESTIN = new Master("M2 MIAGE STIN", 5, 400, 300);
		Master M2MIAGEIFApp = new Master("M2 MIAGE IF App", 5, 600, 300);
		Master M2MIAGEIDApp = new Master("M2 MIAGE ID App", 5, 750, 300);
		Master M2MIAGESTINApp = new Master("M2 MIAGE STIN App", 5, 900, 300);
		
		mesFormations.add(L3MIAGE);
		mesFormations.add(L3MIAGEApp);
		mesFormations.add(M1MIAGE);
		mesFormations.add(M1MIAGEApp);
		mesFormations.add(M2MIAGEIF);
		mesFormations.add(M2MIAGEID);
		mesFormations.add(M2MIAGESTIN);
		mesFormations.add(M2MIAGEIFApp);
		mesFormations.add(M2MIAGEIDApp);
		mesFormations.add(M2MIAGESTINApp);
		
		L3MIAGE.addAvailableFormation(M1MIAGE);
		L3MIAGEApp.addAvailableFormation(M1MIAGEApp);
		M1MIAGE.addAvailableFormation(M2MIAGESTIN);
		M1MIAGE.addAvailableFormation(M2MIAGEIF);
		M1MIAGE.addAvailableFormation(M2MIAGEID);
		M1MIAGEApp.addAvailableFormation(M2MIAGESTINApp);
		M1MIAGEApp.addAvailableFormation(M2MIAGEIDApp);
		M1MIAGEApp.addAvailableFormation(M2MIAGEIFApp);
		
		return mesFormations;
	}

	public void definirPositions(LinkedList<Formation> lesFormations, int canvasX, int canvasY){
		
		/* On definit d'abord le decalage initial
		Pour cela, on va analyser le contenu de lesFormations
		On va compter le nombre de L1, L2,...
		
		*/
		int decalage=0;
		int nbL1=0;
		int nbL2=0;
		int nbL3=0;
		int nbM1=0;
		int nbM2=0;
		
		for(Formation uneFormation : lesFormations){
			switch(uneFormation.getFullName()){
				case "L1" :
				nbL1++;
				break;
				case "L2" :
				nbL2++;
				break;
				case "L3" :
				nbL3++;
				break;
				case "M1" :
				nbM1++;
				break;
				case "M2" :
				nbM2++;
				break;
				default: System.out.println("Attention la formation nsuivante n'a pas �t� reconnue : "+uneFormation.getFullName());
			}
		}
		
		/*
		 *  Maintenant, on calcule le 1er decalage et on va affecter la position des L1
		 */
		decalage = canvasX/(nbL1+1);
		int k=1; //correspond � un iterator, qui vaut � la fin exactement le nombre de L1
		for(Formation uneFormation : lesFormations){
			if (uneFormation.getFullName() == "L1"){
				uneFormation.setPosX(decalage*k);
			}
		}
		
		
	}
}
