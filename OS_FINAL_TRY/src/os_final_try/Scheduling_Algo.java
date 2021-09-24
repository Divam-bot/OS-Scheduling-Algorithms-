/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package os_final_try;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;
import java.text.SimpleDateFormat;
import javax.swing.ImageIcon;
import java.util.* ;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumn;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author Divam
 */
class TaskNumeric extends Task {

    public TaskNumeric(String description, long start, long end) {
        super(description, new Date(start), new Date(end));
    }

    public static TaskNumeric duration(String description, long start, long duration) {
        return new TaskNumeric(description, start, start + duration);
    }

}
// process class
class Process {
    //usefull variables
    int PID,AT,BT1,IO,BT2,Notify,CT,BT1copy,BT2copy,BT,dispflag,firstCT,TBT,execcounter,priority;
    // keeps track of each time a process enters cpu
    ArrayList<Integer> startexec = new ArrayList<>();
    // keeps track of each time a process leaves cpu
    ArrayList<Integer> endexec = new ArrayList<>();
    
    public Process(int pid,int prio, int at, int bt1,int io, int bt2) {
       this.PID = pid;
       this.AT = at;
       this.BT1 = bt1;
       this.IO = io;
       this.BT2 = bt2;
       this.BT1copy = bt1;
       this.BT2copy = bt2;
       this.BT = bt1;
       this.dispflag=0;
       this.TBT = bt1+bt2 ;
       this.priority=prio;
    }
    
    @Override
    public String toString(){
       return "p"+this.PID; 
    }
    
    void setCT(int ct){
        this.CT=ct;
    }
}

// Plugging in comparators for different algorithms:
// common to all
class Sortbycompletion implements Comparator<Process>{

    @Override
    public int compare(Process t, Process t1) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return t.CT-t1.CT ;
    }
}
// NON IO NON preamptive algo comparators...

// starting from FCFS
class SortbyarrivalFCFS implements Comparator<Process>{

    @Override
    public int compare(Process p1, Process p2) {
       if(p1.AT > p2.AT)
           return 1;
       else if(p1.AT == p2.AT && p1.PID>p2.PID)
           return 1; 
       else return -1;
    }
}
// for SJF

class SortbyarrivalSJF implements Comparator<Process>{

    @Override
    public int compare(Process p1, Process p2) {
       if(p1.AT > p2.AT)
           return 1;
       else if(p1.AT == p2.AT && p1.BT>p2.BT)
           return 1;
       else if(p1.AT == p2.AT && p1.BT==p2.BT && p1.PID>p2.PID)
           return 1; 
       else return -1;
    }
    
}

class SortbyburstSJF implements Comparator<Process>{

    @Override
    public int compare(Process p1, Process p2) {
        if(p1.BT > p2.BT)
           return 1;
       else if(p1.BT == p2.BT && p1.PID>p2.PID)
           return 1;
       
       else return -1;
    }
}
// for LJF
class SortbyarrivalLJF implements Comparator<Process>{

    @Override
    public int compare(Process p1, Process p2) {
       if(p1.AT > p2.AT)
           return 1;
       else if(p1.AT == p2.AT && p1.BT<p2.BT)
           return 1;
       else if(p1.AT == p2.AT && p1.BT==p2.BT && p1.PID>p2.PID)
           return 1; 
       else return -1;
    } 
}

class SortbyburstLJF implements Comparator<Process>{

    @Override
    public int compare(Process p1, Process p2) {
        if(p1.BT < p2.BT)
           return 1;
       else if(p1.BT == p2.BT && p1.PID>p2.PID)
           return 1;
       
       else return -1;
    }
    
}
// Priority
class SortbyarrivalPriority implements Comparator<Process>{

    @Override
    public int compare(Process p1, Process p2) {
       if(p1.AT > p2.AT)
           return 1;
       else if(p1.AT == p2.AT && p1.priority>p2.priority)
           return 1;
       else if(p1.AT == p2.AT && p1.priority==p2.priority && p1.PID>p2.PID)
           return 1; 
       else return -1;
    }
    
}

class SortbyPriority implements Comparator<Process>{

    @Override
    public int compare(Process t, Process t1) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return t1.priority - t.priority ;
    }
    
}




// NON IO Preamptive Algos
// for RR
// sortbyarrivalFCFS

// for SRTF
// sortbyarrivalSJF

class SortbyburstSRTF implements Comparator<Process>{

    public int compare(Process p1, Process p2) {
        if(p1.BT1copy > p2.BT1copy)
           return 1;
       else if(p1.BT1copy == p2.BT1copy && p1.PID>p2.PID)
           return 1;
       
       else return -1;
    }
    
}

// for LRTF
// sortbyarrivalLJF

class SortbyburstLRTF implements Comparator<Process>{

    public int compare(Process p1, Process p2) {
        if(p1.BT1copy < p2.BT1copy)
           return 1;
       else if(p1.BT1copy == p2.BT1copy && p1.PID>p2.PID)
           return 1;
       
       else return -1;
    }
}

// IO NON preamtive
class Sortbynotify implements Comparator<Process>{

    @Override
    public int compare(Process p1, Process p2) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
       return p1.Notify - p2.Notify ;
    }  
}
// FCFS
//sortbyarrivalFCFS

//SJF
class SortbyarrivalSJFIO implements Comparator<Process>{

    @Override
    public int compare(Process p1, Process p2) {
       if(p1.AT > p2.AT)
           return 1;
       else if(p1.AT == p2.AT && p1.TBT>p2.TBT)
           return 1;
       else if(p1.AT == p2.AT && p1.TBT==p2.TBT && p1.PID>p2.PID)
           return 1; 
       else return -1;
    }
    
}

class SortbyburstSJFIO implements Comparator<Process>{

    public int compare(Process p1, Process p2) {
        if(p1.TBT > p2.TBT)
           return 1;
       else if(p1.TBT == p2.TBT && p1.PID>p2.PID)
           return 1;
       
       else return -1;
    }
    
}

//LJF
class SortbyarrivalLJFIO implements Comparator<Process>{

    @Override
    public int compare(Process p1, Process p2) {
       if(p1.AT > p2.AT)
           return 1;
       else if(p1.AT == p2.AT && p1.TBT<p2.TBT)
           return 1;
       else if(p1.AT == p2.AT && p1.TBT==p2.TBT && p1.PID>p2.PID)
           return 1; 
       else return -1;
    }
    
}
class SortbyburstLJFIO implements Comparator<Process>{

    public int compare(Process p1, Process p2) {
        if(p1.TBT < p2.TBT)
           return 1;
       else if(p1.TBT == p2.TBT && p1.PID<p2.PID)
           return 1;
       
       else return -1;
    }
    
}

// IO preamptive

// RR
//sortbyarrivalFCFS

//SRTF
//sortbyarrivalSJF
//sortbyburstSRTF

//LRTF
//sortbyarrivalLJF
//sortbyburstLRTF

/**
 *
 * @author Divam
 */
public class Scheduling_Algo extends javax.swing.JFrame {

    /**
     * Creates new form Scheduling_Algo
     */
    // Usefull variables
    int dispclck=0, totalprocess,dispcounter=0,dispanticounter=0,stallpulse=0,filecounter=0;
    // GUI elements fixed in arrray list for efficient access
    ArrayList<JTextField>jtn = new ArrayList<>();
    ArrayList<JProgressBar>jpb = new ArrayList();
    ArrayList<JProgressBar>jpbIO = new ArrayList();
    // arraylist to take data from table for effiecent communication 
    ArrayList<Integer>AT = new ArrayList<>();
    ArrayList<Integer>PrioArr = new ArrayList<>();
    ArrayList<Integer>BT1 = new ArrayList<>();
    ArrayList<Integer>BT2 = new ArrayList<>();
    ArrayList<Integer>IO = new ArrayList<>(); 
    ArrayList<Integer>PT = new ArrayList<>();
    // lists which help in animations and preserves final product
    ArrayList<Process> A = new ArrayList<>(); 
    LinkedList<Process> queue = new LinkedList<>();
    LinkedList<Process> ioqueue = new LinkedList<>();
    ArrayList<Process> display = new ArrayList<>();
    ArrayList<Process> IOdisplay = new ArrayList<>();
    
    
    public Scheduling_Algo() {
        initComponents();
    }


    public boolean CtChecker(ArrayList<Process>A){
        int flag=0;
        for(Process p:A){
            if(p.CT==0){
                flag=1;
                break ;
            }
        }
        if(flag==1)return false;
        else return true;
    }
    // plugging in functions for different scheduling algos
    // firstly non IO non preamptive functions
    
    // SJF plugged in
        void noIonoPre(String algo){
        // pluging in arrival comparators
        if(algo=="SJF")
            Collections.sort(queue,new SortbyarrivalSJF());
        else if(algo=="LJF")
            Collections.sort(queue,new SortbyarrivalLJF());
        else if(algo=="Priority")
            Collections.sort(queue,new SortbyarrivalPriority());
        else
            Collections.sort(queue,new SortbyarrivalFCFS());
        
        //usefull variables            
        int clck=0,counter=0,newcounter=0;
        
        for(int i=0;i<100;i++){
            //first time checking and if queue empty condition
            
            if(clck==A.get(counter).AT && queue.isEmpty()){
                queue.add(A.get(counter));
                
                newcounter=counter;
                if(newcounter<A.size()-1){
                    for (int ne=newcounter+1;ne<A.size();ne++){
                        if(A.get(ne).AT==clck){
                            queue.add(A.get(ne));
                            
                            counter=ne;
                            }
                        }              
                    }
                // plugging required comparators
                if(algo=="SJF")
                    Collections.sort(queue,new SortbyburstSJF());
                else if(algo=="LJF")
                    Collections.sort(queue,new SortbyburstLJF());
                else if(algo=="Priority")
                    Collections.sort(queue,new SortbyPriority());
                
                if(counter<A.size()-1) counter+=1;
               
            }
            
            if(queue.size() !=0){
               // adding start of excecution for gui purpose
                queue.get(0).startexec.add(clck);
                // display for animation
                display.add(queue.get(0)) ;
                for (int j=0;j<queue.get(0).BT;j++){
                    clck+=1;
                    if(clck==A.get(counter).AT){
                        queue.add(A.get(counter));
                        
                        newcounter=counter;
                        if(newcounter<A.size()-1){
                            for (int ne=newcounter+1;ne<A.size();ne++){
                                if(A.get(ne).AT==clck){
                                    queue.add(A.get(ne));
                                    
                                    counter=ne;
                                }
                            }              
                        }
                        
                        if(counter<A.size()-1) counter+=1;
                       
                    }                    
                }
                Process r1 = queue.pop();
                r1.setCT(clck);
                // adding end of execution time
                r1.endexec.add(clck);
                // plugging required comparators
                if(algo=="SJF")
                    Collections.sort(queue,new SortbyburstSJF());
                else if(algo=="LJF")
                    Collections.sort(queue,new SortbyburstLJF());
                else if(algo=="Priority")
                    Collections.sort(queue,new SortbyPriority());
            }
            
            // when all proccess gets completed
            if(CtChecker(A)) 
                break;
            
            // Condition where process is yet to come and cpu remains empty...waiting.
            else if(queue.size()==0 && CtChecker(A)==false){
                clck++;
                stallpulse++;
            }
        }
        Collections.sort(A,new Sortbycompletion());
        for(Process i:A)
            System.out.println(i.startexec.toString()+" "+ i.endexec.toString()+" pid: "+i.PID+"  at: "+i.AT +"  bt: "+i.BT+"  ct: "+i.CT);
        
        System.out.println("display "+display.toString());
    }
    
    // NON IO preamptive algos
    // RR plugged in
        void noIoPre(int TQ,String algo){
        // usefull variables
        int clck=0,counter=0,newcounter=0;
        
        // plugging required comparators
        if(algo=="SJF")
            Collections.sort(queue,new SortbyarrivalSJF());
        else if(algo=="LJF")
            Collections.sort(queue,new SortbyarrivalLJF());
        else if(algo=="Pre-Priority")
            Collections.sort(queue,new SortbyarrivalPriority());
        else
            Collections.sort(A, new SortbyarrivalFCFS());
        
        for(int i=0;i<100;i++){
            //first time checking and if queue empty condition
            
            if(clck==A.get(counter).AT && queue.isEmpty()){
                queue.add(A.get(counter));             
                newcounter=counter;
                if(newcounter<A.size()-1){
                    for (int ne=newcounter+1;ne<A.size();ne++){
                        if(A.get(ne).AT==clck){
                            queue.add(A.get(ne));
                            
                            counter=ne;
                            }
                        }              
                    }
                
                // plugging required comparators
                if(algo=="SRTF")
                    Collections.sort(queue,new SortbyburstSRTF());
                else if(algo=="LRTF")
                    Collections.sort(queue,new SortbyburstLRTF());
                else if(algo=="Pre-Priority")
                    Collections.sort(queue,new SortbyPriority());
                
                if(counter<A.size()-1) counter+=1;
                //else counter=counter;
            }
            
            if(queue.size() !=0){
                if(queue.get(0).BT1copy > TQ){
                    //adding start of execution time
                    queue.get(0).startexec.add(clck);
                    // updating BT of process under exec
                    queue.get(0).BT1copy -= TQ;
                    // ading in display queue for animation
                    display.add(queue.get(0)) ;
                    // running clck till TQ
                    for(int j=0;j<TQ;j++){
                        clck++;
                        // to see if any other process enters exec queue
                        if(clck==A.get(counter).AT){
                            queue.add(A.get(counter));
                            
                            newcounter=counter;
                            if(newcounter<A.size()-1){
                                for (int ne=newcounter+1;ne<A.size();ne++){
                                    if(A.get(ne).AT==clck){
                                        queue.add(A.get(ne));
                                        
                                        counter=ne;
                                    }
                                }              
                            }
                            if(counter<A.size()-1) counter+=1;                        
                        }
                    }
                  // poping from front
                  Process r1 = queue.pop();
                  //adding end of execution time
                    r1.endexec.add(clck);
                  // again adding to queue...if BT still remains
                  queue.add(r1);
                  
                  // plugging required comparators
                  if(algo=="SRTF")
                      Collections.sort(queue,new SortbyburstSRTF());
                  else if(algo=="LRTF")
                      Collections.sort(queue,new SortbyburstLRTF());
                  else if(algo=="Pre-Priority")
                    Collections.sort(queue,new SortbyPriority());
                }
                // if BT left is <= timequantum 
                else{
                    //adding start of execution time
                    queue.get(0).startexec.add(clck);
                    // display for animation
                    display.add(queue.get(0));
                    for (int j=0;j<queue.get(0).BT1copy;j++){
                        clck+=1;
                        if(clck==A.get(counter).AT){
                            queue.add(A.get(counter));
                            
                            newcounter=counter;
                            if(newcounter<A.size()-1){
                                for (int ne=newcounter+1;ne<A.size();ne++){
                                    if(A.get(ne).AT==clck){
                                        queue.add(A.get(ne));
                                        
                                        counter=ne;
                                    }
                                }              
                            }
                            if(counter<A.size()-1) counter+=1;
                           
                        }                    
                    }
                    queue.get(0).BT1copy=0;
                    // poping from execqueue and setting current time as its completion time 
                    Process r1 = queue.pop();
                    r1.setCT(clck);
                   //adding end of execution time
                    r1.endexec.add(clck);    
                    // plugging required comparators
                    if(algo=="SRTF")
                        Collections.sort(queue,new SortbyburstSRTF());
                    else if(algo=="LRTF")
                        Collections.sort(queue,new SortbyburstLRTF());
                    else if(algo=="Priority")
                    Collections.sort(queue,new SortbyPriority());
                }
            }
 
            if( CtChecker(A)) 
                break;
            else if(queue.size()==0 && CtChecker(A)==false){
                clck++;
                stallpulse++;
            }
        }
        System.out.println("after TQProcess");
        Collections.sort(A,new Sortbycompletion());
        for(Process i:A){
            System.out.print("execarrays: " +i.startexec.toString()+" "+i.endexec.toString()+" ");
            System.out.println("pid: "+i.PID+"  at: "+i.AT +"  bt: "+i.BT+"  ct: "+i.CT);
        }
        System.out.println("Display Queue: ");
        for(Process d:display)
            System.out.print(d.PID+"  ");
        System.out.print("disp size"+display.size());
    }
        
