package org.hkrdi.eden.ggm.media.coherentspace;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.beryx.awt.color.ColorFactory;
import org.hkrdi.eden.ggm.media.DrawingOption;
import org.hkrdi.eden.ggm.media.DrawingStyle;
import org.hkrdi.eden.ggm.media.coherentspace.layerprocessor.LayerProcessorManager;
import org.hkrdi.eden.ggm.media.utils.Graphics2dUtils;
import org.hkrdi.eden.ggm.repository.entity.DataMap;
import org.hkrdi.eden.ggm.service.coherentspace.ie.ApplicationDataParameter;
import org.hkrdi.eden.ggm.service.geometry.DataMapFilterUtil;
import org.hkrdi.eden.ggm.service.geometry.SpatialService;
import org.hkrdi.eden.ggm.service.geometry.SpatialUtil;
import org.hkrdi.eden.ggm.service.geometry.TransformUtil;
import org.springframework.cop.support.BeanUtil;
import org.springframework.data.geo.Point;

public final class HexavalentNetworkGraphics2d {

    public static void drawLayer(BufferedImage bufferedImage, List<DataMap> networkAddresses,
                                 List<DataMap> networkOuter, DrawingOption drawingOption) {
        Graphics2D graphics2D = Graphics2dUtils.constructGraphics2D(bufferedImage, drawingOption.getBottomRight());
        graphics2D.setStroke(new BasicStroke((float) drawingOption.getDrawingStyle().getPen()));
        graphics2D.setColor(ColorFactory.web("#" + drawingOption.getDrawingStyle().getColor(),
                drawingOption.getDrawingStyle().getAlpha()));
        Font f = graphics2D.getFont();
        graphics2D.setFont(new Font(f.getName(), Font.BOLD, 24));
        networkOuter.parallelStream().filter(d ->Graphics2dUtils.filterByInBound(d, drawingOption)).forEach(route -> drawRoute(graphics2D, route, drawingOption));
        networkAddresses.parallelStream().filter(d ->Graphics2dUtils.filterByInBound(d, drawingOption)).forEach(address -> drawAddress(graphics2D, address, drawingOption, 2));
        graphics2D.setColor(Graphics2dUtils.getComplementary(graphics2D.getColor()));
        networkAddresses.parallelStream().filter(d ->Graphics2dUtils.filterByInBound(d, drawingOption)).forEach(address -> drawAddress(graphics2D, address, drawingOption, 1));
    }


