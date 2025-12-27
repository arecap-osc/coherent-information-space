package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.backend.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHyperlinkRun;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlCursor;
import org.dom4j.DocumentException;
import org.hkrdi.eden.ggm.repository.common.documentimage.DocumentImageJpaService;
import org.hkrdi.eden.ggm.repository.common.documentimage.entity.DocumentImage;
import org.hkrdi.eden.ggm.repository.entity.DataMap;
import org.hkrdi.eden.ggm.repository.ggm.entity.Application;
import org.hkrdi.eden.ggm.repository.ggm.entity.ApplicationData;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ApplicationDataRepositoryService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.CoherentSpaceService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHyperlink;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSimpleField;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import com.vaadin.flow.spring.annotation.UIScope;

@Service
@UIScope
public class RoadExportDataService {

    @Autowired
    private ApplicationDataRepositoryService applicationDataService;
    
    @Autowired
    private DocumentImageJpaService docImageService;

    @Autowired
    private CoherentSpaceService coherentSpaceService;

    public List<String> getNetworkNames() {
        return coherentSpaceService.getAvailableNetworks();
    }

    @Transactional
    public byte[] generate(Application application, InputStream template, List<String> networks, String road) throws Exception {
//    	byte[] docx = convertDocxToPdf(
//    			generateDocx(application, template, networks, road));
    	byte[] pdfs = generateMergedPdf(application, networks, road);
    	
//    	//Instantiating PDFMergerUtility class
//        PDFMergerUtility pdfMerger = new PDFMergerUtility();
//        
//        File outputFile = File.createTempFile("mergepdf"+System.currentTimeMillis(), ".pdf");
//        //Setting the destination file
//        pdfMerger.setDestinationFileName(outputFile.getAbsolutePath());
//        
//        pdfMerger.addSource(new ByteArrayInputStream(docx));
//        pdfMerger.addSource(new ByteArrayInputStream(pdfs));
//        
//        pdfMerger.mergeDocuments(MemoryUsageSetting.setupMixed(100L));
//        byte[] result = FileCopyUtils.copyToByteArray(outputFile);
//        
//        try {
//        	outputFile.delete();
//        }catch (Exception e) {
//        	e.printStackTrace();
//		}
//        
//        return result;
    	return pdfs;
    }
    
    @Transactional
    public byte[] generateDocx(Application application, InputStream template, List<String> networks, String road) throws Exception {
        XWPFDocument doc = new XWPFDocument(OPCPackage.open(template));

        Map<String, String> mappingsParagraph = new HashMap<String, String>();
        mappingsParagraph.put("$application.name$", application.getBrief().getDescription());
        
        XWPFParagraph applicationNameParagrah = RoadExportDataService.findParagraph(doc, "$application.name$");
        RoadExportDataService.copyParagraph(applicationNameParagrah, doc.createParagraph(), mappingsParagraph);
        
        XWPFParagraph levelNameParagrah = RoadExportDataService.findParagraph(doc, "$level.name$");
        
        XWPFParagraph semanticDataParagrah = RoadExportDataService.findParagraph(doc, "$semanticdata$");
        XWPFParagraph syntaxDataParagrah = RoadExportDataService.findParagraph(doc, "$syntaxdata$");

        for (String networkName : networks) {
            mapLevelSpecificInformationToParagraph(doc, mappingsParagraph, levelNameParagrah, networkName);

            String roadS = road;
            String[] roadNodes = roadS.split(",");
            
            Map<Long, Map<Long, ApplicationData>> applicationDataMap = new HashMap<>();
            List<ApplicationData> appDatas = applicationDataService.findApplicationDataForNetwork(application.getId(), networkName);
            for(ApplicationData appData : appDatas) {
            	Map<Long, ApplicationData> appDataList = applicationDataMap.get(appData.getAddressIndex());
            	if (appDataList == null) {
            		applicationDataMap.put(appData.getAddressIndex(), new HashMap<>());
            	}
            	appDataList = applicationDataMap.get(appData.getAddressIndex());
            	appDataList.put(appData.getToAddressIndex(), appData);
            }
            
            for (int index = 0; index < roadNodes.length-1; index++) {
            	Map<Long, ApplicationData> appDataForNode = applicationDataMap.get(new Long(roadNodes[index]));
            	ApplicationData appData = appDataForNode.get(new Long(roadNodes[index+1]));
            	String semantic = "(" + roadNodes[index] + ") ";
            	String syntax = "(" + roadNodes[index] + " - " + roadNodes[index+1] + ") ";
            	
            	if (appData != null) {
            		semantic += appData.getSemantic();
            		syntax += appData.getSyntax();
            	}
            	
            	mappingsParagraph.put("$syntaxdata$", syntax);
            	mappingsParagraph.put("$semanticdata$", semantic);
            	RoadExportDataService.copyParagraph(semanticDataParagrah, doc.createParagraph(), mappingsParagraph);
            	RoadExportDataService.copyParagraph(syntaxDataParagrah, doc.createParagraph(), mappingsParagraph);
            }
            Map<Long, ApplicationData> appDataForNode = applicationDataMap.get(new Long(roadNodes[roadNodes.length-1]));
            ApplicationData appData = appDataForNode.values().stream().findAny().orElse(null);
            String semantic = "(" + roadNodes[roadNodes.length-1] + ") ";
            if (appData != null) {
        		semantic += appData.getSemantic();
        	}
        	mappingsParagraph.put("$semanticdata$", semantic);
            RoadExportDataService.copyParagraph(semanticDataParagrah, doc.createParagraph(), mappingsParagraph);
            	
            doc.createParagraph().setPageBreak(true);
        }

        removeInitialDocumentParagraphs(doc, applicationNameParagrah, levelNameParagrah, semanticDataParagrah, syntaxDataParagrah);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        doc.write(baos);
        doc.close();
        
        return baos.toByteArray();
    }

