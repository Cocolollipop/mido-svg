package com.github.cocolollipop.mido_svg.svg_generator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import com.github.cocolollipop.mido_svg.model.DataBase;
import com.github.cocolollipop.mido_svg.paper.Format;
import com.github.cocolollipop.mido_svg.university.components.Formation;
import com.github.cocolollipop.mido_svg.university.components.Subject;

public class LicenceSVGGen {

	private DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	private DocumentBuilder db;
	private DOMImplementation domImpl;
	private String svgNS;
	private Document document;
	private SVGGeneratorContext ctx;
	private SVGGraphics2D g;
	private DataBase data;
	private Format format = new Format();
	private Enum drawOnly;

	private enum DrawOnly {
		LICENCE, MASTER, BOTH
	}

	public LicenceSVGGen() {
		this.data = new DataBase();
	}

	public DataBase getData() {
		// TODO Auto-generated method stub
		return this.data;
	}

	/**
	 * defineObjectsPosition determine the position of each Formation in the
	 * someFormations List
	 * 
	 * @param list
	 *            : LinkedList of all the formations available in the University
	 * @param canvasX
	 *            : abscissa size of the actual Canval
	 */
	public void defineObjectsPosition(List<Formation> list, int canvasX, int canvasY) {

		/*
		 * We define initial offset In order to it, we must count number of each
		 * Formation in someFormations Then calculate Y offset depending on
		 * which formation exists (Only L1 or all ?) Finally we calculate X
		 * offset
		 */
		int offsetX = 0;
		int offsetY = 0;
		int nbL1 = 0;
		int nbL2 = 0;
		int nbL3 = 0;
		int nbM1 = 0;
		int nbM2 = 0;
		int totalCptY = 0; // O<=totalCptY<=5 // tal number of potential
							// "stairs" in Y

		int cptY[] = new int[5];// trigger ; cpunt if there is an object in
								// Formation ; cptYL1 =0, else the postion of
								// first "seen" 0<=cptY[i]<=totalcptY
		for (int i = 0; i < 5; i++) {
			cptY[i] = 0;
		}

		// First we count number of each formation
		nbL1 = this.getData().countFormations(list, "L1");
		nbL2 = this.getData().countFormations(list, "L2");
		nbL3 = this.getData().countFormations(list, "L3");
		nbM1 = this.getData().countFormations(list, "M1");
		nbM2 = this.getData().countFormations(list, "M2");

		/*
		 * We calculate Y offset
		 */
		int tempCpt = 0;
		if (nbL1 != 0) {
			totalCptY = totalCptY + 1;
			cptY[0] = tempCpt + 1;
			tempCpt++;
		}
		if (nbL2 != 0) {
			totalCptY = totalCptY + 1;
			cptY[1] = tempCpt + 1;
			tempCpt++;
		}
		if (nbL3 != 0) {
			totalCptY = totalCptY + 1;
			cptY[2] = tempCpt + 1;
			tempCpt++;
		}
		if (nbM1 != 0) {
			totalCptY = totalCptY + 1;
			cptY[3] = tempCpt + 1;
			tempCpt++;
		}
		if (nbM2 != 0) {
			totalCptY = totalCptY + 1;
			cptY[4] = tempCpt + 1;
			tempCpt++;
		}

		/*
		 * Now we calculate X and Y offset
		 */
		offsetX = canvasX / (nbL1 + 1);
		offsetY = canvasY / (totalCptY + 1) * cptY[0];
		associatePositionX(list, "L1", offsetX, offsetY);

		offsetX = canvasX / (nbL2 + 1);
		offsetY = canvasY / (totalCptY + 1) * cptY[1];
		associatePositionX(list, "L2", offsetX, offsetY);

		offsetX = canvasX / (nbL3 + 1);
		offsetY = canvasY / (totalCptY + 1) * cptY[2];
		associatePositionX(list, "L3", offsetX, offsetY);

		offsetX = canvasX / (nbM1 + 1);
		offsetY = canvasY / (totalCptY + 1) * cptY[3];
		associatePositionX(list, "M1", offsetX, offsetY);

		offsetX = canvasX / (nbM2 + 1);
		offsetY = canvasY / (totalCptY + 1) * cptY[4];
		associatePositionX(list, "M2", offsetX, offsetY);

	}

	/**
	 * associatePositionX set the posX of each Formation which satisfy
	 * uneFormation.getFullName() == myYear
	 * 
	 * @param list
	 *            is a LinkedList of Formation
	 * @param myYear
	 *            is a year such as "L3" or "M1"
	 * @param decalage
	 */
	private void associatePositionX(List<Formation> list, String myYear, int decalageX, int decalageY) {
		int i = 1;
		for (Formation aFormation : list) {
			if (aFormation.getFullName().indexOf(myYear) != -1) {
				aFormation.setPosX(decalageX * i);
				aFormation.setPosY(decalageY);
				i++;
				System.out.println("associerOK : " + aFormation.getFullName());
			}
		}
	}

