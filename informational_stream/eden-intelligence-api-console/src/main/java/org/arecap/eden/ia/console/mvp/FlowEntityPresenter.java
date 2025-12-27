package org.arecap.eden.ia.console.mvp;

import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.StatusChangeEvent;
import org.arecap.eden.ia.console.service.RepositoryService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.EventObject;
import java.util.List;


public interface FlowEntityPresenter<V extends FlowView,T, ID extends Serializable,
		S extends RepositoryService<T, ID>> extends FlowPresenter<V> {

	S getService();

	/*
	 *
	 * use #getBinder.getBean for unbuffered mode
	 * Synchronize local entity for buffered mode
	 *
	 */
	default void setEntity(T entity) {
		getBinder().setBean(entity);
	}

	/*
	 *
	 * use #getBinder.getBean for unbuffered mode
	 * Synchronize local entity for buffered mode
	 *
	 */
	default T getEntity() {
		return getBinder().getBean();
	}

	@Transactional
	default void save() {
		beforeSave();

		if (!getBinder().isValid()) {
			getBinder().validate();
			return;
		}

		T localEntity = null;
		try {
			localEntity =  getService().save(getBinder().getBean());
		} catch (DataIntegrityViolationException ee) {
			throw new DataIntegrityViolationException(ee.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}

		setEntity(localEntity);

		afterSave();
	}

	/**
	 * used to change entity programmatically
	 */
	default void beforeSave() {}

	default void afterSave() {}

	@Transactional
	default void delete() {
		getService().delete(getEntity());
	}

	Class<T> getEntityType();

	@Override
	default void prepareModel(EventObject event) {
		//creates entity binder
		setBinder(new BeanValidationBinder<>(getEntityType()));

		//add binder form listeners
		getBinder().addStatusChangeListener(this::onFormStatusChange);

		// bind Form fields with entity fields
		try {
			getBinder().bindInstanceFields(getView());
		}catch (IllegalStateException e) {
			//nothing to bind!!!
//			e.printStackTrace();
			System.err.println(e.getMessage());
		}

		// get Entity from backend. Bind entity to binder
		loadEntity(getInitialEntityId());

		//unbuffered mode. The model is updated after every change if the field is valid
		//have to manually trigger binder,validate() to check for validation errors
		getBinder().setBean(getEntity());
	}

	ID getInitialEntityId();

	default void loadEntity(ID entityId) {

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
			try {
				c = getService().findById(entityId).orElse((T)getEntityType().newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				throw new UnsupportedOperationException(
						"Entity of type " + getEntityType().getName() + " is missing a public no-args constructor", e);
			}
		}
		if (c != null) {
			setEntity(c);
		}
	}

	default void fillEntityAfterCreate(T c) {}

	default void onFormStatusChange(StatusChangeEvent event) {}

	BeanValidationBinder<T> getBinder();

	void setBinder(BeanValidationBinder<T> binder);

	default List<T> getItems() {
		return getService().findAll();
	}
}
