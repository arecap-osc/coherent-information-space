package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.layer;

import org.beryx.awt.color.ColorFactory;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRenderStyle;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRenderStyleAware;
import org.hkrdi.eden.ggm.vaadin.console.media.MediaRendererTransform;
import org.hkrdi.eden.ggm.vaadin.console.media.PNGMediaRendererDrawer;
import org.hkrdi.eden.ggm.vaadin.console.media.util.CoherentSpaceGraphics2DUtils;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.NetworkMediaLayerName;
import org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.media.NetworkMediaLayerStyle;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.ClusterBean;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.CoherentSpaceService;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.EdgeBean;
import org.hkrdi.eden.ggm.vaadin.console.service.coherentspace.NodeBean;
import org.springframework.cop.support.BeanUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HexavalentOtherNonAnimatedMediaLayer implements PNGMediaRendererDrawer, NetworkMediaLayerName, MediaRenderStyleAware {

	private String network;

	private Boolean needRefresh = false;

	private List<EdgeBean> outerEdges = new ArrayList<>();
	private List<NodeBean> nodes = new ArrayList<>();
	private List<ClusterBean> clusters = new ArrayList<>();

	private NetworkMediaLayerStyle networkStyle;

	public HexavalentOtherNonAnimatedMediaLayer() {
		super();
	}
	
	public HexavalentOtherNonAnimatedMediaLayer(String network) {
		super();
		setNetwork(network);	
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
		
		outerEdges.addAll(BeanUtil.getBean(CoherentSpaceService.class).findNetworkOuterEdges(network));
		nodes.addAll(BeanUtil.getBean(CoherentSpaceService.class).getNetworkNodes(network));
		clusters.addAll(BeanUtil.getBean(CoherentSpaceService.class).getNetworkClusters(network));
	}

	@Override
	public Boolean isNeedRefresh() {
		return needRefresh;
	}

	@Override
	public void setNeedRefresh(Boolean needRefresh) {
		this.needRefresh = needRefresh;
	}

	public void drawContent(Graphics2D graphics2d, MediaRendererTransform mrt) {
		CoherentSpaceGraphics2DUtils.drawPoints(graphics2d, nodes, mrt, networkStyle, 2.2);

		graphics2d.setColor(ColorFactory.BLACK);
		if (networkStyle.isAddressIndexVisible()) {
			//here should set style
			clusters.stream().forEach(clusterBean -> {
				BeanUtil.getBean(CoherentSpaceService.class).findClusterNodes(clusterBean).forEach(nodeBean ->  {
					//se poate face privata drawString si sa se ia stringul prin metode specializate ex: drawAddressIndex
					String addressIndex = nodeBean.getAddressIndex() + "";
					CoherentSpaceGraphics2DUtils.drawString(graphics2d, addressIndex, nodeBean, clusterBean, 3, 8, mrt);
				});
			});
		}
		Font font = graphics2d.getFont();
        graphics2d.setFont(new Font(font.getName(), Font.BOLD, font.getSize()+2));
        
		if (networkStyle.isClusterIndexVisible()) {
			//here should set style
			clusters.stream().forEach(clusterBean -> {
				BeanUtil.getBean(CoherentSpaceService.class).findClusterNodes(clusterBean).forEach(nodeBean ->  {
					//se poate face privata drawString si sa se ia stringul prin metode specializate ex: drawAddressIndex
					String clusterIndex = clusterBean.getClusterIndex()+ "";
					CoherentSpaceGraphics2DUtils.drawString(graphics2d, clusterIndex, clusterBean, mrt);
				});
			});
		}
		graphics2d.setFont(font);
		
		if (networkStyle.isTrivalentLogicVisible()) {
			//here should set style
			clusters.stream().forEach(clusterBean -> {
				BeanUtil.getBean(CoherentSpaceService.class).findClusterNodes(clusterBean).forEach(nodeBean -> {
					//se poate face privata drawString si sa se ia stringul prin metode specializate ex: drawTrivalentLogic

					String trivalentLogic = BeanUtil.getBean(CoherentSpaceService.class)
							.getTrivalentLogic(nodeBean, clusterBean);
					if(trivalentLogic.equalsIgnoreCase("SOURCE")) {
						trivalentLogic = "S";
					}
					if(trivalentLogic.equalsIgnoreCase("SENSOR")) {
						trivalentLogic = "&";
					}
					if(trivalentLogic.equalsIgnoreCase("DECIDER")) {
						trivalentLogic = "D";
					}
					CoherentSpaceGraphics2DUtils.drawString(graphics2d, trivalentLogic, nodeBean, clusterBean, 2, 10, mrt);
				});
			});
		}
	}

	@Override
	public void setMediaRenderStyle(MediaRenderStyle mediaRenderStyle) {
		networkStyle = (NetworkMediaLayerStyle) mediaRenderStyle;
	}
}
