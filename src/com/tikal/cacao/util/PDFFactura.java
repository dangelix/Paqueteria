package com.tikal.cacao.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.ExtendedColor;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.tikal.cacao.factura.Estatus;
import com.tikal.mensajeria.modelo.entity.Factura;
import com.tikal.cacao.model.Imagen;
import com.tikal.cacao.sat.cfd.Comprobante;
import com.tikal.cacao.sat.cfd.Comprobante.Conceptos.Concepto;
import com.tikal.cacao.sat.cfd.Comprobante.Impuestos.Retenciones;
import com.tikal.cacao.sat.cfd.Comprobante.Impuestos.Retenciones.Retencion;
import com.tikal.cacao.sat.cfd.Comprobante.Impuestos.Traslados.Traslado;
import com.tikal.cacao.sat.timbrefiscaldigital.TimbreFiscalDigital;

/**
 * @author Tikal
 *
 */
public class PDFFactura {

	private Document document;
	private static String IVA = "IVA";
	private static String IEPS = "IEPS";
	private static String ISR = "ISR";
	private NumberFormat formatter = NumberFormat.getCurrencyInstance();
	private MyFooter pieDePagina = new MyFooter(null);
	
	private Font fontTituloSellos = new Font(Font.FontFamily.HELVETICA, 7.5F, Font.BOLD);
	private Font fontContenidoSellos = new Font(Font.FontFamily.COURIER, 7.5F, Font.NORMAL);
	private Font fontLeyendaFiscal = new Font(Font.FontFamily.HELVETICA, 7.5F, Font.NORMAL);
	private Font font2 = new Font(Font.FontFamily.HELVETICA, 8.5F, Font.BOLD);
	private Font font3 = new Font(Font.FontFamily.HELVETICA, 8.5F, Font.NORMAL);
	private Font fontSerieYFolio = new Font(Font.FontFamily.HELVETICA, 9.5F, Font.BOLD, BaseColor.RED);
	private Font fontHeadFactura = new Font(Font.FontFamily.HELVETICA, 9.5F, Font.BOLD);
	
	private Font fontHead = new Font(Font.FontFamily.HELVETICA, 8.5F, Font.NORMAL);
	private BaseColor tikalColor;
	//fontHead.setColor(BaseColor.GREEN);

	private PdfPCell emptyCell = new PdfPCell();
	private PdfPCell celdaEspacio = new PdfPCell();
	//emptyCell.setBorderWidth(0);

	public PDFFactura() {
		fontHead.setColor(BaseColor.WHITE);
		emptyCell.setBorderWidth(1);
		emptyCell.setBorderColor(BaseColor.GRAY);
		tikalColor = new CustomColor(ExtendedColor.TYPE_RGB, 50F / 255F, 173F / 255F, 16F / 255F);
		
		celdaEspacio.setBorder(PdfPCell.NO_BORDER);
		//celdaEspacio.addElement(Chunk.NEWLINE);
		
		this.document = new Document();
		this.document.setPageSize(PageSize.A4);
		this.document.setMargins(40, 40, 40, 40); // Left Right Top Bottom
	}

	public Document getDocument() {
		return document;
	}

	public MyFooter getPieDePagina() {
		return pieDePagina;
	}
	
	/**
	 * Crea un archivo PDF formado por todas las facturas {@code Factura} que estan en la lista
	 * especificada
	 * @param facturas la lista con las facturas
	 */
	public void construirPDFMasivo(List<Factura> facturas, Imagen imagen) {
		
	}

