/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package os_final_try;

import javax.swing.JProgressBar;

/**
 *
 * @author Divam
 */
public class CustomJProgressBar extends JProgressBar {
    public void startProgress(){
        this.setValue(this.getValue()+1);
    }
}
