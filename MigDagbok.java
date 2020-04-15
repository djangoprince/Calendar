package verkefni2;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JFrame;
import java.lang.Runnable;
import static java.lang.Thread.sleep;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import verkefni2.DiaryData;

public class MigDagbok
{
    static javax.swing.JTextArea theTextArea;
    static com.toedter.calendar.JCalendar theCalendar = new com.toedter.calendar.JCalendar(java.util.Locale.forLanguageTag("is-IS"));
    static MigDagbok k;
    static private Runnable myRunnable;
    private javax.swing.Timer myTimer;
    static javax.swing.JFrame g ;
    static PropertyChangeListener pl;
    static WindowListener wl;
    //private static Klukka tickTock = new Klukka(100, () -> {tick();});
    
    private String dir;
    private String date, orgtext, text;
    
    private static final SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
    public static SimpleDateFormat sdfdags = new SimpleDateFormat("YYYY-MM-dd");
    public static String timeString()
    {
        return sdf.format(new java.util.Date().getTime());
    }
    static DiaryData dagb = new DiaryData("diary", sdfdags.format(theCalendar.getDate())); 
    static String direct = Paths.get(System.getProperty("user.home"),"diary").toString();
    
    public MigDagbok( int delay, Runnable action ) throws InterruptedException
    {
        ActionListener a = new ActionListener()
            { public void actionPerformed( ActionEvent evt )
                {
                    myRunnable.run();
                }
            };
        
       myRunnable = action;
       myTimer = new javax.swing.Timer(delay,a);
        myTimer.start();
        sleep(1000);
        
    }

    public static void tick(){ g.setTitle("Dagbók: " + sdf.format(new java.util.Date()));
}
    public static void main( String args[] ) throws InterruptedException
    { 
        
        Runnable evt =() ->{ g = new javax.swing.JFrame( k.timeString());
				g.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
				g.setLayout (   new net.miginfocom.swing.MigLayout("wrap 1",   "[grow 100]",   "[grow 0][grow 100]"));
                                
              
      
           try {
                k= new MigDagbok (1000,()->{g.setTitle("dagbok "+MigDagbok.timeString());});
            } catch (InterruptedException ex) {
                Logger.getLogger(MigDagbok.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        
        PropertyChangeListener pl =new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent e)
        {
            dagb.setText(theTextArea.getText());
            dagb.save();
            dagb.setDate(sdfdags.format(theCalendar.getDate()));
            theTextArea.setText(dagb.getText());
}
             };
        WindowListener wl = new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {   dagb.setText(theTextArea.getText());
                                                           dagb.save();
                                                            System.exit(0);
                                                        }
    };
                
        theTextArea = new javax.swing.JTextArea();
        g.add(theCalendar,"center");
        javax.swing.JScrollPane sp = new javax.swing.JScrollPane();
        sp.setViewportView(theTextArea);
        g.add(sp,"grow");
        g.pack();
        theCalendar.addPropertyChangeListener(pl);
        g.addWindowListener(wl);
        theTextArea.setText(FileOps.read(direct,sdfdags.format(theCalendar.getDate())));
        
     
     
        g.setVisible(true);
            };
        java.awt.EventQueue.invokeLater(evt);
	}
}