package org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.ui.genericmvp;

import org.hkrdi.eden.ggm.vaadin.firstdegreefeedback.entity.IGgmEntity;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.StatusChangeEvent;
import com.vaadin.flow.router.BeforeEnterEvent;


public interface IGgmEntityPresenter<T extends IGgmEntity> extends IGgmPresenter<T> {

	public void setEntity(T entity);
	
	public T getEntity();
	
	@Transactional
	public default void save() {
		getBinder().validate();

		if (!getBinder().isValid()) {
			return;
		}
		
		T entity = null;
		try {
			entity = (T)getService().save(getEntity());
		} catch (DataIntegrityViolationException ee) {
			throw new DataIntegrityViolationException(ee.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setEntity(entity);
	}
	
	@Transactional
	public default void delete() {
		getService().delete(getEntity().getId());
	}
	
	public Class<T> getEntityType();
	
	@Override
	public default void prepareModel(BeforeEnterEvent event) {
		//creates entity binder
		setBinder(new BeanValidationBinder<>(getEntityType()));

		//add binder form listeners
		getBinder().addStatusChangeListener(this::onFormStatusChange);

		// bind Form fields with entity fields
		getBinder().bindInstanceFields(getView());

		// get Entity from backend. Bind entity to binder
		loadEntity(getInitialEntityId());

		//unbuffered mode. The model is updated after every change if the field is valid
		//have to manually trigger binder,validate() to check for validation errors
		getBinder().setBean(getEntity());
	}
	
	public Long getInitialEntityId();

	public default void loadEntity(Long entityId) {

		T c = null;
		if (entityId == null) {
			try {
				c = (T)getEntityType().newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new UnsupportedOperationException(
						"Entity of type " + getEntityType().getName() + " is missing a public no-args constructor", e);
			}
			// try to populate fields with parent fields if necessary
			fillEntityAfterCreate(c);
		} else {
			c = (T)getService().load(entityId);
		}
		if (c != null) {
			setEntity(c);
		}
	}
	
	public default void fillEntityAfterCreate(IGgmEntity c) {}

	public default void onFormStatusChange(StatusChangeEvent event) {}
	
	public BeanValidationBinder<T> getBinder();
	
	public void setBinder(BeanValidationBinder<T> binder);
}