    private byte[] convertDocxToPdf(byte[] docx) {
    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    	IConverter converter = LocalConverter.builder().build();
        converter.convert(new ByteArrayInputStream(docx), false).as(DocumentType.DOCX).to(outputStream).as(DocumentType.PDF).execute();
        return outputStream.toByteArray();
    }
    
    private List<ApplicationData> getApplicationDataAsNodeWells(Application application, String network, Long addressIndex) {
        Optional<DataMap> dataMapOpt = coherentSpaceService
                .findNodeDataMap(new NodeBean(network, addressIndex));
        if (!dataMapOpt.isPresent()) {
//            LOGGER.info("The dataMap index " + getEntity().getNetwork() + "::" + getEntity().getDataMapId() + " not found from application_data index " + getInitialEntityId());
            return Arrays.asList(new ApplicationData[]{});
        }
        NodeBean node = new NodeBean(dataMapOpt.get().getNetwork(), dataMapOpt.get().getAddressIndex());
        List<Long> dataMapIds = coherentSpaceService.findNodeWells(node).stream().map(DataMap::getId).collect(Collectors.toList());
        List<ApplicationData> applicationIds = applicationDataService.findAllByApplicationIdAndDataMapIdIn(application.getId(),
                dataMapIds);
        if (applicationIds.isEmpty()) {
//            LOGGER.info("The appication ids for application data id" + getInitialEntityId() + " not found ");
            return Arrays.asList(new ApplicationData[]{});
        }
        return applicationIds;
    }
    
    @Transactional
    public byte[] generateMergedPdf(Application application, List<String> networks, String road) throws Exception {

    	//Instantiating PDFMergerUtility class
        PDFMergerUtility pdfMerger = new PDFMergerUtility();
        
        for (String networkName : networks) {

            String roadS = road;
            String[] roadNodes = roadS.split(",");
            
            for (int index = 0; index < roadNodes.length; index++) {
        		Stream<DocumentImage> docs = getDocumentImageForAppData(application, getApplicationDataAsNodeWells(application, networkName, new Long(roadNodes[index])));
        		docs.findFirst().ifPresent(c->{
        			pdfMerger.addSource(new ByteArrayInputStream(
        					c.getImageContent()));
        		});
            }
        }
        
        File outputFile = File.createTempFile("mergepdf"+System.currentTimeMillis(), ".pdf");
        //Setting the destination file
        pdfMerger.setDestinationFileName(outputFile.getAbsolutePath());
        pdfMerger.mergeDocuments(MemoryUsageSetting.setupMixed(100L));
        byte[] result = FileCopyUtils.copyToByteArray(outputFile);
        try {
        	outputFile.delete();
        }catch (Exception e) {
        	e.printStackTrace();
		}
        return result;
    }
    
    private Stream<DocumentImage> getDocumentImageForAppData(Application application, List<ApplicationData> appData) {
		return docImageService.findAnyMatching(application.getId(), appData.stream().map(a->a.getId()).collect(Collectors.toList()), ApplicationData.class.getSimpleName()).stream()
			.collect(Collectors.groupingBy(p -> p.getName())).values().stream().map(l -> (l.get(0)));
	}
    