	/**
	 * Construye y regresa un objeto {@code Document} que se convertir&aacute;
	 * la representaci&oacute;n impresa de un CFDI
	 * 
	 * @param comprobante
	 *            el CFDI con los datos con los que se construira el documento
	 * @param selloDigital
	 *            el sello digital del CFDI
	 * @param bytesQRCode
	 *            el arreglo de bytes que se convertir&aacute; en la imagen del
	 *            c&oacute;digo QR que se agregar&aacute; al documento
	 * @return un objeto {@code Document} que se convertir&aacute; la
	 *         representaci&oacute;n impresa de un CFDI
	 * @throws DocumentException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public Document construirPdf(Comprobante comprobante, String selloDigital, byte[] bytesQRCode, Imagen imagen, Estatus estatus)
			throws DocumentException, MalformedURLException, IOException {
		List<Object> complemento = comprobante.getComplemento().getAny();
		TimbreFiscalDigital tfd = null;
		if (complemento.size() > 0) {
			for (Object object : complemento) {
				if (object instanceof TimbreFiscalDigital) {
					tfd = (TimbreFiscalDigital) object;
				}
			}
		}
		
		construirBoceto(comprobante, imagen, estatus, tfd);

		construirTimbre(selloDigital, bytesQRCode, tfd);
		
		construirHechoPor();
		
		return document;
	}

	public Document construirPdf(Comprobante comprobante, Imagen imagen, Estatus estatus)
			throws DocumentException, MalformedURLException, IOException {

		construirBoceto(comprobante, imagen, estatus, null);
		construirHechoPor();
		return document;
	}
	
	public Document construirPdfCancelado(Comprobante comprobante, String selloDigital,
			byte[] bytesQRCode, Imagen imagen, Estatus estatus, String selloCancelacion, Date fechaCancelacion) throws MalformedURLException, DocumentException, IOException {
		//this.document = construirPdf(comprobante, selloDigital, bytesQRCode, imagen, estatus);
		List<Object> complemento = comprobante.getComplemento().getAny();
		TimbreFiscalDigital tfd = null;
		if (complemento.size() > 0) {
			for (Object object : complemento) {
				if (object instanceof TimbreFiscalDigital) {
					tfd = (TimbreFiscalDigital) object;
				}
			}
		}
		
		construirBoceto(comprobante, imagen, estatus, tfd);
		construirTimbre(selloDigital, bytesQRCode, tfd);
		construirHechoPor();
		return document;
	}

	private void agregarCelda(String contenidoCelda, Font fuente, PdfPTable tabla, boolean centrado) {
		PdfPCell celda = new PdfPCell(new Paragraph(contenidoCelda, fuente));
		celda.setBorderWidth(1);
		celda.setBorderColor(BaseColor.GRAY);
		celda.setPadding(5);

		if (centrado)
			celda.setHorizontalAlignment(Element.ALIGN_CENTER);

		tabla.addCell(celda);
	}

	private void agregarCeldaConFondo(String contenidoCelda, Font fuente, PdfPTable tabla, boolean centrado) {
		PdfPCell celda = new PdfPCell(new Paragraph(contenidoCelda, fuente));
		celda.setBorderWidth(1);
		celda.setBorderColor(BaseColor.GRAY);
		celda.setPadding(5);
		celda.setBackgroundColor(tikalColor);

		if (centrado)
			celda.setHorizontalAlignment(Element.ALIGN_CENTER);

		tabla.addCell(celda);
	}
	

	private void agregarCeldaSinBorde(String contenidoCelda, Font fuente, PdfPTable tabla, boolean centrado) {
		PdfPCell celda = new PdfPCell(new Paragraph(contenidoCelda, fuente));
		celda.setBorder(PdfPCell.NO_BORDER);
		celda.setBorderColor(BaseColor.GRAY);
		celda.setPadding(8);

		if (centrado)
			celda.setHorizontalAlignment(Element.ALIGN_CENTER);

		tabla.addCell(celda);
	}

	private void agregarChunkYNuevaLinea(String contenido, Font fuente, Phrase frase) {
		Chunk chunk = new Chunk(contenido, fuente);
		frase.add(chunk);
		frase.add(Chunk.NEWLINE);
	}

	private List<Traslado> obtenerTraslados(Comprobante c) {
		if (c.getImpuestos() != null) {
			if (c.getImpuestos().getTraslados() != null) {
				List<Traslado> traslados = c.getImpuestos().getTraslados().getTraslado();
				if (traslados.size() > 0)
					return traslados;
				else
					return null;
			} else
				return null;
		} else
			return null;
	}

	private List<Retencion> obtenerRetenciones(Comprobante c) {
		if (c.getImpuestos() != null) {
			if (c.getImpuestos().getRetenciones() != null) {
				List<Retencion> retenciones = c.getImpuestos().getRetenciones().getRetencion();
				if (retenciones.size() > 0)
					return retenciones;
				else
					return null;
			} else
				return null;
		} else
			return null;
	}

	private void agregarDetalleImpuestosTraslados(List<Traslado> impTraslados, PdfPTable tabla, Font font) {
		if (impTraslados != null) {
			double importeIVA = 0.0;
			double tasaIVA = 0.0;

			for (Traslado traslado : impTraslados) {
				if (traslado.getImpuesto().equals(IVA)) {
					if (traslado.getTasa().doubleValue() > 0)
						tasaIVA = traslado.getTasa().doubleValue();
					importeIVA += traslado.getImporte().doubleValue();
				} else if (traslado.getImpuesto().equals(IEPS)) {
					agregarCelda(IEPS.concat("  ").concat(Double.toString(traslado.getTasa().doubleValue())), font,
							tabla, false);
					agregarCelda(formatter.format(traslado.getImporte().doubleValue()), font, tabla, false);
				}
			}
			if (importeIVA > 0.0) {
				agregarCelda(IVA.concat("  ").concat(String.valueOf(tasaIVA)), font, tabla, false);
				agregarCelda(formatter.format(importeIVA), font, tabla, false); // se
																				// quitaron
																				// dos
																				// espacios
																				// en
																				// blanco
																				// al
																				// inicio
																				// de
																				// la
																				// cadena
			}
		}
	}

	/*private void agregarDetalleImpuestosRetenciones(List<Retencion> impRetenidos, PdfPTable tabla, Font font) {
		if (impRetenidos != null) {

			for (Retencion retencion : impRetenidos) {
				if (retencion.getImpuesto().equals(IVA)) {
					agregarCelda(IVA.concat("  "), font, tabla, false);
					agregarCelda(formatter.format(retencion.getImporte().doubleValue()), font, tabla, false);
				} else if (retencion.getImpuesto().equals(ISR)) {
					agregarCelda(ISR.concat("  "), font, tabla, false);
					agregarCelda(formatter.format(retencion.getImporte().doubleValue()), font, tabla, false);
				}
			}

		}
	}*/
	
