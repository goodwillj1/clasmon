package org.clas.detectors;

import org.clas.viewer.DetectorMonitor;
import org.jlab.groot.data.H1F;
import org.jlab.groot.data.H2F;
import org.jlab.groot.group.DataGroup;
import org.jlab.io.base.DataBank;
import org.jlab.io.base.DataEvent;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import javax.swing.event.ChangeListener;
import org.jlab.clas.fcmon.RichView;
import org.jlab.clas.fcmon.RichView.RICHtile;
import org.jlab.clas.fcmon.rich.RichHitCollection.RichHit;
import org.jlab.detector.view.DetectorShape2D;
import org.jlab.groot.base.TColorPalette;
import org.jlab.clas.fcmon.RICHMon;


import org.jlab.clas.fcmon.rich.RichPlotOccupancy;
import org.jlab.clas.fcmon.*;

import java.util.Random;



public class RICHmonitor  extends DetectorMonitor {

    public RICHmonitor(String name) {
        super(name);

        this.setDetectorTabNames("Scalers");
        this.init(false);

    }

    @Override
    public void createHistos() {
      this.getDetectorCanvas().getCanvas("Scalers").setGridX(false);
      this.getDetectorCanvas().getCanvas("Scalers").setGridY(false);
      this.getDetectorCanvas().getCanvas("Scalers").divide(1, 2);
    //  this.getDetectorCanvas().getCanvas("Occupancies and spectra").setAxisFontSize(0);

        H2F RichScaler = new H2F("RichScaler", "RichScaler",261, 0, 261, 207, 0, 207);
        H2F PmtScaler = new H2F("PmtScaler", "PmtScaler", 391, 0, 391 , 1000, 0 , 1000);
        DataGroup dg = new DataGroup(2,2);
        dg.addDataSet(RichScaler, 0);
        dg.addDataSet(PmtScaler, 0);
        this.getDataGroup().add(dg,0,0,0);




       int row = 23;
        double col= 0;
        int count = 28; // # of pmts per row decreasing by row
        int rowTemp =0; //temps used to count to 9 then places a space between PMTs
        int colTemp =0;
        int pmt=391;


        for(int  rowNum = 23*8 +23; rowNum> 0; rowNum--){ //# of rows * 8 + spaces in between rows

            int layer = rowNum;
            double  comp = 0;
            double colStart = col; //reset column


             if(rowTemp == 9){
              rowTemp =0;
              //pmt--;
              //System.out.print("pmt="+pmt);
            } else {

              for(int colNum = 0; colNum != count*8.0+count+9;colNum++){ // count = number of column; count * 8 + count + 9


               if(colTemp ==9) { // adds a space between PMTs
                  colTemp =0;
              }  else {
                  comp = col  + colNum;
                  this.getDataGroup().getItem(0,0,0).getH2F("RichScaler").fill(comp*1.0,layer*1.0);
                }
                colTemp++;
              }
            }
             pmt--;
             System.out.print("pmt="+pmt);

              col = colStart;
              if((rowNum) %  9== 0 && rowNum != 25*8){
                col +=4.5;
                count--;
                //pmt--;
                //System.out.print("pmt="+pmt);                
              }

            rowTemp++;
            pmt--;
            System.out.print("pmt="+pmt);  
      	}

         /*
         fillTile(65,12);
         fillTile(72,12);
         fillTile(80,12);
         fillTile(88,12);
         fillTile(96,12);
         fillTile(104,12);
         fillTile(112,12);
         fillTile(120,12);
         fillTile(128,12);     
         fillTile(16,12);
         fillTile(24,12);
         fillTile(32,12);
         fillTile(40,12);
         fillTile(48,12);
         fillTile(56,12);
         fillTile(64,12);
         */





       Random rando = new Random();

        for(int i =0; i < 10;i++){
          int comp = rando.nextInt(192) + 1;
          int layer = rando.nextInt(138) + 1;

          System.out.println("component, Layer: " + comp + ", " + layer);

          fillTile(comp,layer);

        } //testing layer and component are in right location with 10 random numbers
        /*
        for(int i = 1; i <= 138; i++){
          fillTile(1,i);
          if(i==3||i==5||i==7||i==12||i==15||i==19||i==24||i==28||i==33||i==39||i==44||i==50||i==57||i==63||i==70||i==78||i==85||i==93||i==102||i==110||i==119||i==129||i==138){
          	fillTile(128,i);
          }
          else{
          	fillTile(192,i);
          }
        }


*/




    /*    // initialize canvas and create histograms
      this.setNumberOfEvents(0);
        this.getDetectorCanvas().getCanvas("Occupancies and spectra").divide(1, 3);
        this.getDetectorCanvas().getCanvas("Occupancies and spectra").setGridX(false);
        this.getDetectorCanvas().getCanvas("Occupancies and spectra").setGridY(false);

        H2F summary = new H2F("summary","summary",192, 0.5, 192.5, 138, 0.5, 138.5);
        summary.setTitleX("MAPMT channel");
        summary.setTitleY("tile");
        summary.setTitle("RICH");
        DataGroup sum = new DataGroup(1,1);
        sum.addDataSet(summary, 0);
        this.setDetectorSummary(sum);

        H2F occTDC = new H2F("occTDC", "occTDC", 192, 0.5, 192.5, 138, 0.5, 138.5);
        occTDC.setTitleY("tile number");
        occTDC.setTitleX("channel number");
        occTDC.setTitle("TDC Occupancy");

        H2F tdc_leading_edge = new H2F("tdc_leading_edge", "tdc leading edge", 200, 0, 400, 417, 0.5, 417.5);
        tdc_leading_edge.setTitleX("leading edge time [ns]");
        tdc_leading_edge.setTitleY("MAPMT (3 slots / tile)");
        tdc_leading_edge.setTitle("TDC timing");

        H2F tdc_trailing_edge = new H2F("tdc_trailing_edge", "tdc trailing edge", 200, 0, 400, 417, 0.5, 417.5);
        tdc_trailing_edge.setTitleX("trailing edge time [ns]");
        tdc_trailing_edge.setTitleY("MAPMT (3 slots / tile)");
        tdc_trailing_edge.setTitle("TDC timing");

        DataGroup dg = new DataGroup(2,2);
        dg.addDataSet(occTDC, 0);
        dg.addDataSet(tdc_leading_edge, 1);
        dg.addDataSet(tdc_trailing_edge, 1);
        this.getDataGroup().add(dg,0,0,0);
        */
        int y = 0;
        for(int n=0;n<391;n++){
        	this.getDataGroup().getItem(0,0,0).getH2F("RichScaler").fill(n*1.0,y*1.0);
        	y++;
        }
    }



