package org.nlp.match.Utility;

import org.nlp.match.Sentenizer.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import java.io.FileWriter;
import java.io.IOException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC;
import org.apache.poi.ss.usermodel.Row;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.nlp.match.SentenceMapper.Allocation;

/**
 *
 * @author Bene
 */
public class OutputWriter {

    private static void createDir(){
        File dir = new File("./output");
        dir.mkdir();
    }
    
    public static void writeInPlotTemplateXLS(Map<Integer, Allocation> map, String fileName) {
        try {
            createDir();
            String templateLocation = "./templates/plot_template.xls";
            String fileLocation = "./output/" + fileName + ".xls";
            /*
             Path copySourcePath = Paths.get(templateLocation);
             Path copyTargetPath = Paths.get(fileLocation);
             Files.copy(copySourcePath, copyTargetPath, COPY_ATTRIBUTES, REPLACE_EXISTING);
             */
            FileInputStream file = new FileInputStream(new File(templateLocation));
            HSSFWorkbook workbook = new HSSFWorkbook(file);
            HSSFSheet sheet = workbook.getSheetAt(0);

            Cell cell = null;

            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("KEY1");
            row.createCell(1).setCellValue("KEY2");

            int rowNumber = 1;
            for (Entry entry : map.entrySet()) {
                int key1 = (int) entry.getKey();
                Allocation allocation = (Allocation) entry.getValue();
                int key2 = allocation.getSentenceID2();

                row = sheet.createRow(rowNumber);
                row.createCell(0).setCellType(CELL_TYPE_NUMERIC);
                row.getCell(0).setCellValue(key1);
                row.createCell(1).setCellType(CELL_TYPE_NUMERIC);
                row.getCell(1).setCellValue(key2);

                rowNumber++;
            }

            FileOutputStream fileOut = new FileOutputStream(fileLocation);
            workbook.write(fileOut);
            fileOut.close();

        } catch (IOException ex) {
            Logger.getLogger(OutputWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void writeGEFX(String path, HashMap<ArrayList<Token>, Double> resultMap, String filename) {
        try {
            Namespace ns = Namespace.getNamespace("http://www.gexf.net/1.2draft");
            Element gexf = new Element("gexf");
            gexf.setNamespace(ns);
            gexf.setAttribute("version", "1.2");
            Document doc = new Document(gexf);

            Element graph = new Element("graph");
            graph.setAttribute("mode", "static");
            graph.setAttribute("defaultedgetype", "undirected");
            graph.setNamespace(ns);
            doc.getRootElement().addContent(graph);

            Element attributes = new Element("attributes");
            attributes.setNamespace(ns);
            attributes.setAttribute("class", "node");
            graph.addContent(attributes);

            Element attr0 = new Element("attribute");
            attr0.setNamespace(ns);
            attr0.setAttribute("id", "0");
            attr0.setAttribute("title", "lang");
            attr0.setAttribute("type", "string");
            attributes.addContent(attr0);

            Element nodes = new Element("nodes");
            nodes.setNamespace(ns);
            graph.addContent(nodes);

            Element edges = new Element("edges");
            edges.setNamespace(ns);
            graph.addContent(edges);

            HashMap<String, Integer> nodeIDMap = new HashMap<>();
            HashSet<String> edgeSet = new HashSet<>();
            int nodeIndexCount = 0;
            int edgeIndexCount = 0;
            for (Entry entry : resultMap.entrySet()) {
                int tokenIndex1, tokenIndex2;
                double rating = (double) entry.getValue();
                ArrayList<Token> tokenList = (ArrayList<Token>) entry.getKey();
                if (!nodeIDMap.containsKey(tokenList.get(0).getTokenName() + "-" + tokenList.get(0).getLanguage())) {
                    nodeIDMap.put(tokenList.get(0).getTokenName() + "-" + tokenList.get(0).getLanguage(), nodeIndexCount);
                    tokenIndex1 = nodeIndexCount;
                    nodeIndexCount++;
                    Element node1 = new Element("node");
                    node1.setNamespace(ns);
                    node1.setAttribute(new Attribute("id", "" + tokenIndex1));
                    node1.setAttribute("label", tokenList.get(0).getTokenName());
                    nodes.addContent(node1);

                    Element attvalues1 = new Element("attvalues");
                    attvalues1.setNamespace(ns);
                    node1.addContent(attvalues1);

                    Element attvalue10 = new Element("attvalue");
                    attvalue10.setNamespace(ns);
                    attvalue10.setAttribute("for", "0");
                    attvalue10.setAttribute("value", tokenList.get(0).getLanguage());
                    attvalues1.addContent(attvalue10);

                } else {
                    tokenIndex1 = nodeIDMap.get(tokenList.get(0).getTokenName() + "-" + tokenList.get(0).getLanguage());
                }

                if (!nodeIDMap.containsKey(tokenList.get(1).getTokenName() + "-" + tokenList.get(1).getLanguage())) {
                    nodeIDMap.put(tokenList.get(1).getTokenName() + "-" + tokenList.get(1).getLanguage(), nodeIndexCount);
                    tokenIndex2 = nodeIndexCount;
                    nodeIndexCount++;
                    Element node2 = new Element("node");
                    node2.setNamespace(ns);
                    node2.setAttribute(new Attribute("id", "" + tokenIndex2));
                    node2.setAttribute("label", tokenList.get(1).getTokenName());
                    node2.setAttribute("lang", tokenList.get(1).getLanguage());
                    nodes.addContent(node2);

                    Element attvalues2 = new Element("attvalues");
                    attvalues2.setNamespace(ns);
                    node2.addContent(attvalues2);

                    Element attvalue20 = new Element("attvalue");
                    attvalue20.setNamespace(ns);
                    attvalue20.setAttribute("for", "0");
                    attvalue20.setAttribute("value", tokenList.get(1).getLanguage());
                    attvalues2.addContent(attvalue20);
                } else {
                    tokenIndex2 = nodeIDMap.get(tokenList.get(1).getTokenName() + "-" + tokenList.get(1).getLanguage());
                }
                if ((!edgeSet.contains(tokenIndex1 + "-" + tokenIndex2)) && (!edgeSet.contains(tokenIndex2 + "-" + tokenIndex1))) {
                    Element edge = new Element("edge");
                    edge.setNamespace(ns);
                    edge.setAttribute(new Attribute("id", "" + edgeIndexCount));
                    edgeIndexCount++;
                    edge.setAttribute("source", "" + tokenIndex1);
                    edge.setAttribute("target", "" + tokenIndex2);
                    edge.setAttribute("weight", "" + rating);
                    edgeSet.add(tokenIndex1 + "-" + tokenIndex2);
                    edges.addContent(edge);
                }

            }

            XMLOutputter xmlOutput = new XMLOutputter();

            // display nice nice
            xmlOutput.setFormat(Format.getPrettyFormat());
            createDir();
            xmlOutput.output(doc, new FileWriter("./output/" + filename + ".gexf"));
            //xmlOutput.output(doc, System.out);

        } catch (IOException io) {
            System.out.println(io.getMessage());
        }

    }

    public static void writeMultiPlot(ArrayList<TreeMap<Integer, Allocation>> sentenceMapList, String filename) {
        FileWriter fw = null;

        try {
            createDir();
            fw = new FileWriter("./output/" + filename + ".txt");
            fw.append("key1 key2");
            fw.append("\n");
            for (TreeMap<Integer, Allocation> sentenceList : sentenceMapList) {
                for (Entry entry : sentenceList.entrySet()) {
                    int key1 = (int) entry.getKey();
                    Allocation allocation = (Allocation) entry.getValue();
                    String appendString = key1 + " " + allocation.getSentenceID2();
                    fw.append(appendString);
                    fw.append("\n");
                }
            }

        } catch (IOException e) {
            System.err.println("Konnte Datei nicht erstellen");
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void writePlotIndexOnly(Map<Integer, Allocation> map, String filename) {
        FileWriter fw = null;

        try {
            createDir();
            fw = new FileWriter("./output/" + filename + ".txt");
            fw.append("key1 key2");
            fw.append("\n");
            for (Entry entry : map.entrySet()) {
                int key1 = (int) entry.getKey();
                Allocation allocation = (Allocation) entry.getValue();
                String appendString = key1 + " " + allocation.getSentenceID2();
                fw.append(appendString);
                fw.append("\n");
            }
        } catch (IOException e) {
            System.err.println("Konnte Datei nicht erstellen");
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void writeDebugStatistic(Map<Integer, Allocation> map, String filename) {
        FileWriter fw = null;

        try {
            createDir();
            fw = new FileWriter("./output/" + filename + ".txt");
            fw.append("key1 key2 #ratings avgRating alloRating");
            fw.append("\n");
            for (Entry entry : map.entrySet()) {
                int key1 = (int) entry.getKey();
                Allocation allocation = (Allocation) entry.getValue();
                String appendString = key1 + " " + allocation.getSentenceID2() + " " + allocation.getTokenPairList().size() + " " + allocation.getAvgTokenSimilarityRating() + " " + allocation.getAllocationRating();
                fw.append(appendString.replaceAll("\\.", ","));
                fw.append("\n");
            }
        } catch (IOException e) {
            System.err.println("Konnte Datei nicht erstellen");
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void writeSentence(ArrayList<Sentence> sentenceList, String filename) {
        FileWriter fw = null;

        try {
            createDir();
            fw = new FileWriter("./output/" + filename + ".txt");
            for (Sentence sentence : sentenceList) {
                fw.append(sentence.getSentenceNumber() + " " + sentence.getFulltext());
                fw.append("\n");
            }
        } catch (IOException e) {
            System.err.println("Konnte Datei nicht erstellen");
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