	private boolean agregarEtiquetaRetenciones(Retenciones retenciones, PdfPTable subTablaEtqTotal) {
		boolean existeISR = false;
		if (retenciones != null) {
			List<Retencion> listaRetencion = retenciones.getRetencion();
			//double importe = 0.0;
			if (listaRetencion.size() > 0) {
				if (listaRetencion.get(0).getImpuesto().equals(ISR)) {
					existeISR = true;
					//importe = listaRetencion.get(0).getImporte().doubleValue();
					agregarCeldaConFondo("ISR Retenido", fontHead, subTablaEtqTotal, true);
				}
			} /*else {
				subTablaEtqTotal.addCell(emptyCell);
			}*/
		}
		return existeISR;
	}
	

	private String getFolioYSerie(Comprobante c) {
		String folio = c.getFolio();
		String serie = c.getSerie();
		StringBuilder folioYSerie = new StringBuilder();
		if (serie != null)
			folioYSerie.append(serie);
		if (folio != null)
			folioYSerie.append(folio);

		return folioYSerie.toString();
	}
	
	public void crearMarcaDeAgua(String contenido, PdfWriter writer) {
		PdfContentByte fondo = writer.getDirectContent();
		Font fuente = new Font(FontFamily.HELVETICA, 45);
		Phrase frase = new Phrase(contenido, fuente);
		fondo.saveState();
		PdfGState gs1 = new PdfGState();
		gs1.setFillOpacity(0.5f);
		fondo.setGState(gs1);
		ColumnText.showTextAligned(fondo, Element.ALIGN_CENTER, frase, 297, 650, 45);
		fondo.restoreState();
	}
	