	public static void drawLayerBook(BufferedImage bufferedImage, String network, boolean addressIndex,
			boolean trivalentLogic, boolean clusterIndex, boolean clusterType, boolean book,
			DrawingOption drawingOption, int frame, Map<String,String> selectedDataMapIds) {
		DrawingOption localDrawingOption = new DrawingOption(new DrawingStyle(4.4,//drawingOption.getDrawingStyle().getPen(), 
																				drawingOption.getDrawingStyle().getColor(),
																				drawingOption.getDrawingStyle().getAlpha()), 
															drawingOption.getCenter(), drawingOption.getBottomRight(),drawingOption.getScale());

		
		Graphics2D graphics2D = Graphics2dUtils.constructGraphics2D(bufferedImage, localDrawingOption.getBottomRight());
		SpatialService spatialService = BeanUtil.getBean(SpatialService.class);
		Font f = graphics2D.getFont();
		graphics2D.setFont(new Font(f.getName(), Font.BOLD, 26));
		graphics2D.setStroke(new BasicStroke(4.4f));//(float) drawingOption.getDrawingStyle().getPen()));
		
		//start disabled clusters
		graphics2D.setColor(ColorFactory.web("#f0f0f0", localDrawingOption.getDrawingStyle().getAlpha()));
		spatialService.getOuterRoutes(network).parallelStream()
				.filter(d -> Graphics2dUtils.filterByInBound(d, localDrawingOption))
				.forEach(route -> drawRoute(graphics2D, route, localDrawingOption));

		graphics2D.setColor(ColorFactory.web("#e0e0e0", localDrawingOption.getDrawingStyle().getAlpha()));
		spatialService.getAddresses(network).parallelStream()
				.filter(d -> Graphics2dUtils.filterByInBound(d, localDrawingOption))
				.forEach(address -> drawAddress(graphics2D, address, localDrawingOption, 3));
		//end disabled clusters
		
		graphics2D.setColor(ColorFactory.web("#6b89d8",// + localDrawingOption.getDrawingStyle().getColor(),
				localDrawingOption.getDrawingStyle().getAlpha()));
//		graphics2D.setColor(ColorFactory.web("#f000ff", drawingOption.getDrawingStyle().getAlpha()));
//		graphics2D.setColor(ColorFactory.LIGHTGRAY);
		spatialService.getOuterRoutes(network).parallelStream()
				.filter(d -> d.getAddressIndex() >= 0 && d.getAddressIndex() <= 72 && d.getToAddressIndex() >= 0
						&& d.getToAddressIndex() <= 72 && Graphics2dUtils.filterByInBound(d, localDrawingOption))
				.forEach(route -> drawRoute(graphics2D, route, localDrawingOption));
		
//		graphics2D.setColor(ColorFactory.web("#" + drawingOption.getDrawingStyle().getColor(),
//				drawingOption.getDrawingStyle().getAlpha()));
//		graphics2D.setColor(Graphics2dUtils.getComplementary(graphics2D.getColor()));

		graphics2D.setColor(ColorFactory.LIGHTGRAY);
//		graphics2D.setColor(Graphics2dUtils.getComplementary(graphics2D.getColor()));
		spatialService.getOuterRoutes(network).parallelStream()
				.filter(d ->
					((d.getAddressIndex() >= 0 && d.getAddressIndex() <= 72) && 
						(d.getToAddressIndex() >= 0 && d.getToAddressIndex() <= 72)) && 
					Graphics2dUtils.filterByInBound(d, localDrawingOption))
				.forEach(route -> drawRouteWithAnimation(graphics2D, route, localDrawingOption, frame));
		
		graphics2D.setColor(ColorFactory.DARKGRAY);
		spatialService.getAddresses(network).parallelStream()
				.filter(d ->
				((d.getAddressIndex() >= 0 && d.getAddressIndex() <= 72) && 
						(d.getToAddressIndex() >= 0 && d.getToAddressIndex() <= 72)) &&
					Graphics2dUtils.filterByInBound(d, localDrawingOption))
				.forEach(address -> drawAddress(graphics2D, address, localDrawingOption, 2));

		if (addressIndex) {
			graphics2D.setFont(new Font(f.getName(), Font.BOLD, 24));
			graphics2D.setColor(ColorFactory.web("#e0e0e0", localDrawingOption.getDrawingStyle().getAlpha()));
			spatialService.getRoutes(network).parallelStream()
					.filter(d -> Graphics2dUtils.filterByInBound(d, localDrawingOption))
					.forEach(address -> drawAddressIndex(graphics2D, address, localDrawingOption));
			graphics2D.setColor(ColorFactory.web("#000000", localDrawingOption.getDrawingStyle().getAlpha()));
			spatialService.getRoutes(network).parallelStream()
					.filter(d -> 
						((d.getAddressIndex() >= 0 && d.getAddressIndex() <= 72) && 
						(d.getToAddressIndex() >= 0 && d.getToAddressIndex() <= 72)) &&
						d.getClusterIndex()<=6 &&
						Graphics2dUtils.filterByInBound(d, localDrawingOption))
					.forEach(address -> drawAddressIndex(graphics2D, address, localDrawingOption));
			graphics2D.setColor(ColorFactory.web("#" + localDrawingOption.getDrawingStyle().getColor(),
					localDrawingOption.getDrawingStyle().getAlpha()));
		}
		if (clusterIndex) {
			graphics2D.setFont(new Font(f.getName(), Font.PLAIN, 26));
			graphics2D.setColor(ColorFactory.web("#e0e0e0", localDrawingOption.getDrawingStyle().getAlpha()));
			spatialService.getAddresses(network).parallelStream()
					.filter(DataMapFilterUtil.distinctByKey(DataMap::getClusterIndex))
					.filter(d -> Graphics2dUtils.filterByInBound(d, localDrawingOption))
					.forEach(cluster -> drawClusterIndex(graphics2D, cluster, localDrawingOption));
			graphics2D.setFont(new Font(f.getName(), Font.BOLD, 26));
			spatialService.getAddresses(network).parallelStream()
					.filter(DataMapFilterUtil.distinctByKey(DataMap::getClusterIndex))
					.filter(d ->
						d.getAddressIndex() >= 0 && d.getAddressIndex() <= 72 && 
						d.getToAddressIndex() >= 0 && d.getToAddressIndex() <= 72 &&
						Graphics2dUtils.filterByInBound(d, localDrawingOption))
					.forEach(cluster -> drawClusterIndex(graphics2D, cluster, localDrawingOption));
		}
		
//		drawNodeSelections(bufferedImage, network, addressIndex, trivalentLogic, clusterIndex, clusterType, book, drawingOption, frame, selectedDataMapIds, spatialService, graphics2D);
	}
    
