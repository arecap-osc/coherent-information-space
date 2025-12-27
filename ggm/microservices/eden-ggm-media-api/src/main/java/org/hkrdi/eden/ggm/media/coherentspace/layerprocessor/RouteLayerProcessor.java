package org.hkrdi.eden.ggm.media.coherentspace.layerprocessor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import org.beryx.awt.color.ColorFactory;
import org.hkrdi.eden.ggm.media.DrawingOption;
import org.hkrdi.eden.ggm.media.DrawingStyle;
import org.hkrdi.eden.ggm.media.coherentspace.HexavalentNetworkGraphics2d;
import org.hkrdi.eden.ggm.service.coherentspace.ie.ApplicationDataParameter;

public class RouteLayerProcessor implements LayerProcessorI{
	private static final double SELECTION_PEN_SIZE = 3.0d;
	private static final Color firstMainSelectionColor = ColorFactory.web("#fca103");
	private static final Color firstBorderSelectionColor = ColorFactory.web("#666666");

	private static final Color secondMainSelectionColor = ColorFactory.web("#fca103");
	private static final Color secondBorderSelectionColor = ColorFactory.web("#666666");
	private static final Color secondCenterSelectionColor = ColorFactory.web("#666666");
	
	private static final Color thirdMainSelectionColor = ColorFactory.web("#fca103");
	private static final Color thirdBorderSelectionColor = ColorFactory.web("#666666");
	private static final Color thirdCenterSelectionColor = ColorFactory.web("#666666");
	
	private static final Color fourthMainSelectionColor = ColorFactory.web("#00aa00");
	private static final Color fourthBorderSelectionColor = ColorFactory.web("#666666");

	@Override
	public boolean accept(String layerType) {
		if ("ROUTE".equals(layerType)) {
			return true;
		}
		return false;
	}

	@Override
	public void process(Graphics2D graphics2D, DrawingOption drawingOption, String network, ApplicationDataParameter applicationDataParameter) {
    	applicationDataParameter.getDataMapParameter().getSelectedRoutesFirstList().stream().forEach(pair -> drawRouteSelection(graphics2D, drawingOption, network, pair.getFirst(), pair.getSecond()));
    	
        applicationDataParameter.getDataMapParameter().getSelectedNodesIndexFourthList().stream().forEach(nodeIndex -> drawSelectionNodesFourthList(graphics2D, drawingOption, network, nodeIndex));
    	applicationDataParameter.getDataMapParameter().getSelectedNodesIndexThirdList().stream().forEach(nodeIndex -> drawSelectionNodesThirdList(graphics2D, drawingOption, network, nodeIndex));
    	applicationDataParameter.getDataMapParameter().getSelectedNodesIndexSecondList().stream().forEach(nodeIndex -> drawSelectionNodesSecondList(graphics2D, drawingOption, network, nodeIndex));
    	applicationDataParameter.getDataMapParameter().getSelectedNodesIndexFirstList().stream().forEach(nodeIndex -> drawSelectionNodesFirstList(graphics2D, drawingOption, network, nodeIndex));
    }

    private static void drawRouteSelection(Graphics2D graphics2D, final DrawingOption drawingOption, String network, Long fromNodeIndex, Long toNodeIndex) {
    	DrawingOption localDrawingOption = new DrawingOption(new DrawingStyle(SELECTION_PEN_SIZE,//drawingOption.getDrawingStyle().getPen(), 
															    			drawingOption.getDrawingStyle().getColor(),
															    			drawingOption.getDrawingStyle().getAlpha()), 
    														drawingOption.getCenter(), drawingOption.getBottomRight(),drawingOption.getScale());

        graphics2D.setColor(firstBorderSelectionColor);
        graphics2D.setStroke(new BasicStroke(6));
        HexavalentNetworkGraphics2d.drawRouteWithArrow(graphics2D, network, fromNodeIndex, toNodeIndex, localDrawingOption,  4d);
        
        graphics2D.setColor(firstMainSelectionColor);
        graphics2D.setStroke(new BasicStroke(4));
        HexavalentNetworkGraphics2d.drawRouteWithArrow(graphics2D, network, fromNodeIndex, toNodeIndex, localDrawingOption,  4d);
        
        graphics2D.setStroke(new BasicStroke((float) localDrawingOption.getDrawingStyle().getPen()));
    }

