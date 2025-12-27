package org.hkrdi.eden.ggm.vaadin.console.common;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hkrdi.eden.ggm.repository.common.documentimage.DocumentImageJpaService;
import org.hkrdi.eden.ggm.repository.common.documentimage.entity.DocumentImage;
import org.hkrdi.eden.ggm.repository.common.documentimage.entity.DocumentImage.Source;
import org.hkrdi.eden.ggm.vaadin.console.microservice.common.component.ConfirmDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cop.support.BeanUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.FastByteArrayOutputStream;

import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.upload.Receiver;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;

public class AttachedDocGridComponent extends Div
		implements Receiver/* , SucceededListener */{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AttachedDocGridComponent.class);
	
    private DocumentImageJpaService service;
	
	private Upload uploadButton;
	
	private FastByteArrayOutputStream outputStream;
    private String fileName;
    
    private Grid<DocumentImage> grid;
	
    private Long applicationId;
    
    private List<Long> parentEntityIds;
    
    private String parentEntityClass;
    
    private HorizontalLayout actionButtonsBar;
    
    private boolean active = true;
    
	public AttachedDocGridComponent(Long applicationId, 
			List<Long> parentEntityIds, Class<?> parentEntityClass, HorizontalLayout actionButtonsBar) {
		this(applicationId, parentEntityIds, parentEntityClass.getSimpleName(), actionButtonsBar);
	}
	
	public AttachedDocGridComponent(Long applicationId, 
			List<Long> parentEntityIds, String parentEntityName, HorizontalLayout actionButtonsBar) {
		super();
		this.service = BeanUtil.getBean(DocumentImageJpaService.class);
		this.applicationId = applicationId;
		this.parentEntityIds = parentEntityIds;
		this.parentEntityClass = parentEntityName;
		this.actionButtonsBar = actionButtonsBar;
		init();
	}

	protected void init() {
		
		setWidthFull();
		
		//set design
		add(grid = generateGrid());
		
//		setDataProviderToGrid();
		
		//
//		this.setCaption(env.getProperty("gridcomponent.component.caption."+
//					this.getClass().getSimpleName()));
//		getAddButton().setCaption(env.getProperty("gridcomponent.addbutton.caption."+
//					this.getClass().getSimpleName()));
		//
		
		//add columns to grid
		addDeleteColumnToGrid();
		addColumnsToGrid();
		
		initAddButtonListener();
		
//		add(uploadButton);
		
		refreshGridData();
	}

	
	private Grid<DocumentImage> generateGrid() {
		Grid<DocumentImage> grid = new Grid<DocumentImage>();
		grid.setWidthFull();
		grid.getStyle().set("like-list",""); 
		grid.getStyle().set("center-icon", "");
		
		grid.setSelectionMode(SelectionMode.NONE);
		
//		grid.setRowHeight(53);
		
//		grid.setHeaderVisible(false);
		
		grid.setWidth("100%");
		grid.setWidthFull();
		
//		grid.setHeightMode(HeightMode.ROW);
		
		grid.setHeightByRows(true);
		
		return grid;
	}

	private Column<DocumentImage> deleteColumn;
	
	protected void addDeleteColumnToGrid() {
		deleteColumn = grid.addComponentColumn(this::buildDeleteButton).setWidth("57px").setFlexGrow(0);
	}
	

	public void setActive(boolean active) {
		this.active = active;
		grid.removeColumn(deleteColumn);
		if (active) {
			deleteColumn = grid.addComponentColumn(this::buildDeleteButton).setWidth("57px").setFlexGrow(0);
		}
	}
	
	protected Button buildDeleteButton(DocumentImage doc) {
        Button button = new Button(VaadinIcon.TRASH.create());
//        button.addStyleName("borderless grid-delete");
        
        button.setWidth("30");
        button.setHeight("51");
        button.addClickListener(e ->  {
		        	handleDelete(doc);
		        });
        return button;
    }

	protected void handleDelete(DocumentImage doc) {
		ConfirmDialog dialog = new ConfirmDialog("msg.attacheddoc.title",
				"msg.attacheddoc.question", "msg.attacheddoc.button.confirm", e->{
					doDelete(doc);
					refreshGridData();
				}, "msg.attacheddoc.button.cancel", e->{} 
		);
		dialog.open();
	}

	private void doDelete(DocumentImage doc) {
//		service.delete(applicationId, docId);
		service.deleteByApplicationIdAndParentIdInAndParentDocClassLikeIgnoreCaseAndName(applicationId, parentEntityIds, doc.getParentDocClass(), doc.getName());
		grid.getDataProvider().refreshAll();
	}
	
	public List<QuerySortOrder> getDefaultSortOrder() {
		return Arrays.asList(
				new QuerySortOrder("id", SortDirection.DESCENDING)
			);
	}

	