	private void getPlacement(List<Formation> someFormations) {
		for (Formation aFormation : someFormations) {
			System.out.println("Pour la formation " + aFormation.getFullName());
			System.out.println("PosX = " + aFormation.getPoint().x);
			System.out.println("PosY = " + aFormation.getPoint().y);
			System.out.println("_________________");
		}

	}

	/**
	 * fillListOfFormationToShow method fills the listOfFormationToShow with
	 * objects from FormationList that are of type typeOfFormation
	 * 
	 * @param typeOfFormation
	 * @return
	 * @throws ClassNotFoundException
	 */
	public List<Formation> fillListOfFormationToShow(String type) throws ClassNotFoundException {
		List<Formation> listOfFormationToShow = new LinkedList<Formation>();

		for (Formation formation : this.getData().getFormations()) {
			if (formation.getCategory().toString() == type)
				listOfFormationToShow.add(formation);
		}
		return listOfFormationToShow;
	}

	public void paint(boolean affFormationLicence, boolean affFormationMaster, boolean affResponsable,
			boolean affAdmission, boolean affSubject, boolean affTeacher, String form) throws Exception {
		String output = "./svg/outLicence.svg";

		db = dbf.newDocumentBuilder();
		// Get a DOMImplementation.
		domImpl = db.getDOMImplementation();
		// Create an instance of org.w3c.dom.Document.
		svgNS = "http://www.w3.org/2000/svg";
		document = domImpl.createDocument(svgNS, "svg", null);

		// Create an instance of the SVG Generator.
		ctx = SVGGeneratorContext.createDefault(document);
		ctx.setEmbeddedFontsOn(true);
		g = new SVGGraphics2D(ctx, false);
		// Create position variables

		this.defineObjectsPosition(this.data.getFormations(), 1920, 1080);

		this.drawAdmission(affAdmission);

		this.drawFormation(affFormationLicence, affFormationMaster);

		this.drawResponsable(affResponsable);
		this.drawSubjectTeacher(affSubject, affTeacher);

		format.changeFormat(form);

		g.setSVGCanvasSize(new Dimension(format.getCanevasX(), format.getCanevasY()));
		g.drawString(this.getData().getDepartment().getNomDepartement(), this.getData().getDepartment().getX(),
				this.getData().getDepartment().getY());

		// The tag that the user selected (he wants to see what are the
		// formation that teaches this course)
		// String userSelectedTag = "Probas";
		// String userSelectedTags[] = { "Rugby", "ADD", "Espagnol" };

		// Ask the test to render into the SVG Graphics2D implementation.

		// Tag checking
		/*
		 * int cptTags = 0; for (Formation f : this.data.getListOfFormations())
		 * { cptTags = 0; for (String str : userSelectedTags) {
		 * 
		 * if (Arrays.asList(f.getTagslist()).contains(str)) { cptTags++;
		 * 
		 * if (cptTags == userSelectedTags.length) { g.setPaint(Color.red);
		 * g.drawString("(X)", f.getPoint().x + 50, f.getPoint().y + 20); } } }
		 * }
		 */

		// g.setPaint(Color.black);

		// Finally, stream out SVG using UTF-8 encoding.
		boolean useCSS = true; // we want to use CSS style attributes
		try (Writer out = new OutputStreamWriter(new FileOutputStream(output), "UTF-8")) {
			g.stream(out, useCSS);
		}

		String content = this.svgLinkable(output);
		IOUtils.write(content, new FileOutputStream(output), "UTF-8");

	}

	/**
	 * This is to replace "&lt;" by "<" and "&gt;" by ">" because I did not
	 * found how to avoid converting < into &lt; and > into &gt;
	 **/
	public String svgLinkable(String output) throws FileNotFoundException, IOException {
		String content = IOUtils.toString(new FileInputStream(output), "UTF-8");
		content = content.replaceAll("&lt;", "<");
		content = content.replaceAll("&gt;", ">");
		return content = content.replaceAll("unicode=\"<\"", "unicode=\"\"");

	}

