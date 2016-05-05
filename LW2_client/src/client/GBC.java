package client;

import java.awt.GridBagConstraints;
import java.awt.Insets;

@SuppressWarnings("serial")
public class GBC extends GridBagConstraints {
    public GBC() {
        super();
    }
    public GBC(int gridx, int gridy) {
        this.gridx = gridx;
        this.gridy = gridy;
    }
    public GBC(int gridx, int gridy, int gridwidth, int gridheight) {
        this(gridx, gridy);
        this.gridwidth = gridwidth;
        this.gridheight = gridheight;
    }
    public GBC ipad(int ipx, int ipy) {
        this.ipadx = ipx;
        this.ipady = ipy;
        return this;
    }
    public GBC insets(int v) {
        return insets(v, v, v, v);
    }
    public GBC insets(int top, int left, int bottom, int right) {
        this.insets = new Insets(top, left, bottom, right);
        return this;
    }
    public GBC anchor(int a) {
        this.anchor = a;
        return this;
    }
    public GBC fill(int f) {
        this.fill = f;
        return this;
    }
    public GBC weight(double wx, double wy) {
        this.weightx = wx;
        this.weighty = wy;
        return this;
    }
}
