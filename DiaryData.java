package verkefni2;

import java.nio.file.*;

public class DiaryData
{
    private String dir;
    private String date, orgtext, text;

    public DiaryData( String dir, String date )
    {
        this.dir = Paths.get(System.getProperty("user.home"),dir).toString();
        this.date = date;
        this.text = FileOps.read(dir,date);
        orgtext = text;
    }
  
    public String getText()
    {
        return text;
    }
    
    public String getDate()
    {
        return date;
    }

    public void setText( String text )
    {
        this.text = text;
    }
  
    public boolean isChanged()
    {
        return !text.equals(orgtext);
    }

   
    public void setDate( String date )
    {
        save();
        orgtext = text;
        this.date = date;
        this.text = FileOps.read(dir,date);
        orgtext = text;
    }

    public void save()
    {
        if( !isChanged() ) return;
        try
        {
            if( text.equals("") )
                FileOps.delete(dir,date);
            else
                FileOps.write(dir,date,text);
        }
        catch( Exception e )
        {
          
            e.printStackTrace();
        }
    }
    
 
    public boolean exists( String date )
    {
        return FileOps.exists(dir,date);
    }

    public static void main( String[] args )
    {
        DiaryData d = new DiaryData("diary","2020-01-26"); 
        String save = d.getText();
        d.setText("new text");
        d.setDate("2020-01-27");
        d.setDate("2020-01-26");
        if( !d.getText().equals("new text") ) throw new Error();
        d.setText(save);
        if( !d.getText().equals(save) ) throw new Error();
        d.save();
        if( !d.getText().equals(save) ) throw new Error();
    }
}