    public void fillTile(int comp,int layer){

      int row = 0;
      int NumofTiles = 5;
      int tempLayer = 0;
      int count = 2;
      int[] firstTile = {2,5,8,11,15,19,23,28,33,38,44,50,56,63,70,77,85,93,101,110,119,128,138};

      while(tempLayer != layer){
        row++;

        NumofTiles++;


        if(tempLayer == 2 || tempLayer == 11 || tempLayer == 23 || tempLayer == 38 || tempLayer == 56 || tempLayer == 77|| tempLayer == 101 || tempLayer == 128 ){
          count++;
        }

        for(int i = 0; i < count; i++){
          tempLayer++;
          if(tempLayer == layer){
            break;
          }

        }

        //System.out.println("in this row " + row + " NumofTiles: " + NumofTiles + " count:  " + count + " tempLayer: " + tempLayer );
      }
      
      if(layer == 3 || layer ==5 || layer ==7|| layer ==12 || layer ==15 || layer ==19 || layer ==24|| layer ==28 || layer ==33 ||layer ==39|| layer ==44 || layer ==50|| layer ==57|| layer ==63 || layer ==70 || layer ==78|| layer ==85 || layer ==93 || layer == 102|| layer == 110 || layer == 119 || layer ==129|| layer ==138){
        if(comp >64 && comp < 129){
      	  comp+=64;
      	}
      }


    //  System.out.println(row + " " + layer);
      int  firstRowTile = firstTile[row-1];


    // System.out.print("First tile = " + firstRowTile + " ");


       double x = 0;

        if(row % 2 == 0){
          x  =  5+ (4.5*(23-row)) + 1;
        } else {

          x =  5+ (4.5*(23-row));
        }

      for(int tempTile = firstRowTile; tempTile > layer; tempTile--){
          if(tempTile ==3 || tempTile == 5 || tempTile ==7 || tempTile == 12 || tempTile ==15 || tempTile ==19 || tempTile ==24|| tempTile ==28 || tempTile ==33 || tempTile ==39|| tempTile ==44 || tempTile ==50 || tempTile ==57|| tempTile ==63 || tempTile ==70 || tempTile ==78|| tempTile ==85) {
              x += 2*9;
          } else  if( tempTile ==93 || tempTile == 102|| tempTile == 110 || tempTile == 119 || tempTile ==129|| tempTile ==138){
            x += 2*9;
          } else {
            x += 3*9;
          }
        }
/*
      if(layer == 3 || layer ==5 || layer ==7|| layer ==12 || layer ==15 || layer ==19 || layer ==24|| layer ==28 || layer ==33 ||layer ==39|| layer ==44 || layer ==50|| layer ==57|| layer ==63 || layer ==70 || layer ==78|| layer ==85 || layer ==93 || layer == 102|| layer == 110 || layer == 119 || layer ==129|| layer ==138){
      	if(comp >64 && comp < 129){
      		comp+=64;
      	}
      }
      */


      int y = row *  8 + row - 1;

      if(layer == 3 || layer ==5 || layer ==7|| layer ==12 || layer ==15 || layer ==19 || layer ==24|| layer ==28 || layer ==33 ||layer ==39|| layer ==44 || layer ==50|| layer ==57|| layer ==63 || layer ==70 || layer ==78|| layer ==85 || layer ==93 || layer == 102|| layer == 110 || layer == 119 || layer ==129|| layer ==138){

        if(comp > 0 && comp < 9){
          x+= (comp - 1);
        } else if (comp  > 9 && comp < 17 ) {
          y--;
          x+= (comp - 9);
        }else if (comp  > 16 && comp < 25) {
          x+= (comp - 17);
          y-=2;
        }else if (comp  > 24 && comp < 33) {
          x+= (comp - 25);
          y-=3;
        } else if (comp  > 32 && comp < 41) {
            x+= (comp - 33);
            y-=4;
        } else if (comp > 40  && comp < 49) {
            x+= (comp - 41);
            y-=5;
        } else if (comp  > 48  && comp < 57) {
            x+= (comp - 49);
            y-=6;
        } else if (comp  > 56  && comp < 65) {
            x+= (comp - 57);
            y-=7;
        }else if (comp  > 128  && comp < 137) {
            x+= (comp - 120);
        }else if (comp  > 136  && comp < 145) {
            x+= (comp - 128);
            y--;
        }else if (comp  > 144  && comp < 153) {
            x+= (comp - 136);
            y-=2;
        }else if (comp  > 152  && comp < 161) {
            x+= (comp - 144);
            y-=3;
        }else if (comp  > 160  && comp < 169) {
            x+= (comp - 152);
            y-=4;
        }else if (comp  > 168  && comp < 177) {
            x+= (comp - 160);
            y-=5;
        }else if (comp  > 176  && comp < 185) {
            x+= (comp - 168);
            y-=6;
        }else if (comp  > 184  && comp < 193) {
            x+= (comp - 176);
            y-=7;
        }
    }else {
        if(comp > 0 && comp < 25){
          x+= (comp - 1);

          if(comp - 1 >= 8)
            x++;
          if(comp -1 >= 16)
            x++;
        } else if (comp  > 24 && comp < 49) {
          y--;
          x+= (comp - 25);

            if(comp - 25 >= 8)
              x++;
            if(comp - 25 >= 16)
              x++;
        }else if (comp  > 48 && comp < 73) {
          x+= (comp - 49);
          y-=2;

          if(comp - 49 >= 8)
            x++;
          if(comp - 49 >= 16)
            x++;
        }else if (comp  > 72  && comp < 97) {
          x+= (comp - 73);
          y-=3;

          if(comp - 73 >= 8)
            x++;
          if(comp - 73 >= 16)
            x++;
        } else if (comp  > 96 && comp < 121) {
            x+= (comp - 97);
            y-=4;
            if(comp - 97 >= 8)
              x++;
            if(comp - 97 >= 16)
              x++;
        } else if (comp  > 120 && comp < 145) {
            x+= (comp - 121);
            y-=5;

            if(comp - 121 >= 8)
              x++;
            if(comp - 121 >= 16)
              x++;
        } else if (comp  > 144 && comp < 169) {
            x+= (comp - 145);
            y-=6;

            if(comp - 145 >= 8)
              x++;
            if(comp - 145 >= 16)
              x++;
        } else if (comp  > 168 && comp < 193) {
            x+= (comp - 169);
            y-=7;

            if(comp - 169 >= 8)
              x++;
            if(comp - 169 >= 16)
              x++;
        }
    }



    //  System.out.println("x = " + x);
    //  System.out.println("y = " + y);
/*
    if(comp > 128 && (layer == 3 || layer ==5 || layer ==7|| layer ==12 || layer ==15|| layer ==19 || layer ==24|| layer ==28 || layer ==33)){

    } else if ( comp > 128 && (layer ==39|| layer ==44 || layer ==50|| layer ==57|| layer ==63 || layer ==70|| layer ==78|| layer ==85 || layer ==93 || layer == 102|| layer == 110 || layer == 119 || layer ==129|| layer ==138)) {


    } else if(layer %2 != 0){
      this.getDataGroup().getItem(0,0,0).getH2F("RICH").fill(x,y);
    }
*/
    this.getDataGroup().getItem(0,0,0).getH2F("RichScaler").fill(x,y);

  }

