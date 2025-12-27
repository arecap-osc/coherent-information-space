package org.hkrdi.eden.ggm.vaadin.console.mvp;

import java.util.EventObject;

import org.hkrdi.eden.ggm.vaadin.console.service.repository.CrudRepositoryService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.StatusChangeEvent;



public interface FlowEntityPresenter<T, ID> extends FlowPresenter<T, ID> {

	@Override
	CrudRepositoryService<T, ID> getService();

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

	//service methods are already transactional -> remove Transactional
//	@Transactional
	default void save() {
		beforeSave();

		if (!getBinder().isValid()) {
			getBinder().validate();
			return;
		}

		T localEntity = null;
		try {
			localEntity = (T)getService().save(getBinder().getBean());//getEntity());
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


//	{
//		try {
//			return (Class<T>) Class.forName (getClass().getTypeParameters()[0].getName());
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//		return (Class<T>) Object.class;
//	}

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
}
