package org.hkrdi.eden.ggm.vaadin.console.common.i18n;

import java.util.Locale;

import com.vaadin.flow.component.upload.Receiver;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.UploadI18N;
import com.vaadin.flow.component.upload.UploadI18N.AddFiles;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;

public class UploadI18n extends Upload implements LocaleChangeObserver{
	private Locale locale = new Locale("ro", "RO");
	
	public UploadI18n() {
		super();
		init();
	}
	
	public UploadI18n(Receiver receiver) {
		super(receiver);
		init();
	}
	
	protected void init() {
		setI18n(initI18N());
	}

	protected UploadI18N initI18N(){
		UploadI18N i18n = new UploadI18N();
		i18n.setAddFiles(new AddFiles()
			.setMany(getTranslation("component.upload.addfiles.many"))
			.setOne(getTranslation("component.upload.addfiles.one"))
		);
		
		i18n.setCancel(getTranslation("component.upload.cancel"));
		
		i18n.setDropFiles(new UploadI18N.DropFiles()
			.setMany(getTranslation("component.upload.dropfiles.many"))
			.setOne(getTranslation("component.upload.dropfiles.one"))
		);
		
		i18n.setUploading(new UploadI18N.Uploading()
			.setStatus(new UploadI18N.Uploading.Status()
				.setConnecting(getTranslation("component.upload.uploading.status.connecting"))
				.setHeld(getTranslation("component.upload.uploading.status.held"))
				.setProcessing(getTranslation("component.upload.uploading.status.processing"))
				.setStalled(getTranslation("component.upload.uploading.status.stalled"))
				)
			.setRemainingTime(new UploadI18N.Uploading.RemainingTime()
				.setPrefix(getTranslation("component.upload.uploading.remainingtime.prefix"))
				.setUnknown(getTranslation("component.upload.uploading.remainingtime.unknon"))			
				)
			.setError(new UploadI18N.Uploading.Error()
				.setForbidden(getTranslation("component.upload.uploading.error.forbidden"))
				.setServerUnavailable(getTranslation("component.upload.uploading.error.serverunavailable"))
				.setUnexpectedServerError(getTranslation("component.upload.uploading.error.unexpectedservererror"))
				)
		);
		
		i18n.setError(new UploadI18N.Error()
				.setFileIsTooBig(getTranslation("component.upload.error.fileistoobig"))
				.setIncorrectFileType(getTranslation("component.upload.error.incorrectfiletype"))
				.setTooManyFiles(getTranslation("component.upload.error.toomanyfiles"))
		);
		
		return i18n;
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		locale = event.getLocale();
		init();
	}
	
}