//	protected void setDataProviderToGrid() {
//		this.grid.setDataProvider(
//				new DocumentImageDataProviderOld<String>(applicationId, 
//						parentEntityId, 
//						parentEntityClass.getSimpleName(), 
//						service, getDefaultSortOrder()));
//	}

	private void refreshGridData() {
		grid.setItems(doLoadData());
		checkAndHideDocumentGridIfNoEntry();
	}
	
	private Stream<DocumentImage> doLoadData() {
		return service.findAnyMatching(applicationId, parentEntityIds, parentEntityClass).stream()
			.collect(Collectors.groupingBy(p -> p.getName())).values().stream().map(l -> (l.get(0)));
	}
	
	protected void addColumnsToGrid() {
		
		checkAndHideDocumentGridIfNoEntry();
		//add columns to grid
//		addIconTypeColumn();
		grid.addComponentColumn(this::buildNameButton).setFlexGrow(1);//.setExpandRatio(1);
	}

	protected boolean checkAndHideDocumentGridIfNoEntry () {
 	   if (service.countByApplicationIdAndParentIdAndEntityName(
 			   applicationId, parentEntityIds, parentEntityClass) == 0) {
 			grid.setVisible(false);
 			return true;
 		} 
 	   else {
 		  grid.setVisible(true);
 		   return false;
 	   }
}

	protected void initAddButtonListener() {
		// create an Upload component and set a Receiver and a SucceededListener
		uploadButton = new UploadWithSecurityContext(SecurityContextHolder.getContext());
		uploadButton.setId("uploadButton");
//		addImageDesktop.setButtonCaption("Ataseaza");
//		addImageDesktop.setButtonStyleName("v-button v-widget small v-button-small");
		Button button = new Button("", VaadinIcon.UPLOAD.create());
        button.getStyle().set("font-size", "10px");
        button.getStyle().set("height", "22px");
        uploadButton.setUploadButton(button);
		uploadButton.setVisible(true);
		uploadButton.setReceiver(this);
		uploadButton.addSucceededListener(this::uploadSucceeded);
		uploadButton.setDropAllowed(false);
		actionButtonsBar.getChildren().filter(c->c.getId().isPresent() && "uploadButton".equals(c.getId().get())).forEach(c->actionButtonsBar.remove(c));
		actionButtonsBar.add(uploadButton);
	}
	
	private void addIconTypeColumn() {
		grid.addComponentColumn(i -> getIconAccordingToFilenameTermination(i.getName()));

    }
		
	private Image getIconAccordingToFilenameTermination(String fileName) {
	
		String path = "frontend/img/attachdoc/";
		String filePath = "doc.png";
		String extension = "";

		int i = fileName.lastIndexOf('.');
		if (i > 0) {
		    extension = fileName.substring(i+1);
		    extension = extension.toLowerCase();
		}
		
		if (extension.equals("jpg") || extension.equals("jpeg") ||
			extension.equals("bmp") || extension.equals("png")) {
						filePath = "camera.png";
			};
			
		if (extension.equals("doc") || extension.equals("docx") ||
			extension.equals("txt") || extension.equals("xls")) {
							filePath = "doc.png";
			};
		
		if (extension.equals("pdf"))  {
								filePath = "pdf.png";
			};
			
			
		return new Image(path+filePath, filePath);
	}
		
	
	private HtmlContainer buildNameButton(DocumentImage documentImage) {
        NativeButton button = new NativeButton();
        button.setWidth("100%");
        button.setWidthFull();
  //      button.setSizeUndefined();
        button.getStyle().set("button-like-link","");
        
//        button.setCaptionAsHtml( true );
        button.getElement().setProperty("innerHTML", "<span style=\'font-size: 14px !important;"
        		+ "\'> " + documentImage.getName() + " <br> </span>" +
        		"<span style=\'font-size: 10px !important;\'> " + documentImage.getDateFormatted() + "</span> " );
        
        StreamResource myResource = getResource(documentImage, documentImage.getName());
                       
	    Anchor download = new Anchor(myResource, "");
	    download.getElement().setAttribute("target", "_blank");
//	    download.getElement().setAttribute("download", true);
	    download.add(button);
	    
        return download;
	}
	
	private StreamResource getResource(DocumentImage documentImage, String name) {
	    return new StreamResource(name, new InputStreamFactory() {
			
			@Override
			public InputStream createInputStream() {
				if (documentImage != null) {
	            	DocumentImage di = service.findById(applicationId, documentImage.getId());
	            	if (di.isImagedPresent()) {
	            		return new ByteArrayInputStream(di.getImageContent());
	            	}
	            }
	            return null;
			}
		});
	}
	
	//for UPLOAD
	@Override
	public OutputStream receiveUpload(String s, String s1) {
		fileName = s;
		return outputStream = new FastByteArrayOutputStream();
	}
	
	public void uploadSucceeded(SucceededEvent event) {
		byte[] contentInBytes = outputStream.toByteArray();
		service.save(new DocumentImage(
				applicationId,
				fileName, 
				LocalDateTime.now(), 
				contentInBytes, 
				applicationId,//???
				parentEntityIds.get(0),
				parentEntityClass,
				Source.ATTACHED
				));
		
//		grid.getDataProvider().refreshAll();
		refreshGridData();
		
//		if (grid.getDataProvider().size(null) > 0) {
//			getViewComponent().labelNoImageAttached.setVisible(false);
//		} else {
//			getViewComponent().labelNoImageAttached.setVisible(true);
//		}
		
		Notification.show("Document salvat!");
	}

	public boolean isActive() {
		return active;
	}

}