    @Override
    public void plotHistos() {


      this.getDetectorCanvas().getCanvas("Scalers").draw(this.getDataGroup().getItem(0,0,0).getH2F("RichScaler"));
      this.getDetectorCanvas().getCanvas("Scalers").draw(this.getDataGroup().getItem(0,0,0).getH2F("PmtScaler"));




      // plotting histos
      /*  this.getDetectorCanvas().getCanvas("Occupancies and spectra").cd(0);
        this.getDetectorCanvas().getCanvas("Occupancies and spectra").getPad(0).getAxisZ().setLog(getLogZ());
        this.getDetectorCanvas().getCanvas("Occupancies and spectra").draw(this.getDataGroup().getItem(0,0,0).getH2F("occTDC"));
        this.getDetectorCanvas().getCanvas("Occupancies and spectra").cd(1);
        this.getDetectorCanvas().getCanvas("Occupancies and spectra").getPad(1).getAxisZ().setLog(getLogZ());
        this.getDetectorCanvas().getCanvas("Occupancies and spectra").draw(this.getDataGroup().getItem(0,0,0).getH2F("tdc_leading_edge"));
        this.getDetectorCanvas().getCanvas("Occupancies and spectra").cd(2);
        this.getDetectorCanvas().getCanvas("Occupancies and spectra").getPad(2).getAxisZ().setLog(getLogZ());
        this.getDetectorCanvas().getCanvas("Occupancies and spectra").draw(this.getDataGroup().getItem(0,0,0).getH2F("tdc_trailing_edge"));
        this.getDetectorCanvas().getCanvas("Occupancies and spectra").update();

        this.getDetectorView().getView().repaint();
        this.getDetectorView().update();*/
    }