	private void construirBoceto(Comprobante comprobante, Imagen imagen, Estatus estatus, TimbreFiscalDigital tfd) throws DocumentException, MalformedURLException, IOException {

		PdfPTable tablaEncabezado = new PdfPTable(3);
		tablaEncabezado.setWidthPercentage(100);
		tablaEncabezado.setWidths(new float[] { 30, 40, 30 });

		// ENCABEZADO DEL COMPROBANTE
		PdfPTable subTablaLogo = new PdfPTable(1);
		PdfPCell celdaLogo = new PdfPCell();
		celdaLogo.setBorder(PdfPCell.NO_BORDER);
		Image imgLogo;
//		if (imagen != null) {
//			imgLogo = Image.getInstance(new URL(imagen.getImage()));
//		} else {
			imgLogo = Image.getInstance("img/LogoMervel.png");
			imgLogo.setScaleToFitHeight(false);
			imgLogo.scaleToFit(125F, 37.25F);
		//}
		Chunk chunkLogo = new Chunk(imgLogo, 0, -35);
		celdaLogo.addElement(chunkLogo);
		subTablaLogo.addCell(celdaLogo);
		PdfPCell celdaTablaLogo = new PdfPCell();
		celdaTablaLogo.addElement(subTablaLogo);
		celdaTablaLogo.disableBorderSide(PdfPCell.RIGHT);
		celdaTablaLogo.setBorderColor(BaseColor.GRAY);
		celdaTablaLogo.setBorderWidth(1);
		tablaEncabezado.addCell(celdaTablaLogo);

		PdfPCell celdaDatosEmisor = new PdfPCell();
		Phrase fraseDatosEmisor = new Phrase();
		Chunk chunkNombreEmisor = new Chunk("", font2);
		if(comprobante.getEmisor().getNombre()!=null){
			chunkNombreEmisor = new Chunk(comprobante.getEmisor().getNombre(), font2);
		}
		Chunk chunkRFCEmisor = new Chunk("R.F.C. ".concat(comprobante.getEmisor().getRfc()), font3);
		Chunk chunkDomicilioEmisor = new Chunk(comprobante.getEmisor().getDomicilioFiscal().toString(), font3);
		fraseDatosEmisor.add(chunkNombreEmisor);
		fraseDatosEmisor.add(Chunk.NEWLINE);
		fraseDatosEmisor.add(chunkRFCEmisor);
		fraseDatosEmisor.add(Chunk.NEWLINE);
		fraseDatosEmisor.add(chunkDomicilioEmisor);
		celdaDatosEmisor.setBorderWidth(1);
		celdaDatosEmisor.disableBorderSide(PdfPCell.LEFT);
		celdaDatosEmisor.addElement(fraseDatosEmisor);
		celdaDatosEmisor.setHorizontalAlignment(Element.ALIGN_CENTER);
		celdaDatosEmisor.setBorderColor(BaseColor.GRAY);
		tablaEncabezado.addCell(celdaDatosEmisor);

		PdfPCell celdaSubTablaEncabezado = new PdfPCell();
		celdaSubTablaEncabezado.setBorderWidth(1);
		PdfPTable subTablaEncabezado = new PdfPTable(1);
		agregarCeldaSinBorde("FACTURA", fontHeadFactura, subTablaEncabezado, true);
		subTablaEncabezado.addCell(celdaEspacio);
		agregarCeldaSinBorde(getFolioYSerie(comprobante), fontSerieYFolio, subTablaEncabezado, true);
		celdaSubTablaEncabezado.addElement(subTablaEncabezado);
		celdaSubTablaEncabezado.setBorderColor(BaseColor.GRAY);
		tablaEncabezado.addCell(celdaSubTablaEncabezado);
		document.add(tablaEncabezado);

		// INFORMACI�N DEL RECEPTOR, LUGAR Y FECHA DE EMISI�N / HORA DE
		// CERTIFICACI�N
		PdfPTable tablaReceptorYHoraCert = new PdfPTable(3);
		tablaReceptorYHoraCert.setWidthPercentage(100);
		tablaReceptorYHoraCert.setWidths(new float[] { 35, 20, 45 });

		agregarCeldaConFondo("Nombre o razón social", fontHead, tablaReceptorYHoraCert, false);
		agregarCeldaConFondo("R.F.C.", fontHead, tablaReceptorYHoraCert, false);
		agregarCeldaConFondo("Lugar y fecha de emisión / hora de certificación", fontHead, tablaReceptorYHoraCert,
				false);

		agregarCelda(comprobante.getReceptor().getNombre(), font3, tablaReceptorYHoraCert, false);
		agregarCelda(comprobante.getReceptor().getRfc(), font3, tablaReceptorYHoraCert, false);
		
		String lugarFechaEmiHoraCert = "";
		if (estatus.equals(Estatus.TIMBRADO) || estatus.equals(Estatus.CANCELADO))
			lugarFechaEmiHoraCert = comprobante.getLugarExpedicion().concat(" a ").concat(comprobante.getFecha().toString().concat(" / ").concat(tfd.getFechaTimbrado().toString()));
		else if (estatus.equals(Estatus.GENERADO))
			lugarFechaEmiHoraCert = comprobante.getLugarExpedicion().concat(" a ").concat(comprobante.getFecha().toString());
		agregarCelda(lugarFechaEmiHoraCert, font3, tablaReceptorYHoraCert, false);

		document.add(tablaReceptorYHoraCert);

		// DIRECCI�N Y OTROS DATOS FISCALES
		PdfPTable tablaDirYOtrosDatosFis = new PdfPTable(2);
		tablaDirYOtrosDatosFis.setWidthPercentage(100);
		tablaDirYOtrosDatosFis.setWidths(new float[] { 40, 60 });

		agregarCeldaConFondo("Dirección", fontHead, tablaDirYOtrosDatosFis, false);
		agregarCeldaConFondo("Otros datos fiscales", fontHead, tablaDirYOtrosDatosFis, false);

		agregarCelda(comprobante.getReceptor().getDomicilio().toString(), font3, tablaDirYOtrosDatosFis, false);

		Phrase fraseDatosFiscales = new Phrase();
		if (estatus.equals(Estatus.TIMBRADO)) { //TODO agregar caso de cancelado
			 agregarChunkYNuevaLinea("Folio fiscal: ".concat(tfd.getUUID()),
			 font3, fraseDatosFiscales);
			 agregarChunkYNuevaLinea("Serie del certificado de emisor:".concat(comprobante.getNoCertificado()), font3, fraseDatosFiscales);
			 agregarChunkYNuevaLinea("Serie del certificado del SAT:".concat(tfd.getNoCertificadoSAT()), font3, fraseDatosFiscales);
		}
		
		agregarChunkYNuevaLinea(
				"R�gimen fiscal: ".concat(comprobante.getEmisor().getRegimenFiscal().get(0).getRegimen()), font3,
				fraseDatosFiscales);

		PdfPCell celdaDatosFiscales = new PdfPCell();
		celdaDatosFiscales.setMinimumHeight(45);
		celdaDatosFiscales.setPhrase(fraseDatosFiscales);
		celdaDatosFiscales.setBorderColor(BaseColor.GRAY);
		celdaDatosFiscales.setBorderWidth(1);
		tablaDirYOtrosDatosFis.addCell(celdaDatosFiscales);
		document.add(tablaDirYOtrosDatosFis);

		// TABLA CONCEPTOS
		PdfPTable tablaConceptos = new PdfPTable(6);
		tablaConceptos.setWidthPercentage(100);
		tablaConceptos.setWidths(new float[] { 10, 10, 10, 40, 15, 15 });

		agregarCeldaConFondo("Clave", fontHead, tablaConceptos, true);

		agregarCeldaConFondo("Cantidad", fontHead, tablaConceptos, true);

		agregarCeldaConFondo("Unidad", fontHead, tablaConceptos, true);

		agregarCeldaConFondo("Descripción", fontHead, tablaConceptos, true);

		agregarCeldaConFondo("Valor unitario", fontHead, tablaConceptos, true);

		agregarCeldaConFondo("Importe", fontHead, tablaConceptos, true);

		List<Concepto> listaConceptos = comprobante.getConceptos().getConcepto();
		for (Concepto concepto : listaConceptos) {
			agregarCelda(concepto.getNoIdentificacion(), font3, tablaConceptos, true);
			agregarCelda(concepto.getCantidad().toString(), font3, tablaConceptos, true);
			agregarCelda(concepto.getUnidad(), font3, tablaConceptos, true);
			agregarCelda(concepto.getDescripcion(), font3, tablaConceptos, false);
			agregarCelda(formatter.format(concepto.getValorUnitario().doubleValue()), font3, tablaConceptos, true);
			agregarCelda(formatter.format(concepto.getImporte().doubleValue()), font3, tablaConceptos, true);
		}

		tablaConceptos.setSpacingBefore(5);
		tablaConceptos.setSpacingAfter(5);
		document.add(tablaConceptos);

		// IMPORTE CON LETRA
		PdfPTable tablaImporteConLetra = new PdfPTable(3);
		tablaImporteConLetra.setWidthPercentage(100);
		tablaImporteConLetra.setWidths(new int[] { 20, 65, 15 });
		agregarCeldaConFondo("Importe con letra ", fontHead, tablaImporteConLetra, true);

		double importeTotal = Math.round(comprobante.getTotal().doubleValue() * 100.0) / 100.0;
		String importeConLetra = NumberToLetterConverter.convertNumberToLetter(importeTotal, comprobante.getMoneda());
		Chunk chunkImporteConLetra = new Chunk(importeConLetra, font3);
		Phrase fraseImporteConLetra = new Phrase(chunkImporteConLetra);
		PdfPCell celdaImporteConLetra = new PdfPCell();
		// celdaImporteConLetra.setVerticalAlignment(Element.ALIGN_CENTER);
		celdaImporteConLetra.setBorder(PdfPCell.NO_BORDER);
		celdaImporteConLetra.setPhrase(fraseImporteConLetra);
		tablaImporteConLetra.addCell(celdaImporteConLetra);

		emptyCell.setBackgroundColor(tikalColor);
		tablaImporteConLetra.addCell(emptyCell);
		document.add(tablaImporteConLetra);

		// LEYENDA Y, SUBTOTAL, IVA Y TOTAL
		PdfPTable tablaLeyendaTotal = new PdfPTable(3);
		tablaLeyendaTotal.setWidthPercentage(100);
		tablaLeyendaTotal.setWidths(new float[] { 65, 20, 15 });

		Phrase fraseLeyenda = new Phrase();
		if ((comprobante.getCondicionesDePago() == null || comprobante.getNumCtaPago() == null)
				&& !estatus.equals(Estatus.GENERADO)) {
			fraseLeyenda.add(Chunk.NEWLINE);
		}
		if (!estatus.equals(Estatus.GENERADO)) {
			Chunk chunkLeyenda = new Chunk("Este documento es una representación impresa de un CFDI", fontTituloSellos);
			//fraseLeyenda.add(Chunk.NEWLINE);
			fraseLeyenda.add(chunkLeyenda);
		}
		// fraseLeyenda.add(Chunk.NEWLINE);

		fraseLeyenda.add(Chunk.NEWLINE);
		String strMetodoPago = "Método de pago: ".concat(comprobante.getMetodoDePago())
				.concat("                 Moneda: ").concat(comprobante.getMoneda());
		Chunk chunkMetodoDePago = new Chunk(strMetodoPago, fontLeyendaFiscal);
		fraseLeyenda.add(chunkMetodoDePago);

		// se agrega la leyenda 'Efectos fiscales al pago'
		String strFormaDePago = "Forma de pago: ".concat(comprobante.getFormaDePago())
				.concat("                 Efectos fiscales al pago");
		fraseLeyenda.add(Chunk.NEWLINE);
		Chunk chunkFormaDePago = new Chunk(strFormaDePago, fontLeyendaFiscal);
		fraseLeyenda.add(chunkFormaDePago);
		fraseLeyenda.add(Chunk.NEWLINE);
		
		String condicionesDePago = comprobante.getCondicionesDePago();
		if (condicionesDePago != null) {
			if ( !condicionesDePago.contentEquals("") ) {
				String strCondicionesDePago = "Condiciones de pago: ".
						concat(condicionesDePago).concat("      ");
				Chunk chunkCondicionesDePago = new Chunk(strCondicionesDePago, fontLeyendaFiscal);
				fraseLeyenda.add(chunkCondicionesDePago);
				fraseLeyenda.add(Chunk.NEWLINE);
			}
		}
		
		String numCtaPago = comprobante.getNumCtaPago();
		if (numCtaPago != null) {
			if ( !numCtaPago.contentEquals("") ) {
				String strNumCtaPago = "Número de cuenta de pago: ".
						concat(numCtaPago);
				Chunk chunkCondicionesDePago = new Chunk(strNumCtaPago, fontLeyendaFiscal);
				fraseLeyenda.add(chunkCondicionesDePago);
				fraseLeyenda.add(Chunk.NEWLINE);
			}
		}
		
		PdfPCell celdaLeyenda = new PdfPCell(fraseLeyenda);
		celdaLeyenda.setHorizontalAlignment(Element.ALIGN_CENTER);
		celdaLeyenda.setBorderColor(BaseColor.GRAY);
		celdaLeyenda.setBorderWidth(1);
		tablaLeyendaTotal.addCell(celdaLeyenda);

		PdfPTable subTablaEtqTotal = new PdfPTable(1);
		agregarCeldaConFondo("Subtotal", fontHead, subTablaEtqTotal, true);

		List<Traslado> traslados = comprobante.getImpuestos().getTraslados().getTraslado();
		boolean existeIVATraslado = false;
		double importe = 0.0;
		if (traslados.size() > 0) {
			if (traslados.get(0).getImpuesto().equals(IVA)) {
				existeIVATraslado = true;
				importe = traslados.get(0).getImporte().doubleValue();
				agregarCeldaConFondo("IVA 16%", fontHead, subTablaEtqTotal, true);
			}
		} else {
			subTablaEtqTotal.addCell(emptyCell);
		}
		
		boolean existeISR = agregarEtiquetaRetenciones(comprobante.getImpuestos().getRetenciones(), subTablaEtqTotal);

		agregarCeldaConFondo("Total", fontHead, subTablaEtqTotal, true);
		PdfPCell celdaTablaEtqTotal = new PdfPCell(subTablaEtqTotal);
		celdaTablaEtqTotal.setHorizontalAlignment(Element.ALIGN_CENTER);
		tablaLeyendaTotal.addCell(celdaTablaEtqTotal);

		PdfPTable subTablaValoresTotal = new PdfPTable(1);
		agregarCelda(formatter.format(comprobante.getSubTotal().doubleValue()), font3, subTablaValoresTotal, true);

		if (existeIVATraslado) {
			agregarCelda(formatter.format(importe), font3, subTablaValoresTotal, true);
		} else {
			subTablaValoresTotal.addCell(emptyCell);
		}
		
		if (existeISR) {
			List<Retencion> listaRetencion = comprobante.getImpuestos().getRetenciones().getRetencion();
			double importeISR = 0.0;
			for (Retencion retencion : listaRetencion) {
				if (retencion.getImpuesto().equals(ISR)) {
					importeISR = retencion.getImporte().doubleValue();
					break;
				}
			}
			
			agregarCelda(formatter.format(importeISR), font3, subTablaValoresTotal, true);
		}

		agregarCelda(formatter.format(comprobante.getTotal().doubleValue()), font3, subTablaValoresTotal, true);
		PdfPCell celdaTablaValoresTotal = new PdfPCell(subTablaValoresTotal);
		celdaTablaValoresTotal.setHorizontalAlignment(Element.ALIGN_CENTER);
		tablaLeyendaTotal.addCell(celdaTablaValoresTotal);

		document.add(tablaLeyendaTotal);
	}
	