    // IO NON Preamptive algos
    // plugging FCFSIO 
    void ioNoPre(String algo){
        
        // plugging required comparators
        if(algo=="SJF")
            Collections.sort(queue,new SortbyarrivalSJFIO());
        else if(algo=="LJF")
            Collections.sort(queue,new SortbyarrivalLJFIO());
        else if(algo=="Priority")
            Collections.sort(queue,new SortbyarrivalPriority());
        else
            Collections.sort(A, new SortbyarrivalFCFS());
        
        // Usefull variables
        int clck=0,counter=0,newcounter=0,iocount=0;
        
        for(int i=0;i<100;i++){
            //first time checking and if queue empty condition (in case of stall pulses)
            
            if(clck==A.get(counter).AT && queue.isEmpty()){
                queue.add(A.get(counter));
                                
                // to check if there are more process with same arrival time
                newcounter=counter;
                if(newcounter<A.size()-1){
                    for (int ne=newcounter+1;ne<A.size();ne++){
                        if(A.get(ne).AT==clck){
                            queue.add(A.get(ne));
                            
                            counter=ne;
                            }
                        }              
                    }
                // plugging required comparators
                if(algo=="SJF")
                    Collections.sort(queue,new SortbyburstSJFIO());
                else if(algo=="LJF")
                    Collections.sort(queue,new SortbyburstLJFIO());
                else if(algo=="Priority")
                    Collections.sort(queue,new SortbyPriority());
                
                // once all process from start is in queue then counter dosen't need to move further
                if(counter<A.size()-1) counter+=1;
             
            }
            
            
            if(ioqueue.isEmpty()==false){
                iocount=0;
                //to check if there are more process with same wait time
                for (int z=0;z<ioqueue.size();z++){
                    
                    if(clck==ioqueue.get(z).Notify){
                       
                       // once a process is awakened then 2nd bt is to be given to it
                        ioqueue.get(z).BT = ioqueue.get(z).BT2;
                        queue.add(ioqueue.get(z));
                        ;
                        // icount variable is used to help remember number of process to be removed from ioqueue in next forloop
                        iocount++;                                
                    }    
                }
                // till iocount all process are removed means all these process have been pushed to main queue
                for(int z1=0;z1<iocount;z1++){
                    Process iop=ioqueue.pop();
                }
            }
            
            // main execution starts when queue has atleast one element in it
            if(queue.size() !=0){
                //adding start of execution time
                queue.get(0).startexec.add(clck);
                // for display purpose of sequence of excecution of process
                display.add(queue.get(0)) ;
                for (int j=0;j<queue.get(0).BT;j++){
                    clck+=1;
                    if(clck==A.get(counter).AT){
                        queue.add(A.get(counter));
                       
                        
                        newcounter=counter;
                        if(newcounter<A.size()-1){
                            for (int ne=newcounter+1;ne<A.size();ne++){
                                if(A.get(ne).AT==clck){
                                    queue.add(A.get(ne));
                                    
                                    counter=ne;
                                }
                            }              
                        }
                        if(counter<A.size()-1) counter+=1;
                       
                    }
                    if(ioqueue.isEmpty()==false){
                        iocount=0;
                        for (int z=0;z<ioqueue.size();z++){
                            if(clck==ioqueue.get(z).Notify){
                               
                                ioqueue.get(z).BT = ioqueue.get(z).BT2;
                                queue.add(ioqueue.get(z));
                                
                                iocount++;                                
                            }    
                        }
                        for(int z1=0;z1<iocount;z1++){
                            ioqueue.pop();
                        }
                    }
                }
                
                // After execution process is removed from the main queue
                Process f1=queue.pop();  
                
                // decreasing total bt as comparator works with it
                f1.TBT -= f1.BT ;
                
                //adding end of execution time
                f1.endexec.add(clck);
                // checking that process is removed form main queue has still not got into ioqueue 
                if(f1.BT==f1.BT1 && f1.BT1copy!=0){
                    f1.BT=0;
                    f1.BT1copy=0;
                    // first completion time for gui
                    f1.firstCT = clck;
                    // Process will again come in queue after (current time+ io time)
                    f1.Notify = f1.IO + clck;
                    ioqueue.add(f1);
                    IOdisplay.add(f1);
                    // sorting all process in ioqueue according to notify time, first one to be notified will be removed
                    Collections.sort(ioqueue,new Sortbynotify());
                    Collections.sort(IOdisplay,new Sortbynotify());
                }
                else{
                   f1.BT=0 ;
                   f1.CT=clck;                  
                }    
                // plugging required comparators
                if(algo=="SJF")
                    Collections.sort(queue,new SortbyburstSJFIO());
                else if(algo=="LJF")
                    Collections.sort(queue,new SortbyburstLJFIO());
                else if(algo=="Priority")
                    Collections.sort(queue,new SortbyPriority());
            }
                        
            if(CtChecker(A)) 
                break;
            else if(queue.size()==0  && CtChecker(A)==false){
                clck++;
                stallpulse++;
            }
        }
        
        System.out.println("after FCFSio");
       // Collections.sort(A,new Sortbypid());
        Collections.sort(A,new Sortbycompletion());
        for(Process i:A){
            System.out.println("Start excec: "+i.startexec.toString()+"end excec: "+i.endexec.toString()+"pid: "+i.PID+"  at: "+i.AT +"  bt: "+i.BT1+"  io: "+i.IO+"  bt: "+i.BT2+"  ct: "+i.CT);
        }
        System.out.println("Display: ");
        System.out.println(display.toString());
    }
    
    // IO Preamptive algos
    //plugging in RRIO
    public void ioPre(int TQ,String algo){
        
        // Usefull variables
        int clck=0,counter=0,newcounter=0,iocount=0;
        
        // plugging required comparators
        if(algo=="SRTF")
            Collections.sort(queue,new SortbyarrivalSJFIO());
        else if(algo=="LRTF")
            Collections.sort(queue,new SortbyarrivalLJFIO());
        else if(algo=="Pre-Priority")
            Collections.sort(queue,new SortbyarrivalPriority());
        else
            Collections.sort(A, new SortbyarrivalFCFS());
        
        for(int i=0;i<100;i++){
            //first time checking and if queue empty condition (in case of stall pulses)
            
            if(clck==A.get(counter).AT && queue.isEmpty()){
                queue.add(A.get(counter));
                
                
                // to check if there are more process with same arrival time
                newcounter=counter;
                if(newcounter<A.size()-1){
                    for (int ne=newcounter+1;ne<A.size();ne++){
                        if(A.get(ne).AT==clck){
                            queue.add(A.get(ne));
                           
                            counter=ne;
                            }
                        }              
                    }
                // plugging required comparators
                if(algo=="SRTF")
                    Collections.sort(queue,new SortbyburstSJFIO());
                else if(algo=="LRTF")
                    Collections.sort(queue,new SortbyburstLJFIO());
                else if(algo=="Pre-Priority")
                    Collections.sort(queue,new SortbyPriority());
                
                // once all process from start is in queue then counter dosen't need to move further
                if(counter<A.size()-1) counter+=1;
             
            }
            
            
            if(ioqueue.isEmpty()==false){
                iocount=0;
                //to check if there are more process with same wait time
                for (int z=0;z<ioqueue.size();z++){
                    
                    if(clck==ioqueue.get(z).Notify){
                       
                       // once a process is awakened then 2nd bt is to be given to it
                        ioqueue.get(z).BT = ioqueue.get(z).BT2;
                        queue.add(ioqueue.get(z));
                        
                        // icount variable is used to help remember number of process to be removed from ioqueue in next forloop
                        iocount++;                                
                    }    
                }
                // till iocount all process are removed means all these process have been pushed to main queue
                for(int z1=0;z1<iocount;z1++){
                    Process iop=ioqueue.pop();
                }
            }
            
            // main execution starts when queue has atleast one element in it
            if(queue.size() !=0){
                if(queue.get(0).BT > TQ){
                    //adding start of execution time
                    queue.get(0).startexec.add(clck);
                    display.add(queue.get(0));
                    //decresing BT
                    queue.get(0).BT -= TQ ;
                    // decreasing TBT
                    queue.get(0).TBT -=TQ;
                    for (int j=0;j<TQ;j++){
                        clck+=1;
                        if(clck==A.get(counter).AT){
                            queue.add(A.get(counter));
                            

                            newcounter=counter;
                            if(newcounter<A.size()-1){
                                for (int ne=newcounter+1;ne<A.size();ne++){
                                    if(A.get(ne).AT==clck){
                                        queue.add(A.get(ne));
                                        
                                        counter=ne;
                                    }
                                }              
                            }
                            if(counter<A.size()-1) counter+=1;

                        }
                        if(ioqueue.isEmpty()==false){
                            iocount=0;
                            for (int z=0;z<ioqueue.size();z++){
                                if(clck==ioqueue.get(z).Notify){

                                    ioqueue.get(z).BT = ioqueue.get(z).BT2;
                                    queue.add(ioqueue.get(z));
                                    
                                    iocount++;                                
                                }    
                            }
                            for(int z1=0;z1<iocount;z1++){
                                ioqueue.pop();
                            }
                        }                      
                    }
                    Process ip1 = queue.pop() ;
                    //adding end of execution time
                    ip1.endexec.add(clck);
                    queue.add(ip1);
                    
                    // plugging required comparators
                    if(algo=="SRTF")
                        Collections.sort(queue,new SortbyburstSJFIO());
                    else if(algo=="LRTF")
                        Collections.sort(queue,new SortbyburstLJFIO());
                    else if(algo=="Pre-Priority")
                        Collections.sort(queue,new SortbyPriority());
                }
                else{
                    //adding start of execution time
                    queue.get(0).startexec.add(clck);
                    display.add(queue.get(0));
                    // decreasing TBT
                    queue.get(0).TBT =0;
                    for (int j=0;j<queue.get(0).BT;j++){
                        clck+=1;
                        if(clck==A.get(counter).AT){
                            queue.add(A.get(counter));
                            
                            newcounter=counter;
                            if(newcounter<A.size()-1){
                                for (int ne=newcounter+1;ne<A.size();ne++){
                                    if(A.get(ne).AT==clck){
                                        queue.add(A.get(ne));
                                        
                                        counter=ne;
                                    }
                                }              
                            }
                            if(counter<A.size()-1) counter+=1;

                        }
                        if(ioqueue.isEmpty()==false){
                            iocount=0;
                            for (int z=0;z<ioqueue.size();z++){
                                if(clck==ioqueue.get(z).Notify){

                                    ioqueue.get(z).BT = ioqueue.get(z).BT2;
                                    queue.add(ioqueue.get(z));
                                    
                                    iocount++;                                
                                }    
                            }
                            for(int z1=0;z1<iocount;z1++){
                                ioqueue.pop();
                            }
                        }
                    }

                    // After execution process is removed from the main queue
                    Process f1=queue.pop();            
                    //adding end of execution time
                    f1.endexec.add(clck); 
                    // checking that process is removed form main queue has still not got into ioqueue 
                    if(f1.firstCT==0){
                        
                        
                        // first completion time for gui
                        f1.firstCT = clck;
                        // Process will again come in queue after (current time+ io time)
                        f1.Notify = f1.IO + clck;
                        ioqueue.add(f1);
                        // one time adding in IOdisplay for animation
                        IOdisplay.add(f1);
                        // sorting all process in ioqueue according to notify time, first one to be notified will be removed
                        Collections.sort(ioqueue,new Sortbynotify());
                        Collections.sort(IOdisplay,new Sortbynotify());
                    }
                    else{
                       
                       f1.CT=clck;                  
                    } 
                    // plugging required comparators
                    if(algo=="SRTF")
                        Collections.sort(queue,new SortbyburstSJFIO());
                    else if(algo=="LRTF")
                        Collections.sort(queue,new SortbyburstLJFIO());
                    else if(algo=="Pre-Priority")
                        Collections.sort(queue,new SortbyPriority());
                    
                }
            }
                        
            if(CtChecker(A)) 
                break;
            else if(queue.size()==0  && ioqueue.isEmpty()==false){
                clck++;
                stallpulse++;
            }
        }
        
        System.out.println("after RRio");
       // Collections.sort(A,new Sortbypid());
       Collections.sort(A,new Sortbycompletion());
       for(Process i:A){
            System.out.print("execarrays: " +i.startexec.toString()+" "+i.endexec.toString()+" ");
            System.out.println("pid: "+i.PID+"  at: "+i.AT +"  bt: "+i.BT1+"  io: "+i.IO+"  bt: "+i.BT2+"  ct: "+i.CT);
        }
        
        System.out.println("Display: ");
        System.out.println(display.toString());
    }
    
    // FOr Gant Chart
    private IntervalCategoryDataset getCategoryDataset(ArrayList<Process> A) {
        ArrayList<TaskSeries> gantseries = new ArrayList<>() ;
        
        ArrayList<Task> tasks = new ArrayList<>();
        TaskSeries s1 = new TaskSeries("Process");
        TaskSeriesCollection dataset = new TaskSeriesCollection();
        int i=0;
        for(Process p:A){
            tasks.add(new TaskNumeric(p.toString(),p.startexec.get(0),p.endexec.get(0))); 
            gantseries.add(new TaskSeries(p.toString() )) ;
        }
        for(Process p:A){
            if(p.startexec.size()>1){
                for(int t=1;t<p.startexec.size();t++)
                    tasks.get(i).addSubtask(new TaskNumeric(p.toString(),p.startexec.get(t),p.endexec.get(t))) ;
                    tasks.get(i).addSubtask(tasks.get(i));
            }
            gantseries.get(i).add(tasks.get(i));
            i++;
        }
        for(TaskSeries ts:gantseries)
            dataset.add(ts);
        
        return dataset;
    }   

    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        customJProgressBar1 = new os_algo.CustomJProgressBar();
        Main_scroll_panel = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        numbox = new javax.swing.JTextField();
        // adding document listner