    @Override
    public void processEvent(DataEvent event) {





/*

        if (this.getNumberOfEvents() >= super.eventResetTime_current && super.eventResetTime_current > 0){
            resetEventListener();
        }

		//if (!testTriggerMask()) return;

        // process event info and save into data group
        if(event.hasBank("RICH::adc")==true){
	    DataBank bank = event.getBank("RICH::adc");
	    int rows = bank.rows();
	    for(int loop = 0; loop < rows; loop++){
                int sector  = bank.getByte("sector", loop);
                int layer   = bank.getByte("layer", loop);
                int comp    = bank.getShort("component", loop);
                int order   = bank.getByte("order", loop);
                int adc     = bank.getInt("ADC", loop);
                float time  = bank.getFloat("time", loop);
//                System.out.println("ROW " + loop + " SECTOR = " + sector + " LAYER = " + layer + " COMPONENT = " + comp + " ORDER + " + order +
//                      " ADC = " + adc + " TIME = " + time);
                if(adc>0) {
                    this.getDataGroup().getItem(0,0,0).getH2F("occADC").fill(comp*1.0,layer*1.0);
                    this.getDataGroup().getItem(0,0,0).getH2F("adc").fill(adc*1.0, (comp-1)*138+layer);

                }
	    }
    	}
        if(event.hasBank("RICH::tdc")==true){
            DataBank  bank = event.getBank("RICH::tdc");
            int rows = bank.rows();
            for(int i = 0; i < rows; i++){
                int     sector = bank.getByte("sector",i);
                int  layerbyte = bank.getByte("layer",i);
                long     layer = layerbyte & 0xFF;
                long      comp = bank.getShort("component",i);
                long     pmt   = comp/64;
                int        tdc = bank.getInt("TDC",i);
                int  orderbyte = bank.getByte("order",i); // order specifies left-right for ADC
                long     order = orderbyte & 0xFF;


                         // System.out.println("ROW " + i + " SECTOR = " + sector + " LAYER = " + layer + " COMPONENT = "+ comp + " TDC = " + TDC);
                if(tdc>0){
                    this.getDataGroup().getItem(0,0,0).getH2F("occTDC").fill(comp,layer*1.0);

                    if(orderbyte == 1) this.getDataGroup().getItem(0,0,0).getH2F("tdc_leading_edge").fill(tdc, layer*3 + pmt);
                    if(orderbyte == 0) this.getDataGroup().getItem(0,0,0).getH2F("tdc_trailing_edge").fill(tdc, layer*3 + pmt);

                    this.getDetectorSummary().getH2F("summary").fill(comp,layer*1.0);
                }
            }
        }*/
    }

    @Override
    public void timerUpdate() {

    }


}
