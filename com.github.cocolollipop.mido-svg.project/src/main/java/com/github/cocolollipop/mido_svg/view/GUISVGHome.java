package com.github.cocolollipop.mido_svg.view;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class GUISVGHome {

	protected Shell shlHome;
	private Button GenererButton;
	private Button tagsbutton;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			GUISVGHome window = new GUISVGHome();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlHome = new Shell();
		shlHome.setSize(450, 300);
		shlHome.setText("Home");
		
		tagsbutton = new Button(shlHome, SWT.NONE);
		
		tagsbutton.setBounds(162, 56, 159, 57);
		tagsbutton.setText("Gestion des Tags");
		
		GenererButton = new Button(shlHome, SWT.NONE);
		
		GenererButton.setBounds(162, 137, 159, 57);
		GenererButton.setText("Générer SVG");

	}
	
	
	private void createEvents() {
		
		
		/** This button opens the GUI of settings  */
		GenererButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				GUISVGGeneratorbis svg = new GUISVGGeneratorbis();
				try {
					svg.open();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			}
		});
		
		
		/** This button opens the GUI of Tags  */

		tagsbutton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				GUISVGTAGAjouter a = new GUISVGTAGAjouter();
				a.open();

			}
		});
		
	}
	
	
	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlHome.open();
		shlHome.layout();
		createEvents();
		while (!shlHome.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}


}
