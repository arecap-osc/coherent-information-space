package org.hkrdi.eden.ggm.vaadin.console.service;

import com.vaadin.flow.spring.annotation.UIScope;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.hkrdi.eden.ggm.repository.entity.DataMap;
import org.hkrdi.eden.ggm.repository.ggm.entity.Application;
import org.hkrdi.eden.ggm.repository.ggm.entity.ApplicationData;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ApplicationDataRepositoryService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ClusterBean;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.CoherentSpaceService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.DataMapFilterUtil;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Service
@UIScope
public class ExportDataService {

    @Autowired
    private ApplicationDataRepositoryService applicationDataService;

    @Autowired
    private CoherentSpaceService coherentSpaceService;

    public List<String> getNetworkNames() {
        return coherentSpaceService.getAvailableNetworks();
    }

    @Transactional
    public byte[] generate(Application application, InputStream template, List<String> networks) throws Exception {
        XWPFDocument doc = new XWPFDocument(OPCPackage.open(template));

        Map<String, String> mappingsParagraph = new HashMap<String, String>();
        mappingsParagraph.put("$application.name$", application.getBrief().getDescription());

        XWPFParagraph levelNameParagrah = ExportDataService.findParagraph(doc, "$level.name$");
        XWPFParagraph clusterNameParagrah = ExportDataService.findParagraph(doc, "$cluster.name$");
        XWPFParagraph clusterInnerOuterParagrah = ExportDataService.findParagraph(doc, "$cluster.outer.type$");

        XWPFTable nodeTable = doc.getTables().get(0);
        XWPFTable nodeDetailsTable = doc.getTables().get(1);
        XWPFTable nodeRelationsTable = doc.getTables().get(2);

        for (String networkName : networks) {
            mapLevelSpecificInformationToParagraph(doc, mappingsParagraph, levelNameParagrah, networkName);
            mappingsParagraph.put("$cluster.outer.type$", networkName.split("\\_")[0].equals("SUSTAINABLE") ? "Cadru Sustenabil" : "Metabolism");

            List<Long> clusters = coherentSpaceService.getNetworkClusters(networkName).stream().map(ClusterBean::getClusterIndex).collect(Collectors.toList());
            Collections.sort(clusters);

            Map<Long, ApplicationData> applicationDataMap = new HashMap<>();
            List<ApplicationData> appData = applicationDataService.findApplicationDataForNetwork(application.getId(), networkName);
            appData.stream().forEach(applicationData -> applicationDataMap.put(applicationData.getDataMapId(), applicationData));

            for (Long cluster : clusters) {
                long clusterNo = cluster;
                mapClusterParagraphs(doc, mappingsParagraph, clusterNameParagrah, clusterInnerOuterParagrah, networkName, clusterNo);
                List<Long> addressIndexVisited = new ArrayList<>();

                List<DataMap> allDataMapsInCluster = coherentSpaceService.findClusterDataMaps(new ClusterBean(networkName, cluster));

                allDataMapsInCluster.stream().filter(DataMapFilterUtil.byOuterRoutes())
                        .forEach(outerDataMap -> computeDataMapToDocumentParagraph(application, doc, mappingsParagraph,
                                nodeDetailsTable, nodeRelationsTable, networkName, applicationDataMap, clusterNo,
                                addressIndexVisited, allDataMapsInCluster, outerDataMap));

                ExportDataService.copyParagraph(clusterInnerOuterParagrah, doc.createParagraph(), mappingsParagraph);
                addressIndexVisited.clear();

                allDataMapsInCluster.stream().filter(DataMapFilterUtil.byInnerRoutes())
                        .forEach(innerDataMap -> computeDataMapToDocumentParagraph(application, doc, mappingsParagraph,
                                nodeDetailsTable, nodeRelationsTable, networkName, applicationDataMap, clusterNo,
                                addressIndexVisited, allDataMapsInCluster, innerDataMap));
                doc.createParagraph();
            }
            doc.createParagraph().setPageBreak(true);
        }

        ExportDataService.replaceParagrahVariables(doc, mappingsParagraph);

        removeInitialDocumentParagraphs(doc, levelNameParagrah, clusterNameParagrah, clusterInnerOuterParagrah, nodeTable, nodeDetailsTable, nodeRelationsTable);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        doc.write(baos);
        doc.close();

        return baos.toByteArray();
    }

    private void mapLevelSpecificInformationToParagraph(XWPFDocument doc, Map<String, String> mappingsParagraph, XWPFParagraph levelNameParagrah, String networkName) {
        mappingsParagraph.put("$level.name$", " Level " + networkName);
        ExportDataService.copyParagraph(levelNameParagrah, doc.createParagraph(), mappingsParagraph);
        doc.createParagraph();
        doc.createParagraph();
    }

