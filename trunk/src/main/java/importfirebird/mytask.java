/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package importfirebird;

import java.util.TimerTask;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Administrator
 */
public class mytask extends TimerTask  
{  
	private Logger logger = LoggerFactory.getLogger(mytask.class);
	
    JProgressBar jpb = null;  
    public mytask(JProgressBar jp)  
    {  
        this.jpb = jp;  
    }  
  
    @Override
    public void run()  
    {  
    	logger.info("ImportCore.importcount=" + ImportCore.importcount);
    	mytask.this.jpb.setValue(ImportCore.importcount);  
    }  
}  