        DocumentListener dl = new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent de) {
                updateFieldState() ;
            }

            @Override
            public void removeUpdate(DocumentEvent de) {

            }

            @Override
            public void changedUpdate(DocumentEvent de) {
                updateFieldState() ;
            }

            protected void updateFieldState() {
                // for tq input field if rr/srtf is choosen
                if(algoselector.getSelectedItem().toString()=="RR" || algoselector.getSelectedItem().toString()=="SRTF" || algoselector.getSelectedItem().toString()=="LRTF"){
                    tqbox.setVisible(true);
                }
                // for filling table with empty grid.
                if(numbox.getText()=="")totalprocess=0;
                else  totalprocess=Integer.parseInt(numbox.getText());

                if(iotoggle.isSelected()){
                    if(algoselector.getSelectedItem().toString()!="Priority" && algoselector.getSelectedItem().toString()!="Pre-Priority" ){
                        String columnNames [] ={"P.NO","AT","BT1","IO","BT2","CT","TAT","WT"};
                        // setting Table
                        jtb1.setModel(new javax.swing.table.DefaultTableModel(
                            totalprocess,8
                        ));
                        for(int i=0;i<columnNames.length;i++){

                            TableColumn tc = jtb1.getColumnModel().getColumn(i);
                            tc.setHeaderValue(columnNames[i]);
                        }
                        for(int j=0;j<totalprocess;j++){
                            //adding automated string values in process column
                            jtb1.setValueAt(j+"", j,0);
                        }
                        jtb1.setSize(780,7*40);
                        jtb1.setShowGrid(true);
                        jtb1.setVisible(true);
                    }
                    else{
                        String columnNames [] ={"P.NO","Prio","AT","BT1","IO","BT2","CT","TAT","WT"};
                        // setting Table
                        jtb1.setModel(new javax.swing.table.DefaultTableModel(
                            totalprocess,9
                        ));
                        for(int i=0;i<columnNames.length;i++){

                            TableColumn tc = jtb1.getColumnModel().getColumn(i);
                            tc.setHeaderValue(columnNames[i]);
                        }
                        for(int j=0;j<totalprocess;j++){
                            //adding automated string values in process column
                            jtb1.setValueAt(j+"", j,0);
                        }
                        jtb1.setSize(780,7*40);
                        jtb1.setShowGrid(true);
                        jtb1.setVisible(true);
                    }
                }
                else{
                    if(algoselector.getSelectedItem().toString()!="Priority" && algoselector.getSelectedItem().toString()!="Pre-Priority" ){
                        String columnNames [] ={"P.NO","AT","BT","CT","TAT","WT"};
                        // setting Table
                        jtb1.setModel(new javax.swing.table.DefaultTableModel(
                            totalprocess,6
                        ));
                        for(int i=0;i<columnNames.length;i++){

                            TableColumn tc = jtb1.getColumnModel().getColumn(i);
                            tc.setHeaderValue(columnNames[i]);
                        }
                        for(int j=0;j<totalprocess;j++){
                            jtb1.setValueAt(j+"", j,0);
                        }
                        jtb1.setShowGrid(true);
                        jtb1.setVisible(true);
                        jtb1.setSize(780,totalprocess*40);
                    }
                    else{
                        String columnNames [] ={"P.NO","Prio","AT","BT","CT","TAT","WT"};
                        // setting Table
                        jtb1.setModel(new javax.swing.table.DefaultTableModel(
                            totalprocess,7
                        ));
                        for(int i=0;i<columnNames.length;i++){

                            TableColumn tc = jtb1.getColumnModel().getColumn(i);
                            tc.setHeaderValue(columnNames[i]);
                        }
                        for(int j=0;j<totalprocess;j++){
                            jtb1.setValueAt(j+"", j,0);
                        }
                        jtb1.setShowGrid(true);
                        jtb1.setVisible(true);
                        jtb1.setSize(780,totalprocess*40);
                    }
                }

            }

        };

        numbox.getDocument().addDocumentListener(dl);

        // jtb1.setSize(400, 10);
        //jScrollPane4.setViewportView(jtb1);
        iotoggle = new javax.swing.JToggleButton();
        algoselector = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtb1 = new javax.swing.JTable();
        jtb1.getTableHeader().setFont(new Font("SansSerif",Font.PLAIN, 24));
        Randomize = new javax.swing.JButton();
        CPU_Q_label = new javax.swing.JLabel();
        IO_Q_label = new javax.swing.JLabel();
        IO_Q_label.setVisible(false);
        CPU_Q_scrollpane = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jpb1 = new javax.swing.JProgressBar();
        jpb2 = new javax.swing.JProgressBar();
        jpb3 = new javax.swing.JProgressBar();
        jpb4 = new javax.swing.JProgressBar();
        jpb5 = new javax.swing.JProgressBar();
        jpb6 = new javax.swing.JProgressBar();
        jpb7 = new javax.swing.JProgressBar();
        jpb8 = new javax.swing.JProgressBar();
        jpb9 = new javax.swing.JProgressBar();
        jpb10 = new javax.swing.JProgressBar();
        jpb11 = new javax.swing.JProgressBar();
        jpb12 = new javax.swing.JProgressBar();
        jpb13 = new javax.swing.JProgressBar();
        jpb14 = new javax.swing.JProgressBar();
        jpb15 = new javax.swing.JProgressBar();
        jpb16 = new javax.swing.JProgressBar();
        jpb17 = new javax.swing.JProgressBar();
        jpb18 = new javax.swing.JProgressBar();
        jpb19 = new javax.swing.JProgressBar();
        jpb20 = new javax.swing.JProgressBar();
        jpb21 = new javax.swing.JProgressBar();
        jpb22 = new javax.swing.JProgressBar();
        jpb23 = new javax.swing.JProgressBar();
        jpb24 = new javax.swing.JProgressBar();
        jpb25 = new javax.swing.JProgressBar();
        jpb26 = new javax.swing.JProgressBar();
        jpb27 = new javax.swing.JProgressBar();
        jpb28 = new javax.swing.JProgressBar();
        jpb29 = new javax.swing.JProgressBar();
        jpb30 = new javax.swing.JProgressBar();
        jpb31 = new javax.swing.JProgressBar();
        jpb32 = new javax.swing.JProgressBar();
        jpb33 = new javax.swing.JProgressBar();
        jpb34 = new javax.swing.JProgressBar();
        jpb35 = new javax.swing.JProgressBar();
        jpb36 = new javax.swing.JProgressBar();
        jpb37 = new javax.swing.JProgressBar();
        jpb38 = new javax.swing.JProgressBar();
        jpb39 = new javax.swing.JProgressBar();
        jpb40 = new javax.swing.JProgressBar();
        jpb41 = new javax.swing.JProgressBar();
        jpb42 = new javax.swing.JProgressBar();
        jpb43 = new javax.swing.JProgressBar();
        jpb44 = new javax.swing.JProgressBar();
        jpb45 = new javax.swing.JProgressBar();
        jpb46 = new javax.swing.JProgressBar();
        jpb47 = new javax.swing.JProgressBar();
        jpb48 = new javax.swing.JProgressBar();
        jpb49 = new javax.swing.JProgressBar();
        jpb50 = new javax.swing.JProgressBar();
        jpb51 = new javax.swing.JProgressBar();
        jpb52 = new javax.swing.JProgressBar();
        jpb53 = new javax.swing.JProgressBar();
        jpb54 = new javax.swing.JProgressBar();
        jpb55 = new javax.swing.JProgressBar();
        jpb56 = new javax.swing.JProgressBar();
        jpb57 = new javax.swing.JProgressBar();
        jpb58 = new javax.swing.JProgressBar();
        jpb59 = new javax.swing.JProgressBar();
        jpb60 = new javax.swing.JProgressBar();
        jpb61 = new javax.swing.JProgressBar();
        jpb62 = new javax.swing.JProgressBar();
        jpb63 = new javax.swing.JProgressBar();
        jpb64 = new javax.swing.JProgressBar();
        jpb65 = new javax.swing.JProgressBar();
        jpb66 = new javax.swing.JProgressBar();
        jpb67 = new javax.swing.JProgressBar();
        jpb68 = new javax.swing.JProgressBar();
        jpb69 = new javax.swing.JProgressBar();
        jpb70 = new javax.swing.JProgressBar();
        jpb71 = new javax.swing.JProgressBar();
        jpb72 = new javax.swing.JProgressBar();
        jpb73 = new javax.swing.JProgressBar();
        jpb74 = new javax.swing.JProgressBar();
        jpb75 = new javax.swing.JProgressBar();
        jpb76 = new javax.swing.JProgressBar();
        jpb77 = new javax.swing.JProgressBar();
        jpb78 = new javax.swing.JProgressBar();
        jpb79 = new javax.swing.JProgressBar();
        jpb80 = new javax.swing.JProgressBar();
        jpb81 = new javax.swing.JProgressBar();
        jpb82 = new javax.swing.JProgressBar();
        jpb83 = new javax.swing.JProgressBar();
        jpb84 = new javax.swing.JProgressBar();
        jpb85 = new javax.swing.JProgressBar();
        jpb86 = new javax.swing.JProgressBar();
        jpb87 = new javax.swing.JProgressBar();
        jpb88 = new javax.swing.JProgressBar();
        jpb89 = new javax.swing.JProgressBar();
        jpb90 = new javax.swing.JProgressBar();
        jpb91 = new javax.swing.JProgressBar();
        jpb92 = new javax.swing.JProgressBar();
        jpb93 = new javax.swing.JProgressBar();
        jpb94 = new javax.swing.JProgressBar();
        jpb95 = new javax.swing.JProgressBar();
        jpb96 = new javax.swing.JProgressBar();
        jpb97 = new javax.swing.JProgressBar();
        jpb98 = new javax.swing.JProgressBar();
        jpb99 = new javax.swing.JProgressBar();
        jpb100 = new javax.swing.JProgressBar();
        Start = new javax.swing.JButton();
        Reset = new javax.swing.JButton();
        tqbox = new javax.swing.JTextField();
        clock = new javax.swing.JTextField();
        clocklabel = new javax.swing.JLabel();
        tatchartlabel = new javax.swing.JLabel();
        tatis = new javax.swing.JLabel();
        wtis = new javax.swing.JLabel();
        wtchartlabel = new javax.swing.JLabel();
        effis = new javax.swing.JLabel();
        effchartlabel = new javax.swing.JLabel();
        gantchartlabel = new javax.swing.JLabel();
        numboxlabel = new javax.swing.JLabel();
        tqboxlabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        IO_Q_scrollpanel = new javax.swing.JScrollPane();
        IO_Q_scrollpanel.setVisible(false);
        jPanel4 = new javax.swing.JPanel();
        jpb101 = new javax.swing.JProgressBar();
        jpb102 = new javax.swing.JProgressBar();
        jpb103 = new javax.swing.JProgressBar();
        jpb104 = new javax.swing.JProgressBar();
        jpb105 = new javax.swing.JProgressBar();
        jpb106 = new javax.swing.JProgressBar();
        jpb107 = new javax.swing.JProgressBar();
        jpb108 = new javax.swing.JProgressBar();
        jpb109 = new javax.swing.JProgressBar();
        jpb110 = new javax.swing.JProgressBar();
        jpb111 = new javax.swing.JProgressBar();
        jpb112 = new javax.swing.JProgressBar();
        jpb113 = new javax.swing.JProgressBar();
        jpb114 = new javax.swing.JProgressBar();
        jpb115 = new javax.swing.JProgressBar();
        jpb116 = new javax.swing.JProgressBar();
        jpb117 = new javax.swing.JProgressBar();
        jpb118 = new javax.swing.JProgressBar();
        jpb119 = new javax.swing.JProgressBar();
        jpb120 = new javax.swing.JProgressBar();
        jpb121 = new javax.swing.JProgressBar();
        jpb122 = new javax.swing.JProgressBar();
        jpb123 = new javax.swing.JProgressBar();
        jpb124 = new javax.swing.JProgressBar();
        jpb125 = new javax.swing.JProgressBar();
        jpb126 = new javax.swing.JProgressBar();
        jpb127 = new javax.swing.JProgressBar();
        jpb128 = new javax.swing.JProgressBar();
        jpb129 = new javax.swing.JProgressBar();
        jpb130 = new javax.swing.JProgressBar();
        jpb131 = new javax.swing.JProgressBar();
        jpb132 = new javax.swing.JProgressBar();
        jpb133 = new javax.swing.JProgressBar();
        jpb134 = new javax.swing.JProgressBar();
        jpb135 = new javax.swing.JProgressBar();
        jpb136 = new javax.swing.JProgressBar();
        jpb137 = new javax.swing.JProgressBar();
        jpb138 = new javax.swing.JProgressBar();
        jpb139 = new javax.swing.JProgressBar();
        jpb140 = new javax.swing.JProgressBar();
        jpb141 = new javax.swing.JProgressBar();
        jpb142 = new javax.swing.JProgressBar();
        jpb143 = new javax.swing.JProgressBar();
        jpb144 = new javax.swing.JProgressBar();
        jpb145 = new javax.swing.JProgressBar();
        jpb146 = new javax.swing.JProgressBar();
        jpb147 = new javax.swing.JProgressBar();
        jpb148 = new javax.swing.JProgressBar();
        jpb149 = new javax.swing.JProgressBar();
        jpb150 = new javax.swing.JProgressBar();
        jpb151 = new javax.swing.JProgressBar();
        jpb152 = new javax.swing.JProgressBar();
        jpb153 = new javax.swing.JProgressBar();
        jpb154 = new javax.swing.JProgressBar();
        jpb155 = new javax.swing.JProgressBar();
        jpb156 = new javax.swing.JProgressBar();
        jpb157 = new javax.swing.JProgressBar();
        jpb158 = new javax.swing.JProgressBar();
        jpb159 = new javax.swing.JProgressBar();
        jpb160 = new javax.swing.JProgressBar();
        jpb161 = new javax.swing.JProgressBar();
        jpb162 = new javax.swing.JProgressBar();
        jpb163 = new javax.swing.JProgressBar();
        jpb164 = new javax.swing.JProgressBar();
        jpb165 = new javax.swing.JProgressBar();
        jpb166 = new javax.swing.JProgressBar();
        jpb167 = new javax.swing.JProgressBar();
        jpb168 = new javax.swing.JProgressBar();
        jpb169 = new javax.swing.JProgressBar();
        jpb170 = new javax.swing.JProgressBar();
        jpb171 = new javax.swing.JProgressBar();
        jpb172 = new javax.swing.JProgressBar();
        jpb173 = new javax.swing.JProgressBar();
        jpb174 = new javax.swing.JProgressBar();
        jpb175 = new javax.swing.JProgressBar();
        jpb176 = new javax.swing.JProgressBar();
        jpb177 = new javax.swing.JProgressBar();
        jpb178 = new javax.swing.JProgressBar();
        jpb179 = new javax.swing.JProgressBar();
        jpb180 = new javax.swing.JProgressBar();
        jpb181 = new javax.swing.JProgressBar();
        jpb182 = new javax.swing.JProgressBar();
        jpb183 = new javax.swing.JProgressBar();
        jpb184 = new javax.swing.JProgressBar();
        jpb185 = new javax.swing.JProgressBar();
        jpb186 = new javax.swing.JProgressBar();
        jpb187 = new javax.swing.JProgressBar();
        jpb188 = new javax.swing.JProgressBar();
        jpb189 = new javax.swing.JProgressBar();
        jpb190 = new javax.swing.JProgressBar();
        jpb191 = new javax.swing.JProgressBar();
        jpb192 = new javax.swing.JProgressBar();
        jpb193 = new javax.swing.JProgressBar();
        jpb194 = new javax.swing.JProgressBar();
        jpb195 = new javax.swing.JProgressBar();
        jpb196 = new javax.swing.JProgressBar();
        jpb197 = new javax.swing.JProgressBar();
        jpb198 = new javax.swing.JProgressBar();
        jpb199 = new javax.swing.JProgressBar();
        jpb200 = new javax.swing.JProgressBar();
        jpb1.setVisible(false);
        jpb2.setVisible(false);
        jpb3.setVisible(false);
        jpb4.setVisible(false);
        jpb5.setVisible(false);
        jpb6.setVisible(false);
        jpb7.setVisible(false);
        jpb8.setVisible(false);
        jpb9.setVisible(false);
        jpb10.setVisible(false);
        jpb11.setVisible(false);
        jpb12.setVisible(false);
        jpb13.setVisible(false);
        jpb14.setVisible(false);
        jpb15.setVisible(false);
        jpb16.setVisible(false);
        jpb17.setVisible(false);
        jpb18.setVisible(false);
        jpb19.setVisible(false);
        jpb20.setVisible(false);
        jpb21.setVisible(false);
        jpb22.setVisible(false);
        jpb23.setVisible(false);
        jpb24.setVisible(false);
        jpb25.setVisible(false);
        jpb26.setVisible(false);
        jpb27.setVisible(false);
        jpb28.setVisible(false);
        jpb29.setVisible(false);
        jpb30.setVisible(false);
        jpb31.setVisible(false);
        jpb32.setVisible(false);
        jpb33.setVisible(false);
        jpb34.setVisible(false);
        jpb35.setVisible(false);
        jpb36.setVisible(false);
        jpb37.setVisible(false);
        jpb38.setVisible(false);
        jpb39.setVisible(false);
        jpb40.setVisible(false);
        jpb41.setVisible(false);
        jpb42.setVisible(false);
        jpb43.setVisible(false);
        jpb44.setVisible(false);
        jpb45.setVisible(false);
        jpb46.setVisible(false);
        jpb47.setVisible(false);
        jpb48.setVisible(false);
        jpb49.setVisible(false);
        jpb50.setVisible(false);
        jpb51.setVisible(false);
        jpb52.setVisible(false);
        jpb53.setVisible(false);
        jpb54.setVisible(false);
        jpb55.setVisible(false);
        jpb56.setVisible(false);
        jpb57.setVisible(false);
        jpb58.setVisible(false);
        jpb59.setVisible(false);
        jpb60.setVisible(false);
        jpb61.setVisible(false);
        jpb62.setVisible(false);
        jpb63.setVisible(false);
        jpb64.setVisible(false);
        jpb65.setVisible(false);
        jpb66.setVisible(false);
        jpb67.setVisible(false);
        jpb68.setVisible(false);
        jpb69.setVisible(false);
        jpb70.setVisible(false);
        jpb71.setVisible(false);
        jpb72.setVisible(false);
        jpb73.setVisible(false);
        jpb74.setVisible(false);
        jpb75.setVisible(false);
        jpb76.setVisible(false);
        jpb77.setVisible(false);
        jpb78.setVisible(false);
        jpb79.setVisible(false);
        jpb80.setVisible(false);
        jpb81.setVisible(false);
        jpb82.setVisible(false);
        jpb83.setVisible(false);
        jpb84.setVisible(false);
        jpb85.setVisible(false);
        jpb86.setVisible(false);
        jpb87.setVisible(false);
        jpb88.setVisible(false);
        jpb89.setVisible(false);
        jpb90.setVisible(false);
        jpb91.setVisible(false);
        jpb92.setVisible(false);
        jpb93.setVisible(false);
        jpb94.setVisible(false);
        jpb95.setVisible(false);
        jpb96.setVisible(false);
        jpb97.setVisible(false);
        jpb98.setVisible(false);
        jpb99.setVisible(false);
        jpb100.setVisible(false);
        jpb101.setVisible(false);
        jpb102.setVisible(false);
        jpb103.setVisible(false);
        jpb104.setVisible(false);
        jpb105.setVisible(false);
        jpb106.setVisible(false);
        jpb107.setVisible(false);
        jpb108.setVisible(false);
        jpb109.setVisible(false);
        jpb110.setVisible(false);
        jpb111.setVisible(false);
        jpb112.setVisible(false);
        jpb113.setVisible(false);
        jpb114.setVisible(false);
        jpb115.setVisible(false);
        jpb116.setVisible(false);
        jpb117.setVisible(false);
        jpb118.setVisible(false);
        jpb119.setVisible(false);
        jpb120.setVisible(false);
        jpb121.setVisible(false);
        jpb122.setVisible(false);
        jpb123.setVisible(false);
        jpb124.setVisible(false);
        jpb125.setVisible(false);
        jpb126.setVisible(false);
        jpb127.setVisible(false);
        jpb128.setVisible(false);
        jpb129.setVisible(false);
        jpb130.setVisible(false);
        jpb131.setVisible(false);
        jpb132.setVisible(false);
        jpb133.setVisible(false);
        jpb134.setVisible(false);
        jpb135.setVisible(false);
        jpb136.setVisible(false);
        jpb137.setVisible(false);
        jpb138.setVisible(false);
        jpb139.setVisible(false);
        jpb140.setVisible(false);
        jpb141.setVisible(false);
        jpb142.setVisible(false);
        jpb143.setVisible(false);
        jpb144.setVisible(false);
        jpb145.setVisible(false);
        jpb146.setVisible(false);
        jpb147.setVisible(false);
        jpb148.setVisible(false);
        jpb149.setVisible(false);
        jpb150.setVisible(false);
        jpb151.setVisible(false);
        jpb152.setVisible(false);
        jpb153.setVisible(false);
        jpb154.setVisible(false);
        jpb155.setVisible(false);
        jpb156.setVisible(false);
        jpb157.setVisible(false);
        jpb158.setVisible(false);
        jpb159.setVisible(false);
        jpb160.setVisible(false);
        jpb161.setVisible(false);
        jpb162.setVisible(false);
        jpb163.setVisible(false);
        jpb164.setVisible(false);
        jpb165.setVisible(false);
        jpb166.setVisible(false);
        jpb167.setVisible(false);
        jpb168.setVisible(false);
        jpb169.setVisible(false);
        jpb170.setVisible(false);
        jpb171.setVisible(false);
        jpb172.setVisible(false);
        jpb173.setVisible(false);
        jpb174.setVisible(false);
        jpb175.setVisible(false);
        jpb176.setVisible(false);
        jpb177.setVisible(false);
        jpb178.setVisible(false);
        jpb179.setVisible(false);
        jpb180.setVisible(false);
        jpb181.setVisible(false);
        jpb182.setVisible(false);
        jpb183.setVisible(false);
        jpb184.setVisible(false);
        jpb185.setVisible(false);
        jpb186.setVisible(false);
        jpb187.setVisible(false);
        jpb188.setVisible(false);
        jpb189.setVisible(false);
        jpb190.setVisible(false);
        jpb191.setVisible(false);
        jpb192.setVisible(false);
        jpb193.setVisible(false);
        jpb194.setVisible(false);
        jpb195.setVisible(false);
        jpb196.setVisible(false);
        jpb197.setVisible(false);
        jpb198.setVisible(false);
        jpb199.setVisible(false);
        jpb200.setVisible(false);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Main_scroll_panel.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel1.setMaximumSize(new java.awt.Dimension(1920, 32767));
        jPanel1.setPreferredSize(new java.awt.Dimension(1920, 3000));

        numbox.setBackground(new java.awt.Color(217, 217, 217));
        numbox.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        numbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numboxActionPerformed(evt);
            }
        });

        iotoggle.setFont(new java.awt.Font("Tahoma", 0, 48)); // NOI18N
        iotoggle.setText("IO");
        iotoggle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iotoggleActionPerformed(evt);
            }
        });

        algoselector.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        algoselector.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Algo", "FCFS", "SJF", "LJF", "RR", "SRTF", "LRTF", "Priority", "Pre-Priority", "HRNN", " " }));
        algoselector.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                algoselectorItemStateChanged(evt);
            }
        });

        jtb1.setBackground(new java.awt.Color(217, 217, 217));
        jtb1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jtb1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null}
            },
            new String [] {
                "P.NO.", "AT", "BT", "CT", "TAT", "WT"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jtb1.setDropMode(javax.swing.DropMode.INSERT_ROWS);
        jtb1.setGridColor(new java.awt.Color(255, 87, 87));
        jtb1.setInheritsPopupMenu(true);
        jtb1.setRowHeight(50);
        jtb1.setSelectionBackground(new java.awt.Color(82, 113, 255));
        jScrollPane1.setViewportView(jtb1);

        Randomize.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        Randomize.setText("Randomize");
        Randomize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RandomizeActionPerformed(evt);
            }
        });

        CPU_Q_label.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        CPU_Q_label.setText("CPU_Q");

        IO_Q_label.setFont(new java.awt.Font("Tahoma", 0, 40)); // NOI18N
        IO_Q_label.setText("IO_Q");

        CPU_Q_scrollpane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jpb1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb2, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb3, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb4, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jpb5, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb6, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb7, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb8, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb9, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb10, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb11, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb12, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb13, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb14, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb15, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb16, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb17, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb18, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb19, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb20, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb21, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb22, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb23, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb24, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb25, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb26, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb27, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb28, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb29, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb30, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb31, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb32, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb33, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb34, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb35, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb36, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb37, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb38, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb39, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(jpb40, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb41, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb42, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb43, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb44, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb45, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb46, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb47, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb48, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb49, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb50, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb51, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb52, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb53, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb54, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb55, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb56, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb57, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb58, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb59, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb60, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb61, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb62, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb63, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb64, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jpb65, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb66, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb67, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb68, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb69, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb70, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb71, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb72, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb73, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb74, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb75, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb76, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb77, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb78, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb79, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb80, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb81, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb82, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb83, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb84, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb85, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb86, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb87, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb88, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb89, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb90, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb91, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb92, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb93, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb94, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb95, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb96, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb97, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb98, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb99, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb100, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(298, 298, 298))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jpb98, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jpb99, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jpb100, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jpb96, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jpb97, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jpb93, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jpb94, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jpb95, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jpb75, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jpb76, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jpb80, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jpb79, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jpb78, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jpb77, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jpb81, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jpb83, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jpb84, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jpb82, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jpb85, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jpb86, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jpb87, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jpb88, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jpb89, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jpb91, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jpb92, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jpb90, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jpb7, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb4, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb6, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb5, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb10, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb9, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb12, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb8, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jpb13, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jpb14, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jpb15, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jpb16, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jpb17, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jpb20, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jpb26, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jpb27, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jpb28, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jpb31, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jpb32, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jpb30, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jpb29, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jpb33, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(jpb37, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jpb34, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jpb35, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addComponent(jpb36, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(jpb25, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jpb21, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jpb22, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jpb23, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jpb24, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jpb38, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jpb39, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jpb41, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb42, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb43, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb40, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb44, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb46, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb47, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb45, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb48, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jpb50, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jpb51, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jpb52, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jpb49, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jpb53, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jpb54, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jpb55, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addComponent(jpb56, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb57, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb58, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb59, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jpb61, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jpb60, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jpb62, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jpb63, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jpb64, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jpb65, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb66, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb68, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb67, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb69, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jpb74, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jpb73, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jpb71, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb72, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb70, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        CPU_Q_scrollpane.setViewportView(jPanel2);

        Start.setFont(new java.awt.Font("Times New Roman", 0, 36)); // NOI18N
        Start.setText("Start");
        Start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StartActionPerformed(evt);
            }
        });

        Reset.setFont(new java.awt.Font("Times New Roman", 0, 36)); // NOI18N
        Reset.setText("Reset");
        Reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ResetActionPerformed(evt);
            }
        });

        tqbox.setBackground(new java.awt.Color(217, 217, 217));
        tqbox.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N

        clock.setFont(new java.awt.Font("Times New Roman", 0, 36)); // NOI18N
        clock.setText("    ");

        clocklabel.setFont(new java.awt.Font("Times New Roman", 0, 48)); // NOI18N
        clocklabel.setText("Clock");

        tatchartlabel.setFont(new java.awt.Font("Tahoma", 0, 80)); // NOI18N
        tatchartlabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/os_algo/homepage.png"))); // NOI18N

        tatis.setFont(new java.awt.Font("Times New Roman", 0, 36)); // NOI18N

        wtis.setFont(new java.awt.Font("Times New Roman", 0, 36)); // NOI18N
        wtis.setPreferredSize(new java.awt.Dimension(54, 82));

        wtchartlabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/os_algo/homepage.png"))); // NOI18N

        effis.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N

        effchartlabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/os_algo/homepage.png"))); // NOI18N

        gantchartlabel.setText(".");

        numboxlabel.setFont(new java.awt.Font("Times New Roman", 0, 36)); // NOI18N
        numboxlabel.setText("No. of process");

        tqboxlabel.setFont(new java.awt.Font("Times New Roman", 0, 36)); // NOI18N
        tqboxlabel.setText("TQ");

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 55)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 87, 87));
        jLabel1.setText("Scheculing Algorithm");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/os_algo/bar_50.png"))); // NOI18N

        IO_Q_scrollpanel.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jpb101, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb102, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb103, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb104, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jpb105, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb106, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb107, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb108, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb109, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb110, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb111, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb112, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb113, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb114, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb115, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb116, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb117, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb118, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb119, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb120, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb121, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb122, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb123, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb124, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb125, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb126, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb127, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb128, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb129, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb130, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb131, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb132, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb133, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb134, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb135, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb136, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb137, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb138, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb139, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(jpb140, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb141, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb142, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb143, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb144, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb145, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb146, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb147, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb148, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb149, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb150, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb151, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb152, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb153, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb154, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb155, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb156, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb157, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb158, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb159, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb160, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb161, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb162, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb163, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb164, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jpb165, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb166, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb167, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb168, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb169, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb170, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb171, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb172, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb173, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb174, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb175, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb176, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb177, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb178, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb179, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb180, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb181, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb182, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb183, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb184, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb185, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb186, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb187, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb188, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb189, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb190, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb191, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb192, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb193, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb194, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb195, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb196, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb197, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb198, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb199, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpb200, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(298, 298, 298))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jpb198, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jpb199, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jpb200, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jpb196, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jpb197, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jpb193, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jpb194, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jpb195, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jpb175, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jpb176, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jpb180, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jpb179, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jpb178, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jpb177, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jpb181, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jpb183, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jpb184, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jpb182, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jpb185, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jpb186, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jpb187, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jpb188, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jpb189, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jpb191, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jpb192, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jpb190, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jpb107, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb102, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb101, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb103, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb104, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb106, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb105, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb110, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb109, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb111, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb112, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb118, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb119, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb108, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jpb113, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jpb114, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jpb115, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jpb116, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jpb117, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jpb120, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jpb126, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jpb127, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jpb128, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jpb131, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jpb132, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jpb130, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jpb129, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jpb133, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(jpb137, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jpb134, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jpb135, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addComponent(jpb136, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(jpb125, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jpb121, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jpb122, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jpb123, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jpb124, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jpb138, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jpb139, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jpb141, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb142, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb143, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb140, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb144, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb146, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb147, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb145, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jpb148, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jpb150, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jpb151, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jpb152, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jpb149, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jpb153, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jpb154, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jpb155, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addComponent(jpb156, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb157, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb158, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb159, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jpb161, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jpb160, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jpb162, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jpb163, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jpb164, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jpb165, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb166, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb168, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb167, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb169, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jpb174, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jpb173, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jpb171, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb172, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jpb170, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        IO_Q_scrollpanel.setViewportView(jPanel4);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(gantchartlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 1500, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tatchartlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tatis, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(141, 141, 141)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(effis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(effchartlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(151, 151, 151)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(wtchartlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(wtis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(242, 242, 242))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(tqbox, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(164, 164, 164)
                                        .addComponent(clock, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(155, 155, 155)
                                        .addComponent(clocklabel, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(158, 158, 158)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 780, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Randomize, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Start, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Reset, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(257, 257, 257)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(CPU_Q_label, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
                            .addComponent(IO_Q_label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(CPU_Q_scrollpane, javax.swing.GroupLayout.DEFAULT_SIZE, 829, Short.MAX_VALUE)
                            .addComponent(IO_Q_scrollpanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 767, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(440, 440, 440)
                        .addComponent(algoselector, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(iotoggle)
                        .addGap(178, 178, 178)
                        .addComponent(numbox, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(822, 822, 822)
                        .addComponent(numboxlabel)
                        .addGap(96, 96, 96)
                        .addComponent(tqboxlabel))
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 1798, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 122, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(numboxlabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(numbox)
                                .addGap(1, 1, 1))
                            .addComponent(algoselector, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(iotoggle, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tqboxlabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tqbox, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(Randomize, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Start, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Reset, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(72, 72, 72)
                            .addComponent(clocklabel, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(clock, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(96, 96, 96)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(CPU_Q_label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(CPU_Q_scrollpane, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE))
                .addGap(68, 68, 68)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(IO_Q_label, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                    .addComponent(IO_Q_scrollpanel))
                .addGap(160, 160, 160)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(wtis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(wtchartlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(effis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(effchartlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(tatis, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(tatchartlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(161, 161, 161)
                .addComponent(gantchartlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 1065, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(151, 151, 151))
        );

        Main_scroll_panel.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Main_scroll_panel, javax.swing.GroupLayout.DEFAULT_SIZE, 1914, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Main_scroll_panel, javax.swing.GroupLayout.PREFERRED_SIZE, 1063, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void numboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_numboxActionPerformed

    private void iotoggleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iotoggleActionPerformed
        // TODO add your handling code here:
        if(iotoggle.isSelected()){
            IO_Q_label.setVisible(true);
            IO_Q_scrollpanel.setVisible(true);
        }
        else{
            IO_Q_label.setVisible(false);
            IO_Q_scrollpanel.setVisible(false);
        }
    }//GEN-LAST:event_iotoggleActionPerformed

    private void algoselectorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_algoselectorItemStateChanged
        // TODO add your handling code here:
        if(algoselector.getSelectedItem().toString()=="RR" || algoselector.getSelectedItem().toString()=="SRTF" ||algoselector.getSelectedItem().toString()=="LRTF"  || algoselector.getSelectedItem().toString()=="Pre-Priority" ){
            tqbox.setVisible(true);
            tqboxlabel.setVisible(true);
        }
        else{
            tqbox.setVisible(false);
            tqboxlabel.setVisible(false);
        }

    }//GEN-LAST:event_algoselectorItemStateChanged

    private void RandomizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RandomizeActionPerformed
        // Randomly fillng data in table
        Random rand = new Random();
        for (int i=0;i<totalprocess;i++){
            // when IO is on
            if(algoselector.getSelectedItem().toString()=="Priority" || algoselector.getSelectedItem().toString()=="Pre-Priority"){
                if(iotoggle.isSelected()){
                    // filling prio randomly
                    jtb1.setValueAt(rand.nextInt(10)+1+"", i, 1);
                    // filling AT column
                    jtb1.setValueAt(i+"", i, 2);
                    // filling bt1 randomly
                    jtb1.setValueAt(rand.nextInt(10)+1+"", i, 3);
                    //filling io randodmly
                    jtb1.setValueAt(rand.nextInt(10)+1+"", i, 4);
                    // filling bt2 randomly
                    jtb1.setValueAt(rand.nextInt(10)+1+"", i, 5);
                }
                else{
                    // filling prio randomly
                    jtb1.setValueAt(rand.nextInt(10)+1+"", i, 1);
                    // filling AT column
                    jtb1.setValueAt(i+"", i, 2);
                    // filling bt1 randomly
                    jtb1.setValueAt(rand.nextInt(10)+1+"", i, 3);
                }
            }
            else{
                if(iotoggle.isSelected()){
                    // filling AT column
                    jtb1.setValueAt(i+"", i, 1);
                    // filling bt1 randomly
                    jtb1.setValueAt(rand.nextInt(10)+1+"", i, 2);
                    //filling io randodmly
                    jtb1.setValueAt(rand.nextInt(10)+1+"", i, 3);
                    // filling bt2 randomly
                    jtb1.setValueAt(rand.nextInt(10)+1+"", i, 4);
                }
                else{
                    // filling AT column
                    jtb1.setValueAt(i+"", i, 1);
                    // filling bt1 randomly
                    jtb1.setValueAt(rand.nextInt(10)+1+"", i, 2);
                }
            }
        }
    }//GEN-LAST:event_RandomizeActionPerformed

    private void StartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StartActionPerformed

        //adding all jprogress bar to arraylist for easier access

        jpb.add(jpb1) ;
        jpb.add(jpb2) ;
        jpb.add(jpb3) ;
        jpb.add(jpb4) ;
        jpb.add(jpb5) ;
        jpb.add(jpb6) ;
        jpb.add(jpb7) ;
        jpb.add(jpb8) ;
        jpb.add(jpb9) ;
        jpb.add(jpb10) ;
        jpb.add(jpb11) ;
        jpb.add(jpb12) ;
        jpb.add(jpb13) ;
        jpb.add(jpb14) ;
        jpb.add(jpb15) ;
        jpb.add(jpb16) ;
        jpb.add(jpb17) ;
        jpb.add(jpb18) ;
        jpb.add(jpb19) ;
        jpb.add(jpb20) ;
        jpb.add(jpb21) ;
        jpb.add(jpb22) ;
        jpb.add(jpb23) ;
        jpb.add(jpb24) ;
        jpb.add(jpb25) ;
        jpb.add(jpb26) ;
        jpb.add(jpb27) ;
        jpb.add(jpb28) ;
        jpb.add(jpb29) ;
        jpb.add(jpb30) ;
        jpb.add(jpb31) ;
        jpb.add(jpb32) ;
        jpb.add(jpb33) ;
        jpb.add(jpb34) ;
        jpb.add(jpb35) ;
        jpb.add(jpb36) ;
        jpb.add(jpb37) ;
        jpb.add(jpb38) ;
        jpb.add(jpb39) ;
        jpb.add(jpb40) ;
        jpb.add(jpb41) ;
        jpb.add(jpb42) ;
        jpb.add(jpb43) ;
        jpb.add(jpb44) ;
        jpb.add(jpb45) ;
        jpb.add(jpb46) ;
        jpb.add(jpb47) ;
        jpb.add(jpb48) ;
        jpb.add(jpb49) ;
        jpb.add(jpb50) ;
        jpb.add(jpb51) ;
        jpb.add(jpb52) ;
        jpb.add(jpb53) ;
        jpb.add(jpb54) ;
        jpb.add(jpb55) ;
        jpb.add(jpb56) ;
        jpb.add(jpb57) ;
        jpb.add(jpb58) ;
        jpb.add(jpb59) ;
        jpb.add(jpb60) ;
        jpb.add(jpb61) ;
        jpb.add(jpb62) ;
        jpb.add(jpb63) ;
        jpb.add(jpb64) ;
        jpb.add(jpb65) ;
        jpb.add(jpb66) ;
        jpb.add(jpb67) ;
        jpb.add(jpb68) ;
        jpb.add(jpb69) ;
        jpb.add(jpb70) ;
        jpb.add(jpb71) ;
        jpb.add(jpb72) ;
        jpb.add(jpb73) ;
        jpb.add(jpb74) ;
        jpb.add(jpb75) ;
        jpb.add(jpb76) ;
        jpb.add(jpb77) ;
        jpb.add(jpb78) ;
        jpb.add(jpb79) ;
        jpb.add(jpb80) ;
        jpb.add(jpb81) ;
        jpb.add(jpb82) ;
        jpb.add(jpb83) ;
        jpb.add(jpb84) ;
        jpb.add(jpb85) ;
        jpb.add(jpb86) ;
        jpb.add(jpb87) ;
        jpb.add(jpb88) ;
        jpb.add(jpb89) ;
        jpb.add(jpb90) ;
        jpb.add(jpb91) ;
        jpb.add(jpb92) ;
        jpb.add(jpb93) ;
        jpb.add(jpb94) ;
        jpb.add(jpb95) ;
        jpb.add(jpb96) ;
        jpb.add(jpb97) ;
        jpb.add(jpb98) ;
        jpb.add(jpb99) ;
        jpb.add(jpb100) ;
        // adding progress bar to io queue

        jpbIO.add(jpb101) ;
        jpbIO.add(jpb102) ;
        jpbIO.add(jpb103) ;
        jpbIO.add(jpb104) ;
        jpbIO.add(jpb105) ;
        jpbIO.add(jpb106) ;
        jpbIO.add(jpb107) ;
        jpbIO.add(jpb108) ;
        jpbIO.add(jpb109) ;
        jpbIO.add(jpb110) ;
        jpbIO.add(jpb111) ;
        jpbIO.add(jpb112) ;
        jpbIO.add(jpb113) ;
        jpbIO.add(jpb114) ;
        jpbIO.add(jpb115) ;
        jpbIO.add(jpb116) ;
        jpbIO.add(jpb117) ;
        jpbIO.add(jpb118) ;
        jpbIO.add(jpb119) ;
        jpbIO.add(jpb120) ;
        jpbIO.add(jpb121) ;
        jpbIO.add(jpb122) ;
        jpbIO.add(jpb123) ;
        jpbIO.add(jpb124) ;
        jpbIO.add(jpb125) ;
        jpbIO.add(jpb126) ;
        jpbIO.add(jpb127) ;
        jpbIO.add(jpb128) ;
        jpbIO.add(jpb129) ;
        jpbIO.add(jpb130) ;
        jpbIO.add(jpb131) ;
        jpbIO.add(jpb132) ;
        jpbIO.add(jpb133) ;
        jpbIO.add(jpb134) ;
        jpbIO.add(jpb135) ;
        jpbIO.add(jpb136) ;
        jpbIO.add(jpb137) ;
        jpbIO.add(jpb138) ;
        jpbIO.add(jpb139) ;
        jpbIO.add(jpb140) ;
        jpbIO.add(jpb141) ;
        jpbIO.add(jpb142) ;
        jpbIO.add(jpb143) ;
        jpbIO.add(jpb144) ;
        jpbIO.add(jpb145) ;
        jpbIO.add(jpb146) ;
        jpbIO.add(jpb147) ;
        jpbIO.add(jpb148) ;
        jpbIO.add(jpb149) ;
        jpbIO.add(jpb150) ;
        jpbIO.add(jpb151) ;
        jpbIO.add(jpb152) ;
        jpbIO.add(jpb153) ;
        jpbIO.add(jpb154) ;
        jpbIO.add(jpb155) ;
        jpbIO.add(jpb156) ;
        jpbIO.add(jpb157) ;
        jpbIO.add(jpb158) ;
        jpbIO.add(jpb159) ;
        jpbIO.add(jpb160) ;
        jpbIO.add(jpb161) ;
        jpbIO.add(jpb162) ;
        jpbIO.add(jpb163) ;
        jpbIO.add(jpb164) ;
        jpbIO.add(jpb165) ;
        jpbIO.add(jpb166) ;
        jpbIO.add(jpb167) ;
        jpbIO.add(jpb168) ;
        jpbIO.add(jpb169) ;
        jpbIO.add(jpb170) ;
        jpbIO.add(jpb171) ;
        jpbIO.add(jpb172) ;
        jpbIO.add(jpb173) ;
        jpbIO.add(jpb174) ;
        jpbIO.add(jpb175) ;
        jpbIO.add(jpb176) ;
        jpbIO.add(jpb177) ;
        jpbIO.add(jpb178) ;
        jpbIO.add(jpb179) ;
        jpbIO.add(jpb180) ;
        jpbIO.add(jpb181) ;
        jpbIO.add(jpb182) ;
        jpbIO.add(jpb183) ;
        jpbIO.add(jpb184) ;
        jpbIO.add(jpb185) ;
        jpbIO.add(jpb186) ;
        jpbIO.add(jpb187) ;
        jpbIO.add(jpb188) ;
        jpbIO.add(jpb189) ;
        jpbIO.add(jpb190) ;
        jpbIO.add(jpb191) ;
        jpbIO.add(jpb192) ;
        jpbIO.add(jpb193) ;
        jpbIO.add(jpb194) ;
        jpbIO.add(jpb195) ;
        jpbIO.add(jpb196) ;
        jpbIO.add(jpb197) ;
        jpbIO.add(jpb198) ;
        jpbIO.add(jpb199) ;
        jpbIO.add(jpb200) ;

        // for NON IO process...
        if(!iotoggle.isSelected()){
            //  Taking values form table to arraylists for easier access
            for (int tb =0;tb<totalprocess;tb++){
                if(algoselector.getSelectedItem().toString()=="Priority" || algoselector.getSelectedItem().toString()=="Pre-Priority"){
                    PT.add( Integer.parseInt( (String) jtb1.getValueAt(tb,0)) );
                    PrioArr.add( Integer.parseInt( (String) jtb1.getValueAt(tb,1)) );
                    AT.add( Integer.parseInt( (String) jtb1.getValueAt(tb,2)) );
                    BT1.add( Integer.parseInt( (String) jtb1.getValueAt(tb,3)) );
                }
                else{
                    PT.add( Integer.parseInt( (String) jtb1.getValueAt(tb,0)) );

                    AT.add( Integer.parseInt( (String) jtb1.getValueAt(tb,1)) );
                    BT1.add( Integer.parseInt( (String) jtb1.getValueAt(tb,2)) );
                }
            }
            // From arraylists values are loaded in list of process to go under excecution

            for(int t1=0;t1<totalprocess;t1++){
                if(algoselector.getSelectedItem().toString()=="Priority" || algoselector.getSelectedItem().toString()=="Pre-Priority"){
                    A.add(new Process(PT.get(t1),PrioArr.get(t1),AT.get(t1),BT1.get(t1),0,0));
                }
                // 0,0 at end as its non io process which dosen't require io and bt2
                else
                A.add(new Process(PT.get(t1),0,AT.get(t1),BT1.get(t1),0,0));
            }

            // performing selected algorithm
            if(algoselector.getSelectedItem().toString()=="FCFS" || algoselector.getSelectedItem().toString()=="")noIonoPre("FCFS");
            else if(algoselector.getSelectedItem().toString()=="SJF")noIonoPre("SJF");
            else if(algoselector.getSelectedItem().toString()=="LJF")noIonoPre("LJF");
            else if(algoselector.getSelectedItem().toString()=="Priority")noIonoPre("Priority");
            else if(algoselector.getSelectedItem().toString()=="RR")noIoPre(Integer.parseInt(tqbox.getText()),"RR");
            else if(algoselector.getSelectedItem().toString()=="SRTF")noIoPre(Integer.parseInt(tqbox.getText()),"SRTF");
            else if(algoselector.getSelectedItem().toString()=="LRTF")noIoPre(Integer.parseInt(tqbox.getText()),"LRTF");
            else if(algoselector.getSelectedItem().toString()=="Pre-Priority")noIoPre(Integer.parseInt(tqbox.getText()),"Pre-Priority");
            // Total time for which animation wud work
            int totaltimereq = A.get(totalprocess-1).CT ;
            System.out.println(totaltimereq+" <- tottime");
            // Starting new thread for animation
            Thread p1 = new Thread(){

                public void run(){

                    // main animation for loop
                    for( dispclck=0;dispclck<totaltimereq + 1;dispclck++){
                        synchronized(this){

                            clock.setText("  "+Integer.toString(dispclck));

                            if(dispclck >= display.get(dispcounter).startexec.get(display.get(dispcounter).execcounter)){
                                int startv = display.get(dispcounter).startexec.get(display.get(dispcounter).execcounter);
                                int endv = display.get(dispcounter).endexec.get(display.get(dispcounter).execcounter);
                                jpb.get(dispcounter).setMaximum(endv - startv+1);
                                jpb.get(dispcounter).setStringPainted(true);
                                jpb.get(dispcounter).setString("P"+display.get(dispcounter).PID);
                                jpb.get(dispcounter).setVisible(true);
                                jpb.get(dispcounter).setValue(jpb.get(dispcounter).getValue()+1);
                            }
                            if(dispclck == display.get(dispcounter).endexec.get(display.get(dispcounter).execcounter)){
                                System.out.println("P"+display.get(dispcounter).PID+display.get(dispcounter).execcounter);
                                // increasing execcounter
                                if(display.get(dispcounter).execcounter < display.get(dispcounter).startexec.size() -1) display.get(dispcounter).execcounter++;
                                System.out.println(dispcounter);
                                if(dispcounter<display.size() -1)dispcounter++;

                                if(dispclck >= display.get(dispcounter).startexec.get(display.get(dispcounter).execcounter)){
                                    int startv = display.get(dispcounter).startexec.get(display.get(dispcounter).execcounter);
                                    int endv = display.get(dispcounter).endexec.get(display.get(dispcounter).execcounter);
                                    jpb.get(dispcounter).setMaximum(endv - startv+1);
                                    jpb.get(dispcounter).setStringPainted(true);
                                    jpb.get(dispcounter).setString("P"+display.get(dispcounter).PID);
                                    jpb.get(dispcounter).setVisible(true);
                                    jpb.get(dispcounter).setValue(jpb.get(dispcounter).getValue()+1);
                                }
                            }
                            if(dispclck==A.get(dispanticounter).CT){

                                if(algoselector.getSelectedItem().toString()=="Priority" || algoselector.getSelectedItem().toString()=="Pre-Priority"){
                                    // 5th column indexed 4 for CT
                                    jtb1.setValueAt(A.get(dispanticounter).CT,A.get(dispanticounter).PID ,4);
                                    // 6th column indexed 5 for TAT
                                    jtb1.setValueAt(A.get(dispanticounter).CT - A.get(dispanticounter).AT,A.get(dispanticounter).PID ,5);
                                    // 7th column indexed 6 for WT
                                    jtb1.setValueAt(A.get(dispanticounter).CT - A.get(dispanticounter).AT - A.get(dispanticounter).BT1,A.get(dispanticounter).PID ,6);
                                    if(dispanticounter<A.size() -1)dispanticounter++;
                                }
                                else{
                                    // 5th column indexed 4 for CT
                                    jtb1.setValueAt(A.get(dispanticounter).CT,A.get(dispanticounter).PID ,3);
                                    // 6th column indexed 5 for TAT
                                    jtb1.setValueAt(A.get(dispanticounter).CT - A.get(dispanticounter).AT,A.get(dispanticounter).PID ,4);
                                    // 7th column indexed 6 for WT
                                    jtb1.setValueAt(A.get(dispanticounter).CT - A.get(dispanticounter).AT - A.get(dispanticounter).BT1,A.get(dispanticounter).PID ,5);
                                    if(dispanticounter<A.size() -1)dispanticounter++;
                                }

                            }

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ex) {

                            }

                        }
                    }
                    // context switches
                    int cs = display.size() ;
                    // variable for average tat and average wt and efficiency
                    double avgtat=0,avgwt=0,effic=0,cschart=0,stallchart=0;
                    //datasets
                    DefaultPieDataset tatchartdata = new DefaultPieDataset( );
                    DefaultPieDataset wtchartdata = new DefaultPieDataset( );
                    DefaultPieDataset effchartdata = new DefaultPieDataset( );
                    IntervalCategoryDataset dataset = getCategoryDataset(A);
                    // filling all data
                    for(int jtb2idx=0;jtb2idx<totalprocess;jtb2idx++){
                        int tat,wt ;
                        if(algoselector.getSelectedItem().toString()=="Priority" || algoselector.getSelectedItem().toString()=="Pre-Priority"){
                            tat = Integer.parseInt(jtb1.getValueAt(jtb2idx,4).toString())-Integer.parseInt(jtb1.getValueAt(jtb2idx,2).toString());
                            wt = tat-Integer.parseInt(jtb1.getValueAt(jtb2idx,3).toString()) ;
                        }
                        else{
                            tat = Integer.parseInt(jtb1.getValueAt(jtb2idx,3).toString())-Integer.parseInt(jtb1.getValueAt(jtb2idx,1).toString());
                            wt = tat-Integer.parseInt(jtb1.getValueAt(jtb2idx,2).toString()) ;
                        }
                        avgtat += tat;
                        avgwt += wt;
                        tatchartdata.setValue("P"+jtb2idx, tat);
                        wtchartdata.setValue("P"+jtb2idx, wt);
                    }
                    avgtat/=totalprocess;
                    avgwt/=totalprocess;
                    
                    cschart = cs ;
                    cschart /= totaltimereq ;
                    cschart *= 100;
                    
                    stallchart = stallpulse ;
                    stallchart /= totaltimereq ;
                    stallchart *= 100 ;
                    
                    System.out.println("cs: "+cs+" stallpulse: "+stallpulse+" tottime: "+totaltimereq+"cschart: "+cschart);
                    double temp = cs+stallpulse;
                    temp /= totaltimereq ;
                    effic = 1-temp ;
                    effic *=100 ;
                    System.out.println("effieciency should be: "+Double.toString(effic));
                    // calculating efficiency by formula
                    //effic = (1-(cs/(totaltimereq))-(stallpulse/(totaltimereq))) ;
                    // filling forr efficiency data piechart
                    effchartdata.setValue("Context switching",cschart);
                    effchartdata.setValue("Stall-Pulse",stallchart);
                    effchartdata.setValue("efficiency",effic);

                    //setting avg tat,wt labels
                    System.out.println(Double.toString(avgtat)+" "+Double.toString(avgwt) );
                    tatis.setText("Average TAT: "+Double.toString(avgtat));
                    wtis.setText("Average WT: "+Double.toString(avgwt));

                    effis.setText("<html>"+"Context Switches: "+cs+"<br>"+"Stall-Pulse: "+stallpulse+"<br>"+"Efficiency: "+Double.toString(effic));
                    // setting chart
                    JFreeChart tatchart = ChartFactory.createPieChart("TAT",tatchartdata,true,true,false) ;
                    JFreeChart wtchart = ChartFactory.createPieChart("WT",wtchartdata,true,true,false) ;
                    JFreeChart effchart = ChartFactory.createPieChart("Efficiency",effchartdata,true,true,false) ;
                    // Create gant chart
                    JFreeChart gantchart = ChartFactory.createGanttChart(
                        "Gantt Chart", // Chart title
                        "Process", // X-Axis Label
                        "Timeline", // Y-Axis Label
                        dataset);
                    // For axis of gant chart
                    CategoryPlot plot = gantchart.getCategoryPlot();
                    DateAxis axis = (DateAxis) plot.getRangeAxis();
                    axis.setDateFormatOverride(new SimpleDateFormat("SSS"));
                    System.out.println("tottal time req: "+totaltimereq);
                    axis.setMaximumDate(new Date(totaltimereq));
                    //making image of chart
                    File tatfile = new File("Images/TATchart"+filecounter+".png" );
                    File wtfile = new File("Images/WTchart"+filecounter+".png" );
                    File efffile = new File("Images/EFFchart"+filecounter+".png" );
                    File gantfile = new File("Images/gantchart"+filecounter+".png" );
                    //saving chart image
                    try{ChartUtilities.saveChartAsPNG(tatfile, tatchart, 400, 400);}catch(Exception e){}
                    try{ChartUtilities.saveChartAsPNG(wtfile, wtchart, 400, 400);}catch(Exception e){}
                    try{ChartUtilities.saveChartAsPNG(efffile, effchart, 400, 400);}catch(Exception e){}
                    try{ChartUtilities.saveChartAsPNG(gantfile, gantchart, 1500, 950);}catch(Exception e){}
                    //setting labels as images
                    tatchartlabel.setIcon(new ImageIcon("Images/TATchart"+filecounter+".png"));
                    wtchartlabel.setIcon(new ImageIcon("Images/WTchart"+filecounter+".png"));
                    effchartlabel.setIcon(new ImageIcon("Images/EFFchart"+filecounter+".png"));
                    gantchartlabel.setIcon(new ImageIcon("Images/gantchart"+filecounter+".png"));
                }
            };
            p1.start();
        }
        else{
            //  Taking values form table to arraylists for easier access
            for (int tb =0;tb<totalprocess;tb++){
                if(algoselector.getSelectedItem().toString()=="Priority" || algoselector.getSelectedItem().toString()=="Pre-Priority"){
                    PT.add( Integer.parseInt( (String) jtb1.getValueAt(tb,0)) );
                    PrioArr.add( Integer.parseInt( (String) jtb1.getValueAt(tb,1)) );
                    AT.add( Integer.parseInt( (String) jtb1.getValueAt(tb,2)) );
                    BT1.add( Integer.parseInt( (String) jtb1.getValueAt(tb,3)) );
                    IO.add( Integer.parseInt( (String) jtb1.getValueAt(tb,4)) );
                    BT2.add( Integer.parseInt( (String) jtb1.getValueAt(tb,5)) );
                }
                else{
                    PT.add( Integer.parseInt( (String) jtb1.getValueAt(tb,0)) );
                    AT.add( Integer.parseInt( (String) jtb1.getValueAt(tb,1)) );
                    BT1.add( Integer.parseInt( (String) jtb1.getValueAt(tb,2)) );
                    IO.add( Integer.parseInt( (String) jtb1.getValueAt(tb,3)) );
                    BT2.add( Integer.parseInt( (String) jtb1.getValueAt(tb,4)) );
                }
            }
            // From arraylists values are loaded in list of process to go under excecution

            for(int t1=0;t1<totalprocess;t1++){
                if(algoselector.getSelectedItem().toString()=="Priority"||algoselector.getSelectedItem().toString()=="Pre-Priority")
                A.add(new Process(PT.get(t1),PrioArr.get(t1),AT.get(t1),BT1.get(t1),IO.get(t1),BT2.get(t1)));
                else
                A.add(new Process(PT.get(t1),0,AT.get(t1),BT1.get(t1),IO.get(t1),BT2.get(t1)));
            }

            // performing selected IO algorithm
            if(algoselector.getSelectedItem().toString()=="FCFS" || algoselector.getSelectedItem().toString()=="")ioNoPre("FCFS");
            else if(algoselector.getSelectedItem().toString()=="SJF")ioNoPre("SJF");
            else if(algoselector.getSelectedItem().toString()=="LJF")ioNoPre("LJF");
            else if(algoselector.getSelectedItem().toString()=="Priority")ioNoPre("Priority");
            else if(algoselector.getSelectedItem().toString()=="RR")ioPre(Integer.parseInt(tqbox.getText()),"RR");
            else if(algoselector.getSelectedItem().toString()=="SRTF")ioPre(Integer.parseInt(tqbox.getText()),"SRTF");
            else if(algoselector.getSelectedItem().toString()=="LRTF")ioPre(Integer.parseInt(tqbox.getText()),"LRTF");
            else if(algoselector.getSelectedItem().toString()=="Pre-Priority")ioPre(Integer.parseInt(tqbox.getText()),"Pre-Priority");

            // Total time for which animation wud work
            int totaltimereq = A.get(totalprocess-1).CT ;
            System.out.println(totaltimereq+" <- tottime");
            // Starting new thread for animation
            Thread p1 = new Thread(){

                public void run(){

                    // main animation for loop
                    for( dispclck=0;dispclck<totaltimereq + 1;dispclck++){
                        synchronized(this){

                            clock.setText("  "+Integer.toString(dispclck));

                            if(dispclck >= display.get(dispcounter).startexec.get(display.get(dispcounter).execcounter)){
                                int startv = display.get(dispcounter).startexec.get(display.get(dispcounter).execcounter);
                                int endv = display.get(dispcounter).endexec.get(display.get(dispcounter).execcounter);
                                jpb.get(dispcounter).setMaximum(endv - startv+1);
                                jpb.get(dispcounter).setStringPainted(true);
                                jpb.get(dispcounter).setString("P"+display.get(dispcounter).PID);
                                jpb.get(dispcounter).setVisible(true);
                                jpb.get(dispcounter).setValue(jpb.get(dispcounter).getValue()+1);
                            }
                            if(dispclck == display.get(dispcounter).endexec.get(display.get(dispcounter).execcounter)){
                                System.out.println("P"+display.get(dispcounter).PID+display.get(dispcounter).execcounter);
                                // increasing execcounter
                                if(display.get(dispcounter).execcounter < display.get(dispcounter).startexec.size() -1) display.get(dispcounter).execcounter++;
                                System.out.println(dispcounter);
                                if(dispcounter<display.size() -1)dispcounter++;

                                if(dispclck >= display.get(dispcounter).startexec.get(display.get(dispcounter).execcounter)){
                                    int startv = display.get(dispcounter).startexec.get(display.get(dispcounter).execcounter);
                                    int endv = display.get(dispcounter).endexec.get(display.get(dispcounter).execcounter);
                                    jpb.get(dispcounter).setMaximum(endv - startv+1);
                                    jpb.get(dispcounter).setStringPainted(true);
                                    jpb.get(dispcounter).setString("P"+display.get(dispcounter).PID);
                                    jpb.get(dispcounter).setVisible(true);
                                    jpb.get(dispcounter).setValue(jpb.get(dispcounter).getValue()+1);
                                }
                            }
                            if(dispclck==A.get(dispanticounter).CT){
                                if(algoselector.getSelectedItem().toString()=="Priority" || algoselector.getSelectedItem().toString()=="Pre-Priority"){
                                    // 7th column indexed 6 for CT
                                    jtb1.setValueAt(A.get(dispanticounter).CT,A.get(dispanticounter).PID ,6);
                                    // 8th column indexed 7 for TAT
                                    jtb1.setValueAt(A.get(dispanticounter).CT - A.get(dispanticounter).AT,A.get(dispanticounter).PID ,7);
                                    // 9th column indexed 8 for WT
                                    jtb1.setValueAt(A.get(dispanticounter).CT - A.get(dispanticounter).AT - A.get(dispanticounter).BT1 - A.get(dispanticounter).BT2,A.get(dispanticounter).PID ,8);
                                    if(dispanticounter<A.size() -1)dispanticounter++;
                                }
                                else{
                                    // 7th column indexed 6 for CT
                                    jtb1.setValueAt(A.get(dispanticounter).CT,A.get(dispanticounter).PID ,5);
                                    // 8th column indexed 7 for TAT
                                    jtb1.setValueAt(A.get(dispanticounter).CT - A.get(dispanticounter).AT,A.get(dispanticounter).PID ,6);
                                    // 9th column indexed 8 for WT
                                    jtb1.setValueAt(A.get(dispanticounter).CT - A.get(dispanticounter).AT - A.get(dispanticounter).BT1 - A.get(dispanticounter).BT2,A.get(dispanticounter).PID ,7);
                                    if(dispanticounter<A.size() -1)dispanticounter++;
                                }
                            }
                            // IO queue animation:

                            if(IOdisplay.size()>=1){
                                if(dispclck == IOdisplay.get(0).firstCT){
                                jpbIO.get(0).setMaximum(IOdisplay.get(0).Notify);
                                jpbIO.get(0).setStringPainted(true);
                                jpbIO.get(0).setString("P"+IOdisplay.get(0).PID);
                                jpbIO.get(0).setVisible(true);
                                }
                                jpbIO.get(0).setValue(jpbIO.get(0).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=2){
                                if(dispclck == IOdisplay.get(1).firstCT){
                                jpbIO.get(1).setMaximum(IOdisplay.get(1).Notify);
                                jpbIO.get(1).setStringPainted(true);
                                jpbIO.get(1).setString("P"+IOdisplay.get(1).PID);
                                jpbIO.get(1).setVisible(true);
                                }
                                jpbIO.get(1).setValue(jpbIO.get(1).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=3){
                                if(dispclck == IOdisplay.get(2).firstCT){
                                jpbIO.get(2).setMaximum(IOdisplay.get(2).Notify);
                                jpbIO.get(2).setStringPainted(true);
                                jpbIO.get(2).setString("P"+IOdisplay.get(2).PID);
                                jpbIO.get(2).setVisible(true);
                                }
                                jpbIO.get(2).setValue(jpbIO.get(2).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=4){
                                if(dispclck == IOdisplay.get(3).firstCT){
                                jpbIO.get(3).setMaximum(IOdisplay.get(3).Notify);
                                jpbIO.get(3).setStringPainted(true);
                                jpbIO.get(3).setString("P"+IOdisplay.get(3).PID);
                                jpbIO.get(3).setVisible(true);
                                }
                                jpbIO.get(3).setValue(jpbIO.get(3).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=5){
                                if(dispclck == IOdisplay.get(4).firstCT){
                                jpbIO.get(4).setMaximum(IOdisplay.get(4).Notify);
                                jpbIO.get(4).setStringPainted(true);
                                jpbIO.get(4).setString("P"+IOdisplay.get(4).PID);
                                jpbIO.get(4).setVisible(true);
                                }
                                jpbIO.get(4).setValue(jpbIO.get(4).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=6){
                                if(dispclck == IOdisplay.get(5).firstCT){
                                jpbIO.get(5).setMaximum(IOdisplay.get(5).Notify);
                                jpbIO.get(5).setStringPainted(true);
                                jpbIO.get(5).setString("P"+IOdisplay.get(5).PID);
                                jpbIO.get(5).setVisible(true);
                                }
                                jpbIO.get(5).setValue(jpbIO.get(5).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=7){
                                if(dispclck == IOdisplay.get(6).firstCT){
                                jpbIO.get(6).setMaximum(IOdisplay.get(6).Notify);
                                jpbIO.get(6).setStringPainted(true);
                                jpbIO.get(6).setString("P"+IOdisplay.get(6).PID);
                                jpbIO.get(6).setVisible(true);
                                }
                                jpbIO.get(6).setValue(jpbIO.get(6).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=8){
                                if(dispclck == IOdisplay.get(7).firstCT){
                                jpbIO.get(7).setMaximum(IOdisplay.get(7).Notify);
                                jpbIO.get(7).setStringPainted(true);
                                jpbIO.get(7).setString("P"+IOdisplay.get(7).PID);
                                jpbIO.get(7).setVisible(true);
                                }
                                jpbIO.get(7).setValue(jpbIO.get(7).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=9){
                                if(dispclck == IOdisplay.get(8).firstCT){
                                jpbIO.get(8).setMaximum(IOdisplay.get(8).Notify);
                                jpbIO.get(8).setStringPainted(true);
                                jpbIO.get(8).setString("P"+IOdisplay.get(8).PID);
                                jpbIO.get(8).setVisible(true);
                                }
                                jpbIO.get(8).setValue(jpbIO.get(8).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=10){
                                if(dispclck == IOdisplay.get(9).firstCT){
                                jpbIO.get(9).setMaximum(IOdisplay.get(9).Notify);
                                jpbIO.get(9).setStringPainted(true);
                                jpbIO.get(9).setString("P"+IOdisplay.get(9).PID);
                                jpbIO.get(9).setVisible(true);
                                }
                                jpbIO.get(9).setValue(jpbIO.get(9).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=11){
                                if(dispclck == IOdisplay.get(10).firstCT){
                                jpbIO.get(10).setMaximum(IOdisplay.get(10).Notify);
                                jpbIO.get(10).setStringPainted(true);
                                jpbIO.get(10).setString("P"+IOdisplay.get(10).PID);
                                jpbIO.get(10).setVisible(true);
                                }
                                jpbIO.get(10).setValue(jpbIO.get(10).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=12){
                                if(dispclck == IOdisplay.get(11).firstCT){
                                jpbIO.get(11).setMaximum(IOdisplay.get(11).Notify);
                                jpbIO.get(11).setStringPainted(true);
                                jpbIO.get(11).setString("P"+IOdisplay.get(11).PID);
                                jpbIO.get(11).setVisible(true);
                                }
                                jpbIO.get(11).setValue(jpbIO.get(11).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=13){
                                if(dispclck == IOdisplay.get(12).firstCT){
                                jpbIO.get(12).setMaximum(IOdisplay.get(12).Notify);
                                jpbIO.get(12).setStringPainted(true);
                                jpbIO.get(12).setString("P"+IOdisplay.get(12).PID);
                                jpbIO.get(12).setVisible(true);
                                }
                                jpbIO.get(12).setValue(jpbIO.get(12).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=14){
                                if(dispclck == IOdisplay.get(13).firstCT){
                                jpbIO.get(13).setMaximum(IOdisplay.get(13).Notify);
                                jpbIO.get(13).setStringPainted(true);
                                jpbIO.get(13).setString("P"+IOdisplay.get(13).PID);
                                jpbIO.get(13).setVisible(true);
                                }
                                jpbIO.get(13).setValue(jpbIO.get(13).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=15){
                                if(dispclck == IOdisplay.get(14).firstCT){
                                jpbIO.get(14).setMaximum(IOdisplay.get(14).Notify);
                                jpbIO.get(14).setStringPainted(true);
                                jpbIO.get(14).setString("P"+IOdisplay.get(14).PID);
                                jpbIO.get(14).setVisible(true);
                                }
                                jpbIO.get(14).setValue(jpbIO.get(14).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=16){
                                if(dispclck == IOdisplay.get(15).firstCT){
                                jpbIO.get(15).setMaximum(IOdisplay.get(15).Notify);
                                jpbIO.get(15).setStringPainted(true);
                                jpbIO.get(15).setString("P"+IOdisplay.get(15).PID);
                                jpbIO.get(15).setVisible(true);
                                }
                                jpbIO.get(15).setValue(jpbIO.get(15).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=17){
                                if(dispclck == IOdisplay.get(16).firstCT){
                                jpbIO.get(16).setMaximum(IOdisplay.get(16).Notify);
                                jpbIO.get(16).setStringPainted(true);
                                jpbIO.get(16).setString("P"+IOdisplay.get(16).PID);
                                jpbIO.get(16).setVisible(true);
                                }
                                jpbIO.get(16).setValue(jpbIO.get(16).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=18){
                                if(dispclck == IOdisplay.get(17).firstCT){
                                jpbIO.get(17).setMaximum(IOdisplay.get(17).Notify);
                                jpbIO.get(17).setStringPainted(true);
                                jpbIO.get(17).setString("P"+IOdisplay.get(17).PID);
                                jpbIO.get(17).setVisible(true);
                                }
                                jpbIO.get(17).setValue(jpbIO.get(17).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=19){
                                if(dispclck == IOdisplay.get(18).firstCT){
                                jpbIO.get(18).setMaximum(IOdisplay.get(18).Notify);
                                jpbIO.get(18).setStringPainted(true);
                                jpbIO.get(18).setString("P"+IOdisplay.get(18).PID);
                                jpbIO.get(18).setVisible(true);
                                }
                                jpbIO.get(18).setValue(jpbIO.get(18).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=20){
                                if(dispclck == IOdisplay.get(19).firstCT){
                                jpbIO.get(19).setMaximum(IOdisplay.get(19).Notify);
                                jpbIO.get(19).setStringPainted(true);
                                jpbIO.get(19).setString("P"+IOdisplay.get(19).PID);
                                jpbIO.get(19).setVisible(true);
                                }
                                jpbIO.get(19).setValue(jpbIO.get(19).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=21){
                                if(dispclck == IOdisplay.get(20).firstCT){
                                jpbIO.get(20).setMaximum(IOdisplay.get(20).Notify);
                                jpbIO.get(20).setStringPainted(true);
                                jpbIO.get(20).setString("P"+IOdisplay.get(20).PID);
                                jpbIO.get(20).setVisible(true);
                                }
                                jpbIO.get(20).setValue(jpbIO.get(20).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=22){
                                if(dispclck == IOdisplay.get(21).firstCT){
                                jpbIO.get(21).setMaximum(IOdisplay.get(21).Notify);
                                jpbIO.get(21).setStringPainted(true);
                                jpbIO.get(21).setString("P"+IOdisplay.get(21).PID);
                                jpbIO.get(21).setVisible(true);
                                }
                                jpbIO.get(21).setValue(jpbIO.get(21).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=23){
                                if(dispclck == IOdisplay.get(22).firstCT){
                                jpbIO.get(22).setMaximum(IOdisplay.get(22).Notify);
                                jpbIO.get(22).setStringPainted(true);
                                jpbIO.get(22).setString("P"+IOdisplay.get(22).PID);
                                jpbIO.get(22).setVisible(true);
                                }
                                jpbIO.get(22).setValue(jpbIO.get(22).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=24){
                                if(dispclck == IOdisplay.get(23).firstCT){
                                jpbIO.get(23).setMaximum(IOdisplay.get(23).Notify);
                                jpbIO.get(23).setStringPainted(true);
                                jpbIO.get(23).setString("P"+IOdisplay.get(23).PID);
                                jpbIO.get(23).setVisible(true);
                                }
                                jpbIO.get(23).setValue(jpbIO.get(23).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=25){
                                if(dispclck == IOdisplay.get(24).firstCT){
                                jpbIO.get(24).setMaximum(IOdisplay.get(24).Notify);
                                jpbIO.get(24).setStringPainted(true);
                                jpbIO.get(24).setString("P"+IOdisplay.get(24).PID);
                                jpbIO.get(24).setVisible(true);
                                }
                                jpbIO.get(24).setValue(jpbIO.get(24).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=26){
                                if(dispclck == IOdisplay.get(25).firstCT){
                                jpbIO.get(25).setMaximum(IOdisplay.get(25).Notify);
                                jpbIO.get(25).setStringPainted(true);
                                jpbIO.get(25).setString("P"+IOdisplay.get(25).PID);
                                jpbIO.get(25).setVisible(true);
                                }
                                jpbIO.get(25).setValue(jpbIO.get(25).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=27){
                                if(dispclck == IOdisplay.get(26).firstCT){
                                jpbIO.get(26).setMaximum(IOdisplay.get(26).Notify);
                                jpbIO.get(26).setStringPainted(true);
                                jpbIO.get(26).setString("P"+IOdisplay.get(26).PID);
                                jpbIO.get(26).setVisible(true);
                                }
                                jpbIO.get(26).setValue(jpbIO.get(26).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=28){
                                if(dispclck == IOdisplay.get(27).firstCT){
                                jpbIO.get(27).setMaximum(IOdisplay.get(27).Notify);
                                jpbIO.get(27).setStringPainted(true);
                                jpbIO.get(27).setString("P"+IOdisplay.get(27).PID);
                                jpbIO.get(27).setVisible(true);
                                }
                                jpbIO.get(27).setValue(jpbIO.get(27).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=29){
                                if(dispclck == IOdisplay.get(28).firstCT){
                                jpbIO.get(28).setMaximum(IOdisplay.get(28).Notify);
                                jpbIO.get(28).setStringPainted(true);
                                jpbIO.get(28).setString("P"+IOdisplay.get(28).PID);
                                jpbIO.get(28).setVisible(true);
                                }
                                jpbIO.get(28).setValue(jpbIO.get(28).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=30){
                                if(dispclck == IOdisplay.get(29).firstCT){
                                jpbIO.get(29).setMaximum(IOdisplay.get(29).Notify);
                                jpbIO.get(29).setStringPainted(true);
                                jpbIO.get(29).setString("P"+IOdisplay.get(29).PID);
                                jpbIO.get(29).setVisible(true);
                                }
                                jpbIO.get(29).setValue(jpbIO.get(29).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=31){
                                if(dispclck == IOdisplay.get(30).firstCT){
                                jpbIO.get(30).setMaximum(IOdisplay.get(30).Notify);
                                jpbIO.get(30).setStringPainted(true);
                                jpbIO.get(30).setString("P"+IOdisplay.get(30).PID);
                                jpbIO.get(30).setVisible(true);
                                }
                                jpbIO.get(30).setValue(jpbIO.get(30).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=32){
                                if(dispclck == IOdisplay.get(31).firstCT){
                                jpbIO.get(31).setMaximum(IOdisplay.get(31).Notify);
                                jpbIO.get(31).setStringPainted(true);
                                jpbIO.get(31).setString("P"+IOdisplay.get(31).PID);
                                jpbIO.get(31).setVisible(true);
                                }
                                jpbIO.get(31).setValue(jpbIO.get(31).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=33){
                                if(dispclck == IOdisplay.get(32).firstCT){
                                jpbIO.get(32).setMaximum(IOdisplay.get(32).Notify);
                                jpbIO.get(32).setStringPainted(true);
                                jpbIO.get(32).setString("P"+IOdisplay.get(32).PID);
                                jpbIO.get(32).setVisible(true);
                                }
                                jpbIO.get(32).setValue(jpbIO.get(32).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=34){
                                if(dispclck == IOdisplay.get(33).firstCT){
                                jpbIO.get(33).setMaximum(IOdisplay.get(33).Notify);
                                jpbIO.get(33).setStringPainted(true);
                                jpbIO.get(33).setString("P"+IOdisplay.get(33).PID);
                                jpbIO.get(33).setVisible(true);
                                }
                                jpbIO.get(33).setValue(jpbIO.get(33).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=35){
                                if(dispclck == IOdisplay.get(34).firstCT){
                                jpbIO.get(34).setMaximum(IOdisplay.get(34).Notify);
                                jpbIO.get(34).setStringPainted(true);
                                jpbIO.get(34).setString("P"+IOdisplay.get(34).PID);
                                jpbIO.get(34).setVisible(true);
                                }
                                jpbIO.get(34).setValue(jpbIO.get(34).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=36){
                                if(dispclck == IOdisplay.get(35).firstCT){
                                jpbIO.get(35).setMaximum(IOdisplay.get(35).Notify);
                                jpbIO.get(35).setStringPainted(true);
                                jpbIO.get(35).setString("P"+IOdisplay.get(35).PID);
                                jpbIO.get(35).setVisible(true);
                                }
                                jpbIO.get(35).setValue(jpbIO.get(35).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=37){
                                if(dispclck == IOdisplay.get(36).firstCT){
                                jpbIO.get(36).setMaximum(IOdisplay.get(36).Notify);
                                jpbIO.get(36).setStringPainted(true);
                                jpbIO.get(36).setString("P"+IOdisplay.get(36).PID);
                                jpbIO.get(36).setVisible(true);
                                }
                                jpbIO.get(36).setValue(jpbIO.get(36).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=38){
                                if(dispclck == IOdisplay.get(37).firstCT){
                                jpbIO.get(37).setMaximum(IOdisplay.get(37).Notify);
                                jpbIO.get(37).setStringPainted(true);
                                jpbIO.get(37).setString("P"+IOdisplay.get(37).PID);
                                jpbIO.get(37).setVisible(true);
                                }
                                jpbIO.get(37).setValue(jpbIO.get(37).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=39){
                                if(dispclck == IOdisplay.get(38).firstCT){
                                jpbIO.get(38).setMaximum(IOdisplay.get(38).Notify);
                                jpbIO.get(38).setStringPainted(true);
                                jpbIO.get(38).setString("P"+IOdisplay.get(38).PID);
                                jpbIO.get(38).setVisible(true);
                                }
                                jpbIO.get(38).setValue(jpbIO.get(38).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=40){
                                if(dispclck == IOdisplay.get(39).firstCT){
                                jpbIO.get(39).setMaximum(IOdisplay.get(39).Notify);
                                jpbIO.get(39).setStringPainted(true);
                                jpbIO.get(39).setString("P"+IOdisplay.get(39).PID);
                                jpbIO.get(39).setVisible(true);
                                }
                                jpbIO.get(39).setValue(jpbIO.get(39).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=41){
                                if(dispclck == IOdisplay.get(40).firstCT){
                                jpbIO.get(40).setMaximum(IOdisplay.get(40).Notify);
                                jpbIO.get(40).setStringPainted(true);
                                jpbIO.get(40).setString("P"+IOdisplay.get(40).PID);
                                jpbIO.get(40).setVisible(true);
                                }
                                jpbIO.get(40).setValue(jpbIO.get(40).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=42){
                                if(dispclck == IOdisplay.get(41).firstCT){
                                jpbIO.get(41).setMaximum(IOdisplay.get(41).Notify);
                                jpbIO.get(41).setStringPainted(true);
                                jpbIO.get(41).setString("P"+IOdisplay.get(41).PID);
                                jpbIO.get(41).setVisible(true);
                                }
                                jpbIO.get(41).setValue(jpbIO.get(41).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=43){
                                if(dispclck == IOdisplay.get(42).firstCT){
                                jpbIO.get(42).setMaximum(IOdisplay.get(42).Notify);
                                jpbIO.get(42).setStringPainted(true);
                                jpbIO.get(42).setString("P"+IOdisplay.get(42).PID);
                                jpbIO.get(42).setVisible(true);
                                }
                                jpbIO.get(42).setValue(jpbIO.get(42).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=44){
                                if(dispclck == IOdisplay.get(43).firstCT){
                                jpbIO.get(43).setMaximum(IOdisplay.get(43).Notify);
                                jpbIO.get(43).setStringPainted(true);
                                jpbIO.get(43).setString("P"+IOdisplay.get(43).PID);
                                jpbIO.get(43).setVisible(true);
                                }
                                jpbIO.get(43).setValue(jpbIO.get(43).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=45){
                                if(dispclck == IOdisplay.get(44).firstCT){
                                jpbIO.get(44).setMaximum(IOdisplay.get(44).Notify);
                                jpbIO.get(44).setStringPainted(true);
                                jpbIO.get(44).setString("P"+IOdisplay.get(44).PID);
                                jpbIO.get(44).setVisible(true);
                                }
                                jpbIO.get(44).setValue(jpbIO.get(44).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=46){
                                if(dispclck == IOdisplay.get(45).firstCT){
                                jpbIO.get(45).setMaximum(IOdisplay.get(45).Notify);
                                jpbIO.get(45).setStringPainted(true);
                                jpbIO.get(45).setString("P"+IOdisplay.get(45).PID);
                                jpbIO.get(45).setVisible(true);
                                }
                                jpbIO.get(45).setValue(jpbIO.get(45).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=47){
                                if(dispclck == IOdisplay.get(46).firstCT){
                                jpbIO.get(46).setMaximum(IOdisplay.get(46).Notify);
                                jpbIO.get(46).setStringPainted(true);
                                jpbIO.get(46).setString("P"+IOdisplay.get(46).PID);
                                jpbIO.get(46).setVisible(true);
                                }
                                jpbIO.get(46).setValue(jpbIO.get(46).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=48){
                                if(dispclck == IOdisplay.get(47).firstCT){
                                jpbIO.get(47).setMaximum(IOdisplay.get(47).Notify);
                                jpbIO.get(47).setStringPainted(true);
                                jpbIO.get(47).setString("P"+IOdisplay.get(47).PID);
                                jpbIO.get(47).setVisible(true);
                                }
                                jpbIO.get(47).setValue(jpbIO.get(47).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=49){
                                if(dispclck == IOdisplay.get(48).firstCT){
                                jpbIO.get(48).setMaximum(IOdisplay.get(48).Notify);
                                jpbIO.get(48).setStringPainted(true);
                                jpbIO.get(48).setString("P"+IOdisplay.get(48).PID);
                                jpbIO.get(48).setVisible(true);
                                }
                                jpbIO.get(48).setValue(jpbIO.get(48).getValue()+1) ;
                        }
                        if(IOdisplay.size()>=50){
                                if(dispclck == IOdisplay.get(49).firstCT){
                                jpbIO.get(49).setMaximum(IOdisplay.get(49).Notify);
                                jpbIO.get(49).setStringPainted(true);
                                jpbIO.get(49).setString("P"+IOdisplay.get(49).PID);
                                jpbIO.get(49).setVisible(true);
                                }
                                jpbIO.get(49).setValue(jpbIO.get(49).getValue()+1) ;
                        }

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ex) {

                            }

                        }
                    }

                    // variable for context switches
                    int cs = display.size() ;

                    // variable for average tat and average wt
                    double avgtat=0,avgwt=0,effic=0,stallchart=0,cschart=0;
                    //datasets
                    DefaultPieDataset tatchartdata = new DefaultPieDataset( );
                    DefaultPieDataset wtchartdata = new DefaultPieDataset( );
                    DefaultPieDataset effchartdata = new DefaultPieDataset( );
                    IntervalCategoryDataset dataset = getCategoryDataset(A);
                    // filling all data
                    for(int jtb2idx=0;jtb2idx<totalprocess;jtb2idx++){
                        int tat,wt ;
                        if(algoselector.getSelectedItem().toString()=="Priority"||algoselector.getSelectedItem().toString()=="Pre-Priority"){
                            tat = Integer.parseInt(jtb1.getValueAt(jtb2idx,6).toString())-Integer.parseInt(jtb1.getValueAt(jtb2idx,2).toString());
                            wt = tat-Integer.parseInt(jtb1.getValueAt(jtb2idx,3).toString())-Integer.parseInt(jtb1.getValueAt(jtb2idx,5).toString())  ;
                        }
                        else{
                            tat = Integer.parseInt(jtb1.getValueAt(jtb2idx,5).toString())-Integer.parseInt(jtb1.getValueAt(jtb2idx,1).toString());
                            wt = tat-Integer.parseInt(jtb1.getValueAt(jtb2idx,2).toString())-Integer.parseInt(jtb1.getValueAt(jtb2idx,4).toString())  ;
                        }
                        avgtat += tat;
                        avgwt += wt;

                        tatchartdata.setValue("P"+jtb2idx, tat);
                        wtchartdata.setValue("P"+jtb2idx, wt);
                    }
                    avgtat/=totalprocess;
                    avgwt/=totalprocess;
                    // calculating efficiency by formula
                    System.out.println("cs: "+cs+" stallpulse: "+stallpulse+" tottime: "+totaltimereq);
                    cschart = cs ;
                    cschart /= totaltimereq ;
                    cschart *= 100;
                    
                    stallchart = stallpulse ;
                    stallchart /= totaltimereq ;
                    stallchart *= 100 ;
                    
                    System.out.println("cs: "+cs+" stallpulse: "+stallpulse+" tottime: "+totaltimereq+"cschart: "+cschart);
                    double temp = cs+stallpulse;
                    temp /= totaltimereq ;
                    effic = 1-temp ;
                    effic *=100 ;
                    System.out.println("effieciency should be: "+Double.toString(effic));
                    
                    // filling forr efficiency data piechart
                    effchartdata.setValue("Context switching",cschart);
                    effchartdata.setValue("Stall-Pulse",stallchart);
                    effchartdata.setValue("efficiency",effic);
                    //setting avg tat,wt labels
                    System.out.println(Double.toString(avgtat)+" "+Double.toString(avgwt) );
                    tatis.setText("Average TAT: "+Double.toString(avgtat));
                    wtis.setText("Average WT: "+Double.toString(avgwt));
                    effis.setText("<html>"+"Context Switches: "+cs+"<br>"+"Stall-Pulse: "+stallpulse+"<br>"+"Efficiency: "+Double.toString(effic));
                    // setting chart
                    JFreeChart tatchart = ChartFactory.createPieChart("TAT",tatchartdata,true,true,false) ;
                    JFreeChart wtchart = ChartFactory.createPieChart("WT",wtchartdata,true,true,false) ;
                    JFreeChart effchart = ChartFactory.createPieChart("Efficiency",effchartdata,true,true,false) ;
                    // Create gant chart
                    JFreeChart gantchart = ChartFactory.createGanttChart(
                        "Gantt Chart", // Chart title
                        "Process", // X-Axis Label
                        "Timeline", // Y-Axis Label
                        dataset);
                    // For axis of gant chart
                    CategoryPlot plot = gantchart.getCategoryPlot();
                    DateAxis axis = (DateAxis) plot.getRangeAxis();
                    axis.setDateFormatOverride(new SimpleDateFormat("SSS"));
                    System.out.println("totaltimereq: "+totaltimereq) ;
                    axis.setMaximumDate(new Date(totaltimereq));
                    //making image of chart
                    File tatfile = new File("Images/TATchart"+filecounter+".png" );
                    File wtfile = new File("Images/WTchart"+filecounter+".png" );
                    File efffile = new File("Images/EFFchart"+filecounter+".png" );
                    File gantfile = new File("Images/gantchart"+filecounter+".png" );
                    //saving chart image
                    try{ChartUtilities.saveChartAsPNG(tatfile, tatchart, 400, 400);}catch(Exception e){}
                    try{ChartUtilities.saveChartAsPNG(wtfile, wtchart, 400, 400);}catch(Exception e){}
                    try{ChartUtilities.saveChartAsPNG(efffile, effchart, 400, 400);}catch(Exception e){}
                    try{ChartUtilities.saveChartAsPNG(gantfile, gantchart, 1500, 950);}catch(Exception e){}
                    //setting labels as images
                    tatchartlabel.setIcon(new ImageIcon("Images/TATchart"+filecounter+".png"));
                    wtchartlabel.setIcon(new ImageIcon("Images/WTchart"+filecounter+".png"));
                    effchartlabel.setIcon(new ImageIcon("Images/EFFchart"+filecounter+".png"));
                    gantchartlabel.setIcon(new ImageIcon("Images/gantchart"+filecounter+".png"));
                }
            };
            p1.start();
        }
    }//GEN-LAST:event_StartActionPerformed

    private void ResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ResetActionPerformed
        // Resetting animations and variables for next process keeping table data as it is...

        // resetting variables
        dispclck=0; dispcounter=0; dispanticounter=0;stallpulse=0 ;

        //resetting queues
        A.clear();
        AT.clear();
        PrioArr.clear();
        BT1.clear();
        BT2.clear();
        IO.clear();
        PT.clear();
        queue.clear();
        display.clear();
        IOdisplay.clear();

        //reseting gui elements
        clock.setText("");
        // no need of restting numbox and tqbox

        // clearing animation
        for(JProgressBar jp: jpb){
            jp.setValue(0);
            jp.setVisible(false);
        }
        jpb.clear();

        for(JProgressBar jp: jpbIO){
            jp.setValue(0);
            jp.setVisible(false);
        }
        jpbIO.clear();

        // For graphs

        filecounter++;

        /* tatchartlabel.revalidate();
        tatchartlabel.repaint();
        wtchartlabel.revalidate();
        wtchartlabel.repaint();
        effchartlabel.revalidate();
        effchartlabel.repaint();
        gantchartlabel.revalidate();
        gantchartlabel.repaint();

        tatchartlabel.setIcon(null);
        wtchartlabel.setIcon(null);
        effchartlabel.setIcon(null);
        gantchartlabel.setIcon(null);*/
    }//GEN-LAST:event_ResetActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Scheduling_Algo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Scheduling_Algo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Scheduling_Algo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Scheduling_Algo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
                javax.swing.JFrame f = new Scheduling_Algo();
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                f.setVisible(true);
                /*f.setSize(screenSize.width, screenSize.height);*/
                f.setExtendedState( f.getExtendedState()|javax.swing.JFrame.MAXIMIZED_BOTH );
                System.out.print(screenSize.width+" "+ screenSize.height);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel CPU_Q_label;
    private javax.swing.JScrollPane CPU_Q_scrollpane;
    private javax.swing.JLabel IO_Q_label;
    private javax.swing.JScrollPane IO_Q_scrollpanel;
    private javax.swing.JScrollPane Main_scroll_panel;
    private javax.swing.JButton Randomize;
    private javax.swing.JButton Reset;
    private javax.swing.JButton Start;
    private javax.swing.JComboBox<String> algoselector;
    private javax.swing.JTextField clock;
    private javax.swing.JLabel clocklabel;
    private os_algo.CustomJProgressBar customJProgressBar1;
    private javax.swing.JLabel effchartlabel;
    private javax.swing.JLabel effis;
    private javax.swing.JLabel gantchartlabel;
    private javax.swing.JToggleButton iotoggle;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JProgressBar jpb1;
    private javax.swing.JProgressBar jpb10;
    private javax.swing.JProgressBar jpb100;
    private javax.swing.JProgressBar jpb101;
    private javax.swing.JProgressBar jpb102;
    private javax.swing.JProgressBar jpb103;
    private javax.swing.JProgressBar jpb104;
    private javax.swing.JProgressBar jpb105;
    private javax.swing.JProgressBar jpb106;
    private javax.swing.JProgressBar jpb107;
    private javax.swing.JProgressBar jpb108;
    private javax.swing.JProgressBar jpb109;
    private javax.swing.JProgressBar jpb11;
    private javax.swing.JProgressBar jpb110;
    private javax.swing.JProgressBar jpb111;
    private javax.swing.JProgressBar jpb112;
    private javax.swing.JProgressBar jpb113;
    private javax.swing.JProgressBar jpb114;
    private javax.swing.JProgressBar jpb115;
    private javax.swing.JProgressBar jpb116;
    private javax.swing.JProgressBar jpb117;
    private javax.swing.JProgressBar jpb118;
    private javax.swing.JProgressBar jpb119;
    private javax.swing.JProgressBar jpb12;
    private javax.swing.JProgressBar jpb120;
    private javax.swing.JProgressBar jpb121;
    private javax.swing.JProgressBar jpb122;
    private javax.swing.JProgressBar jpb123;
    private javax.swing.JProgressBar jpb124;
    private javax.swing.JProgressBar jpb125;
    private javax.swing.JProgressBar jpb126;
    private javax.swing.JProgressBar jpb127;
    private javax.swing.JProgressBar jpb128;
    private javax.swing.JProgressBar jpb129;
    private javax.swing.JProgressBar jpb13;
    private javax.swing.JProgressBar jpb130;
    private javax.swing.JProgressBar jpb131;
    private javax.swing.JProgressBar jpb132;
    private javax.swing.JProgressBar jpb133;
    private javax.swing.JProgressBar jpb134;
    private javax.swing.JProgressBar jpb135;
    private javax.swing.JProgressBar jpb136;
    private javax.swing.JProgressBar jpb137;
    private javax.swing.JProgressBar jpb138;
    private javax.swing.JProgressBar jpb139;
    private javax.swing.JProgressBar jpb14;
    private javax.swing.JProgressBar jpb140;
    private javax.swing.JProgressBar jpb141;
    private javax.swing.JProgressBar jpb142;
    private javax.swing.JProgressBar jpb143;
    private javax.swing.JProgressBar jpb144;
    private javax.swing.JProgressBar jpb145;
    private javax.swing.JProgressBar jpb146;
    private javax.swing.JProgressBar jpb147;
    private javax.swing.JProgressBar jpb148;
    private javax.swing.JProgressBar jpb149;
    private javax.swing.JProgressBar jpb15;
    private javax.swing.JProgressBar jpb150;
    private javax.swing.JProgressBar jpb151;
    private javax.swing.JProgressBar jpb152;
    private javax.swing.JProgressBar jpb153;
    private javax.swing.JProgressBar jpb154;
    private javax.swing.JProgressBar jpb155;
    private javax.swing.JProgressBar jpb156;
    private javax.swing.JProgressBar jpb157;
    private javax.swing.JProgressBar jpb158;
    private javax.swing.JProgressBar jpb159;
    private javax.swing.JProgressBar jpb16;
    private javax.swing.JProgressBar jpb160;
    private javax.swing.JProgressBar jpb161;
    private javax.swing.JProgressBar jpb162;
    private javax.swing.JProgressBar jpb163;
    private javax.swing.JProgressBar jpb164;
    private javax.swing.JProgressBar jpb165;
    private javax.swing.JProgressBar jpb166;
    private javax.swing.JProgressBar jpb167;
    private javax.swing.JProgressBar jpb168;
    private javax.swing.JProgressBar jpb169;
    private javax.swing.JProgressBar jpb17;
    private javax.swing.JProgressBar jpb170;
    private javax.swing.JProgressBar jpb171;
    private javax.swing.JProgressBar jpb172;
    private javax.swing.JProgressBar jpb173;
    private javax.swing.JProgressBar jpb174;
    private javax.swing.JProgressBar jpb175;
    private javax.swing.JProgressBar jpb176;
    private javax.swing.JProgressBar jpb177;
    private javax.swing.JProgressBar jpb178;
    private javax.swing.JProgressBar jpb179;
    private javax.swing.JProgressBar jpb18;
    private javax.swing.JProgressBar jpb180;
    private javax.swing.JProgressBar jpb181;
    private javax.swing.JProgressBar jpb182;
    private javax.swing.JProgressBar jpb183;
    private javax.swing.JProgressBar jpb184;
    private javax.swing.JProgressBar jpb185;
    private javax.swing.JProgressBar jpb186;
    private javax.swing.JProgressBar jpb187;
    private javax.swing.JProgressBar jpb188;
    private javax.swing.JProgressBar jpb189;
    private javax.swing.JProgressBar jpb19;
    private javax.swing.JProgressBar jpb190;
    private javax.swing.JProgressBar jpb191;
    private javax.swing.JProgressBar jpb192;
    private javax.swing.JProgressBar jpb193;
    private javax.swing.JProgressBar jpb194;
    private javax.swing.JProgressBar jpb195;
    private javax.swing.JProgressBar jpb196;
    private javax.swing.JProgressBar jpb197;
    private javax.swing.JProgressBar jpb198;
    private javax.swing.JProgressBar jpb199;
    private javax.swing.JProgressBar jpb2;
    private javax.swing.JProgressBar jpb20;
    private javax.swing.JProgressBar jpb200;
    private javax.swing.JProgressBar jpb21;
    private javax.swing.JProgressBar jpb22;
    private javax.swing.JProgressBar jpb23;
    private javax.swing.JProgressBar jpb24;
    private javax.swing.JProgressBar jpb25;
    private javax.swing.JProgressBar jpb26;
    private javax.swing.JProgressBar jpb27;
    private javax.swing.JProgressBar jpb28;
    private javax.swing.JProgressBar jpb29;
    private javax.swing.JProgressBar jpb3;
    private javax.swing.JProgressBar jpb30;
    private javax.swing.JProgressBar jpb31;
    private javax.swing.JProgressBar jpb32;
    private javax.swing.JProgressBar jpb33;
    private javax.swing.JProgressBar jpb34;
    private javax.swing.JProgressBar jpb35;
    private javax.swing.JProgressBar jpb36;
    private javax.swing.JProgressBar jpb37;
    private javax.swing.JProgressBar jpb38;
    private javax.swing.JProgressBar jpb39;
    private javax.swing.JProgressBar jpb4;
    private javax.swing.JProgressBar jpb40;
    private javax.swing.JProgressBar jpb41;
    private javax.swing.JProgressBar jpb42;
    private javax.swing.JProgressBar jpb43;
    private javax.swing.JProgressBar jpb44;
    private javax.swing.JProgressBar jpb45;
    private javax.swing.JProgressBar jpb46;
    private javax.swing.JProgressBar jpb47;
    private javax.swing.JProgressBar jpb48;
    private javax.swing.JProgressBar jpb49;
    private javax.swing.JProgressBar jpb5;
    private javax.swing.JProgressBar jpb50;
    private javax.swing.JProgressBar jpb51;
    private javax.swing.JProgressBar jpb52;
    private javax.swing.JProgressBar jpb53;
    private javax.swing.JProgressBar jpb54;
    private javax.swing.JProgressBar jpb55;
    private javax.swing.JProgressBar jpb56;
    private javax.swing.JProgressBar jpb57;
    private javax.swing.JProgressBar jpb58;
    private javax.swing.JProgressBar jpb59;
    private javax.swing.JProgressBar jpb6;
    private javax.swing.JProgressBar jpb60;
    private javax.swing.JProgressBar jpb61;
    private javax.swing.JProgressBar jpb62;
    private javax.swing.JProgressBar jpb63;
    private javax.swing.JProgressBar jpb64;
    private javax.swing.JProgressBar jpb65;
    private javax.swing.JProgressBar jpb66;
    private javax.swing.JProgressBar jpb67;
    private javax.swing.JProgressBar jpb68;
    private javax.swing.JProgressBar jpb69;
    private javax.swing.JProgressBar jpb7;
    private javax.swing.JProgressBar jpb70;
    private javax.swing.JProgressBar jpb71;
    private javax.swing.JProgressBar jpb72;
    private javax.swing.JProgressBar jpb73;
    private javax.swing.JProgressBar jpb74;
    private javax.swing.JProgressBar jpb75;
    private javax.swing.JProgressBar jpb76;
    private javax.swing.JProgressBar jpb77;
    private javax.swing.JProgressBar jpb78;
    private javax.swing.JProgressBar jpb79;
    private javax.swing.JProgressBar jpb8;
    private javax.swing.JProgressBar jpb80;
    private javax.swing.JProgressBar jpb81;
    private javax.swing.JProgressBar jpb82;
    private javax.swing.JProgressBar jpb83;
    private javax.swing.JProgressBar jpb84;
    private javax.swing.JProgressBar jpb85;
    private javax.swing.JProgressBar jpb86;
    private javax.swing.JProgressBar jpb87;
    private javax.swing.JProgressBar jpb88;
    private javax.swing.JProgressBar jpb89;
    private javax.swing.JProgressBar jpb9;
    private javax.swing.JProgressBar jpb90;
    private javax.swing.JProgressBar jpb91;
    private javax.swing.JProgressBar jpb92;
    private javax.swing.JProgressBar jpb93;
    private javax.swing.JProgressBar jpb94;
    private javax.swing.JProgressBar jpb95;
    private javax.swing.JProgressBar jpb96;
    private javax.swing.JProgressBar jpb97;
    private javax.swing.JProgressBar jpb98;
    private javax.swing.JProgressBar jpb99;
    private javax.swing.JTable jtb1;
    private javax.swing.JTextField numbox;
    private javax.swing.JLabel numboxlabel;
    private javax.swing.JLabel tatchartlabel;
    private javax.swing.JLabel tatis;
    private javax.swing.JTextField tqbox;
    private javax.swing.JLabel tqboxlabel;
    private javax.swing.JLabel wtchartlabel;
    private javax.swing.JLabel wtis;
    // End of variables declaration//GEN-END:variables
}