    private void computeDataMapToDocumentParagraph(Application application, XWPFDocument doc, Map<String, String> mappingsParagraph, XWPFTable nodeDetailsTable, XWPFTable nodeRelationsTable, String networkName, Map<Long, ApplicationData> applicationDataMap, long clusterNo, List<Long> addressIndexVisited, List<DataMap> allDataMapsInCluster, DataMap outerDataMap) {
        Long currentAddressIndex = null;
        boolean addressIndexWanted = true;
        if (addressIndexVisited.contains(outerDataMap.getAddressIndex())) {
            if (addressIndexVisited.contains(outerDataMap.getToAddressIndex())) {
                return;
            } else {
                currentAddressIndex = outerDataMap.getToAddressIndex();
                addressIndexWanted = false;
            }
        } else {
            currentAddressIndex = outerDataMap.getAddressIndex();
        }
        addressIndexVisited.add(currentAddressIndex);

        mapClusterNodeTitleToParagraph(doc, mappingsParagraph, nodeDetailsTable, networkName, applicationDataMap, clusterNo, outerDataMap);

        final Long finalAddressIndex = currentAddressIndex;
        boolean finalAddressIndexWanted = addressIndexWanted;

        //toNodes
        allDataMapsInCluster.stream().filter(dataMap -> dataMap.getAddressIndex() == finalAddressIndex)
                .forEach(toNode -> mapNodeToParagraph(doc, mappingsParagraph, nodeRelationsTable, networkName, clusterNo,
                        outerDataMap, toNode, "TO", applicationDataMap, finalAddressIndexWanted));
        //fromNodes
        allDataMapsInCluster.stream().filter(dataMap -> dataMap.getToAddressIndex() == finalAddressIndex)
                .forEach(fromNode -> mapNodeToParagraph(doc, mappingsParagraph, nodeRelationsTable, networkName, clusterNo,
                        outerDataMap, fromNode, "FROM", applicationDataMap, finalAddressIndexWanted));
        doc.createParagraph();
    }

    private void mapClusterNodeTitleToParagraph(XWPFDocument doc, Map<String, String> mappingsParagraph, XWPFTable nodeDetailsTable, String networkName, Map<Long, ApplicationData> applicationDataMap, long clusterNo, DataMap outerDataMap) {
        ApplicationData applicationData = applicationDataMap.get(outerDataMap.getId());
        mappingsParagraph.put("$cluster.outer.node[0].addres$", networkName + "::" + clusterNo + "::" + outerDataMap.getAddressIndex());
        mappingsParagraph.put("$cluster.outer.node[0].semantic$", applicationData != null ? applicationData.getSemantic() : "");
        ExportDataService.copyTable(nodeDetailsTable, doc.createTable(), mappingsParagraph);
        doc.createParagraph();
    }

    private void mapNodeToParagraph(XWPFDocument doc, Map<String, String> mappingsParagraph, XWPFTable nodeRelationsTable,
                                    String networkName, long clusterNo, DataMap nodeInCluster, DataMap linkedNode, String direction,
                                    Map<Long, ApplicationData> applicationDataMap, boolean addressIndex) {
        ApplicationData nodeInClusterApplicationData = applicationDataMap.get(nodeInCluster.getId());
        ApplicationData linkedNodeApplicationData = applicationDataMap.get(linkedNode.getId());

        mappingsParagraph.put("$cluster.outer.node[0].addres$", networkName + "::" + clusterNo + "::" + (addressIndex ? nodeInCluster.getAddressIndex() : nodeInCluster.getToAddressIndex()));
        mappingsParagraph.put("$cluster.outer.node[0].semantic$", nodeInClusterApplicationData != null ? nodeInClusterApplicationData.getSemantic() : "");
        mappingsParagraph.put("$cluster.outer.node[0].syntax$", linkedNodeApplicationData != null ? linkedNodeApplicationData.getSyntax() : "");
        mappingsParagraph.put("$cluster.outer.node[0].direction$", direction);

        Long addressIndexWithWantedSemantic = "FROM".equals(direction) ? linkedNode.getAddressIndex() : linkedNode.getToAddressIndex();
        Optional<ApplicationData> wantedSemantic = applicationDataMap.values().stream().filter(applicationData -> addressIndexWithWantedSemantic.equals(applicationData.getAddressIndex())).findAny();
        if (wantedSemantic .isPresent()) {
            mappingsParagraph.put("$tosemantic.name$", wantedSemantic.get().getSemantic());
        } else {
            mappingsParagraph.put("$tosemantic.name$", "");
        }

        mappingsParagraph.put("$tosemantic.node$", " " + direction + " " + networkName + "::" + clusterNo + "::" + addressIndexWithWantedSemantic);
        ExportDataService.copyTable(nodeRelationsTable, doc.createTable(), mappingsParagraph);
        doc.createParagraph();
    }


    private void mapClusterParagraphs(XWPFDocument doc, Map<String, String> mappingsParagraph, XWPFParagraph clusterNameParagrah, XWPFParagraph clusterInnerOuterParagrah,
                                      String network, long clusterNo) {
        mappingsParagraph.put("$cluster.name$", "Cluster " + clusterNo);
        mappingsParagraph.put("$cluster.type$", network.split("\\::")[0]);

        ExportDataService.copyParagraph(clusterNameParagrah, doc.createParagraph(), mappingsParagraph);
        ExportDataService.copyParagraph(clusterInnerOuterParagrah, doc.createParagraph(), mappingsParagraph);
    }

    private void removeInitialDocumentParagraphs(XWPFDocument doc, XWPFParagraph levelNameParagrah, XWPFParagraph clusterNameParagrah, XWPFParagraph clusterInnerOuterParagrah, XWPFTable nodeTable, XWPFTable nodeDetailsTable, XWPFTable nodeRelationsTable) {
        doc.removeBodyElement(doc.getPosOfParagraph(levelNameParagrah));
        doc.removeBodyElement(doc.getPosOfParagraph(clusterNameParagrah));
        doc.removeBodyElement(doc.getPosOfParagraph(clusterInnerOuterParagrah));
        doc.removeBodyElement(doc.getPosOfTable(nodeTable));
        doc.removeBodyElement(doc.getPosOfTable(nodeDetailsTable));
        doc.removeBodyElement(doc.getPosOfTable(nodeRelationsTable));
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
