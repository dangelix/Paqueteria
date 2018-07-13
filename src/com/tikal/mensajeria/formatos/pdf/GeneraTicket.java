package com.tikal.mensajeria.formatos.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
//import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
//import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import com.tikal.mensajeria.modelo.entity.Envio;
import com.tikal.mensajeria.modelo.entity.Paquete;
import com.tikal.mensajeria.modelo.entity.Venta;
import com.tikal.mensajeria.modelo.login.Sucursal;
import com.tikal.mensajeria.util.NumberToLetterConverter;

import java.io.*;
import java.net.MalformedURLException;
import java.util.List;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.servlet.ServletOutputStream;
//import java.util.Date;
//import java.util.List; 
public class GeneraTicket {
	
	
	
   

	public GeneraTicket(List<Envio> e, Sucursal s, Venta v,OutputStream ops) throws MalformedURLException, IOException {

    	  final String condiciones = "1.-Después de 15 días MERVEL EXPRESS no se hace responsable de su envío.\n\n"
    	  		+ "2.-No nos hacemos responsables en envíos con valor arriba de 80 dias de salario "
    	  		+ "mínimo.\n\n"
    	  		+"3.-Las partes convienen que si el valor del ENVÍO no es declarado y sufre, por causa"
    	  		+ "imputable a MERVEL EXPRESS, la pédida total, daños o menoscabos, ésta pagará "
    	  		+ "como máxima responsabilidad por la pérdida o daño, la cantidad equivalente de hasta "
    	  		+ "30 veces el salario mínimo general vigente en el D. F., en la fecha en que se haya "
    	  		+ "recibido y documentado el ENVÍO. EL REMITENTE deberá justificar el valor comercial"
    	  		+ "de la pérdida o de su reparación. En caso de envío de valor declarado, deberá "
    	  		+ "asegurarse y la responsabilidad es por el monto total. Conforme a lo siguiente:\n \n"
    	  		+ "4.-SEGURO OPCIONAL\n\n"
    	  		+ "5.-MERVEL EXPRESS tiene el derecho de revisar el paquete más no a obligación, por lo "
    	  		+ "que podrá exigir al REMITENTE su apertura y reconocimiento, si ése se rehusare,"
    	  		+ "MERVEL EXPRESS quedará libre de cualquier responsabilidad.\n\n"
    	  		+ "6.-MERVEL EXPRESS se reserva el derecho de inspeccionar el envío en cualquier "
    	  		+ "momento, así como permitir a las autoriddes competentes a llevar a cabo las "
    	  		+ "inspecciones que consideren adecuadas. Asimismo, MERVEL EXPRESS se reserve el "
    	  		+ "derecho a rechazar o suspender el porte de cualquier envío prohibido o que contenga "
    	  		+ "materiales que dañen o puedan dañar otros envíos que puedan constituir un riesgo a "
    	  		+ "equipo o empleados de MERVEL EXPRESS o de sus prestadores de servicios.\n\n"
    	  		+ "7.-En servicio internacional MERVEL EXPRESS se obliga a entregar puntualmente el "
    	  		+ "envío siempre y cuando las autoridades aduaneras hayan cumplido con sus horarios "
    	  		+ "normales oficiales. El plazo se contará a partir de la recepción y documentación del "
    	  		+ "ENVÍO.\n\n"
    	  		+ "** EL REMITENTE se obliga para con MERVEL EXPRESS\n\n"
    	  		+ "a) A pagar la tarifa y los impuestos del servicio.\n\n"
    	  		+ "b) A entregar toda la documentación en cumplimiento de las disposiciones legales "
    	  		+ "aplicables, de aduanas, de exportación e importación de los países a, desde, o a través "
    	  		+ "de los cuales será transportado el ENVÍO y a efectuar el pago de los gastos y costos que "
    	  		+ "origine incluyendo sin limitación alguna, recargos, aranceles, multas, honorarios,"
    	  		+ "etc. Por lo que MERVEL EXPRESS no asume ninguna responsabilidad por el "
    	  		+ "incumplimiento de estas obligaciones.\n\n"
    	  		+ "8.-MERVEL EXPRESS se coordina con sevicios de mensajeria externos.\n\n"
    	  		+ "9.-MERVEL EXPRESS elegirá el trasporte que a su interés convenga.";
    	  
    	 
    	  
    	try {
    		Rectangle envelope = new Rectangle(200, 690);
    	//	Document pdfDoc = new Document(envelope, 230f, 10f, 100f, 0f);
    		Document document = new Document(envelope,0,0,0,0);   	  
	        PdfWriter.getInstance(document,ops);
	        document.open();
	        
//    		PrintService service = PrintServiceLookup.lookupDefaultPrintService();
//    		DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
//    		DocPrintJob pj = service.createPrintJob(); 
//    		
//    		
//    	    String ss=new String("Aquí lo que vamos a imprimir.");
//    	    byte[] bytes;
//    	    //Transformamos el texto a bytes que es lo que soporta la impresora
//    	    bytes=ss.getBytes();
//    	    //Creamos un documento (Como si fuese una hoja de Word para imprimir)
//    	    Doc doc=new SimpleDoc(bytes,flavor,null);
//    	    //Obligado coger la excepción PrintException
//    	    try {
//	    	    //Mandamos a impremir el documento
//	    	    pj.print(doc, null);
//    	    }
//    	    catch (PrintException x) {
//    	    	System.out.println("Error al imprimir: "+x.getMessage());
//    	    }
    	    
    	    Font f0 = new Font();
      	  //  f1.setStyle(1);
      	    f0.setSize(2);
      	    f0.setColor(BaseColor.BLACK);
    	    
    	    
    	    Font f1 = new Font();
    	  //  f1.setStyle(1);
    	    f1.setSize(8);
    	    f1.setColor(BaseColor.BLACK);
    	    
    	    Font f2 = new Font();
    	   // f2.setStyle(2);
    	    f2.setSize(12);
    	    f2.setColor(BaseColor.BLACK);
    	    f2.setStyle(1);
    	    
    	    Font f3 = new Font();
    	    f3.setStyle(1);
    	    f3.setSize(8);
    	    f3.setColor(BaseColor.BLACK);
    	   
    	    Font f4 = new Font();
        	  //  f1.setStyle(1);
        	    f4.setSize(4);
        	    f4.setColor(BaseColor.BLACK);
        	    
            PdfPTable table = new PdfPTable(6);   
            
            
            Image imagen = Image.getInstance("img/LogoMervel.png");
            imagen.scaleAbsolute(150, 70);
           
      
            PdfPCell c1 = new PdfPCell(imagen);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setVerticalAlignment(Element.ALIGN_CENTER);
            c1.setColspan(6);
            c1.setRowspan(5);
            c1.setBorder(Rectangle.NO_BORDER);
            table.addCell(c1);
           
            Paragraph p2 = new Paragraph(s.getDomicilio(),f1);
            PdfPCell c2 = new PdfPCell(p2);
            c2.setHorizontalAlignment(Element.ALIGN_LEFT);
            c2.setColspan(6);c2.setRowspan(2);
            c2.setBorder(Rectangle.NO_BORDER);
            table.addCell(c2);
            
//            Paragraph p3 = new Paragraph(" ",f1);
//            PdfPCell c3 = new PdfPCell(p3);
//            c3.setHorizontalAlignment(Element.ALIGN_LEFT);
//            c3.setColspan(3);c3.setRowspan(1);
//            c3.setBorder(Rectangle.NO_BORDER);
//            table.addCell(c3);
            
            Paragraph p5 = new Paragraph("RFC: "+s.getRfc(),f1);
            PdfPCell c5 = new PdfPCell(p5);
            c5.setHorizontalAlignment(Element.ALIGN_LEFT);
            c5.setColspan(3);c5.setRowspan(1);
            c5.setBorder(Rectangle.NO_BORDER);
            table.addCell(c5);
            
            Paragraph p4 = new Paragraph("Tel: "+s.getTelefono(),f1);
            PdfPCell c4 = new PdfPCell(p4);
            c4.setHorizontalAlignment(Element.ALIGN_LEFT);
            c4.setColspan(6);c4.setRowspan(1);
            c4.setBorder(Rectangle.NO_BORDER);
            table.addCell(c4);
                       
//            Paragraph x = new Paragraph(" ",f1);
//            PdfPCell x1 = new PdfPCell(x);
//            x1.setHorizontalAlignment(Element.ALIGN_LEFT);
//            x1.setColspan(3);x1.setRowspan(1);
//            x1.setBorder(Rectangle.NO_BORDER);
//            table.addCell(x1);
            
            Paragraph p6 = new Paragraph("Folio:"+v.getFolio(),f2);
            PdfPCell c6 = new PdfPCell(p6);
            c6.setHorizontalAlignment(Element.ALIGN_LEFT);
            c6.setColspan(3);
            c6.setBorder(Rectangle.NO_BORDER);
            table.addCell(c6);
            
            Paragraph p7 = new Paragraph(v.getFecha().toString(), f1);
            PdfPCell c7 = new PdfPCell(p7);
            c7.setHorizontalAlignment(Element.ALIGN_RIGHT);
            c7.setColspan(3);c7.setBorder(Rectangle.NO_BORDER);
            table.addCell(c7);
            
            Paragraph p8 = new Paragraph("Cliente:",f1);
            PdfPCell c8 = new PdfPCell(p8);
            c8.setHorizontalAlignment(Element.ALIGN_CENTER);
            c8.setColspan(2);
            //c8.setBorder(Rectangle.NO_BORDER);
            table.addCell(c8);	
           // table.addCell(c6);
            
           
            Paragraph px = new Paragraph(e.get(0).getCliente().getNombre(),f1);
            PdfPCell cx = new PdfPCell(px);
            cx.setHorizontalAlignment(Element.ALIGN_CENTER);
            cx.setColspan(4);
           // cx.setBorder(Rec tangle.NO_BORDER);
           // cx.setBorder(Rectangle.NO_BORDER);
            table.addCell(cx);
            
          Paragraph x = new Paragraph(" ",f0);
          PdfPCell x1 = new PdfPCell(x);
          x1.setHorizontalAlignment(Element.ALIGN_LEFT);
          x1.setColspan(6);x1.setRowspan(1);
          x1.setBorder(Rectangle.NO_BORDER);
          table.addCell(x1);
            
           
//    // segunda parte... tabla 2
//
            document.add(table);
           // document.add(new Paragraph("\n"));
            //document.add(new Paragraph("\n"));
            
            PdfPTable table2 = new PdfPTable(8);        
            
            
            
            Paragraph pxx = new Paragraph("Cant",f3);
            PdfPCell cxx = new PdfPCell(pxx);
            cxx.setHorizontalAlignment(Element.ALIGN_LEFT);
            cxx.setColspan(2);
          //  cxx.setBorder(Rectangle.NO_BORDER);
            table2.addCell(cxx);
            
            Paragraph p9 = new Paragraph("Descripción",f3);
            PdfPCell c9 = new PdfPCell(p9);
            //c9.setBackgroundColor(black);
            c9.setHorizontalAlignment(Element.ALIGN_LEFT);
            c9.setColspan(3);
            c9.setRowspan(1);
          //  c9.setBackgroundColor(BaseColor.BLACK);
            table2.addCell(c9);
            
            Paragraph p1 = new Paragraph("Importe",f3);
            PdfPCell c11 = new PdfPCell(p1);
            c11.setHorizontalAlignment(Element.ALIGN_CENTER);
            //c11.setBackgroundColor(BaseColor.BLACK);
            c11.setColspan(3);
            table2.addCell(c11);
            
            Paragraph p10 = new Paragraph(v.getCantidad().toString(),f1);//e.getCantidad().toString()),f1);
            PdfPCell c10 = new PdfPCell(p10);
            c10.setHorizontalAlignment(Element.ALIGN_LEFT);
            //c10.setBackgroundColor(BaseColor.BLACK);
            c10.setColspan(2);
            table2.addCell(c10);
            
            Paragraph p12 = new Paragraph("ENVIO",f1);//e.get(0).getDescripcion(),f1);
            PdfPCell c12 = new PdfPCell(p12);
            c12.setHorizontalAlignment(Element.ALIGN_LEFT);
          //  c12.setBackgroundColor(BaseColor.BLACK);
            c12.setColspan(3);
            table2.addCell(c12);
            
            Paragraph p13 = new Paragraph("$ "+ v.getTotal().toString(),f2);//String.valueOf());//e.getPrecio()),f1);
            PdfPCell c13 = new PdfPCell(p13);
            c13.setHorizontalAlignment(Element.ALIGN_LEFT);
            //c13.setBackgroundColor(BaseColor.BLACK);
            c13.setColspan(3);
            table2.addCell(c13);
            
            
            Paragraph xx = new Paragraph(" ",f0);
            PdfPCell x1x = new PdfPCell(xx);
            x1x.setHorizontalAlignment(Element.ALIGN_LEFT);
            x1x.setColspan(8);x1x.setRowspan(1);
            x1x.setBorder(Rectangle.NO_BORDER);
            table2.addCell(x1x);
            
            for (Envio en:e){
            	
            	 if (e.size()==1){
//            
         
            	
            	  Paragraph p14 = new Paragraph("Guía:",f1);
                  PdfPCell c14 = new PdfPCell(p14);
                  c14.setHorizontalAlignment(Element.ALIGN_LEFT);
                 //.setBackgroundColor(BaseColor.BLACK);
                  c14.setColspan(2);
                  table2.addCell(c14);
                  
                  Paragraph p15 = new Paragraph(en.getGuia().toString(),f3);
                  PdfPCell c15 = new PdfPCell(p15);
                  c15.setHorizontalAlignment(Element.ALIGN_LEFT);
                 // c15.setBackgroundColor(BaseColor.BLACK);
                  c15.setColspan(6);
                  table2.addCell(c15);
                  
                  Paragraph p17 = new Paragraph("Rastreo:",f1);
                  PdfPCell c17 = new PdfPCell(p17);
                  c17.setHorizontalAlignment(Element.ALIGN_LEFT);
                 //.setBackgroundColor(BaseColor.BLACK);
                  c17.setColspan(2);
                  table2.addCell(c17);
                  
                  Paragraph p16 = new Paragraph(en.getRastreo().toString(),f3);
                  PdfPCell c16 = new PdfPCell(p16);
                  c16.setHorizontalAlignment(Element.ALIGN_LEFT);
                 // c15.setBackgroundColor(BaseColor.BLACK);
                  c16.setColspan(6);
                  table2.addCell(c16);
//
               
                	  
                  Paragraph p18 = new Paragraph("Tipo Envío:",f1);
                  PdfPCell c18= new PdfPCell(p18);
                  c18.setHorizontalAlignment(Element.ALIGN_LEFT);
                 //.setBackgroundColor(BaseColor.BLACK);
                  c18.setColspan(2);
                  table2.addCell(c18);
                  
                  Paragraph p19 = new Paragraph(en.getTipoEnvio(),f3);
                  PdfPCell c19 = new PdfPCell(p19);
                  c19.setHorizontalAlignment(Element.ALIGN_LEFT);
                 // c15.setBackgroundColor(BaseColor.BLACK);
                  c19.setColspan(6);
                  table2.addCell(c19);
                  
                  Paragraph p20 = new Paragraph("Tipo Paquete",f1);
                  PdfPCell c20= new PdfPCell(p20);
                  c20.setHorizontalAlignment(Element.ALIGN_LEFT);
                 //.setBackgroundColor(BaseColor.BLACK);
                  c20.setColspan(2);
                  table2.addCell(c20);
                  
                  Paragraph p21 = new Paragraph(en.getTipoServicio(),f3);
                  PdfPCell c21 = new PdfPCell(p21);
                  c21.setHorizontalAlignment(Element.ALIGN_LEFT);
                 // c15.setBackgroundColor(BaseColor.BLACK);
                  c21.setColspan(6);
                  table2.addCell(c21);
                  
                  
                  
                  Paragraph p22 = new Paragraph("Medidas:",f1);
                  PdfPCell c22= new PdfPCell(p22);
                  c22.setHorizontalAlignment(Element.ALIGN_LEFT);
                 //.setBackgroundColor(BaseColor.BLACK);
                  c22.setColspan(2);
                  table2.addCell(c22);
                  
                  Paragraph p23 = new Paragraph(en.getPaquete().getLargo().toString(),f3);
                  PdfPCell c23 = new PdfPCell(p23);
                  c23.setHorizontalAlignment(Element.ALIGN_LEFT);
                 // c15.setBackgroundColor(BaseColor.BLACK);
                  c23.setColspan(2);
                  table2.addCell(c23);
                  
                  Paragraph p24 = new Paragraph(en.getPaquete().getAlto().toString(),f3);
                  PdfPCell c24 = new PdfPCell(p24);
                  c24.setHorizontalAlignment(Element.ALIGN_LEFT);
                 // c15.setBackgroundColor(BaseColor.BLACK);
                  c24.setColspan(2);
                  table2.addCell(c24);
                  
                  Paragraph p25 = new Paragraph(en.getPaquete().getAncho().toString(),f3);
                  PdfPCell c25 = new PdfPCell(p25);
                  c25.setHorizontalAlignment(Element.ALIGN_LEFT);
                 // c15.setBackgroundColor(BaseColor.BLACK);
                  c25.setColspan(2);
                  table2.addCell(c25);
                  
//                 
                  Paragraph p266 = new Paragraph("Peso:",f1);
                  PdfPCell c266= new PdfPCell(p266);
                  c266.setHorizontalAlignment(Element.ALIGN_LEFT);
                 //.setBackgroundColor(BaseColor.BLACK);
                  c266.setColspan(2);
                  table2.addCell(c266);
                  
                
                  
                  Paragraph p277 = new Paragraph(en.getPaquete().getPeso().toString(),f3);
                  PdfPCell c277 = new PdfPCell(p277);
                  c277.setHorizontalAlignment(Element.ALIGN_LEFT);
                 // c15.setBackgroundColor(BaseColor.BLACK);
                  c277.setColspan(2);
                  table2.addCell(c277);
                  
                  Paragraph p26 = new Paragraph("Peso Vol.:",f1);
                  PdfPCell c26= new PdfPCell(p26);
                  c26.setHorizontalAlignment(Element.ALIGN_LEFT);
                 //.setBackgroundColor(BaseColor.BLACK);
                  c26.setColspan(2);
                  table2.addCell(c26);
                  
                  Paragraph p27 = new Paragraph(en.getPaquete().getPesoVol().toString(),f3);
                  PdfPCell c27 = new PdfPCell(p27);
                  c27.setHorizontalAlignment(Element.ALIGN_LEFT);
                 // c15.setBackgroundColor(BaseColor.BLACK);
                  c27.setColspan(2);
                  table2.addCell(c27);
                  
                 

                  Paragraph p28 = new Paragraph("",f3);
                  PdfPCell c28= new PdfPCell(p28);
                  c28.setHorizontalAlignment(Element.ALIGN_LEFT);
                 //.setBackgroundColor(BaseColor.BLACK);
                  c28.setBorder(Rectangle.NO_BORDER);
                  c28.setColspan(4);
                  table2.addCell(c28);
//                  
//                  Paragraph p29 = new Paragraph("folio",f3);
//                  PdfPCell c29 = new PdfPCell(p29);
//                  c29.setHorizontalAlignment(Element.ALIGN_LEFT);
//                 // c15.setBackgroundColor(BaseColor.BLACK);
//                  c29.setColspan(2);
//                  table2.addCell(c29);
//                  
                
                  table2.addCell(x1x);
                  
                  
                 
                
 
                  


            }else{
            	
            	 Paragraph p141 = new Paragraph("Guía:",f1);
                 PdfPCell c141 = new PdfPCell(p141);
                 c141.setHorizontalAlignment(Element.ALIGN_LEFT);
                //.setBackgroundColor(BaseColor.BLACK);
                 c141.setColspan(2);
                 table2.addCell(c141);
                 
                 Paragraph p151 = new Paragraph(en.getGuia().toString(),f3);
                 PdfPCell c151 = new PdfPCell(p151);
                 c151.setHorizontalAlignment(Element.ALIGN_LEFT);
                // c15.setBackgroundColor(BaseColor.BLACK);
                 c151.setColspan(4);
                 table2.addCell(c151);
                 
                 Paragraph p172 = new Paragraph("Precio",f1);
                 PdfPCell c172 = new PdfPCell(p172);
                 c172.setHorizontalAlignment(Element.ALIGN_CENTER);
                //.setBackgroundColor(BaseColor.BLACK);
                 c172.setColspan(2);
                 table2.addCell(c172);
                 
                 Paragraph p171 = new Paragraph("Rastreo:",f1);
                 PdfPCell c171 = new PdfPCell(p171);
                 c171.setHorizontalAlignment(Element.ALIGN_LEFT);
                //.setBackgroundColor(BaseColor.BLACK);
                 c171.setColspan(2);
                 table2.addCell(c171);
                 
                 Paragraph p161 = new Paragraph(en.getRastreo().toString(),f3);
                 PdfPCell c161 = new PdfPCell(p161);
                 c161.setHorizontalAlignment(Element.ALIGN_LEFT);
                // c15.setBackgroundColor(BaseColor.BLACK);
                 c161.setColspan(4);
                 table2.addCell(c161);
                
                
                 
                 Paragraph p162 = new Paragraph("$ "+en.getPrecio().toString(),f3);
                 PdfPCell c162 = new PdfPCell(p162);
                 c162.setHorizontalAlignment(Element.ALIGN_CENTER);
                // c15.setBackgroundColor(BaseColor.BLACK);
                 c162.setColspan(2);
                 table2.addCell(c162);
                 
                 table2.addCell(x1x);
            	
            }
            
            	
         }      
           // table2.addCell(x1x);
            
            Paragraph vacia = new Paragraph("",f1);
            PdfPCell cv= new PdfPCell(vacia);
            cv.setHorizontalAlignment(Element.ALIGN_LEFT);
            //.setBackgroundColor(BaseColor.BLACK);
            cv.setBorder(Rectangle.NO_BORDER);
            cv.setColspan(4);
            
            table2.addCell(cv);table2.addCell(cv);
            
            Paragraph p30 = new Paragraph("Total: $ "+v.getTotal().toString(),f2);
            PdfPCell c30= new PdfPCell(p30);
            c30.setHorizontalAlignment(Element.ALIGN_RIGHT);
           //.setBackgroundColor(BaseColor.BLACK);
            c30.setBorder(Rectangle.NO_BORDER);
            c30.setColspan(8);
            table2.addCell(c30);
                            
        	  String importeConLetra = NumberToLetterConverter.convertNumberToLetter(v.getTotal(), "MXN");
            Paragraph p32 = new Paragraph(importeConLetra,f3);
            PdfPCell c32 = new PdfPCell(p32);
            c32.setHorizontalAlignment(Element.ALIGN_CENTER);
            // c15.setBackgroundColor(BaseColor.BLACK);
            c32.setColspan(8);
            table2.addCell(c32);
            
            Paragraph p33 = new Paragraph(condiciones,f4);
            PdfPCell c33 = new PdfPCell(p33);
            c33.setHorizontalAlignment(Element.ALIGN_LEFT);
            c33.setBorder(Rectangle.NO_BORDER);
           // c15.setBackgroundColor(BaseColor.BLACK);
            c33.setColspan(8);
            table2.addCell(c33);
            
            document.add(table2);
            document.close();
    	    System.out.println("Your PDF file has been generated!(¡Se ha generado tu hoja PDF!");
    	} catch (DocumentException documentException) {
    	    System.out.println("The file not exists (Se ha producido un error al generar un documento): " + documentException);
    	}
    }

      
//      public void GeneraComDisPdf(DetalleDiscrepanciaVo det, Document document) throws DocumentException {
//          // Aquí introduciremos el código para crear el PDF.      	  
//      
//    	  	PdfPTable table2 = new PdfPTable(12);      
//    	    Font fuente = new Font();
//	  	    fuente.setStyle(1);
//	  	    fuente.setSize(10);
//	  	    fuente.setStyle(Font.BOLD);
//	  	    Font f1 = new Font();
//	  	    f1.setStyle(2);
//	  	    f1.setSize(9);
//	  	    Font f2 = new Font();
//	  	    f2.setStyle(3);
//	  	    f2.setSize(8);
//            Paragraph d = new Paragraph("Logo");
//            PdfPCell c0 = new PdfPCell(d);
//            c0.setHorizontalAlignment(Element.ALIGN_LEFT);
//            c0.setColspan(12);
//            table2.addCell(c0);
//            Paragraph a = new Paragraph("CROSS AIR SERVICES, S.A. DE C.V.",f1);
//            PdfPCell celdaFinal = new PdfPCell(a);
//            celdaFinal.setHorizontalAlignment(Element.ALIGN_CENTER);             
//            celdaFinal.setColspan(12);
//            table2.addCell(celdaFinal);
//            Paragraph b =new Paragraph("DISCREPANCY REPORT / REPORTE DE DISCREPANCIAS: \n",fuente);
//            PdfPCell c2 =new PdfPCell(b);
//            c2.setHorizontalAlignment(Element.ALIGN_CENTER);             
//            c2.setColspan(12);
//            table2.addCell(c2);
//          
//            Paragraph p2=new Paragraph("CUSTOMER / CLIENTE:"+det.getNombreEmpresa() ,f1);
//            PdfPCell c3 =new PdfPCell(p2);
//            c3.setHorizontalAlignment(Element.ALIGN_CENTER);             
//            c3.setColspan(12);
//            table2.addCell(c3);
//            
//            Paragraph p3=new Paragraph("MARK/MODEL - MARCA/MODELO:\n\n"+det.getModelo() ,f1);
//            PdfPCell c4 =new PdfPCell(p3);
//            c4.setHorizontalAlignment(Element.ALIGN_CENTER);             
//            c4.setColspan(3);
//            table2.addCell(c4);
//            
//            Paragraph p4=new Paragraph("N/S:\n\n"+det.getNoSerie() ,f1);
//            PdfPCell c5 =new PdfPCell(p4);
//            c5.setHorizontalAlignment(Element.ALIGN_CENTER);             
//            c5.setColspan(3);
//            table2.addCell(c5);
//            
//            Paragraph p5=new Paragraph("REG. / MATRICULA:\n\n"+det.getMatricula() ,f1);
//            PdfPCell c6 =new PdfPCell(p5);
//            c6.setHorizontalAlignment(Element.ALIGN_CENTER);             
//            c6.setColspan(3);
//            table2.addCell(c6);
//            
//            Paragraph p6=new Paragraph("O.T. #:\n\n"+det.getMatricula() ,f1);
//            PdfPCell c7 =new PdfPCell(p6);
//            c7.setHorizontalAlignment(Element.ALIGN_CENTER);             
//            c7.setColspan(3);
//            table2.addCell(c7);
//            
//            Paragraph p7=new Paragraph("DISCREPANCIA No:"+d.lastIndexOf(det) ,f1);
//            PdfPCell c8 =new PdfPCell(p7);
//            c8.setHorizontalAlignment(Element.ALIGN_LEFT);             
//            c8.setColspan(6);
//            table2.addCell(c8);
//            
//            Paragraph p8=new Paragraph("DATE / FECHA:"+det.getFechaOrden(),f1);
//            PdfPCell c9 =new PdfPCell(p8);
//            c9.setHorizontalAlignment(Element.ALIGN_LEFT);             
//            c9.setColspan(6);
//            table2.addCell(c9);
//            
//            Paragraph p9=new Paragraph("DISCREPANCY / DISCREPANCIA:",f1);
//            PdfPCell c10 =new PdfPCell(p9);
//            c10.setHorizontalAlignment(Element.ALIGN_LEFT);             
//            c10.setColspan(6);
//            table2.addCell(c10);
//            
//            Paragraph p10=new Paragraph("CORRECT ACCTION / ACCION CORRECTIVA:",f1);
//            PdfPCell c11 =new PdfPCell(p10);
//            c11.setHorizontalAlignment(Element.ALIGN_LEFT);             
//            c11.setColspan(6);
//            table2.addCell(c11);
//            
//            Paragraph p11=new Paragraph("\n"+det.getDescripcion()+"\n",fuente);
//            PdfPCell c12 =new PdfPCell(p11);
//            c12.setHorizontalAlignment(Element.ALIGN_CENTER);             
//            c12.setColspan(6);
//            table2.addCell(c12);
//            
//            Paragraph p12=new Paragraph("\n"+det.getAccion()+"\n",fuente);
//            PdfPCell c13 =new PdfPCell(p12);
//            c13.setHorizontalAlignment(Element.ALIGN_CENTER);             
//            c13.setColspan(6);
//            table2.addCell(c13);
//            
//            Paragraph p13=new Paragraph("\n",f1); ////celda invisible
//            PdfPCell c14 =new PdfPCell(p13);
//            c14.setHorizontalAlignment(Element.ALIGN_CENTER);             
//            c14.setColspan(6);
//            table2.addCell(c14);
//            
//            Paragraph p14=new Paragraph("MAN TIME HOURS / TIEMPO HORAS HOMBRE:",f1); 
//            PdfPCell c15 =new PdfPCell(p14);
//            c15.setHorizontalAlignment(Element.ALIGN_LEFT);             
//            c15.setColspan(6);
//            table2.addCell(c15);
//            
//            Paragraph p15=new Paragraph("DESCRIPTION / DESCRIPCION:",f1); 
//            PdfPCell c16 =new PdfPCell(p15);
//            c16.setHorizontalAlignment(Element.ALIGN_CENTER);             
//            c16.setColspan(2);
//            table2.addCell(c16);
//            
//            Paragraph p16=new Paragraph("PART NUMBER / NUMERO DE PARTE:",f1); 
//            PdfPCell c17=new PdfPCell(p16);
//            c17.setHorizontalAlignment(Element.ALIGN_CENTER);             
//            c17.setColspan(2);
//            table2.addCell(c17);
//            
//            Paragraph p17=new Paragraph("QUANTITY / CANTIDAD:",f1); 
//            PdfPCell c18=new PdfPCell(p17);
//            c18.setHorizontalAlignment(Element.ALIGN_CENTER);             
//            c18.setColspan(2);
//            table2.addCell(c18);
//            
//            Paragraph p18=new Paragraph("N/S REMOVABLE / N/S REMOVIDA:",f1); 
//            PdfPCell c19=new PdfPCell(p18);
//            c19.setHorizontalAlignment(Element.ALIGN_CENTER);             
//            c19.setColspan(2);
//            table2.addCell(c19);
//            
//            Paragraph p19=new Paragraph("N/S INSTALL / N/S INSTALADA:",f1); 
//            PdfPCell c20=new PdfPCell(p19);
//            c20.setHorizontalAlignment(Element.ALIGN_CENTER);             
//            c20.setColspan(2);
//            table2.addCell(c20);
//            
//            Paragraph p20=new Paragraph("OSERVATION/OBSERVATIONS:",f1); 
//            PdfPCell c21=new PdfPCell(p20);
//            c21.setHorizontalAlignment(Element.ALIGN_CENTER);             
//            c21.setColspan(2);
//            table2.addCell(c21);
//            
//            List<ComDisVo> cds = det.getComponentes();
//            System.out.println("cds:"+cds);
//            if (cds.isEmpty()){
//            	
//	            Paragraph pp=new Paragraph("\n",f1); 
//	            PdfPCell cp=new PdfPCell(pp);
//	            cp.setHorizontalAlignment(Element.ALIGN_CENTER);             
//	            cp.setColspan(2);
//	            table2.addCell(cp);table2.addCell(cp);table2.addCell(cp);table2.addCell(cp);table2.addCell(cp);table2.addCell(cp);
//	            table2.addCell(cp);table2.addCell(cp);table2.addCell(cp);table2.addCell(cp);table2.addCell(cp);table2.addCell(cp);
//	            table2.addCell(cp);table2.addCell(cp);table2.addCell(cp);table2.addCell(cp);table2.addCell(cp);table2.addCell(cp);
//	            table2.addCell(cp);table2.addCell(cp);table2.addCell(cp);table2.addCell(cp);table2.addCell(cp);table2.addCell(cp);
//            }
//            for (ComDisVo cd : cds){
//            	
//            	System.out.println("cds. nombre:"+cd.getNombre_componente());
//	            Paragraph p21=new Paragraph(cd.getNombre_componente(),f1); 
//	            PdfPCell c22=new PdfPCell(p21);
//	            c22.setHorizontalAlignment(Element.ALIGN_CENTER);             
//	            c22.setColspan(2);
//	            table2.addCell(c22);
//	            
//	            System.out.println("cds. parte:"+cd.getNoParte());
//	            Paragraph p22=new Paragraph(cd.getNoParte(),f1); 
//	            PdfPCell c23=new PdfPCell(p22);
//	            c23.setHorizontalAlignment(Element.ALIGN_CENTER);             
//	            c23.setColspan(2);
//	            table2.addCell(c23);
//	            
//	            Paragraph p23=new Paragraph(cd.getCantidad().toString(),f1); 
//	            PdfPCell c24=new PdfPCell(p23);
//	            c24.setHorizontalAlignment(Element.ALIGN_CENTER);             
//	            c24.setColspan(2);
//	            table2.addCell(c24);
//	            
//	            Paragraph p24=new Paragraph("N/A",f1); 
//	            PdfPCell c25=new PdfPCell(p24);
//	            c25.setHorizontalAlignment(Element.ALIGN_CENTER);             
//	            c25.setColspan(2);
//	            table2.addCell(c25);
//	            table2.addCell(c25);
//	            
//	            Paragraph p25=new Paragraph("las observaciones de horas hombre",f1); 
//	            PdfPCell c26=new PdfPCell(p25);
//	            c26.setHorizontalAlignment(Element.ALIGN_CENTER);             
//	            c26.setColspan(2);
//	            table2.addCell(c26);
//	            
//	            
//            
//            }
//            /////////////////////////validar cuantos comps vienen para poner los demas celdas vacias
//           // for (){
//            	
//          //  }
//            Paragraph p26=new Paragraph("REMOVIBLE BY/ REMOVIDO POR:\n",f1); 
//            PdfPCell c27=new PdfPCell(p26);
//            c27.setHorizontalAlignment(Element.ALIGN_LEFT);             
//            c27.setColspan(6);
//            table2.addCell(c27);
//            
//            Paragraph p27=new Paragraph("INSTALL BY/ INSTALADA POR:\n",f1); 
//            PdfPCell c28=new PdfPCell(p27);
//            c28.setHorizontalAlignment(Element.ALIGN_LEFT);             
//            c28.setColspan(6);
//            table2.addCell(c28);
//            
//            Paragraph p28=new Paragraph("SIGN / FIRMA:\n\n",f1); 
//            PdfPCell c29=new PdfPCell(p28);
//            c29.setHorizontalAlignment(Element.ALIGN_LEFT);             
//            c29.setColspan(6);
//            table2.addCell(c29);
//            table2.addCell(c29);
//            
//            Paragraph p29=new Paragraph("PERMISSION / LICENCIA:\n",f1); 
//            PdfPCell c30=new PdfPCell(p29);
//            c30.setHorizontalAlignment(Element.ALIGN_LEFT);             
//            c30.setColspan(6);
//            table2.addCell(c30);
//            table2.addCell(c30);
//            
//            Paragraph p30=new Paragraph("DATE / FECHA:\n",f1); 
//            PdfPCell c31=new PdfPCell(p30);
//            c31.setHorizontalAlignment(Element.ALIGN_LEFT);             
//            c31.setColspan(6);
//            table2.addCell(c31);
//            table2.addCell(c31);
//            
//            Paragraph p31=new Paragraph("CUSTOMER AUTORIZATION / AUTORIZADO POR EL CLIENTE:\n\n NAME / NOMBRE:\n\n SIGN / FIRMA:\n\n",f1); 
//            PdfPCell c32=new PdfPCell(p31);
//            c32.setHorizontalAlignment(Element.ALIGN_LEFT);             
//            c32.setColspan(12);
//            table2.addCell(c32);
//            
//            Paragraph p32=new Paragraph("                                  FORM AUTORIZATION / MEDIO DE AUTORIZACION:\n PERSONAL:\n\n BY TELEFON / VIA TELEFONICA:\n\n "
//            		+ "E-MAIL / CORREO ELECTRONICO:\n\n  HOUR / DATE / HORA Y FECHA \n\n",f1); 
//            PdfPCell c33=new PdfPCell(p32);
//            c33.setHorizontalAlignment(Element.ALIGN_LEFT);             
//            c33.setColspan(12);
//            table2.addCell(c33);
//            
//            Paragraph p33=new Paragraph("\n",f1); 
//            PdfPCell c34=new PdfPCell(p33);
//            c34.setHorizontalAlignment(Element.ALIGN_LEFT);             
//            c34.setColspan(6);
//            table2.addCell(c34);
//            
//            Paragraph p34=new Paragraph("Vo Bo INSPECTOR\n\n",f1); 
//            PdfPCell c35=new PdfPCell(p34);
//            c35.setHorizontalAlignment(Element.ALIGN_LEFT);             
//            c35.setColspan(6);
//            table2.addCell(c35);
//            
//            
//            document.add(table2);  
//            document.add(new Paragraph("\n\n\n"));
//            
//      }

      
     


	public String letras(Double number){
    	  Integer entero = number.intValue();//.longValue();
    	  double decimales = number - entero;
    	  
    	  
    	  StringBuffer resultado = new StringBuffer(); 
    	  String strEntero = letras(entero.doubleValue()); 
    	
    	  String strDecimales = letras(decimales); 
    	  resultado.append(strEntero); 
    	  resultado.append(" con "); 
    	  resultado.append(strDecimales); 
    	  return resultado.toString(); 
    	  
    	  //return "si";
      }
	
	
}