    private static void drawSelectionNodesFirstList(Graphics2D graphics2D, final DrawingOption drawingOption, String network, Long nodeIndex) {
    	DrawingOption localDrawingOption = new DrawingOption(new DrawingStyle(SELECTION_PEN_SIZE,//drawingOption.getDrawingStyle().getPen(), 
															    			drawingOption.getDrawingStyle().getColor(),
															    			drawingOption.getDrawingStyle().getAlpha()), 
															drawingOption.getCenter(), drawingOption.getBottomRight(),drawingOption.getScale());

		graphics2D.setColor(firstBorderSelectionColor);
		HexavalentNetworkGraphics2d.drawAddressForIndex(graphics2D, network, nodeIndex, localDrawingOption, 6.5);
		
		graphics2D.setColor(firstMainSelectionColor);
		HexavalentNetworkGraphics2d.drawAddressForIndex(graphics2D, network, nodeIndex, localDrawingOption, 5.5);
    }
    
    private static void drawSelectionNodesSecondList(Graphics2D graphics2D, final DrawingOption drawingOption, String network, Long nodeIndex) {
    	DrawingOption localDrawingOption = new DrawingOption(new DrawingStyle(SELECTION_PEN_SIZE,//drawingOption.getDrawingStyle().getPen(), 
															    			drawingOption.getDrawingStyle().getColor(),
															    			drawingOption.getDrawingStyle().getAlpha()), 
															drawingOption.getCenter(), drawingOption.getBottomRight(),drawingOption.getScale());

		graphics2D.setColor(secondBorderSelectionColor);
		HexavalentNetworkGraphics2d.drawAddressForIndex(graphics2D, network, nodeIndex, localDrawingOption, 6.5);
		
		graphics2D.setColor(secondMainSelectionColor);
		HexavalentNetworkGraphics2d.drawAddressForIndex(graphics2D, network, nodeIndex, localDrawingOption, 5.5);
		
		graphics2D.setColor(secondCenterSelectionColor);
		HexavalentNetworkGraphics2d.drawAddressForIndex(graphics2D, network, nodeIndex, localDrawingOption, 2);
    }

    private static void drawSelectionNodesThirdList(Graphics2D graphics2D, final DrawingOption drawingOption, String network, Long nodeIndex) {
    	DrawingOption localDrawingOption = new DrawingOption(new DrawingStyle(SELECTION_PEN_SIZE,//drawingOption.getDrawingStyle().getPen(), 
															    			drawingOption.getDrawingStyle().getColor(),
															    			drawingOption.getDrawingStyle().getAlpha()), 
															drawingOption.getCenter(), drawingOption.getBottomRight(),drawingOption.getScale());

    	graphics2D.setColor(thirdBorderSelectionColor);
		HexavalentNetworkGraphics2d.drawAddressForIndex(graphics2D, network, nodeIndex, localDrawingOption, 4);
		
		graphics2D.setColor(thirdMainSelectionColor);
		HexavalentNetworkGraphics2d.drawAddressForIndex(graphics2D, network, nodeIndex, localDrawingOption, 3);
		
		graphics2D.setColor(thirdCenterSelectionColor);
		HexavalentNetworkGraphics2d.drawAddressForIndex(graphics2D, network, nodeIndex, localDrawingOption, 1);
    }

    private static void drawSelectionNodesFourthList(Graphics2D graphics2D, final DrawingOption drawingOption, String network, Long nodeIndex) {
    	DrawingOption localDrawingOption = new DrawingOption(new DrawingStyle(SELECTION_PEN_SIZE,//drawingOption.getDrawingStyle().getPen(), 
															    			drawingOption.getDrawingStyle().getColor(),
															    			drawingOption.getDrawingStyle().getAlpha()), 
															drawingOption.getCenter(), drawingOption.getBottomRight(),drawingOption.getScale());

		graphics2D.setColor(fourthBorderSelectionColor);
		HexavalentNetworkGraphics2d.drawAddressForIndex(graphics2D, network, nodeIndex, localDrawingOption, 3.5);
		
		graphics2D.setColor(fourthMainSelectionColor);
		HexavalentNetworkGraphics2d.drawAddressForIndex(graphics2D, network, nodeIndex, localDrawingOption, 2.5);
    }

}
