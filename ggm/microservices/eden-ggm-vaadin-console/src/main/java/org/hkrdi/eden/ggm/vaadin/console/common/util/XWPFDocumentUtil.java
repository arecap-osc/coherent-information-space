package org.hkrdi.eden.ggm.vaadin.console.common.util;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface XWPFDocumentUtil {

	default void addImage(InputStream is, XWPFParagraph paragraph, int type, String fileName, int widthPixels, int heightPixels) throws InvalidFormatException, IOException {
		paragraph.createRun().addPicture(is, type, fileName, Units.toEMU(widthPixels), Units.toEMU(heightPixels)); // 200x200 pixels
	    is.close();
	}
	
	default Map<String, String> fillByEntity(Object entity) {
		Map<String, String> mappingsParameters = new HashMap<>();
		if (entity == null) { return mappingsParameters; }
		final String entityName = entity.getClass().getSimpleName();
		Arrays.asList(entity.getClass().getMethods()).stream().filter(m->m.getName().startsWith("get") 
				&& m.getParameterCount() == 0).
			forEach(m->{
				try {
					mappingsParameters.put(
							"$"+entityName+"."+StringUtil.toLowerCaseFirstLetter(m.getName().replace("get", ""))+"$", 
							""+m.invoke(entity));
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		return mappingsParameters;
	}
	
	default XWPFParagraph createBookmarkedParagraph(XWPFDocument document, String anchor, int bookmarkId) {
		  XWPFParagraph paragraph = document.createParagraph();
		  CTBookmark bookmark = paragraph.getCTP().addNewBookmarkStart();
		  bookmark.setName(anchor);
		  bookmark.setId(BigInteger.valueOf(bookmarkId));
		  XWPFRun run = paragraph.createRun();
		  paragraph.getCTP().addNewBookmarkEnd().setId(BigInteger.valueOf(bookmarkId));
		  return paragraph;
	 }
		 
	default void addHyperlink(XWPFParagraph p_paragraphCell, XWPFParagraph p_paragraphLink, String p_linkedText, String p_paragraphText) {
	    // Create hyperlink in paragraph
	    CTHyperlink cLink = p_paragraphLink.getCTP().addNewHyperlink();
	    cLink.setAnchor(p_paragraphText);
	    // Create the linked text
	    CTText ctText = CTText.Factory.newInstance();
	    ctText.setStringValue(p_linkedText);
	    CTR ctr = CTR.Factory.newInstance();
	    ctr.setTArray(new CTText[] { ctText });

	    // Insert the linked text into the link
	    cLink.setRArray(new CTR[] { ctr });

	    p_paragraphCell.getCTP().setHyperlinkArray(new CTHyperlink[] { cLink });
	    p_paragraphLink.getCTP().removeHyperlink(0);
	}
	
	default void copyTable(XWPFTable source, XWPFTable target, Map<String, String> mappings) {
	    target.getCTTbl().setTblPr(source.getCTTbl().getTblPr());
	    target.getCTTbl().setTblGrid(source.getCTTbl().getTblGrid());
	    for (int r = 0; r<source.getRows().size(); r++) {
	        XWPFTableRow targetRow = target.createRow();
	        XWPFTableRow row = source.getRows().get(r);
	        targetRow.getCtRow().setTrPr(row.getCtRow().getTrPr());
	        for (int c=0; c<row.getTableCells().size(); c++) {
	            //newly created row has 1 cell
	            XWPFTableCell targetCell = c==0 ? targetRow.getTableCells().get(0) : targetRow.createCell();
	            XWPFTableCell cell = row.getTableCells().get(c);
	            targetCell.getCTTc().setTcPr(cell.getCTTc().getTcPr());
	            XmlCursor cursor = targetCell.getParagraphArray(0).getCTP().newCursor();
	            for (int p = 0; p < cell.getBodyElements().size(); p++) {
	                IBodyElement elem = cell.getBodyElements().get(p);
	                if (elem instanceof XWPFParagraph) {
	                    XWPFParagraph targetPar = targetCell.insertNewParagraph(cursor);
	                    cursor.toNextToken();
	                    XWPFParagraph par = (XWPFParagraph) elem;
	                    
	                    copyParagraph(par, targetPar, mappings);
	                } else if (elem instanceof XWPFTable) {
	                    XWPFTable targetTable = targetCell.insertNewTbl(cursor);
	                    XWPFTable table = (XWPFTable) elem;
	                    copyTable(table, targetTable, mappings);
	                    cursor.toNextToken();
	                }
	            }
	            //newly created cell has one default paragraph we need to remove
	            targetCell.removeParagraph(targetCell.getParagraphs().size()-1);
	        }
	    }
	    //newly created table has one row by default. we need to remove the default row.
	    target.removeRow(0);
	}
	
	default String replaceVariables(Map<String, String> mappings, String text) {
		if (text == null || !text.contains("$")) {
			return text;
		}
		String newText = text;
    	for(String var: mappings.keySet()) {
    		if (text.contains(var)) {
    			String mapValue = mappings.get(var);
            	if (mapValue != null) {
            		newText = text.replace(var, mapValue);
            	}
    		}
    	}
    	return newText;
	}
	
	default XWPFParagraph addToToc(XWPFParagraph target) {
		CTP ctP = target.getCTP();
    	CTSimpleField toc = ctP.addNewFldSimple();
    	toc.setInstr("TOC \\h");
    	toc.setDirty(STOnOff.TRUE);
    	return target;
	}
	
	default void copyParagraph(XWPFParagraph source, XWPFParagraph target, Map<String, String> mappings) {
	    target.getCTP().setPPr(source.getCTP().getPPr());
	    for (int i=0; i<source.getRuns().size(); i++ ) {
	        XWPFRun run = source.getRuns().get(i);
	        XWPFRun targetRun = target.createRun();
	        //copy formatting
	        targetRun.getCTR().setRPr(run.getCTR().getRPr());
	        //no images just copy text
	        targetRun.setText(replaceVariables(mappings, run.getText(0)));
	    }
//	    addToToc(target);
	}
	
	default boolean containsAnchorMarker(XWPFParagraph source, String marker) {
	    for (int i=0; i<source.getRuns().size(); i++ ) {
	        XWPFRun run = source.getRuns().get(i);
	        if (run !=null && run.getText(0) != null &&
	        		run.getText(0).contains(marker)) {
	        	return true;
	        }
	    }
	    return false;
	}

	default void replaceParagrahVariables(XWPFDocument doc, Map<String, String> mappings) {
		for (XWPFParagraph p : doc.getParagraphs()) {
		    List<XWPFRun> runs = p.getRuns();
		    if (runs != null) {
		        for (XWPFRun r : runs) {
		            String text = r.getText(0);
		            if (text != null) {
		            	for(String var: mappings.keySet()) {
		            		if (text.contains(var)) {
		            			String newText = mappings.get(var);
				            	if (newText == null) {
				            		newText = text.replace("$", "")+" Replaced";
				            	}else {
				            		newText = text.replace(var, newText);
				            	}
				            	r.setText(newText, 0);
		            		}
		            	}
		            }
		        }
		    }
		}
	}
	
	default void replaceTableVariables(XWPFDocument doc, Map<String, String> mappings) {
		for (XWPFTable tbl : doc.getTables()) {
			   for (XWPFTableRow row : tbl.getRows()) {
			      for (XWPFTableCell cell : row.getTableCells()) {
			         replaceParagrahVariables(doc, mappings);
			      }
			   }
			}
	}
	
	default XWPFParagraph findInTable(XWPFTable source, String what) {
	    for (int r = 0; r<source.getRows().size(); r++) {
	        XWPFTableRow row = source.getRows().get(r);
	        for (int c=0; c<row.getTableCells().size(); c++) {
	            //newly created row has 1 cell
	            XWPFTableCell cell = row.getTableCells().get(c);
	            for (int p = 0; p < cell.getBodyElements().size(); p++) {
	                IBodyElement elem = cell.getBodyElements().get(p);
	                if (elem instanceof XWPFParagraph) {
	                    XWPFParagraph par = (XWPFParagraph) elem;
	                    if (findInParagraph(par, what)) {
	                    	return par;
	                    }
	                } else if (elem instanceof XWPFTable) {
	                    XWPFTable table = (XWPFTable) elem;
	                    XWPFParagraph result = findInTable(table, what);
	                    if (result != null) {
	                    	return result;
	                    }
	                }
	            }
	        }
	    }
	    return null;
	}

//	default XWPFParagraph findParagraph(XWPFDocument doc, String what) {
//		return doc.getParagraphs().stream().filter(p->{
//			List<XWPFRun> runs = p.getRuns();
//		    if (runs != null) {
//		    	String runsValue = "";
//		        for (XWPFRun r : runs) {
//		            String text = r.getText(0);
//		            if (text != null) {
//		            	runsValue += text;
//		            }
//		        }
//		        if (runsValue.contains(what)) {
//		            	return true;
//	            }
//	        }
//		    return false;
//		}).findFirst().get();
//	}

	default XWPFParagraph findParagraph(XWPFDocument doc, String what) {
		return doc.getParagraphs().stream().filter(p->
			findInParagraph(p, what)
		).findFirst().get();
	}
	
	default boolean findInParagraph(XWPFParagraph p, String what) {
		List<XWPFRun> runs = p.getRuns();
	    if (runs != null) {
	    	String runsValue = "";
	        for (XWPFRun r : runs) {
	            String text = r.getText(0);
	            if (text != null) {
	            	runsValue += text;
	            }
	        }
	        if (runsValue.contains(what)) {
	            	return true;
            }
        }
	    return false;
	}
}
