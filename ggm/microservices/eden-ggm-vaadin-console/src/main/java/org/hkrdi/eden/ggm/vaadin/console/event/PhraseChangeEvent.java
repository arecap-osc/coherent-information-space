package org.hkrdi.eden.ggm.vaadin.console.event;

import java.util.EventObject;

public final class PhraseChangeEvent {

    public static class FromSemanticChange extends EventObject {

        /**
         * Constructs a prototypical Event.
         *
         * @param source The object on which the Event initially occurred.
         * @throws IllegalArgumentException if source is null.
         */
        public FromSemanticChange(String source) {
            super(source);
        }
    }

    public static class SyntaxChange extends EventObject {

        /**
         * Constructs a prototypical Event.
         *
         * @param source The object on which the Event initially occurred.
         * @throws IllegalArgumentException if source is null.
         */
        public SyntaxChange(String source) {
            super(source);
        }
    }

    public static class ToSemanticChange extends EventObject {

        /**
         * Constructs a prototypical Event.
         *
         * @param source The object on which the Event initially occurred.
         * @throws IllegalArgumentException if source is null.
         */
        public ToSemanticChange(String source) {
            super(source);
        }
    }



}