	public void construirTimbre(String selloDigital, byte[] bytesQRCode, TimbreFiscalDigital tfd) throws DocumentException, MalformedURLException, IOException {
		// QRCode Y SELLOS DIGITALES
		PdfPTable mainTable = new PdfPTable(2);
		mainTable.setWidthPercentage(100); //antes 100.0f
		mainTable.setWidths(new float[] { 25, 75 });

		PdfPCell primeraCeldaTabla = new PdfPCell();
//		primeraCeldaTabla.setBorderWidthTop(1);
//		primeraCeldaTabla.setBorderWidthBottom(1);
		
//		primeraCeldaTabla.setBorderWidthLeft(1);
		primeraCeldaTabla.setBorderWidth(1);
		primeraCeldaTabla.disableBorderSide(PdfPCell.RIGHT);
		primeraCeldaTabla.setBorderColor(BaseColor.GRAY);

		PdfPTable tablaQRCode = new PdfPTable(1);
		Image imgQRCode = Image.getInstance(bytesQRCode);
		//imgQRCode.setAlignment(Image.MIDDLE);
		// int dpi = imgQRCode.getDpiX();
		// imgQRCode.scalePercent(100 * 72 / dpi - 20);
		
		
		// el tercer par�metro en el constructor de Chunk (offsetY) controla el
		// tama�o de la imagen
		Chunk chunkQRCode = null;
		PdfPCell celdaQRCode = new PdfPCell();
		boolean selloEmisorCorto = false;
		if (tfd.getSelloCFD().length() < 340) {
			selloEmisorCorto = true;
			chunkQRCode = new Chunk(imgQRCode, 5.0F, -75F);
		} else {
			chunkQRCode = new Chunk(imgQRCode, 0.5F, -78F);
		}
		
		celdaQRCode.setBorder(PdfPCell.NO_BORDER);
		
		if (selloEmisorCorto) {
			Phrase fraseQRCode = new Phrase();
			fraseQRCode.add(Chunk.NEWLINE);
			fraseQRCode.add(chunkQRCode);
			fraseQRCode.add(Chunk.NEWLINE);
			celdaQRCode.addElement(fraseQRCode);
		} else {
			celdaQRCode.addElement(Chunk.NEWLINE);
			celdaQRCode.addElement(chunkQRCode);
		}
		
		//celdaQRCode.setHorizontalAlignment(Element.ALIGN_CENTER);
		tablaQRCode.addCell(celdaQRCode);
		primeraCeldaTabla.addElement(tablaQRCode);
		mainTable.addCell(primeraCeldaTabla);

		PdfPCell segundaCeldaTabla = new PdfPCell();
//		segundaCeldaTabla.setBorderWidthTop(1);
//		segundaCeldaTabla.setBorderWidthBottom(1);
//		segundaCeldaTabla.setBorderWidthRight(1);
//		segundaCeldaTabla.setBorderWidthLeft(1);
		segundaCeldaTabla.setBorderWidth(1);
		segundaCeldaTabla.setBorderColor(BaseColor.GRAY);

		PdfPTable tablaTimbre = new PdfPTable(1);
		tablaTimbre.setWidthPercentage(100);

		// celdaQRCode = new PdfPCell(Image.getInstance(bytesQRCode));
		// celdaQRCode.setBorderWidth(0);

		PdfPCell cell1table7 = new PdfPCell();
		cell1table7.setBorderWidth(0);

		Phrase line3footer = new Phrase();
		Chunk line3part1 = new Chunk("Sello digital del emisor ", fontTituloSellos);
		Chunk line3part2 = new Chunk(tfd.getSelloCFD(), fontContenidoSellos);
		
		if (selloEmisorCorto) {
			line3footer.add(Chunk.NEWLINE);
		}
		
		line3footer.add(line3part1);
		line3footer.add(Chunk.NEWLINE);
		line3footer.add(line3part2);

		PdfPCell cell4table7 = new PdfPCell(line3footer);
		cell4table7.setBorderWidth(0);
		// cell4table7.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);

		Phrase line4footer = new Phrase();
		Chunk line4part1 = new Chunk("Sello digital del SAT ", fontTituloSellos);
		Chunk line4part2 = new Chunk(tfd.getSelloSAT(), fontContenidoSellos);
		line4footer.add(line4part1);
		line4footer.add(Chunk.NEWLINE);
		line4footer.add(line4part2);

		PdfPCell cell5table7 = new PdfPCell(line4footer);
		cell5table7.setBorderWidth(0);
		// cell5table7.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);

		Phrase fraseCadenaOriginal = new Phrase();
		Chunk chunkCadenaOriginalEtq = new Chunk("Cadena original del complemento de certificación digital del SAT ",
				fontTituloSellos);
		Chunk chunkCadenaOriginalValor = new Chunk(selloDigital, fontContenidoSellos);
		fraseCadenaOriginal.add(chunkCadenaOriginalEtq);
		fraseCadenaOriginal.add(Chunk.NEWLINE);
		fraseCadenaOriginal.add(chunkCadenaOriginalValor);
		if (selloEmisorCorto) {
			fraseCadenaOriginal.add(Chunk.NEWLINE);
			fraseCadenaOriginal.add(Chunk.NEWLINE);
		}

		PdfPCell celdaCadenaOriginal = new PdfPCell(fraseCadenaOriginal);
		celdaCadenaOriginal.setBorderWidth(0);

		tablaTimbre.addCell(cell4table7);

		tablaTimbre.addCell(cell5table7);

		tablaTimbre.addCell(celdaCadenaOriginal);

		// tablaTimbre.addCell(cell6table7);

		segundaCeldaTabla.addElement(tablaTimbre);
		mainTable.addCell(segundaCeldaTabla);
		//mainTable.setSpacingBefore(2);

		document.add(mainTable);
	}
	
	
	private void construirHechoPor() throws DocumentException {
		Font ffont = new Font(Font.FontFamily.HELVETICA, 8.5F, Font.NORMAL);
		Font ffontBold = new Font(Font.FontFamily.HELVETICA, 8.5F, Font.BOLD);
		
		Phrase footerTikal = new Phrase();
		Image imgTikal;
		Chunk chunkTikal;
		try {
			imgTikal = Image.getInstance("img/LogoMervel.png");
			imgTikal.setScaleToFitHeight(false);
			imgTikal.scaleToFit(25f, 7.45f);
			chunkTikal = new Chunk(imgTikal,0,0);
		} catch (BadElementException e) {
			chunkTikal = new Chunk();
			e.printStackTrace();
		} catch (MalformedURLException e) {
			chunkTikal = new Chunk();
			e.printStackTrace();
		} catch (IOException e) {
			chunkTikal = new Chunk();
			e.printStackTrace();
		}
		Chunk chunkHechoPor = new Chunk(" Hecho por ", ffont);
		
		Chunk chunkContacto = new Chunk(" Contacto: ", ffont);
		Chunk chunkEmail = new Chunk("info@tikal.mx ", ffontBold);
		Chunk chunkWeb = new Chunk(" /  www.tikal.mx", ffontBold);
		
		footerTikal.add(chunkHechoPor);
		footerTikal.add(chunkTikal);
		footerTikal.add(chunkContacto);
		footerTikal.add(chunkEmail);
		footerTikal.add(chunkWeb);
		
		PdfPCell celdaHechoPor = new PdfPCell(footerTikal);
		celdaHechoPor.setHorizontalAlignment(Element.ALIGN_CENTER);
		celdaHechoPor.setBorder(PdfPCell.NO_BORDER);
		
		PdfPTable tablaHechoPor = new PdfPTable(1);
		tablaHechoPor.setWidthPercentage(100);
		tablaHechoPor.addCell(celdaHechoPor);
		tablaHechoPor.setSpacingBefore(5.0f);
		
		document.add(tablaHechoPor);
	}

