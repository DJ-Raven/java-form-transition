package javaswingdev.formtransition;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;

public class FormTransition extends JComponent {

    private Animator animator;
    private Component componentNew;
    private final List<FormAnimate> formAnimatesNew = new ArrayList<>();
    private final List<FormAnimate> formAnimatesOld = new ArrayList<>();
    private Transition newForm;
    private Transition oldForm;
    private boolean show;
    private Point animateStart = new Point(-500, -500);

    public FormTransition() {
        setLayout(new BorderLayout());
        init();
    }

    private void init() {
        animator = new Animator(800, new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                if (show) {
                    animate(fraction, newForm, formAnimatesNew);
                } else {
                    animate(1f - fraction, oldForm, formAnimatesOld);
                }
            }

            @Override
            public void end() {
                if (!show) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            removeAll();
                            add(componentNew);
                            repaint();
                            revalidate();
                            show = true;
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    transfer(newForm, formAnimatesNew);
                                    animator.start();
                                }
                            });
                        }
                    });
                } else {
                    transfer(newForm, formAnimatesOld);
                    newForm.done();
                }
            }
        });
        animator.setAcceleration(.5f);
        animator.setDeceleration(.5f);
        animator.setResolution(1);
    }

    private void animate(float animate, Transition form, List<FormAnimate> formAnimate) {
        form.setAlpha(animate);
        int locationX = animateStart.x;
        int locationY = animateStart.y;
        for (int i = 0; i < formAnimate.size(); i++) {
            FormAnimate f = formAnimate.get(i);
            float ani = ease(animate, i + 1);
            int x = f.getOriginalLocation().x + locationX;
            int y = f.getOriginalLocation().y + locationY;
            double fx = locationX * ani;
            double fy = locationY * ani;
            f.getForm().setLocation((int) (x - fx), (int) (y - fy));
        }
    }

    private float ease(float f, int t) {
        double v = 1 - Math.pow(1 - f, t);
        return (float) v;
    }

    public boolean showForm(Component component) {
        if (!animator.isRunning()) {
            show = getComponentCount() == 0;
            componentNew = component;
            Transition tran = (Transition) component;
            if (show) {
                add(component);
                repaint();
                revalidate();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        newForm = tran;
                        transfer(tran, formAnimatesNew);
                        animator.start();
                    }
                });
            } else {
                oldForm = newForm;
                newForm = tran;
                animator.start();
            }

            return true;
        }
        return false;
    }

    private void transfer(Transition tran, List<FormAnimate> list) {
        Component coms[] = tran.initTransition();
        list.clear();
        for (Component com : coms) {
            list.add(new FormAnimate(com, com.getLocation()));
        }
    }

    public Point getAnimateStart() {
        return animateStart;
    }

    public void setAnimateStart(Point animateStart) {
        this.animateStart = animateStart;
    }
}
