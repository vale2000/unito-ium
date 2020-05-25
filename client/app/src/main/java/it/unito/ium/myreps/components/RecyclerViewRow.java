package it.unito.ium.myreps.components;

import java.io.Serializable;

public abstract class RecyclerViewRow implements Serializable {
    private static final long serialVersionUID = -4095012894177251775L;

    public int getType() {
        return 1;
    }

    public String getHeader() {
        return null;
    }
}