    public static void drawLayer(BufferedImage bufferedImage, String network,
                                 boolean addressIndex, boolean trivalentLogic, boolean clusterIndex, boolean clusterType, boolean book,
								 ApplicationDataParameter applicationDataParameter,
                                 DrawingOption drawingOption, int frame, Map<String,String> selectedDataMapIds) {
    	
    	Graphics2D graphics2D = Graphics2dUtils.constructGraphics2D(bufferedImage, drawingOption.getBottomRight());

    	if (book) {
    		drawLayerBook(bufferedImage, network, addressIndex, trivalentLogic, clusterIndex, clusterType, book, drawingOption, frame, selectedDataMapIds);
    		
    		if(applicationDataParameter != null) {
    			drawApplicationDataParameter(graphics2D, drawingOption, network, applicationDataParameter);
    		}
    		return;
    	}
        SpatialService spatialService= BeanUtil.getBean(SpatialService.class);
        Font f = graphics2D.getFont();
        graphics2D.setFont(new Font(f.getName(), Font.BOLD, 26));
        graphics2D.setStroke(new BasicStroke((float) drawingOption.getDrawingStyle().getPen()));
        graphics2D.setColor(ColorFactory.web("#" + drawingOption.getDrawingStyle().getColor(), drawingOption.getDrawingStyle().getAlpha()));
        spatialService.getOuterRoutes(network).parallelStream().filter(d ->Graphics2dUtils.filterByInBound(d, drawingOption)).forEach(route -> drawRoute(graphics2D, route, drawingOption));

//      graphics2D.setColor(ColorFactory.web("#" + drawingOption.getDrawingStyle().getColor(),
//              drawingOption.getDrawingStyle().getAlpha()));
        graphics2D.setColor(ColorFactory.LIGHTGRAY);
        spatialService.getOuterRoutes(network).parallelStream().filter(d ->Graphics2dUtils.filterByInBound(d, drawingOption)).forEach(route -> drawRouteWithAnimation(graphics2D, route, drawingOption, frame));
        
        graphics2D.setColor(ColorFactory.web("#" + drawingOption.getDrawingStyle().getColor(), drawingOption.getDrawingStyle().getAlpha()));
        spatialService.getAddresses(network).parallelStream().filter(d ->Graphics2dUtils.filterByInBound(d, drawingOption)).forEach(address -> drawAddress(graphics2D, address, drawingOption, 2));
        
//        graphics2D.setColor(Graphics2dUtils.getComplementary(graphics2D.getColor()));
        graphics2D.setColor(ColorFactory.DARKGRAY);
        spatialService.getAddresses(network).parallelStream().filter(d ->Graphics2dUtils.filterByInBound(d, drawingOption)).forEach(address -> drawAddress(graphics2D, address, drawingOption, 1));
        graphics2D.setColor(Graphics2dUtils.getComplementary(graphics2D.getColor()));
        if(addressIndex) {
    		spatialService.getRoutes(network).parallelStream()
					.filter(d->Graphics2dUtils.filterByInBound(d, drawingOption))
					.forEach(address -> drawAddressIndex(graphics2D, address, drawingOption));
        }
        if(clusterType) {
            spatialService.getAddresses(network).parallelStream()
                    .filter(DataMapFilterUtil.distinctByKey(DataMap::getClusterIndex))
                    .filter(d->Graphics2dUtils.filterByInBound(d, drawingOption))
                    .forEach(cluster -> drawClusterType(graphics2D, cluster, drawingOption));
        }
        graphics2D.setColor(Graphics2dUtils.getComplementary(graphics2D.getColor()));
        if(trivalentLogic) {
            spatialService.getRoutes(network).parallelStream()
					.filter(d->Graphics2dUtils.filterByInBound(d, drawingOption))
					.forEach(address -> drawTrivalentLogic(graphics2D, address, drawingOption));
        }
        if(clusterIndex) {
            spatialService.getAddresses(network).parallelStream()
                    .filter(DataMapFilterUtil.distinctByKey(DataMap::getClusterIndex))
                    .filter(d->Graphics2dUtils.filterByInBound(d, drawingOption))
                    .forEach(cluster -> drawClusterIndex(graphics2D, cluster, drawingOption));
        }
		if(applicationDataParameter != null) {
			drawApplicationDataParameter(graphics2D, drawingOption, network, applicationDataParameter);
		}

//        drawNodeSelections(bufferedImage, network, addressIndex, trivalentLogic, clusterIndex, clusterType, book, drawingOption, frame, selectedDataMapIds, spatialService, graphics2D);
    }

