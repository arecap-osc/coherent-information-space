package org.hkrdi.eden.ggm.repository.entity;

import org.hkrdi.eden.ggm.algebraic.util.GeometryUtil;
import org.springframework.data.geo.Point;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "data_map")
public class DataMap implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "data_map_id_seq")
    @SequenceGenerator(name = "data_map_id_seq", allocationSize = 1)
    @Column(name = "data_map_id")
    private Long id;

    /**
     *
     * The space resolution is the dimension of small diagonal the initial sustainable cluster
     *
     *
     */
    private Long resolution;


    /**
     *
     * The providing of pair coordinates (x,y) should have correction for index in the deep calculation
     *
     */
    private Integer deepMax;

    private Long atX;

    private Long atY;

    private Long toX;

    private Long toY;

    private String network;

    private Long clusterIndex;

    private Long x;

    private Long y;

    private Long addressIndex;

    private Long toAddressIndex;

    private String clusterType;

    private String trivalentLogic;

    private String trivalentLogicType;

    private String toTrivalentLogic;

    private String toTrivalentLogicType;

    private Integer beltIndex;

    private Integer rowIndex;

    private Integer columnIndex;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getResolution() {
        return resolution;
    }

    public void setResolution(Long resolution) {
        this.resolution = resolution;
    }

    public Integer getDeepMax() {
        return deepMax;
    }

    public void setDeepMax(Integer deepMax) {
        this.deepMax = deepMax;
    }

    public Long getAtX() {
        return atX;
    }

    public void setAtX(Long atX) {
        this.atX = atX;
    }

    public Long getAtY() {
        return atY;
    }

    public void setAtY(Long atY) {
        this.atY = atY;
    }

    public Long getToX() {
        return toX;
    }

    public void setToX(Long toX) {
        this.toX = toX;
    }

    public Long getToY() {
        return toY;
    }

    public void setToY(Long toY) {
        this.toY = toY;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public Long getClusterIndex() {
        return clusterIndex;
    }

    public void setClusterIndex(Long clusterIndex) {
        this.clusterIndex = clusterIndex;
    }

    public Long getX() {
        return x;
    }

    public void setX(Long x) {
        this.x = x;
    }

    public Long getY() {
        return y;
    }

    public void setY(Long y) {
        this.y = y;
    }

    public Long getAddressIndex() {
        return addressIndex;
    }

    public void setAddressIndex(Long addressIndex) {
        this.addressIndex = addressIndex;
    }

    public Long getToAddressIndex() {
        return toAddressIndex;
    }

    public void setToAddressIndex(Long toAddressIndex) {
        this.toAddressIndex = toAddressIndex;
    }

    public String getClusterType() {
        return clusterType;
    }

    public void setClusterType(String clusterType) {
        this.clusterType = clusterType;
    }

    public String getTrivalentLogic() {
        return trivalentLogic;
    }

    public void setTrivalentLogic(String trivalentLogic) {
        this.trivalentLogic = trivalentLogic;
    }

    public String getTrivalentLogicType() {
        return trivalentLogicType;
    }

    public void setTrivalentLogicType(String trivalentLogicType) {
        this.trivalentLogicType = trivalentLogicType;
    }

    public String getToTrivalentLogic() {
        return toTrivalentLogic;
    }

    public void setToTrivalentLogic(String toTrivalentLogic) {
        this.toTrivalentLogic = toTrivalentLogic;
    }

    public String getToTrivalentLogicType() {
        return toTrivalentLogicType;
    }

    public void setToTrivalentLogicType(String toTrivalentLogicType) {
        this.toTrivalentLogicType = toTrivalentLogicType;
    }

    public Integer getBeltIndex() {
        return beltIndex;
    }

    public void setBeltIndex(Integer beltIndex) {
        this.beltIndex = beltIndex;
    }

    public Integer getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }

    public Integer getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(Integer columnIndex) {
        this.columnIndex = columnIndex;
    }
    
    @Transient
    public Point getAtAddressCoordinates() {
        return new Point(toDoubleAddress(getAtX()), toDoubleAddress(getAtY()));
    }

    @Transient
    public Point getToAddressCoordinates() {
        return new Point(toDoubleAddress(getToX()), toDoubleAddress(getToY()));
    }

    @Transient
    public Point getClusterCoordinates() {
        return new Point(toDoubleAddress(getX()), toDoubleAddress(getY()));
    }

    public static Double toDoubleAddress(Long address) {
        return GeometryUtil.round(new Double(address) / 10_000_000_000L, 2);
    }

    private static Long toLongAddress(double address) {
        return new Long(Math.round(address*10_000_000_000L));
    }

    public static Long toLongDoubleRoundAddress(double address) {
        return toLongAddress(toDoubleAddress(toLongAddress(address)));
    }

}
