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

	public JTable findtb;
	public  MyTableModel findtm;
	
	public MainFrame mfrm;
	
	public LsFindFrame() 
	{
		mfrm = null;
		// TODO Auto-generated constructor stub
		init();
	}
	private void init() {
    	   	
        // Set the window icon
        //setWindowIcon();

		this.setSize(300, 400);
        // Enable window resize
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

        // Below the toolbar there is the pane with insets
        //contentPane.add(insetsPane, BorderLayout.CENTER);


        findtb = new JTable();
        findtb.addMouseListener(  this );
        findtb.addKeyListener(this);
        
        findtm = new MyTableModel();
        
        findtb.setModel(findtm);
        //findtm.flist = 
        contentPane.add(findtb, BorderLayout.CENTER);
        
        // Add a 2-pixel gap between the file table and status bar
        YBoxPanel southPanel = new YBoxPanel();
        southPanel.addSpace(2);

        // Perform CloseAction when the user asked the window to close
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        //ActionKeymap.registerActions(this);
        //this.setVisible(true); 

    }
	
// JTable findtble operation;	
	public String getCellstr( int row, int col){
		String str = "";
		if( col >= findtb.getModel().getColumnCount() || col < 0)
			return str;
		if( row >= findtb.getModel().getRowCount() || row < 0 )
			return str;
		
		str = findtb.getModel().getValueAt(row, col).toString();
		
		return str;
	}
	public void delPath(){
		int row = findtb.getSelectedRow();
		//System.out.println(row);
		String str = getCellstr( row, 0);
		LsFindTable ft = LsFindTable.lsfind;
		if( ft == null)
			return;
		ft.DelPath(str);
		
		findtm.flist.remove(row);
		//findtb.removeRowSelectionInterval(row, row);
		row = row -1;
		if( row < 0 ) 
			row = 0;
		if( row < findtb.getRowCount())
			findtb.setRowSelectionInterval(row, row);
		findtb.updateUI();
	}
	public void GoPath(){
		//int col = findtb.getModel().getColumnCount();
		int row = findtb.getSelectedRow();
		String str =getCellstr(row, 0); //= findtb.getModel().getValueAt(findtb.getSelectedRow(), 0).toString();
		LsFindTable.lsfind.selstr = str;
		//System.out.println("frame"+str);
		if(mfrm!=null)
		{
			mfrm.getActivePanel().getLocationTextField().setText(str);
			//mfrm.getRightPanel().getLocationTextField().setText(str);
		}
	}
	public void keyPressed(KeyEvent e)
	{
		int kc = e.getKeyCode();
		if ( (kc == KeyEvent.VK_ENTER) || (kc == KeyEvent.VK_G)) {
			GoPath();
			this.dispose();
		}
		//else if( kc == KeyEvent.VK_G){
		//	GoPath();
		//	this.dispose();
		//}
        else if( (kc == KeyEvent.VK_I) || (kc ==KeyEvent.VK_J)){
        	int i = findtb.getSelectedRow();
        	if( i <=0)
        		return;
			findtb.setRowSelectionInterval(i-1, i-1);
		}
		else if( (kc == KeyEvent.VK_K)|| (kc ==KeyEvent.VK_L)){
			int i = findtb.getSelectedRow();
        	if( i > findtb.getRowCount()-1)
        		return;
			findtb.setRowSelectionInterval(i+1, i+1);
		}
		else if ( (kc == KeyEvent.VK_D) || (kc ==KeyEvent.VK_U)){
			//findtb.remo
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
				GoPath();
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