	public static void drawApplicationDataParameter(Graphics2D graphics2D, DrawingOption drawingOption, String network, ApplicationDataParameter applicationDataParameter) {
		(new LayerProcessorManager()).paint(graphics2D, drawingOption, network, applicationDataParameter);
	}


	public static void drawNodeSelections(BufferedImage bufferedImage, String network,
            boolean addressIndex, boolean trivalentLogic, boolean clusterIndex, boolean clusterType, boolean book,
            DrawingOption drawingOption, int frame, Map<String,String> selectedDataMapIds, SpatialService spatialService, Graphics2D graphics2D) {
    	try {
	        if (selectedDataMapIds.containsKey("selectedNodesX")) {
	        	Long x = new Long(selectedDataMapIds.get("selectedNodesX"));
	        	Long y = new Long(selectedDataMapIds.get("selectedNodesY"));
	        	Boolean at = new Boolean(selectedDataMapIds.get("selectedNodesAt"));
	        	Optional<DataMap> foundDM = spatialService.getAddresses(network).parallelStream().filter(dm->
		        			(dm.getAtX().equals(x) && dm.getAtY().equals(y) && at) ||
		        			(dm.getToX().equals(x) && dm.getToY().equals(y) && !at)
	        			).findAny();
	        	if (foundDM.isPresent()) {
	        		graphics2D.setColor(ColorFactory.web("#00ff00", drawingOption.getDrawingStyle().getAlpha()));
	        		if ((foundDM.get().getAtX().equals(x) && foundDM.get().getAtY().equals(y) && at)) {
	        			drawAddress(graphics2D, foundDM.get(), drawingOption, 5);
	        		}else {
	        			drawToAddress(graphics2D, foundDM.get(), drawingOption, 5);
	        		}
//	        		graphics2D.setStroke(new BasicStroke((float) drawingOption.getDrawingStyle().getPen() + 3));
//					spatialService.getRoutes(network).parallelStream()
//							.filter(d ->Graphics2dUtils.filterByInBound(d, drawingOption)).filter(d->
//				        		(d.getAtX().equals(x) && d.getAtY().equals(y) && at) ||
//			        			(d.getToX().equals(x) && d.getToY().equals(y) && !at)
//	        				).forEach(
//	        						route -> {
//	        							graphics2D.setColor(ColorFactory.web("#00ff99", drawingOption.getDrawingStyle().getAlpha()));
//	        							drawRoute(graphics2D, route, drawingOption);
//	        							graphics2D.setColor(ColorFactory.web("#ffff00", drawingOption.getDrawingStyle().getAlpha()));
//	        							if ((route.getToX().equals(x) && route.getToY().equals(y) && !at)) {	        								
//	        								drawAddress(graphics2D, route, drawingOption, 5);
//	        							}else {
//	        								drawToAddress(graphics2D, route, drawingOption, 5);
//	        							}
//	        						}
//	        						);
	        	}
	        }
        }catch(NumberFormatException e) {
        	e.printStackTrace();
        }
        
        try {
	        if (selectedDataMapIds.containsKey("dblClickSelectedNodesXs")) {
	        	List xS = getListFromRequestParam(selectedDataMapIds.get("dblClickSelectedNodesXs"));
	        	List yS = getListFromRequestParam(selectedDataMapIds.get("dblClickSelectedNodesYs"));
	        	List atS = getListFromRequestParam(selectedDataMapIds.get("dblClickSelectedNodesAts"));
	        	for (int index = 0; index<xS.size(); index++) {
		        	Long x = new Long(xS.get(index).toString());
		        	Long y = new Long(yS.get(index).toString());
		        	Boolean at = new Boolean(atS.get(index).toString());
		        	Optional<DataMap> foundDM = spatialService.getAddresses(network).stream().filter(dm->
					(dm.getAtX().equals(x) && dm.getAtY().equals(y)
							&& at)/*
									 * || (dm.getToX().equals(x) && dm.getToY().equals(y) && !at)
									 */
		        			).findFirst();
		        	if (foundDM.isPresent()) {
		        		graphics2D.setColor(ColorFactory.web("#0000ff", drawingOption.getDrawingStyle().getAlpha()));
		        		if (foundDM.get().getAtX().equals(x) && foundDM.get().getAtY().equals(y) && at) {
		        			drawAddress(graphics2D, foundDM.get(), drawingOption, 4);
		        		}else {
		        			graphics2D.setColor(ColorFactory.web("#7811ff", drawingOption.getDrawingStyle().getAlpha()));
		        			drawToAddress(graphics2D, foundDM.get(), drawingOption, 4);
		        		}
		        	}else {
		        		foundDM = spatialService.getAddresses(network).parallelStream().filter(dm->
		        			(dm.getToX().equals(x) && dm.getToY().equals(y) && !at)).findAny();
			        	if (foundDM.isPresent()) {
			        		graphics2D.setColor(ColorFactory.web("#7811ff", drawingOption.getDrawingStyle().getAlpha()));
		        			drawToAddress(graphics2D, foundDM.get(), drawingOption, 4);
			        	}
		        	}
	        	}
	        }
        }catch(NumberFormatException e) {
        	e.printStackTrace();
        }
    }
    