	/**
	 * Drawing of the objects the user has the choice between showing all
	 * "formations" or only "licence" or master for that he has to change
	 * the @param showOnly to (licenceOnly, masterOnly, both) then the
	 * rectangles arround the "formations" are drawn, lines also
	 * 
	 * @param lineCENTER
	 * @param lineYDOWN
	 * @param lineYUP
	 * @throws ClassNotFoundException
	 */
	public void drawFormation(boolean affFormationLicence, boolean affFormationMaster) throws ClassNotFoundException {
		if (affFormationLicence == true && affFormationMaster == true) {
			this.drawOnly = DrawOnly.BOTH;
		} else if (affFormationLicence == true && affFormationMaster == false) {
			this.drawOnly = DrawOnly.LICENCE;
		} else if (affFormationLicence == false && affFormationMaster == true) {
			this.drawOnly = DrawOnly.MASTER;
		}
		// Makes the line arrive in the center of the rectangle
		int lineCENTER = 50;
		// Makes the line go DOWN a little so the line is not on the text
		int lineYDOWN = 7;
		// Makes the line go UP a little so the line is noton the text
		int lineYUP = -20;
		List<Formation> listToDraw = new LinkedList();
		// showing only licence formations
		if (this.drawOnly == DrawOnly.LICENCE || this.drawOnly == DrawOnly.MASTER) {
			listToDraw.addAll(this.fillListOfFormationToShow(drawOnly.toString()));
		}
		if (this.drawOnly == DrawOnly.BOTH) {
			listToDraw = this.getData().getFormations();
		}

		for (Formation l : listToDraw) {
			l.setShown(true);
			g.setPaint(Color.black);
			g.drawString(l.getFullNameWithLink(), l.getPoint().x, l.getPoint().y);
			// write the name of formation
			Rectangle t = new Rectangle(l.getPoint().x - 10, l.getPoint().y - 20, l.getFullName().length() * 10, 25); // draw
			// rectangle
			g.draw(t);
			g.setPaint(Color.blue);
			for (Formation l2 : l.getAvailableFormations()) {
				// draw the lines between the formation and the avalaible
				// formations
				g.drawLine(l.getPoint().x + lineCENTER, l.getPoint().y + lineYDOWN, l2.getPoint().x + lineCENTER,
						l2.getPoint().y + lineYUP);

			}

		}

	}

	/**
	 * This function shows the Admission of a "formation" (if this one is SHOWN
	 * in the SVG)
	 * 
	 * The admission is written in BLUE
	 * 
	 * @param admission
	 * 
	 */

	public void drawAdmission(boolean admission) {

		if (admission == true) {

			for (Formation f : this.getData().getFormations()) {
				if (f.isShown() == true) {
					g.setPaint(Color.blue);
					g.drawString(f.getAdmisssion(), f.getPoint().x - 30, f.getPoint().y - 30);
				}
			}
		}

	}

	/**
	 * This function shows the name of the responsable of a formation (if this
	 * one is SHOWN in the SVG)
	 * 
	 * The responsable name is written in GREEN
	 * 
	 * @param responsable
	 * 
	 */

	public void drawResponsable(boolean reponsable) {

		if (reponsable == true) {

			g.setPaint(Color.green);

			for (Formation f : this.getData().getFormations()) {
				if (f.isShown() == true) {
					if (f.hasGotATeacher(f) == true)
						g.drawString(f.getTeacher().getFullNameTeacher(),
								f.getPoint().x
										- (g.getFontMetrics().stringWidth(f.getTeacher().getFullNameTeacher()) + 5),
								f.getPoint().y);
				}
			}

			g.setPaint(Color.black);

		}

	}

	/**
	 * This function shows the name of the subjects or teachers of a formation
	 * (if this one is SHOWN in the SVG)
	 * 
	 * If the subjects are not shown, teachers wont appear in the SVG also
	 * 
	 * Either we'll have both, or only subjects
	 * 
	 * The size of teachers is smaller and in RED
	 * 
	 * 
	 * @param subject
	 * @param teacher
	 * 
	 */

	public void drawSubjectTeacher(boolean subject, boolean teacher) {

		if (subject == true) {
			int decY = 0;
			for (Formation f : this.getData().getFormations()) {
				if (f.isShown() == true) {
					for (Subject s : f.getSubjects()) {
						g.drawString(s.getTitle(), f.getPoint().x + 100, f.getPoint().y + decY);
						s.setPosX(f.getPoint().x + 100);
						s.setPosY(f.getPoint().y + decY);
						decY += 15;

					}
				}
			}

			if (teacher == true) {
				g.setPaint(Color.red);

				for (Formation f : this.getData().getFormations()) {
					if (f.isShown() == true) {

						for (Subject s : f.getSubjects()) {

							java.awt.Font font = new java.awt.Font("TimesRoman", 9, 9);
							g.setFont(font);
							g.drawString(s.getResponsible().getLastName(),
									s.getPoint().x + (g.getFontMetrics().stringWidth(s.getTitle()) + 30),
									s.getPoint().y);
							decY += 15;

						}
					}

				}
			}

		}

	}

	public static void main(String[] args) throws Exception {
		LicenceSVGGen test = new LicenceSVGGen();
		test.paint(false, true, true, true, true, true, "A3");

	}
}