    public static void mergePDF(List<byte[]> pdfFilesAsByteArray) throws DocumentException, IOException {

    	//Loading an existing PDF document
        File file1 = new File("D:/Downloads/1. Introduction to Cassandra course.pdf");
        PDDocument doc1 = PDDocument.load(file1);
         
        File file2 = new File("D:/Downloads/2. Introduction Big Data.pdf");
        PDDocument doc2 = PDDocument.load(file2);
           
        //Instantiating PDFMergerUtility class
        PDFMergerUtility PDFmerger = new PDFMergerUtility();

        //Setting the destination file
        PDFmerger.setDestinationFileName("D:/Downloads/merged.pdf");

        //adding the source files
        PDFmerger.addSource(file1);
        PDFmerger.addSource(file2);

        //Merging the two documents
        PDFmerger.mergeDocuments(MemoryUsageSetting.setupMixed(100L));

        System.out.println("Documents merged");
        //Closing the documents
        doc1.close();
        doc2.close();

    }

    private void mapLevelSpecificInformationToParagraph(XWPFDocument doc, Map<String, String> mappingsParagraph, XWPFParagraph levelNameParagrah, String networkName) {
        mappingsParagraph.put("$level.name$", " Level " + networkName);
        RoadExportDataService.copyParagraph(levelNameParagrah, doc.createParagraph(), mappingsParagraph);
        doc.createParagraph();
        doc.createParagraph();
    }

    private void removeInitialDocumentParagraphs(XWPFDocument doc, XWPFParagraph applicationNameParagrah, XWPFParagraph levelNameParagrah, XWPFParagraph clusterNameParagrah, XWPFParagraph clusterInnerOuterParagrah) {
    	doc.removeBodyElement(doc.getPosOfParagraph(applicationNameParagrah));
    	doc.removeBodyElement(doc.getPosOfParagraph(levelNameParagrah));
        doc.removeBodyElement(doc.getPosOfParagraph(clusterNameParagrah));
        doc.removeBodyElement(doc.getPosOfParagraph(clusterInnerOuterParagrah));
    }


    public static XWPFHyperlinkRun createHyperlinkRunToAnchor(XWPFParagraph paragraph, String anchor) {
        CTHyperlink cthyperLink = paragraph.getCTP().addNewHyperlink();
        cthyperLink.setAnchor(anchor);
        cthyperLink.addNewR();
        return new XWPFHyperlinkRun(
                cthyperLink,
                cthyperLink.getRArray(0),
                paragraph
        );
    }

    public static XWPFParagraph createBookmarkedParagraph(XWPFDocument document, String anchor, int bookmarkId) {
        XWPFParagraph paragraph = document.createParagraph();
        CTBookmark bookmark = paragraph.getCTP().addNewBookmarkStart();
        bookmark.setName(anchor);
        bookmark.setId(BigInteger.valueOf(bookmarkId));
        XWPFRun run = paragraph.createRun();
        paragraph.getCTP().addNewBookmarkEnd().setId(BigInteger.valueOf(bookmarkId));
        return paragraph;
    }

    public static void addHyperlink(XWPFParagraph p_paragraphCell, XWPFParagraph p_paragraphLink, String p_linkedText, String p_paragraphText) {
        // Create hyperlink in paragraph
        CTHyperlink cLink = p_paragraphLink.getCTP().addNewHyperlink();
        cLink.setAnchor(p_paragraphText);
        // Create the linked text
        CTText ctText = CTText.Factory.newInstance();
        ctText.setStringValue(p_linkedText);
        CTR ctr = CTR.Factory.newInstance();
        ctr.setTArray(new CTText[]{ctText});

        // Insert the linked text into the link
        cLink.setRArray(new CTR[]{ctr});

        p_paragraphCell.getCTP().setHyperlinkArray(new CTHyperlink[]{cLink});
        p_paragraphLink.getCTP().removeHyperlink(0);
    }

    public static void copyTable(XWPFTable source, XWPFTable target, Map<String, String> mappings) {
        target.getCTTbl().setTblPr(source.getCTTbl().getTblPr());
        target.getCTTbl().setTblGrid(source.getCTTbl().getTblGrid());
        for (int r = 0; r < source.getRows().size(); r++) {
            XWPFTableRow targetRow = target.createRow();
            XWPFTableRow row = source.getRows().get(r);
            targetRow.getCtRow().setTrPr(row.getCtRow().getTrPr());
            for (int c = 0; c < row.getTableCells().size(); c++) {
                //newly created row has 1 cell
                XWPFTableCell targetCell = c == 0 ? targetRow.getTableCells().get(0) : targetRow.createCell();
                XWPFTableCell cell = row.getTableCells().get(c);
                targetCell.getCTTc().setTcPr(cell.getCTTc().getTcPr());
                XmlCursor cursor = targetCell.getParagraphArray(0).getCTP().newCursor();
                for (int p = 0; p < cell.getBodyElements().size(); p++) {
                    IBodyElement elem = cell.getBodyElements().get(p);
                    if (elem instanceof XWPFParagraph) {
                        XWPFParagraph targetPar = targetCell.insertNewParagraph(cursor);
                        cursor.toNextToken();
                        XWPFParagraph par = (XWPFParagraph) elem;

                        if (containsAnchorMarker(par, "$tosemantic.node$")) {
                            createHyperlinkRunToAnchor(targetPar,
                                    "Bookmark" + replaceVariables(mappings, "$tosemantic.node$").replace("TO ", "").replace("FROM ", ""));
                        }

                        copyParagraph(par, targetPar, mappings);
                    } else if (elem instanceof XWPFTable) {
                        XWPFTable targetTable = targetCell.insertNewTbl(cursor);
                        XWPFTable table = (XWPFTable) elem;
                        copyTable(table, targetTable, mappings);
                        cursor.toNextToken();
                    }
                }
                //newly created cell has one default paragraph we need to remove
                targetCell.removeParagraph(targetCell.getParagraphs().size() - 1);
            }
        }
        //newly created table has one row by default. we need to remove the default row.
        target.removeRow(0);
    }