    public static List getListFromRequestParam(String value) {
    	return Arrays.asList(value.replace("[", "").replace("]", "").split(",")).stream().collect(Collectors.toList());
    }
    

    public static void drawClusterType(Graphics2D graphics2D, DataMap dataMap, DrawingOption drawingOption) {
        Point graphics2dVertex = TransformUtil.graphics2dPoint(SpatialUtil.getClusterPoint(dataMap),
                drawingOption.getCenter(), drawingOption.getBottomRight(), drawingOption.getScale());
        graphics2D.drawString(dataMap.getClusterType(), (float)graphics2dVertex.getX(), (float)graphics2dVertex.getY());
    }

    //TODO fara parametru de rezolutie si private
    @Deprecated
	public static void drawRoute(Graphics2D graphics2D, DataMap dataMap, DrawingOption drawingOption, double resolution) {
		Point graphics2dHead = TransformUtil.graphics2dPoint(SpatialUtil.getAddressPoint(dataMap),
				drawingOption.getCenter(), drawingOption.getBottomRight(), drawingOption.getScale());
		Point graphics2dTail = TransformUtil.graphics2dPoint(SpatialUtil.getToAddressPoint(dataMap),
				drawingOption.getCenter(), drawingOption.getBottomRight(), drawingOption.getScale());
		graphics2D.draw(new Line2D.Double(graphics2dHead.getX(), graphics2dHead.getY(),
				graphics2dTail.getX(), graphics2dTail.getY()));
	}


