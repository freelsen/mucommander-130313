package com.mucommander.ui.main;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;

import com.mucommander.conf.MuConfigurations;
import com.mucommander.conf.MuPreference;
import com.mucommander.conf.MuPreferences;
import com.mucommander.conf.MuSnapshot;
import com.mucommander.desktop.DesktopManager;
import com.mucommander.ui.action.ActionKeymap;
import com.mucommander.ui.action.ActionManager;
import com.mucommander.ui.action.impl.CloseWindowAction;
import com.mucommander.ui.button.ToolbarMoreButton;
import com.mucommander.ui.layout.ProportionalSplitPane;
import com.mucommander.ui.layout.YBoxPanel;
import com.mucommander.ui.main.MainFrame.CustomFocusTraversalPolicy;
import com.mucommander.ui.main.commandbar.CommandBar;
import com.mucommander.ui.main.menu.MainMenuBar;
import com.mucommander.ui.main.toolbar.ToolBar;

public class LsFindFrame extends JFrame implements MouseListener,KeyListener {

	public JTable mfindtable;
	public LsFindTableModel mfindtablemodel;
	
	public LsFindFrame() 
	{
		// TODO Auto-generated constructor stub
		init();
	}
	private void init() {
	   	
        initLayout();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
	private void initLayout()
	{
		this.setSize(300, 400);
        setResizable(true);

        // The toolbar should have no inset, this is why it is left out of the insetsPane
        JPanel contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        JPanel insetsPane = new JPanel(new BorderLayout()) {
                // Add an x=3,y=3 gap around content pane
                @Override
                public Insets getInsets() {
                    return new Insets(0, 3, 3, 3);      // No top inset 
                }
            };
            
        // Add a 2-pixel gap between the file table and status bar
        YBoxPanel southPanel = new YBoxPanel();
        southPanel.addSpace(2);

        mfindtablemodel = new LsFindTableModel();        
        mfindtable = new JTable();
        mfindtable.addMouseListener(  this );
        mfindtable.addKeyListener(this);
        mfindtable.setModel(mfindtablemodel);

        contentPane.add(mfindtable, BorderLayout.CENTER);    
	}
	
	public void delPath(){
		int row = mfindtable.getSelectedRow();
		String str = mfindtablemodel.getCellstr( row, 0);
		
		LsFastFoder.mthis.DelPath(str);		
		mfindtablemodel.removeRow(row);
		
		selectRow( row -1 );
		
		mfindtable.updateUI();
	}
	public void GotoPath(){
		int row = mfindtable.getSelectedRow();
		String str =mfindtablemodel.getCellstr(row, 0); //= findtb.getModel().getValueAt(findtb.getSelectedRow(), 0).toString();
		LsFastFoder.mthis.gotoPath(str);	
	}
	
	private void selectRow(int row)
	{
		if( row < 0 ) 
			row = 0;
		if( row < mfindtable.getRowCount())
			mfindtable.setRowSelectionInterval(row, row);
	}
	private void gotoFirstRow()
	{
		gotoRow(mfindtable.getRowCount());
	}
	private void gotoEndRow()
	{
		gotoRow(-mfindtable.getRowCount());
	}
	private void gotoRow(int inc)
	{
		int i = mfindtable.getSelectedRow();
		i = i+inc;
        	if( i <0 )
			i=mfindtable.getRowCount()-1;
		else if(i>= mfindtable.getRowCount())
			i = 0;
		if( i < 0)
			return;
		mfindtable.setRowSelectionInterval(i, i);
	}
	public void keyPressed(KeyEvent e)
	{
		int kc = e.getKeyCode();
		if ( (kc == KeyEvent.VK_ENTER) || (kc == KeyEvent.VK_G)) {
			GotoPath();
			this.dispose();
		}
        else if( (kc == KeyEvent.VK_H) || (kc ==KeyEvent.VK_K)){
        		gotoRow(-1);
		}
		else if( (kc == KeyEvent.VK_J)|| (kc ==KeyEvent.VK_L)){
			gotoRow(+1);
		}
		else if(  (kc ==KeyEvent.VK_U) ){
			gotoFirstRow();
		}
		else if( (kc == KeyEvent.VK_O) ){
			gotoEndRow();
		}
		else if ( (kc == KeyEvent.VK_D) ){
			this.delPath();
		}
	}
	public void keyReleased(KeyEvent e){
		
	}
	public void keyTyped(KeyEvent e){
		
	}
	public void mouseClicked(MouseEvent e){
		if(e.getButton()==MouseEvent.BUTTON1){
			if(e.getClickCount()==2){
				GotoPath();
				this.dispose();
			}
		}
	}
    public void mouseEntered(MouseEvent e) {
    }
    public void mouseExited(MouseEvent e) {
    }
    public void mousePressed(MouseEvent e) {    	
    }
    public void mouseReleased(MouseEvent e) {
    }
    public void mouseDragged(MouseEvent e) {
    }
    public void mouseMoved(MouseEvent e) {
    }

}
