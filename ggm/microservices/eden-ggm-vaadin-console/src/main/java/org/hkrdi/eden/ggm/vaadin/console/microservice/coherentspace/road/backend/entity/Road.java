package org.hkrdi.eden.ggm.vaadin.console.microservice.coherentspace.road.backend.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

@Entity
@Table(name = "road")
@Audited
public class Road implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "road_id_seq")
    @SequenceGenerator(name = "road_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;
	
	private String network;
	private String name;
	
	@Column(columnDefinition = "text")
	private String road;
	
	private String groupName;
	private Integer orderPosition;
	@Column(columnDefinition = "text")
	private String excludeClusters;
	
	private String searchSyntax;
	
	@Column(columnDefinition = "text")
	private String fractolon;
	
	@Transient
	private Long fromNode = null;
	
	@Transient
	private Long toNode = null;
	
	@Transient
	private List<Long> includeNodes = new ArrayList<>();
	
	
	public Road() {
	}
	
	public Road(String network, String name, String road) {
		super();
		this.network = network;
		this.name = name;
		this.road = road;
	}
	
	public Road(Long id, String network, String name, String road, String groupName, Integer orderPosition, Long fromNode, Long toNode, List<Long> includeNodes,
			String excludeClusters, String searchSyntax) {
		super();
		this.id = id;
		this.network = network;
		this.name = name;
		this.road = road;
		this.groupName = groupName;
		this.orderPosition = orderPosition;
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.includeNodes = includeNodes;
		this.excludeClusters = excludeClusters;
		this.searchSyntax = searchSyntax;
	}
	
	public Road duplicate() {
		return new Road(this.id, this.network, this.name, this.road, this.groupName, this.orderPosition, this.fromNode, this.toNode, this.includeNodes, 
				this.excludeClusters, this.searchSyntax);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRoad() {
		return road;
	}
	
//	public List<Long> getRoadAsList() {
//		if (getRoad() == null || "".equals(getRoad())) {
//			return new ArrayList<>();
//		}
//		
//		//extract road name
//		return road;
//	}

	public void setRoad(String road) {
		this.road = road;
		
//		if (road !=null && !"".equals(road.trim())){
//			String[] nodes = road.split("\\s*,\\s*");
//			if (nodes.length>0) {
//				this.fromNode = Long.valueOf(nodes[0]);
//			}
//			if (nodes.length>1) {
//				this.toNode = Long.valueOf(nodes[nodes.length-1]);
//			}
//			if (nodes.length>2) {
//				this.includeNodes = Stream.of(nodes).limit(nodes.length-1).skip(1).map(Long::valueOf).collect(Collectors.toList());
//			}
////			this.manualNodes = Stream.of(nodes).map(Long::valueOf).collect(Collectors.toList());
//		}else {
//			this.fromNode = null;
//			this.toNode = null;
//			this.includeNodes = null;
//		}
	}

	public Long getFromNode() {
		return fromNode;
	}

	public void setFromNode(Long fromNode) {
		this.fromNode = fromNode;
	}

	public Long getToNode() {
		return toNode;
	}

	public void setToNode(Long toNode) {
		this.toNode = toNode;
	}

	public List<Long> getIncludeNodes() {
		return includeNodes;
	}

	public void setIncludeNodes(List<Long> includeNodes) {
		this.includeNodes = includeNodes;
	}

	public String getExcludeClusters() {
		return excludeClusters;
	}

	public void setExcludeClusters(String excludeClusters) {
		this.excludeClusters = excludeClusters;
	}

	public String getSearchSyntax() {
		return searchSyntax;
	}

	public void setSearchSyntax(String searchSyntax) {
		this.searchSyntax = searchSyntax;
	}
	
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Integer getOrderPosition() {
		return orderPosition;
	}

	public void setOrderPosition(Integer orderPosition) {
		this.orderPosition = orderPosition;
	}
	
	public String getFractolon() {
		return fractolon;
	}

	public void setFractolon(String fractolon) {
		this.fractolon = fractolon;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((excludeClusters == null) ? 0 : excludeClusters.hashCode());
		result = prime * result + ((fractolon == null) ? 0 : fractolon.hashCode());
		result = prime * result + ((fromNode == null) ? 0 : fromNode.hashCode());
		result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
		result = prime * result + ((includeNodes == null) ? 0 : includeNodes.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((network == null) ? 0 : network.hashCode());
		result = prime * result + ((orderPosition == null) ? 0 : orderPosition.hashCode());
		result = prime * result + ((road == null) ? 0 : road.hashCode());
		result = prime * result + ((searchSyntax == null) ? 0 : searchSyntax.hashCode());
		result = prime * result + ((toNode == null) ? 0 : toNode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Road other = (Road) obj;
		if (excludeClusters == null) {
			if (other.excludeClusters != null)
				return false;
		} else if (!excludeClusters.equals(other.excludeClusters))
			return false;
		if (fractolon == null) {
			if (other.fractolon != null)
				return false;
		} else if (!fractolon.equals(other.fractolon))
			return false;
		if (fromNode == null) {
			if (other.fromNode != null)
				return false;
		} else if (!fromNode.equals(other.fromNode))
			return false;
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		if (includeNodes == null) {
			if (other.includeNodes != null)
				return false;
		} else if (!includeNodes.equals(other.includeNodes))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (network == null) {
			if (other.network != null)
				return false;
		} else if (!network.equals(other.network))
			return false;
		if (orderPosition == null) {
			if (other.orderPosition != null)
				return false;
		} else if (!orderPosition.equals(other.orderPosition))
			return false;
		if (road == null) {
			if (other.road != null)
				return false;
		} else if (!road.equals(other.road))
			return false;
		if (searchSyntax == null) {
			if (other.searchSyntax != null)
				return false;
		} else if (!searchSyntax.equals(other.searchSyntax))
			return false;
		if (toNode == null) {
			if (other.toNode != null)
				return false;
		} else if (!toNode.equals(other.toNode))
			return false;
		return true;
	}
}
