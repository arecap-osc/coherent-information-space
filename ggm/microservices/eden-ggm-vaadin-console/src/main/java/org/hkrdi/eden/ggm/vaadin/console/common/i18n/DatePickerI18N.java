package org.hkrdi.eden.ggm.vaadin.console.common.i18n;

import java.text.DateFormatSymbols;
import java.time.DayOfWeek;
import java.time.temporal.WeekFields;
import java.util.Arrays;
import java.util.Locale;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;

public class DatePickerI18N extends DatePicker implements LocaleChangeObserver {

	private Locale locale = UI.getCurrent().getLocale();
	{
		init();
	}
	
	protected void init() {
		setI18n(initI18n());
		setLocale(locale);
	}
	
	protected DatePickerI18n initI18n(){
		DateFormatSymbols SYMBOLS=new DateFormatSymbols(locale);
		DatePickerI18n i18n=new DatePickerI18n();
		
		i18n.setCalendar(getTranslation("component.datepicker.calendar"));
		i18n.setCancel(getTranslation("component.datepicker.cancel"));
		i18n.setClear(getTranslation("component.datepicker.clear"));
		i18n.setToday(getTranslation("component.datepicker.today"));
		i18n.setWeek(getTranslation("component.datepicker.week"));
		
		DayOfWeek firstDayOfWeek = WeekFields.of(locale).getFirstDayOfWeek();
		i18n.setFirstDayOfWeek(firstDayOfWeek.getValue()==7?0:firstDayOfWeek.getValue());
		
		i18n.setMonthNames(Arrays.asList(SYMBOLS.getMonths()));
		i18n.setWeekdays(Arrays.asList(SYMBOLS.getWeekdays()).subList(1, 8));
		i18n.setWeekdaysShort(Arrays.asList(SYMBOLS.getShortWeekdays()).subList(1, 8));
		
		return i18n;
	}
	
    @Override
    public void localeChange(LocaleChangeEvent event) {
        locale = event.getLocale();
        init();
    }

}
