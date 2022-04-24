package javaswingdev.formtransition;

import java.awt.Component;
import java.awt.Point;

public class FormAnimate {

    public Component getForm() {
        return form;
    }

    public void setForm(Component form) {
        this.form = form;
    }

    public Point getOriginalLocation() {
        return originalLocation;
    }

    public void setOriginalLocation(Point originalLocation) {
        this.originalLocation = originalLocation;
    }

    public FormAnimate(Component form, Point originalLocation) {
        this.form = form;
        this.originalLocation = originalLocation;
    }

    public FormAnimate() {
    }

    private Component form;
    private Point originalLocation;
}