	public static class MyFooter extends PdfPageEventHelper {
		Font ffont = new Font(Font.FontFamily.HELVETICA, 8.5F, Font.NORMAL);
		Font ffontBold = new Font(Font.FontFamily.HELVETICA, 8.5F, Font.BOLD);
		private String uuid;
		private Date fechaCancel;
		private String selloCancel;
		
//		/**El template con el n&uacute;mero total de p&aacute;ginas*/
//		PdfTemplate total;
		
		/**  */
		private boolean masivo;
		
		/** N&uacute;mero de paginas que tendra el archivo PDF*/
		//private int numPags;
		
		/** N&uacute;mero de pagina actual del archivo PDF*/
		//private int numPaginaActual;

		public MyFooter(String uuid) {
			this.uuid = uuid;
		}

		public void setUuid(String uuid) {
			this.uuid = uuid;
		}
		
		public void setFechaCancel(Date fechaCancel) {
			this.fechaCancel = fechaCancel;
		}
		
		public void setSelloCancel(String selloCancel) {
			this.selloCancel = selloCancel;
		}
		
		public void setMasivo(boolean masivo) {
			this.masivo = masivo;
		}
		
		public boolean isMasivo() {
			return masivo;
		}
		
//		@Override
//		public void onOpenDocument(PdfWriter writer, Document document) {
//			super.onOpenDocument(writer, document);
//			total = writer.getDirectContent().createTemplate(30, 16);
//		}

