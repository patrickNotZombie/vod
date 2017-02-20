package com.dcampus.common.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfUtils {
	
	/** 9号  小五 */
	public static final Font P9NORMAL = new Font(getSTSongLight(), 9, Font.NORMAL);
	/** 11号  五号 */
	public static final Font P11NORMAL = new Font(getSTSongLight(), 11, Font.NORMAL);
	public static final Font P12NORMAL = new Font(getSTSongLight(), 12, Font.NORMAL);
	public static final Font P14NORMAL = new Font(getSTSongLight(), 14, Font.NORMAL);
	public static final Font P16NORMAL = new Font(getSTSongLight(), 16, Font.NORMAL);

	public static final Font P11BOLD = new Font(getSTSongLight(), 11, Font.BOLD);
	public static final Font P12BOLD = new Font(getSTSongLight(), 12, Font.BOLD);
	public static final Font P14BOLD = new Font(getSTSongLight(), 14, Font.BOLD);
	public static final Font P16BOLD = new Font(getSTSongLight(), 16, Font.BOLD);

	public static final Font P11UNDERLINE = new Font(getSTSongLight(), 11, Font.UNDERLINE);
	public static final Font P12UNDERLINE = new Font(getSTSongLight(), 12, Font.UNDERLINE);
	public static final Font P14UNDERLINE= new Font(getSTSongLight(), 14, Font.UNDERLINE);
	public static final Font P16UNDERLINE= new Font(getSTSongLight(), 16, Font.UNDERLINE);
	
	public static final Font P22BOLD = new Font(getSTSongLight(), 22, Font.BOLD);

	public static final Font P22BOLD_HEI = new Font(getHEITI(), 22, Font.BOLD);

	public static final BaseFont getSTSongLight() {
		try {
			return BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static final BaseFont getHEITI() {
		try {
			return BaseFont.createFont("/font/simhei.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param fontSize  字体大小
	 * @param fontType  字体风格  如：com.itextpdf.text.Font.BOLD
	 * @return
	 */
	public static Font getFont(int fontSize, int fontStype) {
		return new Font(getSTSongLight(), fontSize, fontStype);
	}
	
	/**
	 * 合并pdf文件
	 * @param srcOS  文件1 ByteArrayOutputStream
	 * @param filePath  要合并文件的路径
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static ByteArrayOutputStream tranPdfFile(ByteArrayOutputStream srcOS, String filePath) throws IOException, DocumentException {
		File file = new File(filePath);
		if (file.exists()) {
			Document document = new Document(PageSize.A4);
			ByteArrayOutputStream baos_result = new ByteArrayOutputStream();
			PdfCopy copy = new PdfCopy(document, baos_result);
			document.open();
			
			// 生成的文档
			PdfReader reader1 = new PdfReader(srcOS.toByteArray());
			// 要合并的文档
			PdfReader reader2 = new PdfReader(filePath);
			PdfCopy.PageStamp stamp = null;
			
			// 复制第一部分，并写页码
			for (int i = 1; i <= reader1.getNumberOfPages(); i++) {
				PdfImportedPage page = copy.getImportedPage(reader1, i);
				stamp = copy.createPageStamp(page);
				stamp.alterContents();
				copy.addPage(page);
			}
			// 复制第二部分，写入页码
			for (int i = 1; i <= reader2.getNumberOfPages(); i++) {
				PdfImportedPage page = copy.getImportedPage(reader2, i);
				stamp = copy.createPageStamp(page);
				stamp.alterContents();
				copy.addPage(page);
			}
			reader1.close();
			reader2.close();
			document.close();
			srcOS.close();
			return baos_result;
		}
		return srcOS;
	}

	/**
	 * 合并图片文件
	 * @param srcOS  文件1 ByteArrayOutputStream
	 * @param filePath  要合并文件的路径
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static ByteArrayOutputStream tranJpgFile(ByteArrayOutputStream srcOS, String filePath) throws IOException, DocumentException {

		Document document_jpg = new Document(PageSize.A4);
		document_jpg.setMargins(70, 70, 70, 70);
		ByteArrayOutputStream baos_jpg = new ByteArrayOutputStream();
		PdfWriter writer = PdfWriter.getInstance(document_jpg, baos_jpg);
		document_jpg.open();
		
		PdfContentByte pdfContent = writer.getDirectContent();

		float widthMax = PageSize.A4.getRight() - 70f - 70f;
		float heightMax = PageSize.A4.getTop() - 70f - 70f;
		File file = new File(filePath);
		if (file.exists()) {
			Image image = Image.getInstance(java.awt.Toolkit.getDefaultToolkit().createImage(filePath), null);
			float width = image.getWidth();
			float height = image.getHeight();
			if (new Double(width) / height > 1) {
				image.setRotationDegrees(90);//逆时针90度
			}
			image.scaleToFit(widthMax, heightMax);
			document_jpg.add(image);
		}
		document_jpg.close();
		
		Document document_result = new Document(PageSize.A4);
		document_result.setMargins(70, 70, 70, 70);
		ByteArrayOutputStream baos_result = new ByteArrayOutputStream();
		PdfCopy copy = new PdfCopy(document_result, baos_result);
		document_result.open();

		PdfReader reader1 = new PdfReader(srcOS.toByteArray());
		PdfReader reader2 = new PdfReader(baos_jpg.toByteArray());
		PdfCopy.PageStamp stamp = null;
		
		// 复制第一部分，并写页码
		for (int i = 1; i <= reader1.getNumberOfPages(); i++) {
			PdfImportedPage page = copy.getImportedPage(reader1, i);
			stamp = copy.createPageStamp(page);	
			stamp.alterContents();
			copy.addPage(page);
		}
		
		// 复制第二部分，写入页码
		for (int i = 1; i <= reader2.getNumberOfPages(); i++) {
			PdfImportedPage page = copy.getImportedPage(reader2, i);
			stamp = copy.createPageStamp(page);
			stamp.alterContents();
			copy.addPage(page);
		}
		reader1.close();
		reader2.close();
		document_result.close();
		srcOS.close();
		
		return baos_result;
	}

	/**
	 * 合并文件
	 * @param srcOS  文件1 ByteArrayOutputStream
	 * @param filePath  文件2 ByteArrayOutputStream
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static ByteArrayOutputStream tranBaosFile(ByteArrayOutputStream baos1, ByteArrayOutputStream baos2) throws IOException, DocumentException {

		Document document_result = new Document(PageSize.A4);
		document_result.setMargins(70, 70, 70, 70);
		ByteArrayOutputStream baos_result = new ByteArrayOutputStream();
		PdfCopy copy = new PdfCopy(document_result, baos_result);
		document_result.open();

		PdfReader reader1 = new PdfReader(baos1.toByteArray());
		PdfReader reader2 = new PdfReader(baos2.toByteArray());
		PdfCopy.PageStamp stamp = null;
		
		// 复制第一部分，并写页码
		for (int i = 1; i <= reader1.getNumberOfPages(); i++) {
			PdfImportedPage page = copy.getImportedPage(reader1, i);
			stamp = copy.createPageStamp(page);	
			stamp.alterContents();
			copy.addPage(page);
		}
		
		// 复制第二部分，写入页码
		for (int i = 1; i <= reader2.getNumberOfPages(); i++) {
			PdfImportedPage page = copy.getImportedPage(reader2, i);
			stamp = copy.createPageStamp(page);
			stamp.alterContents();
			copy.addPage(page);
		}
		reader1.close();
		reader2.close();
		document_result.close();
		baos1.close();
		baos2.close();
		return baos_result;
	}

	public static PdfPCell getCell(int rowspan, int colspan, Phrase phrase) {
		return getCell(rowspan, colspan, phrase, PdfPCell.ALIGN_CENTER, 5);
	}
	public static PdfPCell getCell(int rowspan, int colspan, Phrase phrase, int align, int padding) {
		PdfPCell cell = new PdfPCell();
		cell.setHorizontalAlignment(align);
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell.setPadding(padding);
		cell.setRowspan(rowspan);//本格跨占多少行
		cell.setColspan(colspan);//本格跨占多少列
		cell.setPhrase(phrase);
		return cell;
	}
	public static PdfPCell getCell(int rowspan, int colspan, Phrase phrase, 
			int align, int valign, int padding, int fixedHeight) {
		PdfPCell cell = new PdfPCell();
		cell.setHorizontalAlignment(align);
		cell.setVerticalAlignment(valign);
		cell.setPadding(padding);
		cell.setFixedHeight(fixedHeight);//固定高度
	//	cell.setMinimumHeight(minimumHeight);//最小高度
		cell.setRowspan(rowspan);//本格跨占多少行
		cell.setColspan(colspan);//本格跨占多少列
		cell.setPhrase(phrase);
		return cell;
	}
}