    public static XWPFParagraph findInTable(XWPFTable source, String what) {
        for (int r = 0; r < source.getRows().size(); r++) {
            XWPFTableRow row = source.getRows().get(r);
            for (int c = 0; c < row.getTableCells().size(); c++) {
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

    public static String replaceVariables(Map<String, String> mappings, String text) {
        if (text == null && !text.contains("$")) {
            return text;
        }
        String newText = text;
        for (String var : mappings.keySet()) {
            if (text.contains(var)) {
                String mapValue = mappings.get(var);
                if (mapValue != null) {
                    newText = text.replace(var, mapValue);
                }
            }
        }
        return newText;
    }

    public static XWPFParagraph addToToc(XWPFParagraph target) {
        CTP ctP = target.getCTP();
        CTSimpleField toc = ctP.addNewFldSimple();
        toc.setInstr("TOC \\h");
        toc.setDirty(STOnOff.TRUE);
        return target;
    }

    public static void copyParagraph(XWPFParagraph source, XWPFParagraph target, Map<String, String> mappings) {
        target.getCTP().setPPr(source.getCTP().getPPr());
        for (int i = 0; i < source.getRuns().size(); i++) {
            XWPFRun run = source.getRuns().get(i);
            XWPFRun targetRun = target.createRun();
            //copy formatting
            targetRun.getCTR().setRPr(run.getCTR().getRPr());
            //no images just copy text
            targetRun.setText(replaceVariables(mappings, run.getText(0)));
        }
//	    addToToc(target);
    }

    public static boolean containsAnchorMarker(XWPFParagraph source, String marker) {
        for (int i = 0; i < source.getRuns().size(); i++) {
            XWPFRun run = source.getRuns().get(i);
            if (run != null && run.getText(0) != null &&
                    run.getText(0).contains(marker)) {
                return true;
            }
        }
        return false;
    }

    public static Set<String> variables;

    static {
        variables = new HashSet<String>();
        variables.addAll(Arrays.asList(new String[]{
                "$application.name$",
                "$level.name$",
                "$cluster.name$",
                "$cluster.type$",
                "$cluster.outer.type$",
                "$cluster.outer.node[0].addres$",
                "$cluster.outer.node[0].semantic$",
                "$cluster.outer.node[0].syntax$",
                "$cluster.outer.node[0].direction$",
                "$tosemantic.name$",
                "$tosemantic.node$"
        }));
    }

    public static void replaceParagrahVariables(XWPFDocument doc, Map<String, String> mappings) {
        for (XWPFParagraph p : doc.getParagraphs()) {
            List<XWPFRun> runs = p.getRuns();
            if (runs != null) {
                for (XWPFRun r : runs) {
                    String text = r.getText(0);
                    if (text != null) {
                        for (String var : mappings.keySet()) {
                            if (text.contains(var)) {
                                String newText = mappings.get(var);
                                if (newText == null) {
                                    newText = text.replace("$", "") + " Replaced";
                                } else {
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

    public static void replaceTableVariables(XWPFDocument doc, Map<String, String> mappings) {
        for (XWPFTable tbl : doc.getTables()) {
            for (XWPFTableRow row : tbl.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    replaceParagrahVariables(doc, mappings);
                }
            }
        }
    }

    public static XWPFParagraph findParagraph(XWPFDocument doc, String what) {
        return doc.getParagraphs().stream().filter(p ->
                findInParagraph(p, what)
        ).findFirst().get();
    }

    public static boolean findInParagraph(XWPFParagraph p, String what) {
        List<XWPFRun> runs = p.getRuns();
        if (runs != null) {
            for (XWPFRun r : runs) {
                String text = r.getText(0);
                if (text != null && text.contains(what)) {
                    return true;
                }
            }
        }
        return false;
    }
}