	@Deprecated
	public static void drawRoute(Graphics2D graphics2D, DataMap dataMap, DrawingOption drawingOption) {
		drawRoute(graphics2D, dataMap, drawingOption, 2.5);
	}

	public static void drawRoute(Graphics2D graphics2D, String network, Long fromAddressIndex, Long toAddressIndex, DrawingOption drawingOption, double resolution) {
		List<DataMap> networkEdges = BeanUtil.getBean(SpatialService.class).getRoutes(network);
		drawRoute(graphics2D, networkEdges.stream().filter(DataMapFilterUtil.byAddressIndex(fromAddressIndex))
				.filter(DataMapFilterUtil.byToAddressIndex(toAddressIndex)).findAny().get(), drawingOption, resolution);
	}

	private static void drawRouteWithArrow(Graphics2D graphics2D, DataMap dataMap, DrawingOption drawingOption, double resolution) {
		Point graphics2dHead = TransformUtil.graphics2dPoint(SpatialUtil.getAddressPoint(dataMap),
				drawingOption.getCenter(), drawingOption.getBottomRight(), drawingOption.getScale());
		Point graphics2dTail = TransformUtil.graphics2dPoint(SpatialUtil.getToAddressPoint(dataMap),
				drawingOption.getCenter(), drawingOption.getBottomRight(), drawingOption.getScale());
		graphics2D.draw(new Line2D.Double(graphics2dHead.getX(), graphics2dHead.getY(),
				graphics2dTail.getX(), graphics2dTail.getY()));
		graphics2D.draw(Graphics2dUtils.middleArrow1Line(graphics2dHead, graphics2dTail, drawingOption, resolution));
		graphics2D.draw(Graphics2dUtils.middleArrow2Line(graphics2dHead, graphics2dTail, drawingOption, resolution));
	}

	public static void drawRouteWithArrow(Graphics2D graphics2D, String network, Long fromAddressIndex, Long toAddressIndex, DrawingOption drawingOption, double resolution) {
		List<DataMap> networkEdges = BeanUtil.getBean(SpatialService.class).getRoutes(network);

		//TODO de trimis din ImageLayer in ordinea corecta, ca sa nu mai caut aici from->to
		Optional<DataMap> dm = networkEdges.stream().filter(DataMapFilterUtil.byAddressIndex(fromAddressIndex))
													.filter(DataMapFilterUtil.byToAddressIndex(toAddressIndex)).findAny();
		if (!dm.isPresent()) {
			dm = networkEdges.stream().filter(DataMapFilterUtil.byAddressIndex(toAddressIndex))
									  .filter(DataMapFilterUtil.byToAddressIndex(fromAddressIndex)).findAny();
		}
		//
		drawRouteWithArrow(graphics2D, dm.get(), drawingOption, resolution);
	}

    public static void drawRouteWithAnimation(Graphics2D graphics2D, DataMap dataMap, DrawingOption drawingOption, int frame) {
        int[] frameParts = frameParts(frame);
        for ( int i = 0 ; i < frameParts.length; i=i+2) {
            Point graphics2dHead = TransformUtil
                    .graphics2dPoint(TransformUtil
                                    .splitPoint(SpatialUtil.getAddressPoint(dataMap),
                                            SpatialUtil.getToAddressPoint(dataMap), frameParts[i] ,12),
                            drawingOption.getCenter(), drawingOption.getBottomRight(), drawingOption.getScale());
            Point graphics2dTail = TransformUtil
                    .graphics2dPoint(TransformUtil
                                    .splitPoint(SpatialUtil.getAddressPoint(dataMap),
                                            SpatialUtil.getToAddressPoint(dataMap), frameParts[i + 1] ,12),
                            drawingOption.getCenter(), drawingOption.getBottomRight(), drawingOption.getScale());
            
//            Color c = new Color(graphics2D.getColor().getRed(), graphics2D.getColor().getGreen(), graphics2D.getColor().getBlue());
//            graphics2D.setColor(Color.white);
            graphics2D
                    .draw(new Line2D.Double(graphics2dHead.getX(),
                            graphics2dHead.getY(), graphics2dTail.getX(), graphics2dTail.getY()));
//            graphics2D.setColor(c);
//            graphics2D.draw(Graphics2dUtils.headArrow1Line(graphics2dHead, graphics2dTail, drawingOption, 1.5));
//            graphics2D.draw(Graphics2dUtils.headArrow2Line(graphics2dHead, graphics2dTail, drawingOption, 1.5));
        }
    }