		public void onEndPage(PdfWriter writer, Document document) {
			PdfContentByte cb = writer.getDirectContent();
			Phrase footer = new Phrase();
			Phrase footerFecha = new Phrase();
			Phrase footerSello = new Phrase();
			
			if (masivo) {
				Chunk chunkHojaNum = new Chunk("Hoja número ".concat(Integer.toString(1)).concat(" de ")
						.concat(Integer.toString(1)).concat(" del CFDI con UUID:").concat(uuid), ffont);
				footer.add(chunkHojaNum);
			} else {
				Chunk chunkHojaNum = new Chunk("Hoja número ".concat(Integer.toString(document.getPageNumber()))
						.concat(" del CFDI con UUID:").concat(uuid), ffont);
//				Chunk chunkHojaNum = new Chunk("Hoja n�mero ".concat(Integer.toString(document.getPageNumber())).concat(" de ")
//						.concat(Integer.toString(writer.getPageNumber())).concat(" del CFDI con UUID:").concat(uuid), ffont);
				footer.add(chunkHojaNum);
			}
			
			if (fechaCancel != null) {
				Chunk chunkFecha = new Chunk("Fecha de Cancelación: ".concat(fechaCancel.toString()), ffont);
				//footer.add(Chunk.NEWLINE);
				footerFecha.add(chunkFecha);
				
				ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, footerFecha,
						(document.right() - document.left()) / 2 + document.leftMargin(), document.bottom() - 30, 0);
			}
			
			if (selloCancel != null) {
				Chunk chunkEtqSelloCancel = new Chunk("Sello digital SAT (Cancelación): ", ffontBold);
				Chunk chunkSelloCancel = new Chunk(selloCancel, ffont);
				//footer.add(Chunk.NEWLINE);
				footerSello.add(chunkEtqSelloCancel);
				footerSello.add(chunkSelloCancel);
				
				ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, footerSello,
						(document.right() - document.left()) / 2 + document.leftMargin(), document.bottom() - 20, 0);
			}
			
			ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, footer,
					(document.right() - document.left()) / 2 + document.leftMargin(), document.bottom() - 10, 0);
		}
		
//		@Override
//		public void onCloseDocument(PdfWriter writer, Document document) {
//			super.onCloseDocument(writer, document);
//			//TODO probar
//			ColumnText.showTextAligned(total, Element.ALIGN_CENTER, new Phrase(String.valueOf(writer.getPageNumber() - 1)),
//					(document.right() - document.left()) / 2 + document.leftMargin(), document.bottom() - 10, 0);
//		}
	}

}