    public static void drawClusterIndex(Graphics2D graphics2D, DataMap dataMap, DrawingOption drawingOption) {
        Point graphics2dVertex = TransformUtil.graphics2dPoint(SpatialUtil.getClusterPoint(dataMap),
                drawingOption.getCenter(), drawingOption.getBottomRight(), drawingOption.getScale());
        
        drawStringInCenter(graphics2D, dataMap.getClusterIndex() + "", drawingOption, graphics2dVertex);
    }

    public static void drawClusterPosition(Graphics2D graphics2D, DataMap dataMap, DrawingOption drawingOption) {
        Point graphics2dVertex = TransformUtil.graphics2dPoint(SpatialUtil.getClusterPoint(dataMap),
                drawingOption.getCenter(), drawingOption.getBottomRight(), drawingOption.getScale());
        graphics2D.drawString(dataMap.getClusterIndex() + "", (float)graphics2dVertex.getX(), (float)graphics2dVertex.getY());
    }


    public static void drawAddressIndex(Graphics2D graphics2D, DataMap dataMap, DrawingOption drawingOption) {
//        Point graphics2dVertex = TransformUtil.graphics2dPoint(SpatialUtil.getAddressPoint(dataMap),
//                drawingOption.getCenter(), drawingOption.getBottomRight(), drawingOption.getScale());
    	Point graphics2dVertex = TransformUtil.graphics2dPoint(
        		TransformUtil.splitPoint(SpatialUtil.getAddressPoint(dataMap), SpatialUtil.getClusterPoint(dataMap), 3, 10),
                drawingOption.getCenter(), drawingOption.getBottomRight(), drawingOption.getScale());
        Font f = graphics2D.getFont();
		graphics2D.setFont(new Font(f.getName(), Font.BOLD, 24));
        String addressIndexText = dataMap.getAddressIndex() + "";
        
        drawStringInCenter(graphics2D, addressIndexText, drawingOption, graphics2dVertex);
    }

	public static void drawAddressIndexInCenter(Graphics2D graphics2D, DataMap dataMap, DrawingOption drawingOption) {
		drawAddressIndexInCenter(graphics2D, dataMap, drawingOption, 20);
	}

	public static void drawStringInCenter(Graphics2D graphics2D, String s, DrawingOption drawingOption, Point graphics2dVertex) {
		Font f = graphics2D.getFont();
		graphics2D.setFont(new Font(f.getName(), f.getStyle(), f.getSize()));
		FontMetrics fontMetrics = graphics2D.getFontMetrics();
		graphics2D.drawString(s, (float)graphics2dVertex.getX() - fontMetrics.stringWidth(s) / 2,
				(float)graphics2dVertex.getY() - fontMetrics.getHeight() / 2 + fontMetrics.getAscent());
	}
	
	public static void drawAddressIndexInCenter(Graphics2D graphics2D, DataMap dataMap, DrawingOption drawingOption, int fontSize) {
		Point graphics2dVertex = TransformUtil.graphics2dPoint(SpatialUtil.getAddressPoint(dataMap),
				drawingOption.getCenter(), drawingOption.getBottomRight(), drawingOption.getScale());
		Font f = graphics2D.getFont();
		graphics2D.setFont(new Font(f.getName(), Font.BOLD, fontSize));
		String addressIndexText = dataMap.getAddressIndex() + "";
		drawStringInCenter(graphics2D, addressIndexText, drawingOption, graphics2dVertex);
	}

	public static void drawTrivalentLogic(Graphics2D graphics2D, DataMap dataMap, DrawingOption drawingOption) {
        Point graphics2dVertex = TransformUtil.graphics2dPoint(
        		TransformUtil.splitPoint(SpatialUtil.getAddressPoint(dataMap), SpatialUtil.getClusterPoint(dataMap), 2, 10),
                drawingOption.getCenter(), drawingOption.getBottomRight(), drawingOption.getScale());
		Font f = graphics2D.getFont();
		graphics2D.setFont(new Font(f.getName(), Font.BOLD, 26));
        FontMetrics fontMetrics = graphics2D.getFontMetrics();
        String trivalentLogicText =  "";
        if(dataMap.getTrivalentLogic().equalsIgnoreCase("SOURCE")) {
        	trivalentLogicText = "S";
		}
		if(dataMap.getTrivalentLogic().equalsIgnoreCase("SENSOR")) {
			trivalentLogicText = "&";
		}
		if(dataMap.getTrivalentLogic().equalsIgnoreCase("DECIDER")) {
			trivalentLogicText = "D";
		}
        graphics2D.drawString(trivalentLogicText, (float)graphics2dVertex.getX() - fontMetrics.stringWidth(trivalentLogicText) / 2,
				(float)graphics2dVertex.getY() - fontMetrics.getHeight() / 2 + fontMetrics.getAscent());
        graphics2D.setFont(f);
    }

	@Deprecated
    public static void drawAddress(Graphics2D graphics2D, DataMap dataMap, DrawingOption drawingOption, double resolution) {
        Point graphics2dVertex = TransformUtil.graphics2dPoint(SpatialUtil.getAddressPoint(dataMap),
                drawingOption.getCenter(), drawingOption.getBottomRight(), drawingOption.getScale());
        Graphics2dUtils.drawPoint(graphics2D, graphics2dVertex, drawingOption, resolution);
    }

    @Deprecated
    public static void drawToAddress(Graphics2D graphics2D, DataMap dataMap, DrawingOption drawingOption, double resolution) {
        Point graphics2dVertex = TransformUtil.graphics2dPoint(SpatialUtil.getToAddressPoint(dataMap),
                drawingOption.getCenter(), drawingOption.getBottomRight(), drawingOption.getScale());
        Graphics2dUtils.drawPoint(graphics2D, graphics2dVertex, drawingOption, resolution);
    }

	public static void drawAddressForIndex(Graphics2D graphics2D, String network, Long addressIndex, DrawingOption drawingOption, double resolution) {
    	List<DataMap> networkEdges = BeanUtil.getBean(SpatialService.class).getRoutes(network);
    	DataMap dataMap = networkEdges.stream()
				.filter(DataMapFilterUtil.byAddressIndex(addressIndex)).findAny()
				.orElse(networkEdges.stream().filter(DataMapFilterUtil.byToAddressIndex(addressIndex)).findAny().get());
		Point graphics2dVertex = TransformUtil.graphics2dPoint(dataMap.getAddressIndex().compareTo(addressIndex) == 0 ?
						SpatialUtil.getAddressPoint(dataMap) :  SpatialUtil.getToAddressPoint(dataMap),
				drawingOption.getCenter(), drawingOption.getBottomRight(), drawingOption.getScale());
		Graphics2dUtils.drawPoint(graphics2D, graphics2dVertex, drawingOption, resolution);
	}


    public static int[] frameParts(int frame) {
        if ( frame == 1 ) {
            return new int [] { 0 , 1 , 4 , 5 , 8 , 9 } ;
        } else if ( frame == 2 ) {
            return new int [] { 1 , 2 , 5 , 6 , 9 , 10 } ;
        } else if ( frame == 3 ) {
            return new int [] { 2 , 3 , 6 , 7 , 10 , 11 } ;
        } else if ( frame == 4 ) {
            return new int [] { 3 , 4 , 7 , 8 , 11 , 12 } ;
        }
        return new int [] { 0 , 12 } ;
    }

}
